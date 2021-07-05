var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth();
var day = date.getDate();
var upBtnYear = year;
var upBtnMonth = month;
$(function () {
    showMonth();
    if (month == 12) {
        month = 1;
        year = year + 1;
    }
    if (month == 0) {
        month = 12;
        year = year - 1;
    }
    if (month < 10) {
        month = "0" + month;
    }
    showWagsList(year + "-" + month);
    $("#test15").val(year + "-" + month);
    getSel();//显示岗位下拉框
});

//点击查看上个月数据
function monthUpBtn() {
    upBtnMonth--;
    if (upBtnMonth < "1") {
        upBtnYear--;
        upBtnMonth = 12;
    }
    if (upBtnMonth < 10) {
        upBtnMonth = "0" + upBtnMonth
    }
    var monthUp = upBtnYear + "-" + upBtnMonth;
    layui.use('form', function () {
        var form = layui.form;
        $("#test15").val(monthUp);
        form.render('select');
        form.render(); //更新全部
    });
    showWagsList($("#test15").val());
    $("#calculationButton").css("display","none");
    if (upBtnMonth == Number(month)+1) {
        $("#calculationButton").css("display","block");
    }
}

//点击查看下个月数据
function monthDownBtn() {
    upBtnMonth++;
    if (upBtnMonth > '12') {
        upBtnYear++;
        upBtnMonth = 1;
    }
    if (upBtnMonth < 10) {
        upBtnMonth = "0" + upBtnMonth
    }
    var monthDown = upBtnYear + "-" + upBtnMonth;
    layui.use('form', function () {
        var form = layui.form;
        $("#test15").val(monthDown);
        form.render('select');
        form.render(); //更新全部
    });
    showWagsList($("#test15").val());
    $("#calculationButton").css("display","none");
    if (upBtnMonth == Number(month)+1) {
        $("#calculationButton").css("display","block");
    }
}

//显示时间
function showMonth() {
    var initial = upBtnYear + "-" + upBtnMonth;
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
                upBtnYear = value.substring(0, 4);
                upBtnMonth = value.substr(5, 2);
                showWagsList(value);
            }
        });
        laydate.render({
            elem: '#test16'
            , type: 'month'
            , trigger: 'click'//呼出事件改成click
            , done: function (value) {
            }
        });
    })
}

//显示岗位下拉框
function getSel(){
    layui.use(['form'], function () {
        var form = layui.form;
        $.ajax({
            type: "GET",
            url: path + "/wa/wags/getWagesPostMap",
            dataType: "json",
            success: function (data) {
                $("#selPostName").empty();
                var option = "<option value='0' >请选择岗位</option>";
                for (var i = 0; i < data.length; i++) {
                    option += "<option value='" + data[i].id + "' label='"+data[i].wage+"'>" + data[i].name + "</option>"
                }
                $('#selPostName').html(option);
                form.render();//菜单渲染 把内容加载进去
                //岗位等级下拉框加载
                form.on('select(selPostName)', function (data) {
                    $("#selPostNameHidden").val(data.value);
                    var dom = data.elem[data.elem.selectedIndex].label;
                    dom = parseFloat(dom)
                    $(".basePay").val(dom);
                    // sumWages();
                    $.ajax({
                        type: "GET",
                        url: path + "/wa/wags/getPostGradeMap",
                        data: {id: $("#selPostNameHidden").val()},
                        dataType: "json",
                        success: function (data) {
                            $("#selPostLevelName").empty();
                            var option = "<option value='0' >请选择岗位等级</option>";
                            for (var i = 0; i < data.length; i++) {
                                option += "<option value='" + data[i].id + "' label='"+data[i].wage+"'>" + data[i].name + "</option>"
                            }
                            $('#selPostLevelName').html(option);
                            form.render();//菜单渲染 把内容加载进去
                        }
                    });
                });
                form.on('select(selPostLevelName)', function (data1) {
                    $("#selPostLevelNameHidden").val(data1.value);
                    var dom1 = data1.elem[data1.elem.selectedIndex].label;
                    dom1 = parseFloat(dom1)
                    $(".positionSalary").val(dom1);
                    // sumWages();
                });
            }
        });

    });
}
//输入两位小数
function twoDecimal(id, value) {
    var reg = /^\d+(\.\d{0,2})?$/;
    if (reg.test(value) == false) {
        var td = value.indexOf(".");
        var tdNumFront = value.substring(0, td);
        var tdNumAfter = value.substr(td, 3);
        value = tdNumFront + tdNumAfter;
    }
    $("#" + id).val(value);
}

//输入三位小数
function threeDecimal(id, value) {
    var reg = /^\d+(\.\d{0,3})?$/;
    if (reg.test(value) == false) {
        var td = value.indexOf(".");
        var tdNumFront = value.substring(0, td);
        var tdNumAfter = value.substr(td, 4);
        value = tdNumFront + tdNumAfter;
    }
    $("#" + id).val(value);
}

/*function sumWages() {
    var basePay = parseFloat($(".basePay").val());//岗位工资
    var positionSalary = parseFloat($(".positionSalary").val());//职级工资
    //绩效基数=(岗位工资+职级工资)/2
    var performanceBase = (basePay+positionSalary)/2;
    performanceBase = performanceBase.toFixed(2);
    $(".performanceBase").val(performanceBase);

    //绩效工资=绩效基数*绩效系数
    var performanceCoefficient = parseFloat($(".performanceCoefficient").val());
    var meritPay = performanceBase*performanceCoefficient;
    meritPay = meritPay.toFixed(2);
    $(".meritPay").val(meritPay);//绩效工资

    //工资小计=岗位工资+职务工资+绩效工资+其他  1
    //工资小计=岗位工资+职级工资  2
    var other = parseFloat($(".other").val());//其他
    var wageSubtotal = basePay + positionSalary;
    wageSubtotal = wageSubtotal.toFixed(2);
    $(".wageSubtotal").val(wageSubtotal);//工资小计

    //应发工资==(岗位工资+职务工资+其他)+绩效工资*绩效系数  1
    //应发工资==(岗位工资+职级工资)/2+绩效工资+其他   2
    var performanceCoefficient = parseFloat($(".performanceCoefficient").val());//绩效系数
    var wagesPayable = (basePay + positionSalary)/2 + other +  parseFloat(meritPay);
    wagesPayable = wagesPayable.toFixed(2);
    $(".wagesPayable").val(wagesPayable);

    //补贴小计=高温补贴+餐补
    var foodSupplement = parseFloat($(".foodSupplement").val());
    var highTemperatureSubsidy = Number($(".highTemperatureSubsidy").val());
    var subTotalOfSubsidies = foodSupplement + parseFloat(highTemperatureSubsidy);
    subTotalOfSubsidies = subTotalOfSubsidies.toFixed(2);
    $(".subTotalOfSubsidies").val(subTotalOfSubsidies);

    //应发合计=应发工资+补贴小计
    var totalPayable = Number(wagesPayable) + Number(subTotalOfSubsidies);
    totalPayable = totalPayable.toFixed(2);
    $(".totalPayable").val(totalPayable);

    //扣款合计=养老保险+医疗保险+公积金+失业金+工会费+其他扣款
    var endowmentInsurance = parseFloat($(".endowmentInsurance").val());
    var medicalInsurance = parseFloat($(".medicalInsurance").val());
    var accumulationFund = parseFloat($(".accumulationFund").val());
    var unemploymentBenefits = parseFloat($(".unemploymentBenefits").val());
    var unionFees = parseFloat($(".unionFees").val());
    var otherDeductions = parseFloat($(".otherDeductions").val());
    var deduction = endowmentInsurance + medicalInsurance + accumulationFund + unemploymentBenefits + unionFees + otherDeductions;
    deduction = deduction.toFixed(2)
    $(".totalDeduction").val(deduction);

    //计税合计=应发合计-扣款合计
    var totalTax = Number(totalPayable) - Number(deduction);
    totalTax = totalTax.toFixed(2);
    $(".totalTax").val(totalTax);

    $.ajax({
        type: "post",
        url: path + "/wa/wags/taxation",
        data: {
            employeeId: $("#userId").val(),//员工ID
            month: $("#test15").val(),//月份
            totalTax: $(".totalTax").val(),//计税合计
            specialAdditionalDeduction: $(".sixSpecialDeductions").val()//专项扣除  六项专项扣除金额
        },
        dataType: "json",
        success: function (data) {
            var individualTaxAdjustment = data.tax;
            individualTaxAdjustment = Math.floor(individualTaxAdjustment * 100) / 100;
            $(".individualTaxAdjustment").val(individualTaxAdjustment);
            var netSalary = data.netSalary;
            netSalary = Math.floor(netSalary * 100) / 100;
            $(".netSalary").val(netSalary);
        }
    });
}*/

//查询数据
function showWagsList(m) {
    var top = $(".top").css("height");
    var win = $(window).height();
    var tp = top.indexOf("p");
    var topHeight = top.substring(0, tp);
    var height = (win - topHeight - 20) - 20;
    $("#test15").val(m)
    layui.use(['table','form'], function () {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/wags/getWagsList?month=' + m //数据接口
            , toolbar: true
            , totalRow: true
            , cols: [[ //表头
                {field: 'userNumber', title: '编号', sort: true, width: 70, totalRowText: '合计', align: 'center'}
                , {field: 'employeeName', title: '姓名', width: 100, align: 'center'}
                , {field: 'departmentName', title: '项目部', sort: true, align: 'center'}
                , {field: 'isChanged', title: '人事异动', sort: true, align: 'center'}
                , {field: 'laowupaiqian', title: '劳务派遣', sort: true, align: 'center'}
                , {field: 'wagesPostName', title: '岗位等级', sort: true, align: 'center', toolbar: '#barDemoPost'}
                , {field: 'wageSubtotal', title: '工资小计', sort: true, align: 'center', totalRow: true}
                , {field: 'performanceCoefficient', title: '绩效系数', sort: true, align: 'center'}
                , {field: 'wagesPayable', title: '应发工资', sort: true, totalRow: true, align: 'center'}
                , {field: 'subTotalOfSubsidies', title: '补贴小计', sort: true, totalRow: true, align: 'center'}
                , {field: 'totalDeduction', title: '扣款合计', sort: true, totalRow: true, align: 'center'}
                , {field: 'sixSpecialDeductions', title: '六项专项扣除', sort: true, totalRow: true, align: 'center', hide: true}
                , {field: 'totalTax', title: '计税合计', sort: true, align: 'center', totalRow: true, hide: true}
                , {field: 'individualTaxAdjustment', title: '个调税', sort: true, align: 'center', totalRow: true}
                , {field: 'netSalary', title: '实发工资', sort: true, totalRow: true, align: 'center'}
                , {field: 'basePay', title: '基本工资', align: 'center', hide: true}
                , {field: 'skillPay', title: '技能工资', align: 'center', hide: true}
                , {field: 'seniorityWage', title: '工龄工资', align: 'center', hide: true}
                , {field: 'positionSalary', title: '职务工资', align: 'center', hide: true}
                , {field: 'other', title: '其他', align: 'center', hide: true}
                , {field: 'meritPay', title: '绩效工资', align: 'center', hide: true}
                , {field: 'meritBase', title: '绩效基数', align: 'center', hide: true}
                , {field: 'totalPayable', title: '应发合计', align: 'center', hide: true}
                , {field: 'foodSupplement', title: '餐补', align: 'center', hide: true}
                , {field: 'highTemperatureSubsidy', title: '高温补贴', align: 'center', hide: true}
                , {field: 'unionFees', title: '工会费', align: 'center', hide: true}
                , {field: 'accumulationFund', title: '公积金', align: 'center', hide: true}
                , {field: 'medicalInsurance', title: '医疗保险', align: 'center', hide: true}
                , {field: 'unemploymentBenefits', title: '失业金', align: 'center', hide: true}
                , {field: 'endowmentInsurance', title: '养老保险', align: 'center', hide: true}
                , {field: 'otherDeductions', title: '其他扣款', align: 'center', hide: true}
                , {field: 'remark', title: '备注', align: 'center', hide: true}
                , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: 120, align: 'center'}
            ]]
            , done: function (res, curr, count) {
                var currentYear = date.getFullYear();
                var currentMonth = date.getMonth() + 1;
                var selYear = ($("#test15").val()).substr(0,4);
                var selMonth = ($("#test15").val()).substr(5,2);
                if (currentYear == selYear && currentMonth > selMonth) {
                    for (var i = 0; i < res.count; i ++) {
                        $('.editBtn').css("display", "none");
                    }
                }
            }
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            $("#dateHidden").val(data.date);
            $("#wagsId").val(data.id);
            $("#userId").val(data.employeeId);//员工id
            $(".userNumber").val(data.userNumber);//员工编号
            $(".employeeName").val(data.employeeName);//员工姓名
            layui.use('form',function (){
                var form = layui.form;
                $.ajax({
                    type: "GET",
                    url: path + "/wa/wags/getPostGradeMap",
                    data: {id: data.wagesPostId},
                    async: false,
                    dataType: "json",
                    success: function (data) {
                        $("#selPostLevelName").empty();
                        var option = "<option value='0' >请选择岗位等级</option>";
                        for (var i = 0; i < data.length; i++) {
                            option += "<option value='" + data[i].id + "' label='"+data[i].wage+"'>" + data[i].name + "</option>"
                        }
                        $('#selPostLevelName').html(option);
                        form.render();//菜单渲染 把内容加载进去
                    }
                });
                $("#selPostNameHidden").val(data.wagesPostId);
                $("#selPostName").val(data.wagesPostId);
                $("#selPostLevelNameHidden").val(data.postGradeId);
                $("#selPostLevelName").val(data.postGradeId);
                form.render();
            });
           /* $.ajax({
                type: "GET",
                url: path + "/wa/wags/getPerformanceCoefficientByEmployeeId",
                data: {cycle: m,employeeId:data.employeeId},
                async: false,
                dataType: "json",
                success: function (data) {*/
            $(".performanceCoefficient").val(data.performanceCoefficient);//绩效系数
            $(".foodSupplement").val(data.foodSupplement);//餐补
            //     }
            // })
            $(".userPost").val(data.wagesPostName);//岗位
            $(".postLevel").val(data.postGradeName);//岗位等级
            $(".basePay").val(data.basePay);//岗位工资
            $(".positionSalary").val(data.positionSalary);//职级工资
            $(".performanceBase").val(data.meritBase);//绩效基数
            $(".meritPay").val(data.meritPay);//绩效工资
            $(".other").val(data.other);//其他
            $(".wageSubtotal").val(data.wageSubtotal);//工资小计
            $(".wagesPayable").val(data.wagesPayable);//应发工资
            if (data.foodSupplement == "" || data.foodSupplement == null) {
                data.foodSupplement = 0.00;
            }
            if (data.highTemperatureSubsidy == "" || data.highTemperatureSubsidy == null) {
                data.highTemperatureSubsidy = 0.00;
            }
            $(".highTemperatureSubsidy").val(data.highTemperatureSubsidy);//高温补贴
            $(".subTotalOfSubsidies").val(data.subTotalOfSubsidies);//补贴小计
            $(".totalPayable").val(data.totalPayable);//应发合计
            $(".endowmentInsurance").val(data.endowmentInsurance);//养老保险
            $(".medicalInsurance").val(data.medicalInsurance);//医疗保险
            $(".accumulationFund").val(data.accumulationFund);//公积金
            $(".unemploymentBenefits").val(data.unemploymentBenefits);//失业金
            $(".unionFees").val(data.unionFees);//工会费
            $(".otherDeductions").val(data.otherDeductions);//其他扣款
            $(".totalDeduction").val(data.totalDeduction);//扣款合计
            $(".totalTax").val(data.totalTax);//计税合计
            $(".sixSpecialDeductions").val(data.sixSpecialDeductions);//六项专项扣除金额
            $(".individualTaxAdjustment").val(data.individualTaxAdjustment);//个调税
            $(".netSalary").val(data.netSalary);//实发工资
            $(".remark").val(data.remark);//备注
            if (obj.event === 'edit') {//修改
                 $.ajax({
                type: "GET",
                url: path + "/wa/wags/getPerformanceCoefficientByEmployeeId",
                data: {cycle: m,employeeId:data.employeeId},
                async: false,
                dataType: "json",
                success: function (data) {
                        $(".performanceCoefficient").val(data.performanceCoefficient);//绩效系数
                        $(".foodSupplement").val(data.foodSupplement);//餐补
                        // sumWages();
                    }
                });
                layer.open({
                    type: 1
                    , id: 'updateFinance' //防止重复弹出
                    , content: $(".updateFinance")
                    , btnAlign: 'c' //按钮居中
                    , shade: 0.5 //不显示遮罩
                    , area: ['100%', '100%']
                    , yes: function () {
                    }
                });
            } else if (obj.event === 'detail') {//查看
                layer.open({
                    type: 1
                    , id: 'detailFinance' //防止重复弹出
                    , content: $(".detailFinance")
                    , btnAlign: 'c' //按钮居中
                    , shade: 0.5 //不显示遮罩
                    , area: ['80%', '80%']
                    , yes: function () {
                    }
                });
            }
        });
    });
}

//打开复制页面
function copyWags() {
    layui.use('form', function () {
        var form = layui.form;
        $("#test16").val('');
        form.render('select');
        form.render(); //更新全部
    });
    layer.open({
        type: 1
        , id: 'copyDiv' //防止重复弹出
        , content: $(".copyDiv")
        , btnAlign: 'c' //按钮居中
        , shade: 0.5 //不显示遮罩
        , area: ['320px', '200px']
        , yes: function () {
        }
    });
}

//确定复制
function copyOk() {
    var monthStart = $("#test15").val();
    var monthEnd = $("#test16").val();
    $.ajax({
        url: path + "/wa/wags/copyToThisMonthWags",//请求地址
        dataType: "json",//数据格式
        data: {"monthStart": monthStart, "monthEnd": monthEnd},
        type: "get",//请求方式
        success: function (data) {
            if (data == "success") {
                alert("周期已复制");
            } else if (data == 'fail') {
                alert("周期已存在");
            }
            layer.closeAll();
        }
    });
}

//本月工资核算
function calculationWags () {
    layer.open({
        type: 1
        ,title: false //不显示标题栏
        ,closeBtn: false
        ,area: '300px;'
        ,shade: 0.8
        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
        ,btn: ['确定', '取消']
        ,btnAlign: 'c'
        ,moveType: 1 //拖拽模式，0或者1
        ,content: '<div style="padding: 50px 10px 50px 17px; box-sizing: border-box; line-height: 22px; background-color: #f2f2f2; color: #000; font-weight: 500;font-size: 18px;">确认计算该月工资吗？</div>'
        ,success: function(layero){
            var btn = layero.find('.layui-layer-btn');
            btn.find('.layui-layer-btn0').click(function () {
                $(".loading").css("display",'block');
                $.ajax({
                    "type" : 'put',
                    "url": path + "/wa/wags/thisMonthCalculation",
                    data: {month:$("#test15").val()},
                    dataType: "json",
                    "success":function(data){
                        if (data == "success"){
                            $(".loading").css("display",'none');
                        }
                    }
                });
            });
        }
    });
}

//本月工资核算
function calculationWags () {
    layer.open({
        type: 1
        ,title: false //不显示标题栏
        ,closeBtn: false
        ,area: '300px;'
        ,shade: 0.8
        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
        ,btn: ['确定', '取消']
        ,btnAlign: 'c'
        ,moveType: 1 //拖拽模式，0或者1
        ,content: '<div style="padding: 50px 10px 50px 17px; box-sizing: border-box; line-height: 22px; background-color: #f2f2f2; color: #000; font-weight: 500;font-size: 18px;">确认计算该月工资吗？</div>'
        ,success: function(layero){
            var btn = layero.find('.layui-layer-btn');
            btn.find('.layui-layer-btn0').click(function () {
                $(".loading").css("display",'block');
                $.ajax({
                    "type" : 'put',
                    "url": path + "/wa/wags/thisMonthCalculation",
                    data: {month:$("#test15").val()},
                    dataType: "json",
                    "success":function(data){
                        if (data == "success"){
                            $(".loading").css("display",'none');
                        } else if (data == "error"){ //后台错误
                            $(".loading").css("display",'none');
                            layer.alert("操作失败");
                        } else if (data == "noParameters"){ //参数错误
                            $(".loading").css("display",'none');
                            layer.alert("前台参数错误");
                        } else if (data == "noUser"){ //用户信息过期
                            $(".loading").css("display",'none');
                            layer.alert("用户信息过期");
                        }
                    }
                });
            });
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
    wages.wagesPostId = Number($("#selPostNameHidden").val());
    wages.postGradeId = Number($("#selPostLevelNameHidden").val());
    wages.userNumber = $("#userNumber").val();
    wages.basePay = parseFloat($("#basePay").val());
    wages.positionSalary = parseFloat($("#positionSalary").val());
    wages.wageSubtotal = parseFloat($("#wageSubtotal").val());//工资小计
    wages.meritPay = parseFloat($("#meritPay").val());
    wages.performanceBase = parseFloat($("#performanceBase").val());
    wages.performanceCoefficient = parseFloat($("#performanceCoefficient").val());
    wages.wagesPayable = parseFloat($("#wagesPayable").val());//应发工资
    wages.foodSupplement = parseFloat($("#foodSupplement").val());
    wages.highTemperatureSubsidy = parseFloat($("#highTemperatureSubsidy").val());
    wages.subTotalOfSubsidies = parseFloat($("#subTotalOfSubsidies").val());
    wages.currentIncomeTax = parseFloat($("#totalPayable").val());//应发合计  本期收入
    wages.endowmentInsurance = parseFloat($("#endowmentInsurance").val());
    wages.unemploymentBenefits = parseFloat($("#unemploymentBenefits").val());
    wages.medicalInsurance = parseFloat($("#medicalInsurance").val());
    wages.accumulationFund = parseFloat($("#accumulationFund").val());
    wages.otherDeductions = parseFloat($("#otherDeductions").val());
    wages.unionFees = parseFloat($("#unionFees").val());
    wages.fiveInsurancesAndOneFund = parseFloat($("#totalDeduction").val());//五险一金=扣款合计
    wages.totalDeduction = parseFloat($("#totalDeduction").val());//扣款合计
    wages.totalTax = parseFloat($("#totalTax").val());//计税合计
    wages.sixSpecialDeductions = parseFloat($("#sixSpecialDeductions").val());//六项专项扣除金额   专项扣除
    wages.individualTaxAdjustment = parseFloat($("#individualTaxAdjustment").val());
    wages.netSalary = parseFloat($("#netSalary").val());//实发工资
    wages.other = $("#other").val();//其他
    wages.remark = $("#remark").val();
    wages.month = $("#test15").val();
    wages.deductionOfExpenses = '5000.00';//减除费用
    $.ajax({
        type: "post",
        url: path + "/wa/wags/updWages",
        data: JSON.stringify(wages),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            layer.closeAll();
            showWagsList($("#test15").val());
        }
    });
}

//取消
function cancel() {
    layer.closeAll();
}