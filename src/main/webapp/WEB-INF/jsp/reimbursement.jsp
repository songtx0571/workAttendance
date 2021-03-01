<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/reimbursement.js"></script>
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
            height: 80px;
            padding: 20px 0;
            box-sizing: border-box;
        }
        .department, .smartIMIS{
            padding: 0 10px;
            box-sizing: border-box;
        }
        .insertReimbursement1,.examineDiv,.updateReimbursement{
            width: 450px;
            margin: 10px auto 0;
            display: none;
        }
        .insertReimbursement1 table,.updateReimbursement table,.examineDiv table{
            width: 100%;
        }
        .insertReimbursement1 table tr,.updateReimbursement table tr,.examineDiv table tr{
            line-height: 50px;
        }
        .insertReimbursement1 table tr td,.updateReimbursement table tr td,.examineDiv table tr td{
            text-align: left;
        }
        .insertReimbursement1 table tr td:first-of-type,.updateReimbursement table tr td:first-of-type,.examineDiv table tr td:first-of-type{
            text-align: right;
            padding-right: 10px;
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
        <button class="layui-btn layui-btn-sm" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;" onclick="monthUpBtn()" >&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;</button>
        <div class="layui-inline" style="margin-bottom: 10px;float:left;">
            <div class="layui-input-inline">
                <input type="text" class="layui-input" id="test3" placeholder="yyyy-MM">
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
        <shiro:hasPermission name="报销项目部">
        <button class="layui-btn layui-btn-sm  layui-btn-normal" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;" onclick="showAddDiv()">添加</button>
        </shiro:hasPermission>
    </div>
    <div class="department">
        <table id="demo" lay-filter="test"></table>
        <script type="text/html" id="barDemoDepartOk">
            {{#  if(d.financeResult == 2){ }}
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="departEdit">修改</a>
            <span style="color: #FFB800;">无效</span>
            {{#  } else if(d.financeResult == 1) { }}
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="departEdit">修改</a>
            <span style="color: #009688;">有效</span>
            {{#  } else if(d.financeResult == 0){ }}
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="departEdit">修改</a>
            <shiro:hasPermission name="报销管理员">
                <a class="layui-btn layui-btn-xs" lay-event="departOk">有效</a>
                <a class="layui-btn layui-btn-xs  layui-btn-warm" lay-event="departNo">无效</a>
            </shiro:hasPermission>
            {{#  } else if(d.financeResult == 3) { }}
            <span style="color: #009688;">有效</span>
            {{#  } else if(d.financeResult == 4) { }}
            <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="departEdit">修改</a>
            <span style="color: #009688;">有效</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemoDepartExamine">
            {{#  if(d.financeResult == 3){ }}
            <span style="color: #009688;">审核通过</span>
            {{#  } else if(d.financeResult == 4) { }}
            <span style="color: #FFB800;">审核未通过</span>
            {{#  } else if(d.financeResult == 2) { }}
            <span style="color: #FFB800;">审核未通过</span>
            {{#  } else if(d.financeResult == 1) { }}
            <shiro:hasPermission name="报销监视员">
                <span style="color: #1E9FFF;">审核中</span>
            </shiro:hasPermission>
            <shiro:hasPermission name="报销财务员">
                <a class="layui-btn layui-btn-xs" lay-event="examine">审核</a>
            </shiro:hasPermission>
            {{#  } else if(d.financeResult == 0) { }}
            <span style="color: #009688;">未审核</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemoDepartSubject">
            {{#  if(d.subject == 1){ }}
            <span>差旅费</span>
            {{#  } else if(d.subject == 2) { }}
            <span>差旅补助</span>
            {{#  } else if(d.subject == 3) { }}
            <span>招待费</span>
            {{#  } else if(d.subject == 4) { }}
            <span>办公费</span>
            {{#  } else if(d.subject == 5) { }}
            <span>劳保费</span>
            {{#  } else if(d.subject == 6) { }}
            <span>员工福利费</span>
            {{#  } else if(d.subject == 7) { }}
            <span>员工保险费</span>
            {{#  } else if(d.subject == 8) { }}
            <span>员工培训费</span>
            {{#  } else if(d.subject == 9) { }}
            <span>车辆费用</span>
            {{#  } else if(d.subject == 10) { }}
            <span>器具</span>
            {{#  } else if(d.subject == 11) { }}
            <span>固定资产</span>
            {{#  } else if(d.subject == 12) { }}
            <span>房租</span>
            {{#  } else { }}
            <span>其他</span>
            {{#  } }}
        </script>
    </div>
    <%--添加--%>
    <form  method="post" target="iframe1" class="layui-form" action="">
        <div class="insertReimbursement1">
            <table>
                <tr>
                    <td><samp style="color: red;">*</samp>日期</td>
                    <td>
                        <input type="hidden" id="addTest4Hidden">
                        <div class="layui-inline">
                            <div class="layui-input-inline" style="width: 300px;">
                                <input type="text" class="layui-input" id="test4" placeholder="yyyy-MM-dd">
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><span style="float: right;margin-bottom: 10px;">部门</span><samp style="color: red;float: right;">*</samp></td>
                    <td>
                        <input type="hidden" id="addDepartNameHidden">
                        <input type="hidden" id="addDepartHidden">
                        <div class="layui-form-item" style="margin-bottom: 0px;">
                            <div class="layui-inline">
                                <div class="layui-input-inline" style="width: 300px;">
                                    <select name="modules" lay-verify="required" lay-filter="addDepartName" lay-search="" id="addDepartName">
                                    </select>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>报销人</td>
                    <td>
                        <input type="text" id="addPeopleName" disabled="disabled"  style="width: 300px;height: 38px;text-indent: 10px;">
                    </td>
                </tr>
                <tr>
                    <td><samp style="color: red;">*</samp>报销内容</td>
                    <td style="padding-top: 10px;">
                        <textarea name="" id="addContent" cols="30" rows="5" style="width: 300px;text-indent: 10px;"></textarea>
                    </td>
                </tr>
                <tr>
                    <td><samp style="color: red;">*</samp>金额</td>
                    <td>
                        <input type="text" id="addMoney" placeholder="输入两位小数" style="width: 300px;height: 38px;margin-top: 20px;margin-bottom: 20px;text-indent: 10px;"onBlur="twoDecimal('addMoney',this.value)";>
                    </td>
                </tr>
                <tr>
                    <td><span style="float: right;margin-bottom: 10px;">科目</span><samp style="color: red;float: right;">*</samp></td>
                    <td>
                        <input type="hidden" id="addSubjectNameHidden">
                        <form class="layui-form" action="">
                            <div class="layui-form-item">
                                <div class="layui-inline">
                                    <div class="layui-input-inline" style="width: 300px;">
                                        <select name="modules" lay-verify="required" lay-filter="addSubjectName" lay-search="" id="addSubjectName"  placeholder="请选择">
                                            <option value="0">直接选择或搜索选择</option>
                                            <option value="1">差旅费</option>
                                            <option value="2">差旅补助</option>
                                            <option value="3">招待费</option>
                                            <option value="4">办公费</option>
                                            <option value="5">劳保费</option>
                                            <option value="6">员工福利费</option>
                                            <option value="7">员工保险费</option>
                                            <option value="8">员工培训费</option>
                                            <option value="9">车辆费用</option>
                                            <option value="10">器具</option>
                                            <option value="11">固定资产</option>
                                            <option value="12">房租</option>
                                            <option value="13">其他</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td>
                        <textarea name="" id="addRemark" cols="30" rows="5" style="width: 300px;text-indent: 10px;"></textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;padding-top: 20px;box-sizing: border-box;">
                        <button class="layui-btn" onclick="insertOk()">确认</button>
                        <button class="layui-btn layui-btn-normal" onclick="cancel()">取消</button>
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <iframe id="iframe1" name="iframe1" style="display:none;"></iframe>
    <%--修改--%>
    <form  method="post" target="iframe2" class="layui-form" action="">
        <div class="updateReimbursement">
            <input type="hidden" id="updReimbursementId">
            <table>
                <tr>
                    <td><samp style="color: red;">*</samp>日期</td>
                    <td>
                        <input type="text" id="updTest5Hidden" disabled="disabled" style="width: 300px;height: 38px;margin-top: 20px;margin-bottom: 20px;text-indent: 10px;">
                    </td>
                </tr>
                <tr>
                    <td><span style="float: right;margin-bottom: 10px;">部门</span><samp style="color: red;float: right;">*</samp></td>
                    <td>
                        <input type="hidden" id="updDepartNameHidden">
                        <input type="hidden" id="updDepartHidden">
                        <div class="layui-form-item" style="margin-bottom: 0px;">
                            <div class="layui-inline">
                                <div class="layui-input-inline" style="width: 300px;">
                                    <select name="modules" lay-verify="required" lay-filter="updDepartName" lay-search="" id="updDepartName" disabled="disabled">
                                    </select>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>报销人</td>
                    <td>
                        <input type="text" id="updPeopleName" disabled="disabled"  style="width: 300px;height: 38px;text-indent: 10px;">
                    </td>
                </tr>
                <tr>
                    <td><samp style="color: red;">*</samp>报销内容</td>
                    <td style="padding-top: 10px;">
                        <textarea name="" id="updContent" cols="30" rows="5" style="width: 300px;text-indent: 10px;"></textarea>
                    </td>
                </tr>
                <tr>
                    <td><samp style="color: red;">*</samp>金额</td>
                    <td>
                        <input type="text" id="updMoney" style="width: 300px;height: 38px;margin-top: 20px;margin-bottom: 20px;text-indent: 10px;" onBlur="twoDecimal('updMoney',this.value)";>
                    </td>
                </tr>
                <tr>
                    <td><span style="float: right;margin-bottom: 10px;">科目</span><samp style="color: red;float: right;">*</samp></td>
                    <td>
                        <input type="hidden" id="updSubjectNameHidden">
                        <form class="layui-form" action="">
                            <div class="layui-form-item">
                                <div class="layui-inline">
                                    <div class="layui-input-inline" style="width: 300px;">
                                        <select name="modules" lay-verify="required" lay-filter="updSubjectName" lay-search="" id="updSubjectName">
                                            <option value="0">直接选择或搜索选择</option>
                                            <option value="1">差旅费</option>
                                            <option value="2">差旅补助</option>
                                            <option value="3">招待费</option>
                                            <option value="4">办公费</option>
                                            <option value="5">劳保费</option>
                                            <option value="6">员工福利费</option>
                                            <option value="7">员工保险费</option>
                                            <option value="8">员工培训费</option>
                                            <option value="9">车辆费用</option>
                                            <option value="10">器具</option>
                                            <option value="11">固定资产</option>
                                            <option value="12">房租</option>
                                            <option value="13">其他</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                    <td>
                        <textarea name="" id="updRemark" cols="30" rows="5" style="width: 300px;text-indent: 10px;"></textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;padding-top: 20px;box-sizing: border-box;">
                        <shiro:hasPermission name="报销项目部">
                            <button class="layui-btn" onclick="updateOk()">确认</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="报销管理员">
                            <button class="layui-btn" onclick="updateOk()">确认</button>
                        </shiro:hasPermission>
                        <button class="layui-btn layui-btn-normal" onclick="cancel()">取消</button>
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <iframe id="iframe2" name="iframe2" style="display:none;"></iframe>
    <%--审核--%>
    <div class="examineDiv">
        <input type="hidden" id="examineId">
        <table>
            <tr>
                <td>审核意见：</td>
                <td>
                    <textarea name="" id="examineOpinion" cols="30" rows="5" style="width: 300px;text-indent: 10px;"></textarea>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center;padding-top: 20px;box-sizing: border-box;">
                    <button class="layui-btn" onclick="examineOK()">确认</button>
                    <button class="layui-btn layui-btn-normal" onclick="examineNO()">驳回</button>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
