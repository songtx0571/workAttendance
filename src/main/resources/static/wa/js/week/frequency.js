// var path = "http://192.168.1.26:8081/";
var path = "";
$(function(){
    var userId=$("#userId").val();
    var startTime= $('#startTime').val();
    $('#frequencyTable').datagrid({
        url: path + '/wa/kpi/getFrequencyList',
        method: 'get',
        title: '巡检次数数据展示',
        //width: 'auto',
        height: 600,
        //fitColumns: true,//自适应列
        loadMsg: '正在加载信息...',
        pagination: true,//允许分页
        //singleSelect: true,//单行选中。
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 15, 20, 30, 50],
        queryParams: { 'userId': userId,'startTime':startTime }, //往后台传参数用的。
        columns: [[
            {field: 'id', title: '编号', width: 30, align: 'center',hidden:true},
            {field: 'inspectionStaTime', title: '开始执行时间',align: 'center', width: 30},
            {field: 'inspectionEndTime', title: '实际结束时间', width: 30, align: 'center'},
            {field: 'inspectionEndTheoryTime', title: '理论结束时间', width: 30, align: 'center'},
        ]],
        onClickRow: function(rowIndex, rowData){
            $('#frequencyTable').datagrid('clearSelections');
        },
        onLoadSuccess: function (data) {
            if (data.total == 0) {

            }
            else $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').show();
        },
    });
});