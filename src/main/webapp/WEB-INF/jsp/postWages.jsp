<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/postWages.js"></script>
    <style>
        *{
            margin: 0;
            padding: 0;
        }
        .warp{
            width: 100%;
            height: 100%;
        }
        .top{
            width: 100%;
            height: 60px;
            padding: 15px 10px 15px 10px;
            box-sizing: border-box;
        }
        .content{
            padding: 0 10px;
            box-sizing: border-box;
        }
        .addPostDiv, .addLevelDiv, .updPostDiv,.postLevelDiv,.postLevelBigDiv,.updLevelDiv{
            display: none;
        }
        .addPostDiv table ,.addLevelDiv table, .updPostDiv table,.updLevelDiv table{
            width: 500px;
            margin: 0 auto;
        }
        .addPostDiv table tr, .addLevelDiv table tr, .updPostDiv table tr, .updLevelDiv table tr{
            line-height: 80px;
        }
        .addPostDiv table tr td, .addLevelDiv table tr td, .updPostDiv table tr td, .updLevelDiv table tr td{
            text-align: left;
        }
        .addPostDiv table tr td:first-child, .addLevelDiv table tr td:first-child, .updPostDiv table tr td:first-child, .updLevelDiv table tr td:first-child{
            text-align: right;
            padding-right: 5px;
            box-sizing: border-box;
        }
        .addPostDiv table tr td .addInput, .addLevelDiv table tr td .addInput,.updPostDiv table tr td .addInput, .updLevelDiv table tr td .addInput{
            width: 360px;
            height: 38px;
            border: 1px solid #e6e6e6;
            padding-left: 10px;
            box-sizing: border-box;
        }
        .postLevelDiv{
            padding: 0px 10px;
            box-sizing: border-box;
        }
        body::-webkit-scrollbar{
            display: none;
        }
    </style>
</head>
<body>
    <div class="warp">
        <div class="top">
            <button type="button" class="layui-btn" onclick="showAddPost()">添加岗位</button>
        </div>
        <div class="content">
            <table id="demo" lay-filter="test"></table>
            <script type="text/html" id="barDemo">
                <a class="layui-btn layui-btn-xs" lay-event="edit">修改</a>
                <a class="layui-btn layui-btn-xs" lay-event="level">等级</a>
            </script>
            <script type="text/html" id="barGradeName">
                {{#  layui.each(d.postGrade, function(index, item){ }}
                    {{#  if(item.gradeName == null){ }}
                    <p style="color: red;">无等级</p>
                    {{#  } else { }}
                    <span>{{ item.gradeName }}：<strong style="color: #0c7cd5">{{ item.postGradeWage }}</strong>；</span>
                    {{#  } }}
                {{#  }); }}
            </script>
        </div>
        <%--添加岗位页面--%>
        <div class="addPostDiv">
            <h1 style="text-align: center;margin-bottom: 30px">添加岗位</h1>
            <table>
                <tr>
                    <td><span>岗位名称:</span></td>
                    <td><input type="text" id="addInput" class="addInput"></td>
                </tr>
                <tr>
                    <td><span>岗位工资:</span></td>
                    <td><input type="text" id="addWages" class="addInput"></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <button class="layui-btn" onclick="addPost()">确定</button>
                        <button class="layui-btn layui-btn-normal" onclick="cancel()">取消</button>
                    </td>
                </tr>
            </table>
        </div>
        <%-- 岗位修改 --%>
        <div class="updPostDiv">
            <input type="hidden" id="selPostId">
            <table>
                <tr>
                    <td>岗位名称：</td>
                    <td>
                        <input id="selPostName" class="addInput" />
                    </td>
                </tr>
                <tr>
                    <td>岗位工资：</td>
                    <td>
                        <input id="updWages" class="addInput" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <button class="layui-btn" onclick="updPost()">确定</button>
                        <button class="layui-btn layui-btn-normal" onclick="cancel()">取消</button>
                    </td>
                </tr>
            </table>
        </div>

        <div class="postLevelBigDiv">
            <%--岗位等级--%>
            <div class="postLevelDiv">
                <div class="top">
                    <button type="button" class="layui-btn" onclick="showAddLevel()">添加等级</button>
                </div>
                <input type="hidden" id="postLevelId">
                <table id="demoLevel" lay-filter="testLevel"></table>
                <script type="text/html" id="barDemoLevel">
                    <a class="layui-btn layui-btn-xs" lay-event="editLevel">修改</a>
                </script>
            </div>
            <%--添加岗位等级--%>
            <div class="addLevelDiv">
                <h1 style="text-align: center;margin-bottom: 30px">添加岗位等级</h1>
                <table>
                    <tr>
                        <td><span>岗位:</span></td>
                        <td id="addPostName"></td>
                    </tr>
                    <tr>
                        <td><span>岗位等级:</span></td>
                        <td>
                            <input type="text" id="addPostLevelName" class="addInput">
                        </td>
                    </tr>
                    <tr>
                        <td><span>等级工资:</span></td>
                        <td>
                            <input type="text" id="addWagesName" class="addInput">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center;">
                            <button class="layui-btn" onclick="addPostLevel()">确定</button>
                            <button class="layui-btn layui-btn-normal" onclick="cancel1()">取消</button>
                        </td>
                    </tr>
                </table>
            </div>
            <%--修改岗位等级--%>
            <div class="updLevelDiv">
                <input type="hidden" id="levelId">
                <h1 style="text-align: center;margin-bottom: 30px">添加岗位等级</h1>
                <table>
                    <tr>
                        <td><span>岗位:</span></td>
                        <td id="updPostName"></td>
                    </tr>
                    <tr>
                        <td><span>岗位等级:</span></td>
                        <td>
                            <input type="text" id="updPostLevelName" class="addInput">
                        </td>
                    </tr>
                    <tr>
                        <td><span>等级工资:</span></td>
                        <td>
                            <input type="text" id="updWagesName" class="addInput">
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center;">
                            <button class="layui-btn" onclick="updPostLevel()">确定</button>
                            <button class="layui-btn layui-btn-normal" onclick="cancel1()">取消</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
