var path = "/wa/wagebase";
var name = "";
var gradeName = "";
$(function(){
    showTable();
});
//显示数据
function showTable(){
    var win = $(window).height();
    var height = win - 100;
    layui.use('table', function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/getWagesPostList' //数据接口
            // ,toolbar: true
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide: true}
                ,{field: 'name', title: '岗位名称', align: 'center', sort: true}
                ,{field: 'wagesPostWage', title: '岗位工资', align: 'center', sort: true, width: 150, style: 'color:#0c7cd5'}
                ,{field: 'gradeName', title: '职级等级', toolbar: '#barGradeName', align: 'center', sort: true}
                ,{fixed: '', title: '操作', toolbar: '#barDemo', align: 'center', width: 150}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            $("#selPostName").val(data.name);
            $("#selPostId").val(data.id);
            $("#updWages").val(data.wagesPostWage);
            name = data.name;
            if (obj.event == 'edit') {// 修改
                layui.use('layer', function () {
                    var layer = layui.layer;
                    layer.open({
                        type: 1
                        , id: 'updPostDiv' //防止重复弹出
                        , content: $(".updPostDiv")
                        , btnAlign: 'c' //按钮居中
                        , shade: 0.5 //不显示遮罩
                        , area: ['100%', '100%']
                        , success: function () {
                        }
                        , yes: function () {
                        }
                    });
                });
            } else if (obj.event == 'level') {
                $("#postLevelId").val(data.id);
                layui.use('layer', function () {
                    var layer = layui.layer;
                    layer.open({
                        type: 1
                        , id: 'postLevelBigDiv' //防止重复弹出
                        , content: $(".postLevelBigDiv")
                        , btnAlign: 'c' //按钮居中
                        , shade: 0.5 //不显示遮罩
                        , area: ['100%', '100%']
                        , success: function () {
                        }
                        , yes: function () {
                        }
                    });
                });
                showPostLevel();
            }
        });
    });
}
//打开添加岗位页面
function showAddPost () {
    $("#addInput").val("");
    $("#addRemark").val("");
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            type: 1
            , id: 'addPostDiv' //防止重复弹出
            , content: $(".addPostDiv")
            , btnAlign: 'c' //按钮居中
            , shade: 0.5 //不显示遮罩
            , area: ['100%', '100%']
            , success: function () {
            }
            , yes: function () {
            }
        });
    });
}
// 添加岗位
function addPost() {
    var wagesPost = {};
    wagesPost.name = $("#addInput").val();
    wagesPost.wagesPostWage = $("#addWages").val();
    if(wagesPost.name.trim() == "" || wagesPost.wagesPostWage.trim() == "") {
        layer.alert("请完善岗位信息！");
        return false;
    };
    $.ajax({
        type: "post",
        dataType: "json",
        url: path + "/addPost",
        data: JSON.stringify(wagesPost),
        contentType: "application/json; charset=utf-8",
        success: function(data){
            layer.closeAll();
            if (data == "SUCCESS") {
                showTable();
            } else if (data == "havaRecord") {
                layer.alert("该岗位已存在！");
            }
        }
    });
}
//修改岗位
function updPost() {
    var wagesPost = {};
    wagesPost.id = Number($("#selPostId").val());
    wagesPost.name = $("#selPostName").val();
    wagesPost.wagesPostWage = parseInt($("#updWages").val());
    if (name == wagesPost.name) {
        wagesPost.index = 0;
    } else {
        wagesPost.index = 1;
    }
    $.ajax({
        type: "post",
        url: path + "/updPost",
        data:JSON.stringify(wagesPost),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            layer.closeAll();
            if (data == "SUCCESS") {
                showTable();
            } else if (data == "havaRecord") {
                layer.alert("该岗位已存在！");
            }
        }
    });
}
//岗位等级
function showPostLevel () {
    $(".postLevelDiv").css("display","block");
    var win = $(window).height();
    var height = win - 150;
    layui.use('table', function() {
        var table = layui.table;
        table.render({
            elem: '#demoLevel'
            , height: height
            , url: path + '/getPostGradeList?wagesPostId='+$("#postLevelId").val() //数据接口
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide: true}
                ,{field: 'gradeName', title: '岗位等级', align: 'center', sort: true}
                ,{field: 'postGradeWage', title: '岗位工资', align: 'center', sort: true}
                ,{fixed: '', title: '操作', toolbar: '#barDemoLevel', align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(testLevel)', function (obj) {
            var data = obj.data;
            $("#levelId").val(data.id);
            $("#updPostName").text($("#selPostName").val());
            $("#updPostLevelName").val(data.gradeName);//等级名称
            gradeName = data.gradeName;
            $("#updWagesName").val(data.postGradeWage);//岗位等级工资
            if (obj.event == "editLevel") {
                $(".updLevelDiv").css("display", "block");
                $(".postLevelDiv").css("display", "none");
                $(".addLevelDiv").css("display", "none");
            }
        });
    });
}
//打开添加等级页面
function showAddLevel () {
    $(".postLevelDiv").css("display", "none");
    $(".updLevelDiv").css("display", "none");
    $(".addLevelDiv").css("display", "block");
    $("#addPostName").text($("#selPostName").val());
    $("#addPostLevelName").val("");
    $("#addWagesName").val("");
}
//添加等级
function addPostLevel () {
    var postGrade = {};
    postGrade.wagesPostId = Number($("#selPostId").val());//岗位Id
    postGrade.gradeName = $("#addPostLevelName").val();//等级名称
    postGrade.postGradeWage = $("#addWagesName").val();//岗位等级工资
    if(postGrade.gradeName.trim() == "" ||  postGrade.postGradeWage.trim() == "") {
        layer.alert("请完善等级信息！");
        return false;
    };
    $.ajax({
        type: "post",
        url: path + "/addPostGrade",
        data: JSON.stringify(postGrade),
        contentType: "application/json; charset=utf-8",
        dataType: "text",
        success: function(data){
            $(".addLevelDiv").css("display", "none");
            $(".postLevelDiv").css("display", "block");
            $(".updLevelDiv").css("display", "none");
            if (data == "SUCCESS") {
                showPostLevel();
            } else if (data == "havaRecord") {
                layer.alert("该等级已存在！");
            }
        }
    });
}
//修改
function updPostLevel() {
    var postGrade = {};
    postGrade.id = Number($("#levelId").val());
    postGrade.wagesPostId = Number($("#selPostId").val());//岗位Id
    postGrade.gradeName = $("#updPostLevelName").val();//等级名称
    postGrade.postGradeWage = parseInt($("#updWagesName").val());//岗位等级工资
    if (gradeName == postGrade.gradeName) {
        postGrade.index = 0;
    } else {
        postGrade.index = 1;
    }
    $.ajax({
        type: "post",
        url: path + "/updPostGrade",
        data: JSON.stringify(postGrade),
        contentType: "application/json; charset=utf-8",
        dataType: "text",
        success: function(data){
            $(".addLevelDiv").css("display", "none");
            $(".postLevelDiv").css("display", "block");
            $(".updLevelDiv").css("display", "none");
            if (data == "SUCCESS") {
                showPostLevel();
            } else if (data == "havaRecord") {
                layer.alert("该等级已存在！");
            }
        }
    });
}
//取消
function cancel () {
    layer.closeAll();
}
function cancel1 () {
    $(".addLevelDiv").css("display", "none");
    $(".postLevelDiv").css("display", "block");
    $(".updLevelDiv").css("display", "none");
}