$(function () {
    //当用户点击登录时执行
    $("#btn_sub").click(function () {
        login();
    });

    //当用户在用户名输入框敲下回车则执行登录
    $("#username").keydown(function (e) {
        var username_val = $("#username").val();
        if(e.which === 13 && username_val.length > 0){
            login();
        }
    });

    //当用户在密码输入框敲下回车则执行登录
    $("#password").keydown(function (e) {
        var password_val = $("#password").val();
        if(e.which === 13 && password_val.length > 0){
            login();
        }
    });

    //当用户在验证码输入框敲下回车则执行登录
    $("#check").keydown(function (e) {
        var check_val = $("#check").val();
        if(e.which === 13 && check_val.length > 0){
            login();
        }
    });
});

function login() {
    //给登录按钮绑定单击事件
    //发送ajax请求,提交表单数据
    $.post("user/login",$("#loginForm").serialize(),function (data) {
        //data : {flag:true,erroeMsg:''}
        //处理响应结果
        if (data.flag){
            //登录成功,跳转主页
            location.href = "index.html";
        }else {
            //登录失败，显示错误信息
            $("#errorMsg").html(data.errorMsg);
            //刷新一次验证码
            // document.getElementById("checkCode").src = "checkCode?"+new Date().getTime();
            $("#checkCode").prop("src","checkCode?"+new Date().getTime());
        }
    });
}
