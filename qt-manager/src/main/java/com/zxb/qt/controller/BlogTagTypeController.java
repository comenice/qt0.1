package com.zxb.qt.controller;


import com.sun.corba.se.spi.ior.ObjectKey;
import com.zxb.qt.exploit.entity.BlogTag;
import com.zxb.qt.exploit.entity.BlogType;
import com.zxb.qt.service.IBlogTagService;
import com.zxb.qt.service.IBlogTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-21
 */
@Controller
@RequestMapping("/blogTag")
public class BlogTagTypeController {

    @Autowired
    @Qualifier( "blogTagServiceImpl" )
    private IBlogTagService iBlogTagService;

    @Autowired
    @Qualifier( "blogTypeServiceImpl" )
    private IBlogTypeService iBlogTypeService;

    @GetMapping( "/list" )
    public Object ListTagType(Model model){
        List<BlogTag> tagList = iBlogTagService.list();
        List<BlogType> typeList = iBlogTypeService.list();

        model.addAttribute( "typeList",typeList );
        model.addAttribute( "tagList",tagList );
        return model;
    }

}
