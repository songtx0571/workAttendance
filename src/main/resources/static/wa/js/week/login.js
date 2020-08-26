function loginPage(){
    $.ajax({
        type: "GET",
        url: "/loginPage",
        data: {UserName:$("#UserName").val(), Password:$("#Password").val()},
        dataType: "json",
        async: true,
        success: function(data){
            $('#UserName').empty();//清空UserName里面的所有内容
            $('#Password').empty();//清空Password里面的所有内容
        }
    });
}

