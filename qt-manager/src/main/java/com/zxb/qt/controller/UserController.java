package com.zxb.qt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxb.qt.exploit.common.ajax.AjaxResponse;
import com.zxb.qt.exploit.common.boot.upfile.SFtpJSch;
import com.zxb.qt.exploit.common.boot.exception.BlogException;
import com.zxb.qt.exploit.common.boot.utils.IPUtils;
import com.zxb.qt.exploit.common.boot.utils.VcodeUtils;
import com.zxb.qt.exploit.common.email.MailService;
import com.zxb.qt.exploit.common.enums.UserStateEnum;
import com.zxb.qt.exploit.common.redis.RedisUtils;
import com.zxb.qt.exploit.common.shiro.CurrentUser;
import com.zxb.qt.exploit.common.shiro.PassWordHelper;
import com.zxb.qt.exploit.entity.User;
import com.zxb.qt.service.IBlogCommentService;
import com.zxb.qt.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
    @Qualifier( "blogCommentServiceImpl" )
    private IBlogCommentService iBlogCommentService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("/testAnnoUser")
    public void testAnnoUser(){
        iBlogCommentService.testAnnoUser( null );
    }

    //该请求路径一般是由shrio拦截后 请求到这里
    @RequestMapping("")
    @ResponseBody
    public AjaxResponse index(Map map  , HttpServletRequest req , HttpServletResponse response){
        // 由于前台会使用异步方式 所有使用模板引擎并不适合 我将返回json在由前台来决定 !
        //但单返回json那也不适合 哈哈哈哈
        // return "/login" ;
        map.put( "html" , "user/html" );
        System.out.println( req.getRequestURL()  );
        map.put( "url" , req.getRequestURL() ) ;
        return new AjaxResponse( "1" , "该模块需要登陆才能使用" , map );
    }

    //登陆界面 登陆页面不单独出现 配合layer弹层出现
    @RequestMapping("/html")
    public String indexHtml(){
        return "login" ;
    }

    //注册界面 登陆页面不单独出现 配合layer弹层出现
    @RequestMapping("/register")
    public String register(){
        return "register" ;
    }




    @ResponseBody
    @RequestMapping("/findUserList")
    public AjaxResponse findUserList(){
        List<User> list = iUserService.list();
        list.forEach( System.out::print );
        return new AjaxResponse( "1","请求成功啦" );
    }

    /**
     * 登陆认证
     * @param userName
     * @param passWord
     * @param me
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public AjaxResponse login( String userName , String passWord , @RequestParam( defaultValue = "0") Boolean me ){
        System.out.println( "userNaee : " + userName );
        System.out.println( "passWord : " + passWord );
        //shrio
        Subject currentUser = SecurityUtils.getSubject();
        //是否认证
        if ( !currentUser.isAuthenticated() ){
            UsernamePasswordToken token = new UsernamePasswordToken( userName ,passWord);
            token.setRememberMe( me );
            try {
                currentUser.login( token );
            } catch (UnknownAccountException ex){
                throw new BlogException( "-1" , ex.getMessage() );
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
        return new AjaxResponse( "0" ,"登陆成功" , (User) currentUser.getPrincipal() );
    }


    /**
     * 退出
     * @return
     */
    @GetMapping("/logout")
    @ResponseBody
    public AjaxResponse logout(){
        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxResponse( "-1" , "退出失败！" );
        }
        return new AjaxResponse( "0" , "退出成功！" );
    }



    /**
     * 查询账号是否存在
     * @param userName
     * @return
     */
    @ResponseBody
    @PostMapping("/isExist")
    public AjaxResponse isExist( String userName ){
        return new AjaxResponse( iUserService.userIsExist( userName ) );
    }

    /**
     * 获取图形验证码
     * @param userName
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendImgCode")
    public void sendImgCode( String userName , HttpServletResponse response , HttpServletRequest req ){

        //String uuid = UUID.randomUUID().toString();
        String ip = IPUtils.getIpAddr( req );


        //图片流发送
        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);

        //发送验证码 缓存key 时间1分钟
        VcodeUtils vcodeUtils = new VcodeUtils( ip , redisUtils );
        vcodeUtils.getRandcode( response );
    }

    /**
     * 验证 验证码是否正确
     * @param code
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping("/isCode")
    public AjaxResponse isCode( @RequestParam( value = "code"  ) String code , HttpServletRequest req ){
        String ip = IPUtils.getIpAddr( req );
        //验证码是否正确
        VcodeUtils vcode = new VcodeUtils( ip , code , redisUtils );
        return vcode.verifyCode() == true ?  new AjaxResponse( "0","验证码正确" ) :  new AjaxResponse( "-1","验证码输入有误" );

    }

    @ResponseBody
    @PostMapping("sendCode")
    public AjaxResponse getCode( String email ){
        String code = mailService.sendRegisterCode( email );
        return new AjaxResponse( "0","验证码已发送" , code );
    }


    @ResponseBody
    @RequestMapping("/isLogin")
    public AjaxResponse isLogin(){
        Subject currentUser = SecurityUtils.getSubject();
        //是否认证
        if ( !currentUser.isAuthenticated() ) {
            return new AjaxResponse( "1" , "true" );
        }
        return new AjaxResponse( "-1" , "false" );
    }

    /**
     * 注册用户
     * AccountLock true  1 锁定
     * AccountLock false 0 被锁定
     */
    @ResponseBody
    @RequestMapping("registerUser")
    public AjaxResponse register(
                                  MultipartFile file,
                                  User user ,
                                  HttpServletRequest  req ){
        //文件上传
        SFtpJSch jSch = SFtpJSch.getConnect();
        String fileName =  null;
        try {
            fileName = jSch.upload( file.getInputStream() , file.getOriginalFilename() );
        } catch (IOException e) {
            e.printStackTrace();
            new BlogException( "-1","文件上传失败" );
        }
        //保存文件到用户信息种 （这个路径只是uri，并没有资源全路径）
        // 通过 ip prot + nginx映射路径 + user.getUrl(); 来访问这个资源
        user.setUrl( fileName );
        user.setAccountLock( false );
        PassWordHelper wordHelper = new PassWordHelper();
        //密码加密
        wordHelper.encryptionMD5( user );
        Boolean b = iUserService.save( user );
        if ( b ){
            return new AjaxResponse( "0","注册成功"  );
        }
        return new AjaxResponse( "-1","注册失败" );
    }

    @PostMapping( "/current" )
    @ResponseBody
    public AjaxResponse currentUser (  ){
        Subject currentUser = SecurityUtils.getSubject();
        //是否认证
        if ( currentUser.isAuthenticated() ){
            return new AjaxResponse( (User) currentUser.getPrincipal() );
        }
        return new AjaxResponse( "-1" , "还没有 用户登陆" );
    }

    /**
     * 目前就只可以修改 用户名
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public AjaxResponse updateUserInfo( User user ){
        if (  iUserService.updateById( user ) ){
            return new AjaxResponse( "1" , "信息修改成功" );
        }
        return new AjaxResponse( "-1" , "信息修改失败" );
    }


    //用户信息
    @GetMapping( "/info" )
    public String userInfo ( HttpServletRequest req ,@RequestParam( required =  false ) String userId  ){
        System.out.println( "userId : " + userId );
        /**  用户ID 用户可以通过点击自己的头像 ， 和别人的头像 分别查看信息
         如果value 为null查询自己的， 否则为其他的  **/
        try {
            if ( CurrentUser.getUser() != null && CurrentUser.getUser().getId().equals( userId ) ) {
                req.setAttribute("userId", null);
            }else{
                req.setAttribute("userId", userId);
            }
        } catch (Exception e) {
            req.setAttribute("userId", null);
            e.printStackTrace();
        }
        return "user/userinfo";
    }

    @RequestMapping("myinfo")
    public String myInfo(){
        return "my/myinfo";
    }



    @RequiresRoles( "有没有访问这个接口的 角色？？" )
    @RequiresPermissions( "有没有访问这个接口的 权限？？" )
    //会去 realm 的授权方法 查看是否拥有以上 ！
    @RequestMapping("yyy")
    public void yyy(){
        System.out.println( "YYYYYYYYYYY" );
    }



    // 后台 *********************************************** //
    /**
     * getUserInfoPage
     * @param page
     * @param limit
     * @return
     */
    public AjaxResponse userPage(@RequestParam( "page" ) Integer page ,@RequestParam( "limit" ) Integer limit ) {
        Page pages = new Page( page , limit );
        return new AjaxResponse( "1" , "用户信息获取成功" , iUserService.page( pages ,new QueryWrapper<User>().eq( "ACCOUNT_LOCK" , UserStateEnum.NORMAL.getCode() )).getRecords() );
    }

    /**
     *
     * @param ids 用户ID 数组
     * @return
     */
    public AjaxResponse userDels(@RequestParam( "ids" ) String ids ){

        if ( iUserService.removeByIds(  Arrays.asList( ids.split( "," )) )){
            return new AjaxResponse( "1" , "删除成功" );
        }else{
            return new AjaxResponse( "-1" , "删除失败" );
        }
    }



}


