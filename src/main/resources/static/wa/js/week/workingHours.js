var path = "";
$(function(){
    showDate();
    showDepartName();
});
//显示时间
function showDate() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询所有数据日期
        //年月选择器
        laydate.render({
            elem: '#test15'
            ,type: 'month'
            , done: function (value) {
                $("#selStartTime").val(value);
            }
        });
    })
}
//显示部门
function showDepartName() {
    layui.use(['form'], function () {
        var form = layui.form;
        $.ajax({
            type: "GET",
            url: path + "/wa/department/getDepartmentList",
            dataType: "json",
            success: function (data) {
                $("#selDepartName").empty();
                var option = "<option value='0' >请选择部门</option>";
                for (var i = 0; i < data.length; i++) {
                    option += "<option value='" + data[i].id + "'>" + data[i].text + "</option>"
                }
                $('#selDepartName').html(option);
                form.render();//菜单渲染 把内容加载进去
            }
        });
        form.on('select(selDepartName)', function (data) {
            $("#selDepartNameHidden").val(data.value);
        });
    });
}
//查询工时数据
function showKpi() {
    if ( $("#selDepartNameHidden").val() == "" ||  $("#selDepartNameHidden").val() == "0"){
        alert("请选择部门");
        return;
    }
    var depart = $("#selDepartNameHidden").val();
    var startTime = $("#selStartTime").val();
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: 500
            , url: path + '/wa/kpi/getWorkHoursList?depart='+depart+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide:true,width: 120},
                {field: 'name', title: '员工', align: 'center'},
                {field: 'workingHours', title: '月度总工时',sort :true, align: 'center',event: 'workingHour', style:'color: red;'},
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            var userId = data.id;
            var startTime = $("#selDepartNameHidden").val();
            if (obj.event == 'workingHour') {//月度总工时
                layer.open({
                    type: 1
                    ,id: 'showWorkingHour' //防止重复弹出
                    ,content: $(".showWorkingHour")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
                workHour(userId,startTime)
            }
        });
    });
}
//月度总工时
function workHour(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoWH'
            , height: 500
            , url: path + '/wa/kpi/toSelWorkHours?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'day', title: '日期', align: 'center'},
                {field: 'workHours', title: '工时', align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
        })
    });
}
