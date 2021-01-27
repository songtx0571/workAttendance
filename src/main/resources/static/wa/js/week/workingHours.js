var path = "";
$(function(){
    showDate();
});
//显示时间
function showDate() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
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
            , url: path + '/wa/kpi/getWorkHoursList?startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide:true,width: 120},
                {field: 'name', title: '员工', align: 'center'},
                {field: 'workingHours', title: '月度总工时',sort :true, align: 'center',event: 'workingHour', style:'color: red;'},
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            var userId = data.id;
            if (obj.event == 'workingHour') {//月度总工时
                layer.open({
                    type: 1
                    ,id: 'showWorkingHour' //防止重复弹出
                    ,content: $(".showWorkingHour")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
                workHour(userId,startTime)
            }
        });
    });
}
//月度总工时
function workHour(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoWH'
            , height: 500
            , url: path + '//wa/kpi/getSelWorkHoursList?userId='+userId+'&startTime='+startTime+'&depart='+$("#selDepartNameHidden").val() //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'day', title: '日期', align: 'center'},
                {field: 'workHours', title: '工时', align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test1)', function (obj) {
        })
    });
}
