package com.zxb.qt.controller;


import com.zxb.qt.exploit.common.app.WxUtils;
import com.zxb.qt.exploit.common.boot.utils.HttpRequestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//@Controller
@RequestMapping( "/app" )
public class AppSecurityController {

    @Value("${appid}")
    private String appid;

    @Value("${callBack}")
    private String callBack;

    @Value("${scope}")
    private String scope;

    @Value("${appsecret}")
    private String appsecret;


    //公众平台
    @RequestMapping("/gz")
    public String indexGz(Model model) throws UnsupportedEncodingException {
        String oauthUrl = "  https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        String redirect_uri = URLEncoder.encode(callBack, "utf-8"); ;
        oauthUrl =  oauthUrl.replace("APPID",appid).replace("REDIRECT_URI",redirect_uri).replace("SCOPE",scope);
        model.addAttribute("name","zxb");
        model.addAttribute("oauthUrl",oauthUrl);
        return "wx";
    }

    @RequestMapping("/gz1")
    public String indexGz1(Model model) throws UnsupportedEncodingException {
        String oauthUrl = "  https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        String redirect_uri = URLEncoder.encode(callBack, "utf-8"); ;
        oauthUrl =  oauthUrl.replace("APPID",appid).replace("REDIRECT_URI",redirect_uri).replace("SCOPE",scope);
        model.addAttribute("name","zxb");
        model.addAttribute("oauthUrl",oauthUrl);
        return "wx1";
    }

    // 微信开放平台

    @RequestMapping("/")
    public String index(Model model) throws UnsupportedEncodingException {
        String oauthUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
       String redirect_uri = URLEncoder.encode(callBack, "utf-8"); ;
        oauthUrl =  oauthUrl.replace("APPID",appid).replace("REDIRECT_URI",redirect_uri).replace("SCOPE",scope);
        model.addAttribute("name","zxb");
        model.addAttribute("oauthUrl",oauthUrl);
        return "wx";
    }

    @RequestMapping("/1")
    public String index1(Model model) throws UnsupportedEncodingException {
        String redirect_uri = URLEncoder.encode(callBack, "utf-8"); ;
        model.addAttribute("name","zxb");
        model.addAttribute("appid",appid);
        model.addAttribute("scope",scope);
        model.addAttribute("redirect_uri",redirect_uri);
        return "wx1";
    }


    @RequestMapping("/callBack")
    public String callBack(String code,String state,Model model) throws Exception{

        //1.通过code获取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        url = url.replace("APPID",appid).replace("SECRET",appsecret).replace("CODE",code);
        String tokenInfoStr =  HttpRequestUtils.httpGet(url,null,null);

        JSONObject tokenInfoObject = new JSONObject(tokenInfoStr);

        //2.通过access_token和openid获取用户信息
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        userInfoUrl = userInfoUrl.replace("ACCESS_TOKEN",tokenInfoObject.getString("access_token")).replace("OPENID",tokenInfoObject.getString("openid"));
        String userInfoStr =  HttpRequestUtils.httpGet(userInfoUrl,null,null);

        model.addAttribute("tokenInfoObject",tokenInfoObject);
        model.addAttribute("userInfoObject",userInfoStr);

        return "result";
    }






















    @RequestMapping(value = "/wx", method = RequestMethod.GET)
    public void wxSecurity(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr) {
        try {
            if (WxUtils.checkSignature(signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                System.out.println( echostr );
                out.close();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
