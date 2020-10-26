// var path = "http://192.168.1.26:8081/";
var path = "";
window.onload = function() {

    jQuery("#nav").accordion({ //初始化accordion
        fillSpace:true,
        fit:true,
        border:false,
        animate:true,
    });
    $.post("/getMenu", { "parent": "0" }, //获取第一层目录
        function (data) {
            $.each(data, function (i, e) {//循环创建手风琴的项
                var id = e.id;
                $('#nav').accordion('add', {
                    title: e.name,
                    content: "<ul id='tree"+id+"' ></ul>",
                    selected: true,
                    iconCls:"icon-open"//e.Icon
                });
                $.parser.parse();
                /*$.post(path + "/wa/menu/getMenuTree?id="+id,  function(data) {//循环创建树的项
                    $("#tree" + id).tree({
                        data: data,
                        onClick : function(node){
                            if (node.attributes) {
                                Open(node.text, node.url);
                            }
                        }
                    });
                }, 'json');*/

                $.ajax({
                    type: "POST",
                    url: path + "/wa/menu/getMenuTree?id="+id,
                    dataType: "json",
                    xhrFields: {
                        withCredentials: true,
                    },
                    // beforeSend: function(xhr) {
                    //     xhr.withCredentials = true;
                    // },
                    // contentType: "application/x-www-form-urlencoded",
                    crossDomain: true,
                    success: function(data){
                        $("#tree" + id).tree({
                            data: data,
                            onClick : function(node){
                                if (node.attributes) {
                                    Open(node.text, node.url);
                                }
                            }
                        });
                    }
                });
            });
        }, "json");

    //在右边center区域打开菜单，新增tab
    function Open(text, url) {
        if ($("#tabs").tabs('exists', text)) {
            $('#tabs').tabs('select', text);
        } else {
            $('#tabs').tabs('add', {
                title : text,
                closable : true,
                content : '<iframe width="100%" height="100%" frameborder="0" src="'+url+'" style="width:100%;height:100%;margin:0px 0px;"></iframe>'
            });
        }
    }
    //监听右键事件，创建右键菜单
    $('#tabs').tabs({
        onContextMenu:function(e, title,index){
            e.preventDefault();
            if(index>0){
                $('#mm').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                }).data("tabTitle", title);
            }
        }
    });
    //右键菜单click
    $("#mm").menu({
        onClick : function (item) {
            closeTab(this, item.name);
        }
    });
    //删除Tabs
    function closeTab(menu, type){
        var allTabs = $("#tabs").tabs('tabs');
        var allTabtitle = [];
        $.each(allTabs,function(i,n){
            var opt=$(n).panel('options');
            if(opt.closable)
                allTabtitle.push(opt.title);
        });
        switch (type){
            case 1 :
                var curTabTitle = $(menu).data("tabTitle");
                $("#tabs").tabs("close", curTabTitle);
                return false;
                break;
            case 2 :
                for(var i=0;i<allTabtitle.length;i++){
                    $('#tabs').tabs('close', allTabtitle[i]);
                }
                break;
            case 3 :
                var curTabTitle = $(menu).data("tabTitle");
                for(var i=0;i<allTabtitle.length;i++){
                    if(curTabTitle==allTabtitle[i]){

                    }else{
                        $('#tabs').tabs('close', allTabtitle[i]);
                    }
                }
                break;
        }
    }
}