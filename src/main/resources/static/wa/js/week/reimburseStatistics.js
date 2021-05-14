var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth() + 1;
$(function () {
    //显示考核日期
    showCycleData();
    //显示部门
    //showCompany();
    // 查询员工绩效信息
    showTable(year, "");
    $("#test2").val(year);
});

/*显示考核日期*/
function showCycleData() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
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
                {field: 'id', title: '编号', align: 'center', hide: true}
                , {
                    field: 'subject',
                    title: '科目',
                    toolbar: '#barDemoDepartSubject',
                    align: 'center',
                    sort: true,
                    totalRowText: '合计'
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