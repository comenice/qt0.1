package com.zxb.qt.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fx")
public class FXController {

    @RequestMapping( "" )
    public String index(){
        return "fx/index";
    }

}
