package com.zxb.qt.controller;


import com.zxb.qt.exploit.common.boot.exception.BlogException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rz")
public class AuthenticationController {

    @RequestMapping( "/menuHandle" )
    public String menuHandle( String right , String href  ){

         Subject currenUser = SecurityUtils.getSubject();
         if ( !currenUser.isAuthenticated() ){
             System.out.println( "来啦老弟" );
         //    throw  new BlogException( "-1" , "你目前没有权限哟 😊" );
         }
         if ( currenUser.isPermitted( right ) ){
         //    throw  new BlogException( "-1" , "你莫得权限 嗄嘎嘎" );
         }
        System.out.println( href );
        System.out.println( right );
         return "redirect:"+ href ;
    }

}
