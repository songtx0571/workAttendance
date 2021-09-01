<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>运行考勤页面</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/operatingHours.js"></script>
    <style>
        *{
            margin: 0;
            padding: 0;
        }
        .warp{
            width: 100%;
        }
        .top{
            width: 100%;
            height: 60px;
            padding-top: 10px;
            box-sizing: border-box;
        }
        .content{
            width: 100%;
            height: calc(100% - 80px);
            padding: 0 10px;
            box-sizing: border-box;
        }
        .loading{
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            background-color: rgb(0, 0, 0);
            opacity: 0.8;
            text-align: center;
            padding-top: 300px;
            z-index: 19891020;
            display: none;
        }
        .loading div{
            animation:turn 1s linear infinite;
        }
        @keyframes turn{
            0%{-webkit-transform:rotate(0deg);}
            25%{-webkit-transform:rotate(90deg);}
            50%{-webkit-transform:rotate(180deg);}
            75%{-webkit-transform:rotate(270deg);}
            100%{-webkit-transform:rotate(360deg);}
        }
        .div table {
            border: 1px solid #e6e6e6;
            collapse: 0;
            color: #666;
            font-size: 14px;
        }
        .div table  tr th, .div table tr td{
            min-width: 65px;
            line-height: 33px;
            text-align: center;
            border: 1px solid #e6e6e6;
        }
        .div table thead, .div table tbody {
            display: block;
        }
        .div table tbody {
            overflow-y: auto;
        }
        .div table tbody::-webkit-scrollbar{
            display: none;
        }
        .operatingTd{
            text-align: center;
            cursor: pointer;

        }
        .layui-layer-tips {
            width: 200px;
        }
    </style>
</head>
<body>
<div class="warp">
    <div class="top">
        <input type="hidden" id="monthStart">
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
        <button class="layui-btn layui-btn-sm layui-btn-normal" style="float: left;height: 38px;margin-left: 15px;"
                onclick="preservationData()" id="preservationBtn">保存
        </button>
    </div>
    <div class="content">
        <div class="div"></div>
    </div>
    <div class="loading">
        <div style="width: 50px;margin: 0 auto;">
            <i class="layui-icon layui-icon-loading" style="font-size: 60px; color: #fff;"></i>
        </div>
    </div>
</div>
</body>
</html>
