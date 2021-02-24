<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/workingHours.js?version=1.0"></script>
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
    .showWorkingHour{
        display: none;
    }
</style>
<div class="warp">
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
    <div style="clear: both;"></div>
    <%--表格--%>
    <div class="center">
        <table id="demo" lay-filter="test"></table>
    </div>
    <%--月度总工时--%>
    <div class="showWorkingHour">
        <table id="demoWH" lay-filter="test1"></table>
    </div>
</div>
</html>