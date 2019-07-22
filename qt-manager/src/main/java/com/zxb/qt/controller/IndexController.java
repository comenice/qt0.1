package com.zxb.qt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxb.qt.exploit.entity.Index;
import com.zxb.qt.service.IIndexService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping( "index.html" )
public class IndexController {

    @Autowired
    @Qualifier( "indexServiceImpl" )
    private IIndexService iIndexService;

    @RequiresRoles("admin")
    @RequestMapping( "" )
    public String index(Model model ,@RequestParam( name = "indexId" , defaultValue = "1")  Integer indexId){
        Index index = iIndexService.getOne( new QueryWrapper<Index>().eq( "ID" , indexId ));
        model.addAttribute( "index" , index );
        return "index";
    }
}


