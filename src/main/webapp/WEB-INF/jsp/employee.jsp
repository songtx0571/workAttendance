<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<html>
<head>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/employee.js"></script>
    <link rel="stylesheet" href="../js/font/css/font-awesome.css">
</head>
<style type="text/css">
    * {
        margin: 0;
        padding: 0;
        font-family: "PingFang SC", "Lantinghei SC", "Microsoft YaHei", "HanHei SC", "Helvetica Neue", "Open Sans", Arial, "Hiragino Sans GB", "微软雅黑", STHeiti, "WenQuanYi Micro Hei", SimSun, sans-serif;
    }

    .warp {
        width: 100%;
        height: 100%;
        overflow-x: auto;
        overflow-y: scroll;
    }

    .warp::-webkit-scrollbar {
        display: none;
    }

    .bodyHeader {
        width: 100%;
    }

    .bodyContent {
        width: 100%;
        height: 90%;

    }

    .bodyContentHead {
        margin: 0 10px;
        height: 100%;
        text-align: center;
    }

    .updateUser {
        width: 100%;
        padding: 5px;
        color: #333;
        overflow: scroll;
        background: #fff;
        display: none;
    }

    .updateUser::-webkit-scrollbar {
        display: none;
    }

    .updateUser table {
        border: 1px solid #ddd;
        width: 100%;
    }

    .updateUser table tr {
        height: 50px;
        font-size: 15px;
        width: 100%;
    }

    .updateUser table th {
        text-align: center;
        border: 1px solid #ddd;
        font-size: 20px;
    }

    .updateUser table tr td {
        border: 1px solid #ddd;
        width: 38%;
    }

    .updateUser table tr td input {
        width: 98%;
        outline: none;
        height: 47px;
        border: none;
        margin-left: 1px;
        padding-left: 8px;
        box-sizing: border-box;
    }

    .updateUser table tr td:first-of-type, .updateUser table tr td:nth-child(3) {
        text-align: right;
        width: 146px;
    }

    .updateUser input[type=button], .bodyHeader input[type=button] {
        width: 100px;
        height: 35px;
        outline: none;
        font-size: 18px;
        background-color: #1E9FFF;
        color: #fff;
        border: none;
    }

    .bodyHeader input[type=text] {
        width: 500px;
        height: 50px;
        float: left;
        font-size: 20px;
        padding-left: 8px;
        box-sizing: border-box;
    }

    .bodyContent #testAdd .layui-layer-page #updateUser {
        width: 98%;
        padding: 10px;
    }

    .bodyContent #testAdd .layui-layer-page #updateUser .layui-anim-upbit {
        height: 150px;
    }

    body::-webkit-scrollbar {
        display: none;
    }
</style>
<body>
<div class="warp">
    <div class="bodyHeader">
        <div style="width: 750px;margin: 10px auto;">
            <input type="text" placeholder="请输入员工编号/姓名/地址" id="searchName">
            <input type="button" value="搜索" style="height: 50px;" id="searchBtn">
            <a onclick="showEmployeeList('noDistribution')" style="font-size: 17px;float: right;line-height: 50px;cursor: pointer;">未分配人员&nbsp;&nbsp;&nbsp;<span class="layui-badge" id="peopleNum"></span></a>
        </div>
    </div>
    <%--内容主体--%>
    <div class="bodyContent">
        <div id="bodyContentHead" class="bodyContentHead">
            <table id="demo" lay-filter="test"></table>
            <script type="text/html" id="barDemo11">
                <a class="layui-btn layui-btn-xs" lay-event="edit" id="showUpdataUser">编辑</a>
            </script>
        </div>
        <form id="testAdd" method="post" target="iframe1" class="layui-form" action="">
            <div class="updateUser">
                <input type="hidden" id="userIdHidden">
                <table>
                    <thead>
                    <tr>
                        <th colspan="4">员工信息</th>
                    </tr>
                    </thead>
                    <tbody class="userTbody">
                    <tr>
                        <td style="width: 16%;">编号：</td>
                        <td><input type="text" id="userNumber" name="userNumber" readonly></td>
                        <td>姓名：</td>
                        <td><input type="text" id="name" name="name" readonly></td>
                    </tr>
                    <tr>
                        <td>性别：</td>
                        <td><input type="text" id="sex" name="sex" readonly></td>
                        <td>入职日期：</td>
                        <td><input type="text" id="entryDate" name="entryDate" readonly></td>
                    </tr>
                    <tr>
                        <td>部门：</td>
                        <td>
                            <input type="text" id="departmentName" name="departmentName" readonly>
                        </td>
                        <td>岗位：</td>
                        <td>
                            <input type="text" id="postName" name="postName" readonly>
                        </td>
                    </tr>
                    <tr>
                        <td>电话号码：</td>
                        <td><input type="tel" id="phone" name="phone" readonly></td>
                        <td>邮箱：</td>
                        <td><input type="text" id="email" name="email" readonly></td>
                    </tr>
                    <tr>
                        <td>证书1：</td>
                        <td>
                            <input type="text" id="credentials1" name="credentials1">
                        </td>
                        <td>证书2：</td>
                        <td>
                            <input type="text" id="credentials2" name="credentials2">
                        </td>
                    </tr>
                    <tr>
                        <td>证书3：</td>
                        <td>
                            <input type="text" id="credentials3" name="credentials3">
                        </td>
                        <td>身份证：</td>
                        <td><input type="text" id="idNumber" name="idNumber"></td>
                    </tr>
                    <tr>
                        <td>住址：</td>
                        <td><input type="text" id="address" name="address"></td>
                        <td>人事异动：</td>
                        <td>
                            <div class="layui-form-item" style="margin-bottom: 0px">
                                <div class="layui-inline">
                                    <div class="layui-input-inline">
                                        <select name="isChanged" lay-verify="required" lay-filter="isChanged"
                                                lay-search="" id="isChanged">
                                            <option value="0">正常</option>
                                            <option value="1">当月入职</option>
                                            <option value="2">当月离职(正常)</option>
                                            <option value="3">试用期</option>
                                            <option value="4">试用转正</option>
                                            <option value="5">派遣调整</option>
                                            <option value="6">部门调整</option>
                                            <option value="7">薪酬调整</option>
                                            <option value="8">当月离职(试用期)</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" id="isChangedHidden" name="isChangedHidden">
                        </td>
                    </tr>
                    <tr>
                        <td>应急联系人：</td>
                        <td><input type="text" id="emergency" name="emergency"></td>
                        <td>应急电话：</td>
                        <td><input type="number" id="emergencyTel" name="emergencyTel"></td>
                    </tr>
                    <tr>
                        <td>衣服尺寸：</td>
                        <td><input type="text" id="closhe" name="closhe"></td>
                        <td>安全帽编号：</td>
                        <td><input type="text" id="hat" name="hat"></td>

                    </tr>
                    <tr>
                        <td>劳务派遣：</td>
                        <td>
                            <div class="layui-form-item" style="margin-bottom: 0px">
                                <div class="layui-inline">
                                    <div class="layui-input-inline">
                                        <select name="laowupaiqian" lay-verify="required" lay-filter="laowupaiqian"
                                                lay-search="" id="laowupaiqian">
                                            <option value="是">是</option>
                                            <option value="否">否</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" id="laowupaiqianHidden" name="laowupaiqianHidden">
                        </td>
                        <td>绩效管理：</td>
                        <td>
                            <div class="layui-form-item" style="margin-bottom: 0px">
                                <div class="layui-inline">
                                    <div class="layui-input-inline">
                                        <select name="managerName" lay-verify="required" lay-filter="managerName"
                                                lay-search="" id="managerName">
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" id="managerHidden" name="managerHidden">
                        </td>
                    </tr>
                    <tr>
                        <td>开户行：</td>
                        <td><input type="text" id="bank" name="bank"></td>
                        <td>银行卡号：</td>
                        <td><input type="number" id="card" name="card"></td>
                    </tr>
                    <tr>
                        <td>学历：</td>
                        <td><input type="text" id="education" name="education"></td>
                        <td>签名：</td>
                        <td><input type="text" id="sign" name="sign" placeholder="最长输入30字！"></td>
                    </tr>
                    <tr>
                        <td>备注：</td>
                        <td colspan="3"><input type="text" id="remark" name="remark"></td>
                    </tr>
                    </tbody>
                </table>
                <div style="text-align: center;margin: 15px 0px;">
                    <shiro:hasPermission name='基本信息修改'>
                        <input type="button" value="确定" onclick="updUserInfo()">
                    </shiro:hasPermission>
                    <input style="margin-left: 5%" type="button" value="取消" onclick="cancel()">
                </div>
            </div>
        </form>
        <iframe id="iframe1" name="iframe1" style="display:none;"></iframe>
    </div>
</div>
</body>
</html>