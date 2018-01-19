<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <script src="../rs/js/jquery.js"></script>
    <script src="../rs/js/underscore.js"></script>

    <!-- 当前页面-->
    <link rel="stylesheet" type="text/css" href="../rs/css/mdcss/md.css">


<title>用户列表</title>
<script type="text/javascript">

    var pageNumber = 1;
    var pageSize = 10;
    var base = '<%=request.getAttribute("base")%>';
    function user_reload() {
        var me = '<%=session.getAttribute("me") %>';

    	if (me != "null") {
            $("#login_div").hide();
            $("#userInfo").html("您的Id是" + me);
            $("#user_info_div").show();
        } 
    	
        $.ajax({
            url : base + "/user/query",
            data : $("#user_query_form").serialize(),
            dataType : "json",
            success : function(data) {
                console.log(data);
                $("#user_count").html("共"+data.pager.recordCount+"个用户, 总计"+data.pager.pageCount+"页");
                var list_html = "";
                console.log(data.list);
                var tpl =_.template($("#tpl").text());
                // for (var i=0;i<data.list.length;i++) {
                //     var user = data.list[i];
                //     console.log(user);
                //     var tmp = "\n<p>" + user.id + " " + user.name
                //               + " <button onclick='user_update(" + user.id +");'>修改</button> "
                //               + " <button onclick='user_delete(" + user.id +");'>删除</button> "
                //               + "</p>";
                //     list_html += tmp;
                // }
                $("#user_list").html(tpl(data.list));
            }
        });
    }
    $(function() {
        user_reload();
        $("#user_query_btn").click(function() {
            user_reload();
        });
        $("#user_add_btn").click(function() {
            $.ajax({
                url : base + "/user/add",
                data : $("#user_add_form").serialize(),
                dataType : "json",
                success : function(data) {
                    if (data.ok) {
                        user_reload();
                        alert("添加成功");
                    } else {
                        alert(data.msg);
                    }
                }
            });
        });
    });
    function user_update(userId) {
        var passwd = prompt("请输入新的密码");
        if (passwd) {
            $.ajax({
                url : base + "/user/update",
                data : {"id":userId,"password":passwd},
                dataType : "json",
                success : function (data) {
                    if (data.ok) {
                        user_reload();
                        alert("修改成功");
                    } else {
                        alert(data.msg);
                    }
                }
            });
        }
    };
    function user_delete(userId) {
        var s = prompt("请输入y确认删除");
        if (s == "y") {
            $.ajax({
                url : base + "/user/delete",
                data : {"id":userId},
                dataType : "json",
                success : function (data) {
                    if (data.ok) {
                        user_reload();
                        alert("删除成功");
                    } else {
                        alert(data.msg);
                    }
                }
            });
        }
    };

      _.templateSettings = {
       interpolate :  /\{\{\=(.+?)\}\}/g,
       evaluate: /\{\{(.+?)\}\}/g

     };
</script>
    <script type="text/template" id="tpl">
        {{_.each(obj,function(user){}}
            <tr> 
                <td> {{=user.id }} </td>
                <td> {{=user.name }} </td>
                <td><button onclick='user_update({{=user.id }});'>修改</button></td>
                <td><button onclick='user_delete({{=user.id }});'>删</button></td>
            </tr>
        {{})}}
    </script>
</head>
<body>

<!--主操作区-->
<div id="md-page-main">
<div class="row"  >
   

    <form action="#" id="user_query_form">
        条件<input type="text" class="form-control mr-sm-2" name="name">
        页数<input type="text" name="pageNumber" value="1">
        <!--每页 -->
        <input type="hidden" name="pageSize" value="100">
    </form>
    <button class="btn btn-outline-success my-2 my-sm-0">
            <span id="user_query_btn" class="md-button-label">查询</span>
    </button>
     
    <p>---------------------------------------------------------------</p>
    <p id="user_count"></p>
    <div class="row">
        <div class="paper z-depth-1 rounded md-table-container demo-table">
            <i class="md-table-title">用户列表</i>
            <table class="md-table" id="user_list">
                <tr>
                    <th>id</th>
                    <th>姓名</th>
                    <th>操作</th>
                    <th>操作</th>

                </tr>
            </table>
        </div>
    </div>
</div>
<div>
    <p>---------------------------------------------------------------</p>
</div>
<div class="row" id="user_add">
    <form action="#" id="user_add_form">

        <div class="md-text-field">
            <input type="text" name="name" nm="用户名">
            <hr class="underline">
            <hr class="underline-focus">
            <div class="err-tip">无效的用户名</div>
        </div>
        <div class="md-text-field has-float-label">
            <label>密码</label>
            <input type="text" name="pwd" onfocus="this.type='password';" nm="密码">
            <hr class="underline">
            <hr class="underline-focus">
            <div class="err-tip">无效的密码</div>
        </div>
    </form>
    <button class="btn btn-outline-success my-2 my-sm-0">
            <span id="user_add_btn" class="md-button-label">新增</span>
        </button>
</div>

<div id="user_info_div">
    <p id="userInfo"></p>
    <a href="${base}/user/logout">登出</a>
</div>
</div>
<div id="md-page-main-overlay">
</div>

</body>
</html>