package com.zxb.qt.service.impl;

import com.zxb.qt.exploit.entity.GalleryAlbum;
import com.zxb.qt.exploit.entity.GalleryBasics;
import com.zxb.qt.exploit.mapper.GalleryAlbumMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxb.qt.service.IGalleryAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-23
 */
@Service
public class GalleryAlbumServiceImpl extends ServiceImpl<GalleryAlbumMapper, GalleryAlbum> implements IGalleryAlbumService {

    @Autowired
    private GalleryAlbumMapper galleryAlbumMapper;

    @Override
    public GalleryAlbum getMaxOne() {
        return galleryAlbumMapper.getMaxOne();
    }
}
