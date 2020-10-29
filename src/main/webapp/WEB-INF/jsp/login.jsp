<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="js/easyui/themes/icon.css">
    <script type="text/javascript" src="js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="js/easyui/jquery.easyui.min.js"></script>
    <style>
        body {
            margin: 0;
            width: 100%;
            height: 100%;
        }

        .whole {
            width: 100%;
            height: 971px;
            background: url("img/loginA.png") no-repeat;
            backgroud-size: 1920px 971px;
        }

        .white_content {
            position: absolute;
            top: 232px;
            left: 735px;
            width: 430px;
            height: 430px;
            background-color: white;
            z-index: 1001;
            border-radius: 4px;
            padding: 20px;
        }

        * {
            padding: 0px;
            margin: 0px;
        }

        #tab {
            width: 320px;
            padding: 5px;
            height: 250px;
            margin: 100px 50px;
            text-align: center;
        }

        #tab ul {
            list-style: none;
            height: 30px;
            line-height: 30px;
            margin: 0 auto;
            margin-left: 10px;
        }

        #tab ul li {
            background: #FFF;
            cursor: pointer;
            float: left;
            list-style: none;
            height: 29px;
            line-height: 29px;
            padding: 0px 30px;
            border-bottom: 2px solid #E6E6E6;
            text-align: center;
            font-size: 18px;
            marigin: 20px;
            font-family: "华文黑体";
        }

        #tab ul li.on {
            border-bottom: 2px solid #4A4A4A;
        }

        #tab div {
            height: 400px;
            width: 300px;
            line-height: 24px;
            border-top: none;
            padding: 10px;
        }

        #loginlist {
            line-height: 70px;
            font-family: "华文黑体";
            font-size: 24px;
        }

        .set {
            background: white;
            border-radius: 4px;
            width: 200px;
            height: 40px;
            font-family: "华文黑体";
            font-size: 18px;
            color: #9B9B9B;
            text-align: left;
            outline: none;
            padding: 0 10px 0 10px;
            border: 1px #44ACFF solid;
            margin-left: 10px;
        }
        input:focus {
            -webkit-box-shadow: 0 0 5px #ccc; /*点击input 外阴影*/
            -moz-box-shadow: 0 0 5px #ccc;
            box-shadow: 0 0 8px #44ACFF;
            outline: 0; /*去掉默认谷歌点击input边框显示蓝色  */
            background: #fff; /*input内背景为白色*/
        }

        .button {
            display: inline-block;
            background: #44ACFF;
            border: 1px solid #44ACFF;
            border-radius: 4px;
            color: white;
            width: 300px;
            height: 40px;
            font-size: 18px;
            outline: none;
            cursor: pointer;
        }

        .button:active {
            background-color: #E27635;
            border-color: #E27635;
        }
    </style>
    <title>浩维管理平台</title>
</head>
<body leftmargin=0 topmargin=0>
<div class="whole">
    <div class="white_content">
        <div
                style="float: left; width: 34%; text-align: right; height: 70px;">
            <img src="img/logo.png" width="64px" height="64px" />
        </div>
        <div style="width: 65%; float: right; text-align: left; color: #4A4A4A;">
            <a style="line-height: 70px; font-size: 24px;">浩维管理平台</a>
        </div>
        <div id="tab">
            <div id="secondPage" class="show">
                <form name="register2" id="loginlist" action="/loginPage" method="post">
                    账号:<input type="text" name="userNumber" placeholder="请输入账号"
                              id="userNumber" class="set"  onfocus="this.placeholder=''"
                              onblur="this.placeholder='请输入账号'"><br>
                    密码:<input
                        type="password" name="password" placeholder="请输入密码" id="password"
                        class="set"  onfocus="this.placeholder=''"
                        onblur="this.placeholder='请输入密码'"><br>
                    <input type="submit" value="登录" class="button" style="text-align: center;" id="button02">
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
