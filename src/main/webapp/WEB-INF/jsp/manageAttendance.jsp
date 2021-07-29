<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/layui/layui.js"></script>
    <link rel="stylesheet" href="../js/layui/css/layui.css">
    <script type="text/javascript" src="../js/week/manageAttendance.js"></script>

    <style>
        .warp {
            margin: 0;
            padding: 0;
        }

        .top {
            height: 60px;
            padding-top: 15px;
            box-sizing: border-box;
        }

        .center {
            width: 100%;
            padding: 0px 10px 0 10px;
            box-sizing: border-box;
        }

        #goWorkBtn {
            display: none;
            margin-left: 10px;
            float: left;
            line-height: 38px;
            color: #000;
            font-size: 20px;
            text-align: center;
            cursor: pointer;
        }

        #goWorkBtn span {
            font-size: 16px;
            padding: 0px 10px;
            box-sizing: border-box;
            background: #1E9FFF;
            color: #fff;
            display: inline-block;
        }
        .loading {
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            background-color: rgb(0, 0, 0);
            opacity: 0.8;
            text-align: center;
            padding-top: 300px;
            z-index: 9999999;
            display: none;
        }

        .loading div {
            animation: turn 1s linear infinite;
        }

        @keyframes turn {
            0% {
                -webkit-transform: rotate(0deg);
            }
            25% {
                -webkit-transform: rotate(90deg);
            }
            50% {
                -webkit-transform: rotate(180deg);
            }
            75% {
                -webkit-transform: rotate(270deg);
            }
            100% {
                -webkit-transform: rotate(360deg);
            }
        }
        .layui-layer-tips {
            width: 150px;
        }
        .layui-table-cell{
            padding: 0;
        }
    </style>
</head>
<body>
<div class="warp">
    <div class="top">
        <button class="layui-btn layui-btn-sm" style="margin-left: 50px;float: left;height: 38px;margin-right: 5px;"
                onclick="monthUpBtn()">&nbsp;&nbsp;&lt;&lt;&nbsp;&nbsp;
        </button>
        <div class="layui-inline" style="margin-bottom: 10px;float:left;">
            <div class="layui-input-inline">
                <input type="text" class="layui-input" id="test" placeholder="年月">
            </div>
        </div>
        <button class="layui-btn layui-btn-sm" style="float: left;height: 38px;margin-left: 5px;"
                onclick="monthDownBtn()">&nbsp;&nbsp;&gt;&gt;&nbsp;&nbsp;
        </button>
        <p id="goWorkBtn"><span onclick="goWork()">上班</span></p>

    </div>
    <%--表格--%>
    <div class="center">
        <table id="demo" lay-filter="test"></table>
        <script type="text/html" id="barDemo1">
            {{#  if(d.data["01"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['01'].detail.monthDay}}01" onclick="showTime('{{d.data["01"].detail.workStartTime}}<br>{{d.data["01"].detail.workEndTime}}','{{d.employeeId}}{{d.data["01"].detail.monthDay}}01')">{{d.data["01"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['01'].detail.monthDay}}01" onclick="showTime('无','{{d.employeeId}}{{d.data["01"].detail.monthDay}}01')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo2">
            {{#  if(d.data["02"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['02'].detail.monthDay}}02" onclick="showTime('{{d.data["02"].detail.workStartTime}}<br>{{d.data["02"].detail.workEndTime}}','{{d.employeeId}}{{d.data["02"].detail.monthDay}}02')">{{d.data["02"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['02'].detail.monthDay}}02" onclick="showTime('无','{{d.employeeId}}{{d.data["02"].detail.monthDay}}02')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo3">
            {{#  if(d.data["03"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['03'].detail.monthDay}}03" onclick="showTime('{{d.data["03"].detail.workStartTime}}<br>{{d.data["03"].detail.workEndTime}}','{{d.employeeId}}{{d.data["03"].detail.monthDay}}03')">{{d.data["03"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['03'].detail.monthDay}}03" onclick="showTime('无','{{d.employeeId}}{{d.data["03"].detail.monthDay}}03')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo4">
            {{#  if(d.data["04"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['04'].detail.monthDay}}04" onclick="showTime('{{d.data["04"].detail.workStartTime}}<br>{{d.data["04"].detail.workEndTime}}','{{d.employeeId}}{{d.data["04"].detail.monthDay}}04')">{{d.data["04"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['04'].detail.monthDay}}04" onclick="showTime('无','{{d.employeeId}}{{d.data["04"].detail.monthDay}}04')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo5">
            {{#  if(d.data["05"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['05'].detail.monthDay}}05" onclick="showTime('{{d.data["05"].detail.workStartTime}}<br>{{d.data["05"].detail.workEndTime}}','{{d.employeeId}}{{d.data["05"].detail.monthDay}}05')">{{d.data["05"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['05'].detail.monthDay}}05" onclick="showTime('无','{{d.employeeId}}{{d.data["05"].detail.monthDay}}05')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo6">
            {{#  if(d.data["06"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['06'].detail.monthDay}}06" onclick="showTime('{{d.data["06"].detail.workStartTime}}<br>{{d.data["06"].detail.workEndTime}}','{{d.employeeId}}{{d.data["06"].detail.monthDay}}06')">{{d.data["06"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['06'].detail.monthDay}}06" onclick="showTime('无','{{d.employeeId}}{{d.data["06"].detail.monthDay}}06')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo7">
            {{#  if(d.data["07"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['07'].detail.monthDay}}07" onclick="showTime('{{d.data["07"].detail.workStartTime}}<br>{{d.data["07"].detail.workEndTime}}','{{d.employeeId}}{{d.data["07"].detail.monthDay}}07')">{{d.data["07"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['07'].detail.monthDay}}07" onclick="showTime('无','{{d.employeeId}}{{d.data["07"].detail.monthDay}}07')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo8">
            {{#  if(d.data["08"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['08'].detail.monthDay}}08" onclick="showTime('{{d.data["08"].detail.workStartTime}}<br>{{d.data["08"].detail.workEndTime}}','{{d.employeeId}}{{d.data["08"].detail.monthDay}}08')">{{d.data["08"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['08'].detail.monthDay}}08" onclick="showTime('无','{{d.employeeId}}{{d.data["08"].detail.monthDay}}08')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo9">
            {{#  if(d.data["09"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['09'].detail.monthDay}}09" onclick="showTime('{{d.data["09"].detail.workStartTime}}<br>{{d.data["09"].detail.workEndTime}}','{{d.employeeId}}{{d.data["09"].detail.monthDay}}09')">{{d.data["09"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['09'].detail.monthDay}}09" onclick="showTime('无','{{d.employeeId}}{{d.data["09"].detail.monthDay}}09')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo10">
            {{#  if(d.data["10"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['10'].detail.monthDay}}10" onclick="showTime('{{d.data["10"].detail.workStartTime}}<br>{{d.data["10"].detail.workEndTime}}','{{d.employeeId}}{{d.data["10"].detail.monthDay}}10')">{{d.data["10"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['10'].detail.monthDay}}10" onclick="showTime('无','{{d.employeeId}}{{d.data["10"].detail.monthDay}}10')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo11">
            {{#  if(d.data["11"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['11'].detail.monthDay}}11" onclick="showTime('{{d.data["11"].detail.workStartTime}}<br>{{d.data["11"].detail.workEndTime}}','{{d.employeeId}}{{d.data["11"].detail.monthDay}}11')">{{d.data["11"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['11'].detail.monthDay}}11" onclick="showTime('无','{{d.employeeId}}{{d.data["11"].detail.monthDay}}11')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo12">
            {{#  if(d.data["12"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['12'].detail.monthDay}}12" onclick="showTime('{{d.data["12"].detail.workStartTime}}<br>{{d.data["12"].detail.workEndTime}}','{{d.employeeId}}{{d.data["12"].detail.monthDay}}12')">{{d.data["12"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['12'].detail.monthDay}}12" onclick="showTime('无','{{d.employeeId}}{{d.data["12"].detail.monthDay}}12')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo13">
            {{#  if(d.data["13"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['13'].detail.monthDay}}13" onclick="showTime('{{d.data["13"].detail.workStartTime}}<br>{{d.data["13"].detail.workEndTime}}','{{d.employeeId}}{{d.data["13"].detail.monthDay}}13')">{{d.data["13"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['13'].detail.monthDay}}13" onclick="showTime('无','{{d.employeeId}}{{d.data["13"].detail.monthDay}}13')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo14">
            {{#  if(d.data["14"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['14'].detail.monthDay}}14" onclick="showTime('{{d.data["14"].detail.workStartTime}}<br>{{d.data["14"].detail.workEndTime}}','{{d.employeeId}}{{d.data["14"].detail.monthDay}}14')">{{d.data["14"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['14'].detail.monthDay}}14" onclick="showTime('无','{{d.employeeId}}{{d.data["14"].detail.monthDay}}14')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo15">
            {{#  if(d.data["15"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['15'].detail.monthDay}}15" onclick="showTime('{{d.data["15"].detail.workStartTime}}<br>{{d.data["15"].detail.workEndTime}}','{{d.employeeId}}{{d.data["15"].detail.monthDay}}15')">{{d.data["15"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['15'].detail.monthDay}}15" onclick="showTime('无','{{d.employeeId}}{{d.data["15"].detail.monthDay}}15')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo16">
            {{#  if(d.data["16"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['16'].detail.monthDay}}16" onclick="showTime('{{d.data["16"].detail.workStartTime}}<br>{{d.data["16"].detail.workEndTime}}','{{d.employeeId}}{{d.data["16"].detail.monthDay}}16')">{{d.data["16"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['16'].detail.monthDay}}16" onclick="showTime('无','{{d.employeeId}}{{d.data["16"].detail.monthDay}}16')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo17">
            {{#  if(d.data["17"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['17'].detail.monthDay}}17" onclick="showTime('{{d.data["17"].detail.workStartTime}}<br>{{d.data["17"].detail.workEndTime}}','{{d.employeeId}}{{d.data["17"].detail.monthDay}}17')">{{d.data["17"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['17'].detail.monthDay}}17" onclick="showTime('无','{{d.employeeId}}{{d.data["17"].detail.monthDay}}17')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo18">
            {{#  if(d.data["18"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['18'].detail.monthDay}}18" onclick="showTime('{{d.data["18"].detail.workStartTime}}<br>{{d.data["18"].detail.workEndTime}}','{{d.employeeId}}{{d.data["18"].detail.monthDay}}18')">{{d.data["18"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['18'].detail.monthDay}}18" onclick="showTime('无','{{d.employeeId}}{{d.data["18"].detail.monthDay}}18')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo19">
            {{#  if(d.data["19"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['19'].detail.monthDay}}19" onclick="showTime('{{d.data["19"].detail.workStartTime}}<br>{{d.data["19"].detail.workEndTime}}','{{d.employeeId}}{{d.data["19"].detail.monthDay}}19')">{{d.data["19"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['19'].detail.monthDay}}19" onclick="showTime('无','{{d.employeeId}}{{d.data["19"].detail.monthDay}}19')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo20">
            {{#  if(d.data["20"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['20'].detail.monthDay}}20" onclick="showTime('{{d.data["20"].detail.workStartTime}}<br>{{d.data["20"].detail.workEndTime}}','{{d.employeeId}}{{d.data["20"].detail.monthDay}}20')">{{d.data["20"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['20'].detail.monthDay}}20" onclick="showTime('无','{{d.employeeId}}{{d.data["20"].detail.monthDay}}20')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo21">
            {{#  if(d.data["21"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['21'].detail.monthDay}}21" onclick="showTime('{{d.data["21"].detail.workStartTime}}<br>{{d.data["21"].detail.workEndTime}}','{{d.employeeId}}{{d.data["21"].detail.monthDay}}21')">{{d.data["21"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['21'].detail.monthDay}}21" onclick="showTime('无','{{d.employeeId}}{{d.data["21"].detail.monthDay}}21')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo22">
            {{#  if(d.data["22"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['22'].detail.monthDay}}22" onclick="showTime('{{d.data["22"].detail.workStartTime}}<br>{{d.data["22"].detail.workEndTime}}','{{d.employeeId}}{{d.data["22"].detail.monthDay}}22')">{{d.data["22"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['22'].detail.monthDay}}22" onclick="showTime('无','{{d.employeeId}}{{d.data["22"].detail.monthDay}}22')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo23">
            {{#  if(d.data["23"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['23'].detail.monthDay}}23" onclick="showTime('{{d.data["23"].detail.workStartTime}}<br>{{d.data["23"].detail.workEndTime}}','{{d.employeeId}}{{d.data["23"].detail.monthDay}}23')">{{d.data["23"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['23'].detail.monthDay}}23" onclick="showTime('无','{{d.employeeId}}{{d.data["23"].detail.monthDay}}23')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo24">
            {{#  if(d.data["24"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['24'].detail.monthDay}}24" onclick="showTime('{{d.data["24"].detail.workStartTime}}<br>{{d.data["24"].detail.workEndTime}}','{{d.employeeId}}{{d.data["24"].detail.monthDay}}24')">{{d.data["24"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['24'].detail.monthDay}}24" onclick="showTime('无','{{d.employeeId}}{{d.data["24"].detail.monthDay}}24')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo25">
            {{#  if(d.data["25"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['25'].detail.monthDay}}25" onclick="showTime('{{d.data["25"].detail.workStartTime}}<br>{{d.data["25"].detail.workEndTime}}','{{d.employeeId}}{{d.data["25"].detail.monthDay}}25')">{{d.data["25"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['25'].detail.monthDay}}25" onclick="showTime('无','{{d.employeeId}}{{d.data["25"].detail.monthDay}}25')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo26">
            {{#  if(d.data["26"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['26'].detail.monthDay}}26" onclick="showTime('{{d.data["26"].detail.workStartTime}}<br>{{d.data["26"].detail.workEndTime}}','{{d.employeeId}}{{d.data["26"].detail.monthDay}}26')">{{d.data["26"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['26'].detail.monthDay}}26" onclick="showTime('无','{{d.employeeId}}{{d.data["26"].detail.monthDay}}26')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo27">
            {{#  if(d.data["27"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['27'].detail.monthDay}}27" onclick="showTime('{{d.data["27"].detail.workStartTime}}<br>{{d.data["27"].detail.workEndTime}}','{{d.employeeId}}{{d.data["27"].detail.monthDay}}27')">{{d.data["27"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['27'].detail.monthDay}}27" onclick="showTime('无','{{d.employeeId}}{{d.data["27"].detail.monthDay}}27')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo28">
            {{#  if(d.data["28"].detail.workIngHour){ }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['28'].detail.monthDay}}28" onclick="showTime('{{d.data["28"].detail.workStartTime}}<br>{{d.data["28"].detail.workEndTime}}','{{d.employeeId}}{{d.data["28"].detail.monthDay}}28')">{{d.data["28"].detail.workIngHour}}</span>
            {{#  } else  { }}
            <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['28'].detail.monthDay}}28" onclick="showTime('无','{{d.employeeId}}{{d.data["28"].detail.monthDay}}28')">0</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo29">
            {{#  if(d.data[29]){ }}
                {{#  if(d.data["29"].detail.workIngHour){ }}
                <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['29'].detail.monthDay}}29" onclick="showTime('{{d.data["29"].detail.workStartTime}}<br>{{d.data["29"].detail.workEndTime}}','{{d.employeeId}}{{d.data["29"].detail.monthDay}}29')">{{d.data["29"].detail.workIngHour}}</span>
                {{#  } else  { }}
                <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['29'].detail.monthDay}}29" onclick="showTime('无','{{d.employeeId}}{{d.data["29"].detail.monthDay}}29')">0</span>
                {{#  } }}
            {{#  } else  { }}
            <span>/</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo30">
            {{#  if(d.data[30]){ }}
                {{#  if(d.data["30"].detail.workIngHour){ }}
                <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['30'].detail.monthDay}}30" onclick="showTime('{{d.data["30"].detail.workStartTime}}<br>{{d.data["30"].detail.workEndTime}}','{{d.employeeId}}{{d.data["30"].detail.monthDay}}30')">{{d.data["30"].detail.workIngHour}}</span>
                {{#  } else  { }}
                <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['30'].detail.monthDay}}30" onclick="showTime('无','{{d.employeeId}}{{d.data["30"].detail.monthDay}}30')">0</span>
                {{#  } }}
            {{#  } else  { }}
            <span>/</span>
            {{#  } }}
        </script>
        <script type="text/html" id="barDemo31">
            {{#  if(d.data[31]){ }}
                {{#  if(d.data["31"].detail.workIngHour){ }}
                <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['31'].detail.monthDay}}31" onclick="showTime('{{d.data["31"].detail.workStartTime}}<br>{{d.data["31"].detail.workEndTime}}','{{d.employeeId}}{{d.data["31"].detail.monthDay}}31')">{{d.data["31"].detail.workIngHour}}</span>
                {{#  } else  { }}
                <span style="width: 100%;display: inline-block;" id="{{d.employeeId}}{{d.data['31'].detail.monthDay}}31" onclick="showTime('无','{{d.employeeId}}{{d.data["31"].detail.monthDay}}31')">0</span>
                {{#  } }}
            {{#  } else  { }}
            <span>/</span>
            {{#  } }}
        </script>
    </div>
    <div class="loading">
        <div style="width: 50px;margin: 0 auto;">
            <i class="layui-icon layui-icon-loading" style="font-size: 60px; color: #fff;"></i>
        </div>
    </div>
</div>
</body>
</html>
