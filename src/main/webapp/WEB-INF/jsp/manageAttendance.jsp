<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>管理考勤页面</title>
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
            background: brown;
            color: #fff;
            display: inline-block;
        }
        .loading {
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            background-color: rgb(0, 0, 0);
            opacity: 0.8;
            text-align: center;
            padding-top: 300px;
            z-index: 9999999;
            display: none;
        }

        .loading div {
            animation: turn 1s linear infinite;
        }

        @keyframes turn {
            0% {
                -webkit-transform: rotate(0deg);
            }
            25% {
                -webkit-transform: rotate(90deg);
            }
            50% {
                -webkit-transform: rotate(180deg);
            }
            75% {
                -webkit-transform: rotate(270deg);
            }
            100% {
                -webkit-transform: rotate(360deg);
            }
        }
        .layui-layer-tips {
            width: 150px;
        }
        .layui-table-cell{
            padding: 0;
            height: 100%;
        }
        .layui-table-view .layui-table td{
            padding: 0;
            height: 39px;
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
        <shiro:hasPermission name="保存考勤数据">
        <button class="layui-btn layui-btn-sm layui-btn-normal" style="float: left;height: 38px;margin-left: 15px;"
                onclick="preservationData()" id="preservationBtn">保存
        </button>
        </shiro:hasPermission>
    </div>
    <%--表格--%>
    <div class="center">
        <table id="demo" lay-filter="test"></table>
    </div>
    <div class="loading">
        <div style="width: 50px;margin: 0 auto;">
            <i class="layui-icon layui-icon-loading" style="font-size: 60px; color: #fff;"></i>
        </div>
    </div>
</div>
</body>
</html>
