var index = 0;
// var path = "http://192.168.1.26:8081/";
var path = "";
$(function () {
    //查询所有数据
    showLeaveList();
    //显示时间
    showDate();
    //显示请假配置名称
    // showLeaveName();
});
//查询所有数据
function showLeaveList() {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: 500
            , url: path + 'leave/getLeaveDataList' //数据接口
            , page: true //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'employeeName', title: '请假人', width: 150}
                , {field: 'leaveName', title: '请假类型'}
                , {field: 'startTime', title: '开始时间',sort: true}
                , {field: 'endTime', title: '结束时间',sort: true}
                // , {field: 'intervalTime', title: '间隔时间',sort: true}
                , {field: 'created', title: '创建时间',sort: true}
                , {field: 'createdName', title: '创建人',sort: true}
                // , {field: 'status', title: '状态',sort: true}
                , {field: 'remark', title: '备注',sort: true}
                // , {fixed: '', title: '操作', toolbar: '#barDemo1', align: 'center ', width: 250}
            ]]
            , done: function (res, curr, count) {

            }
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            /*if (obj.event == 'updLeave') {// 修改
                index=layer.open({
                    type: 1
                    ,id: 'updateLeave' //防止重复弹出
                    ,content: $(".updateLeave")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '500px']
                    ,yes: function(){}
                });
            }*/
        });
    });
}
//显示时间
function showDate() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询所有数据日期
        laydate.render({
            elem: '#test1'
            , type: 'month'
            , done: function (value) {
            }
        });
    })
}
// 显示请假配置名称
/*
function showLeaveName() {
    layui.use(['form'], function () {
        var form = layui.form;
        $.ajax({
            type: "GET",
            url: path + "inform/getCompanyList",
            dataType: "json",
            success: function (data) {
                //通用公司下拉框
                $("#addLeaveName").empty();
                var option = "<option value='0' >请选择请假名称</option>";
                for (var i = 0; i < data.length; i++) {
                    option += "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
                }
                $('#addLeaveName').html(option);
                form.render();//菜单渲染 把内容加载进去
            }
        });
        form.on('select(companyList)', function (data) {
        });
    });
}*/
