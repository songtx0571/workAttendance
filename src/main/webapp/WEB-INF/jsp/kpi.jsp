<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/kpi.js?version=1.0"></script>
    <script type="text/javascript" src="../js/week/alert.js"></script>
</head>
<style type="text/css">
    .top{
        width: 100%;
        padding-top: 20px;
        box-sizing: border-box;
    }
    .center{
        width: 99%;
        margin: 0 auto;
    }
    .showMonthNum,.showMonthPoint{
        display: none;
    }
    body::-webkit-scrollbar{
        display: none;
    }
</style>
<body>
<div class="warp">
    <div class="top">
        <input type="hidden" id="selStartTime">
        <div class="layui-inline" style="margin-bottom: 10px;">
            <label class="layui-form-label">日期:</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" id="test15" placeholder="年月">
            </div>
        </div>
    </div>
    <%--表格--%>
    <div class="center">
        <table id="demo" lay-filter="test"></table>
    </div>
    <%--月巡检次数--%>
    <div class="showMonthNum">
        <table id="demoNum" lay-filter="test1"></table>
    </div>
    <%--月巡检点数--%>
    <div class="showMonthPoint">
        <table id="demoPoint" lay-filter="test2"></table>
    </div>
</div>
</body>
</html>