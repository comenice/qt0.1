function homemenu( url ) {
    //可能存在打开页面未关闭时 而服务器已经重启了
    rState();

    if ( url == "/book" ){
        layer.msg( "敬请期待 " );
        return false;
    }
    if ( url == "/fx" ){
        $("#myiframe").prop( "src" , url );
        $(this).find("li").addClass("check").parent().siblings().children().removeClass("check");
        return false;
    }
    var res = isRole( url );
    if ( !res ){
        return false;
    }
    if ( url == ""  || url == null){
        location.href = window.location.href.split("?")[0];
    }
    // 获取 url 发送给服务器 是否被shrio拦截
    /// 没有被拦截 代表拥有权限 被拦截做出先应提示 ( 登陆 )
    if ( url == "/gallery" || url == "/blog" ){
        //相册不需要 在iframe中打开  全屏显示
        cookie.set( cookie.go , "url" );
    }else{
        //其他的在ifrmae 中打开
        cookie.set( cookie.go , "iframe" );
    }
    localStorage.setItem( "url" , url );
    if ( cookie.get( "loginstate" ) == 0  ){
        if ( url == "/gallery" || url == "/blog"  ){
            return true;
        }else{
            $("#myiframe").prop( "src" , url );
        }
        $(this).find("li").addClass("check").parent().siblings().children().removeClass("check");
        return false;
    }
    var i = isLogin( url );
    if ( !i ){
        $(this).find("li").addClass("check").parent().siblings().children().removeClass("check");
        return false;
    }
    return false;
}