<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
    content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="${base}/favicon.ico">

<title>subtopic</title>
<!-- Bootstrap core JavaScript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="${base}/rs/js/jquery.js"></script>

   <link href="//cdn.bootcss.com/tether/1.3.6/css/tether.min.css" rel="stylesheet">
    <script src="//cdn.bootcss.com/tether/1.3.6/js/tether.min.js"></script>
<script src="${base}/rs/js/bootstrap.min.js"></script>
<script src="${base}/rs/js/bootstrap-datetimepicker.js"></script>
<script src="${base}/rs/js/underscore.js"></script>
<!-- Bootstrap core CSS -->
<link href="${base}/rs/css/bootstrap.min.css" rel="stylesheet" />
<link href="${base}/rs/css/bootstrap-datetimepicker.css" rel="stylesheet" />
<script type="text/javascript">

    var pageNumber = 1;
    var pageSize = 10;
    var base = '<%=request.getAttribute("base")%>';
    var me = '<%=session.getAttribute("me")%>';
    var tid = '${obj}'
    function user_reload() {

        if (me != "null") {
            $("#login_div").hide();
            $("#userInfo").html("您的Id是" + me);
            $("#user_info_div").show();
        }
        //var tid = window.location.search.split("=")[1];
        $.ajax({
            url : base + "/subtopic/query",
            data : {"tid":tid},
            dataType : "json",
            success : function(data) {
                console.log(data);
                var list_html = "";
                console.log(data.list);
                _(data.list).chain().map(function(obj){
                    if(obj.status==1){
                        obj.type="计划中";
                    }else if(obj.status==9){
                        obj.type="已执行";
                    }else{
                        obj.type="执行失败";
                    }
                    return obj;
                }).map(function(obj){
                	if(obj.person){
                		obj.personname = obj.person.name;
                	}
                	return obj;
                }).value();
                var tpl = _.template($("#tpl").text());

                $("#user_list tbody").html(tpl(data.list));
            }
        });
        $.ajax({
            url : base + "/topic/get",
            data : {"id":tid},
            dataType : "json",
            success : function(data) {
                console.log(data);
                var name = $("<a>").html(data.name)
                .attr("href","https://www.mybmwclub.cn/forum.php"
                		+"?mod=viewthread&tid="
                		+data.oldid)
                .attr("target","_blank");
                $("#topic_name").html(name);
                $("#topic_content").html(data.content);
                $("#topic_type").val(data.type);

            }
        });
    }
    $(function() {
        user_reload();
        $("#user_query_btn").click(function() {
            user_reload();
            return false;
        });
        $("#user_add_btn").click(function() {
            $.ajax({
                url : base + "/subtopic/add",
                data : $("#user_add input").serialize(),
                type : "post",
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
            $("input[name=plan]").val("");
            $("input[name=context]").val("");
            $("input[name=name]").val("");
            return false;
        });
    });
    function user_update(userId) {
        var passwd = prompt("请输入新的密码");
        if (passwd) {
            $.ajax({
                url : base + "/subtopic/update",
                data : {
                    "id" : userId,
                    "password" : passwd
                },
                dataType : "json",
                success : function(data) {
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
        if (confirm()) {
            $.ajax({
                url : base + "/subtopic/delete",
                data : {
                    "id" : userId
                },
                dataType : "json",
                success : function(data) {
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
        interpolate : /\{\{\=(.+?)\}\}/g,
        evaluate : /\{\{(.+?)\}\}/g

    };
</script>

<!-- Custom styles for this template -->
<link href="${base}/rs/exp/dashboard/dashboard.css" rel="stylesheet">
</head>

<body>
    <nav
        class="navbar navbar-toggleable-md navbar-inverse fixed-top bg-inverse">
        <button class="navbar-toggler navbar-toggler-right hidden-lg-up"
            type="button" data-toggle="collapse"
            data-target="#navbarsExampleDefault"
            aria-controls="navbarsExampleDefault" aria-expanded="false"
            aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <a class="navbar-brand" href="${base}/user/main">主页</a>

        <div class="collapse navbar-collapse" id="navbarsExampleDefault">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active" id="login_div"></li>
                <li class="nav-item"></li>
            </ul>
            <a class="btn btn-outline-success my-2 my-sm-0"
                href="${base}/user/logout">退出</a>
            <!--

        -->
        </div>
    </nav>

    <div class="container-fluid">
        <div class="row">
            <nav class="col-sm-3 col-md-2 hidden-xs-down bg-faded sidebar">
                <ul class="nav nav-pills flex-column">
                    <li class="nav-item"><a class="nav-link "
                        href="${base}/user/main">管理员</a></li>
                    <li class="nav-item"><a class="nav-link"
                        href="${base}/person/main">用户</a></li>
                    <li class="nav-item"><a class="nav-link active"
                        href="${base}/topic/main">帖子</a></li>

                    <li class="nav-item"><a class="nav-link"
                        href="${base}/plan/main">计划</a></li>
                </ul>

            </nav>

            <main class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 pt-3">
            <h1 id="topic_name"></h1>
            <p id="topic_content"></p>

            <h2>列表</h2>
            <div class="table-responsive">
                <table class="table table-striped" id="user_list">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>内容</th>
                            <th>作者</th>
                            <th>时间</th>
                            <th>状态</th>
                            <th>删除</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                   <script type="text/template" id="tpl">
                            {{_.each(obj,function(sub){}}
                                <tr> 
                                    <td> {{=sub.id }} </td>
                                    <td> {{=sub.context }} </td>
                                    <td> {{=sub.personname }} </td>
                                    <td>{{=sub.plan}}</td>
                                    <td>{{=sub.type}}</td>
                                    <td><button onclick='user_delete({{=sub.id }});'>删</button></td>
                                </tr>
                            {{})}}
                        </script>
                
            </div>
            </main>
        </div>

        <div id="user_add" class="row" style="min-height: 500px;">
            <nav class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 pt-3">


                <div class="form-inline mt-2 mt-md-0" action="#" id="user_add_form">
                    <div class="form-group">
                        <input name="context" placeholder="回贴" />
                    </div>
                   <div class="form-group">
                        <input type="text" name="name" placeholder="用户名" />
                    </div>
                    <div class="form-group">
                    <input size="16" type="text" value="" name="plan" placeholder="计划发布时间" readonly class="form_datetime"  data-date-format="yyyy-mm-dd hh:ii" />
 
                    <script type="text/javascript">
                        $(".form_datetime").datetimepicker({format: 'yyyy-mm-dd hh:ii',autoclose: true});
                    </script>  
                    <input type="hidden" name="tid" value="${obj}">
                    <input type="hidden" name="type" id="topic_type">

                    </div>

                    <!--每页 -->

                    <button id="user_add_btn"  class="btn btn-outline-success my-2 my-sm-0">
                        <span  class="md-button-label">新增</span>
                    </button>
                </div>
            </nav>
        </div>

    </div>

</body>
</html>
