<%--
  Created by IntelliJ IDEA.
  User: jayun
  Date: 2021/3/25
  Time: 14:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="/wa/js/jquery.min.js"></script>
    <script type="text/javascript" src="/wa/js/layui/layui.js"></script>
    <link rel="stylesheet" href="/wa/js/layui/css/layui.css">
</head>
<body>


<div class="top">
    <input type="hidden" id="selStartTime">
    <button class="layui-btn layui-btn-sm" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;" onclick="monthUpBtn()" >&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;</button>
    <div class="layui-inline" style="margin-bottom: 10px;float:left;">
        <div class="layui-input-inline">
            <input type="text" class="layui-input" id="test15" placeholder="年月">
        </div>
    </div>
    <button class="layui-btn layui-btn-sm" style="float: left;height: 38px;margin-left: 5px;" onclick="monthDownBtn()" >&nbsp;&nbsp;&gt;&gt;&nbsp;&nbsp;</button>
    <form class="layui-form" action="">
        <div class="layui-inline" style="float: left;margin-left: 50px;">
            <div class="layui-input-inline">
                <input type="hidden" id="selDepartNameHidden">
                <select name="modules" lay-verify="required" lay-filter="selDepartName" lay-search="" id="selDepartName">
                </select>
            </div>
        </div>
    </form>
</div>


<table class="layui-table" id="demo" lay-filter="test">



</table>




</body>

<script>
    let path="";
    var date = new Date();
    let year = date.getFullYear();
    var month = date.getMonth() + 1;
    $(function (){
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
    function  showKpi(startTime,departmentId){
        var win = $(window).height();
        var height = win-100;
        layui.use(['table',"form"], function() {
            var table = layui.table;
            table.render({
                elem: '#demo'
                , height: height
                , url: path + '/wa/kpi/getExamKnowledgeKpiList?startTime='+startTime+'&departmentId='+departmentId //数据接口
                , page: false
                ,totalRow: true
                , cols: [[ //表头
                    {field: 'Id', title: '编号', align: 'center', hide:true,width: 120, totalRowText: '合计'},
                    {field: 'userNumber', title: '员工编号', align: 'center', totalRowText: '合计'},
                    {field: 'companyName', title: '公司名称', align: 'center' ,},
                    {field: 'departmentName', title: '部门名称', align: 'center'},
                    {field: 'userName', title: '员工', align: 'center' },
                    {field: 'kt_count', title: '创建数',sort :true, align: 'center',event: '', style:'cursor: pointer;color: red;', totalRow: true},
                    {field: 'kc_count', title: '审核数',sort :true, align: 'center',event: '',     style:'cursor: pointer;color: red;', totalRow: true}
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

</script>
</html>
