var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth() + 1;
$(function(){
    showDate();
    showDepart();
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
    $("#selDepartNameHidden").val("");
    showKpi(year+"-"+month,$("#selDepartNameHidden").val());
    $("#test15").val(year+"-"+month);
});
//显示时间
function showDate() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#test15'
            ,type: 'month'
            ,trigger: 'click'//呼出事件改成click
            , done: function (value) {
                showKpi(value,$("#selDepartNameHidden").val())
            }
        });
    })
}
//显示部门
function showDepart() {
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
            showKpi($("#test15").val(),$("#selDepartNameHidden").val());
        });
    })
}
//查询工时数据
function showKpi(startTime,departmentId) {
    var win = $(window).height();
    var height = win-100;
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/kpi/getWorkHoursList?startTime='+startTime+'&departmentId='+departmentId //数据接口
            , page: false
            ,totalRow: true
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide:true,width: 120},
                {field: 'userNumber', title: '员工编号', align: 'center', totalRowText: '合计'},
                {field: 'companyName', title: '公司名称', align: 'center' ,},
                {field: 'departmentName', title: '部门名称', align: 'center'},
                {field: 'name', title: '员工', align: 'center'},
                {field: 'workingHours', title: '月度总工时',sort :true, align: 'center',event: 'workingHour', style:'color: red;', totalRow: true},
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            var userId = data.id;
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
    showKpi(a1,$("#selDepartNameHidden").val())
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
    showKpi(a2,$("#selDepartNameHidden").val())
}
//月度总工时
function workHour(userId,startTime) {
    var win = $(window).height();
    var height = win-100;
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoWH'
            , height: height
            , url: path + '/wa/kpi/getSelWorkHoursList?userId='+userId+'&startTime='+startTime  //数据接口
            , cols: [[ //表头
                {field: 'day', title: '日期', align: 'center'},
                {field: 'workHours', title: '工时', align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test1)', function (obj) {
        })
    });
}
