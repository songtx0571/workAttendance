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
        //查询所有数据日期
        //年月选择器
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
    var height = win-200;
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/kpi/getKPIList?startTime='+startTime+'&departmentId='+departmentId //数据接口
            , page: false
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide:true,width: 120},
                {field: 'userNumber', title: '员工编号', align: 'center', totalRowText: '合计'},
                {field: 'companyName', title: '公司', align: 'center'},
                {field: 'departmentName', title: '部门', align: 'center'},
                {field: 'userName', title: '员工', align: 'center'},
                {field: 'frequency', title: '月巡检次数',sort :true, align: 'center', event: 'monthNum', style:'cursor: pointer;color: red;'},
                {field: 'point', title: '月巡检点数',sort :true, align: 'center', event: 'monthPoint', style:'cursor: pointer;color: red;'}
            ]]
            ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
                $("#sumNum").text(res.data.frequencySum);
                $("#averageNum").text(res.data.frequencyAverage);
                $("#averagePoint").text(res.data.pointAverage);
                $("#sumPoint").text(res.data.pointSum);
                $("#varianceNum").text(res.data.frequencyVariance);
                $("#variancePoint").text(res.data.pointVariance);
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": res.count, //解析数据长度
                    "data": res.data.list //解析数据列表
                };
            }
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            var userId = data.id;
            if (obj.event == 'monthNum') {//月巡检次数
                num(userId,startTime);
                layer.open({
                    type: 1
                    ,id: 'showMonthNum' //防止重复弹出
                    ,content: $(".showMonthNum")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
            } else if(obj.event == 'monthPoint'){//月巡检点数
                point(userId,startTime);
                layer.open({
                    type: 1
                    ,id: 'showMonthPoint' //防止重复弹出
                    ,content: $(".showMonthPoint")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
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
function num(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoNum'
            , height: 500
            , url: path + '/wa/kpi/getFrequencyList?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'inspectionStaTime', title: '开始执行时间', align: 'center'},
                {field: 'inspectionEndTime', title: '实际结束时间', align: 'center'},
                {field: 'inspectionEndTheoryTime', title: '理论结束时间',sort :true, align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test1)', function (obj) {
        })
    });
}
function point(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoPoint'
            , height: 500
            , url: path + '/wa/kpi/getPointList?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'equipment', title: '设备', align: 'center'},
                {field: 'measuringType', title: '测定类型', align: 'center'},
                {field: 'measuringTypeData', title: '数据',sort :true, align: 'center'},
                {field: 'unit', title: '单位',sort :true, align: 'center'},
                {field: 'created', title: '执行时间',sort :true, align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test2)', function (obj) {
        })
    });
}