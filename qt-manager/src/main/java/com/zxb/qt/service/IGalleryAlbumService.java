package com.zxb.qt.service;

import com.zxb.qt.exploit.entity.GalleryAlbum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-23
 */
public interface IGalleryAlbumService extends IService<GalleryAlbum> {

    GalleryAlbum getMaxOne();

}
