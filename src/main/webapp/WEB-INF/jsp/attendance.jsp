<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/icon.css" />
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/default/easyui.css" />
    <script type="text/javascript" src="../js/week/attendance.js?version=1.0"></script>
</head>
<style type="text/css">

</style>
<body class="easyui-layout">
    <input type="text" value="<%=request.getAttribute("attendance") %>" id="type" hidden/>
    <table id="attendance" class="easyui-datagrid" title="信息列表"
           fitColumns="true" pagination="true" rownumbers="true"
           fit="true" toolbar="#attendanceBar">
    </table>
    <div id="attendanceBar" style="height: 100px;text-align: center;line-height: 100px;">
        <div onclick="javascript:search()" style="width: 100%;float: left;height: 100px;line-height: 100px;background-color: #00ee00;border-radius: 8px;display: inline-block">
            <img src="../../img/sousuo.png" width="100px" height="100px"/>
        </div>
    </div>
    <%--搜索弹窗--%>
    <div style="padding: 30px 30px 30px 30px;" hidden id="search">
        <form method="post">
            <table style="border-collapse:separate; border-spacing:0px 20px;">
                <tr id="tr1">
                    <td style="float: right">部门&nbsp;&nbsp;</td>
                    <td colspan="2"><select id="depart" class="easyui-combobox"  data-options="required:true,prompt:'请选择项目部',width:'350px'" style="width:350px;height: 40px;line-height: 40px"></select></td>
                </tr>
                <tr id="tr2">
                    <td style="float: right">员工&nbsp;&nbsp;</td>
                    <td colspan="2"><select id="empName" class="easyui-combobox"  data-options="required:true,prompt:'请选择员工',width:'350px'" style="width:350px;height: 40px;line-height: 40px"></select></td>
                </tr>
                <tr id="tr3">
                    <td style="float: right">日期&nbsp;&nbsp;</td>
                    <td colspan="2">
                        <select id="startTime" class="easyui-datebox"  data-options="required:true,width:'165px'"></select>
                        <span>--</span>
                        <select id="endTime" class="easyui-datebox"  data-options="required:true,width:'165px'"></select>
                    </td>
                </tr>
            </table>
            <div style="text-align: center; padding: 5px;height: 80px;line-height: 80px">
                <div id="searchOk" style="height: 40px;width: 80px;border-radius: 5px;background-color: #00bbee;display: inline-block;line-height: 40px;" onclick="javascript:showLaborAll()" class="easyui-linkbutton" plain="true">
                    <a href="javascript:void(0)" id="btn-save" style="text-decoration: none;color: #222222">确定</a>
                </div>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <div style="height: 40px;width: 80px;border-radius: 5px;background-color: #00bbee;display: inline-block;line-height: 40px;" onclick="javascript:closeSearch(searchd)">
                    <a href="javascript:void(0)" id="btn-close-save" style="text-decoration: none;color: #222222">取消</a>
                </div>
            </div>
        </form>
    </div>
</body>
</html>