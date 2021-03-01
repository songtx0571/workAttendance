var path = "";
var date = new Date();
var year = date.getFullYear();
var month = date.getMonth() + 1;
$(function(){
    //显示考核日期
    showCycleData();
    //显示部门
    showCompany();
    // 查询员工绩效信息
    showTable(year,"");
    $("#test2").val(year);
    $("#test3").val(year+"-"+month);
});
/*显示考核日期*/
function showCycleData() {
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //查询所有数据日期
        laydate.render({
            elem: '#test2'
            , type: 'year'
            , trigger: 'click'//呼出事件改成click
            , done: function (value) {
                showTable(value,"");
            }
        });
    });
}
//显示部门
function showCompany() {
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
            showTable($("#test3").val(),$("#selDepartNameHidden").val())
        });
        form.on('select(selMonth)', function (data) {
            $("#selMonthHidden").val(data.value);
            if ($("#tetst2").val() == "") {
                layer.alert("请选择年份");
                return;
            }
            if ($("#selMonthHidden").val() < 10) {
                $("#selMonthHidden").val("0"+$("#selMonthHidden").val());
            }
            showTable($("#test2").val()+"-"+$("#selMonthHidden").val(),$("#selDepartNameHidden").val());
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
            , url: path + '/wa/reimbursement/getStatistics?month='+cycle+'&depart='+depart //数据接口
            ,toolbar: true
            ,totalRow: true
            , page: false
            , cols: [[ //表头
                {field: 'id', title: '编号', align: 'center', hide: true}
                ,{fixed: '', title: '科目', toolbar: '#barDemoDepartSubject', align: 'center', sort: true, totalRowText: '合计'}
                ,{field: 'totalAmount', title: '报销金额', align: 'center', totalRow: true}
            ]]
            , done: function (res, curr, count) {}
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
        });
    });
}