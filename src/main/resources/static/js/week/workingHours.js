$(function(){
    date();
    init();
});

function date() {
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
}

//初始化
function init(){

    //获取部门信息
    $.ajax({
        type:"post",
        url:"/wa/department/getDepartmentList",
        dataType:"json",
        success:function(json){
            $("#depart").combobox({//往下拉框塞值
                data:json,
                valueField:"id",//value值
                textField:"text",//文本值
                panelHeight:"300",
            });
        }
    });
}

/**
 * 查询工时数据
 */
function showKpi(){
    var depart= $('#depart').combobox('getValue');
    var startTime= $('#startTime').combobox('getText');

    if(depart==null||depart==''){
        $.messager.alert("提示","请选择部门");
        return;
    }

    $('#KPITable').datagrid({
        url: '/wa/kpi/getWorkHoursList',
        method: 'get',
        title: 'kpi数据列表',
        width: 'auto',
        height: 'auto',
        //fitColumns: true,//自适应列
        loadMsg: '正在加载信息...',
        sortName:'workingHours',		// 排序的字段
        sortOrder:'desc',			// 升序或者降序
        remoteSort:false,			// 定义从服务器对数据进行排序。
        pagination: true,//允许分页
        //singleSelect: true,//单行选中。
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 15, 20, 30, 50],
        queryParams: { 'depart':depart,'startTime':startTime}, //往后台传参数用的。
        columns: [[
            {field: 'id', title: '编号', align: 'center', hidden:true,width: 120},
            {field: 'name', title: '员工', align: 'center',width: 750},
            {field: 'workingHours', title: '月度总工时',sortable :true, align: 'center',width: 750,
                formatter: function (value, row, index) {
                    var html='<a href="javascript:toSelWorkHours('+row.id+','+depart+')" style="text-decoration: none">'+row.workingHours+'</a>';
                    return html;
                }
            }
        ]],
        onClickRow: function(rowIndex, rowData){
            $('#KPITable').datagrid('clearSelections');
        },
        onLoadSuccess: function (data) {
            if (data.total == 0) {

            }
            else $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').show();
        },
    });
}

function toSelWorkHours(userId,depart) {
    var startTime= $('#startTime').combobox('getText');
    var text="工时详情-"+userId;
    if (parent.$('#tabs').tabs('exists',text)){
        parent.$('#tabs').tabs('select', text);
    }else {
        var content = '<iframe width="100%" height="100%" frameborder="0" src="/wa/kpi/toSelWorkHours?userId='+userId+',&startTime='+startTime+',&depart='+depart+'" style="width:100%;height:100%;margin:0px 0px;"></iframe>';
        parent.$('#tabs').tabs('add',{
            title:text,
            content:content,
            closable:true
        });
    }
}

