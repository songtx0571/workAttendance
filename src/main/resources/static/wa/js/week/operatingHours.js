var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth()+1;
var day = date.getDate();
$(function () {
    showMonth();
    showDepart();
    if (month < 10) {
        month = "0" + month;
    }
    showTableList(year + "-" + month,"");
    $("#test15").val(year + "-" + month);
    var y = $("#test15").val().substr(0,4);
    var m = $("#test15").val().substr(5,2)
    if ((y == year && m <= month) || y < year){
        $("#preservationBtn").css('display',"revert");
    } else {
        $("#preservationBtn").css('display',"none");
    }
});

//显示时间
function showMonth() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#test15'
            , type: 'month'
            ,trigger: 'click'//呼出事件改成click
            ,done: function(value){
                var y = value.substr(0,4);
                var m = value.substr(5,2)
                if ((y == year && m <= month) || y < year){
                    $("#preservationBtn").css('display',"revert");
                } else {
                    $("#preservationBtn").css('display',"none");
                }
                showTableList($("#test15").val(),$("#selDepartNameHidden").val());
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
            showTableList($("#test15").val(),$("#selDepartNameHidden").val());
        });
    })
}
function  showTableList (month,projectId) {
    // $("#test15").val(month)
    // var div = $(".div");
    // var table = "<table><thead><tr style='background: #f2f2f2;'><th>编号</th><th>姓名</th>";
    // $(".loading").css("display","block");
    // $.ajax({
    //     url: path + '/wa/working/getOperatingHoursList',//请求地址
    //     dataType: "json",//数据格式
    //     type: "get",//请求方式
    //     data: {date: month,departmentId:projectId},
    //     success: function (data) {
    //         var tian = data.count;
    //         data = data.data;
    //         for (var i = 0; i < tian; i ++) {
    //             table += "<th>"+(i+1)+"<br />日</th>"
    //         }
    //         var w = window.innerHeight - 150;
    //         table += "<th style='font-weight: bold;'>本月<br />工时</th><th style='font-weight: bold;'>考勤<br />天数</th><th style='font-weight: bold;'>加班<br />工时</th></tr></thead><tbody style='height: "+w+"px;'><tr>";
    //         if (data == "") {
    //             $(".loading").css("display","none");
    //             table += "<td colspan='"+(tian+5)+"' style='text-align: center;line-height: 56px;font-size: 20px;min-width: 100%;border: none;padding-left: 10px;'>无数据！</td></tr></tbody></table>";
    //             div.html(table);
    //             return false;
    //         }
    //
    //         for (var item in data) {
    //             var data1 = data[item]
    //             table += "<td>"+data1.userNumber+"</td><td>"+data1.userName+"</td>";
    //             for (var i = 1; i <= tian; i ++) {
    //                 if (i < 10) {
    //                     i = "0" + i;
    //                 }
    //                 var content = "'";
    //                 if (data1.data[i].detail.length < 3) {
    //                     data1.data[i].detail.successorTime = "无";
    //                     data1.data[i].detail.tradersTime = "无";
    //                 }
    //                 if (data1.data[i].detail.successorTime == "" || data1.data[i].detail.successorTime == null) {
    //                     data1.data[i].detail.successorTime ="无";
    //                 }
    //                 if (data1.data[i].detail.tradersTime == "" || data1.data[i].detail.tradersTime == null) {
    //                     data1.data[i].detail.tradersTime ="无";
    //                 }
    //                 content += ""+"<p>接班时间:"+data1.data[i].detail.successorTime+"；</p><p>交班时间:"+data1.data[i].detail.tradersTime+"</p><br>";
    //                 content += "'"
    //
    //                 if (content.length < 3 ) {
    //                     content = "'"+"无数据！"+"'";
    //                 }
    //
    //                 var operatingTd = "'operatingTd_"+item+"_"+i+"'";
    //                 table += '<td class="operatingTd" id="operatingTd_'+item+"_"+i+'" onclick="showDiv('+content+','+operatingTd+')">'+data1.data[i].total+'</td>'
    //             }
    //             table += "<td style='font-weight: bold;color: red;'>"+data1.workingHoursTotal+"</td><td style='font-weight: bold;color: red;'>"+data1.workAttendance+"</td><td style='font-weight: bold;color: red;'>"+data1.workingOvertimeTotal+"</td></tr>"
    //         }
    //         table += "</tbody></table>";
    //         div.html(table);
    //         $(".loading").css("display","none");
    //     }
    // })


    $(".loading").css("display",'block');
    var win = $(window).height();
    var height = win - 90;
    layui.use(['table', "form"], function () {
        var table = layui.table;
        var cols = [{field: 'userNumber', title: '编号', align: 'center', width: 80, fixed: 'left'}
            , {field: 'userName', title: '员工名称', align: 'center', width: 80, fixed: 'left'}];
        for (let i = 0; i < 31; i++) {
            col = {};
            col.field='';
            col.title=(i + 1) + '日';
            col.align= 'center';
            col.width= '65';
            col.templet=function(a){
                var j = i < 9 ? "0"+(i+1): (i + 1)
                var content = "'";
                if (a.data[j]) {
                    if (a.data[j].detail.toString().length < 3) {
                        a.data[j].detail.successorTime = "无";
                        a.data[j].detail.tradersTime = "无";
                    }
                    if (a.data[j].detail.successorTime == "" || a.data[j].detail.successorTime == null) {
                        a.data[j].detail.successorTime = "无";
                    }
                    if (a.data[j].detail.tradersTime == "" || a.data[j].detail.tradersTime == null) {
                        a.data[j].detail.tradersTime = "无";
                    }
                    content += "" + "<p>接班时间:" + a.data[j].detail.successorTime + "；</p><p>交班时间:" + a.data[j].detail.tradersTime + "</p><br>";
                    content += "'"
                    return '<span style="width: 100%;display: inline-block;line-height: 39px;" id="' + a.employeeId + '' + j + '" onclick="showDiv(' + content + ',' + a.employeeId + '' + j + ')">' + a.data[j].total + '</span>'
                } else {
                    return '<span style="width: 100%;display: inline-block;line-height: 39px;" >/</span>'
                }
            };
            cols.push(col);
        }
        cols.push({field: 'workingHoursTotal', title: '本月工时', align: 'center', width: 80, style: 'color:red;'})
        cols.push({field: 'workAttendance', title: '考勤天数', align: 'center', width: 80, style: 'color:red;'})
        cols.push({field: 'workingOvertimeTotal', title: '加班工时', align: 'center', width: 80, style: 'color:red;'});
        table.render({
            elem: '#demo'
            , height: height
            , url: path + "/wa/working/getOperatingHoursList?date=" + month + "&departmentId=" + projectId //数据接口
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
function showDiv (content, id) {
    layer.closeAll();
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.tips(content, "#"+id,  {
            tips: ['3','#000'],
            time:0
        });
    })
}

//保存数据
function preservationData () {
    $(".loading").css("display","block");
    //////0运行考勤,1检修考勤,2管理考勤
    $.ajax({
        url:  path +"/wa/working/saveWorkingHour?type=0&date=" + $("#test15").val()+"&confirmType=0",//请求地址
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
                                url: path + "/wa/working/saveWorkingHour?type=0&date=" + $("#test15").val()+"&confirmType=1",//请求地址
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

//上个月
function monthUpBtn() {
    var time = $("#test15").val();
    var y = time.substring(0,4);
    var m = time.substring(5,7);
    m --;
    if (m == 0) {
        m = 12;
        y = y - 1;
    }
    if (m > 0 && m < 10){
        m = "0" + m;
    }
    $("#test15").val(y+"-"+m);
    var a1 = y+"-"+m;
    showTableList(a1,$("#selDepartNameHidden").val())
    if ((y == year && m <= month) || y < year){
        $("#preservationBtn").css('display',"revert");
    } else {
        $("#preservationBtn").css('display',"none");
    }
}
//下个月
function monthDownBtn() {
    var time = $("#test15").val();
    var y = time.substring(0,4);
    var m = time.substring(5,7);
    m ++;
    if (m == 13){
        m = 1;
        y = Number(y) + 1;
    }
    if (m > 0 && m < 10){
        m = "0" + m;
    }
    $("#test15").val(y+"-"+m);
    var a2 = y+"-"+m;
    showTableList(a2,$("#selDepartNameHidden").val())
    if ((y == year && m <= month) || y < year){
        $("#preservationBtn").css('display',"revert");
    } else {
        $("#preservationBtn").css('display',"none");
    }
}