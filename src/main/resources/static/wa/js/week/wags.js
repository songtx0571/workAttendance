var path = "";
var date = new Date();
var year = date.getFullYear();
var mouth = date.getMonth();
var day = date.getDate();
var upBtnYear = year;
var upBtnMonth = mouth;
$(function(){
    showMonth();
    if (mouth == 12) {
        mouth = 1;
        year = year + 1;
    }
    if (mouth == 0) {
        mouth = 12;
        year = year - 1;
    }
    if (mouth < 10) {
        mouth = "0"+mouth;
    }
    showWagsList(year+"-"+mouth);
});
//点击查看上个月数据
function monthUpBtn() {
    upBtnMonth --;
    if (upBtnMonth < "1"){
        upBtnYear --;
        upBtnMonth = 12;
    }
    if (upBtnMonth<10){
        upBtnMonth = "0"+upBtnMonth
    }
    var monthUp = upBtnYear + "-" + upBtnMonth;
    $("#monthStart").val(monthUp);
    showWagsList(monthUp);
    layui.use('form', function(){
        var form = layui.form;
        $("#test15").val(monthUp);
        form.render('select');
        form.render(); //更新全部
    });
}
//点击查看下个月数据
function monthDownBtn() {
    if (upBtnMonth > mouth  && upBtnYear >= year) {
        layui.use('layer', function() {
            layer.msg('<p style="width: 100px;text-align: center;">无数据</p>', {
                time: 1000
            });
        });
        return;
    }
    upBtnMonth ++;
    if (upBtnMonth > '12'){
        upBtnYear ++;
        upBtnMonth = 1;
    }
    if (upBtnMonth<10){
        upBtnMonth = "0"+upBtnMonth
    }
    var monthDown = upBtnYear + "-" + upBtnMonth;
    $("#monthStart").val(monthDown);
    showWagsList(monthDown);
    layui.use('form', function(){
        var form = layui.form;
        $("#test15").val(monthDown);
        form.render('select');
        form.render(); //更新全部
    });
}
//显示时间
function showMonth() {
    var initial = upBtnYear +"-"+upBtnMonth;
    $("#monthStart").val(initial);
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询所有数据日期
        //年月选择器
        laydate.render({
            elem: '#test15'
            , type: 'month'
            , trigger: 'click'//呼出事件改成click
            , done: function (value) {
                $("#monthStart").val(value);
                upBtnYear = value.substring(0,4);
                upBtnMonth = value.substr(5,2);
                showWagsList(value);
            }
        });
        laydate.render({
            elem: '#test16'
            , type: 'month'
            , trigger: 'click'//呼出事件改成click
            , done: function (value) {
                $("#monthEnd").val(value);
            }
        });
    })
}
//输入两位小数
function twoDecimal(id,value) {
    var reg=/^\d+(\.\d{0,2})?$/;
    if (reg.test(value)==false){
        var td = value.indexOf(".");
        var tdNumFront = value.substring(0,td);
        var tdNumAfter = value.substr(td,3);
        value = tdNumFront+tdNumAfter;
    }
    $("#"+id).val(value);
}
//输入三位小数
function threeDecimal(id,value) {
    var reg=/^\d+(\.\d{0,3})?$/;
    if (reg.test(value)==false){
        var td = value.indexOf(".");
        var tdNumFront = value.substring(0,td);
        var tdNumAfter = value.substr(td,4);
        value = tdNumFront+tdNumAfter;
    }
    $("#"+id).val(value);
}
function sumWages() {
    //工资小计
    var basePay = parseFloat($("#basePay").val());
    var skillPay = parseFloat($("#skillPay").val());
    var positionSalary = parseFloat($("#positionSalary").val());
    var seniorityWage = parseFloat($("#seniorityWage").val());
    var meritPay = parseFloat($("#meritPay").val());

    var other = parseFloat($("#other").val());
    var wages = basePay + skillPay + positionSalary + seniorityWage + meritPay + other;
    $("#wageSubtotal").val(wages);
    //应发工资
    var performanceCoefficient = parseFloat($("#performanceCoefficient").val());
    var wagesPayable = basePay + skillPay + positionSalary + seniorityWage + other;
    var wagesPayableSum = wagesPayable + meritPay * performanceCoefficient;
    $("#wagesPayable").val(wagesPayableSum);
    //补贴小计
    var foodSupplement = parseFloat($("#foodSupplement").val());
    var highTemperatureSubsidy = Number($("#highTemperatureSubsidy").val());
    var subTotalOfSubsidies = foodSupplement + parseFloat(highTemperatureSubsidy);
    $("#subTotalOfSubsidies").val(subTotalOfSubsidies);
    //应发合计
    var wagesPayable = $("#wagesPayable").val();
    var subTotalOfSubsidies1 = $("#subTotalOfSubsidies").val();
    $("#totalPayable").val(Number(wagesPayable)+Number(subTotalOfSubsidies1));
    //扣款合计
    var endowmentInsurance = parseFloat($("#endowmentInsurance").val());
    var medicalInsurance = parseFloat($("#medicalInsurance").val());
    var accumulationFund = parseFloat($("#accumulationFund").val());
    var unemploymentBenefits = parseFloat($("#unemploymentBenefits").val());
    var unionFees = parseFloat($("#unionFees").val());
    var otherDeductions = parseFloat($("#otherDeductions").val());
    var deduction = endowmentInsurance + medicalInsurance + accumulationFund + unemploymentBenefits + unionFees + otherDeductions;
    deduction = deduction.toFixed(2)
    $("#totalDeduction").val(deduction);
    //计税合计
    var totalTax = parseFloat($('#totalPayable').val())-parseFloat($("#totalDeduction").val());
    totalTax = totalTax.toFixed(2);
    $("#totalTax").val(totalTax);
    $.ajax({
        type: "post",
        url: path + "/wa/wags/taxation",
        data: {
            currentIncomeTax:$("#totalPayable").val(),
            other:$("#other").val(),
            deductionOfExpenses:'5000.00',
            employeeId:$("#userId").val(),
            month: $("#monthStart").val(),
            fiveInsurancesAndOneFund:$("#totalDeduction").val(),
            totalTax:$("#totalTax").val(),
            id:$("#wagsId").val(),
            specialAdditionalDeduction:$("#sixSpecialDeductions").val()
        },
        dataType: "json",
        success: function(data){
            var individualTaxAdjustment = data.tax;
            individualTaxAdjustment = Math.floor(individualTaxAdjustment * 100) / 100;
            $("#individualTaxAdjustment").val(individualTaxAdjustment);
            var netSalary = data.netSalary;
            netSalary = Math.floor(netSalary * 100) / 100;
            $("#netSalary").val(netSalary);
        }
    });
}
//查询数据
function showWagsList(m){
    var top = $(".top").css("height");
    var win = $(window).height();
    var tp = top.indexOf("p");
    var topHeight = top.substring(0,tp);
    var height = (win-topHeight-20)-20;
    $("#test15").val(m)
    layui.use('table', function(){
        var table = layui.table;
        table.render({
            elem: '#demo'
            ,height: height
            ,url: path + '/wa/wags/getWagsList?month='+ m //数据接口
            ,toolbar: true
            ,totalRow: true
            ,cols: [[ //表头
                {field: 'userNumber', title: '编号', sort: true,width:70, totalRowText: '合计', align:'center'}
                ,{field: 'employeeName', title: '姓名',width:100, align:'center'}
                ,{field: 'laowupaiqian', title: '劳务派遣', sort: true, align:'center'}
                ,{field: 'wageSubtotal', title: '工资小计', sort: true, align:'center', totalRow: true}
                ,{field: 'performanceCoefficient', title: '绩效系数', sort: true, align:'center'}
                ,{field: 'wagesPayable', title: '应发工资', sort: true, totalRow: true, align:'center'}
                ,{field: 'totalDeduction', title: '扣款合计', sort: true, totalRow: true, align:'center'}
                ,{field: 'totalTax', title: '计税合计', sort: true, align:'center', totalRow: true}
                ,{field: 'individualTaxAdjustment', title: '个调税', sort: true, align:'center', totalRow: true}
                ,{field: 'netSalary', title: '实发工资', sort: true, totalRow: true, align:'center'}
                ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:120, align:'center'}
            ]]
            ,done: function(res, curr, count){
                /*if (res.count > 0 && hideM != mouth + 1){
                    for (var i = 0; i < res.count; i ++) {
                        $('.editBtn').css("display", "none");
                    }
                }
                if (res.count > 0 && hideM == mouth + 1 && day > 8) {
                    for (var i = 0; i < res.count; i ++) {
                        $('.editBtn').css("display", "none");
                    }
                }*/
            }
        });
        //监听行工具事件
        table.on('tool(test)', function(obj) {
            var data = obj.data;
            $("#dateHidden").val(data.date);
            $("#wagsId").val(data.id);
            $("#userId").val(data.employeeId);
            $(".userNumber").val(data.userNumber);
            $(".employeeName").val(data.employeeName);
            $(".basePay").val(data.basePay);
            $(".skillPay").val(data.skillPay);
            $(".positionSalary").val(data.positionSalary);
            $(".seniorityWage").val(data.seniorityWage);
            $(".wageSubtotal").val(data.wageSubtotal);
            $(".meritPay").val(data.meritPay);
            $(".performanceCoefficient").val(data.performanceCoefficient);
            $(".wagesPayable").val(data.wagesPayable);
            $(".foodSupplement").val(data.foodSupplement);
            $(".highTemperatureSubsidy").val(data.highTemperatureSubsidy);
            $(".totalPayable").val(data.totalPayable);
            $(".endowmentInsurance").val(data.endowmentInsurance);
            $(".unemploymentBenefits").val(data.unemploymentBenefits);
            $(".medicalInsurance").val(data.medicalInsurance);
            $(".accumulationFund").val(data.accumulationFund);
            $(".otherDeductions").val(data.otherDeductions);
            $(".unionFees").val(data.unionFees);
            $(".totalDeduction").val(data.totalDeduction);
            $(".totalTax").val(data.totalTax);
            $(".sixSpecialDeductions").val(data.sixSpecialDeductions);
            $(".other").val(data.other);
            $(".remark").val(data.remark);
            $(".individualTaxAdjustment").val(data.individualTaxAdjustment);
            $(".netSalary").val(data.netSalary);
            var subTotalOfSubsidies = parseFloat($(".foodSupplement").val())+parseFloat($(".highTemperatureSubsidy").val());
            $("#subTotalOfSubsidies").val(subTotalOfSubsidies);
            setInterval(function () {
                $("#totalTax").val(Number($('#totalPayable').val())-Number($("#totalDeduction").val()));
            },1000);
            sumWages();
            if(obj.event === 'edit'){//修改
                layer.open({
                    type: 1
                    ,id: 'updateFinance' //防止重复弹出
                    ,content: $(".updateFinance")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,yes: function(){}
                });
            }else if(obj.event === 'detail'){//查看
                layer.open({
                    type: 1
                    ,id: 'detailFinance' //防止重复弹出
                    ,content: $(".detailFinance")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['80%', '80%']
                    ,yes: function(){}
                });
            }
        });
    });
}
//打开复制页面
function copyWags() {
    layui.use('form', function(){
        var form = layui.form;
        $("#test16").val('');
        form.render('select');
        form.render(); //更新全部
    });
    layer.open({
        type: 1
        ,id: 'copyDiv' //防止重复弹出
        ,content: $(".copyDiv")
        ,btnAlign: 'c' //按钮居中
        ,shade: 0.5 //不显示遮罩
        ,area: ['320px', '200px']
        ,yes: function(){}
    });
}
//确定复制
function copyOk() {
    var monthStart = $("#monthStart").val();
    var monthEnd = $("#monthEnd").val();
    $.ajax({
        url: path+"/wa/wags/copyToThisMonthWags",//请求地址
        dataType: "json",//数据格式
        data: {"monthStart": monthStart,"monthEnd": monthEnd},
        type: "get",//请求方式
        success: function (data) {
            if (data == "success") {
                alert("周期已复制");
            } else if(data=='fail'){
                alert("周期已存在");
            }
            layer.closeAll();
        }
    });
}
//修改
function updFinance() {
    var wages = {};
    wages.id = $("#wagsId").val();
    if ($("#dateHidden").val() == "" || $("#dateHidden").val() == null) {
        wages.date = $("#test15").val();
    } else {
        wages.date = $("#dateHidden").val();
    }
    wages.employeeId = $("#userId").val();
    wages.employeeName = $("#employeeName").val();
    wages.userNumber = $("#userNumber").val();
    wages.basePay = $("#basePay").val();
    wages.skillPay = $("#skillPay").val();
    wages.positionSalary = $("#positionSalary").val();
    wages.seniorityWage = $("#seniorityWage").val();
    wages.wageSubtotal = $("#wageSubtotal").val();//工资小计
    wages.meritPay = $("#meritPay").val();
    wages.performanceCoefficient = $("#performanceCoefficient").val();
    wages.wagesPayable = $("#wagesPayable").val();//应发工资
    wages.foodSupplement = $("#foodSupplement").val();
    wages.highTemperatureSubsidy = $("#highTemperatureSubsidy").val();
    wages.subTotalOfSubsidies = $("#subTotalOfSubsidies").val();
    wages.currentIncomeTax = $("#totalPayable").val();//应发合计  本期收入
    wages.endowmentInsurance = $("#endowmentInsurance").val();
    wages.unemploymentBenefits = $("#unemploymentBenefits").val();
    wages.medicalInsurance = $("#medicalInsurance").val();
    wages.accumulationFund = $("#accumulationFund").val();
    wages.otherDeductions = $("#otherDeductions").val();
    wages.unionFees = $("#unionFees").val();
    wages.fiveInsurancesAndOneFund = $("#totalDeduction").val();//扣款合计  五险一金
    wages.totalTax = $("#totalTax").val();//计税合计
    wages.sixSpecialDeductions = $("#sixSpecialDeductions").val();//六项专项扣除金额   专项扣除
    wages.individualTaxAdjustment = $("#individualTaxAdjustment").val();
    wages.netSalary = $("#netSalary").val();//实发工资
    wages.other = $("#other").val();//其他
    wages.remark = $("#remark").val();
    wages.month = $("#test15").val();
    wages.deductionOfExpenses = '5000.00';//减除费用
    console.log(wages)
    $.ajax({
        type: "post",
        url: path + "/wa/wags/updWages",
        data: JSON.stringify(wages),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            layer.closeAll();
            showWagsList( $("#test15").val());
        }
    });
}
//取消
function cancel() {
    layer.closeAll();
}