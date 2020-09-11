<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <script type="text/javascript" src="../js/week/alert.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <link rel="stylesheet" href="../js/font/css/font-awesome.css">
    <script type="text/javascript" src="../js/week/leaveConfigure.js"></script>
    <style>
        *{
            margin: 0;
            padding: 0;
        }
        .warp{
            width: 100%;
            height: 100%;
        }
        .content{
            width: 99%;
            height: 100%;
            margin: 0 auto;
        }
        .td .layui-form-select {
            margin-top: -10px;
            margin-left: -15px;
            margin-right: -15px;
        }
        .bottom{
            width: 99%;
            height: 99px;
            margin: 0 auto;
            padding-top: 30px;
            box-sizing: border-box;
        }
        .updTable{
            width: 100%;
            border: 1px solid #e6e6e6;
            text-align: center;
        }
        .updTable tr{
            line-height: 40px;
        }
        .updTable td{
            border: 1px solid #e6e6e6;
        }
        .updTable tr .updTableTd input{
            width: 70%;
            height: 100%;
            border: none;
        }
        .addLeaveConfigureHtml{
            display: none;
        }

        .layui-table-cell{
            overflow: visible !important;
        }
        td .layui-form-select{
            margin-top: -10px;
            margin-left: -15px;
            margin-right: -15px;
        }
        .layui-form-select dl{
            z-index:9999;

        }

        .layui-table-cell{
            overflow:visible;

        }
        .layui-table-box{
            overflow:visible;

        }
        .layui-table-body{
            overflow:visible;

        }
        .div-inline{ display:inline}


        #fileName {
            position: relative;
            display: inline-block;
            background: #D0EEFF;
            border: 1px solid #99D3F5;
            border-radius: 4px;
            padding: 4px 12px;
            overflow: hidden;
            color: #1E88C7;
            text-decoration: none;
            text-indent: 0;
            line-height: 20px;
        }
        #fileName input {
            position: absolute;
            font-size: 100px;
            right: 0;
            top: 0;
            opacity: 0;
        }
        #fileName:hover {
            background: #AADFFD;
            border-color: #78C3F3;
            color: #004974;
            text-decoration: none;
        }
        .layui-input, .layui-select, .layui-textarea {
            height: 30px;
            line-height: 30px;
            border-style: solid;
            margin-top: 9px;
        }
</style>
</head>
<body>
    <div class="warp">
        <div class="content">
            <input type="hidden" id="statusHidden">
            <table id="demo" lay-filter="test"></table>
            <script type="text/html" id="barDemo11">
                <a class="layui-btn layui-btn-xs layui-btn-normal openStatus{{d.id}}" lay-event="openStatus">开启</a>
                <a class="layui-btn layui-btn-xs layui-btn-normal closeStatus{{d.id}}" lay-event="closeStatus">关闭</a>
            </script>
            <script type="text/html" id="barDemo12">
                <a class="layui-btn layui-btn-xs" lay-event="updLeaveConfigure" id="updLeaveConfigure">修改</a>
            </script>
            <script type="text/html" id="barDemo13">
                <input type="hidden" id="updUnitHidden">
                <select name='duty_type' id="duty_type" lay-filter="testSelect" lay-search='' style="text-align: center">
                    <option value="0" {{# if (d.unit==0){ }} selected="selected" {{# } }}>请选择</option>
                    <option value="1" {{# if (d.unit==1){ }} selected="selected" {{# } }}>天</option>
                    <option value="2" {{# if (d.unit==2){ }} selected="selected" {{# } }}>次</option>
                    <option value="3" {{# if (d.unit==3){ }} selected="selected" {{# } }}>月</option>
                    <option value="4" {{# if (d.unit==4){ }} selected="selected" {{# } }}>小时</option>
                </select>
            </script>
            <div>
                <div class="bottom">
                    <button type="button" class="layui-btn layui-btn-fluid" onclick="showAddLeaveConfigure()">添加配置项</button>
                </div>
                <div class="addLeaveConfigureHtml">
                    <form class="layui-form" action="">
                        <table class="updTable" cellspacing="0">
                            <tr style="background: #f2f2f2">
                                <td>名字</td>
                                <td>数值</td>
                                <td>单位</td>
                                <td>操作</td>
                            </tr>
                            <tr>
                                <td class="updTableTd">
                                    <input type="text" id="addName" placeholder="请输入名字">
                                </td>
                                <td class="updTableTd">
                                    <input type="text" id="addData" oninput = "value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" placeholder="请输入数值">
                                </td>
                                <td>
                                    <input type="hidden" id="addUnitHidden">
                                    <form class="layui-form" action="">
                                        <div class="layui-inline">
                                            <div class="layui-input-inline">
                                                <select name="modules" lay-verify="required" lay-filter="unitList" lay-search="" id="unitList">
                                                    <option value="0">请选择单位</option>
                                                    <option value="1">天</option>
                                                    <option value="2">次</option>
                                                    <option value="3">月</option>
                                                    <option value="4">小时</option>
                                                </select>
                                            </div>
                                        </div>
                                    </form>
                                </td>
                                <td>
                                    <button type="button" class="layui-btn layui-btn-sm" onclick="addLeaveConfigure()">确定</button>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
