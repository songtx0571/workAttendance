var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth() + 1;
$(function () {
    if (month == 12) {
        month = 1;
        year = year + 1;
    }
    if (month == 0) {
        month = 12;
        year = year - 1;
    }
    if (month < 10) {
        month = "0"+month;
    }
    //查询所有数据
    showLeaveStatisticsList(1,year+"-"+month);
    $("#test15").val(year+"-"+month);
    //显示时间
    showDate();
});
//显示当前月份
function showLeaveStatisticsList(pageCount,month) {
    var win = $(window).height();
    var height = win-90;
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/leave/getLeaveDataStatisticsList?month='+month //数据接口
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
                selShowLeaveList(value)
            }
        });
    })
}
//根据条件查询
function selShowLeaveList(startTime) {
    showLeaveStatisticsList(1,startTime);
}
//上个月
function monthUpBtn() {
    var time = $("#test15").val();
    var y = time.substring(0,4);
    var m = time.substring(5,7);
    m --;
    if (m == 0) {
        m = 12;
        y = y - 1;
    }
    if (m > 0 && m < 10){
        m = "0" + m;
    }
    $("#test15").val(y+"-"+m);
    var a1 = y+"-"+m;
    showLeaveStatisticsList(1,a1);
}
//下个月
function monthDownBtn() {
    var time = $("#test15").val();
    var y = time.substring(0,4);
    var m = time.substring(5,7);
    m ++;
    if (m == 13){
        m = 1;
        y = Number(y) + 1;
    }
    if (m > 0 && m < 10){
        m = "0" + m;
    }
    $("#test15").val(y+"-"+m);
    var a2 = y+"-"+m;
    showLeaveStatisticsList(1,a2);
}