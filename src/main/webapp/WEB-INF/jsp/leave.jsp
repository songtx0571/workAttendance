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
        .addLeave{
            width: 600px;
            display: none;
            margin: 20px auto;
            position: relative;
        }
        .updLeave1{
            width: 600px;
            display: none;
            margin: 20px auto;
        }
        .addLeave table,.updLeave1 table{
            width: 100%;
        }
        .addLeave table tr,.updLeave1 table tr{
            line-height: 65px;
            height: 65px;
        }
        .addLeave table tr td:first-of-type,.updLeave1 table tr td:first-of-type{
            text-align: right;
            width: 200px;
        }
        .addLeave table tr td input,.updLeave1 table tr td input{
            width: 100%;
        }
        .addLeave table tr td .layui-form-item .layui-inline,.addLeave table tr td .layui-form-item .layui-inline .layui-input-inline{
            width: 100%;
        }
        .updLeave1 table tr td .layui-form-item .layui-inline,.updLeave1 table tr td .layui-form-item .layui-inline .layui-input-inline{
            width: 100%;
        }
        .inputTr td input{
            height: 38px;
            font-size: 20px;/
        outline: none;
            border: 1px solid #e6e6e6;
        }
        body::-webkit-scrollbar{
            display: none;
        }
        /*审核*/
        .examineLeaveInfo{
            width: 90%;
            margin: 10px auto;
            display: none;
        }
        .examineLeaveInfo table{
            width: 100%;
            border: 1px solid;
            font-size: 18px;
        }
        .examineLeaveInfo table tr{
            line-height: 60px;
        }
        .examineLeaveInfo table td{
            border: 1px solid;
            width: 25%;
            text-align: center;
        }
        .examineLeavePeople,.showExamineLeavePeople{
            margin-top: 15px;
            width: 100%;
            display: none;
        }
        .showExamineLeavePeople table{
            margin-top: 15px;
        }
    </style>
</head>
<body>
<%--leave--%>
<div class="warp">
    <div class="top">
        <button class="layui-btn layui-btn-sm" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;" onclick="monthUpBtn()" >&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;</button>
        <div class="layui-inline" style="margin-bottom: 10px;float:left;">
            <div class="layui-input-inline">
                <input type="text" class="layui-input" id="test15" placeholder="年月">
            </div>
        </div>
        <button class="layui-btn layui-btn-sm" style="float: left;height: 38px;margin-left: 5px;" onclick="monthDownBtn()" >&nbsp;&nbsp;&gt;&gt;&nbsp;&nbsp;</button>
    </div>
    <div style="clear: both;"></div>
    <%--表格--%>
    <div class="concent">
        <div class="addBtn">
            <button type="button" class="layui-btn layui-btn-fluid" onclick="showAddLeave()">添加请假</button>
        </div>
        <table id="demo" lay-filter="test"></table>
        <script type="text/html" id="barDemo1">
            {{#  if(d.reviewResult == "通过"){ }}
            <a class="layui-btn  layui-btn-xs" lay-event="showLeave" >查看</a>
            {{#  } else if(d.reviewResult == "未通过") { }}
            <a class="layui-btn  layui-btn-xs" lay-event="showLeave">查看</a>
            <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="delLeave">删除</a>
            {{#  } else  { }}
            <a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="exmLeave" id="exmLeave">审核</a>
            <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="delLeave">删除</a>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo2">
            {{#  if(d.status == 2){ }}
            <span style="color: #0c7cd5">审核完毕</span>
            {{#  } else if(d.status == 1) { }}
            <span style="color: #009f95;">审核中</span>
            {{#  } else { }}
            <span style="color: red;">未审核</span>
            {{#  } }}
        </script>
    </div>
    <form class="layui-form" action="">
        <%--添加--%>
        <div class="addLeave">
            <table>
                <tr>
                    <td>请假人：</td>
                    <td style="position: relative;">
                        <input type="hidden" id="addEmployeeNameHidden">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <div class="layui-input-inline">
                                    <select name="modules" lay-verify="required" lay-filter="addEmployeeName" lay-search="" id="addEmployeeName">
                                    </select>
                                </div>
                            </div>
                        </div>
                        <span id="addEmployeeNameSpan" style="position: absolute;right: -94px;top: -10px;color: red;display: none">请选择员工</span>
                    </td>
                </tr>
                <tr>
                    <td>请假类型：</td>
                    <td style="position: relative;">
                        <input type="hidden" id="addLeaveNameHidden">
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <div class="layui-input-inline">
                                    <select name="modules" lay-verify="required" lay-filter="addLeaveName" lay-search="" id="addLeaveName">
                                    </select>
                                </div>
                            </div>
                        </div>
                        <span id="addLeaveNameSpan" style="position: absolute;right: -125px;top: 0px;color: red;display: none">请选择请假类型</span>
                    </td>
                </tr>
                <tr>
                    <td>选择日期：</td>
                    <td style="position: relative;">
                        <input type="hidden" id="addLeaveTimeHidden">
                        <div class="layui-inline" style="width: 100%">
                            <div class="layui-input-inline" style="width: 100%">
                                <input type="text" class="layui-input" id="test2" placeholder="yyyy-MM-dd">
                            </div>
                        </div>
                        <span id="addStartTimeSpan" style="position: absolute;right: -125px;top: 0px;color: red;display: none">请选择请假日期</span>
                    </td>
                </tr>
                <tr>
                    <td>选择时间：</td>
                    <td style="position: relative;">
                        <input type="hidden" id="addLeaveStartTimeHidden">
                        <input type="hidden" id="addLeaveEndTimeHidden">
                        <input type="hidden" id="addStartTimeHidden">
                        <input type="hidden" id="addEndTimeHidden">
                        <div class="layui-inline" style="width: 100%">
                            <div class="layui-input-inline" style="width: 100%">
                                <input type="text" class="layui-input" id="test3" placeholder="yyyy-MM-dd HH:mm">
                            </div>
                        </div>
                        <span id="addEndTimeSpan" style="position: absolute;right: -125px;top: 0px;color: red;display: none">请选择请假时间</span>
                    </td>
                </tr>
                <tr class="inputTr">
                    <td>备注：</td>
                    <td>
                        <input type="text" id="addRemark">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center">
                        <button type="button" class="layui-btn layui-btn-normal" onclick="addBtnOk()">确定</button>
                        <button type="button" class="layui-btn layui-btn-normal" onclick="cancel()">取消</button>
                    </td>
                </tr>
            </table>
            <p style="text-align: center;color: red;font-size: 20px;line-height: 30px;display: none;">请假超额!</p>
        </div>
        <%--修改--%>
        <%--<div class="updLeave1">
            <table>
                <tr>
                    <td>请假人：</td>
                    <td  style="position: relative;">
                        <input type="hidden" id="updEmployeeId">
                            <div class="layui-form-item">
                                <div class="layui-inline">
                                    <div class="layui-input-inline">
                                        <select name="modules" lay-verify="required" lay-filter="updEmployeeName" lay-search="" id="updEmployeeName" disabled>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        <span id="updEmployeeNameSpan" style="position: absolute;right: -94px;top: -10px;color: red;display: none">请选择员工</span>
                    </td>
                </tr>
                <tr>
                    <td>请假类型：</td>
                    <td style="position: relative;">
                        <input type="hidden" id="updLeaveNameHidden">
                            <div class="layui-form-item">
                                <div class="layui-inline">
                                    <div class="layui-input-inline">
                                        <select name="modules" lay-verify="required" lay-filter="updLeaveName" lay-search="" id="updLeaveName">
                                        </select>
                                    </div>
                                </div>
                            </div>
                        <span id="updLeaveNameSpan" style="position: absolute;right: -125px;top: 0px;color: red;display: none">请选择请假类型</span>
                    </td>
                </tr>
                <tr>
                    <td>开始时间：</td>
                    <td>
                        <input type="hidden" id="updStartTimeHidden">
                        <div class="layui-inline" style="width: 100%">
                            <div class="layui-input-inline" style="width: 100%">
                                <input type="text" class="layui-input" id="test4" placeholder="yyyy-MM-dd HH:mm">
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>结束时间：</td>
                    <td>
                        <input type="hidden" id="updEndTimeHidden">
                        <div class="layui-inline" style="width: 100%">
                            <div class="layui-input-inline" style="width: 100%">
                                <input type="text" class="layui-input" id="test5" placeholder="yyyy-MM-dd HH:mm">
                            </div>
                        </div>
                    </td>
                </tr>
                <tr class="inputTr">
                    <td>备注：</td>
                    <td>
                        <input type="text" id="updRemark">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center">
                        <button type="button" class="layui-btn layui-btn-normal" onclick="updBtnOk()">确定</button>
                        <button type="button" class="layui-btn layui-btn-normal" onclick="cancel()">取消</button>
                    </td>
                </tr>
            </table>
        </div>--%>
        <%--审核--%>
        <div class="examineLeaveInfo">
            <input type="hidden" id="examineIdHidden">
            <table cellspacing="0">
                <tr>
                    <td>请假人</td>
                    <td><span class="exmEmployeeName"></span></td>
                    <td>请假类型</td>
                    <td><span class="exmLeaveName"></span></td>
                </tr>
                <tr>
                    <td>开始时间</td>
                    <td><span class="exmStartTime"></span></td>
                    <td>结束时间</td>
                    <td><span class="exmEndTime"></span></td>
                </tr>
                <tr>
                    <td>创建人</td>
                    <td><span class="exmCreatedName"></span></td>
                    <td>审核状态</td>
                    <td><span class="exmStatusName"></span></td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td colspan="3"><span class="exmRemark"></span></td>
                </tr>
            </table>
            <div class="examineLeavePeople">
                <table cellspacing="0">
                    <tr>
                        <td>审核时间</td>
                        <td><span id="exmTime"></span></td>
                        <td>审核人</td>
                        <td><span id="exmName"></span></td>
                    </tr>
                    <tr>
                        <td>审核意见</td>
                        <td colspan="3">
                            <textarea name="" id="exmReviewRemark" rows="3" style="width: 100%;border: none;"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <p class="examineBtn">
                                <button type="button" class="layui-btn layui-btn-normal" onclick="examineOk()">同意</button>
                                <button type="button" class="layui-btn layui-btn-normal" onclick="examineNo()">驳回</button>
                            </p>
                            <span id="examineShowOk" style="display: none;">审核：同意</span>
                            <span id="examineShowNo" style="display: none;">审核：驳回</span>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="showExamineLeavePeople">

            </div>
        </div>
    </form>
</div>
</body>
</html>
