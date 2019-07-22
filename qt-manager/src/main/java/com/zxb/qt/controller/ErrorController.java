package com.zxb.qt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("myerror")
public class ErrorController {

    @RequestMapping("")
    public String m404(){
        return "404" ;
    }
}
