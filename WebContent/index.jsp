<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>宝马模拟器</title>
<!-- 导入jquery -->
<script src="${base}/rs/js/jquery.js"></script>

    <link href="//cdn.bootcss.com/tether/1.3.6/css/tether.min.css" rel="stylesheet">
    <script src="//cdn.bootcss.com/tether/1.3.6/js/tether.min.js"></script>
<script src="${base}/rs/js/bootstrap.min.js"></script>
<script src="${base}/rs/js/underscore.js"></script>
<!-- Bootstrap core CSS -->
<link href="${base}/rs/css/bootstrap.min.css" rel="stylesheet">
    <link href="${base}/rs/exp/signin/signin.css" rel="stylesheet">
<!-- 把user id复制到一个js变量 -->
<script type="text/javascript">
    var me = '<%=session.getAttribute("me") %>';
    var base = '${base}';
    $(function() {
        $("#login_button").click(function() {
            $.ajax({
                url : base + "/user/login",
                type: "POST",
                data:$('#loginForm').serialize(),
                error: function(request) {
                    alert("Connection error");
                },
                dataType:"json",
                success: function(data) {
                    if (data == true) {
                        window.location.href = '${base}/user/main';
                    } else {
                        alert("登陆失败,请检查账号密码")
                    }
                }
            });
            return false;
        });
        if (me != "null") {
            $("#login_div").hide();
            $("#userInfo").html("您的Id是" + me);
            $("#user_info_div").show();
        } else {
            $("#login_div").show();
            $("#user_info_div").hide();
        }
    });
</script>


</head>



    <!-- Bootstrap core CSS -->

    <!-- Custom styles for this template -->







<body>

    <div class="container">

      <form class="form-signin" id="loginForm">
        <h2 class="form-signin-heading">请管理员登陆</h2>
        <label for="inputEmail" class="sr-only">用户名</label>
        <input type="text" name="username" id="username" value="" class="form-control" placeholder="用户名" required autofocus>

        <label for="inputPassword" class="sr-only">密码</label>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>

        <button class="btn btn-lg btn-primary btn-block" id="login_button" type="submit">登录</button>
      </form>

    </div> <!-- /container -->
</body>
</html>