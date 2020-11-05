var path = "";
var month = "";
var year = "";
$(function () {
    var date = new Date();
    month = date.getMonth()+1;
    year = date.getFullYear();
    var dateYM = year+"-"+month;
    //查询所有数据
    showLeaveStatisticsList(1,dateYM,"");
    //显示时间
    showDate();
    //显示人
    showEmployeeName();
});
//显示当前月份
function showLeaveStatisticsList(pageCount,month,employeeId) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: 500
            , url: path + '/wa/leave/getLeaveDataStatisticsList?month='+month+'&employeeId='+employeeId //数据接口
            , page: {
                curr: pageCount
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'employeeName', title: '姓名', width: 200}
                , {field: 'leaveName', title: '请假类型',sort: true}
                , {field: 'total', title: '数量',sort: true}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
        });
    });
}
//显示时间
function showDate() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询所有数据日期
        //年月选择器
        laydate.render({
            elem: '#test15'
            ,type: 'month'
            ,trigger: 'click'//呼出事件改成click
            , done: function (value) {
                $("#selStartTime").val(value);
            }
        });
    })
}
//显示请假人
function showEmployeeName() {
    layui.use(['form'], function () {
        var form = layui.form;
        $.ajax({
            type: "GET",
            url: path + "/wa/leave/getEmployeeName",
            dataType: "json",
            success: function (data) {
                $("#selEmployeeName").empty();
                var option = "<option value='0' >请选择人员</option>";
                for (var i = 0; i < data.length; i++) {
                    option += "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
                }
                $('#selEmployeeName').html(option);
                form.render();//菜单渲染 把内容加载进去
            }
        });
        form.on('select(selEmployeeName)', function (data) {
            $("#selEmployeeNameHidden").val(data.value);
        });
    });
}
//根据条件查询
function selShowLeaveList() {
    var startTime1 = $("#selStartTime").val();
    var selEmployeeId = $("#selEmployeeNameHidden").val();
    showLeaveStatisticsList(1,startTime1,selEmployeeId);
}