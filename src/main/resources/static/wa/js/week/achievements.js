var path = "";
var userNumber = "";
var classUserNumber = "";
$(function(){
    //显示考核日期
    showCycleData();
    // 查询员工绩效信息
    showAchievementsList("");
    getUser();
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
                showAchievement(value);
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
                showBehavior(value);
            }
        });
        //复制标准月份
        laydate.render({
            elem: '#test7'
            , type: 'month'
            ,trigger: 'click'//呼出事件改成click
            ,done: function(value){
                $("#copyTimeHidden").val(value);
                $("#copyTimeBtn").css("display","block");
            }
        });
    });
}
/*查看当前登陆人*/
function getUser() {
    $.ajax({
        type: "GET",
        url: path + '/wa/achievements/getUserInform',
        dataType: "json",
        success: function(data){
            classUserNumber = data.userNumber;
        }
    })
}
/*******************************工作业绩**********************************************/
/*查询员工绩效信息*/
function showAchievementsList(cycle){
    var win = $(window).height();
    var height = win - 100;
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    if (cycle == "") {
        if (month == 1){
            month = 12;
            year = year - 1;
        }
        if (month < 10 && month >= 1) {
            month = "0"+month;
        }
        $("#test3").val(year+"-"+month);
    }
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
                ,{field: 'zhiban', title: '值班天数', sort: true,hide:true}
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
            userNumber = data.userNumber;
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
                $("#test4").val($("#test3").val());
                showAchievement($("#test3").val());
                layer.open({
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
                $("#test6").val($("#test3").val());
                showBehavior($("#test3").val());
                layer.open({
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
                layer.open({
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
function showAchievement(cycle) {
    jsonHtml();
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    if(cycle == "" || cycle.length <= 0){
        if (month == 1){
            month = 12;
            year = year - 1;
        }
        if (month < 10 && month >= 1) {
            month = "0"+month;
        }
        cycle = year+"-"+month;
        $("#test4").val(cycle)
    }
    var id = $("#employeeIdHidden").val();
    $.ajax({
        type: "GET",
        url: path + '/wa/achievements/findPeAcc?cycle=' + cycle + '&employeeId=' + id,
        dataType: "json",
        success: function(data){
            if (data == "" || data == null) {
                $("#achievementTbody").css("display","none");
                return;
            }
            $("#achievementTbody").css("display","contents");
            var tbody = document.getElementById("achievementTbody");
            tbody.innerHTML = "";
            for(var i=0;i<data.length;i++){
                $("#achievementIdHidden").val(data[i].id);
                var tr = document.createElement("tr");
                tr.setAttribute("class","achievementTr");
                var td = "<td><textarea rows='2' class='readonly1 workTasks"+data[i].id+"'>"+data[i].workTasks+"</textarea></td>" +
                    "<td><textarea rows='2' class='readonly1 access"+data[i].id+"'>"+data[i].access+"</textarea></td>" +
                    "<td><textarea rows='2' class='readonly1 detail"+data[i].id+"'>"+data[i].detail+"</textarea></td>" +
                    // "<td><input type='text'  class='readonly1 detail"+data[i].id+"' value='"+data[i].detail+"' /></td>" +
                    "<td><input type='text'  class='readonly1 score"+data[i].id+"' value='"+data[i].score+"' /></td>" +
                    "<td><input type='text'  class='readonly1 weights"+data[i].id+"' value='"+data[i].weights+"'/></td>" +
                    "<td class='hideTd"+userNumber+"'><input type='button' value='修改' class='layui-btn' onclick='updAchievement("+data[i].id+")' />" +
                    "<input type='button' value='删除' class='layui-btn' onclick='delectAchievement("+data[i].id+")' /></td>";
                tr.innerHTML = td;
                tbody.appendChild(tr);
            }
            if (userNumber == classUserNumber) {
                $(".hideTd"+userNumber).html("无操作！");
            }
            /*//获取当前月份
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
            }*/
        }
    });
}
//打开复制考核
function showCopyTime() {
    $("#hideTimeDiv").css("display","block")
}
//确定复制
function copyTimeOk() {
    var startTime = $("#test4").val();
    var endTime = $("#test7").val();
    var employeeId = $("#employeeIdHidden").val();
    $.ajax({
         url: path+"/wa/achievements/copyPeAcc",//请求地址
        //url: "http://192.168.1.89:8081/wa/achievements/copyPeAcc",//请求地址
        dataType: "json",//数据格式
        data: {"cycle": startTime,"lastcycle": endTime,"employeeId":employeeId},
        type: "get",//请求方式
        success: function (data) {
            if (data == "success") {
                layer.alert("周期已复制");
            } else if(data=='fail'){
                layer.alert("周期已存在");
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
                showAchievement("");
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
    if (Number(score) > Number(weights)) {
        layer.alert("考核分不能大于权重！");
        return;
    }
    $.ajax({
        url: path + '/wa/achievements/updatePeAcc',//请求地址
        dataType: "json",//数据格式
        type: "post",//请求方式
        data: {"id": id, "workTasks": workTasks, "access": access, "detail": detail, "score": score, "weights": weights},
        success: function (data) {
            if (data =="SUCCESS"){
                // showAchievement("");
                layer.alert("修改成功");
                // layer.closeAll()
            }
        }
    });
}
//显示添加工作业绩
function showAddateAchievement() {
    layui.use('layer', function(){ //独立版的layer无需执行这一句
        var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
        layer.open({
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
                layer.closeAll();
                showAchievement("");
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
function showBehavior(cycle) {
    jsonHtml();
    var id = $("#employeeIdHidden2").val();
    if (userNumber == classUserNumber) {
        $(".hideBtn").css("display","none");
    } else {
        $(".hideBtn").css("display","inline");
    }
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    if(cycle == "" || cycle.length <= 0){
        if (month == 1){
            month = 12;
            year = year - 1;
        }
        if (month < 10 && month >= 1) {
            month = "0"+month;
        }
        cycle = year+"-"+month;
        $("#test6").val(cycle)
    }
    var cycleM = cycle.substring(5,cycle.length);
    var cycleY = cycle.substring(0,4);
    if (cycleM > month && year == cycleY){
        $(".hideBtn").css("display", "none");
        $('.week1').val("");
        $('.week2').val("");
        $('.week3').val("");
        $('.week4').val("");
        $('#sum').val("");
        $('#remark').val("");
        $('#jiaban').val('0');
        $('#kaoqin').val('0');
        $('#netPerformance').val('0');
        $('#comprehensivePerformance').val('0');
        return;
    }
    $.ajax({
        type:"post",
        url: path + '/wa/achievements/findBehavior',
        data:{'employeeId': id, "cycle": cycle},
        dataType:"json",
        success:function(data){
            $("#leaveConfig").html("");
            if (data == "" || data == null){
                return;
            }
            var leaveData = data[0].leaveData;
            if (leaveData != "" || leaveData != null){
                var leaveConfig = $("#leaveConfig");
                var tr = '<tr><td rowspan="'+(leaveData.length+1)+'" style="vertical-align:middle">考勤情况（50分）</td><td colspan="2">满勤50分</td><td colspan="7">满勤。每半天1分，每天2分</td><td><input id="fchuqing" name="fchuqing" readonly value="50"></td></tr>';
                for (var i = 0; i < leaveData.length; i ++) {
                    tr += '<tr><td ><input value="'+leaveData[i].leaveCount+'" readonly name="tiaoxiu" class="inputCount tiaoxiu'+leaveData[i].employeeId+'"></td><td>'+leaveData[i].leaveUnitName+'</td><td colspan="7">'+leaveData[i].leaveName+'</td><td><input class="ftiaoxiu'+leaveData[i].employeeId+'" name="fResult" readonly value="'+leaveData[i].leaveResult+'"></tr>';
                }
                leaveConfig.html(tr);
            }
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
            $("#BeId").val(data[0].id);
            $(".week1").val(data[0].week1);
            $(".week2").val(data[0].week2);
            $(".week3").val(data[0].week3);
            $(".week4").val(data[0].week4);
            $(".period").val('10');
            $("#jiaban").val(data[0].jiaban);//加班
            $("#kaoqin").val(data[0].kaoqin);//考勤
            $("#remark").val(data[0].remark);//备注

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
    if ((cycleM == 12 && month == 1 && cycleY == year - 1) || (cycleM == month - 1 && cycleY == year) || (cycleM == month && cycleY == year)) {
        $(".hideBtn").css("display", "revert");
    } else  {
        $(".hideBtn").css("display", "none");
    }
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
    addReducePerformance(id,day);
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
    addReducePerformance(id,day);
}
//增加减少改变绩效
function addReducePerformance(id, day) {
    var employeeId = $("#employeeIdHidden2").val();
    var cycle = $("#test6").val();
    if (id == "jiaban"){
        $.ajax({
            url: path + '/wa/achievements/getAssessmentByJiaban',//请求地址
            dataType: "json",//数据格式
            type: "post",//请求方式
            data: {cycle: cycle,employeeId:employeeId,jiaban:day},
            success: function (data) {
                $("#comprehensivePerformance").val(data.comprehensivePerformance);//综合绩效
            }
        })
    }
}
//计算考勤、合计
function calculateAttendance() {
    //考试成绩
    var week1 = $('.week1').val();
    var week2 = $('.week2').val();
    var week3 = $('.week3').val();
    var week4 = $('.week4').val();
    var period = 10;
    //考勤
    var fResult = $("input[name='fResult']");
    var fResultArr = [];
    for (var i = 0; i < fResult.length; i ++) {
        fResultArr.push(Number(fResult[i].value));
    }
    var fResultSum = 0;
    for (var j = 0 ; j < fResultArr.length; j ++) {
        fResultSum += fResultArr[j];
    }
    $("#fchuqing").val(50 - fResultSum);
    //成绩
    var week = Number(week1) + Number(week2) + Number(week3) + Number(week4) + Number(period) ;
    //合计
    var sum = Number(week) + Number($("#fchuqing").val());
    $("#sum").val(sum);
}
//修改工作行为
function updBehavior() {
    jsonHtml();
    var id=$("#BeId").val();
    var employeeId = $("#employeeIdHidden2").val();
    var remark = $('#remark').val();
    var jiaban = $('#jiaban').val();
    var kaoqin = $('#kaoqin').val();
    var cycle = $("#test6").val();
    var sum = $("#sum").val();
    var netPerformance = $("#netPerformance").val();//净绩效
    var comprehensivePerformance = $("#comprehensivePerformance").val();//综合绩效

    var performance = {};
    performance.id = id;
    performance.remark = remark;
    performance.cycle = cycle;
    performance.kaoqin = kaoqin;
    performance.jiaban = jiaban;
    performance.employeeId = employeeId;
    performance.sum = sum;
    performance.netPerformance = netPerformance;
    performance.comprehensivePerformance = comprehensivePerformance;
    $.ajax({
        url: path + '/wa/achievements/updateBehavior',//请求地址
        dataType: "json",//数据格式
        type: "post",//请求方式
        data: JSON.stringify(performance),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data == "SUCCESS") {
                layer.alert("修改成功");
                showBehavior("");
                // layer.closeAll();
            }
        }
    })

}
//上个月
function monthUpBtn(type) {
    var time = $("#test4").val();
    var time1 = $("#test6").val();
    var time2 = $("#test3").val();
    if (type == "yeji"){
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
        $("#test4").val(y+"-"+m);
        var a1 = y+"-"+m;
        showAchievement(a1)
    } else if (type == "xingwei") {
        var y = time1.substring(0,4);
        var m = time1.substring(5,7);
        m --;
        if (m == 0) {
            m = 12;
            y = y - 1;
        }
        if (m > 0 && m < 10){
            m = "0" + m;
        }
        $("#test6").val(y+"-"+m);
        var b1 = y+"-"+m;
        showBehavior(b1);
    } else if (type == "zong") {
        var y = time2.substring(0,4);
        var m = time2.substring(5,7);
        m --;
        if (m == 0) {
            m = 12;
            y = y - 1;
        }
        if (m > 0 && m < 10){
            m = "0" + m;
        }
        $("#test3").val(y+"-"+m);
        var c1 = y+"-"+m;
        showAchievementsList(c1);
    }
}
//下个月
function monthDownBtn(type) {
    var time = $("#test4").val();
    var time1 = $("#test6").val();
    var time2 = $("#test3").val();
    if (type == "yeji"){
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
        $("#test4").val(y+"-"+m);
        var a2 = y+"-"+m;
        showAchievement(a2)
    } else if (type == "xingwei") {
        var y = time1.substring(0,4);
        var m = time1.substring(5,7);
        m ++;
        if (m == 13){
            m = 1;
            y = Number(y) + 1;
        }
        if (m > 0 && m < 10){
            m = "0" + m;
        }
        $("#test6").val(y+"-"+m);
        var b2 = y+"-"+m;
        showBehavior(b2);
    } else if (type == "zong") {
        var y = time2.substring(0,4);
        var m = time2.substring(5,7);
        m ++;
        if (m == 13){
            m = 1;
            y = Number(y) + 1;
        }
        if (m > 0 && m < 10){
            m = "0" + m;
        }
        $("#test3").val(y+"-"+m);
        var c2 = y+"-"+m;
        showAchievementsList(c2);
    }
}
//取消
function cancel() {
    layer.closeAll();
    clearVal();
}