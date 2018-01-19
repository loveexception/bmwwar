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

<title>topic</title>
<!-- Bootstrap core JavaScript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="${base}/rs/js/jquery.js"></script>

    <link href="//cdn.bootcss.com/tether/1.3.6/css/tether.min.css" rel="stylesheet">
    <script src="//cdn.bootcss.com/tether/1.3.6/js/tether.min.js"></script>
<script src="${base}/rs/js/bootstrap.min.js"></script>
<script src="${base}/rs/js/underscore.js"></script>
<!-- Bootstrap core CSS -->
<link href="${base}/rs/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript">

    var pageNumber = 1;
    var pageSize = 10;
    var base = '<%=request.getAttribute("base")%>';
    var me = '<%=session.getAttribute("me")%>';
	function user_reload(cache) {

		if (me != "null") {
			$("#login_div").hide();
			$("#userInfo").html("您的Id是" + me);
			$("#user_info_div").show();
		}
		var myurl = base + "/topic/query" ;
		if(cache){
			myurl = base + "/topic/spider";
		}
		$.ajax({
			url : myurl,
			data : $("#user_query_form").serialize(),
			dataType : "json",
			type: "POST",
			success : function(data) {
				console.log(data);
				$("#totalcount").text(data.pager.recordCount);
				$("#totalpage").text(data.pager.pageCount);
				
				var tpl = _.template($("#tpl").text());

				$("#user_list tbody").html(tpl(data.list));
				$("#user_list tbody td").click(function(){
					var key = $(this).attr("key");
					if(!key){
						return;
					}
					
					
					var plan = prompt("请输入计划目标");
					var col = $(this).attr("col");
					var data = {"id":key};
					data[col] = plan;

		 			$.ajax({
						url :base + "/topic/update",
						data:data,
						dataType:"json",
						success:function(data){
							console.log(data);
							if(data.ok){
								user_reload();
								alert();
								$(".error").text("添加成功")
							}else{
								alert(data.msg);
							}
						}
					}) ;
			

				});
			}
		});
	}
	$(function() {
		user_reload();
		$("#user_query_btn").click(function() {
			user_reload();
			return false;
		});
		$("#user_query2_btn").click(function() {
			user_reload("cache");
			return false;
		});


	});


	function user_update(userId,sort) {
		if (confirm()) {
			$.ajax({
				url : base + "/topic/update",
				data : {
					"id" : userId,
					"sort" : sort
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
						href="${base}/user/main">管理员 
					</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${base}/person/main">用户</a></li>
					<li class="nav-item"><a class="nav-link " 
						href="${base}/topic/main">帖子</a></li>
					<li class="nav-item"><a class="nav-link active" 
						href="${base}/topic/newone">新手帖子</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${base}/plan/main">计划</a></li>
				</ul>

			</nav>

			<main class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 pt-3">
			<h1>帖子</h1>
			<p>
			<form class="form-inline mt-2 mt-md-0" action="#"
				id="user_query_form">
				条件<input type="text" class="form-control mr-sm-2" name="name">
				页数<input type="text" name="pageNumber" value="1">
				共<span id="totalpage"></span>页 <span id="totalcount"></span>条
				<!--每页 -->
				<input type="hidden" name="pageSize" value="100">
				<input type="hidden" name="type" value="1">
				<button class="btn btn-outline-success my-2 my-sm-0">
					<span id="user_query_btn" class="md-button-label">查询</span>
				</button>
				<button class="btn btn-outline-success my-2 my-sm-0">
					<span id="user_query2_btn" class="md-button-label">抓取</span>
				</button>
			</form>
			</p>
			<h2>列表</h2>
			<div class="error">  </div>
			<div class="table-responsive">
				<table class="table table-striped" id="user_list">
					<thead>
						<tr>
							<th>#</th>
							<th>标题</th>
							<th>阅读</th>
							<th>目标阅读</th>
							<th>点赞</th>
							<th>目标点赞</th>
							<th>收藏</th>
							<th>目标收藏</th>
							<th>回贴</th>
							<th>置顶</th>
						</tr>
					</thead>
					<tbody>

					</tbody>
				</table>
			<script type="text/template" id="tpl">
		        {{_.each(obj,function(topic){}}
		            <tr > 
		                <td> {{=topic.id }} </td>
		                <td> <a href="https://www.mybmwclub.cn/forum.php?mod=viewthread&tid={{=topic.oldid}}" target="_blank">{{=topic.name }}</a> </td>
		                <td> {{=topic.readed }} </td>
		                <td key="{{=topic.id}}" col="readedplan"> {{=topic.readedplan }} </td>
		                <td> {{=topic.liked }} </td>
		                <td key="{{=topic.id}}" col="likedplan"> {{=topic.likedplan }} </td>
		                <td > {{=topic.collected }} </td>
		                <td key="{{=topic.id}}" col="collectedplan"> {{=topic.collectedplan }} </td>
		                <td><a href="${base}/subtopic/main?tid={{=topic.id}}">回贴{{=topic.replies}}</a></td>
                		<td>
							{{ if(topic.sort>1){ }} 
							<button onclick='user_update({{=topic.id }},0);'>
								 取消置顶 
							</button>
							{{ }else{ }}
							<button onclick='user_update({{=topic.id}},10000);'>
								 置顶 
							</button>
							{{ } }}	
						</td>
		            </tr>
		        {{})}}
		    </script>
			</div>
			</main>
		</div>

	

	</div>

</body>
</html>
