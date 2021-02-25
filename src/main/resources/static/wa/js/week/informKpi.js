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
                showKpi(value,$("#selDepartNameHidden").val());
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
            , url: path + '/wa/kpi/getInformKPIList?startTime='+startTime+'&departmentId='+departmentId //数据接口
            , page: false
            ,totalRow: true
            , cols: [[ //表头
                {field: 'Id', title: '编号', align: 'center', hide:true,width: 120, totalRowText: '合计'},
                {field: 'userNumber', title: '员工编号', align: 'center', totalRowText: '合计'},
                {field: 'companyName', title: '公司名称', align: 'center' ,},
                {field: 'departmentName', title: '部门名称', align: 'center'},
                {field: 'userName', title: '员工', align: 'center' },
                {field: 'createdCount', title: '创建数',sort :true, align: 'center',event: 'createdCount', style:'cursor: pointer;color: red;', totalRow: true},
                {field: 'selCount', title: '查看数',sort :true, align: 'center',event: 'selCount', style:'cursor: pointer;color: red;', totalRow: true}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            var userId = data.Id;
            var startTime = $("#selStartTime").val();
            if (obj.event == 'createdCount') {//创建数
                countCreate(userId,startTime);
                layer.open({
                    type: 1
                    ,id: 'createdCountDiv' //防止重复弹出
                    ,content: $(".createdCountDiv")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
            } else if(obj.event == 'selCount'){//查看数
                countSel(userId,startTime);
                layer.open({
                    type: 1
                    ,id: 'selCountDiv' //防止重复弹出
                    ,content: $(".selCountDiv")
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
function countCreate(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoCC'
            , height: 500
            , url: path + '/wa/kpi/getCreatedList?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'created', title: '通知时间', align: 'center'},
                {field: 'title', title: '标题', align: 'center'},
                {field: 'content', title: '内容',sort :true, align: 'center'},
                {field: 'createdByName', title: '发起人',sort :true, align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test1)', function (obj) {
        })
    });
}
function countSel(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoSC'
            , height: 500
            , url: path + '/wa/kpi/getSelList?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'created', title: '通知时间', align: 'center'},
                {field: 'title', title: '标题', align: 'center'},
                {field: 'content', title: '内容',sort :true, align: 'center'},
                {field: 'createdByName', title: '查看人',sort :true, align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test2)', function (obj) {
        })
    });
}