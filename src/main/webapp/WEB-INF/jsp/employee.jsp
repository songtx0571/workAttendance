<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/employee.js"></script>
    <link rel="stylesheet" href="../js/font/css/font-awesome.css">
</head>
<style type="text/css">
    *{
        margin: 0;
        padding: 0;
        font-family: "PingFang SC","Lantinghei SC","Microsoft YaHei","HanHei SC","Helvetica Neue","Open Sans",Arial,"Hiragino Sans GB","微软雅黑",STHeiti,"WenQuanYi Micro Hei",SimSun,sans-serif;
    }
    .bodyHeader{
        width: 100%;
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
    .employeeTable{
        margin:auto;
        border: 1px solid #00bbee;
        border-collapse:collapse
    }
    .updateUser{
        width: 663px;
        /*height: 50%;*/
        padding: 10px;
        color: #333;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -25%);
        overflow: scroll;
        background: #fff;
        display: none;
    }
    .updateUser::-webkit-scrollbar{
        display:none;
    }
    .updateUser table{
        border: 1px solid #ddd;
        width: 100%;
    }
    .updateUser table tr {
        height: 50px;
        font-size: 15px;
    }
    .updateUser table th{
        text-align: center;
        border: 1px solid #ddd;
        font-size: 20px;
    }
    .updateUser table tr td{
        border: 1px solid #ddd;
        width: 38%;
    }
    .updateUser table tr td input{
        width: 98%;
        outline: none;
        height: 47px;
        border: none;
        margin-left: 1px;
        padding-left: 8px;
        box-sizing: border-box;
    }
    .updateUser table tr td:first-of-type,.updateUser table tr td:nth-child(3){
        text-align: right;
        width: 146px;
    }
    .updateUser input[type=button],.bodyHeader input[type=button]{
        width: 20%;
        height: 35px;
        outline: none;
        font-size: 18px;
        background-color: #1E9FFF;
        color: #fff;
        margin-right:  5%;
        border: none;
        border-radius: 8px;
    }
    .bodyHeader input[type=text]{
        width: 70%;
        height: 50px;
        margin-right: 5%;
        float: left;
        font-size: 20px;
        padding-left: 8px;
        box-sizing: border-box;
    }
</style>
<body>
<div class="bodyHeader">
    <div style="width: 50%;height: 35px;margin: 20px auto;">
        <input type="text" placeholder="请输入员工编号/姓名/地址" id="searchName">
        <input type="button" value="搜索" style="height: 50px;" onclick="searchBtn()">
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
    <form  id="testAdd" method="post" target="iframe1" class="layui-form" action="">
        <div class="updateUser">
            <input type="hidden" id="userIdHidden">
            <table>
                <thead>
                <tr>
                    <th colspan="4">员工信息
                        <span style="display: inline-block;margin-left: 15px" class="doubleUpBtn1" onclick="doubleUp1()"><i class='fa fa-angle-double-up fa-lg'></i></span>
                        <span style="display: inline-block;margin-left: 15px;display: none;" class="doubleDownBtn1" onclick="doubleDown1()"><i class='fa fa-angle-double-down fa-lg'></i></span>
                    </th>
                </tr>
                </thead>
                <tbody class="userTbody">
                <tr>
                    <td>编号：</td>
                    <td><input type="text" id="userNumber" name="userNumber" disabled></td>
                    <td>姓名：</td>
                    <td><input type="text" id="name" name="name" disabled></td>
                </tr>
                <tr>
                    <td>性别：</td>
                    <td><input type="text" id="sex" name="sex" disabled></td>
                    <td>入职日期：</td>
                    <td><input type="text" id="entryDate" name="entryDate" disabled></td>
                </tr>
                <tr>
                    <td>部门：</td>
                    <td><input type="text" id="departmentName" name="departmentName" disabled></td>
                    <td>岗位：</td>
                    <td><input type="text" id="postName" name="postName" disabled></td>
                </tr>
                <tr>
                    <td>电话号码：</td>
                    <td><input type="tel" id="phone" name="phone" disabled></td>
                    <td>证书1：</td>
                    <td><input type="text" id="credentials1" name="credentials1"></td>
                </tr>
                <tr>
                    <td>证书2：</td>
                    <td><input type="text" id="credentials2" name="credentials2"></td>
                    <td>证书3：</td>
                    <td><input type="text" id="credentials3" name="credentials3"></td>
                </tr>
                <tr>
                    <td>身份证：</td>
                    <td><input type="text" id="idNumber" name="idNumber"></td>
                    <td>住址：</td>
                    <td><input type="text" id="address" name="address"></td>
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
                    <td><input type="text" id="laowupaiqian" name="laowupaiqian"></td>
                    <td>学历：</td>
                    <td><input type="text" id="education" name="education"></td>
                </tr>
                <tr>
                    <td>备注：</td>
                    <td colspan="3"><input type="text" id="remark" name="remark"></td>
                </tr>
                </tbody>
            </table>
            <table style="margin-top: 20px;">
                <thead>
                <tr>
                    <th colspan="4">工资信息
                        <span style="display: inline-block;margin-left: 15px" class="doubleUpBtn2" onclick="doubleUp2()"><i class='fa fa-angle-double-up fa-lg'></i></span>
                        <span style="display: inline-block;margin-left: 15px;display: none;" class="doubleDownBtn2" onclick="doubleDown2()"><i class='fa fa-angle-double-down fa-lg'></i></span>
                    </th>
                </tr>
                </thead>
                <tbody class="payTbody">
                <tr>
                    <td>开户行：</td>
                    <td><input type="text" id="bank" name="bank"></td>
                    <td>银行卡号：</td>
                    <td><input type="number" id="card" name="card"></td>
                </tr>
                <tr>
                    <td>待遇标准：</td>
                    <td><input type="text" id="wages" name="wages"></td>
                    <td>基本工资：</td>
                    <td><input type="text" id="basicwages" name="basicwages"></td>
                </tr>
                <tr>
                    <td>绩效工资：</td>
                    <td><input type="text" id="meritpay" name="meritpay"></td>
                    <td>绩效管理人：</td>
                    <td><input type="text" id="managerName" name="managerName" disabled></td>
                </tr>
                </tbody>
            </table>
            <div style="text-align: center;margin: 15px 0px;">
                <input type="button" value="确定" onclick="updUserInfo()">
                <input  style="margin-left: 5%" type="button" value="取消" onclick="cancel()">
            </div>
        </div>
    </form>
    <iframe id="iframe1" name="iframe1" style="display:none;"></iframe>
</div>
</body>
</html>