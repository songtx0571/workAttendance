var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth() + 1;
$(function () {
    //显示考核日期
    showCycleData();
    //显示部门
    // showCompany();
    // 查询员工绩效信息
    showTable(year, "");
    $("#test2").val(year);
});

/*显示考核日期*/
function showCycleData() {
    layui.use(['laydate','form'], function () {
        var laydate = layui.laydate, form = layui.form;
        //查询所有数据日期
        laydate.render({
            elem: '#test2'
            , type: 'year'
            , trigger: 'click'//呼出事件改成click
            , done: function (value) {
                showTable(value, "");
            }
        });

    });
    layui.use(['form'], function () {
        var form = layui.form;
        form.on('select(selMonth)', function (data) {
            $("#selMonthHidden").val(data.value);
            if ($("#test2").val() == "") {
                layer.alert("请选择年份");
                return;
            }
            timeMonth();
        });
    })
}


function timeMonth() {
    if ($("#selMonthHidden").val() != "13") {
        if ($("#selMonthHidden").val().length < 2) {
            $("#selMonthHidden").val("0" + $("#selMonthHidden").val());
        }
        showTable($("#test2").val() + "-" + $("#selMonthHidden").val(), $("#selDepartNameHidden").val());
    } else {
        showTable($("#test2").val(), $("#selDepartNameHidden").val());
    }
}

//显示数据
function showTable(cycle, depart) {
    var win = $(window).height();
    var height = win - 100;
    if (depart == '0') {
        depart = "";
    }
    layui.use('table', function () {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/reimbursement/getStatistics?month=' + cycle + '&depart=' + depart //数据接口
            , toolbar: true
            , totalRow: true
            , page: false
            , cols: [[ //表头
                {
                    field: 'subject',
                    title: '科目',
                    align: 'center',
                    sort: true,
                    totalRowText: '合计',
                    templet: function (a) {
                        if (a.subject == 1) {
                            return '差旅费'
                        } else if (a.subject == 2) {
                            return '差旅补助'
                        } else if (a.subject == 3) {
                            return '招待费'
                        } else if (a.subject == 4) {
                            return '办公费'
                        } else if (a.subject == 5) {
                            return '劳保费'
                        } else if (a.subject == 6) {
                            return '员工福利费'
                        } else if (a.subject == 7) {
                            return '员工保险费'
                        } else if (a.subject == 8) {
                            return '员工培训费'
                        } else if (a.subject == 9) {
                            return '车辆费用'
                        } else if (a.subject == 10) {
                            return '器具'
                        } else if (a.subject == 11) {
                            return '固定资产'
                        } else if (a.subject == 12) {
                            return '房租'
                        } else if (a.subject == 14) {
                            return '工资'
                        } else if (a.subject == 15) {
                            return '社保、公积金'
                        } else if (a.subject == 16) {
                            return '中介机构费'
                        } else {
                            return '其他'
                        }
                    }
                }
                , {field: 'jasAmount', title: '嘉爱斯报销金额(元)', align: 'center', totalRow: true}
                , {field: 'tasAmount', title: '泰爱斯报销金额(元)', align: 'center', totalRow: true}
                , {field: 'pjAmount', title: '浦江报销金额(元)', align: 'center', totalRow: true}
                , {field: 'ljAmount', title: '临江报销金额(元)', align: 'center', totalRow: true}
                , {field: 'smAmount', title: '三美化工报销金额(元)', align: 'center', totalRow: true}
                , {field: 'zhAmount', title: '智慧报销金额(元)', align: 'center', totalRow: true}
                , {field: 'zgAmount', title: '综管报销金额(元)', align: 'center', totalRow: true}
            ]]
            , done: function (res, curr, count) {
            }
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
        });
    });
}