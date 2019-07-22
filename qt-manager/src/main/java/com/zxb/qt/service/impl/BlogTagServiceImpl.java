package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.exploit.entity.BlogTag;
import com.zxb.qt.exploit.mapper.BlogTagMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.service.IBlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-21
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements IBlogTagService {

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Override
    public List<BlogTag> list() {
        return baseMapper.selectList( new QueryWrapper<BlogTag>());
    }
}
