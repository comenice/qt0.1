package com.zxb.qt.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxb.qt.exploit.common.boot.utils.JsonUtils;
import com.zxb.qt.exploit.common.layui.pojo.FlowPojo;
import com.zxb.qt.exploit.common.redis.RedisKeys;
import com.zxb.qt.exploit.common.redis.RedisUtils;
import com.zxb.qt.exploit.entity.SysRight;
import com.zxb.qt.exploit.entity.vo.BlogBasicTagVO;
import com.zxb.qt.service.IBlogBasicsService;
import com.zxb.qt.service.IBlogPropService;
import com.zxb.qt.service.ISysRightService;
import com.zxb.qt.service.asyn.IAsynService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@EnableAsync
@Controller
@RequestMapping( "home.html" )
public class HomeController {

    private static final String BLOG_PAGE_COUNT = "blog:page:pageCount";


    @Qualifier( value = "sysRightServiceImpl")
    @Autowired
    private ISysRightService iSysRightService;

    @Qualifier( value = "blogBasicsServiceImpl")
    @Autowired
    private IBlogBasicsService iBlogBasicsService;

    @Qualifier( value = "blogPropServiceImpl" )
    @Autowired
    private IBlogPropService iBlogPropService;

    @Qualifier( "IAsynServiceImpl" )
    @Autowired
    private IAsynService iAsynService;

    @Autowired
    private  RedisUtils redisUtils;



    /**
     * 博客缓存
     * 显示菜单
     *
     * @param model
     * @param limit
     * @param size
     * @return
     */
    @RequestMapping("")
    public String index(Model model , @RequestParam( defaultValue = "0") int limit ,@RequestParam( defaultValue = "100") int size){
        ZSetOperations zSet = redisUtils.opsZSet();
        //导航菜单
        List<SysRight> sysRights = iSysRightService.list();
        Page page = new Page( limit , size );

        if (  zSet.size( RedisKeys.DEFAULT_BLOG_PAGE_ZSET ) != 0 ){

        }else{
            //分页博客数据
            IPage<BlogBasicTagVO> iPage = iBlogBasicsService.page( page );
            Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
                //创建set容器  存入zset中
                for (  BlogBasicTagVO basicTagVO : iPage.getRecords()  ){
                    ZSetOperations.TypedTuple<Object> objectTypedTuple = new DefaultTypedTuple<Object>(JsonUtils.obj2String( basicTagVO ), basicTagVO.getBasic_id().doubleValue() );
                    tuples.add( objectTypedTuple );
                }
                String blogKey =  "blog:page:zset" ;
                //缓存博客
                zSet.add( blogKey , tuples );
//                System.out.println( "page getPages : " + iPage.getPages() );
//                System.out.println( "page getPages : " + iPage.getTotal() );
//                System.out.println( "page getCurrent : " + iPage.getCurrent() );
//                System.out.println( "page getSize : " + iPage.getSize() );
                //缓存博客总数
                page.setSize( 5 );
                redisUtils.set(  BLOG_PAGE_COUNT , String.valueOf( iPage.getPages() ) );
                // 1 因为博客浏览量.. 不会直接展示出来 所以没必要浪费获取这些资源的时间
                // 2 使用的是redis的zSet
                // 3 博客数据 一条博客 数据太多了 拿出来慢
                //异步去去获取每一篇文章的 浏览量 评论量..
                //缓存评论浏览量..
                iAsynService.queryBlogPropCache( page );
        }
        model.addAttribute( "sysRights"  , sysRights);
        return "home";
    }


    @RequestMapping("index1")
    public String index1(Model model , @RequestParam( defaultValue = "0") int limit ,@RequestParam( defaultValue = "100") int size){
        return "home";
    }



    /**
     * 博客分页查询
     * @param limit
     * @param size
     * @return
     */
    @ResponseBody
    @GetMapping( "/asyn" )
    public Object asyn(  int limit ,@RequestParam( required = false , defaultValue = "5") int size ){
        System.out.println( "limit : size " + limit  );
        // 0 , 5 = 0 , 4  v1 = va1 * va2
        // 1 , 5 = 5 , 9
        // 2 , 5 = 10 , 14
        //根据分页参数 获取redis zSet的区间索引  l 到 l1
        long l = 0 ;
        long l1  =  0;
        if ( limit > 0  ){
            int mlimit = limit-1 ;
            l = mlimit * size ;
            l1 = l + size - 1 ;
        }
        ZSetOperations zSet = redisUtils.opsZSet();
        Set<ZSetOperations.TypedTuple<Object>> sets = new HashSet<ZSetOperations.TypedTuple<Object>>();
        sets = zSet.reverseRange( RedisKeys.DEFAULT_BLOG_PAGE_ZSET  , l , l1 );
        if ( sets != null && sets.size() > 0 ){
            String pages = redisUtils.get( BLOG_PAGE_COUNT ).toString();
            return new FlowPojo(  sets , Long.valueOf( pages )  );
        }

        Page page = new Page( limit , size );
        page.setPages( limit );
        IPage<BlogBasicTagVO> blogBasicTagVOPage = iBlogBasicsService.page( page );
        //封装layui  flow POJO对象 返回该对象 layui 会对指定属性作出对应判断

        return new FlowPojo( blogBasicTagVOPage.getRecords()  , blogBasicTagVOPage.getPages() );
    }


}
