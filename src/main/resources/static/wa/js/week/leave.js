var index = 0;
var path = "";
$(function () {
    //查询所有数据
    showLeaveList(1,"","");
    //显示时间
    showDate();
    //显示请假配置名称
    showLeaveName();
    //显示人
    showEmployeeName();
});
//查询所有数据
function showLeaveList(pageCount,startTime,employeeId) {
    var top = $(".top").css("height");
    var win = $(window).height();
    var addBtn = $(".addBtn").css("height");
    var tp = top.indexOf("p");
    var ap = addBtn.indexOf("p");
    var topHeight = top.substring(0,tp);
    var addBtnHeight = addBtn.substring(0,ap);
    var height = win-topHeight-addBtnHeight-20;
    layui.use(['table',"form"], function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/leave/getLeaveDataList?startTime='+startTime+'&employeeId='+employeeId //数据接口
            , page: {
                curr: pageCount
            } //开启分页
            , limit: 10
            , limits: [10, 20, 30]
            , cols: [[ //表头
                {field: 'employeeName', title: '请假人', width: 150}
                , {field: 'leaveName', title: '请假类型',sort: true}
                , {field: 'startTime', title: '开始时间',sort: true}
                , {field: 'endTime', title: '结束时间',sort: true}
                , {field: 'bothTime', title: '间隔时间',sort: true}
                , {field: 'createdName', title: '创建人',sort: true}
                , {fixed: '', title: '审核状态',sort: true, width: 120, align: 'center', toolbar: '#barDemo2'}//审核状态 0待审核 1审核中 2审核结束
                , {field: 'reviewResult', title: '审核结果',sort: true, align: 'center', width: 120}
                , {fixed: '', title: '操作', toolbar: '#barDemo1', align: 'center', width: 100}
            ]]
            , done: function (res, curr, count) {
                for(var i =0;i<res.data.length;i++){
                    console.log(res.data[i].excess)
                    if(res.data[i].excess == 1){
                        $(".layui-table tbody tr").eq(i).css("background-color","red")
                    }
                }
            }
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            $("#updRemark").val(data.remark);
            $("#updEmployeeId").val(data.id);
            $("#examineIdHidden").val(data.id);
            layui.use('form', function(){
                var form = layui.form;
                $("#updEmployeeName").val(data.employeeId);
                $("#updLeaveName").val(data.leaveId);
                $("#updLeaveNameHidden").val(data.leaveId);
                $("#test4").val(data.startTime);
                $("#test5").val(data.endTime);
                form.render('select');
                form.render(); //更新全部
            });
            //审核
            $(".exmEmployeeName").text(data.employeeName);
            $(".exmLeaveName").text(data.leaveName);
            $(".exmStartTime").text(data.startTime);
            $(".exmEndTime").text(data.endTime);
            $(".exmCreatedName").text(data.createdName);
            $(".exmStatusName").text(data.statusName);
            $(".exmRemark").text(data.remark);
            if (obj.event == 'updLeave') {// 修改
                index = layer.open({
                    type: 1 //此处以iframe举例
                    ,area: ['100%', '100%']
                    ,shade: 0.5
                    ,id: 'updLeave1' //防止重复弹出
                    ,content: $(".updLeave1")
                    ,yes: function(){
                    }
                });
            }else if (obj.event == 'exmLeave') {// 审核
                var employeeId = data.employeeId;
                $(".examineLeavePeople").css("display", "block");
                $(".showExamineLeavePeople").css("display","none");
                //获取当前时间
                var date = new Date();
                var year = date.getFullYear();
                var mouth = date.getMonth()+1;
                var day = date.getDate();
                var hour = date.getHours();
                var minutes = date.getMinutes();
                var time = year+"-"+mouth+"-"+day+" "+hour+":"+minutes;
                $("#exmTime").text(time);
                $.ajax({
                    url: path + "/wa/employee/getEmployeeInf",
                    dataType: "json",//数据格式
                    type: "post",//请求方式
                    success: function (data) {
                        if (employeeId == data.id){
                            //配置一个透明的询问框
                            layer.msg('无权限', {
                                time: 2000, //2s后自动关闭
                                btn: ['明白了']
                            });
                            return;
                        }
                        else{
                            layer.open({
                                type: 1 //此处以iframe举例
                                ,area: ['100%', '100%']
                                ,shade: 0.5
                                ,id: 'examineLeaveInfo' //防止重复弹出
                                ,content: $(".examineLeaveInfo")
                                ,yes: function(){
                                }
                            });
                        }
                        $("#exmName").text(data.name);
                    }
                });
                $(".examineBtn").css("display","block");
                $("#examineShowOk").css("display","none");
                $("#examineShowNo").css("display","none");
            } else if (obj.event == 'showLeave'){// 查看
                $(".examineLeavePeople").css("display", "none");
                $(".showExamineLeavePeople").css("display","block");
                //审核时间
                var timeArr = (data.reviewTime).split(',');
                var time = [];
                for (var i = 0; i < timeArr.length-1; i ++){
                    time.push(timeArr[i].slice(-16,timeArr[i].length));
                }
                //审核意见
                var reviewRemarkArr = (data.reviewRemark).split(',');
                var reviewRemark = [];
                for (var i = 0; i < reviewRemarkArr.length-1; i ++){
                    reviewRemark.push(reviewRemarkArr[i].slice(4,reviewRemarkArr[i].length));
                }
                //审核人
                var reviewArr = (data.review).split(',');
                var review = [];
                for (var i = 0; i < reviewArr.length-1; i ++){
                    var dh = reviewArr[i].indexOf('=');
                    review.push(reviewArr[i].substring(0,dh));
                }
                var showExamineLeavePeople = $(".showExamineLeavePeople");
                var table = "";
                //布局;
                showExamineLeavePeople.html("");
                for (var i = 0; i <reviewRemarkArr.length-1; i ++ ){
                    table += "<table cellspacing='0'><tr><td>审核时间</td><td><span clasee='showExmTime'>"+time[i]+"</span></td><td>审核人</td><td><span clasee='showExmName'>"+review[i]+"</span></td></tr><tr><td>审核意见</td><td colspan='3'><span clasee='showExmReviewRemark'>"+reviewRemark[i]+"</span></td></tr><tr><td>审核结果</td><td colspan='3'><span clasee='showExmResult'>"+data.reviewResult+"</span></td></tr></table>"
                }
                showExamineLeavePeople.append(table);
                layer.open({
                    type: 1
                    ,area: ['100%', '100%']
                    ,shade: 0.5
                    ,id: 'examineLeaveInfo' //防止重复弹出
                    ,content: $(".examineLeaveInfo")
                    ,yes: function(){
                    }
                });
            }
        });
    });
}
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
        laydate.render({
            elem: '#test2'
            ,type: 'date'
            , done: function (value) {
                $("#addLeaveTimeHidden").val(value);
                $("#addStartTimeSpan").css("display","none");
            }
        });
        laydate.render({
            elem: '#test3'
            ,type: 'time'
            ,range: true
            ,min: '08:30:00'
            ,max: '17:00:00'
            , done: function (value) {
                $("#addLeaveStartTimeHidden").val(value.substring(0,8));
                $("#addLeaveEndTimeHidden").val(value.substring(11));
                $("#addStartTimeHidden").val($("#addLeaveTimeHidden").val()+" "+$("#addLeaveStartTimeHidden").val());
                $("#addEndTimeHidden").val($("#addLeaveTimeHidden").val()+" "+$("#addLeaveEndTimeHidden").val());
                $("#addEndTimeSpan").css("display","none");
            }
        });
        laydate.render({
            elem: '#test4'
            , format: 'yyyy-MM-dd HH:mm'
            ,type: 'datetime'
            , done: function (value) {
                $("#updStartTimeHidden").val(value);
            }
        });
        laydate.render({
            elem: '#test5'
            , format: 'yyyy-MM-dd HH:mm'
            ,type: 'datetime'
            , done: function (value) {
                $("#updEndTimeHidden").val(value);
            }
        });
    })
}
