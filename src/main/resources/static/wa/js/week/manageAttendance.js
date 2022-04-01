var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth() + 1;
var day = date.getDate();
var objType;
$(function () {
    if (month < 10) {
        month = "0" + month;
    }
    $("#test").val(year + "-" + month);
    showTable(year + "-" + month);
    showCycleData();
    showGoWorkBtn();
    var y = $("#test").val().substr(0,4);
    var m = $("#test").val().substr(5,2)
    if ((y == year && m <= month) || y < year){
        $("#preservationBtn").css('display',"revert");
    } else {
        $("#preservationBtn").css('display',"none");
    }
})


function showGoWorkBtn () {
    $.ajax({
        type: "get",
        url: path + "/wa/working/getManagerWorkingType",
        dataType: "json",
        success: function (jsr) {
            objType = jsr.data.type;
            if (jsr.code == 0 || jsr.code == 200) {
                if (jsr.data.type == 1) {
                    $("#goWorkBtn").html("打卡结束");
                } else if (jsr.data.type == 0) {
                    $("#goWorkBtn").html("<span onclick=\"goWork()\">下班</span>")
                } else {
                    $("#goWorkBtn").html("<span onclick=\"goWork()\">上班</span>")
                }
            } else {
                layer.alert(jsr.msg);
            }
        }
    });

}

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
                var y = value.substr(0,4);
                var m = value.substr(5,2)
                if ((y == year && m <= month) || y < year){
                    $("#preservationBtn").css('display',"revert");
                } else {
                    $("#preservationBtn").css('display',"none");
                }
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
                            return '<span style="width: 100%;display: inline-block;line-height: 39px;" id="'+a.employeeId+''+j+'" onclick="showTime('+content+','+a.employeeId+''+ j+')">'+a.data[j].detail.workingHour+'</span>'
                        }else if (a.data[j].detail.type==0) {
                            var content = "'"+a.data[j].detail.workStartTime+"<br>无'";
                            return '<span style="width: 100%;display: inline-block;line-height: 39px;" id="'+a.employeeId+''+j+'" onclick="showTime('+content+','+a.employeeId+''+ j+')">0</span>'
                        } else  {
                            return '<span style="width: 100%;display: inline-block;line-height: 39px;" id="'+a.employeeId+''+j+'" onclick="showTime(0,'+a.employeeId+''+ j+')">0</span>'
                        }
                    } else {
                        return '<span style="width: 100%;display: inline-block;line-height: 39px;" id="'+a.employeeId+''+j+'" onclick="showTime(0,'+a.employeeId+''+ j+')">0</span>'
                    }
                } else {
                    return '<span style="width: 100%;display: inline-block;line-height: 39px;" >/</span>'
                }
            };
            cols.push(col);
        }
        cols.push({field: 'workAttendance', title: '考勤天数', align: 'center', width: 80})
        cols.push({field: 'workingHoursTotal', title: '本月工时', align: 'center', width: 80});
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/working/getManagerWorkingHours?date='+month //数据接口
            , cols: [cols]
            ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
                if (res.code == 0 || res.code == 200) {
                    $(".loading").css("display",'none');
                } else {
                    $(".loading").css("display",'none');
                    layer.alert(res.msg)
                }
            }
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {

        });
    });
}

function goWork() {
    $(".loading").css("display", 'block');
    var dataType = objType;
    var monthDay = year + "-" + month + "-" + (day < 10 ? "0" + day : day);
    if (dataType  == 0) {
        dataType = 1;
    } else {
        dataType = 0;
    }
    $.ajax({
        type: "POST",
        url: path + "/wa/working/postManagerWorkingHours",
        data: {monthDay: monthDay, type: dataType},
        dataType: "json",
        success: function (jsr) {
            $(".loading").css("display", 'none');
            if (jsr.code == 0 || jsr.code == 200) {
                showTable($("#test").val());
                if (objType == 0 || objType == 1) {
                    $("#goWorkBtn").html("打卡结束");
                } else {
                    objType = 0;
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

//保存数据
function preservationData () {
    $(".loading").css("display","block");
    $.ajax({
        url: path +"/wa/working/saveWorkingHour?type=2&date=" + $("#test").val()+"&confirmType=0",//请求地址
        dataType: "json",//数据格式
        type: "get",//请求方式
        success: function (data) {
            if (data.code == 0 || data.code == 200) {
                $(".loading").css("display","none");
            } else if (data.code == 223) {
                $(".loading").css("display","none");
                layer.open({
                    type: 1,
                    title: false ,
                    closeBtn: false,
                    area: '300px;',
                    shade: 0.8,
                    id: 'LAY_layuipro' ,
                    btn: ['确定', '取消'],
                    btnAlign: 'c',
                    moveType: 1 ,
                    content: '<div style="padding: 50px 10px 50px 17px; box-sizing: border-box; line-height: 22px; background-color: #f2f2f2; color: #000; font-weight: 500;font-size: 18px;">记录已经存在,是否覆盖他们？</div>',
                    success: function (layero) {
                        var btn = layero.find('.layui-layer-btn');
                        btn.find('.layui-layer-btn0').click(function () {
                            $(".loading").css("display", 'block');
                            $.ajax({
                                url: path + "/wa/working/saveWorkingHour?type=2&date=" + $("#test").val()+"&confirmType=1",//请求地址
                                dataType: "json",//数据格式
                                type: "get",//请求方式
                                success: function (data1) {
                                    if (data1.code == 0 || data1.code == 200) {
                                        $(".loading").css("display","none");
                                    } else {
                                        layer.alert(data1.msg);
                                        $(".loading").css("display","none");
                                    }
                                }
                            })
                        });
                    }
                });
            } else {
                layer.alert(data.msg)
                $(".loading").css("display","none");
            }
        },
        error: function (data) {
            layer.alert("操作错误");
        }
    })
}

//显示时间
function showTime(content, id) {
    if (content == 0) {
        content = "无";
    }
    layer.closeAll();
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.tips(content, "#"+id,  {
            tips: ['1','#000'],
            time:0
        });
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
    if ((y == year && m <= month) || y < year){
        $("#preservationBtn").css('display',"revert");
    } else {
        $("#preservationBtn").css('display',"none");
    }
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
    if ((y == year && m <= month) || y < year){
        $("#preservationBtn").css('display',"revert");
    } else {
        $("#preservationBtn").css('display',"none");
    }
}