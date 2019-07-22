package com.zxb.qt.service.asyn;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxb.qt.exploit.entity.BlogBasics;
import com.zxb.qt.exploit.entity.vo.BlogBasicPropVO;
import com.zxb.qt.exploit.entity.vo.BlogIndexVO;
import org.apache.solr.client.solrj.SolrClient;

import java.util.Map;

/**
 * 本项目所有 异步接口
 */
public interface IAsynService {

    /**
     *  大致分两种
     *    1 : 比如 新增博客 而此时博客的总页数 还在缓存中 为了数据一致 自己去改变它
     *    2 :比如  告诉他 新增了博客 由用户下次请求 去数据库中获取
      */


    /**
     * 分页缓存 blogProp
     * @param page
     * @return
     */
    public Map<Object, Object> queryBlogPropCache (Page page   );

    /**
     * Id 缓存单条 blogProp
     *  如果分页缓存销毁了 用户再次查看的时候 如果在分页缓存就没用了 无法正确命中
     *  用户需要查看的 得根据ID 查找
     * @param id
     * @return
     */
    public BlogBasicPropVO queryBlogPropCache (Integer id   );

    /**
     *
     * @param blogId 博客ID
     * @param props 博客需要改变的属性 0 浏览量 1 评论量 ...
     */
    public void updateBlogProp( Integer blogId , int... props );


    /**
     *  异步更新 solr 索引库数据
     */
    public void updateSolrDate(BlogBasics blogBasics ,  String tags );


}
