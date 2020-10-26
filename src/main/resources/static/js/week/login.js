function loginPage(){
    var path = "";
    $.ajax({
        type: "GET",
        url: path+"/loginPage",
        dataType: "json",
        data: {userNumber:$("#UserNumber").val(), password:$("#Password").val()},
        success: function(data){

        }
    });
}
