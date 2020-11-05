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
                $("#selStartTime").val(value);
            }
        });
    })
}
//查询工时数据
function showKpi() {
    if ( $("#startTime").val() == ""){
        alert("请选择日期");
        return;
    }
    var startTime = $("#selStartTime").val();
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: 500
            , url: path + '/wa/kpi/getInformKPIList?startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'Id', title: '编号', align: 'center', hide:true,width: 120},
                {field: 'userName', title: '员工', align: 'center'},
                {field: 'createdCount', title: '创建数',sort :true, align: 'center',event: 'createdCount', style:'cursor: pointer;color: red;'},
                {field: 'selCount', title: '查看数',sort :true, align: 'center',event: 'selCount', style:'cursor: pointer;color: red;'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            var userId = data.Id;
            var startTime = $("#selStartTime").val();
            if (obj.event == 'createdCount') {//创建数
                countCreate(userId,startTime);
                layer.open({
                    type: 1
                    ,id: 'createdCountDiv' //防止重复弹出
                    ,content: $(".createdCountDiv")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
            } else if(obj.event == 'selCount'){//查看数
                countSel(userId,startTime);
                layer.open({
                    type: 1
                    ,id: 'selCountDiv' //防止重复弹出
                    ,content: $(".selCountDiv")
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
function countCreate(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoCC'
            , height: 500
            , url: path + '/wa/kpi/getCreatedList?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'created', title: '通知时间', align: 'center'},
                {field: 'title', title: '标题', align: 'center'},
                {field: 'content', title: '内容',sort :true, align: 'center'},
                {field: 'createdByName', title: '发起人',sort :true, align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test1)', function (obj) {
        })
    });
}
function countSel(userId,startTime) {
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demoSC'
            , height: 500
            , url: path + '/wa/kpi/getSelList?userId='+userId+'&startTime='+startTime //数据接口
            , page: {
                curr: 1
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'created', title: '通知时间', align: 'center'},
                {field: 'title', title: '标题', align: 'center'},
                {field: 'content', title: '内容',sort :true, align: 'center'},
                {field: 'createdByName', title: '查看人',sort :true, align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test2)', function (obj) {
        })
    });
}