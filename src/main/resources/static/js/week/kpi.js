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
            , done: function (value) {
                $("#selStartTime").val(value);
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
        });
    });
}
//查询工时数据
function showKpi() {
    if ( $("#selDepartNameHidden").val() == "" ||  $("#selDepartNameHidden").val() == "0"){
        alert("请选择部门");
        return;
    }
    var depart = $("#selDepartNameHidden").val();
    var startTime = $("#selStartTime").val();
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: 500
            , url: path + '/wa/kpi/getKPIList?depart='+depart+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide:true,width: 120},
                {field: 'userName', title: '员工', align: 'center'},
                {field: 'frequency', title: '月巡检次数',sort :true, align: 'center',event: 'monthNum', style:'color: red;'},
                {field: 'point', title: '月巡检点数',sort :true, align: 'center',event: 'monthPoint', style:'color: red;'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            var userId = data.id;
            var startTime = $("#selDepartNameHidden").val();
            if (obj.event == 'monthNum') {//月巡检次数
                layer.open({
                    type: 1
                    ,id: 'showMonthNum' //防止重复弹出
                    ,content: $(".showMonthNum")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
                num(userId,startTime)
            } else if(obj.event == 'monthPoint'){//月巡检点数
                point(userId,startTime)
                layer.open({
                    type: 1
                    ,id: 'showMonthPoint' //防止重复弹出
                    ,content: $(".showMonthPoint")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
            }
        });
    });
}
function num(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoNum'
            , height: 500
            , url: path + '/wa/kpi/toFrequency?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'inspectionStaTime', title: '开始执行时间', align: 'center'},
                {field: 'inspectionEndTime', title: '实际结束时间', align: 'center'},
                {field: 'inspectionEndTheoryTime', title: '理论结束时间',sort :true, align: 'center',event: 'monthNum'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
        })
    });
}
function point(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoPoint'
            , height: 500
            , url: path + '/wa/kpi/toPoint?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'equipment', title: '设备', align: 'center'},
                {field: 'measuringType', title: '测定类型', align: 'center'},
                {field: 'measuringTypeData', title: '数据',sort :true, align: 'center'},
                {field: 'unit', title: '单位',sort :true, align: 'center'},
                {field: 'created', title: '执行时间',sort :true, align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
        })
    });
}