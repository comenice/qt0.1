package com.zxb.qt.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 *报错实现错误代码的处理
 *
 * @author caidingnu
 * @version 1.0.0
 * @create 2019-03-23 19:52
 */
@Controller
public class MyException implements ErrorController {


    /**
     * 异常的分别处理--------------------------------》》》》》》》》》》》》
     *
     * @param request
     * @return
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if ( statusCode == 404 ){
            return "ex/error/m404" ;
        }
        System.out.println( "statusCode :  " + statusCode  );
        return statusCode+"";
    }
    /**
     * 该方法和以上方法不能同时共存，因为@RequestMapping("/error")相同
     * 异常的统一处理----------------------------》》》》》》》》》》》》》》》》》》
     *是有的错误都到这个页面
     * @return
     */
    @Override
    @RequestMapping("/erro1r")
    public String getErrorPath() {
        return "index";
    }
}
