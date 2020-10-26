var index = 0;
var path = "";
$(function () {
    getConfigureList();
    showUnit();
});
var id = "";
var name = "";
var unit = "";
var remark = "";
var quota = "";
var data1 = "";
//查询配置信息
function getConfigureList() {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        var form = layui.form;
        table.render({
            elem: '#demo'
            , height: 500
            , url: path + '/wa/leave/getConfigureList' //数据接口
            , page: true //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'name', title: '名称', width: 150, sort: true, edit: 'text'}
                , {field: 'unit', title: '单位', width: 100,align:'center',templet: '#barDemo13'}
                , {field: 'data', title: '数值', event: 'setSign', style: 'cursor: pointer;', sort: true, edit: 'text', width: 70}
                , {field: 'created', title: '创建时间', sort: true}
                , {field: 'remark', title: '备注', sort: true, edit: 'text'}
                , {field: 'quota', title: '限额', sort: true, edit: 'text'}
                , {fixed: '', title: '状态', toolbar: '#barDemo11', align: 'center ', width: 180}
                , {fixed: '', title: '操作', toolbar: '#barDemo12', align: 'center ', width: 100}
            ]]
            , done: function (res, curr, count) {
                for (var i = 0; i < res.data.length; i ++){
                    var status = res.data[i].status;
                    if (status == "0") {
                        $("#statusHidden").val("0");
                        $(".closeStatus"+res.data[i].id).css("background", "#ccc");
                        $(".openStatus"+res.data[i].id).css("background", "#1E9FFF");
                        $(".openStatus"+res.data[i].id).attr({"disabled":"disabled"});
                        $(".openStatus"+res.data[i].id).css("cursor","no-drop");
                        $(".closeStatus"+res.data[i].id).removeAttr("disabled");
                        $(".closeStatus"+res.data[i].id).css("cursor","pointer");
                    } else{
                        $("#statusHidden").val("1");
                        $(".closeStatus"+res.data[i].id).css("background", "#1E9FFF");
                        $(".openStatus"+res.data[i].id).css("background", "#ccc");
                        $(".closeStatus"+res.data[i].id).attr({"disabled":"disabled"});
                        $(".closeStatus"+res.data[i].id).css("cursor","no-drop");
                        $(".openStatus"+res.data[i].id).removeAttr("disabled");
                        $(".openStatus"+res.data[i].id).css("cursor","pointer");
                    }
                }
            }
        });
        //监听单元格编辑
        table.on('edit(test)', function (obj) {
        });
        form.on('select(testSelect)', function (data) {
            var unitTd = data.value;
            $("#updUnitHidden").val(unitTd);
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            if (obj.event == 'openStatus') {//开启
                $("#statusHidden").val("0");
                $(".closeStatus"+data.id).css("background", "#ccc");
                $(".openStatus"+data.id).css("background", "#1E9FFF");
                $(".openStatus"+data.id).attr({"disabled":"disabled"});
                $(".openStatus"+data.id).css("cursor","no-drop");
                $(".closeStatus"+data.id).removeAttr("disabled");
                $(".closeStatus"+data.id).css("cursor","pointer");
            } else if (obj.event == 'closeStatus') {//关闭
                $("#statusHidden").val("1");
                $(".closeStatus"+data.id).css("background", "#1E9FFF");
                $(".openStatus"+data.id).css("background", "#ccc");
                $(".closeStatus"+data.id).attr({"disabled":"disabled"});
                $(".closeStatus"+data.id).css("cursor","no-drop");
                $(".openStatus"+data.id).removeAttr("disabled");
                $(".openStatus"+data.id).css("cursor","pointer");
            } else if (obj.event == 'updLeaveConfigure') {// 修改
                var res = obj.data;
                id = res.id;
                name = res.name;
                unit = $("#updUnitHidden").val();
                remark =  res.remark;
                quota = res.quota;
                data1 = res.data;
                var status = $("#statusHidden").val();
                if (unit == "0"){
                    alert("请选择单位");
                    return;
                }
                $.ajax({
                    url: path + "/wa/leave/updateConfigure",
                    dataType: "json",//数据格式
                    type: "post",//请求方式
                    data: {name:name, unit: unit, data: data1, status: status, id : id,remark: remark, quota: quota},
                    success: function(data){
                        if (data == "SUCCESS") {
                            // alert("修改成功");
                            getConfigureList();
                        }
                    }
                });
            }
        });
    });
}
//显示新增配置页面
function showAddLeaveConfigure() {
    $(".addLeaveConfigureHtml").css("display", "block");
}
//单位下拉框
function showUnit() {
    layui.use(['form'], function(){
        var form = layui.form;
        form.on('select(unitList)', function(data){
            $("#addUnitHidden").val(data.value);
        });
    });
}
//添加配置
function addLeaveConfigure() {
    var leave = {};
    leave.name = $("#addName").val();
    leave.data = $("#addData").val();
    leave.unit = $("#addUnitHidden").val();
    leave.quota = $("#addQuota").val();
    leave.remark = $("#addRemark").val();
    if (leave.name == ""){
        alert("请填写名字");
        return;
    }
    if (leave.data == ""){
        alert("请填写数值");
        return;
    }
    if (leave.unit == "" || leave.unit == "0") {
        alert("请选择单位");
        return;
    }
    if (leave.quota == ""){
        leave.quota = 0;
    }else{
        $.ajax({
            url: path + "/wa/leave/addConfigure",
            dataType: "json",//数据格式
            type: "post",//请求方式
            data: JSON.stringify(leave),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                getConfigureList();
                $(".addLeaveConfigureHtml").css("display", "none");
            }
        });
    }
}