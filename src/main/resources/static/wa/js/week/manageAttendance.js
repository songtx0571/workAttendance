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
        var cols = [{field: 'userNumber', title: '编号', align: 'center', width: 80}
            , {field: 'userName', title: '员工名称', align: 'center', width: 80}];
        for (let i = 0; i < 31; i++) {
            col = {};
            col.field='';
            col.title=(i + 1) + '日';
            col.align= 'center';
            col.width= '65';
            col.templet=function(a){
                var j = i < 9 ? "0"+(i+1): (i + 1)
                if (a.data[j]) {
                    if (a.data[j].detail instanceof  Object) {
                        if (a.data[j].detail.type==1) {
                            var content = "'"+a.data[j].detail.workStartTime+"<br>"+a.data[j].detail.workEndTime+"'";
                            return '<span style="width: 100%;display: inline-block;" id="'+a.employeeId+''+j+'" onclick="showTime('+content+','+a.employeeId+''+ j+')">'+a.data[j].detail.workingHour+'</span>'
                        }else if (a.data[j].detail.type==0) {
                            var content = "'"+a.data[j].detail.workStartTime+"<br>无'";
                            return '<span style="width: 100%;display: inline-block;" id="'+a.employeeId+''+j+'" onclick="showTime('+content+','+a.employeeId+''+ j+')">0</span>'
                        } else  {
                            return '<span style="width: 100%;display: inline-block;" id="'+a.employeeId+''+j+'" onclick="showTime(0,'+a.employeeId+''+ j+')">0</span>'
                        }
                    } else {
                        return '<span style="width: 100%;display: inline-block;" id="'+a.employeeId+''+j+'" onclick="showTime(0,'+a.employeeId+''+ j+')">0</span>'
                    }
                } else {
                    return '<span style="width: 100%;display: inline-block;" >/</span>'
                }
            };
            cols.push(col);
        }
        cols.push({field: 'workAttendance', title: '考勤天数', align: 'center', width: 80})
        cols.push({field: 'workingHoursTotal', title: '本月工时', align: 'center', width: 80});
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/working/getManagerWorkingHours?month='+month //数据接口
            , cols: [cols]
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
                console.log(objArr[0].data[day<10?"0"+day:day])
                if ( objArr[0].data[day<10?"0"+day:day].detail.length==0)	{
                    $("#goWorkBtn").html("<span onclick=\"goWork()\">上班</span>")
                } else if (objArr[0].data[day<10?"0"+day:day].detail.type == 1) {
                    $("#goWorkBtn").html("打卡结束");
                } else if (objArr[0].data[day<10?"0"+day:day].detail.type == 0) {
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
    var monthDay = year + "-" + month + "-" + (day<10?"0"+day:day);
    var type = 0;
    if (data.data[day<10?"0"+day:day].detail.type == '0' || data.data[day<10?"0"+day:day].detail.type == '1') {
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
                if (objArr[0].data[day<10?"0"+day:day].detail.type == 0 || objArr[0].data[day<10?"0"+day:day].detail.type == 1) {
                    $("#goWorkBtn").html("打卡结束");
                } else {
                    $("#goWorkBtn").html("<span onclick=\"goWork()\">下班</span>")
                }
            } else {
                layer.alert(jsr.msg);
            }

        }, error: function (res) {
            $(".loading").css("display", 'none');
            layer.alert("操作失败");
        }
    });

}

//显示时间
function showTime(content, id) {
    if (content == 0) {
        content = "无";
    }
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.tips(content, "#" + id)
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