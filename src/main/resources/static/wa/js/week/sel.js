var path = "";
$(function(){
    var userId=$("#userId").val();
    $('#selTable').datagrid({
        url: path +'/wa/kpi/getSelList',
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
        queryParams: { 'userId': userId }, //往后台传参数用的。
        columns: [[
            {field: 'id', title: '编号', width: 30, align: 'center',hidden:true},
            {field: 'created', title: '通知时间', width: 30, align: 'center'},
            {field: 'title', title: '标题', width: 30, align: 'center'},
            {field: 'content', title: '内容',align: 'center', width: 30},
            {field: 'createdByName', title: '发起人', width: 30, align: 'center'},
        ]],
        onClickRow: function(rowIndex, rowData){
            $('#pointTable').datagrid('clearSelections');
        },
        onLoadSuccess: function (data) {
            if (data.total == 0) {

            }
            else $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').show();
        },
    });
});