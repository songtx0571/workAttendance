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
    .layui-table-view{
        margin-bottom: 0px;
        border-bottom: none;
    }
    .averageVariance{
        width: 100%;
        border: 1px solid #e6e6e6;
        border-top: none;
    }
    .averageVariance tr{
        line-height: 38px;
        text-align: center;
        color: #666;
        font-size: 14px;
    }
    .averageVariance tr td{
        width: 16.6%;
        border: 1px solid #e6e6e6;
        background: rgb(242,242,242);
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
        <table class="averageVariance" cellpadding="0">
            <tr>
                <td>合计</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td style="color: red;" id="sumNum">&nbsp;</td>
                <td style="color: red;" id="sumPoint">&nbsp;</td>
            </tr>
            <tr>
                <td>均值</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td style="color: red;" id="averageNum">&nbsp;</td>
                <td style="color: red;" id="averagePoint">&nbsp;</td>
            </tr>
            <tr>
                <td>方差</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td style="color: red;" id="varianceNum">&nbsp;</td>
                <td style="color: red;" id="variancePoint">&nbsp;</td>
            </tr>
        </table>
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