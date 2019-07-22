package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.exploit.common.redis.RedisKeys;
import com.zxb.qt.exploit.common.redis.RedisUtils;
import com.zxb.qt.exploit.entity.vo.BlogBasicPropVO;
import com.zxb.qt.exploit.mapper.BlogPropMapper;
import com.zxb.qt.service.IBlogPropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
@Service
public class BlogPropServiceImpl extends ServiceImpl<BlogPropMapper, BlogBasicPropVO> implements IBlogPropService {

    @Autowired
    private BlogPropMapper blogPropMapper;

    @Autowired
    private  RedisUtils redisUtils;

    /**
     * 单条查询 博客属性
     * @param queryWrapper
     * @return
     */
    @Override
    public BlogBasicPropVO getOne(Wrapper<BlogBasicPropVO> queryWrapper) {
        return blogPropMapper.selectOne( queryWrapper );
    }

    /**
     * 分页查询 博客属性
     * @param page
     * @param queryWrapper
     * @return
     */
    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<BlogBasicPropVO> page, Wrapper<BlogBasicPropVO> queryWrapper) {
        return blogPropMapper.selectMapsPage(page, null);
    }
}
