package com.zxb.qt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.common.boot.exception.BlogException;
import com.zxb.qt.exploit.common.boot.utils.IPUtils;
import com.zxb.qt.exploit.common.layui.pojo.FlowPojo;
import com.zxb.qt.exploit.common.redis.RedisKeys;
import com.zxb.qt.exploit.common.shiro.CurrentUser;
import com.zxb.qt.exploit.entity.BlogComment;
import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.exploit.entity.vo.BlogBasicTagVO;
import com.zxb.qt.exploit.entity.vo.BlogCommentVO;
import com.zxb.qt.service.IBlogCommentService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-21
 */
@Controller
@RequestMapping ( "/comment" )
public class BlogCommentController {


    @ResponseBody
    @RequestMapping("test")
    private void is(){

    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
    @Qualifier( "blogCommentServiceImpl" )
    private IBlogCommentService iBlogCommentService;

    /**
     * 评论
     */
    @RequestMapping( "" )
    @ResponseBody
    public AjaxResponse comment(BlogComment comment){
        if ( iBlogCommentService.saveBlogComment( comment ) ){
          return  new AjaxResponse( "1" , "评论成功" );
        }
        return new AjaxResponse( "-1","评论失败" );
    }

    /**
     * 回复
     * @return
     */
    @RequestMapping( "/reply" )
    @ResponseBody
    public AjaxResponse replyComment(BlogComment comment){

        if ( iBlogCommentService.saveBlogReply( comment ) ){
            return  new AjaxResponse( "1" , "回复成功" );
        }
        return new AjaxResponse( "-1","回复失败" );
    }


    /**
     * 分页查询一个用户的 评论
     * @return
     */
    @RequestMapping("/commentBlogUserInfo")
    @ResponseBody
    public FlowPojo queryCommentCreateBlogUserInfo(
                                int limit ,
                                @RequestParam( required = false , defaultValue = "5") int size ,
                                @RequestParam( required = false  ) String uid
                                    ){
        System.out.println( "UUUUUUUUUUID : " + uid  );
        Page page = new Page( limit , size );
        iBlogCommentService.queryCommentCreateBlogUserInfo( page , uid );
        return new FlowPojo( page.getRecords()  , page.getPages() , page.getTotal() );
    }


    /**
     * 分页获取评论数据
     * @param limit
     * @param size
     * @return
     */
    @ResponseBody
    @GetMapping( "/asynComment" )
    public FlowPojo asyn(   int limit ,
                          @RequestParam( required = false , defaultValue = "5") int size ,
                          @RequestParam( required = true , name = "pid") String parentId ,
                          @RequestParam( required = false , defaultValue = "0" ) String type
                ){
        System.out.println( "limit : size " + limit  );

        Page page = new Page( limit , size );
        page.setPages( limit );

        iBlogCommentService.queryCommenByPage( page , parentId , type );
        //封装layui  flow POJO对象 返回该对象 layui 会对指定属性作出对应判断
        return new FlowPojo( page.getRecords()  , page.getPages() , page.getTotal() );
    }


    /**
     * 拉取一个评论下面的回复信息
     * 占未提供分页
     * @param id
     * @return
     */
    @RequestMapping("/queryReplyinfo")
    @ResponseBody
    public AjaxResponse queryReplyinfo(
                    @RequestParam( required = false , defaultValue = "40") int size ,
                    @RequestParam( required = false , defaultValue = "0")  int limit ,
                    String id   ){
        Page page = new Page( limit , size );

        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq( "PARENT_ID" , id );

        iBlogCommentService.queryCommenByPage( page , id , "1" );
        if ( page.getRecords() == null ){
            return new AjaxResponse( "-1" , "回复内容获取失败" );
        }
        return new AjaxResponse( "1" , "回复内容获取成功" , page.getRecords() );
    }


    @RequestMapping("/delCommentById")
    @ResponseBody
    //评论ID
    public AjaxResponse delCommentById ( BlogComment comment ){

        if ( iBlogCommentService.deleteByCommentId( comment ) ){
            return new AjaxResponse( "1" , "评论删除成功" );
        }
        return new AjaxResponse( "-1" , "评论删除失败" );
    }

    /**
     * 点赞
     * @param commentId
     * @return
     */
    @RequestMapping("/star")
    @ResponseBody
    public AjaxResponse starCommentById(String commentId , HttpServletRequest req){
       return iBlogCommentService.updateCommentReplyStarCount( commentId , IPUtils.getIpAddr( req ) );
    }

    @RequestMapping("replyIndex")
    public String replyIndex(){
        return "blog/reply";
    }


}
