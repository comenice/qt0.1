
let libPath = "../../common/editor.md-master/lib/" ;
function openMdeditor( id , path  ){
    if ( path != null ){
        libPath = path
    }
        var testEditor;
        testEditor = editormd( id , {
            placeholder:'左边编写，右边预览',  //默认显示的文字，这里就不解释了
            width: "90%",
            height: 640,
            syncScrolling: "single",
            path: libPath,   //你的path路径（原资源文件中lib包在我们项目中所放的位置）
            // theme: "dark",//工具栏主题
            theme  : "3024-day",
            editorTheme: "3024-day",//编辑主题
            saveHTMLToTextarea: true,
            emoji: true,
            imageUpload    : true ,
            imageFormats   : ["jpg", "jpeg", "gif", "png", "bmp", "webp"] ,
            imageUploadURL : "/blog/saveEdImg" ,
            taskList: true,
            tocm: true,                  // Using [TOCM]
            tex: true,                   // 开启科学公式TeX语言支持，默认关闭
            flowChart: true,             // 开启流程图支持，默认关闭
            sequenceDiagram: true,       // 开启时序/序列图支持，默认关闭,
            htmlDecode : "style,script,iframe|on*",
            // toolbarIcons  : function() {
            //     return ["undo","redo","|","bold","italic","quote","uppercase","lowercase","|","h1","h2","h3","h4","|","list-ul","list-ol","hr","|","link","image","code","code-block","table","html-entities","|","watch","preview","fullscreen","clear","|","help","testIcon"]
            // },
            toolbarIconsClass : {
                testIcon : "far fa-save"  // 指定一个FontAawsome的图标类
            },
            toolbarIconTexts : {
                testIcon2 : "测试按钮"  // 如果没有图标，则可以这样直接插入内容，可以是字符串或HTML标签
            },


    // 自定义工具栏按钮的事件处理
    toolbarHandlers : {
        /**
         * @param {Object}      cm         CodeMirror对象
         * @param {Object}      icon       图标按钮jQuery元素对象
         * @param {Object}      cursor     CodeMirror的光标对象，可获取光标所在行和位置
         * @param {String}      selection  编辑器选中的文本
         */
        testIcon : function(cm, icon, cursor, selection) {
            //var cursor    = cm.getCursor();     //获取当前光标对象，同cursor参数
            //var selection = cm.getSelection();  //获取当前选中的文本，同selection参数
            var html = $("#my-editormd .markdown-body").html();
            var json = $("form").serialize();
        }}


        });
        return testEditor;
}