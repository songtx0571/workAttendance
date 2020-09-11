<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <script type="text/javascript" src="../js/week/alert.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <link rel="stylesheet" href="../js/font/css/font-awesome.css">
    <script type="text/javascript" src="../js/week/leave.js"></script>
    <style>
        .top{
            width: 100%;
            height: 80px;
            padding-top: 20px;
            box-sizing: border-box;
        }
        .concent{
            width: 99%;
            margin: 0 auto;
        }
        .addBtn{
            width: 100%;
            height: 50px;
        }
        .addLeave,.updLeave{
            width: 500px;
            height: 300px;
            background: antiquewhite;
        }
    </style>
</head>
<body>
    <%--leave--%>
    <div class="warp">
        <div class="top">
            <div class="layui-inline">
                <label class="layui-form-label">日期</label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" id="test1" placeholder="yyyy-MM">
                </div>
            </div>
        </div>
        <%--表格--%>
        <div class="concent">
            <%--<div class="addBtn">--%>
                <%--<button type="button" class="layui-btn layui-btn-fluid" onclick="">添加请假</button>--%>
            <%--</div>--%>
            <table id="demo" lay-filter="test"></table>
            <script type="text/html" id="barDemo1">
                <a class="layui-btn layui-btn-xs" lay-event="updLeave" id="updLeave">修改</a>
            </script>
        </div>
        <%--添加--%>
        <%--<div class="addLeave">
            <table>
                <tr>
                    <td>请假人：</td>
                    <td>
                        <input type="text">
                    </td>
                </tr>
                <tr>
                    <td>请假配置名称：</td>
                    <td>
                        <form class="layui-form" action="">
                            <div class="layui-form-item">
                                <div class="layui-inline">
                                    <div class="layui-input-inline">
                                        <select name="modules" lay-verify="required" lay-filter="addLeaveName" lay-search="" id="addLeaveName">
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </td>
                </tr>
                <tr>
                    <td>开始时间：</td>
                    <td>
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" id="test2" placeholder="yyyy-MM">
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>结束时间：</td>
                    <td>
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" id="test3" placeholder="yyyy-MM">
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>状态：</td>
                    <td>
                        <input type="text">
                    </td>
                </tr>
                <tr>
                    <td>备注：</td>
                    <td>
                        <input type="text">
                    </td>
                </tr>
            </table>
        </div>--%>
        <%--修改--%>
        <%--<div class="updLeave"></div>--%>
    </div>
</body>
</html>
