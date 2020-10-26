var path = "";
$(function(){
    date();
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

/**
 * 查询通知数据
 */
function showKpi(){
    var startTime= $('#startTime').combobox('getText');

    $('#KPITable').datagrid({
        url: path + '/wa/kpi/getInformKPIList',
        method: 'get',
        title: 'kpi数据列表',
        width: 'auto',
        height: 'auto',
        //fitColumns: true,//自适应列
        loadMsg: '正在加载信息...',
        pagination: true,//允许分页
        //singleSelect: true,//单行选中。
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 15, 20, 30, 50],
        queryParams: {'startTime':startTime}, //往后台传参数用的。
        columns: [[
            {field: 'Id', title: '编号', align: 'center', hidden:true,width: 120},
            {field: 'userName', title: '姓名', align: 'center',width: 750},
            {field: 'createdCount', title: '创建数', align: 'center',width: 750,
                formatter: function (value, row, index) {
                    var html='<a href="javascript:toCreatedCount('+row.Id+')" style="text-decoration: none">'+row.createdCount+'</a>';
                    return html;
                }
            },
            {field: 'selCount', title: '查看数', align: 'center',width: 750,
                formatter: function (value, row, index) {
                    var html='<a href="javascript:toSelCount('+row.Id+')" style="text-decoration: none">'+row.selCount+'</a>';
                    return html;
                }
            },
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

/**
 * 查询当前数据具体类容
 * @param userId
 */
function toCreatedCount(userId) {
    var startTime= $('#startTime').combobox('getText');
    var text="创建数-"+userId;
    if (parent.$('#tabs').tabs('exists',text)){
        parent.$('#tabs').tabs('select', text);
    }else {
        var content = '<iframe width="100%" height="100%" frameborder="0" src="/wa/kpi/toCreated?userId='+userId+',&startTime='+startTime+'" style="width:100%;height:100%;margin:0px 0px;"></iframe>';
        parent.$('#tabs').tabs('add',{
            title:text,
            content:content,
            closable:true
        });
    }
}

/**
 * 查询当前数据具体类容
 * @param userId
 */
function toSelCount(userId) {
    var startTime= $('#startTime').combobox('getText');
    var text="查看数-"+userId;
    if (parent.$('#tabs').tabs('exists',text)){
        parent.$('#tabs').tabs('select', text);
    }else {
        var content = '<iframe width="100%" height="100%" frameborder="0" src="/wa/kpi/toSel?userId='+userId+',&startTime='+startTime+'" style="width:100%;height:100%;margin:0px 0px;"></iframe>';
        parent.$('#tabs').tabs('add',{
            title:text,
            content:content,
            closable:true
        });
    }
}
