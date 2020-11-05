var path = "";
$(function(){
    showDate();
    showDepartName();
});
//显示时间
function showDate() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询所有数据日期
        //年月选择器
        laydate.render({
            elem: '#test15'
            ,type: 'month'
            ,trigger: 'click'//呼出事件改成click
            , done: function (value) {
                $("#selStartTime").val(value);
                $("#dayTime").css("display","inline-block");
            }
        });
        laydate.render({
            elem: '#test16'
            ,trigger: 'click'//呼出事件改成click
            ,format: 'dd'
            , done: function (value) {
                var day = $("#selStartTime").val()+"-"+value;
                $("#selDayTime").val(day);
                console.log(day)
            }
        });
    })
}
//显示部门和员工
function showDepartName() {
    layui.use(['form'], function () {
        var form = layui.form;
        $.ajax({
            type: "GET",
            url: path + "/wa/department/getDepartmentList",
            dataType: "json",
            success: function (data) {
                $("#selDepartName").empty();
                var option = "<option value='0' >请选择部门</option>";
                for (var i = 0; i < data.length; i++) {
                    option += "<option value='" + data[i].id + "'>" + data[i].text + "</option>"
                }
                $('#selDepartName').html(option);
                form.render();//菜单渲染 把内容加载进去
            }
        });
        form.on('select(selDepartName)', function (data) {
            $("#selDepartNameHidden").val(data.value);
            var getValue = $("#selDepartNameHidden").val();
            $.ajax({
                type: "GET",
                url: path + "/getNameByProjectId",
                data:{'projectId':getValue},
                dataType: "json",
                success: function (data) {
                    $("#selEmployeeName").empty();
                    var option = "<option value='0' >请选择人员</option>";
                    for (var i = 0; i < data.length; i++) {
                        option += "<option value='" + data[i].id + "'>" + data[i].text + "</option>"
                    }
                    $('#selEmployeeName').html(option);
                    form.render();//菜单渲染 把内容加载进去
                }
            });
            form.on('select(selEmployeeName)', function (data) {
                console.log(data)
                $("#selEmployeeNameHidden").val(data.value);
            });
        });
    });
}
//查询工时数据
function selShowLaborList() {
    if ( $("#selDepartNameHidden").val() == "" ||  $("#selDepartNameHidden").val() == "0"){
        alert("请选择部门");
        return;
    }
    if ( $("#selEmployeeNameHidden").val() == "" ||  $("#selEmployeeNameHidden").val() == "0"){
        alert("请选择员工");
        return;
    }
    var userName= $("#selEmployeeNameHidden").val();
    var MonthDate= $("#selStartTime").val();
    var DayDateT = $("#selDayTime").val();
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: 500
            , url: path + '/wa/attendance/showLaborAll?userName='+userName+'&MonthDate='+MonthDate+'&DayDateT='+DayDateT //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide:true},
                {field: 'maintenanceId', title: '', align: 'center', hide:true},
                {field: 'people', title: '员工号', align: 'center', hide:true},
                {field: 'type', title: '类型', align: 'center', hide:true},
                {field: 'peopleName', title: '员工名称', align: 'center', hide:true},
                {field: 'defectNumber', title: '故障号',align: 'center',sort: true},
                {field: 'content', title: '工作详情', align: 'center',sort: true},
                {field: 'workingHours', title: '工时详情', align: 'center',sort: true},
                {field: 'datetime', title: '创建时间',align: 'center',sort: true}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
        });
    });
}

