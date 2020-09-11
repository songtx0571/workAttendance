function loginPage(){
    //var path = "http://192.168.1.26:8081";
    var path = "";
    $.ajax({
        type: "GET",
        url: path+"/loginPage",
        dataType: "json",
        data: {userNumber:$("#UserNumber").val(), password:$("#Password").val()},
        success: function(data){
            // location.href = data;
            // if (data != "") {
            //     location.href = data;
            // }else {
            //     alert("用户名或密码输入错误")
            // }
            // $('#UserNumber').empty();//清空UserNumber里面的所有内容
            // $('#Password').empty();//清空Password里面的所有内容
        }
    });
}