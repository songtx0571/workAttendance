var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth()+1;
var day = date.getDate();
$(function () {
    showMonth();
    showDepart();
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
    showTableList(year + "-" + month,"");
    $("#test15").val(year + "-" + month);
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
    $("#test15").val(month);
    $(".loading").css("display","block");
    var div = $(".div");
    var table = "<table class='layui-table'><thead><tr><th>编号</th><th>姓名</th>";
    $.ajax({
        url: "/wa/working/getOverhaulHours?departmentId="+projectId+"&date="+month,//请求地址
        dataType: "json",//数据格式
        type: "get",//请求方式
        success: function (data) {

            var tian = data.count;
            data = data.data;
            for (var i = 0; i < tian; i ++) {
                table += "<th>"+(i+1)+"日</th>"
            }
            table += "<th style='font-weight: bold;'>本月工时</th><th style='font-weight: bold;'>加班工时</th></tr></thead><tbody><tr>";
            if (data == "") {
                $(".loading").css("display","none");
                table += "<td colspan='"+(tian+4)+"' style='text-align: center;line-height: 56px;font-size: 20px;'>无数据！</td></tr></tbody></table>";
                div.html(table);
                return false;
            }

            for (var item in data) {
                var data1 = data[item]
                table += "<td>"+data1.userNumber+"</td><td>"+data1.userName+"</td>";

                for (var i = 1; i <= tian; i ++) {
                    if (i < 10) {
                        i = "0" + i;
                    }
                    var content = "'";
                    for (var z= 0; z < data1.data[i].detail.length; z ++) {
                        if (data1.data[i].detail[z].overhaulNo == "" || data1.data[i].detail[z].overhaulNo == null) {
                            data1.data[i].detail[z].overhaulNo = "无编号"
                        }
                        if (data1.data[i].detail[z].overtime == "" || data1.data[i].detail[z].overtime == null) {
                            data1.data[i].detail[z].overtime = "0"
                        }
                        content += ""+"<p>ID:"+data1.data[i].detail[z].overhaulNo+"；工时:"+data1.data[i].detail[z].workingHour+"；加班工时:"+data1.data[i].detail[z].overtime+"</p><br>";
                    }

                    content += "'"

                    if (content.length < 3 ) {
                        content = "'"+"无数据！"+"'";
                    }
                    var operatingTd = "'operaHourTd_"+item+"_"+i+"'";
                    table += '<td class="operaHourTd_" id="operaHourTd_'+item+"_"+i+'" onclick="showDiv('+content+','+operatingTd+')">'+data1.data[i].total+'</td>'
                }
                table += "<td style='font-weight: bold;color: red;'>"+data1.all+"</td><td style='font-weight: bold;color: red;'>"+data1.over+"</td></tr>"
            }
            table += "</tbody></table>";
            div.html(table);
            $(".loading").css("display","none");
        }
    })

}
function showDiv (content, id) {
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.tips(content, "#"+id)
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
}
//取消
function cancel() {
}