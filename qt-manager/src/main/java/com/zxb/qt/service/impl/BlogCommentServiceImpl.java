package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.common.anno.CurrentUserAnno;
import com.zxb.qt.exploit.common.boot.exception.BlogException;
import com.zxb.qt.exploit.common.redis.RedisUtils;
import com.zxb.qt.exploit.common.shiro.CurrentUser;
import com.zxb.qt.exploit.entity.BlogComment;
import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.exploit.entity.vo.BlogCommentVO;
import com.zxb.qt.exploit.mapper.BlogBasicsMapper;
import com.zxb.qt.exploit.mapper.BlogCommentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.service.IBlogCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.annotation.Nullable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-21
 */
@Transactional
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements IBlogCommentService {

    @Autowired
    private BlogCommentMapper blogCommentMapper;

    @Autowired
    private BlogBasicsMapper blogBasicsMapper;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    @CurrentUserAnno
    public void testAnnoUser( User user) {
        System.out.println( "成功了吗? 看结果: [ " + user + " ]" );
    }

    /**
     * 新增博客评论
     * parentId  父ID 顶级是博客ID
     * type 0 博客评论
     * passivityUserId 被回复的 顶级是博客文章发布者
     * createTime 回复时间
     * content 内容
     * ActiveUserId 主动回复ID  默认当前登陆者
     * @param comment
     * @return
     */
    @Override
    public boolean saveBlogComment(BlogComment comment ) {
        init( comment );
        comment.setType( "0" );
        comment.setActiveUserId( CurrentUser.getUser().getId() );

        if ( blogCommentMapper.insert( comment ) == 0 ){
            throw new BlogException( "-1" , "博客评论失败" );
        }
        //博客评论成功 新增博客评论数量 1
        if ( updateBlogCommentCount( comment.getParentId()+"" ) == false ){
            throw new BlogException( "-1" , "博客评论统计更新失败" );
        }
        return true;
    }

    /**
     * 新增博客回复
     * parentId  父ID 顶级是博客ID
     * type 1 博客回复
     * passivityUserId 被回复的 顶级是博客文章发布者
     * createTime 回复时间
     * content 内容
     * ActiveUserId 主动回复ID  默认当前登陆者
     * @param comment
     * @return
     */
    @Override
    public boolean saveBlogReply(BlogComment comment) {
        System.out.println( "saveBlogReply" );
        System.out.println( "comment : " + comment );
        init( comment );
        comment.setActiveUserId( CurrentUser.getUser().getId() );
        comment.setType( "1" );

        if ( blogCommentMapper.insert( comment ) == 0 ){
            throw new BlogException( "-1" , "博客回复失败" );
        }
        //博客评论成功 新增博客评论数量 1 sd
        if ( updateBlogCommentCountByReply( comment ) == false ){
            throw new BlogException( "-1" , "博客评论统计更新失败" );
        }
        if (  !updateCommentReplyCount( comment.getParentId()+"" ) ){

        }

        return true;
    }

    /**
     * 更新 博客评论(评论和回复都算)总数
     * @param id
     * @return
     */
    @Override
    public boolean updateBlogCommentCount(String id) {
        System.out.println( "updateBlogCommentCount" );
        return blogBasicsMapper.updateCommentCountByBlogId( id )> 0 ? true : false;
    }


    //修改 blog_basic评论数
    public boolean updateBlogCommentCountByReply(BlogComment comment) {
        // 这条记录 是回复记录 ID 也是回复内容的ID 要增加的是评论ID 的回复量
        // 也就是PARENT_ID
        BlogComment blogComment = blogCommentMapper.selectById( comment.getParentId() );
        return blogBasicsMapper.updateCommentCountByBlogId(     blogComment.getParentId()+""  ) > 0 ? true : false;
    }


    //修改blog_comment 下面的回复数量
    @Override
    public boolean updateCommentReplyCount( String pid ) {
        System.out.println( "updateCommentReplyCount" );
        return blogCommentMapper.updateCommentReplyCount( pid ) > 0 ? true : false;
    }

    //删除评论
    @Override
    public boolean deleteByCommentId(BlogComment comment) {
        if ( blogCommentMapper.deleteById( comment.getId() ) == 0  ){
            throw  new BlogException( "-1" , "评论删除失败" );
        }
        return  blogBasicsMapper.uupdateCommentCountByBlogIdCut1( comment.getParentId()+"" ) > 0;
    }

    //点赞
    @Override
    public AjaxResponse updateCommentReplyStarCount(String commentId , String ip) {

        String id = "";
        //如果用户已经登陆 存放用户Id  没有则存放IP地址
        id = CurrentUser.getUser() == null ? ip : CurrentUser.getUser().getId();
        String key = "blog:comment:star:"+commentId+":"+id ;
        if ( redisUtils.hasKey( key ) ){
            return  new AjaxResponse( "-1" , "你近期已经点过赞" );
        }else{
            Integer isSuccess = blogCommentMapper.updateCommentStarCount( commentId );
            if ( isSuccess > 0 ){
                redisUtils.set( key , isSuccess , 6000 );
                return new AjaxResponse( "1" , "点赞成功 +1" );
            }
        }
        return new AjaxResponse( "-1" , "评论点赞失败" );
    }

    @Override
    public IPage<BlogCommentVO> queryCommenByPage(IPage<BlogCommentVO> iPage, String parentId, String type) {
        System.out.println( "queryCommenByPage" );
        return iPage.setRecords( blogCommentMapper.queryComment( iPage ,  parentId , type ) );
    }

    @Override
    public IPage<BlogCommentVO> queryCommentCreateBlogUserInfo( IPage<BlogCommentVO> iPage , String id ) {
        if ( id == "" || id == null ){
            id =  CurrentUser.getUser().getId();
        }
        return iPage.setRecords( blogCommentMapper.queryCommnetByUserId( iPage , id )  ) ;
    }

    private void init( BlogComment comment ){
        comment.setStarCount( 0 );
        comment.setReplyCount( 0 );
    }

}