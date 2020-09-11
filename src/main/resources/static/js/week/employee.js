var index = 0;
// var path = "http://192.168.1.26:8081/wa";
var path = "";
$(function(){
    // showCompany();
    showEmployeeList();
});
/*查询员工信息*/
function showEmployeeList(){
    layui.use('table', function(){
        var table = layui.table;
        table.render({
            elem: '#demo'
            ,height: 500
            ,url: path + '/employee/getEmployeeList'  //数据接口
            ,page: true //开启分页
            ,limit: 10
            ,limits: [10, 20, 30]
            ,cols: [[ //表头
                {field: 'userNumber', title: '编号', width:80, sort: true}
                ,{field: 'name', title: '姓名', width:100}
                ,{field: 'sexName', title: '性别', width:80}// 1男
                ,{field: 'companyName', title: '公司'}
                ,{field: 'departmentName', title: '部门'}
                ,{field: 'postName', title: '岗位'}
                ,{field: 'managerName', title: '绩效管理人'}
                ,{field: 'stateName', title: '在职状态', width: 110, sort: true}// 1 在职
                ,{fixed: '', title:'操作', toolbar: '#barDemo11', width:70,align:'center '}
            ]]
            ,done: function(res, curr, count){}
        });
        //监听行工具事件
        table.on('tool(test)', function(obj) {
            var data = obj.data;
            $("#userIdHidden").val(data.id)
            shouInfo();
            if (obj.event === 'edit') {
                index=layer.open({
                    type: 1
                    ,id: 'updateUser' //防止重复弹出
                    ,content: $(".updateUser")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['700px', '500px']
                    ,yes: function(){}
                });
            }
        });
    });
}
//修改数据的显示
function shouInfo() {
    $.ajax({
        type: "GET",
        url: path + "/employee/getEmployee",
        data: {"id": $("#userIdHidden").val()},
        dataType: "json",
        success: function(data){
            $("#name").val(data.name);//姓名
            $("#sex").val(data.sexName);//性别
            $("#phone").val(data.phone);//电话
            $("#idNumber").val(data.idNumber); //身份证
            $("#address").val(data.address);//住址
            $("#remark").val(data.remark);//备注
            $("#emergency").val(data.emergency);//应急联系人
            $("#emergencyTel").val(data.emergencyTel); //应急电话
            $("#closhe").val(data.closhe);//衣服尺寸
            $("#hat").val(data.hat);//安全帽编号
            $("#laowupaiqian").val(data.laowupaiqian);//劳务派遣
            $("#education").val(data.education);//学历
            $("#entryDate").val(data.entryDate);//入职日期
            $("#departmentName").val(data.departmentName); //部门
            $("#postName").val(data.postName);//岗位
            $("#userNumber").val(data.userNumber);//编号
            $("#credentials1").val(data.credentials1);//证书1
            $("#credentials2").val(data.credentials2);//证书2
            $("#credentials3").val(data.credentials3);//证书3

            $("#bank").val(data.bank); //开户行
            $("#card").val(data.card);//银行卡号
            $("#wages").val(data.wages);//待遇标准
            $("#basicwages").val(data.basicwages);//基本工资
            $("#meritpay").val(data.meritpay);//绩效工资
            $("#managerName").val(data.managerName);//入职日期
        }
    });
}
/*根据姓名搜索员工信息*/
function searchBtn() {
    var searchName = $("#searchName").val();
    layui.use('table', function(){
        var table = layui.table;
        table.render({
            elem: '#demo'
            ,height: 500
            ,url: path + "/employee/searchEmployee?search="+ searchName //数据接口
            ,page: true //开启分页
            ,limit: 10
            ,limits: [10, 20, 30]
            ,cols: [[ //表头
                {field: 'userNumber', title: '编号', width:80, sort: true}
                ,{field: 'name', title: '姓名', width:100}
                ,{field: 'sexName', title: '性别', width:80}// 1男
                ,{field: 'companyName', title: '公司'}
                ,{field: 'departmentName', title: '部门'}
                ,{field: 'postName', title: '岗位'}
                ,{field: 'managerName', title: '绩效管理人'}
                ,{field: 'stateName', title: '在职状态', width: 110, sort: true}// 1 在职
                ,{fixed: '', title:'操作', toolbar: '#barDemo11', width:70,align:'center '}
            ]]
            ,done: function(res, curr, count){}
        });
        //监听行工具事件
        table.on('tool(test)', function(obj) {
            var data = obj.data;
            $("#userIdHidden").val(data.id)
            shouInfo();
            if (obj.event === 'edit') {
                index=layer.open({
                    type: 1
                    ,id: 'updateUser' //防止重复弹出
                    ,content: $(".updateUser")
                    ,btnAlign: 'c' //按钮居中
                    ,shade: 0.5 //不显示遮罩
                    ,area: ['700px', '500px']
                    ,yes: function(){}
                });
            }
        });
    });
}
//修改员工信息
function updUserInfo() {
    var employee = {};
    employee.id = $("#userIdHidden").val();
    employee.name = $("#name").val();
    employee.sex = $("#sex").val();
    employee.phone = $("#phone").val();
    employee.idnumber = $("#idNumber").val();
    employee.address = $("#address").val();
    employee.remark = $("#remark").val();
    employee.emergency = $("#emergency").val();
    employee.emergencyTel = $("#emergencyTel").val();
    employee.closhe = $("#closhe").val();
    employee.hat = $("#hat").val();
    employee.laowupaiqian = $("#laowupaiqian").val();
    employee.education = $("#education").val();
    employee.entryDate = $("#entryDate").val();
    employee.departmentName = $("#departmentName").val();
    employee.postName = $("#postName").val();
    employee.userNumber = $("#userNumber").val();
    employee.credentials1 = $("#credentials1").val();
    employee.credentials2 = $("#credentials2").val();
    employee.credentials3 = $("#credentials3").val();

    employee.bank = $("#bank").val();
    employee.card = $("#card").val();
    employee.wages = $("#wages").val();
    employee.basicwages = $("#basicwages").val();
    employee.meritpay = $("#meritpay").val();
    employee.managerName = $("#managerName").val();
    var q=JSON.stringify(employee);
    $.ajax({
        type: "post",
        url: path + "/employee/updateEmployee",
        data: JSON.stringify(employee),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            layer.close(index);
            showEmployeeList();
        }
    });
}
function doubleUp1() {
    $(".doubleUpBtn1").css("display", "none");
    $(".userTbody").css("display","none");
    $(".doubleDownBtn1").css("display","inline-block");
}
function doubleDown1() {
    $(".doubleUpBtn1").css("display", "inline-block");
    $(".doubleDownBtn1").css("display","none");
    $(".userTbody").css("display","block");
}
function doubleUp2() {
    $(".doubleUpBtn2").css("display", "none");
    $(".payTbody").css("display","none");
    $(".doubleDownBtn2").css("display","inline-block");
}
function doubleDown2() {
    $(".doubleUpBtn2").css("display", "inline-block");
    $(".doubleDownBtn2").css("display","none");
    $(".payTbody").css("display","block");
}
//取消
function cancel() {
    $(".updateUser").css("display","none");
    layer.close(index);
}