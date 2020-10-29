<%--
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/icon.css" />
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/default/easyui.css" />
    <script type="text/javascript" src="../js/week/informKpi.js?version=1.0"></script>
</head>
<style type="text/css">
    .bodyHeader{
        width: 100%;
        height: 10%;
        background-color: #F0F8FF;
    }
    .bodyContent{
        width: 100%;
        height: 90%;
        overflow-x: auto;
        overflow-y: scroll;
    }
    .bodyContent::-webkit-scrollbar {
        display: none;
    }
    .bodyContentHead{
        margin: 10px;
        height: 100%;
        text-align: center;
    }
    ul{
        margin: 0px 0px 0px 0px;
        padding: 20px 0px 0px 0px;
        width: 100%;
        height: 80%;
        background-color: #F0F8FF;
        list-style-type: none;
    }
    li{
        height: 100%;
        float: left;
        margin-left: 40px;
    }
    .KPITable{
        margin:auto;
        border: 1px solid #00bbee;
        border-collapse:collapse
    }
</style>
<body class="easyui-layout">
    <div class="bodyHeader">
        <ul>
            <li>
                <div>
                    <span>日期&nbsp;&nbsp;</span>&nbsp;&nbsp;
                    <select id="startTime" class="easyui-datebox"  data-options="required:true,width:'165px'"></select>
                </div>
            </li>
            <li>
                <div id="search" style="height: 40px;width: 80px;border-radius: 5px;background-color: #00bbee;line-height: 40px;" onclick="javascript:showKpi()" class="easyui-linkbutton" plain="true">
                    <a href="javascript:void(0)" id="btn-save" style="text-decoration: none;color: #222222">确定</a>
                </div>
            </li>
        </ul>
    </div>
    &lt;%&ndash;内容主体&ndash;%&gt;
    <div class="bodyContent">
        <div id="bodyContentHead" class="bodyContentHead">
            <table id="KPITable" class="KPITable">

            </table>
        </div>
    </div>
</body>
</html>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/informKpi.js?version=1.0"></script>
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
    .createdCountDiv,.selCountDiv{
        display: none;
    }
</style>
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
        <form class="layui-form" action="" style="display: inline-block;">
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button type="button" class="layui-btn" onclick="showKpi()">查询</button>
                </div>
            </div>
        </form>
    </div>
    <%--表格--%>
    <div class="center">
        <table id="demo" lay-filter="test"></table>
    </div>
    <%--月巡检次数--%>
    <div class="createdCountDiv">
        <table id="demoCC" lay-filter="test"></table>
    </div>
    <%--月巡检点数--%>
    <div class="selCountDiv">
        <table id="demoSC" lay-filter="test"></table>
    </div>
</div>
</html>