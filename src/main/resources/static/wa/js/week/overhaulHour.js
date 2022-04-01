var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth() + 1;
var day = date.getDate();
$(function () {
    showMonth();
    showDepart();
    if (month < 10) {
        month = "0" + month;
    }
    showTableList(year + "-" + month, "");
    $("#test15").val(year + "-" + month);
    $("#test15").val(year + "-" + month);
    var y = $("#test15").val().substr(0, 4);
    var m = $("#test15").val().substr(5, 2)
    if ((y == year && m <= month) || y < year) {
        $("#preservationBtn").css('display', "revert");
    } else {
        $("#preservationBtn").css('display', "none");
    }
});

//显示时间
function showMonth() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#test15'
            , type: 'month'
            , trigger: 'click'//呼出事件改成click
            , done: function (value) {
                var y = value.substr(0, 4);
                var m = value.substr(5, 2)
                if ((y == year && m <= month) || y < year) {
                    $("#preservationBtn").css('display', "revert");
                } else {
                    $("#preservationBtn").css('display', "none");
                }
                showTableList($("#test15").val(), $("#selDepartNameHidden").val());
            }
        });
    })
}

//显示部门
function showDepart() {
    layui.use(['form'], function () {
        var form = layui.form;
        $.ajax({
            type: "GET",
            url: path + "/wa/working/getDepMap",
            dataType: "json",
            success: function (data) {
                data = data.data;
                $("#selDepartName").empty();
                var option = "<option value='' >请选择部门</option>";
                for (var i = 0; i < data.length; i++) {
                    option += "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
                }
                $('#selDepartName').html(option);
                form.render();//菜单渲染 把内容加载进去
            }
        });
        form.on('select(selDepartName)', function (data) {
            $("#selDepartNameHidden").val(data.value);
            showTableList($("#test15").val(), $("#selDepartNameHidden").val());
        });
    })
}

function showTableList(month, projectId) {
    $("#test15").val(month);
    var div = $(".div");
    var table = "<table><thead><tr style='background: #f2f2f2;'><th>编&nbsp;号</th><th>姓名</th>";
    $(".loading").css("display", "block");
    $.ajax({
        url: "/wa/working/getOverhaulHours?departmentId=" + projectId + "&date=" + month,//请求地址
        dataType: "json",//数据格式
        type: "get",//请求方式
        success: function (data) {

            var tian = data.count;
            data = data.data;
            for (var i = 0; i < tian; i++) {
                table += "<th>" + (i + 1) + "<br />日</th>"
            }
            var w = window.innerHeight - 150;
            table += "<th style='font-weight: bold;'>本月<br />工时</th><th style='font-weight: bold;'>本月<br />考勤</th><th style='font-weight: bold;'>加班<br />工时</th></tr></thead><tbody style='height: " + w + "px;'><tr>";
            if (data == "") {
                $(".loading").css("display", "none");
                table += "<td colspan='" + (tian + 5) + "' style='text-align: center;line-height: 56px;font-size: 20px;min-width: 100%;border: none;padding-left: 10px;'>无数据！</td></tr></tbody></table>";
                div.html(table);
                return false;
            }

            for (var item in data) {
                var data1 = data[item]
                table += "<td>" + data1.userNumber + "</td><td>" + data1.userName + "</td>";

                for (var i = 1; i <= tian; i++) {
                    if (i < 10) {
                        i = "0" + i;
                    }
                    var content = "'";
                    for (var z = 0; z < data1.data[i].detail.length; z++) {
                        if (data1.data[i].detail[z].overhaulNo == "" || data1.data[i].detail[z].overhaulNo == null) {
                            data1.data[i].detail[z].overhaulNo = "无编号"
                        }
                        if (data1.data[i].detail[z].overtime == "" || data1.data[i].detail[z].overtime == null) {
                            data1.data[i].detail[z].overtime = "0"
                        }
                        content += "" + "<p>ID:" + data1.data[i].detail[z].overhaulNo + "；工时:" + data1.data[i].detail[z].workingHour + "；加班工时:" + data1.data[i].detail[z].overtime + "</p><br>";
                    }

                    content += "'"

                    if (content.length < 3) {
                        content = "'" + "无数据！" + "'";
                    }
                    var operatingTd = "'operaHourTd_" + item + "_" + i + "'";
                    table += '<td class="operaHourTd_" id="operaHourTd_' + item + "_" + i + '" onclick="showDiv(' + content + ',' + operatingTd + ')">' + data1.data[i].total + '</td>'
                }
                var overtimeCon = "'";
                var workingOvertimeId;
                workingOvertimeId = "'workingOvertime_" + item + "_" + i + "'";
                if (data1.workingOvertimeDetailList.length > 0) {
                    for (var j = 0; j < data1.workingOvertimeDetailList.length; j++) {
                        overtimeCon += "" + "<p>日期:" + data1.workingOvertimeDetailList[j].day + "；缺陷编号:" + data1.workingOvertimeDetailList[j].overhaulNo + "；加班时间:" + data1.workingOvertimeDetailList[j].overtime + "</p><br>";
                    }
                    overtimeCon += "'"
                } else {
                    overtimeCon = "'" + "无数据！" + "'";
                }
                table += '<td style="font-weight: bold;color: red;">' + data1.workingHoursTotal + '</td>' +
                    '<td style="font-weight: bold;color: red;">' + data1.workAttendance + '</td>' +
                    '<td id="workingOvertime_' + item + "_" + i + '" style="font-weight: bold;color: red;" onclick="showDiv(' + overtimeCon + ',' + workingOvertimeId + ')">' + data1.workingOvertimeTotal + '</td></tr>'
            }
            table += "</tbody></table>";
            div.html(table);
            $(".loading").css("display", "none");
        }
    })

}

function showDiv(content, id) {
    layer.closeAll();
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.tips(content, "#"+id,  {
            tips: ['1','#000'],
            time:0
        });
    })
}

//保存数据
function preservationData() {
    $(".loading").css("display", "block");
    $.ajax({
        url: path + "/wa/working/saveWorkingHour?type=1&date=" + $("#test15").val() + "&confirmType=0",//请求地址
        dataType: "json",//数据格式
        type: "get",//请求方式
        success: function (data) {
            if (data.code == 0 || data.code == 200) {
                $(".loading").css("display", "none");
            } else if (data.code == 223) {
                $(".loading").css("display", "none");
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: false,
                    area: '300px;',
                    shade: 0.8,
                    id: 'LAY_layuipro',
                    btn: ['确定', '取消'],
                    btnAlign: 'c',
                    moveType: 1,
                    content: '<div style="padding: 50px 10px 50px 17px; box-sizing: border-box; line-height: 22px; background-color: #f2f2f2; color: #000; font-weight: 500;font-size: 18px;">记录已经存在,是否覆盖他们？</div>',
                    success: function (layero) {
                        var btn = layero.find('.layui-layer-btn');
                        btn.find('.layui-layer-btn0').click(function () {
                            $(".loading").css("display", 'block');
                            $.ajax({
                                url: path + "/wa/working/saveWorkingHour?type=1&date=" + $("#test15").val() + "&confirmType=1",//请求地址
                                dataType: "json",//数据格式
                                type: "get",//请求方式
                                success: function (data1) {
                                    if (data1.code == 0 || data1.code == 200) {
                                        $(".loading").css("display", "none");
                                    } else {
                                        layer.alert(data1.msg);
                                        $(".loading").css("display", "none");
                                    }
                                }
                            })
                        });
                    }
                });
            } else {
                layer.alert(data.msg)
                $(".loading").css("display", "none");
            }
        },
        error: function (data) {
            layer.alert("操作错误");
        }
    })
}

//上个月
function monthUpBtn() {
    var time = $("#test15").val();
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
    $("#test15").val(y + "-" + m);
    var a1 = y + "-" + m;
    showTableList(a1, $("#selDepartNameHidden").val())
    if ((y == year && m <= month) || y < year) {
        $("#preservationBtn").css('display', "revert");
    } else {
        $("#preservationBtn").css('display', "none");
    }
}

//下个月
function monthDownBtn() {
    var time = $("#test15").val();
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
    $("#test15").val(y + "-" + m);
    var a2 = y + "-" + m;
    showTableList(a2, $("#selDepartNameHidden").val())
    if ((y == year && m <= month) || y < year) {
        $("#preservationBtn").css('display', "revert");
    } else {
        $("#preservationBtn").css('display', "none");
    }
}