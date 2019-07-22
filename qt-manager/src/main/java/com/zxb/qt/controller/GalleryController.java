package com.zxb.qt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.exploit.entity.GalleryAlbum;
import com.zxb.qt.exploit.entity.GalleryBasics;
import com.zxb.qt.service.IGalleryAlbumService;
import com.zxb.qt.service.IGalleryBasicsService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/gallery")
public class GalleryController {


    @Autowired
    @Qualifier( "galleryAlbumServiceImpl" )
    private IGalleryAlbumService iGalleryAlbumService;

    @Autowired
    @Qualifier( "galleryBasicsServiceImpl" )
    private IGalleryBasicsService iGalleryBasicsService;


    @RequiresRoles( "love")
    @GetMapping("")
    public String index( Model model ){
        //获取 最新相册 推荐封面
        GalleryAlbum galleryAlbum = iGalleryAlbumService.getMaxOne();
        //最新相册对应的 图片
        QueryWrapper queryWrapper = new QueryWrapper<GalleryBasics>();
        queryWrapper.eq( "GALLERY_ID" , galleryAlbum.getId() );

        List<GalleryBasics> galleryBasicLists  = iGalleryBasicsService.list( queryWrapper );
        model.addAttribute( "galleryAlbum" , galleryAlbum );
        model.addAttribute( "galleryBasicLists" , galleryBasicLists );
        //map.forEach( (o, o2) -> System.out.println( map ) );
        return "/gallery/index";
    }

}
