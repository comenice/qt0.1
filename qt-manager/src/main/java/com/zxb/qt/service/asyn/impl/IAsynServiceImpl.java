package com.zxb.qt.service.asyn.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxb.qt.exploit.common.boot.utils.JsonUtils;
import com.zxb.qt.exploit.common.redis.RedisKeys;
import com.zxb.qt.exploit.common.redis.RedisUtils;
import com.zxb.qt.exploit.entity.BlogBasics;
import com.zxb.qt.exploit.entity.vo.BlogBasicPropVO;
import com.zxb.qt.exploit.entity.vo.BlogIndexVO;
import com.zxb.qt.exploit.mapper.BlogPropMapper;
import com.zxb.qt.service.asyn.IAsynService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Async
public class IAsynServiceImpl implements IAsynService {

    @Autowired
    private SolrClient client;

    @Autowired
    private BlogPropMapper blogPropMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    /**
     * 查询 博客的属性 点击量 并缓存
     * 可以不返回
     */
    public Map queryBlogPropCache(Page page) {
        IPage<BlogBasicPropVO> pages = null;
        // list -- map<Integer, BlogBasicPropVo>
        Map<Integer, Object> map = null;
        //不存在 缓存
        if (  !redisUtils.hasKey( RedisKeys.DEFAULT_BLOG_PROP_HASH ) ){
            pages =  blogPropMapper.queryBlogPropPage( page );
            map = pages.getRecords().stream().collect(Collectors.toMap( BlogBasicPropVO::getBasicId , a -> a,(k1, k2)->k1)   );
            redisUtils.hmset( RedisKeys.DEFAULT_BLOG_PROP_HASH , map );
            return  map;
        }else {
            return (Map) redisUtils.get( RedisKeys.DEFAULT_BLOG_PROP_HASH );
        }
    }

    /**
     * 用于单条数据未命中时 去数据库得到并缓存
     * @param id
     * @return
     */
    @Override
    public BlogBasicPropVO queryBlogPropCache(Integer id) {
        // 缓存中已经存在
        if ( redisUtils.hHasKey( RedisKeys.DEFAULT_BLOG_PROP_HASH , id ) ){
            return JsonUtils.string2Obj( redisUtils.hget( RedisKeys.DEFAULT_BLOG_PROP_HASH , id )+"" , BlogBasicPropVO.class );
        }
        //缓存中没有存在 重新存入缓存 ( 这时已经是最新数据了 )
        BlogBasicPropVO  blogBasicPropVO = blogPropMapper.selectOne( new QueryWrapper<BlogBasicPropVO>().eq( "id" , id ));
        if ( blogBasicPropVO != null  ){
            redisUtils.hset( RedisKeys.DEFAULT_BLOG_PROP_HASH , id , blogBasicPropVO );
        }
        return null;
    }

    /**
     * @param blogId 博客ID
     * @param props 博客需要改变的属性 0 浏览量 1 评论量 ...
     */
    @Override
    public void updateBlogProp( Integer blogId , int[] props ) {

        BlogBasicPropVO blogBasicPropVO = queryBlogPropCache( blogId );
        if ( blogBasicPropVO != null ){
            for ( int i = 0  ; i < props.length ; i++ ){
                if ( props[i] == 0 ){
                    synchronized ( this ){
                        blogBasicPropVO.setBrowseCount( blogBasicPropVO.getBrowseCount()+1 );

                    }}
                if(  props[i]  == 1 ){
                    synchronized ( this ){
                        blogBasicPropVO.setDotCount( blogBasicPropVO.getDotCount()+1 );
                    }}
            }
            synchronized (this){
                redisUtils.hset( RedisKeys.DEFAULT_BLOG_PROP_HASH , blogId , JsonUtils.obj2String( blogBasicPropVO ) );
                blogBasicPropVO.setCommentCount( null );
                blogPropMapper.updateById( blogBasicPropVO   );

            }
        }
    }


    //更新solr 索引库！
    @Override
    public void updateSolrDate(BlogBasics blog , String tags ) {
        //  "tags":"2,1,11,3",
        System.out.println( "tags的值是:  " + tags );

        SolrInputDocument solr = new SolrInputDocument();
        String[] key = new String[]{  blog.getTitle() , blog.getContent()  };
        solr.addField( "blog_id" , blog.getId() );
        solr.addField( "keywords" , key );
        solr.addField( "head_content" ,blog.getHeadContent() == null ? "我什么都没有" : blog.getHeadContent() );
        solr.addField( "title" , blog.getTitle() );
        solr.addField( "tags" , tags == null || tags == "" ? " " : tags );
        try {
            client.add("new_core" , solr );
            client.commit( "new_core" );
            System.out.println( "异步执行完毕！" );
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
