<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/wags.js"></script>
    <script type="text/javascript" src="../js/week/alert.js"></script>
    <style>
        *{
            margin: 0;
            padding: 0;
        }
        .warp{
            padding: 10px;
            box-sizing: border-box;
        }
        body::-webkit-scrollbar{
            display: none;
        }
        .updateFinance table,.detailFinance table{
            width: 99%;
        }
        .updateFinance table tr,.detailFinance table tr{
            line-height: 50px;
        }
        .updateFinance table tr td:first-of-type,.updateFinance table tr td:nth-child(3),.detailFinance table tr td:first-of-type,.detailFinance table tr td:nth-child(3){
            text-align: right;
        }
        .updateFinance table tr td input,.detailFinance table tr td input{
            width: 98%;
            outline: none;
            height: 47px;
            border: none;
            border-bottom: 1px solid #e2e2e2;
            margin-left: 1px;
            padding-left: 8px;
            box-sizing: border-box;
        }
        .updateFinance,.detailFinance{
            display: none;
        }
        .copyDiv{
            display: none;
            padding-top: 20px;
            box-sizing: border-box;
        }
        .layui-table-body::-webkit-scrollbar {
            display:none
        }
        #calculationButton{
            display: none;
        }
        .loading{
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            background-color: rgb(0, 0, 0);
            opacity: 0.8;
            text-align: center;
            padding-top: 300px;
            z-index: 9999999;
            display: none;
        }
        .loading div{
            animation:turn 1s linear infinite;
        }
        @keyframes turn{
            0%{-webkit-transform:rotate(0deg);}
            25%{-webkit-transform:rotate(90deg);}
            50%{-webkit-transform:rotate(180deg);}
            75%{-webkit-transform:rotate(270deg);}
            100%{-webkit-transform:rotate(360deg);}
        }
    </style>
</head>
<body>
<div class="warp">
    <div class="top">
        <input type="hidden" id="monthStart">
        <button class="layui-btn layui-btn-sm" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;" onclick="monthUpBtn()" >&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;</button>
        <div class="layui-inline" style="margin-bottom: 10px;float:left;">
            <div class="layui-input-inline">
                <input type="text" class="layui-input" id="test15" placeholder="年月">
            </div>
        </div>
        <button class="layui-btn layui-btn-sm" style="float: left;height: 38px;margin-left: 5px;" onclick="monthDownBtn()" >&nbsp;&nbsp;&gt;&gt;&nbsp;&nbsp;</button>
        <shiro:hasPermission name='工资修改'>
            <button class="layui-btn layui-btn-normal layui-btn-sm" style="margin-left: 50px;float:left;height: 38px;" onclick="copyWags()" id="copyButton">复制此月数据</button>
        </shiro:hasPermission>
        <shiro:hasPermission name='工资修改'>
            <button class="layui-btn layui-btn-warm layui-btn-sm" style="margin-left: 50px;float:left;height: 38px;" onclick="calculationWags()" id="calculationButton">本月工资核算</button>
        </shiro:hasPermission>
        <%--        <shiro:hasPermission name='工资修改'>--%>
        <button class="layui-btn layui-btn-warm layui-btn-sm" style="margin-left: 50px;float:left;height: 38px;" onclick="calculationWags()" id="calculationButton">本月工资核算</button>
        <%--        </shiro:hasPermission>--%>
        <div style="clear: both"></div>
        <div class="copyDiv">
            <input type="hidden" id="monthEnd">
            <div class="layui-inline" style="margin-bottom: 25px;">
                <label class="layui-form-label">复制周期</label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input" id="test16" placeholder="年月">
                </div>
            </div>
            <button class="layui-btn layui-btn-normal layui-btn-sm" style="margin-left: 37%;transform: translate(-37%, 0px);" onclick="copyOk()">确定</button>&nbsp;
            <button class="layui-btn layui-btn-normal layui-btn-sm" onclick="cancel()">取消</button>
        </div>
    </div>
    <div class="container">
        <input type="hidden" id="wagsId">
        <input type="hidden" id="userId">
        <table id="demo" lay-filter="test"></table>
        <script type="text/html" id="barDemoPost">
            <span>{{= d.wagesPostName}} {{= d.postGradeName}}</span>
        </script>
        <script type="text/html" id="barDemo">
            <a class="layui-btn layui-btn-xs detailBtn" lay-event="detail">详情</a>
            <shiro:hasPermission name='工资修改'>
                <a class="layui-btn layui-btn-xs layui-btn-normal editBtn" lay-event="edit">编辑</a>
            </shiro:hasPermission>
        </script>
    </div>
    <div class="updateFinance">
        <input type="hidden" id="dateHidden">

        <table style="margin-top: 20px;" cellspacing="0">
            <thead>
            <tr>
                <th colspan="4" style="font-size: 24px;">工资信息</th>
            </tr>
            </thead>
            <tbody class="financeTbody">
            <tr>
                <td style="width: 16%;">编号：</td>
                <td><input type="text" class="userNumber" id="userNumber" name="userNumber" readonly></td>
                <td>姓名：</td>
                <td><input type="text" class="employeeName" id="employeeName" name="employeeName" readonly></td>
            </tr>
            <tr>
                <td>岗位：</td>
                <td>
                    <form class="layui-form" action="" style="width: 100%;">
                        <div class="layui-inline" style="width: 100%;">
                            <div class="layui-input-inline" style="width: 99%;">
                                <input type="hidden" id="selPostNameHidden">
                                <select name="modules" lay-verify="required" lay-filter="selPostName" lay-search="" id="selPostName">
                                </select>
                            </div>
                        </div>
                    </form>
                </td>
                <td>职级：</td>
                <td>
                    <form class="layui-form" action="" style="width: 100%;">
                        <div class="layui-inline" style="width: 100%;">
                            <div class="layui-input-inline" style="width: 99%;">
                                <input type="hidden" id="selPostLevelNameHidden">
                                <select name="modules" lay-verify="required" lay-filter="selPostLevelName" lay-search="" id="selPostLevelName">
                                </select>
                            </div>
                        </div>
                    </form>
                </td>
            </tr>
            <tr style="line-height: 80px;font-size: 20px;font-weight: bold;">
                <td colspan="4" style="text-align: center;">应发金额</td>
            </tr>
            <tr>
                <td>岗位工资：</td>
                <td><input type="text" class="basePay" id="basePay" name="basePay" placeholder="0.00"  onchange="twoDecimal('basePay',this.value);" readonly></td>
                <td>职级工资：</td>
                <td><input type="text" class="positionSalary" id="positionSalary" name="positionSalary" onchange="twoDecimal('positionSalary',this.value);" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>绩效基数：</td>
                <td><input type="number" class="performanceBase" id="performanceBase" name="performanceBase" placeholder="0.00" onchange="twoDecimal('performanceBase',this.value);" readonly></td>
                <td>绩效系数：</td>
                <td><input type="text" class="performanceCoefficient" id="performanceCoefficient" name="performanceCoefficient" placeholder="0.000" onBlur="threeDecimal('performanceCoefficient',this.value);" readonly></td>
            </tr>
            <tr>
                <td>绩效工资：</td>
                <td><input type="text" class="meritPay" id="meritPay" name="meritPay" placeholder="0.00" onchange="twoDecimal('meritPay',this.value);" readonly></td>
                <td>其他：</td>
                <td><input type="text" class="other" id="other" name="other" placeholder="0.00" onBlur="twoDecimal('other',this.value);sumWages();" ></td>
            </tr>
            <tr>
                <td style="color: red;">工资小计：</td>
                <td><input type="number" class="wageSubtotal" id="wageSubtotal" name="wageSubtotal" placeholder="0.00" onchange="twoDecimal('wageSubtotal',this.value);" readonly></td>
                <td style="color: red;">应发工资：</td>
                <td><input type="number" class="wagesPayable" id="wagesPayable" name="wagesPayable" placeholder="0.00" onchange="twoDecimal('wagesPayable',this.value);" readonly></td>
            </tr>
            <tr>
                <td>餐补：</td>
                <td><input type="text" class="foodSupplement" id="foodSupplement" name="foodSupplement" placeholder="0.00"  onBlur="twoDecimal('foodSupplement',this.value);" readonly></td>
                <td>高温补贴：</td>
                <td><input type="text" class="highTemperatureSubsidy" id="highTemperatureSubsidy" name="highTemperatureSubsidy" placeholder="0.00" onBlur="twoDecimal('highTemperatureSubsidy',this.value);sumWages();"></td>
            </tr>
            <tr>
                <td style="color: red;">补贴小计：</td>
                <td><input type="number" class="subTotalOfSubsidies" id="subTotalOfSubsidies" name="subTotalOfSubsidies" placeholder="0.00" onchange="twoDecimal('subTotalOfSubsidies',this.value);" readonly></td>
                <td style="color: red;">应发合计：</td>
                <td><input type="number" class="totalPayable" id="totalPayable" name="totalPayable" placeholder="0.00" onchange="twoDecimal('totalPayable',this.value);" readonly></td>
            </tr>
            <tr style="line-height: 80px;font-size: 20px;font-weight: bold;">
                <td colspan="4" style="text-align: center;">应扣金额</td>
            </tr>
            <tr>
                <td>养老保险：</td>
                <td><input type="text" class="endowmentInsurance" id="endowmentInsurance" name="endowmentInsurance" placeholder="0.00" onBlur="twoDecimal('endowmentInsurance',this.value);sumWages();"></td>
                <td>医疗保险：</td>
                <td><input type="text" class="medicalInsurance" id="medicalInsurance" name="medicalInsurance" placeholder="0.00" onBlur="twoDecimal('medicalInsurance',this.value);sumWages();"></td>
            </tr>
            <tr>
                <td>公积金：</td>
                <td><input type="text" class="accumulationFund" id="accumulationFund" name="accumulationFund" placeholder="0.00" onBlur="twoDecimal('accumulationFund',this.value);sumWages();"></td>
                <td>失业金：</td>
                <td><input type="text" class="unemploymentBenefits" id="unemploymentBenefits" name="unemploymentBenefits" placeholder="0.00" onBlur="twoDecimal('unemploymentBenefits',this.value);sumWages();"></td>
            </tr>
            <tr>
                <td>工会费：</td>
                <td><input type="text" class="unionFees" id="unionFees" name="unionFees" placeholder="0.00" onBlur="twoDecimal('unionFees',this.value);sumWages();"></td>
                <td>其他扣款：</td>
                <td><input type="text" class="otherDeductions" id="otherDeductions" name="otherDeductions" placeholder="0.00" onBlur="twoDecimal('otherDeductions',this.value);sumWages();"></td>
            </tr>
            <tr>
                <td style="color: red;">扣款合计：</td>
                <td><input type="text" class="totalDeduction" id="totalDeduction" name="totalDeduction" placeholder="0.00"  onchange="twoDecimal('totalDeduction',this.value);" readonly></td>
            </tr>
            <tr style="line-height: 80px;font-size: 20px;font-weight: bold;">
                <td colspan="4" style="text-align: center;">其他</td>
            </tr>
            <tr>
                <td style="color: red;">计税合计：</td>
                <td><input type="number" class="totalTax" id="totalTax" name="totalTax" placeholder="0.00" onchange="twoDecimal('totalTax',this.value);" readonly></td>
                <td>六项专项扣除金额：</td>
                <td><input type="text" class="sixSpecialDeductions" id="sixSpecialDeductions" name="sixSpecialDeductions" placeholder="0.00" onBlur="twoDecimal('sixSpecialDeductions',this.value);sumWages()"></td>
            </tr>
            <tr>
                <td style="color: red;">个调税：</td>
                <td><input type="text" class="individualTaxAdjustment" id="individualTaxAdjustment" name="individualTaxAdjustment" placeholder="0.00"  onchange="twoDecimal('individualTaxAdjustment',this.value);" readonly></td>
            </tr>
            <tr>
                <td style="color: dodgerblue;font-weight: bold;">实发工资：</td>
                <td colspan="3"><input type="text" class="netSalary" id="netSalary" name="netSalary" placeholder="0.00" onchange="twoDecimal('netSalary',this.value);" readonly></td>
            </tr>
            <tr>
                <td>备注：</td>
                <td colspan="3"><input type="text" class="remark" id="remark" name="remark"></td>
            </tr>
            <tr style="line-height: 80px;">
                <td colspan="4" style="text-align: center;">
                    <button class="layui-btn layui-btn-normal" onclick="updFinance()">确定</button>
                    <button  style="margin-left: 5%" class="layui-btn layui-btn-normal" onclick="cancel()">取消</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="detailFinance">
        <table style="margin-top: 20px;" cellspacing="0">
            <thead>
            <tr>
                <th colspan="4" style="font-size: 24px;">工资信息</th>
            </tr>
            </thead>
            <tbody class="financeTbody">
            <tr>
                <td style="width: 16%;">编号：</td>
                <td><input type="text" class="userNumber" name="userNumber" readonly></td>
                <td>姓名：</td>
                <td><input type="text" class="employeeName" name="employeeName" readonly></td>
            </tr>
            <tr>
                <td style="width: 16%;">岗位：</td>
                <td><input type="text" class="userPost" name="userPost" readonly></td>
                <td>职级：</td>
                <td><input type="text" class="postLevel" name="postLevel" readonly></td>
            </tr>
            <tr>
                <td>岗位工资：</td>
                <td><input type="number" class="basePay" name="basePay" placeholder="0.00" readonly></td>
                <td>职级工资：</td>
                <td><input type="number" class="positionSalary" name="positionSalary" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>绩效基数：</td>
                <td><input type="number" class="performanceBase" name="performanceBase" placeholder="0.00" readonly></td>
                <td>绩效系数：</td>
                <td><input type="number" class="performanceCoefficient" name="performanceCoefficient" placeholder="0.000" readonly></td>
            </tr>
            <tr>
                <td>绩效工资：</td>
                <td><input type="number" class="meritPay" name="meritPay" placeholder="0.00" readonly></td>
                <td>其他：</td>
                <td><input type="number" class="other" name="other" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>工资小计：</td>
                <td><input type="number" class="wageSubtotal" name="wageSubtotal" placeholder="0.00" readonly></td>
                <td>应发工资：</td>
                <td><input type="text" class="wagesPayable" name="wagesPayable" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>餐补：</td>
                <td><input type="number" class="foodSupplement" name="foodSupplement" placeholder="0.00" readonly></td>
                <td>高温补贴：</td>
                <td><input type="text" class="highTemperatureSubsidy" name="highTemperatureSubsidy" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>补贴小计：</td>
                <td><input type="text" class="subTotalOfSubsidies" name="subTotalOfSubsidies" placeholder="0.00" readonly></td>
                <td>应发合计：</td>
                <td><input type="text" class="totalPayable" name="totalPayable" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>养老保险：</td>
                <td><input type="number" class="endowmentInsurance" name="endowmentInsurance" placeholder="0.00" readonly></td>
                <td>医疗保险：</td>
                <td><input type="number" class="medicalInsurance" name="medicalInsurance" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>公积金：</td>
                <td><input type="number" class="accumulationFund" name="accumulationFund" placeholder="0.00" readonly></td>
                <td>失业金：</td>
                <td><input type="number" class="unemploymentBenefits" name="unemploymentBenefits" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>工会费：</td>
                <td><input type="number" class="unionFees" name="unionFees" placeholder="0.00" readonly></td>
                <td>其他扣款：</td>
                <td><input type="number" class="otherDeductions" name="otherDeductions" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>扣款合计：</td>
                <td><input type="text" class="totalDeduction" name="totalDeduction" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>计税合计：</td>
                <td><input type="text" class="totalTax" name="totalTax" placeholder="0.00" readonly ></td>
                <td>六项专项扣除金额：</td>
                <td><input type="number" class="sixSpecialDeductions" name="sixSpecialDeductions" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>个调税：</td>
                <td><input type="number" class="individualTaxAdjustment" name="individualTaxAdjustment" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>实发工资：</td>
                <td colspan="3"><input type="text" class="netSalary" name="netSalary" placeholder="0.00" readonly></td>
            </tr>
            <tr>
                <td>备注：</td>
                <td colspan="3"><input type="text" class="remark" name="remark" readonly></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<div class="loading">
    <div style="width: 50px;margin: 0 auto;">
        <i class="layui-icon layui-icon-loading" style="font-size: 60px; color: #fff;"></i>
    </div>
</div>
</body>
</html>
