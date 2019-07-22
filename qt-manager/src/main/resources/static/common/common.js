var flow ;


//***************   日期  ******************//
(function($) {
    $.extend({
        myTime: {
            /**
             * 当前时间戳
             * @return <int>  unix时间戳(秒)
             */
            CurTime: function(){
                return Date.parse(new Date())/1000;
            },
            /**
             * 日期 转换为 Unix时间戳
             * @param <string> 2014-01-01 20:20:20 日期格式
             * @return <int>  unix时间戳(秒)
             */
            DateToUnix: function(string) {
                var f = string.split(' ', 2);
                var d = (f[0] ? f[0] : '').split('-', 3);
                var t = (f[1] ? f[1] : '').split(':', 3);
                return (new Date(
                    parseInt(d[0], 10) || null,
                    (parseInt(d[1], 10) || 1) - 1,
                    parseInt(d[2], 10) || null,
                    parseInt(t[0], 10) || null,
                    parseInt(t[1], 10) || null,
                    parseInt(t[2], 10) || null
                )).getTime() / 1000;
            },
            /**
             * 时间戳转换日期
             * @param <int> unixTime 待时间戳(秒)
             * @param <bool> isFull 返回完整时间(Y-m-d 或者 Y-m-d H:i:s)
             * @param <int> timeZone 时区
             */
            UnixToDate: function(unixTime, isFull, timeZone) {
                if (typeof (timeZone) == 'number')
                {
                    unixTime = parseInt(unixTime) + parseInt(timeZone) * 60 * 60;
                }
                var time = new Date(unixTime * 1000);
                var ymdhis = "";
                ymdhis += time.getUTCFullYear() + "-";
                ymdhis += ((time.getUTCMonth()+1) < 10 ? "0" + (time.getUTCMonth()+1) : (time.getUTCMonth()+1)) + "-";
                ymdhis += (time.getUTCDate() < 10 ? "0" + time.getUTCDate() : time.getUTCDate()) + " ";
                ymdhis += (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":";
                ymdhis += (time.getUTCMinutes() < 10 ? "0" + time.getUTCMinutes() : time.getUTCMinutes()) + ":";
                ymdhis += (time.getUTCSeconds() < 10 ? "0" + time.getUTCSeconds() : time.getUTCSeconds());
                if (isFull === true)
                {
                    ymdhis += (time.getHours() < 10 ? "0" + time.getHours() : time.getHours()) + ":";
                    ymdhis += (time.getUTCMinutes() < 10 ? "0" + time.getUTCMinutes() : time.getUTCMinutes()) + ":";
                    ymdhis += (time.getUTCSeconds() < 10 ? "0" + time.getUTCSeconds() : time.getUTCSeconds());
                }
                return ymdhis;
            }
        }
    });
})(jQuery);

// 拼接博客展示信息
function appendBlog( item ) {
    //博客标签
    tagList = item.tagList;
    var tag = "" ;
    if ( tagList == null || tagList.length == 0 ){
        tag = "<span class='label' style='background-color: #2c2827'>  我没有标签 😊   </span>" ;
    }else{
        for ( i = 0 ; i < tagList.length ; i++ ){
            tag += "<span  class='mytag " + tagList[i].color + "'> " + tagList[i].name + " </span> " ;
        }
    }

    //item 为blog对象 把item转成json字符串 用于（ 前提是该blog内容没用更新 详情查看）
    // var  json = item;
    // json = JSON.stringify( json);

    var id = item.basic_id;
    var date = item.createTime == null ? "神秘的日期" : item.createTime;
    var blog = "<div id=\"blog_content\" class=\"contents\" >\n" +
        "<div class=\"title\">\n" +
        "\t<h1 class=\"name\"  > " + item.title + " </h1>\n" +
        "\t<span class=\"date\" >    "+ date +"   </span>\n" +
        "</div>\n" +
        "<div class=\"article\">\n" +
        "\t<p> " + item.headContent + "</p>\n" +
        "\t<a href='#' class='more'>more>></a>\n" +
        "\t<div class=\"line\"></div>\n" +
        "</div>\n" +
        "<div class=\"foot\">\n" +
        "\t<span class=\"glyphicon glyphicon-tag\" style=\"color: #999;margin-right: 20px;vertical-align: middle;\"></span>\n" +
        "\t\t " + tag + " \n" +
        "\t <button data-id='"+item.basic_id+"' id='look' type=\"button\"  class=\"btn btn-primary btn-sm\" > <div style='display:none'><input type='hidden' data-id='"+ id +"'></div> 展开全文 >> </button> \n" +
        "</div>\n" +
        "</div>";
    return blog;
}



//打开新的窗口 并且附带值
function openPostWindow(url,data1){

    var tempForm = document.createElement("form");
    tempForm.id = "tempForm1";
    tempForm.method = "get";
    tempForm.action = url;
    tempForm.target="_blank"; //打开新页面
    var hideInput1 = document.createElement("input");
    hideInput1.type = "hidden";
    hideInput1.name="blogId"; //后台要接受这个参数来取值
    hideInput1.value = data1; //后台实际取到的值

    var hideInput2 = document.createElement("input");
    hideInput2.type = "hidden";
    hideInput2.name="page"; //后台要接受这个参数来取值
    hideInput2.value = "/blog/blogDetail"; //后台实际取到的值

    tempForm.appendChild(hideInput2);
    tempForm.appendChild(hideInput1);
    if(document.all){
        tempForm.attachEvent("onsubmit",function(){});        //IE
    }else{
        var subObj = tempForm.addEventListener("submit",function(){},false);    //firefox
    }
    document.body.appendChild(tempForm);
    if(document.all){
        tempForm.fireEvent("onsubmit");
    }else{
        tempForm.dispatchEvent(new Event("submit"));
    }
    tempForm.submit();
    document.body.removeChild(tempForm);
}


// *******************    客户端储存    ***********************//
// localStorage.setItem( "loginState" , "0" ); //用户登陆标识
// localStorage.setItem( "go" , "comment" );
var cookie =new Cookie();
function Cookie() {
    this.go = "go" ; //这是键 对应的值表示（login）layer弹层销毁后该执行的内容
    this.set = set;
    this.get = get;
    function set( key , val , date ) {
        if ( !date ){
            date = 7 ;
        }
        $.cookie( key , val , { expires: date , path: '/' } );
    }
    function get( key ) {
       return $.cookie( key );
    }
}


// *******************    权限判断  对应提示    ***********************//

//清除 用户登陆状态
function rState() {
    var flag = isLogin( null , true );
   // false 登陆 true 未登录
    if( flag ){
        $.removeCookie('loginstate',{ path: '/'});
      //  $.cookie('loginstate',  { expires: -1 });
    }
    return flag ;
}

//是否拥有 权限
function isRole( url , readOnly ) {
    $.removeCookie(cookie.go ,{ path: '/'});
    var is = true ;
    $.ajax({
        async: false ,
        url : url ,
        success : function ( res ) {
            //没有该权限
            if ( res.code == "403"  ){
                parent.layui.use( "layer" , function () {
                    parent.layer.msg( res.msg );
                });
                is = false;
                return false;
            }
            is = true;
        },error : function ( res ) {
            //后台报错500  目前报错原因是因为没用登陆 不能验证
            isLogin();
            is = false;
            return false;
        }
    }) ;
    return is;
}

//是否登陆
function isLogin(  url , readOnly ) {
    var is = true ;
        $.ajax({
            async: false ,
            url : "/user/isLogin" ,
            success : function ( res ) {
                //被shrio 拦截啦
                if   ( res.code>0 ) {
                    if ( !readOnly ) {
                        openLogin();
                    }
                    is = false ;
                }
            }
        }) ;
    if ( is == false && readOnly == true ){ //如果是只获取 用户是否登陆 直接返回是否登陆信息
        return true;
    }
    if ( readOnly ){ //如果是只获取 用户是否登陆 直接返回是否登陆信息
        return false;
    }
    return is ;
}



//当前页面 弹出登陆界面
//需要layui layer
function openLogin() {
    var t ;
    parent.layui.use( "layer" , function () {
        t = $(this);

        parent.layer.open({
            type: 2, // 样式
            closeBtn: false,
            content: "user/html",
            shift: 2, // 弹出特效
            area: ['310px', '380px'],
            shadeClose: true,
            title: false,
            anim: 2,
            resize: false,
            offset: 'auto' ,
            fixed : true ,
            scrollbar: false,
            zIndex:213,
            end: function(layero, index){
                loginEnd();
            },
            yes :function ( index ){ //按钮1  提交的回调事件
               loginEnd();
                return false;
            }
        });
        $(".layui-layer" , window.parent.document).css({"borderRadius":"15px","backgroundColor":"none"});
    });
    $(".layui-layer" , window.parent.document).css({"borderRadius":"15px","backgroundColor":"none"});
    return t;
}


function openUserInfo( obj ) {
    //没有登陆
    if ( !obj ){
        openLogin();
        return false
    }
    parent.layui.use( "layer" , function () {
        parent.layer.open({
            type: 2, // 样式
            closeBtn: false,
            content: "user/info",
            shift: 2, // 弹出特效
            area: ['850px', '520px'],
            shadeClose: true,
            title: false,
            anim: 2,
            resize: false,
            offset: 'auto' ,
            fixed : true ,
            scrollbar: false,
            zIndex:213,
            end: function(layero, index){
            },
            yes :function ( index ){ //按钮1  提交的回调事
            }
        });
        $(".layui-layer", window.parent.document).css({
            "border-radius":"30px",
            "box-shadow":"0 10px 10px #8e8e8e"
            ,});
    });
    $(".layui-layer", window.parent.document).css({
        "border-radius":"30px",
        "box-shadow":"0 10px 10px #8e8e8e"
        ,});
}

function openUserInfoById( id ){
    parent.layui.use( "layer" , function () {
        parent.layer.open({
            type: 2, // 样式
            closeBtn: false,
            content: "user/info?userId="+id,
            shift: 2, // 弹出特效
            area: ['850px', '520px'],
            shadeClose: true,
            title: false,
            anim: 2,
            resize: false,
            offset: 'auto' ,
            fixed : true ,
            scrollbar: false,
            zIndex:213,
            success : function(){
                return false;
            },
            end: function(layero, index){
            },
            yes :function ( index ){ //按钮1  提交的回调事
            }
        });
        $(".layui-layer", window.parent.document).css({
            "border-radius":"30px",
            "box-shadow":"0 10px 10px #8e8e8e"
            ,});
    });
    $(".layui-layer", window.parent.document).css({
        "border-radius":"30px",
        "box-shadow":"0 10px 10px #8e8e8e"
        ,});
    return false;
}

function openReply( passivityUserId , parentId ){

    parent.layui.use( "layer" , function () {
        parent.layer.open({
            title:false,
            type:2,
            content:"../comment/replyIndex",
            area: ['620px', '430px'],
            resize: false,
            offset: 'auto' ,
            shadeClose: true,
            fixed : true ,
            scrollbar: false,
            closeBtn: 0,
            success: function(layero, index){
              var src = layero.find('iframe').attr( "src" );
              //给到 回复需要的参数
                layero.find('iframe').attr( "comm-passivityUserId" ,  passivityUserId );
                layero.find('iframe').attr( "comm-parentId" , parentId );
           },
            yes :function (layero , index ){ //按钮1  提交的回调事件
                var isr = cookie.get( "isReply" );
                //回复成功
                if ( isr > 0 ){
                    $("#news").html("");
                    $("#commentCount").html( 1 + parseInt($("#commentCount").html()) );
                    loadComment( flow );
                }
                cookie.set( "isReply" , 0 );
            },
            end: function(){
               // alert( "回复" );
                var isr = cookie.get( "isReply" );
                //回复成功
                if ( isr > 0 ){
                    $("#news").html("");
                    $("#commentCount").html( 1 + parseInt($("#commentCount").html()) );
                    loadComment( flow );
                }
                cookie.set( "isReply" , 0 );
            }
        });
        $(".layui-layer", window.parent.document).css({
            "border-radius":"10px",
            "box-shadow":"0 10px 10px #8e8e8e"
            ,});
    });
    $(".layui-layer", window.parent.document).css({
        "border-radius":"10px",
        "box-shadow":"0 10px 10px #8e8e8e"
        ,});

}

//弹层销毁结束 判断login后 需要做的事情
function loginEnd(){
    if ( cookie.get( "loginstate" ) == 0 ){
        var go = cookie.get( "go" );
        switch( go )
        {
            case "comment": comment() ; break;
            case "url": menuUrl() ;break;
            case "iframe": menuIframe() ;break;
            default:location.reload();break;
        }
        cookie.set( "go" , null );
    }
}
//跳转页面
function menuUrl(){
    var url = localStorage.getItem( "url" );
    location.href =  url;
}
//主页iframe 打开
function menuIframe(){
    var url = localStorage.getItem( "url" );
    $("#myiframe", window.parent.document).prop( "src" , url );
}






// ****************** 表单验证 ****************** //
function verifyTrue( img ){
    img.attr( "display" , "black" );
    img.prop("src", "http://139.224.129.134/file/img/icon/default/true.svg");
}

function verifyFalse( img ) {
    img.attr( "display" , "black" );
    img.prop("src", "http://139.224.129.134/file/img/icon/default/false.svg");
}


// ****************** bootstrap警告提示 ****************** //
function alertFlase( type ,  msg ){
    var v = "<div style='width: 880px;height:72px;padding:15px;border: 1px solid transparent;border-radius:4px" +
        "' class=\"alert-warning\" role=\"alert\">" +
        " <h5><strong> " + type + " </strong></h5> <p style='position: absolute;top: 36px;left: 24px'>"+ msg +"</p>" +
        "</div>" ;
    return v;
}

function alertTrue( type ,  msg ){
    var v = "<div style='width: 880px;height:72px;padding:15px;border: 1px solid transparent;border-radius:4px" +
        "' class=\"alert-success\" role=\"alert\">" +
        " <h5><strong> " + type + " </strong></h5> <p style='position: absolute;top: 36px;left: 24px'>"+ msg +"</p>" +
        "</div>" ;
    return v;
}


/**
 * 屏幕中间 上方 弹出提示
 * @param v
 */
function layerTips( v ){
    parent.layer.open({
        type:1
        ,content: v //alertFlase( "提示！" , "用户名必须有中文的呀！" )
        ,area: ['880px',"72px"]
        ,shade: 0
        ,offset: "40px"
        ,shadeClose: true
        ,title: false
        ,time:2500
        ,anim: 2
        ,resize: false
        ,fixed : true
        ,scrollbar: true
        ,closeBtn: false
        ,zIndex: parent.layer.zIndex //重点1
        ,success: function(layero){
           // layer.setTop(layero); //重点2
        }
    });

}


//获取当前用户信息
function currentUser( name , tx   ) {
    $.ajax({
        url : "/user/current",
        type : "post",
        success : function ( res ) {
            if ( res.data != null ){
                $(name).html(  res.data.userName  );
                $(tx).attr( "src" , res.data.url );
            }
        },error : function ( res ) {
        }
    });
}
//退出登陆
function logout() {
    cookie.set( "loginstate" , null , -1 );
    localStorage.clear();
    sessionStorage.clear();
    $.ajax({
        url : "/user/logout",
        type : "get",
        success : function ( res ) {
            if ( res.code >= 0 ){
                layerTips( alertTrue( "提示" , res.msg ) );

            }else{
                layerTips( alertFlase( "提示" , res.msg ) );
            }
        }
    });
    setTimeout (pr,800);
}

function pr() {
    parent.location.reload();
}


// ********************** 评论 **********************//
function comment( json ) {
    if ( !json ){
        json = localStorage.getItem( "comment" );
        json = JSON.parse( json );
    }
    $.ajax({
        url : "/comment/",
        type : "post",
        data : json,
        async: false ,
        success : function ( res ) {
            //回复成功
            if( res.code >  0  ){
                layer.msg(res.msg, {
                });
                $("#commentCount").html( 1 + parseInt($("#commentCount").html()) );
              //insertComment( json );
            }else{
                layerTips( alertFlase( "提示" , res.msg ) )
            }
        },error ( res ){

        }
    }) ;
}

// 评论文章后 文本追加
// 追加评论
function  insertComment( json ){
    var username = $("#user_name", window.parent.document).html() ;
    var userurl  = $("#user_tx", window.parent.document).prop( "src" );
    var createTime = $.myTime.UnixToDate( $.myTime.CurTime() ) ;
    //拼接成评论信息

    var comment = " <div class=\"new\">\n" +
                  "     <div class=\"portrait\"><img src="+userurl+"></div>\n" +
                  "     <a href=\"#\" class=\"Nickname\"> " + username + " </a>\n" +
                //  "     <span class=\"city\">[<span id=\"city\">长沙</span>市网友]</span>\n" +
                  "     <span class=\"time\"> " + createTime + " </span>\n" +
                  "     <p class=\"speak\"> " + json.content + " </p>\n" +
                  "     <div class=\"Interaction\">\n" +
                  "         <a href=\"#\" id=\"reply\">回复</a>\n" +
                  "         <i class=\"glyphicon glyphicon-thumbs-up\" style=\"color: #999; margin-right: 20px;\"></i>\n" +
                  "         <i class=\"glyphicon glyphicon-thumbs-down\" style=\"color: #999; margin-right: 20px;\"></i>\n" +
                  "     </div>\n" +
                  " </div>" ;
    //sessionStorage.removeItem( "comment" );
    //清空 本地评论
    localStorage.removeItem( "comment" );
    var c = $("#commentCount").html();
    $("#commentCount").html( ++c );
   // $("#news", window.parent.document).prepend( comment );
    $("#news").prepend( comment );
}

//用于 layui flow 流加载
function insertComment2( json ) {
    // activeName
    // activeUserHeadUrl
    // activeUserId
    // blogTitle
    // content
    // createBlogUserId
    // createBlogUserName
    // createBlogUserUrl
    // createTime
    // deleted
    // parentId
    // passiveName
    // passiveUserHeadUrl
    // passiveUserId
    // replyCount
    // starCount
    // type
    // updateTime
    //var lookReply = "<a href='#' onclick='return false;'>没有回复</a>";
    if ( json.replyCount >= 0 ){
        lookReply = " <a href='#' data-id='"+ json.id +"' id=\"replys\">查看回复 ("+json.replyCount+")</a>" ;
    }

    var comment = " <div class=\"new\">\n" +
        "     <div class=\"portrait\"><img src="+ json.activeUserHeadUrl +"></div>\n" +
        "     <a href=\"#\" class=\"Nickname\" onclick=openUserInfoById('"+json.activeUserId+"')> " + json.activeName + " </a>\n" +
      //  "     <span class=\"city\">[<span id=\"city\">长沙</span>市网友]</span>\n" +
        "     <span class=\"time\"> " + json.createTime + " </span>\n" +
        "     <p class=\"speak\"> " + json.content + " </p>\n" +

        "    <div id=\"answers\" class='answers' >\n" +
        "    </div>\n" +
        "     <div class=\"Interaction\">\n" +
        "         <a comm-parentId="+json.id+" comm-passivityUserId="+json.activeUserId+" href='#' id=\"reply\">回复</a>\n" +
        "         <i id='star' class=\"glyphicon glyphicon-thumbs-up\" style=\"color: #999; margin-right: 20px;\">"+ json.starCount +"</i>\n" +
        // "         <i class=\"glyphicon glyphicon-thumbs-down\" style=\"color: #999; margin-right: 20px;\"></i>\n" +
        "           "+lookReply+" " +
        "    </div>\n" +
        " </div>" ;
    return comment ;
}

//拼接 评论下的回复
function appendReply( data ) {
    var info = "  <div  class=\"answer\">\n" +
        "            <div class=\"portrait\"><img src='"+data.activeUserHeadUrl+"' onclick=''></div>\n" +
        "            <a href=\"#\" class=\"Nickname\">"+data.activeName+"</a><span class=\"a\">回复</span>\n" +
        "            <div class=\"portrait\"><img src='"+data.passiveUserHeadUrl+"'></div>\n" +
        "            <a href=\"#\" class=\"Nickname\">"+data.passiveName+"</a>\n" +
        "            <span class=\"time\">"+data.createTime+"</span>\n" +
        "            <p class=\"speak\">"+data.content+"</p>\n" +
        "            <div class=\"Interaction\">\n" +
        "            <a comm-parentId="+data.parentId+" comm-passivityUserId="+data.activeUserId+" href='#' id='reply'>回复</a>" +
        "            <i  id='star'  class=\"glyphicon glyphicon-thumbs-up\">"+data.starCount+"</i>\n" +
        "            <span data-id='"+ data.id +"' ></span></div>\n" +
        "         </div>" ;
    return info;
}


function loadComment( f ){
    flow = f ;
    var pid = $("#container").attr( "data-pid" );
    flow.load({
        scrollElem:$("#myiframe" , window.parent.document)
        ,mb:900
        ,end:'评论已经到底了'
        ,elem: '#news' //指定列表容器
        ,isAuto:true
        ,done: function(page, next){ //到达临界点（默认滚动触发），触发下一页
            var lis = [];
            //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
            var tag = "";
            //page--;
            $.get('/comment/asynComment?limit=' + page+"&pid=" + pid , function(res){
                var data = res.data;
                for ( i = 0 ; i < data.length ; i++ ){
                    var comment = insertComment2( data[i] );
                    lis.push(  comment );
                }
                //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
                //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
                next(lis.join(''), page < res.pages);
            });
        }
    });
}