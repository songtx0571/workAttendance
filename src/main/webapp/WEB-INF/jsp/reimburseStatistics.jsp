<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/reimburseStatistics.js"></script>
    <style>
        *{
            margin: 0;
            padding: 0;
        }
        .warp{
            width: 100%;
            height: 100%;
        }
        .top{
            height: 80px;
            padding: 20px 0;
            box-sizing: border-box;
        }
        .content{
            padding: 0 10px;
            box-sizing: border-box;
        }
        body::-webkit-scrollbar{
            display: none;
        }
    </style>
</head>
<body>
<div class="warp">
    <div class="top">
        <div class="layui-inline" style="margin-bottom: 10px;float:left;margin-left: 10px;">
            <label class="layui-form-label">年份</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" id="test2" placeholder="yyyy">
            </div>
        </div>
        <form class="layui-form" action="">
            <input type="hidden" id="selMonthHidden">
            <div class="layui-inline" style="float: left;margin-left: 50px;">
                <div class="layui-input-inline">
                    <select name="modules" lay-verify="required" lay-filter="selMonth" lay-search="" id="selMonth">
                        <option value="0">请选择月份</option>
                        <option value="1">1月</option>
                        <option value="2">2月</option>
                        <option value="3">3月</option>
                        <option value="4">4月</option>
                        <option value="5">5月</option>
                        <option value="6">6月</option>
                        <option value="7">7月</option>
                        <option value="8">8月</option>
                        <option value="9">9月</option>
                        <option value="10">10月</option>
                        <option value="11">11月</option>
                        <option value="12">12月</option>
                    </select>
                </div>
            </div>
        </form>
        <form class="layui-form" action="">
            <div class="layui-inline" style="float: left;margin-left: 50px;">
                <div class="layui-input-inline">
                    <input type="hidden" id="selDepartNameHidden">
                    <select name="modules" lay-verify="required" lay-filter="selDepartName" lay-search="" id="selDepartName">
                    </select>
                </div>
            </div>
        </form>
    </div>
    <div class="content">
        <table id="demo" lay-filter="test"></table>
        <script type="text/html" id="barDemoDepartSubject">
            {{#  if(d.subject == 1){ }}
            <span>差旅费</span>
            {{#  } else if(d.subject == 2) { }}
            <span>差旅补助</span>
            {{#  } else if(d.subject == 3) { }}
            <span>招待费</span>
            {{#  } else if(d.subject == 4) { }}
            <span>办公费</span>
            {{#  } else if(d.subject == 5) { }}
            <span>劳保费</span>
            {{#  } else if(d.subject == 6) { }}
            <span>员工福利费</span>
            {{#  } else if(d.subject == 7) { }}
            <span>员工保险费</span>
            {{#  } else if(d.subject == 8) { }}
            <span>员工培训费</span>
            {{#  } else if(d.subject == 9) { }}
            <span>车辆费用</span>
            {{#  } else if(d.subject == 10) { }}
            <span>器具</span>
            {{#  } else if(d.subject == 11) { }}
            <span>固定资产</span>
            {{#  } else if(d.subject == 12) { }}
            <span>房租</span>
            {{#  } else if(d.subject == 14) { }}
            <span>工资</span>
            {{#  } else if(d.subject == 15) { }}
            <span>社保、公积金</span>
            {{#  } else if(d.subject == 16) { }}
            <span>中介机构费</span>
            {{#  } else { }}
            <span>其他</span>
            {{#  } }}
        </script>
    </div>
</div>
</body>
</html>
