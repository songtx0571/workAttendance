<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/leaveStatistics.js"></script>
    <style>
        .top{
            width: 100%;
            padding-top: 20px;
            box-sizing: border-box;
        }
        .center{
            width: 99%;
            margin: 0 auto;
        }
    </style>
</head>
<body>
    <div class="warp">
        <div class="top">
            <input type="hidden" id="selStartTime">
            <div class="layui-inline" style="margin-bottom: 10px;">
                <label class="layui-form-label">日期</label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" id="test15" placeholder="年月">
                </div>
            </div>
            <input type="hidden" id="selEmployeeNameHidden">
            <form class="layui-form" action="" style="display: inline-block;margin-bottom: 10px;">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">员工</label>
                        <div class="layui-input-inline">
                            <select name="modules" lay-verify="required" lay-filter="selEmployeeName" lay-search="" id="selEmployeeName">
                            </select>
                        </div>
                    </div>
                </div>
            </form>
            <form class="layui-form" action="" style="display: inline-block;">
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <button type="button" class="layui-btn" onclick="selShowLeaveList()">查询</button>
                    </div>
                </div>
            </form>
        </div>
        <%--表格--%>
        <div class="center">
            <table id="demo" lay-filter="test"></table>
        </div>
    </div>
</body>
</html>
