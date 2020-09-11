// var path = "http://192.168.1.26:8081";
var path = "";
$(function(){
    var userId=$("#userId").val();
    var startTime=$("#startTime").val();
    var depart=$("#depart").val();
    $('#selTable').datagrid({
        url: path +'/wa/kpi/getSelWorkHoursList',
        method: 'get',
        title: '查看通知数据展示',
        //width: 'auto',
        height: 600,
        //fitColumns: true,//自适应列
        loadMsg: '正在加载信息...',
        pagination: true,//允许分页
        //singleSelect: true,//单行选中。
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 15, 20, 30, 50],
        queryParams: { 'userId': userId,'startTime': startTime,'depart':depart}, //往后台传参数用的。
        columns: [[
            {field: 'day', title: '日期', width: 30, align: 'center'},
            {field: 'workHours', title: '工时', width: 30, align: 'center',
                formatter:function (value, row, index) {
                    var html='';
                    if(row.workHours>0){
                        html='<span style="color: #c62828">'+row.workHours+'</span>';
                    }else{
                        html='<span style="color: #4d4d4d">'+row.workHours+'</span>';
                    }
                    return html;
                }
            },
        ]],
        onClickRow: function(rowIndex, rowData){
            $('#selTable').datagrid('clearSelections');
        },
        onLoadSuccess: function (data) {
            if (data.total == 0) {

            }
            else $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').show();
        },
    });
});