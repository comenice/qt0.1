package com.zxb.qt.service.impl;

import com.zxb.qt.exploit.entity.BlogType;
import com.zxb.qt.exploit.mapper.BlogTypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.service.IBlogTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-22
 */
@Service
public class BlogTypeServiceImpl extends ServiceImpl<BlogTypeMapper, BlogType> implements IBlogTypeService {

    @Autowired
    private BlogTypeMapper blogTypeMapper;

    @Override
    public List<BlogType> list() {
        return blogTypeMapper.selectList( null );
    }
}
