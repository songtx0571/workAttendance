<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/icon.css" />
    <link rel="stylesheet" type="text/css" href="../js/easyui/themes/default/easyui.css" />
    <script type="text/javascript" src="../js/week/selWorkHours.js"></script>
</head>
<style type="text/css">

</style>
<script type="text/javascript">

</script>
<body class="easyui-layout">
    <input type="text" id="userId" value="${userId}" hidden />
    <input type="text" id="startTime" value="${startTime}" hidden />
    <input type="text" id="depart" value="${depart}" hidden />
    <table id="selTable" class="easyui-datagrid" title="工时详情${userId}"
           fitColumns="true" pagination="true" rownumbers="true" fit="true">
    </table>
</body>
</html>