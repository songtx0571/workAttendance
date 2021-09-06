var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth();
var day = date.getDate();
var upBtnYear = year;
var upBtnMonth = month;
var laowupaiqian = ""
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
    // $("#calculationButton").css("display","none");
    // if (upBtnMonth == Number(month)+1) {
    //     $("#calculationButton").css("display","block");
    // }
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
    // $("#calculationButton").css("display","none");
    // if (upBtnMonth == Number(month)+1) {
    //     $("#calculationButton").css("display","block");
    // }
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
function getSel() {
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
                    option += "<option value='" + data[i].id + "' label='" + data[i].wage + "'>" + data[i].name + "</option>"
                }
                $('#selPostName').html(option);
                form.render();//菜单渲染 把内容加载进去
                //岗位等级下拉框加载
                form.on('select(selPostName)', function (data) {
                    $("#selPostNameHidden").val(data.value);
                    var dom = data.elem[data.elem.selectedIndex].label;
                    dom = parseFloat(dom)
                    $(".basePay").val(dom);
                    sumWages();
                    $.ajax({
                        type: "GET",
                        url: path + "/wa/wags/getPostGradeMap",
                        data: {id: $("#selPostNameHidden").val()},
                        dataType: "json",
                        success: function (data) {
                            $("#selPostLevelName").empty();
                            var option = "<option value='0' >请选择岗位等级</option>";
                            for (var i = 0; i < data.length; i++) {
                                option += "<option value='" + data[i].id + "' label='" + data[i].wage + "'>" + data[i].name + "</option>"
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
                    sumWages();
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

function sumWages() {
  /*  var wages = {};
    wages.employeeId = parseFloat($("#userId").val());//员工ID
    wages.date = $("#test15").val();//月份
    wages.basePay = parseFloat($(".basePay").val());//岗位工资
    wages.positionSalary = parseFloat($(".positionSalary").val());//职级工资
    wages.other = parseFloat($(".other").val());//其他
    wages.foodSupplement = parseFloat($(".foodSupplement").val())//餐补
    wages.highTemperatureSubsidy = parseFloat($(".highTemperatureSubsidy").val());//高温补贴
    wages.endowmentInsurance = parseFloat($(".endowmentInsurance").val());// 养老保险
    wages.medicalInsurance = parseFloat($(".medicalInsurance").val());//医疗保险
    wages.accumulationFund = parseFloat($(".accumulationFund").val());//公积金
    wages.unemploymentBenefits = parseFloat($(".unemploymentBenefits").val());//失业金
    wages.unionFees = parseFloat($(".unionFees").val());//工会费
    wages.otherDeductions = parseFloat($(".otherDeductions").val());//其他扣款
    wages.specialAdditionalDeduction = parseFloat($(".specialAdditionalDeduction").val());//专项附加扣除
    wages.sixSpecialDeductions = parseFloat($(".sixSpecialDeductions").val());//六项专项扣除金额
    wages.laowupaiqian = laowupaiqian;//劳务派遣
    wages.overtimeSubsidy = parseFloat($(".overtimeSubsidy").val());//加班补贴
    $.ajax({
        type: "post",
        url: path + "/wa/wags/taxation",
        data: JSON.stringify(wages),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.code == 0 || data.code == 200) {
                data = data.data;
                $(".individualIncomeTaxTotal").val(data.individualIncomeTaxTotal);//累计个税
                $(".deductionOfExpensesTaxTotal").val(data.deductionOfExpensesTaxTotal);//累计费用减免
                $(".incomeTotal").val(data.incomeTotal);//累计收入额
                $(".individualIncomeTaxPaidTotal").val(data.individualIncomeTaxPaidTotal);//累计已缴纳个税
                $(".individualTaxAdjustment").val(data.individualTaxAdjustment);//个调税
                $(".meritBase").val(data.meritBase);//绩效基数
                $(".meritPay").val(data.meritPay);//绩效工资
                $(".netSalary").val(data.netSalary);//实发工资
                $(".otherDeductionTaxTotal").val(data.otherDeductionTaxTotal);//累计其他扣除
                $(".specialAdditionalDeductionTaxTotal").val(data.specialAdditionalDeductionTaxTotal);//累计附加专项扣除
                $(".specialDeductionTaxTotal").val(data.specialDeductionTaxTotal);//累计专项扣除
                $(".subTotalOfSubsidies").val(data.subTotalOfSubsidies);//补贴小计
                $(".taxableIncomeTotal").val(data.taxableIncomeTotal);//累计应缴纳税所得额
                $(".totalDeduction").val(data.totalDeduction);//扣款合计
                $(".totalPayable").val(data.totalPayable);//应发合计
                $(".totalTax").val(data.totalTax);//计税合计
                $(".totalTaxTotal").val(data.totalTaxTotal);//累计计税合计
                $(".wageSubtotal").val(data.wageSubtotal);//工资小计
                $(".wagesPayable").val(data.wagesPayable);//应发工资
            } else {
                layer.alert(data.msg);
            }
        }
    });*/
}

//查询数据
function showWagsList(m) {
    var top = $(".top").css("height");
    var win = $(window).height();
    var tp = top.indexOf("p");
    var topHeight = top.substring(0, tp);
    var height = (win - topHeight - 20) - 20;
    $("#test15").val(m)
    layui.use(['table', 'form'], function () {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/wags/getWagsList?month=' + m //数据接口
            , toolbar: true
            , totalRow: true
            , cols: [[ //表头
                {field: 'userNumber', type: 'checkbox'}
                , {field: 'userNumber', title: '编号', sort: true, width: 70, totalRowText: '合计', align: 'center'}
                , {field: 'employeeName', title: '姓名', width: 100, align: 'center'}
                , {field: 'departmentName', title: '项目部', sort: true, align: 'center'}
                , {field: 'isChanged', title: '人事异动', sort: true, align: 'center'}
                , {field: 'laowupaiqian', title: '劳务派遣', sort: true, align: 'center'}
                , {field: 'wagesPostName', title: '岗位等级', sort: true, align: 'center', toolbar: '#barDemoPost'}
                , {field: 'wageSubtotal', title: '工资小计', sort: true, align: 'center', totalRow: true}
                , {field: 'performanceCoefficient', title: '绩效系数', sort: true, align: 'center'}
                , {field: 'jiaban', title: '加班工时', sort: true, align: 'center'}
                , {field: 'workAttendance', title: '考勤天数', sort: true, align: 'center'}
                , {field: 'wagesPayable', title: '应发工资', sort: true, totalRow: true, align: 'center'}
                , {field: 'subTotalOfSubsidies', title: '补贴小计', sort: true, totalRow: true, align: 'center'}
                , {field: 'totalDeduction', title: '扣款合计', sort: true, totalRow: true, align: 'center'}
                , {field: 'sixSpecialDeductions',title: '六项专项扣除',sort: true,totalRow: true,align: 'center',hide: true}
                , {field: 'totalTax', title: '计税合计', sort: true, align: 'center', totalRow: true, hide: true}
                , {field: 'individualTaxAdjustment', title: '个调税', sort: true, align: 'center', totalRow: true}
                , {field: 'netSalary', title: '实发工资', sort: true, totalRow: true, align: 'center'}
                , {field: 'basePay', title: '岗位工资', align: 'center', hide: true}
                , {field: 'positionSalary', title: '职级工资', align: 'center', hide: true}
                , {field: 'other', title: '其他', align: 'center', hide: true}
                , {field: 'meritPay', title: '绩效工资', align: 'center', hide: true}
                , {field: 'meritBase', title: '绩效基数', align: 'center', hide: true}
                , {field: 'totalPayable', title: '应发合计', align: 'center', hide: true}
                , {field: 'foodSupplement', title: '餐补', align: 'center', hide: true}
                , {field: 'highTemperatureSubsidy', title: '高温补贴', align: 'center', hide: true}
                , {field: 'overtimeSubsidy', title: '加班补贴', align: 'center', hide: true}
                , {field: 'unionFees', title: '工会费', align: 'center', hide: true}
                , {field: 'accumulationFund', title: '公积金', align: 'center', hide: true}
                , {field: 'medicalInsurance', title: '医疗保险', align: 'center', hide: true}
                , {field: 'unemploymentBenefits', title: '失业金', align: 'center', hide: true}
                , {field: 'endowmentInsurance', title: '养老保险', align: 'center', hide: true}
                , {field: 'otherDeductions', title: '其他扣款', align: 'center', hide: true}
                , {field: 'incomeTotal', title: '累计收入额', align: 'center', hide: true}
                , {field: 'deductionOfExpensesTaxTotal', title: '累计费用减免', align: 'center', hide: true}
                , {field: 'specialDeductionTaxTotal', title: '累计专项扣除', align: 'center', hide: true}
                , {field: 'specialAdditionalDeductionTaxTotal', title: '累计附加专项扣除', align: 'center', hide: true}
                , {field: 'individualIncomeTaxTotal', title: '累计个税', align: 'center', hide: true}
                , {field: 'individualIncomeTaxPaidTotal', title: '累计已缴纳个税', align: 'center', hide: true}
                , {field: 'otherDeductionTaxTotal', title: '累计其他扣除', align: 'center', hide: true}
                , {field: 'taxableIncomeTotal', title: '累计应缴纳税所得额', align: 'center', hide: true}
                , {field: 'specialAdditionalDeduction', title: '专项附加扣除', align: 'center', hide: true}
                , {field: 'totalTaxTotal', title: '累计计税合计', align: 'center', hide: true}
                , {field: 'remark', title: '备注', align: 'center', hide: true}
                , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: 120, align: 'center'}
            ]]
            , done: function (res, curr, count) {
                /*var currentYear = date.getFullYear();
                var currentMonth = date.getMonth() + 1;
                var selYear = ($("#test15").val()).substr(0,4);
                var selMonth = ($("#test15").val()).substr(5,2);
                if (currentYear == selYear && currentMonth > selMonth) {
                    for (var i = 0; i < res.count; i ++) {
                        $('.editBtn').css("display", "none");
                    }
                }*/
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
            laowupaiqian = data.laowupaiqian;//劳务派遣
            var date = new Date();
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var day = 31;
            if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) {
                if (month == 2) {
                    day = 28;
                } else {
                    day = 29;
                }
            }
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month ==12) {
                day = 31;
            } else  if (month == 4 || month == 6 || month == 9 || month == 11) {
                day = 30;
            }
            $(".isChanged").val(data.isChanged);
            if (data.isChanged == "试用期") {
                $(".wagesPayableTd").html("应发工资<span style='color: #6a737b;'>×0.8</span>");
            } else if (data.isChanged == "当月离职") {
                $(".wagesPayableTd").html("应发工资<span style='color: #6a737b'>×"+data.workAttendance+"/"+day+"</span>");
            } else {
                $(".wagesPayableTd").html("应发工资");
            }
            layui.use('form', function () {
                var form = layui.form;
                if (data.wagesPostId) {
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
                                option += "<option value='" + data[i].id + "' label='" + data[i].wage + "'>" + data[i].name + "</option>"
                            }
                            $('#selPostLevelName').html(option);
                            form.render();//菜单渲染 把内容加载进去
                        }
                    });
                }
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
            $(".overtimeSubsidy").val(data.overtimeSubsidy);//加班补贴
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
            $(".incomeTotal").val(data.incomeTotal);//累计收入额
            $(".deductionOfExpensesTaxTotal").val(data.deductionOfExpensesTaxTotal);//累计费用减免
            $(".specialDeductionTaxTotal").val(data.specialDeductionTaxTotal);//累计专项扣除
            $(".specialAdditionalDeductionTaxTotal").val(data.specialAdditionalDeductionTaxTotal);//累计附加专项扣除
            $(".individualIncomeTaxTotal").val(data.individualIncomeTaxTotal);//累计个税
            $(".individualIncomeTaxPaidTotal").val(data.individualIncomeTaxPaidTotal);//累计已缴纳个税
            $(".otherDeductionTaxTotal").val(data.otherDeductionTaxTotal);//累计其他扣除
            $(".taxableIncomeTotal").val(data.taxableIncomeTotal);//累计应缴纳税所得额
            $(".specialAdditionalDeduction").val(data.specialAdditionalDeduction);//专项附加扣除
            $(".totalTaxTotal").val(data.totalTaxTotal);//雷计计税合计
            if (obj.event === 'edit') {//修改
                /*$.ajax({
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
               });*/
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
function calculationWags() {
    var ids = [];
    layui.use(['table', 'form'], function () {
        var table = layui.table, layer = layui.layer;
        var checkStatus = table.checkStatus('demo');
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            if (o.id != null) {
                ids.push(o.id);
            }
        });
        ids = ids.join(",");
        if (ids.length < 1) {
            layer.msg('请勾选需要核算的人员');
            return false;
        } else {
            layer.open({
                type: 1
                ,
                title: false //不显示标题栏
                ,
                closeBtn: false
                ,
                area: '300px;'
                ,
                shade: 0.8
                ,
                id: 'LAY_layuipro' //设定一个id，防止重复弹出
                ,
                btn: ['确定', '取消']
                ,
                btnAlign: 'c'
                ,
                moveType: 1 //拖拽模式，0或者1
                ,
                content: '<div style="padding: 50px 10px 50px 17px; box-sizing: border-box; line-height: 22px; background-color: #f2f2f2; color: #000; font-weight: 500;font-size: 18px;">确认计算该月工资吗？</div>'
                ,
                success: function (layero) {
                    var btn = layero.find('.layui-layer-btn');
                    btn.find('.layui-layer-btn0').click(function () {
                        $(".loading").css("display", 'block');
                        $.ajax({
                            "type": 'put',
                            "url": path + "/wa/wags/thisMonthCalculation",
                            data: {month: $("#test15").val(), id: ids},
                            dataType: "json",
                            "success": function (data) {
                                if (data == "success") {
                                    $(".loading").css("display", 'none');
                                    showWagsList($("#test15").val());
                                } else if (data == "noParameters") { //参数错误
                                    $(".loading").css("display", 'none');
                                    layer.alert("前台参数错误");
                                } else if (data == "noUser") { //用户信息过期
                                    $(".loading").css("display", 'none');
                                    layer.alert("用户信息过期");
                                } else { //后台错误
                                    $(".loading").css("display", 'none');
                                    layer.alert(data);
                                }
                            }, error: function (res) {
                                $(".loading").css("display", 'none');
                                layer.alert("操作失败");
                            }
                        });
                    });
                }
            });
        }
    })


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
    wages.meritBase = parseFloat($("#performanceBase").val());//绩效基数
    wages.performanceCoefficient = parseFloat($("#performanceCoefficient").val());//绩效系数
    wages.wagesPayable = parseFloat($("#wagesPayable").val());//应发工资
    wages.foodSupplement = parseFloat($("#foodSupplement").val());
    wages.highTemperatureSubsidy = parseFloat($("#highTemperatureSubsidy").val());
    wages.overtimeSubsidy = parseFloat($("#overtimeSubsidy").val());//加班补贴
    wages.subTotalOfSubsidies = parseFloat($("#subTotalOfSubsidies").val());
    wages.totalPayable = parseFloat($("#totalPayable").val());//应发合计  本期收入
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
    wages.incomeTotal = $("#incomeTotal").val();//累计收入额
    wages.deductionOfExpensesTaxTotal = $("#deductionOfExpensesTaxTotal").val();//累计费用减免
    wages.specialDeductionTaxTotal = $("#specialDeductionTaxTotal").val();//累计专项扣除
    wages.specialAdditionalDeductionTaxTotal = $("#specialAdditionalDeductionTaxTotal").val();//累计附加专项扣除
    wages.individualIncomeTaxTotal = $("#individualIncomeTaxTotal").val();//累计个税
    wages.individualIncomeTaxPaidTotal = $("#individualIncomeTaxPaidTotal").val();//累计已缴纳个税
    wages.otherDeductionTaxTotal = $("#otherDeductionTaxTotal").val();//累计其他扣除
    wages.taxableIncomeTotal = $("#taxableIncomeTotal").val();//累计应缴纳税所得额
    wages.specialAdditionalDeduction = $("#specialAdditionalDeduction").val();//专项附加扣除
    wages.totalTaxTotal = $(".totalTaxTotal").val();//类计计税合计
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