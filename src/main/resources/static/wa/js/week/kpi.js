var path = "";
$(function(){
    showDate();
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
                showKpi(value)
            }
        });
    })
}
//查询工时数据
function showKpi(startTime) {
    var win = $(window).height();
    var height = win-100;
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/kpi/getKPIList?startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide:true,width: 120},
                {field: 'userNumber', title: '员工编号', align: 'center'},
                {field: 'companyName', title: '公司', align: 'center'},
                {field: 'departmentName', title: '部门', align: 'center'},
                {field: 'userName', title: '员工', align: 'center'},
                {field: 'sex', title: '性别', align: 'center'},
                {field: 'frequency', title: '月巡检次数',sort :true, align: 'center', event: 'monthNum', style:'cursor: pointer;color: red;'},
                {field: 'point', title: '月巡检点数',sort :true, align: 'center', event: 'monthPoint', style:'cursor: pointer;color: red;'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            var userId = data.id;
            if (obj.event == 'monthNum') {//月巡检次数
                num(userId,startTime);
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
            } else if(obj.event == 'monthPoint'){//月巡检点数
                point(userId,startTime);
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
            , url: path + '/wa/kpi/getFrequencyList?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'inspectionStaTime', title: '开始执行时间', align: 'center'},
                {field: 'inspectionEndTime', title: '实际结束时间', align: 'center'},
                {field: 'inspectionEndTheoryTime', title: '理论结束时间',sort :true, align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test1)', function (obj) {
        })
    });
}
function point(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoPoint'
            , height: 500
            , url: path + '/wa/kpi/getPointList?userId='+userId+'&startTime='+startTime //数据接口
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
        table.on('tool(test2)', function (obj) {
        })
    });
}