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
            position: absolute;
            display: none;
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
            height: 50px;
            line-height: 50px;
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
            width: 100%;
            border: 1px solid;
            collapse: 0;
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
    </style>
</head>
<body>
    <div style="width: 100%;height: 100%;">
        <div>
            <div class="top">
                <input type="hidden" id="cycleDataHidden">
                <div class="layui-inline">
                    <label class="layui-form-label">考核日期</label>
                    <div class="layui-input-inline">
                        <input type="text" class="layui-input" id="test3" placeholder="yyyy-MM">
                    </div>
                </div>
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
                    <div class="layui-inline">
                        <label class="layui-form-label">考核日期</label>
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="test4" placeholder="yyyy-MM">
                        </div>
                    </div>
                </li>
                <%--<li><button class="layui-btn" onclick="showAchievement()">查询</button></li>--%>
                <li><button class="layui-btn" onclick="showAddateAchievement()" id="addBtnAchievementDiv" style="display: none;">添加考核项</button></li>
            </ul>
            <div id="achievementTable" style="display: none">
                <table class="achievementTable" style="width: 100%;border: 1px solid #ccc;collapse: 0;text-align: center">
                    <thead>
                    <tr>
                        <th style="width: 13%;">工作任务</th>
                        <th>考核标准</th>
                        <th style="width: 6%;">考核详情</th>
                        <th style="width: 6%;">考核分</th>
                        <th style="width: 6%;">权重</th>
                        <th style="width: 15%;">操作</th>
                    </tr>
                    </thead>
                    <tbody id='achievementTbody'>

                    </tbody>
                </table>
            </div>
        </div>
        <%--修改工作业绩--%>
        <%--<div class="updateAchievement">
            <input type="hidden" id="achievementIdHidden">
            <table>
                <caption align="top">修改考核内容</caption>
                <tr>
                    <td>工作任务:</td>
                    <td><input type="text" id="udeWorkTasks"></td>
                </tr>
                <tr>
                    <td>考核标准:</td>
                    <td><input type="text" id="uqdAccess"></td>
                </tr>
                <tr>
                    <td>考核详情:</td>
                    <td><input type="text" id="updDetail"></td>
                </tr>
                <tr class="cycleTr" style="display: none;">
                    <td>考核分:</td>
                    <td><input type="number" id="updScore"></td>
                </tr>
                <tr>
                    <td>权重:</td>
                    <td><input type="number" id="updWeights"></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;width: 100%;"><button class="layui-btn" onclick="updateAchievement()">确定</button><button class="layui-btn" onclick="cancel1()">取消</button></td>
                </tr>
            </table>
        </div>--%>
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
                    <div class="layui-inline">
                        <label class="layui-form-label">考核日期</label>
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="test6" placeholder="yyyy-MM">
                        </div>
                    </div>
                </li>
                <%--<li><button class="layui-btn" onclick="showBehavior()">查询</button></li>--%>
                <%--<li><button class="layui-btn" onclick="showAddBehavior()" id="addBtnBehaviorDiv" style="display: none">添加考核项</button></li>--%>
            </ul>
            <div class="behaviorTable">
                    <table style="width: 100%;border: 1px solid #ccc;collapse: 0;text-align: center">
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
                            <td><input id="period" name="period" placeholder="请输入"></td>
                        </tr>
                        <tr>
                            <td rowspan="8" style="vertical-align:middle">考勤情况（50分）</td>
                            <td colspan="2">满勤50分</td>
                            <td colspan="7">满勤。每半天1分，每天2分</td>
                            <td><input id="fchuqing" name="fchuqing" readonly></td>
                        </tr>
                        <tr>
                            <td >
                                <a style="display: inline" onclick="addCount('tiaoxiu')"><img height="15px" src="../img/and.png"></a>
                                <input value="0" id="tiaoxiu" name="tiaoxiu" class="inputCount">
                                <a style="display: inline" onclick="reduceCount('tiaoxiu')"><img height="15px" src="../img/reduce.png"></a>
                                <span style="display: none;">数值已为0</span>
                            </td>
                            <td>天</td>
                            <td colspan="7">调休。每次半天不扣分，一天扣1分</td>
                            <td><input id="ftiaoxiu" name="ftiaoxiu" readonly>
                        </tr>
                        <tr>
                            <td>
                                <a style="display: inline" onclick="addCount('qingjia')"><img height="15px" src="../img/and.png"></a>
                                <input value="0" id="qingjia" name="qingjia" class="inputCount">
                                <a style="display: inline" onclick="reduceCount('qingjia')"><img height="15px" src="../img/reduce.png"></a>
                                <span style="display: none;">数值已为0</span>
                            </td>
                            <td>天</td>
                            <td colspan="7">请假。每半天3分，每天6分</td>
                            <td><input id="fqingjia" name="fqingjia" readonly></td>
                        </tr>
                        <tr>
                            <td>
                                <a style="display: inline" onclick="addCount('kuanggong')"><img height="15px" src="../img/and.png"></a>
                                <input value="0" id="kuanggong" name="kuanggong" class="inputCount">
                                <a style="display: inline" onclick="reduceCount('kuanggong')"><img height="15px" src="../img/reduce.png"></a>
                                <span style="display: none;">数值已为0</span>
                            </td>
                            <td>天</td>
                            <td colspan="7">旷工。每半天10分，每天20分</td>
                            <td><input id="fkuanggong" name="fkuanggong" readonly></td>
                        </tr>
                        <tr>
                            <td>
                                <a style="display: inline" onclick="addCount('chidao')"><img height="15px" src="../img/and.png"></a>
                                <input value="0" id="chidao" name="chidao" class="inputCount">
                                <a style="display: inline" onclick="reduceCount('chidao')"><img height="15px" src="../img/reduce.png"></a>
                                <span style="display: none;">数值已为0</span>
                            </td>
                            <td>次</td>
                            <td colspan="7">迟到与早退。每次1分。每月10个学时</td>
                            <td><input id="fchidao" name="fchidao" readonly></td>
                        </tr>
                        <tr>
                            <td>
                                <a style="display: inline" onclick="addCount('lunxiu')"><img height="15px" src="../img/and.png"></a>
                                <input value="0" id="lunxiu" name="lunxiu" class="inputCount">
                                <a style="display: inline" onclick="reduceCount('lunxiu')"><img height="15px" src="../img/reduce.png"></a>
                                <span style="display: none;">数值已为0</span>
                            </td>
                            <td>天</td>
                            <td colspan="7">轮休。每半天0.5分，每天1分</td>
                            <td><input id="flunxiu" name="flunxiu"  readonly></td>
                        </tr>
                        <tr>
                            <td>
                                <a style="display: inline" onclick="addCount('chuchai')"><img height="15px" src="../img/and.png"></a>
                                <input value="0" id="chuchai" name="chuchai" class="inputCount">
                                <a style="display: inline" onclick="reduceCount('chuchai')"><img height="15px" src="../img/reduce.png"></a>
                                <span style="display: none;">数值已为0</span>
                            </td>
                            <td>天</td>
                            <td colspan="7">出差。每半天1分，每天2分</td>
                            <td><input id="fchuchai" name="fchuchai" readonly></td>
                        </tr>
                        <tr>
                            <td colspan="9">合计</td>
                            <td><input type="text" id="sum" name="sum" disabled="disabled" ></td>
                        </tr>
                        <tr>
                            <td>加班(小时)</td>
                            <td colspan="2">
                                <a style="display: inline" onclick="addCount('jiaban')"><img height="15px" src="../img/and.png"></a>
                                <input value="0" id="jiaban" name="jiaban" class="inputCount">
                                <a style="display: inline" onclick="reduceCount('jiaban')"><img height="15px" src="../img/reduce.png"></a>
                                <span style="display: none;">数值已为0</span>
                            </td>
                            <td colspan="2">值班(天)</td>
                            <td colspan="2">
                                <a style="display: inline" onclick="addCount('zhiban')"><img height="15px" src="../img/and.png"></a>
                                <input value="0" id="zhiban" name="zhiban" class="inputCount">
                                <a style="display: inline" onclick="reduceCount('zhiban')"><img height="15px" src="../img/reduce.png"></a>
                                <span style="display: none;">数值已为0</span>
                            </td>
                            <td colspan="3">考勤(天)</td>
                            <td>
                                <a style="display: inline" onclick="addCount('kaoqin')"><img height="15px" src="../img/and.png"></a>
                                <input value="0" id="kaoqin" name="kaoqin" class="inputCount">
                                <a style="display: inline" onclick="reduceCount('kaoqin')"><img height="15px" src="../img/reduce.png"></a>
                                <span style="display: none;">数值已为0</span>
                            </td>
                        </tr>
                        <tr>
                            <td>净绩效</td>
                            <td colspan="4"><input type="text" id='netPerformance' name="netPerformance" disabled="disabled" ></td>
                            <td colspan="5">综合绩效</td>
                            <td><input type="text" id="comprehensivePerformance" name="comprehensivePerformance" disabled="disabled" ></td>
                        </tr>
                        <tr>
                            <td>备注</td>
                            <td colspan="10"><input id="remark" name="remark"></td>
                        </tr>
                        <tr>
                            <td colspan="11">
                                <button class="layui-btn" onclick="updBehavior()">确定</button><button class="layui-btn" onclick="cancel()">取消</button>
                            </td>
                        </tr>
                    </table>
            </div>
        </div>
        <div class="showSetSign">
            <p id="showSetSignData" style="display: none;"></p>
            <h2 style="text-align: center;margin: 20px 0;font-weight: bold;">业绩详情</h2>
            <table>
                <thead>
                    <tr>
                        <th>业绩1</th>
                        <th>业绩2</th>
                        <th>业绩3</th>
                        <th>业绩4</th>
                        <th>业绩5</th>
                        <th>业绩6</th>
                        <th>业绩7</th>
                        <th>业绩8</th>
                        <th>业绩9</th>
                        <th>业绩10</th>
                    </tr>
                </thead>
                <tbody class="showSetSignTbody">

                </tbody>
            </table>
        </div>
    </div>
    <script>
        layui.use('table', function(){
            var table = layui.table;

        });
    </script>
</body>
</html>
