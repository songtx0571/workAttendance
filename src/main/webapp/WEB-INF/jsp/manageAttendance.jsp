<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/manageAttendance.js"></script>

    <style>
        .warp {
            margin: 0;
            padding: 0;
        }

        .top {
            height: 60px;
            padding-top: 15px;
            box-sizing: border-box;
        }

        .center {
            width: 100%;
            padding: 0px 10px 0 10px;
            box-sizing: border-box;
        }

        #goWorkBtn {
            display: none;
            margin-left: 10px;
            float: left;
            line-height: 38px;
            color: #000;
            font-size: 20px;
            text-align: center;
            cursor: pointer;
        }

        #goWorkBtn span {
            font-size: 16px;
            padding: 0px 10px;
            box-sizing: border-box;
            background: #1E9FFF;
            color: #fff;
            display: inline-block;
        }
    </style>
</head>
<body>
<div class="warp">
    <div class="top">
        <button class="layui-btn layui-btn-sm" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;"
                onclick="monthUpBtn()">&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;
        </button>
        <div class="layui-inline" style="margin-bottom: 10px;float:left;">
            <div class="layui-input-inline">
                <input type="text" class="layui-input" id="test" placeholder="年月">
            </div>
        </div>
        <button class="layui-btn layui-btn-sm" style="float: left;height: 38px;margin-left: 5px;"
                onclick="monthDownBtn()">&nbsp;&nbsp;&gt;&gt;&nbsp;&nbsp;
        </button>
        <p id="goWorkBtn"><span onclick="goWork()">上班</span></p>

    </div>
    <%--表格--%>
    <div class="center">
        <table id="demo" lay-filter="test"></table>
    </div>
</div>
</body>
</html>
