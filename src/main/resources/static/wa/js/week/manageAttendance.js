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
    $(".loading").css("display",'block');
    var win = $(window).height();
    var height = win - 90;
    layui.use(['table', "form"], function () {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/working/getManagerWorkingHours?month='+month //数据接口
            , cols: [[ //表头
                {field: 'userNumber', title: '编号', align: 'center', width:80}
                , {field: 'userName', title: '员工名称', align: 'center', width:80}
                , {field: '', title: '01日', toolbar: '#barDemo1', align: 'center', width:65}
                , {field: '', title: '02日', toolbar: '#barDemo2', align: 'center', width:65}
                , {field: '', title: '03日', toolbar: '#barDemo3', align: 'center', width:65}
                , {field: '', title: '04日', toolbar: '#barDemo4', align: 'center', width:65}
                , {field: '', title: '05日', toolbar: '#barDemo5', align: 'center', width:65}
                , {field: '', title: '06日', toolbar: '#barDemo6', align: 'center', width:65}
                , {field: '', title: '07日', toolbar: '#barDemo7', align: 'center', width:65}
                , {field: '', title: '08日', toolbar: '#barDemo8', align: 'center', width:65}
                , {field: '', title: '09日', toolbar: '#barDemo9', align: 'center', width:65}
                , {field: '', title: '10日', toolbar: '#barDemo10', align: 'center', width:65}
                , {field: '', title: '11日', toolbar: '#barDemo11', align: 'center', width:65}
                , {field: '', title: '12日', toolbar: '#barDemo12', align: 'center', width:65}
                , {field: '', title: '13日', toolbar: '#barDemo13', align: 'center', width:65}
                , {field: '', title: '14日', toolbar: '#barDemo14', align: 'center', width:65}
                , {field: '', title: '15日', toolbar: '#barDemo15', align: 'center', width:65}
                , {field: '', title: '16日', toolbar: '#barDemo16', align: 'center', width:65}
                , {field: '', title: '17日', toolbar: '#barDemo17', align: 'center', width:65}
                , {field: '', title: '18日', toolbar: '#barDemo18', align: 'center', width:65}
                , {field: '', title: '19日', toolbar: '#barDemo19', align: 'center', width:65}
                , {field: '', title: '20日', toolbar: '#barDemo20', align: 'center', width:65}
                , {field: '', title: '21日', toolbar: '#barDemo21', align: 'center', width:65}
                , {field: '', title: '22日', toolbar: '#barDemo22', align: 'center', width:65}
                , {field: '', title: '23日', toolbar: '#barDemo23', align: 'center', width:65}
                , {field: '', title: '24日', toolbar: '#barDemo24', align: 'center', width:65}
                , {field: '', title: '25日', toolbar: '#barDemo25', align: 'center', width:65}
                , {field: '', title: '26日', toolbar: '#barDemo26', align: 'center', width:65}
                , {field: '', title: '27日', toolbar: '#barDemo27', align: 'center', width:65}
                , {field: '', title: '28日', toolbar: '#barDemo28', align: 'center', width:65}
                , {field: '', title: '29日', toolbar: '#barDemo29', align: 'center', width:65}
                , {field: '', title: '30日', toolbar: '#barDemo30', align: 'center', width:65}
                , {field: '', title: '31日', toolbar: '#barDemo31', align: 'center', width:65}
            ]]
            ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
                if (res.code == 0 || res.code == 200) {
                    $(".loading").css("display",'none');
                } else {
                    $(".loading").css("display",'none');
                    layer.alert(res.msg)
                }
            }
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
    $(".loading").css("display",'block');
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
            $(".loading").css("display",'none');
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
            $(".loading").css("display",'none');
            layer.alert("今日已打卡完毕");
        }
    });

}

//显示时间
function showTime(content, id) {
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.tips(content, "#"+id)
    })
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