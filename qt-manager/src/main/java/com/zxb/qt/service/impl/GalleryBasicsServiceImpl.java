package com.zxb.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.exploit.entity.GalleryBasics;
import com.zxb.qt.exploit.mapper.GalleryBasicsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.service.IGalleryBasicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-23
 */
@Service
public class GalleryBasicsServiceImpl extends ServiceImpl<GalleryBasicsMapper, GalleryBasics> implements IGalleryBasicsService {

    @Autowired
    private GalleryBasicsMapper galleryBasicsMapper;

    @Override
    public List<GalleryBasics> list(Wrapper<GalleryBasics> queryWrapper) {
        return galleryBasicsMapper.selectList( queryWrapper );
    }
}
