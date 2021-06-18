<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <%--<script type="text/javascript" src="../js/week/alert.js"></script>--%>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/achievements.js"></script>
    <link rel="stylesheet" href="../js/font/css/font-awesome.css">
    <style>
        *{
            margin: 0;
            padding: 0;
        }
        li{
            list-style: none;
            float: left;
        }
        .top{
            height: 80px;
            padding: 20px 0;
            box-sizing: border-box;
        }
        .showAchievementDiv{
            width: 100%;
            height: 100%;
            display: none;
            position: absolute;
            padding: 0 10px;
            box-sizing: border-box;
        }
        .showAchievementList,.showBehaviorList{
            height: 50px;
            display: flex;
            justify-content: space-around;
            margin-bottom: 20px;
        }
        .showAchievementList li,.showBehaviorList li{
            line-height: 40px;
        }
        #AssessmentTr{
            text-align: center;
        }
        #achievementTable .achievementTable tr,.behaviorTable table tr{
            height: 40px;
            line-height: 40px;
        }
        #achievementTable .achievementTable th, #achievementTable .achievementTable td,.behaviorTable table td{
            border: 1px solid #ccc;
        }
        .achievementTable td textarea,.achievementTable td input[type=text]{
            width: 99%;
            line-height: 35px;
            border: none;
            text-align: center;
        }
        .addateAchievement{
            width: 100%;
            height: 100%;
            position: absolute;
            display: none;
        }
        .addateAchievement table{
            width: 80%;
            margin: 20px auto;
            font-size: 20px;
        }
        .addateAchievement tr{
            height: 65px;
            line-height: 65px;
        }
        .addateAchievement tr td:first-of-type{
            width: 30%;
            text-align: right;
            padding-right: 10px;
            box-sizing: border-box;
        }
        .addateAchievement tr td input{
            width: 100%;
            height: 40px;
            border: 1px solid #e6e6e6;
        }
        .showBehaviorDiv{
            width: 100%;
            height: 100%;
            position: absolute;
            display: none;
            padding: 0 10px;
            box-sizing: border-box;
        }
        .showSetSign{
            width: 100%;
            height: 100%;
            position: absolute;
            display: none;
        }
        .showSetSign table{
            border: 1px solid;
            collapse: 0;
            text-align: center;
            margin: 0 auto;
            width: 90%;
        }
        .showSetSign table tr{
            height: 50px;
            line-height: 50px;
        }
        .showSetSign table td, .showSetSign table th{
            border: 1px solid;
        }
        .inputCount{
            width: 32px;
            text-align: center;
        }
        .behaviorTable input{
            border: none;
        }
        #remark{
            width: 100%;
            font-size: 18px;
            padding-left: 10px;
        }
        .behaviorTable span{
            color: red;
            font-size: 14px;
        }
        body::-webkit-scrollbar{
            display: none;
        }
    </style>
</head>
<body>
    <div style="width: 100%;height: 100%;">
        <div>
            <div class="top">
                <input type="hidden" id="cycleDataHidden">
                <button class="layui-btn layui-btn-sm" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;" onclick="monthUpBtn('zong')" >&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;</button>
                <div class="layui-inline" style="margin-bottom: 10px;float:left;">
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" id="test3" placeholder="yyyy-MM">
                    </div>
                </div>
                <button class="layui-btn layui-btn-sm" style="float: left;height: 38px;margin-left: 5px;" onclick="monthDownBtn('zong')" >&nbsp;&nbsp;&gt;&gt;&nbsp;&nbsp;</button>
            </div>
            <div id="conentTable" style="padding: 0 10px;box-sizing: border-box;">
                <table id="demo" lay-filter="test"></table>
                <script type="text/html" id="barDemo11">
                    <a class="layui-btn layui-btn-xs" lay-event="showAchievement" id="showAchievement">工作业绩考核</a>
                    <a class="layui-btn layui-btn-xs" lay-event="showBehavior" id="showBehavior">工作行为考核</a>
                </script>
            </div>
        </div>
        <%--工作业绩考核--%>
        <div class="showAchievementDiv">
            <h3 style="text-align: center;margin: 20px 0;font-weight: bold;">工作业绩考核</h3>
            <input type="hidden" id="employeeIdHidden">
            <input type="hidden" id="employeeIdHidden1">
            <ul class="showAchievementList">
                <li>编号：</li>
                <li><strong id="userNumber">G99</strong></li>
                <li>姓名：</li>
                <li><strong id="userName">99</strong></li>
                <li>
                    <input type="hidden" id="cycleDataHidden1">
                    <button class="layui-btn layui-btn-sm" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;" onclick="monthUpBtn('yeji')" >&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;</button>
                    <div class="layui-inline" style="margin-bottom: 10px;float:left;">
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="test4" placeholder="yyyy-MM">
                        </div>
                    </div>
                    <button class="layui-btn layui-btn-sm" style="float: left;height: 38px;margin-left: 5px;" onclick="monthDownBtn('yeji')" >&nbsp;&nbsp;&gt;&gt;&nbsp;&nbsp;</button>
                </li>
            </ul>
            <ul class="showAchievementList">
                <li><button class="layui-btn" onclick="showAddateAchievement()" id="addBtnAchievementDiv">添加考核项</button></li>
                <li>
                    <button class="layui-btn layui-btn-normal" style="float: left;margin-left: 10px;"  onclick="switchBehavior('achievement')">切换</button>
                </li>
                <li>
                    <button class="layui-btn" style="float: left;" onclick="showCopyTime()">复制</button>
                </li>
                <li>
                    <input type="hidden" id="copyTimeHidden">
                    <div class="layui-inline" style="margin-bottom: 10px;float:left;display: none;margin-left: 5px" id="hideTimeDiv">
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="test7" placeholder="yyyy-MM">
                        </div>
                    </div>
                </li>
                <li>
                    <button class="layui-btn" style="float: left;display: none;margin-left: 10px;" id="copyTimeBtn" onclick="copyTimeOk()">确定复制</button>
                </li>
            </ul>
            <div id="achievementTable">
                <table class="achievementTable" style="width: 100%;border: 1px solid #ccc;collapse: 0;text-align: center">
                    <thead>
                    <tr>
                        <th style="width: 13%;">工作任务</th>
                        <th>考核标准</th>
                        <th style="width: 10%;">考核详情</th>
                        <th style="width: 6%;">考核分</th>
                        <th style="width: 6%;">权重</th>
                        <th style="width: 15%;">操作</th>
                    </tr>
                    </thead>
                    <tbody id='achievementTbody'>

                    </tbody>
                    <tbody id="achievementTbodyBtn">
                        <tr>
                            <td colspan="6" style="text-align: center;">
                                <input type='button' value='修改' class='layui-btn' onclick='updAchievement()' />
                                <input type='button' value='取消' class='layui-btn' onclick='cancel()' />
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <%--添加工作业绩--%>
        <div class="addateAchievement">
            <table>
                <caption align="top">添加考核内容</caption>
                <tr>
                    <td>工作任务:</td>
                    <td><input type="text" id="addWorkTasks"></td>
                </tr>
                <tr>
                    <td>考核标准:</td>
                    <td><input type="text" id="addAccess"></td>
                </tr>
                <tr>
                    <td>考核详情:</td>
                    <td><input type="text" id="addDetail"></td>
                </tr>
                <%--<tr>--%>
                    <%--<td>考核分:</td>--%>
                    <%--<td><input type="number" id="addScore"></td>--%>
                <%--</tr>--%>
                <tr>
                    <td>权重:</td>
                    <td><input type="number" id="addWeights"></td>
                </tr>
                <tr>
                    <td>考核日期:</td>
                    <td>
                        <input type="hidden" id="addCycleDataHidden">
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" id="test5" placeholder="yyyy-MM">
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;width: 100%;"><button class="layui-btn" onclick="addAteAchievement()">确定</button><button class="layui-btn" onclick="cancel()">取消</button></td>
                </tr>
            </table>
        </div>
        <%--工作行为考核--%>
        <div class="showBehaviorDiv">
            <input type="hidden" id="employeeIdHidden2">
            <input type="hidden" id="BeId">
            <h3 style="text-align: center;margin: 20px 0;font-weight: bold;">工作行为考核</h3>
            <ul class="showBehaviorList">
                <li>编号：</li>
                <li><strong id="userNumber1">G99</strong></li>
                <li>姓名：</li>
                <li><strong id="userName1">99</strong></li>
                <li>
                    <input type="hidden" id="cycleDataHidden2">
                    <button class="layui-btn layui-btn-sm" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;" onclick="monthUpBtn('xingwei')" >&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;</button>
                    <div class="layui-inline" style="margin-bottom: 10px;float:left;">
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="test6" placeholder="yyyy-MM">
                        </div>
                    </div>
                    <button class="layui-btn layui-btn-sm" style="float: left;height: 38px;margin-left: 5px;" onclick="monthDownBtn('xingwei')" >&nbsp;&nbsp;&gt;&gt;&nbsp;&nbsp;</button>
                </li>
                <li>
                    <button class="layui-btn layui-btn-normal" style="float: left;margin-left: 10px;"  onclick="switchBehavior('behavior')">切换</button>
                </li>
            </ul>
            <div class="behaviorTable">
                <table style="width: 100%;border: 1px solid #ccc;collapse: 0;text-align: center">
                    <tbody style="text-align: center">
                        <tr>
                            <td>考核项目</td>
                            <td colspan="2" >考核分值</td>
                            <td colspan="7" >考核标准</td>
                            <td>考核分</td>
                        </tr>
                        <tr>
                            <td rowspan="5" style="vertical-align:middle">学习能力（50分）</td>
                            <td width="100px">10</td>
                            <td>分</td>
                            <td colspan="7">第一周考试成绩</td>
                            <td><input class="week1" name="week1" readonly /></td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>分</td>
                            <td colspan="7">第二周考试成绩</td>
                            <td><input class="week2" name="week2" readonly /></td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>分</td>
                            <td colspan="7">第三周考试成绩</td>
                            <td><input class="week3" name="week3" readonly /></td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>分</td>
                            <td colspan="7">第四周考试成绩</td>
                            <td><input class="week4" name="week4" readonly /></td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>学时</td>
                            <td colspan="7">学时完成情况。每月10个学时</td>
                            <td><input class="period" name="period" value="10" readonly></td>
                        </tr>
                        </tbody>
                    <tbody style="text-align: center" id="leaveConfig"></tbody>
                    <tbody style="text-align: center">
                        <tr>
                            <td colspan="10">合计</td>
                            <td><input type="text" id="sum" name="sum" disabled="disabled" ></td>
                        </tr>
                        <tr>
                            <td>加班(小时)</td>
                            <td colspan="5" id="jiaban">
                            </td>
                            <td colspan="4">考勤(天)</td>
                            <td colspan="4" id="kaoqin">
                            </td>
                        </tr>
                        <tr>
                            <td>净绩效</td>
                            <td colspan="5"><input type="text" id='netPerformance' name="netPerformance" disabled="disabled" ></td>
                            <td colspan="4">综合绩效</td>
                            <td><input type="text" id="comprehensivePerformance" name="comprehensivePerformance" disabled="disabled" ></td>
                        </tr>
                        <tr>
                            <td>备注</td>
                            <td colspan="10"><input id="remark" name="remark"></td>
                        </tr>
                        <tr>
                            <td colspan="11">
                                <button class="layui-btn hideBtn" onclick="updBehavior()">确定</button><button class="layui-btn" onclick="cancel()">取消</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="showSetSign">
            <p id="showSetSignData" style="display: none;"></p>
            <h2 style="text-align: center;margin: 20px 0;font-weight: bold;">业绩详情</h2>
            <p style="font-size: 20px;font-weight: bold;text-align: center;line-height: 116px;color: red;display: none;" id="setSignP">无业绩！</p>
            <table id="achievementTableId">
            </table>
        </div>
    </div>
</body>
</html>
