var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth() + 1;
var day = date.getDate();
var objArr;
$(function () {
    // 查询员工绩效信息
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
    $("#test").val(year + "-" + month);
    showTable(year + "-" + month);
    showCycleData();
})

/*显示日期*/
function showCycleData() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询工作业绩日期
        laydate.render({
            elem: '#test'
            , type: 'month'
            , trigger: 'click'//呼出事件改成click
            , done: function (value) {
                showTable(value);
            }
        });
    });
}

//查询考勤人列表
function showTable(month) {
    var win = $(window).height();
    var height = win - 90;
    layui.use(['table', "form"], function () {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/working/getManagerWorkingHours?month=' + month //数据接口
            , cols: [[ //表头
                {field: 'userNumber', title: '编号', align: 'center'},
                {field: 'userName', title: '员工名称', align: 'center'},
                {
                    field: '', title: '日期', align: 'center', templet: function (a) {
                        return $("#test").val() + "-" + day;
                    }
                },
                {
                    field: '', title: '状态', align: 'center', templet: function (a) {
                        var html = "";
                        if (a.data[day].detail.type == 0) {
                            html = "未下班打卡"
                        } else if (a.data[day].detail.type == 1)  {
                            html = "打卡结束"
                        } else  {
                            html = "未上班打卡"
                        }
                        return html;
                    }
                }
            ]]
            , done: function (res, curr, count) {
                objArr = res.data;
                $("#goWorkBtn").css("display", "revert");
                if (objArr[0].data[day].detail.type == 1) {
                    $("#goWorkBtn").html("打卡结束");
                } else if (objArr[0].data[day].detail.type == 0) {
                    $("#goWorkBtn").html("<span onclick=\"goWork()\">下班</span>")
                } else {
                    $("#goWorkBtn").html("<span onclick=\"goWork()\">上班</span>")
                }
            }
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {

        });
    });
}

function goWork() {
    var data = objArr[0];
    var monthDay = year + "-" + month + "-" + day;
    var type = 0;
    if (data.data[day].detail.type == '0' || data.data[day].detail.type == '1') {
        type = 1;
    }
    $.ajax({
        type: "POST",
        url: path + "/wa/working/postManagerWorkingHours",
        data: {employeeId: data.employeeId, monthDay: monthDay, type: type},
        dataType: "json",
        success: function (jsr) {
            if (jsr.code == 0 || jsr.code == 200) {
                showTable($("#test").val());
                if (objArr[0].data[day].detail.type == 0 || objArr[0].data[day].detail.type == 1) {
                    $("#goWorkBtn").html("打卡结束");
                } else {
                    $("#goWorkBtn").html("<span onclick=\"goWork()\">下班</span>")
                }
            } else {
                layer.alert(jsr.msg);
            }

        }, error: function (res) {
            layer.alert("今日已打卡完毕");
        }
    });

}

//上个月
function monthUpBtn() {
    var time = $("#test").val();
    var y = time.substring(0, 4);
    var m = time.substring(5, 7);
    m--;
    if (m == 0) {
        m = 12;
        y = y - 1;
    }
    if (m > 0 && m < 10) {
        m = "0" + m;
    }
    $("#test").val(y + "-" + m);
    showTable(y + "-" + m)
}

//下个月
function monthDownBtn() {
    var time = $("#test").val();
    var y = time.substring(0, 4);
    var m = time.substring(5, 7);
    m++;
    if (m == 13) {
        m = 1;
        y = Number(y) + 1;
    }
    if (m > 0 && m < 10) {
        m = "0" + m;
    }
    $("#test").val(y + "-" + m);
    showTable(y + "-" + m)

}