// var path = "http://192.168.1.26:8081/";
var path = "";
$(function(){
    $('#startTime').datebox({
        onShowPanel : function() {// 显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
            span.trigger('click'); // 触发click事件弹出月份层
            if (!tds)
                setTimeout(function() {// 延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                    tds = p.find('div.calendar-menu-month-inner td');
                    tds.click(function(e) {
                        e.stopPropagation(); // 禁止冒泡执行easyui给月份绑定的事件
                        var year = /\d{4}/.exec(span.html())[0];
                        var month = parseInt($(this).attr('abbr'), 10) ;
                        if(month+1<10){
                            $('#startTime').datebox('hidePanel')// 隐藏日期对象
                                .datebox('setValue', year + '-0' + month); // 设置日期的值
                        }else{
                            $('#startTime').datebox('hidePanel')// 隐藏日期对象
                                .datebox('setValue', year + '-' + month); // 设置日期的值
                        }
                    });
                }, 0);
        },
        parser : function(s) {// 配置parser，返回选择的日期
            if (!s)
                return new Date();
            var arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
        },
        formatter: function (d) {
            if(d.getMonth()+1<10){
                return d.getFullYear() + '-0' + (d.getMonth() + 1);
            }else{
                return d.getFullYear() + '-' + (d.getMonth() + 1);
            }
        }
    });
    var p = $('#startTime').datebox('panel'), // 日期选择对象
    tds = false, // 日期选择对象中月份
    span = p.find('span.calendar-text'); // 显示月份层的触发控件
});

/**
 * 弹窗
 */
function search() {
    var type=$("type").val();
    //获取部门信息
    $.ajax({
        type:"post",
        url:path + "/wa/department/getDepartmentList",
        dataType:"json",
        success:function(json){
            $("#depart").combobox({//往下拉框塞值
                data:json,
                valueField:"id",//value值
                textField:"text",//文本值
                panelHeight:"300",
                onChange: function (n,o) {
                    var getValue =$("#depart").combobox('getValue');
                    //获取员工信息
                    $.ajax({
                        type:"post",
                        url:path +"/getNameByProjectId",
                        data:{'projectId':getValue},
                        dataType:"json",
                        success:function(json){
                            $("#empName").combobox({//往下拉框塞值
                                data:json,
                                valueField:"id",//value值
                                textField:"text",//文本值
                                panelHeight:"300",
                            });
                        }
                    });
                }
            });
        }
    });

    searchd=$('#search').window({
        title:'工时查询',
        height: 400,
        width: 500,
        closed: true,
        minimizable:false,
        maximizable:false,
        collapsible:false,
        cache:false,
        shadow:true
    });
    searchd.window('open');
}

/**
 * 关闭弹窗
 */
function closeSearch() {
    searchd.window('close');
}

/**
 * 查询工时数据
 */
function showLaborAll(){
    var type=$("type").val();
    var userName= $('#empName').combobox('getValue');
    var MonthDate= $('#startTime').combobox('getText');
    var DayDateT= $('#endTime').combobox('getText');

    $('#attendance').datagrid({
        url: path + '/wa/attendance/showLaborAll',
        method: 'get',
        title: '设备创建',
        //width: 'auto',
        height: 600,
        //fitColumns: true,//自适应列
        loadMsg: '正在加载信息...',
        pagination: true,//允许分页
        //singleSelect: true,//单行选中。
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 15, 20, 30, 50],
        queryParams: { 'type': type,'userName':userName,'MonthDate':MonthDate,'DayDateT':DayDateT}, //往后台传参数用的。
        columns: [[
            {field: 'id', title: '编号', align: 'center', hidden:true},
            {field: 'maintenanceId', title: '', align: 'center', hidden:true},
            {field: 'people', title: '员工号', align: 'center', hidden:true},
            {field: 'type', title: '类型', align: 'center', hidden:true},
            {field: 'peopleName', title: '员工名称', align: 'center', hidden:true},
            {field: 'defectNumber', title: '故障号', width: 30,align: 'center',height: 10},
            {field: 'content', title: '工作详情', width: 30,align: 'center',height: 10},
            {field: 'workingHours', title: '工时详情', width: 10, align: 'center',height: 10},
            {field: 'datetime', title: '创建时间', width: 10, align: 'center',height: 10}
        ]],
        onClickRow: function(rowIndex, rowData){
            $('#attendance').datagrid('clearSelections');
        },
        onLoadSuccess: function (data) {
            if (data.total == 0) {

            }
            else $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').show();
        },
    });
}
