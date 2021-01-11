var index = 0;
var index1 = 0;
var path = "";
$(function(){
    //显示考核日期
    showCycleData();
    // 查询员工绩效信息
    showAchievementsList("");
});
/*显示考核日期*/
function showCycleData() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询所有数据日期
        laydate.render({
            elem: '#test3'
            , type: 'month'
            ,trigger: 'click'//呼出事件改成click
            ,done: function(value){
                $("#cycleDataHidden").val(value);
                // 查询员工绩效信息
                showAchievementsList(value);
            }
        });
        //查询工作业绩日期
        laydate.render({
            elem: '#test4'
            , type: 'month'
            ,trigger: 'click'//呼出事件改成click
            ,done: function(value){
                $("#cycleDataHidden1").val(value);
                showAchievement();
            }
        });
        //添加工作业绩日期
        laydate.render({
            elem: '#test5'
            , type: 'month'
            ,trigger: 'click'//呼出事件改成click
            ,done: function(value){
                $("#addCycleDataHidden").val(value);
            }
        });
        //查询工作行为日期
        laydate.render({
            elem: '#test6'
            , type: 'month'
            ,trigger: 'click'//呼出事件改成click
            ,done: function(value){
                $("#cycleDataHidden2").val(value);
                showBehavior();
            }
        });
    });
}
/*******************************工作业绩**********************************************/
/*查询员工绩效信息*/
function showAchievementsList(cycle){
    var win = $(window).height();
    var height = win - 100;
    layui.use('table', function(){
        var table = layui.table;
        table.render({
            elem: '#demo'
            ,height: height
            ,url: path + '/wa/achievements/getAssessment?cycle='+  cycle //数据接口
            ,toolbar: true
            ,cols: [[ //表头
                {field: 'userNumber', title: '编号', width:80, sort: true}
                ,{field: 'name', title: '姓名', width:100}
                ,{field: 'score2', title: '业绩合计', event: 'setSign', style:'cursor: pointer;color:red;', sort: true,align:'center'}
                ,{field: 'zhiban', title: '值班天数', sort: true}
                ,{field: 'kaoqin', title: '考勤天数', sort: true}// 1 在职
                ,{field: 'netPerformance', title: '净绩效', sort: true}
                ,{field: 'comprehensivePerformance', title: '综合绩效', sort: true}
                ,{fixed: '', title:'操作', toolbar: '#barDemo11',align:'center', width:250}
            ]]
            ,done: function(res, curr, count){
            }
        });
        //监听行工具事件
        table.on('tool(test)', function(obj) {
            var data = obj.data;
            // 将data转为字符串
            var jStr = "{ ";
            for(var item in data){
                jStr += "'"+item+"':'"+data[item]+"',";
            }
            jStr += " }";
            $("#showSetSignData").text(jStr);
            //工作业绩员工的编号姓名
            $("#userNumber").text(data.userNumber);
            $("#userName").text(data.name);
            //工作行为员工的编号姓名
            $("#userNumber1").text(data.userNumber);
            $("#userName1").text(data.name);
            if (obj.event == 'showAchievement') {//工作业绩考核
                $("#test4").val("");
                $("#achievementTable").css("display","none");
                index1=layer.open({
                    type: 1
                    ,id: 'showAchievementDiv' //防止重复弹出
                    ,content: $(".showAchievementDiv")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
            } else if (obj.event == 'showBehavior') {//工作行为考核
                $("#test6").val("");
                index=layer.open({
                    type: 1
                    ,id: 'showBehaviorDiv' //防止重复弹出
                    ,content: $(".showBehaviorDiv")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                    }
                    ,yes: function(){
                    }
                });
            }
            else if(obj.event == 'setSign'){
                $("#employeeIdHidden").val(data.id);
                index=layer.open({
                    type: 1
                    ,id: 'showSetSign' //防止重复弹出
                    ,content: $(".showSetSign")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['100%', '100%']
                    ,success: function () {
                        showSetSign();
                    }
                    ,yes: function(){
                        showSetSign();
                    }
                });
            }
        });
    });
}
//将字符串转变为对象
function jsonHtml() {
    var res = $("#showSetSignData").text().substring(0);
    // 将字符串转换为json
    function strToJson(str){
        var json = eval('(' + str + ')');
        res = json;
    }
    strToJson(res);
    $("#employeeIdHidden").val(res.id);
    $("#employeeIdHidden1").val(res.id);
    $("#employeeIdHidden2").val(res.id);
}
//查看业绩合计
function showSetSign() {
    var cycle = $("#cycleDataHidden").val();
    var id = $("#employeeIdHidden").val();
    $.ajax({
        url: path + '/wa/achievements/findPeAcc',//请求地址
        dataType: "json",//数据格式
        type: "get",//请求方式
        data: {"employeeId": id, "cycle": cycle},
        success: function (data) {
            var tbody = $(".showSetSignTbody");
            tbody.html("");

            tbody.html("<tr id='AssessmentTr'></tr>");
            var tr = $("#AssessmentTr");
            var td = "";
            for (var i = 0; i < data.length; i ++) {
                if(data[i].score == ''){
                    data[i].score=0;
                }
                parseInt(data[i].score);
                td += '<td>'+ data[i].score + '</td>';
            }
            tr.html(td);
        }
    })
}
//查询考核数据
function showAchievement() {
    jsonHtml();
    var cycle = $("#cycleDataHidden1").val();
    if(cycle == "" || cycle.length <= 0){
        layer.close(index);
        layer.alert("请选择考核日期");
        return;
    }
    $("#achievementTable").css("display","block");
    var id = $("#employeeIdHidden").val();
    $.ajax({
        type: "GET",
        url: path + '/wa/achievements/findPeAcc?cycle=' + cycle + '&employeeId=' + id,
        dataType: "json",
        success: function(data){
            if (data == "" || data == null) {
                layer.alert("无数据");
                return;
            }
            var tbody = document.getElementById("achievementTbody");
            tbody.innerHTML = "";
            for(var i=0;i<data.length;i++){
                $("#achievementIdHidden").val(data[i].id);
                var tr = document.createElement("tr");
                tr.setAttribute("class","achievementTr");
                var td = "<td><textarea rows='3' class='readonly1 workTasks"+data[i].id+"'>"+data[i].workTasks+"</textarea></td>" +
                    "<td><textarea rows='3' class='readonly1 access"+data[i].id+"'>"+data[i].access+"</textarea></td>" +
                    "<td><input type='text'  class='readonly1 detail"+data[i].id+"' value='"+data[i].detail+"' /></td>" +
                    "<td><input type='text'  class='readonly1 score"+data[i].id+"' value='"+data[i].score+"' /></td>" +
                    "<td><input type='text'  class='readonly1 weights"+data[i].id+"' value='"+data[i].weights+"'/></td>" +
                    "<td><input type='button' value='修改' class='layui-btn' onclick='updAchievement("+data[i].id+")' />" +
                    "<input type='button' value='删除' class='layui-btn' onclick='delectAchievement("+data[i].id+")' /></td>";
                tr.innerHTML = td;
                tbody.appendChild(tr);
            }
            $("#addBtnAchievementDiv").css("display","block");
            //获取当前月份
            var cycleMouth = Number(cycle.substring(5));
            //获取当前时间
            var time = new Date();
            var mouth = time.getMonth() + 1;
            var date = time.getDate();
            if (mouth == cycleMouth && (date <= 10 && date >= 1)){
                $('.readonly1').attr("readonly","readonly");//设为只读
            } else if(mouth == cycleMouth+1 && (date <= 10 && date >= 1)){
                $('.readonly1').removeAttr("readonly");//取消只读的设置
            } else{
                $('.readonly1').attr("readonly","readonly");//设为只读
            }
        }
    });
}
//删除工作业绩
function delectAchievement(id) {
    $.ajax({
        url: path + '/wa/achievements/deletePeAcc',//请求地址
        dataType: "json",//数据格式
        type: "post",//请求方式
        data: {"id": id},
        success: function (data) {
            if (data =="SUCCESS"){
                showAchievement();
            }
        }
    });
}
//修改工作业绩
function updAchievement(id) {
    var workTasks =$(".workTasks"+id).val();// 工作任务
    var access =$(".access"+id).val();//考核标准
    var detail =$(".detail"+id).val();//考核详情
    var score =$(".score"+id).val();//考核分
    var weights =$(".weights"+id).val();//权重
    $.ajax({
        url: path + '/wa/achievements/updatePeAcc',//请求地址
        dataType: "json",//数据格式
        type: "post",//请求方式
        data: {"id": id, "workTasks": workTasks, "access": access, "detail": detail, "score": score, "weights": weights},
        success: function (data) {
            if (data =="SUCCESS"){
                showAchievement();
                layer.alert("修改成功");
                layer.close(index1)
            }
        }
    });
}
//显示添加工作业绩
function showAddateAchievement() {
    layui.use('layer', function(){ //独立版的layer无需执行这一句
        var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
        index=layer.open({
            type: 1
            ,id: 'addateAchievement' //防止重复弹出
            ,content: $(".addateAchievement")
            ,btnAlign: 'c' //按钮居中
            ,shade: 0.5 //不显示遮罩
            ,area: ['100%', '100%']
            ,success: function () {
            }
            ,yes: function(){
            }
        });
    });
}
//添加工作业绩
function addAteAchievement() {
    jsonHtml();
    var performance = {};
    performance.workTasks =$("#addWorkTasks").val();// 工作任务
    performance.access =$("#addAccess").val();//考核标准
    performance.detail =$("#addDetail").val();//考核详情
    // performance.score =$("#addScore").val();//考核分
    performance.weights =$("#addWeights").val();//权重
    performance.employeeId = $("#employeeIdHidden1").val();// 员工id
    performance.cycle = $("#addCycleDataHidden").val();// 考核日期
    $.ajax({
        url: path + '/wa/achievements/insertPeAcc',//请求地址
        dataType: "json",//数据格式
        type: "post",//请求方式
        data: JSON.stringify(performance),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data == "SUCCESS") {
                layer.alert("添加成功");
                layer.close(index);
                showAchievement();
                clearVal();
            } else{
                layer.alert("添加失败")
            }
        }
    });
}
//清空值
function clearVal() {
    $("#addWorkTasks").val("");
    $("#addAccess").val("");
    $("#addDetail").val("");
    $("#addScore").val("");
    $("#addWeights").val("");
    $("#test5").val("");
}
/*******************************工作行为**********************************************/
//查询工作行为
function showBehavior() {
    jsonHtml();
    var id = $("#employeeIdHidden2").val();
    var cycle = $("#cycleDataHidden2").val();
    $.ajax({
        type:"post",
        url: path + '/wa/achievements/findBehavior',
        data:{'employeeId': id, "cycle": cycle},
        dataType:"json",
        success:function(data){
            if (data == "" || data == null){
                layer.alert("无数据");
                return;
            }
            $("#chidao").val("0");//迟到
            $("#chuchai").val("0");//出差
            $("#kuanggong").val("0");//矿工
            $("#lunxiu").val("0");//轮休
            $("#qingjia").val("0");//请假
            $("#tiaoxiu").val("0");//调休
            if (data[0].week1 == "") {
                data[0].week1 = 0;
            }
            if (data[0].week2 == "") {
                data[0].week2 = 0;
            }
            if (data[0].week3 == "") {
                data[0].week3 = 0;
            }
            if (data[0].week4 == "") {
                data[0].week4 = 0;
            }
            if (data[0].chidao == "") {
                data[0].chidao = 0;
            }
            if (data[0].chuchai == "") {
                data[0].chuchai = 0;
            }
            if (data[0].kuanggong == "") {
                data[0].kuanggong = 0;
            }
            if (data[0].lunxiu == "") {
                data[0].lunxiu = 0;
            }
            if (data[0].qingjia == "") {
                data[0].qingjia = 0;
            }
            if (data[0].tiaoxiu == "") {
                data[0].tiaoxiu = 0;
            }
            $("#BeId").val(data[0].id);
            $(".week1").val(data[0].week1);
            $(".week2").val(data[0].week2);
            $(".week3").val(data[0].week3);
            $(".week4").val(data[0].week4);
            $("#chidao").val(data[0].chidao);//迟到
            $("#chuchai").val(data[0].chuchai);//出差
            $("#kuanggong").val(data[0].kuanggong);//矿工
            $("#lunxiu").val(data[0].lunxiu);//轮休
            $("#qingjia").val(data[0].qingjia);//请假
            $("#tiaoxiu").val(data[0].tiaoxiu);//调休
            $("#sum").val(data[0].sum);//合计
            $("#jiaban").val(data[0].jiaban);//加班
            $("#kaoqin").val(data[0].kaoqin);//考勤
            $("#zhiban").val(data[0].zhiban);//值班
            $("#remark").val(data[0].remark);//备注
            $("#period").val(data[0].period);//课时
            //计算数值
            calculateAttendance();
            $.ajax({
                url:  path + "/wa/achievements/getAssessmentByEmployeeId",//请求地址
                datatype: "json",//数据格式
                data: {"cycle":cycle,employeeId:id},
                type: "post",//请求方式
                success: function (res) {
                    // 将字符串转换为json
                    function strToJson(str){
                        var json = eval('(' + str + ')');
                        res = json;
                    }
                    strToJson(res);
                    $('#netPerformance').val(res.netPerformance);
                    $('#comprehensivePerformance').val(res.comprehensivePerformance);
                }
            });
        }
    });
}
//增加
function addCount(id) {
    var td = $("#" + id).parents('td')[0];
    var span = td.lastElementChild;
    var day = $("#" + id).val();
    if (day == null) {
        day = 0.5;
    } else {
        day = day * 1 + 0.5;
    }
    span.style.display = "none";
    $("#" + id).val(day);
}
//减少
function reduceCount(id) {
    var td = $("#" + id).parents('td')[0];
    var span = td.lastElementChild;
    span.style.display = "none";
    var day = $("#" + id).val();
    if (day == null | day == 0) {
        day = 0;
        span.style.display = "inline-block";
    } else {
        day = day * 1 - 0.5;
    }
    $("#" + id).val(day);
}
//计算调休，请假，轮休，迟到，出差和出勤
function calculateAttendance() {
    //考试成绩
    var week1 = $('.week1').val();
    var week2 = $('.week2').val();
    var week3 = $('.week3').val();
    var week4 = $('.week4').val();
    var period = $('#period').val();
    //按钮
    var tiaoxiu = $('#tiaoxiu').val();
    var qingjia = $('#qingjia').val();
    var kuanggong = $('#kuanggong').val();
    var chidao = $('#chidao').val();
    var lunxiu = $('#lunxiu').val();
    var chuchai = $('#chuchai').val();
    //文本
    $('#ftiaoxiu').val((1.0) * tiaoxiu);
    $('#fqingjia').val((6.0) * qingjia);
    $('#fkuanggong').val((20.0) * kuanggong);
    $('#fchidao').val((1.0) * chidao);
    $('#flunxiu').val((1.0) * lunxiu);
    $('#fchuchai').val(2.0 * chuchai);
    //成绩
    var week = Number(week1) + Number(week2) + Number(week3) + Number(week4) + Number(period) ;
    //出勤文本
    var chuqing = 50.0 - Number($('#ftiaoxiu').val()) * 1 - Number($('#fqingjia').val()) * 1 - Number($('#fkuanggong').val()) * 1 - Number($('#fchidao').val()) * 1 - Number($('#flunxiu').val()) * 1;
    $("#fchuqing").val(chuqing);
    //合计
    var sum = Number(week) + Number(chuqing) + Number($('#fchuchai').val());
    $("#sum").val(sum);
}
//修改工作行为
function updBehavior() {
    jsonHtml();
    var employeeId = $("#employeeIdHidden2").val();
    var week1 = $('.week1').val();
    var week2 = $('.week2').val();
    var week3 = $('.week3').val();
    var week4 = $('.week4').val();
    var period = $('#period').val();
    var remark = $('#remark').val();
    var jiaban = $('#jiaban').val();
    var zhiban = $('#zhiban').val();
    var kaoqin = $('#kaoqin').val();

    var tiaoxiu = $('#tiaoxiu').val();
    var qingjia = $('#qingjia').val();
    var kuanggong = $('#kuanggong').val();
    var chidao = $('#chidao').val();
    var lunxiu = $('#lunxiu').val();
    var chuchai = $('#chuchai').val();
    var id=$("#BeId").val();
    var cycle = $("#cycleDataHidden2").val();
    var sum = $("#sum").val();
    calculateAttendance();

    var performance = {};
    performance.id = id;
    performance.week1 = week1;
    performance.week2 = week2;
    performance.week3 = week3;
    performance.week4 = week4;
    performance.period = period;
    performance.tiaoxiu = tiaoxiu;
    performance.qingjia = qingjia;
    performance.kuanggong = kuanggong;
    performance.chidao = chidao;
    performance.lunxiu = lunxiu;
    performance.chuchai = chuchai;
    performance.remark = remark;
    performance.cycle = cycle;
    performance.kaoqin = kaoqin;
    performance.zhiban = zhiban;
    performance.jiaban = jiaban;
    performance.employeeId = employeeId;
    performance.sum = sum;
    $.ajax({
        url: path + '/wa/achievements/updateBehavior',//请求地址
        dataType: "json",//数据格式
        type: "post",//请求方式
        data: JSON.stringify(performance),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data == "SUCCESS") {
                layer.alert("修改成功");
                showBehavior();
            }
        }
    })

}
//取消
function cancel() {
    layer.close(index);
    clearVal();
}