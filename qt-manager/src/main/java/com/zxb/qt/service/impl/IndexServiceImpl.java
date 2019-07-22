package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.exploit.entity.Index;
import com.zxb.qt.exploit.mapper.IndexMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.service.IIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-19
 */
@Service
public class IndexServiceImpl extends ServiceImpl<IndexMapper, Index> implements IIndexService {

    @Autowired
    private IndexMapper indexMapper;

    @Override
    public List<Index> list() {
        return indexMapper.selectList( new QueryWrapper<Index>());
    }

    public Index getOne(Wrapper<Index> queryWrapper) {
        return indexMapper.selectOne( queryWrapper );
    }
}
