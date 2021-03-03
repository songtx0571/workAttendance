var path = "";
var peopleName = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth() + 1;
$(function(){
    //当前登陆人
    getLoginUserName();
    //显示考核日期
    showCycleData();
    //显示部门
    showDepart();
    showDepartID();
    // 查询员工绩效信息
    if (month == 12) {
        month = 1;
        year = year + 1;
    }
    if (month == 0) {
        month = 12;
        year = year - 1;
    }
    if (month < 10) {
        month = "0"+month;
    }
    showTable(year+"-"+month, "");
    $("#test3").val(year+"-"+month);
});
//获取当前登陆人
function getLoginUserName() {
    $.ajax({
        type: "GET",
        url: path + "/wa/reimbursement/getLoginUserName",
        dataType: "json",
        success: function (data) {
            peopleName = data.userName;
        }
    });
}
//金额两位小数
function twoDecimal(id,value) {
    var reg1=/^\d+(\.\d{0,2})?$/;
    var reg=/^\d+$|^\d*\.\d+$/g;
    value = value.trim();
    if (!reg1.test(value)){
        if (reg.test(value)){
            var td = value.indexOf(".");
            var tdNumFront = value.substring(0,td);
            var tdNumAfter = value.substr(td,3);
            value = tdNumFront+tdNumAfter;
            $("#"+id).val(value);
        } else {
            layer.alert("只能输入两位小数！");
        }
    }
}
/*显示考核日期*/
function showCycleData() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询所有数据日期
        laydate.render({
            elem: '#test3'
            , type: 'month'
            , trigger: 'click'//呼出事件改成click
            , done: function (value) {
                showTable(value);
            }
        });
        laydate.render({
            elem: '#test4'
            , type: 'date'
            , trigger: 'click'//呼出事件改成click
            , done: function (value) {
                $("#addTest4Hidden").val(value)
            }
        });
    });
}
//显示添加修改部门
function showDepartID() {
    layui.use(['form'], function () {
        var form = layui.form;
        $.ajax({
            type: "GET",
            url: path + "/wa/reimbursement/getDepartmentMap?companyId=1",
            dataType: "json",
            success: function (data) {
                $("#addDepartName").empty();
                $("#updDepartName").empty();
                var option = "<option value='0' >请选择部门</option>";
                for (var i = 0; i < data.length; i++) {
                    option += "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
                }
                $('#addDepartName').html(option);
                $('#updDepartName').html(option);
                form.render();//菜单渲染 把内容加载进去
            }
        });
        form.on('select(addDepartName)', function (data) {
            $("#addDepartNameHidden").val(data.value);
        });
        form.on('select(updDepartName)', function (data) {
            $("#updDepartNameHidden").val(data.value);
        });
        form.on('select(addSubjectName)', function (data) {
            $("#addSubjectNameHidden").val(data.value);
        });
        form.on('select(updSubjectName)', function (data) {
            $("#updSubjectNameHidden").val(data.value);
        });
    });
}
//显示部门
function showDepart() {
    layui.use(['form'], function () {
        var form = layui.form;
        $.ajax({
            type: "GET",
            url: path + "/wa/reimbursement/getDepartmentMap",
            dataType: "json",
            success: function (data) {
                $("#selDepartName").empty();
                var option = "<option value='0' >请选择部门</option>";
                for (var i = 0; i < data.length; i++) {
                    option += "<option value='" + data[i].id + "'>" + data[i].name + "</option>"
                }
                $('#selDepartName').html(option);
                form.render();//菜单渲染 把内容加载进去
            }
        });
        form.on('select(selDepartName)', function (data) {
            $("#selDepartNameHidden").val(data.value);
            showTable($("#test3").val(), $("#selDepartNameHidden").val());
        });
    })
}
//显示数据
function showTable(cycle,depart){
    var win = $(window).height();
    var height = win - 100;
    if (depart == '0') {
        depart = "";
    }
    layui.use('table', function() {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , url: path + '/wa/reimbursement/getReimbursementList?month='+cycle+'&departmentId='+depart //数据接口
            ,toolbar: true
            , page: {
                curr: 1
            } //开启分页
            , limit: 50
            , limits: [50, 100, 150]
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide: true}
                ,{field: 'reimbursementDate', title: '报销日期', align: 'center', sort: true}
                ,{field: 'reimbursorName', title: '报销人', align: 'center'}
                ,{field: 'departemntName', title: '部门', align: 'center', sort: true}
                ,{field: 'reimbursementContent', title: '报销内容', align: 'center'}
                ,{field: 'reimbursementAmount', title: '报销金额', align: 'center'}
                ,{fixed: '', title: '科目', toolbar: '#barDemoDepartSubject', align: 'center'}
                ,{field: 'remark', title: '备注', align: 'center'}
                ,{fixed: '', title: '确认情况', toolbar: '#barDemoDepartOk', align: 'center',width: 170}
                ,{field: 'financeRemark', title: '财务审核意见', align: 'center'}
                ,{fixed: '', title: '财务审核结果', toolbar: '#barDemoDepartExamine', align: 'center'}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            if (obj.event == 'departOk') {// 确认
                $.ajax({
                    url: path + "/wa/reimbursement/examine",
                    dataType: "json",//数据格式
                    type: "post",//请求方式
                    data: {id: data.id, financeRemark: "",financeResult: '1',status: data.status},
                    success: function (data) {
                        showTable($("#test3").val(),$("#selDepartNameHidden").val());
                    }
                });
            } else if (obj.event == 'departNo') {// 取消
                $.ajax({
                    url: path + "/wa/reimbursement/examine",
                    dataType: "json",//数据格式
                    type: "post",//请求方式
                    data: {id: data.id, financeRemark: "",financeResult: '2',status: data.status},
                    success: function (data) {
                        showTable($("#test3").val(),$("#selDepartNameHidden").val());
                    }
                });
            } else if (obj.event == 'examine') {// 财务审核
                $("#examineId").val(data.id);
                layui.use('layer', function () {
                    var layer = layui.layer;
                    layer.open({
                        type: 1
                        , id: 'examineDiv' //防止重复弹出
                        , content: $(".examineDiv")
                        , btnAlign: 'c' //按钮居中
                        , shade: 0.5 //不显示遮罩
                        , area: ['100%', '100%']
                        , success: function () {
                        }
                        , yes: function () {
                        }
                    });
                });
            } else if (obj.event == 'departEdit') { // 修改
                updateDepart(JSON.stringify(data));
            }
        });
    });
}
//显示添加的div
function showAddDiv() {
    layui.use('form', function(){
        var form = layui.form;
        $("#test4").val("");
        $("#addTest4Hidden").val("");
        $("#addDepartName").val("-1");
        $("#addDepartNameHidden").val("");
        $("#addPeopleName").val(peopleName);
        $("#addContent").val("");
        $("#addMoney").val("");
        $("#addSubjectName").val("0");
        $("#addSubjectNameHidden").val("0");
        $("#addRemark").val("");
        form.render('select');
        form.render(); //更新全部
    });
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            type: 1
            , id: 'insertReimbursement1' //防止重复弹出
            , content: $(".insertReimbursement1")
            , btnAlign: 'c' //按钮居中
            , shade: 0.5 //不显示遮罩
            , area: ['100%', '100%']
            , success: function () {
            }
            , yes: function () {
            }
        });
    });
}
//添加
function insertOk() {
    if ($("#addTest4Hidden").val() == "" || $("#addDepartNameHidden").val() == "" || $("#addContent").val() == "" || $("#addMoney").val() == ""|| $("#addSubjectNameHidden").val() == "0") {
        layer.alert("请将信息填写完整！");
        return;
    }
    var reimbursement = {};
    reimbursement.reimbursementDate = $("#addTest4Hidden").val();
    reimbursement.departmentId = $("#addDepartNameHidden").val();
    reimbursement.reimbursementContent = $("#addContent").val();
    reimbursement.reimbursementAmount = $("#addMoney").val();
    reimbursement.subject = $("#addSubjectNameHidden").val();
    reimbursement.remark = $("#addRemark").val();
    $.ajax({
        url: path + "/wa/reimbursement/addReimbursement",
        dataType: "json",//数据格式
        type: "post",//请求方式
        async: false,
        data: JSON.stringify(reimbursement),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (data == "RESIGNIN"){
                layer.alert("身份验证过期，请重新登陆！");
            } else if (data == "SUCCESS"){
                layer.closeAll();
                showTable($("#test3").val(),"");
            } else {
                layer.alert("添加失败！")
            }
        }
    });
}
//显示修改的值
function updateDepart(data) {
    data =  eval('(' + data + ')');
    layui.use(['form','layer'], function () {
        var layer = layui.layer;
        var form = layui.form;
        $("#updReimbursementId").val(data.id);
        $("#updTest5Hidden").val(data.reimbursementDate);
        $("#updDepartName").val(data.departmentId);
        $("#updDepartNameHidden").val(data.departmentId);
        $("#updPeopleName").val(data.reimbursorName);
        $("#updContent").val(data.reimbursementContent);
        $("#updMoney").val(data.reimbursementAmount);
        $("#updSubjectName").val(data.subject);
        $("#updSubjectNameHidden").val(data.subject);
        $("#updRemark").val(data.remark);
        form.render();
        layer.open({
            type: 1
            , id: 'updateReimbursement' //防止重复弹出
            , content: $(".updateReimbursement")
            , btnAlign: 'c' //按钮居中
            , shade: 0.5 //不显示遮罩
            , area: ['100%', '100%']
            , success: function () {
            }
            , yes: function () {
            }
        });
    });
}
//修改
function updateOk() {
    var reimbursement = {};
    reimbursement.id = $("#updReimbursementId").val();
    reimbursement.departmentId = $("#updDepartNameHidden").val();
    reimbursement.reimbursementContent = $("#updContent").val();
    reimbursement.reimbursementAmount = $("#updMoney").val();
    reimbursement.subject = $("#updSubjectNameHidden").val();
    reimbursement.remark = $("#updRemark").val();
    reimbursement.financeResult = '0';
    $.ajax({
        url: path + "/wa/reimbursement/updReimbursement",
        dataType: "json",//数据格式
        type: "post",//请求方式
        async: false,
        data: JSON.stringify(reimbursement),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            layer.closeAll();
            showTable($("#test3").val(),$("#selDepartNameHidden").val());
        }
    });
}
//审核通过
function examineOK() {
    var examineOpinion = $("#examineOpinion").val();
    $.ajax({
        url: path + "/wa/reimbursement/examine",
        dataType: "json",//数据格式
        type: "post",//请求方式
        data: {id: $("#examineId").val(),financeRemark: examineOpinion, financeResult: '3',status:'1'},
        success: function (data) {
            layer.closeAll();
            showTable($("#test3").val(),$("#selDepartNameHidden").val());
        }
    });
}
//审核驳回
function examineNO() {
    var examineOpinion = $("#examineOpinion").val();
    $.ajax({
        url: path + "/wa/reimbursement/examine",
        dataType: "json",//数据格式
        type: "post",//请求方式
        data: {id: $("#examineId").val(),financeRemark: examineOpinion, financeResult: '4',status:'1'},
        success: function (data) {
            layer.closeAll();
            showTable($("#test3").val(),$("#selDepartNameHidden").val());
        }
    });
}
//上个月
function monthUpBtn() {
    var time = $("#test3").val();
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
    $("#test3").val(y+"-"+m);
    var a1 = y+"-"+m;
    showTable(a1,$("#selDepartNameHidden").val())
}
//下个月
function monthDownBtn() {
    var time = $("#test3").val();
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
    $("#test3").val(y+"-"+m);
    var a2 = y+"-"+m;
    showTable(a2,$("#selDepartNameHidden").val())
}
//取消
function cancel() {
    layer.closeAll();
}