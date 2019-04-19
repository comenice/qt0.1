package com.zxb.qt.controller;


import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.common.boot.exception.BlogException;
import com.zxb.qt.exploit.common.email.MailService;
import com.zxb.qt.exploit.common.shiro.PassWordHelper;
import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author  --郑晓波-- 
 * @since 2019-04-07
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    @Qualifier( value = "userServiceImpl")
    private IUserService iUserService;

    @Autowired
    private MailService mailService;

    @ResponseBody
    @RequestMapping("/findUserList")
    public AjaxResponse findUserList(){
        List<User> list = iUserService.list();
        list.forEach( System.out::print );
        return new AjaxResponse( "1","请求成功啦" );
    }

    @PostMapping("/login")
    @ResponseBody
    public Object login( String userName , String passWord ){
        //shrio
        Subject currentUser = SecurityUtils.getSubject();
        //是否认证
        if ( !currentUser.isAuthenticated() ){
            UsernamePasswordToken token = new UsernamePasswordToken( userName ,passWord);
            token.setRememberMe( true );
            try {
                currentUser.login( token );
            } catch (IncorrectCredentialsException ex) {
                throw new BlogException( "-1" , " 账号或则密码错误 " );
            } catch (IllegalArgumentException ex){
                /**
                 *  数值异常 Odd number of characters.
                 *  传入不正常的密码明文
                 */
                throw new BlogException( "-1" , " 账号或则密码错误 " );
            }

        }
        return new AjaxResponse( "0" ,"testShrio" );
    }

    @ResponseBody
    @PostMapping("sendCode")
    public AjaxResponse getCode( String email ){
        String code = mailService.sendRegisterCode( email );
        return new AjaxResponse( "0","验证码已发送" , code );
    }

    @ResponseBody
    @PostMapping("registerUser")
    public AjaxResponse register( User user , String code ){
        LocalDateTime time = LocalDateTime.now();
        PassWordHelper wordHelper = new PassWordHelper();
        //验证码是否正确
//        if ( !code.equals( mailService.getCode() ) ){
//            return new AjaxResponse( "-1","验证码有误" );
//        }

        user.setCreateTime( time );
        //密码加密 赋予 salt 和 加密后的密码
        wordHelper.encryptionMD5( user );
        Boolean b = iUserService.save( user );
        if ( b ){
            return new AjaxResponse( "0","注册成功" , code );
        }
        return new AjaxResponse( "0","注册失败" , code );
    }

    public static void main(String[] args) {

        LocalDateTime time = LocalDateTime.now();

    }


    @RequiresRoles( "有没有访问这个接口的 角色？？" )
    @RequiresPermissions( "有没有访问这个接口的 权限？？" )
    //会去 realm 的授权方法 查看是否拥有以上 ！
    @RequestMapping("yyy")
    public void yyy(){
        System.out.println( "YYYYYYYYYYY" );
    }

}
