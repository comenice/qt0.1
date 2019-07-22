package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.entity.BlogBasics;
import com.zxb.qt.exploit.entity.vo.BlogBasicDetailVO;
import com.zxb.qt.exploit.entity.vo.BlogBasicTagVO;
import com.zxb.qt.exploit.mapper.BlogBasicsMapper;
import com.zxb.qt.exploit.mapper.BlogBasicsTagVoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.service.IBlogBasicsService;
import com.zxb.qt.service.asyn.IAsynService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-21
 */

@EnableAsync
@Transactional( propagation = Propagation.REQUIRED , readOnly = true)
@Service
public class BlogBasicsServiceImpl extends ServiceImpl<BlogBasicsTagVoMapper, BlogBasicTagVO> implements IBlogBasicsService {

    @Autowired
    private BlogBasicsTagVoMapper blogBasicsTagVoMapper;

    @Autowired
    private BlogBasicsMapper blogBasicsMapper;

    @Qualifier ( "IAsynServiceImpl" )
    @Autowired
    private IAsynService iAsynService;

    @Override
    public BlogBasicDetailVO queryBlogdetail(Integer blogId) {
        return blogBasicsTagVoMapper.queryBlogBasicDetailById( blogId  );
    }

    @Override
    @Transactional
    /**
     *  新增一篇博客
     */
    public AjaxResponse saveBlog(BlogBasics blog, int[] tags) {
        //新增博客
        int i = blogBasicsMapper.insert( blog );
        if ( i > 0 ){
            int blogId = blog.getId() ;
            // 博客的标签 多对多
            int isi = blogBasicsTagVoMapper.insertBlogTag( blogId , tags );
            if ( isi > 0 ){
                //int [1,2,3] --> String [1,2,3]
                String vTags = Arrays.toString( tags );
                //更新solr索引
                iAsynService.updateSolrDate( blog , vTags.substring( 1 , vTags.length()-1 )  );
                return new AjaxResponse( "0" , "博客添加成功" );
            }
        }
        return new AjaxResponse( "-1" , "博客添加失败" );
    }

    @Override
    public IPage<BlogBasicTagVO> page(IPage<BlogBasicTagVO> page) {
        return blogBasicsTagVoMapper.queryBasicTag((Page) page);
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<BlogBasicTagVO> page, Wrapper<BlogBasicTagVO> queryWrapper) {
        return blogBasicsTagVoMapper.selectMapsPage(page, queryWrapper);
    }
}
