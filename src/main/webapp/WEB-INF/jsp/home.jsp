<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon" />
    <title>浩维管理平台</title>
</head>
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript" src="../js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../js/easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="../js/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="../js/easyui/themes/icon.css?version=1.0" />
<script type="text/javascript" src="../js/week/home.js?version=1.11"></script>

<script type="text/javascript">
</script>
<body class="easyui-layout">
<!--  页面上方区域     -->
<div region="north" split="true" style="height:60px;font-size: 26px;text-align: center;padding: 8px;background-color:#e0ecff">
    <div style="float: left;">欢迎您:${userName}</div>
</div>

<div data-options="region:'west',title:'导航菜单',split:true,iconCls:'icon-text-align-justify'" style="width:200px;height: auto">
    <div id="nav" class="easyui-accordion" fit="true" border="false">

    </div>
</div>
<!--  页面中间内容（主面板）区域     -->
<div region="center">
    <div class="easyui-tabs" fit="true" border="false" id="tabs">
        <div title="首页">
            <%--<table id="home" class="easyui-datagrid" title=""
                   fitColumns="true" pagination="true" rownumbers="true"
                   fit="true" toolbar="#tb" >
            </table>--%>
        </div>
    </div>
</div>
<%--右键菜单--%>
<div id="mm" class="easyui-menu" style="width:120px;">
    <div id="mm-tabclose" data-options="name:1">关闭</div>
    <div id="mm-tabcloseall" data-options="name:2">全部关闭</div>
    <div id="mm-tabcloseother" data-options="name:3">其他关闭</div>
</div>
</body>
</html>
