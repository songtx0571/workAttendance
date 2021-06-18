var path = "";
$(function () {
    // 显示员工信息();
    showEmployeeList('distribution');
    //显示绩效管理人
    showManagerName();
    /*根据姓名搜索员工信息*/
    $("#searchBtn").click(function () {
        var searchName = $("#searchName").val();
        var top = $(".bodyHeader").css("height");
        var win = $(window).height();
        var tp = top.indexOf("p");
        var topHeight = top.substring(0, tp);
        var height = win - topHeight - 20;
        layui.use('table', function () {
            var table = layui.table;
            table.render({
                elem: '#demo'
                , height: height
                , toolbar: true
                , totalRow: true
                , url: path + "/wa/employee/searchEmployee?search=" + searchName //数据接口
                , cols: [[ //表头
                    {field: 'userNumber', title: '编号', width: 80, sort: true}
                    , {field: 'name', title: '姓名', width: 100}
                    , {field: 'sexName', title: '性别', width: 80}// 1男
                    , {field: 'companyName', title: '公司', hide: true}
                    , {field: 'departmentName', title: '部门', sort: true}
                    , {field: 'postName', title: '岗位', sort: true}
                    , {field: 'managerName', title: '绩效管理人', sort: true}
                    , {field: 'stateName', title: '在职状态', width: 110, sort: true, hide: true}// 1 在职
                    , {fixed: '', title: '操作', toolbar: '#barDemo11', width: 70, align: 'center '}
                ]]
                , done: function (res, curr, count) {
                }
            });
            //监听行工具事件
            table.on('tool(test)', function (obj) {
                var data = obj.data;
                $("#userIdHidden").val(data.id)
                shouInfo();
                if (obj.event === 'edit') {
                    index = layer.open({
                        type: 1
                        , id: 'updateUser' //防止重复弹出
                        , content: $(".updateUser")
                        , btnAlign: 'c' //按钮居中
                        , shade: 0.5 //不显示遮罩
                        , area: ['100%', '100%']
                        , yes: function () {
                        }
                    });
                }
            });
        });
    });
    $('#searchName').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            $("#searchBtn").click();
        }
    });
    $.ajax({
        type: "GET",
        url: path + "/wa/employee/getNoDistributionList",
        data: {},
        dataType: "text",
        success: function (data) {
            $("#peopleNum").html(data)
        }
    })
});

//显示绩效管理人  、 劳务派遣
function showManagerName() {
    layui.use(['form'], function () {
        var form = layui.form;
        $.ajax({
            type: "GET",
            url: path + "/wa/employee/getEmployeeList",
            dataType: "json",
            success: function (data) {
                data = data.data;
                $("#managerName").empty();
                var option = "<option value='0' >请选择绩效管理人</option>";
                for (var i = 0; i < data.length; i++) {
                    option += "<option value='" + data[i].id + "'>" + data[i].name + "</option>";
                }
                $('#managerName').append(option);
                form.render();//菜单渲染 把内容加载进去
            }
        });
        form.on('select(managerName)', function (data) {
            $("#managerHidden").val(data.value);//得到被选中的值
        });
        form.on('select(laowupaiqian)', function (data) {
            $("#laowupaiqianHidden").val(data.value);//得到被选中的值
        });
        form.on('select(isChanged)', function (data) {
            $("#isChangedHidden").val(data.value);//得到被选中的值
        });
    });
}

/*查询员工信息*/
function showEmployeeList(type) {
    var top = $(".bodyHeader").css("height");
    var win = $(window).height();
    var tp = top.indexOf("p");
    var topHeight = top.substring(0, tp);
    var height = win - topHeight - 20;
    layui.use('table', function () {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: height
            , toolbar: true
            , totalRow: true
            , url: path + '/wa/employee/getEmployeeList?sign='+type  //数据接口
            , cols: [[ //表头
                {field: 'userNumber', title: '编号', width: 80, sort: true}
                , {field: 'name', title: '姓名', width: 100}
                , {field: 'sexName', title: '性别', width: 80}// 1男
                , {field: 'companyName', title: '公司', hide: true}
                , {field: 'departmentName', title: '部门', sort: true}
                , {field: 'postName', title: '岗位', sort: true}
                , {field: 'managerName', title: '绩效管理人', sort: true}
                , {field: 'isChanged', title: '人事异动', sort: true}
                , {field: 'stateName', title: '在职状态', width: 110, sort: true, hide: true}// 1 在职
                , {fixed: '', title: '操作', toolbar: '#barDemo11', width: 70, align: 'center '}
            ]]
            , done: function (res, curr, count) {
            }
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            $("#userIdHidden").val(data.id);
            shouInfo();
            if (obj.event === 'edit') {
                index = layer.open({
                    type: 1
                    , id: 'updateUser' //防止重复弹出
                    , content: $(".updateUser")
                    , btnAlign: 'c' //按钮居中
                    , shade: 0.5 //不显示遮罩
                    , area: ['100%', '100%']
                    , yes: function () {
                    }
                });
            }
        });
    });
}

//修改数据的显示
function shouInfo() {
    $.ajax({
        type: "GET",
        url: path + "/wa/employee/getEmployee",
        data: {"id": $("#userIdHidden").val()},
        dataType: "json",
        success: function (data) {
            $("#name").val(data.name);//姓名
            $("#sex").val(data.sexName);//性别
            $("#phone").val(data.phone);//电话
            $("#idNumber").val(data.idNumber); //身份证
            $("#address").val(data.address);//住址
            $("#remark").val(data.remark);//备注
            $("#sign").val(data.sign);//签名
            $("#email").val(data.email);//邮箱
            $("#emergency").val(data.emergency);//应急联系人
            $("#emergencyTel").val(data.emergencyTel); //应急电话
            $("#closhe").val(data.closhe);//衣服尺寸
            $("#hat").val(data.hat);//安全帽编号
            $("#laowupaiqianHidden").val(data.laowupaiqian);//劳务派遣
            $("#isChangedHidden").val(data.isChanged);//人事异动
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
            layui.use('form', function () {
                var form = layui.form;
                $("#managerName").val(data.manager);//绩效管理人
                $("#laowupaiqian").val(data.laowupaiqian);//绩效管理人
                $("#isChanged").val(data.isChanged);//绩效管理人
                form.render('select');
                form.render(); //更新全部
            });
        }
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
    employee.sign = $("#sign").val();
    employee.email = $("#email").val();
    employee.remark = $("#remark").val();
    employee.emergency = $("#emergency").val();
    employee.emergencyTel = $("#emergencyTel").val();
    employee.closhe = $("#closhe").val();
    employee.hat = $("#hat").val();
    employee.laowupaiqian = $("#laowupaiqianHidden").val();
    employee.isChanged = $("#isChangedHidden").val();
    employee.education = $("#education").val();
    employee.entryDate = $("#entryDate").val();
    employee.departmentName = $("#departmentName").val();
    employee.postName = $("#postName").val();
    employee.userNumber = $("#userNumber").val();
    employee.credentials1 = $("#credentials1").val();
    employee.credentials2 = $("#credentials2").val();
    employee.credentials3 = $("#credentials3").val();
    employee.manager = $("#managerHidden").val();
    employee.bank = $("#bank").val();//开户行
    employee.card = $("#card").val();//银行卡号
    if (employee.isChanged == "") {
        employee.isChanged = "0";
    }
    if (employee.sign.length > 30) {
        layer.alert("签名输入过长；");
        return;
    }
    $.ajax({
        type: "post",
        url: path + "/wa/employee/updateEmployee",
        data: JSON.stringify(employee),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            layer.close(index);
            showEmployeeList('distribution');
        }
    });
}

//取消
function cancel() {
    $(".updateUser").css("display", "none");
    layer.close(index);
}