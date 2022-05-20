/*
				表单校验：
					1.用户名：单词字符，长度8到20位
					2.密码：单词字符，长度8到20位
					3.email：邮箱格式
					4.姓名：非空
					5.手机号：手机号格式
					6.出生日期：非空
					7.验证码：非空
			 */

//校验用户名
//单词字符，长度7到20位
function checkUsername() {
    //获取用户名的id
    var username = $("#username");
    //获取用户名的值
    var username_val = username.val();
    //定义正则
    var reg_username = /^\w{7,20}$/;
    //判断，给出提示信息
    var flag = reg_username.test(username_val);
    if (flag){
        //用户名合法
        username.css("border","");
    }else {
        //用户名非法，加一个红色边框
        username.css("border","1px solid red");
    }
    return flag;
}

//校验密码
//单词字符，长度8到20位
function checkPassword() {
    //获取密码的id
    var password = $("#password");
    //获取密码的值
    var password_val = password.val();
    //定义正则
    var reg_password = /^\w{8,20}$/;
    //判断，给出提示信息
    var flag = reg_password.test(password_val);
    if (flag){
        //密码合法
        password.css("border","");
    }else {
        //密码非法
        password.css("border","1px solid red");
    }
    return flag;
}

//校验邮箱
//邮箱格式
function checkEmail(){
    //获取邮箱的id
    var email = $("#email");
    //获取邮箱的值
    var email_val = email.val();
    //定义正则  si6x@qq.com
    var reg_email = /^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;
    //判断，给出提示信息
    var flag = reg_email.test(email_val);
    if (flag){
        //邮箱合法
        email.css("border","");
    }else {
        //邮箱非法
        email.css("border","1px solid red");
    }
    return flag;
}

//校验姓名
//非空
function checkName(){
    //获取名字的id
    var name = $("#name");
    //获取名字的值
    var name_val = name.val();
    //定义正则
    var reg_name = /^[\s\S]*.*[^\s][\s\S]*$/;
    //判断，给出提示信息
    var flag = reg_name.test(name_val);
    if (flag){
        //姓名合法
        name.css("border","");
    }else {
        //姓名非法
        name.css("border","1px solid red");
    }
    return flag;
}

//校验手机号
//手机号格式
function checkTel(){
    //获取手机号的id
    var telephone = $("#telephone");
    //获取手机号的值
    var tel_val = telephone.val();
    //定义正则
    var reg_tel = /^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/;
    //判断，给出提示信息
    var flag = reg_tel.test(tel_val);
    if (flag){
        //手机号合法
        telephone.css("border","");
    }else {
        //手机号非法
        telephone.css("border","1px solid red");
    }
    return flag;
}

//校验出生日期
//非空
function checkBirthday() {
    //获取出生日期的id
    var birthday = $("#birthday");
    //获取出生日期的值
    var birthday_val = birthday.val();
    //定义正则
    var reg_birthday = /^[\s\S]*.*[^\s][\s\S]*$/;
    //判断，给出提示信息
    var flag = reg_birthday.test(birthday_val);
    if (flag){
        //日期合法
        birthday.css("border","");
    }else {
        //日期非法
        birthday.css("border","1px solid red");
    }
    return flag;
}

//校验验证码
//非空
function checkCode() {
    //获取验证码的id
    var check = $("#check");
    //获取用户输入验证码的值
    var check_val = check.val();
    //定义正则
    var reg_check = /^[\s\S]*.*[^\s][\s\S]*$/;
    //判断，给出提示信息
    var flag = reg_check.test(check_val);
    if (flag){
        //验证码合法
        check.css("border","");
    }else {
        //验证码非法
        check.css("border","1px solid red");
    }
    return flag;
}

$(function () {
    //当表单提交时，调用所有校验方法
    $("#registerForm").submit(function () {
        //1.发送数据到服务器
        if (checkUsername() && checkPassword() && checkEmail() && checkName() && checkTel() && checkBirthday() && checkCode()){
            //校验通过，发送ajax请求，提交表单的数据
            $.post("user/regist",$(this).serialize(),function (data) {
                //处理服务器响应的数据 data {flag:true,errorMsg:"注册失败"}
                if (data.flag){
                    //注册成功，跳转成功页面
                    location.href = "register_ok.html";
                }else {
                    //注册失败,给errorMsg添加提示信息
                    $("#errorMsg").html(data.errorMsg);
                    //刷新一次验证码
                    $("#checkCode").prop("src","checkCode?"+new Date().getTime());
                }
            });
        }

        //2.跳转页面
        return false;
        //如果这个方法没有返回值，或者返回为true,则表单提交，如果返回为false，则表单不提交
    });
    //当某一个组件是去焦点时，调用对应的校验方法
    $("#username").blur(checkUsername);
    $("#password").blur(checkPassword);
    $("#email").blur(checkEmail);
    $("#name").blur(checkName);
    $("#telephone").blur(checkTel);
    $("#birthday").blur(checkBirthday);
    $("#check").blur(checkCode);

    //当用户敲下回车则执行登录
    $(document).keydown(function (e) {
        if(e.which === 13){
            $("#register_submit").submit();
        }
    });
});
