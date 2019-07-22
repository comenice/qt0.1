package com.zxb.qt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.entity.BlogComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.exploit.entity.vo.BlogCommentVO;
import org.apache.ibatis.annotations.Param;
import reactor.util.annotation.Nullable;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-21
 */
public interface IBlogCommentService extends IService<BlogComment> {

    /**
     * 查询评论 包括 评论的博客 创建着信息
     * @param id
     * @return
     */
    IPage<BlogCommentVO> queryCommentCreateBlogUserInfo( IPage<BlogCommentVO> page , String id );

    boolean saveBlogComment( BlogComment  comment );

    boolean saveBlogReply( BlogComment comment );

    boolean updateBlogCommentCount( String id );

    boolean updateCommentReplyCount( String pid );

    //点赞
    AjaxResponse updateCommentReplyStarCount( String commentId , String ip );

    boolean deleteByCommentId( BlogComment comment );

    /**
     *
     * @param page分页入参
     * @param parentId父节点信息
     * @param type查询类型
     * @return
     */
    IPage<BlogCommentVO> queryCommenByPage(IPage<BlogCommentVO> page,
                                           @Param("parentId") String parentId,
                                           @Param("type") String type);

    @com.zxb.qt.exploit.common.anno.CurrentUserAnno
    void testAnnoUser( User user );

}
