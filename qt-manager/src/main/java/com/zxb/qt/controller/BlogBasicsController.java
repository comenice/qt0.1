package com.zxb.qt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.common.boot.exception.BlogException;
import com.zxb.qt.exploit.common.boot.upfile.SFtpJSch;
import com.zxb.qt.exploit.common.boot.utils.JsonUtils;
import com.zxb.qt.exploit.common.editor.EdPojo;
import com.zxb.qt.exploit.common.redis.RedisKeys;
import com.zxb.qt.exploit.common.redis.RedisUtils;
import com.zxb.qt.exploit.common.solr.BlogSolrService;
import com.zxb.qt.exploit.entity.BlogBasics;
import com.zxb.qt.exploit.entity.BlogTag;
import com.zxb.qt.exploit.entity.BlogType;
import com.zxb.qt.exploit.entity.vo.BlogBasicPropVO;
import com.zxb.qt.exploit.entity.vo.BlogIndexVO;
import com.zxb.qt.service.IBlogBasicsService;
import com.zxb.qt.service.IBlogPropService;
import com.zxb.qt.service.IBlogTagService;
import com.zxb.qt.service.IBlogTypeService;
import com.zxb.qt.service.asyn.IAsynService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-21
 */
@Controller
@RequestMapping("/blog")
public class BlogBasicsController {

    @Autowired
    @Qualifier( "blogTagServiceImpl" )
    private IBlogTagService iBlogTagService;

    @Autowired
    @Qualifier( "blogTypeServiceImpl" )
    private IBlogTypeService iBlogTypeService;

    @Autowired
    @Qualifier( "blogBasicsServiceImpl" )
    private IBlogBasicsService iBlogBasicsService;

    @Qualifier( "blogPropServiceImpl" )
    @Autowired
    private IBlogPropService iBlogPropService;

    @Qualifier( "IAsynServiceImpl" )
    @Autowired
    private IAsynService iAsynService;

    @Autowired
    private BlogSolrService blogSolrService;

    @Autowired
    private RedisUtils redisUtils;

    // 博客分页显示页面
    @RequestMapping ( "/show" )
    public String blog(){
        return "blog" ;
    }


    @RequiresRoles( "admin")
    @RequestMapping( "" )
    public String index(){
        return "blog/mdeditor";
    }

    /**
     * 查询所有博客标签 和 博客 分类 并缓存
     * @return
     */
    @RequestMapping( "/typeTagList" )
    @ResponseBody
    public Object typeTagList(Model map  , HttpServletResponse response){
        List<BlogTag> blogTagList = null;
        List<BlogType> blogTypeList = null;
        if ( redisUtils.hasKey( RedisKeys.DEFAULT_BLOG_TAG_LIST  ) ){
            blogTagList = JsonUtils.string2Obj( redisUtils.get( RedisKeys.DEFAULT_BLOG_TAG_LIST ).toString() , List.class );
        }else{
            blogTagList =  iBlogTagService.list();
            redisUtils.set( RedisKeys.DEFAULT_BLOG_TAG_LIST , JsonUtils.obj2String( blogTagList ) );
        }

        if ( redisUtils.hasKey(  RedisKeys.DEFAULT_BLOG_TYPE_LIST  ) ){
            blogTypeList = JsonUtils.string2Obj( redisUtils.get( RedisKeys.DEFAULT_BLOG_TYPE_LIST ).toString() , List.class );
        }else{
            blogTypeList =  iBlogTypeService.list();
            redisUtils.set( RedisKeys.DEFAULT_BLOG_TYPE_LIST , JsonUtils.obj2String( blogTypeList ) );
        }
        map.addAttribute( "blogTagList" , blogTagList );
        map.addAttribute( "blogTypeList" , blogTypeList );
        return new AjaxResponse( map ) ;
    }

//    public Boolean isBlogUpdate ( @RequestParam( name = "id") Integer id , @RequestParam( name = "version" , required = true) Integer vesion ) {
//
//    }

    /**
     * 获取查询博客详情
     *      博客详情
     * @param model
     * @param id
     * @return
     */
    @GetMapping( "/blogDetail" )
    public String blogDetail(Model model,  @RequestParam( name = "blogId"  ) Integer blogId ){
        //修改点击量
        iAsynService.updateBlogProp( blogId , 0 );
        model.addAttribute( "data" , iBlogBasicsService.queryBlogdetail( blogId  ) );
        // iframe 详情页面
        // model.addAttribute( "page" , page );
        return "blog/detail";
    }

    /**
     * 保存博客
     * @param typeId
     * @param userId
     * @param title
     * @param content_show
     * @param content_edit
     * @param content_head
     * @param tags
     * @return
     */
    @RequestMapping("/saveBlog")
    @ResponseBody
    public AjaxResponse blogSave (
            @RequestParam( name = "typeId" , required = false  ) Integer typeId,
            @RequestParam( name = "userId" , defaultValue = "0" , required = false ) Integer userId,
            @RequestParam( name = "title" , required = false  ) String title,
            @RequestParam( name = "content_show" , required = false ) String content_show,
            @RequestParam( name = "content_edit" , required = false ) String content_edit,
            @RequestParam( name = "content_head" , required = false ) String content_head,
            @RequestParam( name = "tags" , required = false  ) String tags ){

        BlogBasics blogBasics = new BlogBasics();
        blogBasics.setTitle( title );
        blogBasics.setContent( content_show );
        System.out.println( "add : " + content_show );
        blogBasics.setEditorContent( content_edit );
        blogBasics.setHeadContent( content_head );
        blogBasics.setUserId( userId );
        blogBasics.setTypeId( typeId );
        blogBasics.setTop( 0 );
        blogBasics.setBrowseCount( 0 );
        blogBasics.setDotCount( 0 );
        blogBasics.setCommentCount( 0 );
        int[] tagArr =  Arrays.stream( tags.split(",") ).mapToInt( Integer::valueOf ).toArray();

        return iBlogBasicsService.saveBlog( blogBasics , tagArr );
    }


    /**
     * 获取博客属性  浏览量 评论
     * @param id
     * @return
     */
    @GetMapping( "/blogProp" )
    @ResponseBody
    public AjaxResponse blogProp( @RequestParam( name = "basic_id" , required = true ) Integer id ){

        BlogBasicPropVO propVO  = null ;
        // 这个Id 是否存在
        if ( redisUtils.hHasKey( RedisKeys.DEFAULT_BLOG_PROP_HASH , id  ) ){
            propVO  = JsonUtils.string2Obj( redisUtils.hget( RedisKeys.DEFAULT_BLOG_PROP_HASH , id )+"" , BlogBasicPropVO.class ) ;
        }else{
            propVO = iBlogPropService.getOne( new QueryWrapper<BlogBasicPropVO>().eq( "id" , id ));
            redisUtils.hset(  RedisKeys.DEFAULT_BLOG_PROP_HASH ,  id , propVO );
        }
        return new AjaxResponse( propVO );
    }

    /**
     * 修改 博客点击 浏览
     *  0 浏览量  1 评论量 ...
     * @param blogId
     * @param prop
     * @return
     */
    @GetMapping( "/updateBlogProp" )
    @ResponseBody
    public void updateBlogProp ( Integer blogId , int prop ){
        //不通过在前台调用本方法 不然前台许多按钮都得这样来一次 把该代码放到了查询博客详情
        // iAsynService.updateBlogProp( blogId , prop );
    }


    /**
     *  查询solr索引
     * @param serarch 查询关键字
     * @param title  目前没有使用
     * @param content 目前没有使用
     * @param limit 分页数
     * @param size  一页有多少条数据
     * @return  List<BlogIndexVO></> 只有博客ID 标题
     */
    @RequestMapping( "/serarchIndex" )
    @ResponseBody
    public AjaxResponse blogIndex(
             @RequestParam( name = "serarchKeyWord" , defaultValue = "*" ) String serarch ,
             @RequestParam( name = "limit" , defaultValue = "0" , required = false ) int limit,
             @RequestParam( name = "size"  , defaultValue = "6" , required = false ) int size,
             @RequestParam( name = "title" ,   required = false ) String title ,
             @RequestParam( name = "content" , required = false ) String content ){

        //高亮
        String heightPrefix = "<font style='color:#ee836f'>";
        String heightSuffix = "</font>";
        List<BlogIndexVO> blogIndexVOS = new ArrayList<>();
        try {
            blogIndexVOS = blogSolrService.searchQuestion( serarch , limit , size , heightPrefix ,heightSuffix );
        } catch (Exception e) {
            new BlogException( "-1"  , "没有任何东东 ! ! !" );
            e.printStackTrace();
        }

        //索引库存在的只有 博客ID 标题 和信息 需要得到标签 还有评论数量
        return  new AjaxResponse( blogIndexVOS );
    }

    /**
     * 保存 ed 编辑时上传的图片
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping( "/saveEdImg" )
    public EdPojo saveEdImg(  @RequestParam(value = "editormd-image-file", required = false)  MultipartFile file ) {
        try {
            //文件名 不使用  file.getOriginalFilename() 可能会出现一些特殊符号导致有一定问题`
            return SFtpJSch.getConnect().uploadBlogImg(file.getInputStream(), UUID.randomUUID()+"" );
        } catch (Exception e) {
            e.printStackTrace();
            return new EdPojo();
        }
    }



}
