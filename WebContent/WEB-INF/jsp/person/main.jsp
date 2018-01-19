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

<title>person</title>
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
<link href="${base}/rs/css/bootstrap.min.css" rel="stylesheet">
<link href="${base}/rs/css/bootstrap-datetimepicker.css" rel="stylesheet" />

<script type="text/javascript">

    var pageNumber = 1;
    var pageSize = 10;
    var base = '<%=request.getAttribute("base")%>';
    var me = '<%=session.getAttribute("me")%>';
	function user_reload() {

		if (me != "null") {
			$("#login_div").hide();
			$("#userInfo").html("您的Id是" + me);
			$("#user_info_div").show();
		}
		
		var show = $("[name=today]").val();

		$.ajax({
			url : base + "/person/query",
			data : $("#user_query_form").serialize(),
			dataType : "json",
			success : function(data) {
				console.log(data);

				var list_html = "";
				$("#totalcount").text(data.pager.recordCount);
				$("#totalpage").text(data.pager.pageCount);
				
				
				console.log(data.list);
				var datalist = _(data.list).chain()
				.map(function(item){
				    var today = item.today;
				    if(!today||today.id<1){
				    	item.today.style = "";
				    	item.today.text = "本日无计划";
				    }else if (today.plan&&today.send) {
				    	item.today.style = "bg-success";
				    	item.today.text = today.send;
				    }else  {
				    	item.today.style = "bg-info";
				    	item.today.text = today.plan;
				    }
				    return item;
				}).map(function(item){
				    var tomorrow = item.tomorrow;
				    if(!tomorrow||tomorrow.id<1){
				    	item.tomorrow.style = "";
				    	item.tomorrow.text = "未计划";
				    }else if (tomorrow.plan&&tomorrow.send&&tomorrow.stauts==9) {
				    	item.tomorrow.style = "bg-success";
				    	item.tomorrow.text = tomorrow.send;
				    }else if (tomorrow.plan&&tomorrow.stauts==3) {
				    	item.tomorrow.style = "bg-danger";
				    	item.tomorrow.text = tomorrow.plan;
				    }else  {
				    	item.tomorrow.style = "bg-info";
				    	item.tomorrow.text = tomorrow.plan;
				    }
				    return item;
				}).map(function(item){
					temp = _(8).chain().times(function(item){
						return item;
						}).map(function(item){
							return ['id','pid','style','text','uid']; 
						}).map(function(item){ 
							return _.object(item,[0,0,'','',0])
						}).value();
					item.t = _(temp).chain().groupBy(function(item,key){
						var day =new Date()
						day.setTime(day.getTime()+ (key*24*60*60*1000));
						return day.Format('MM-dd');
					}).value()
					return item;					
				}).map(function(item){
					item.weekhtml='<td class="temp hide" user="'+item.id+'"></td>';
					return item;
				}).value();
				var tpl = _.template($("#tpl").text());
				var week = _(8).chain().times(function(item){
					var day =new Date()
					day.setTime(day.getTime()+ (item *24*60*60*1000));
					var str = day.Format('MM-dd');
					$("th[tomorrow="+item+"]").attr("time",str).text(str).css("font-size","10px");
					return str;
				}).value();
				

				$("#user_list tbody").html(tpl(data.list));
				$("#user_list tbody td.temp").hide();
				_(week).chain().rest().each(function(item){
					$("#user_list tbody td.temp").each(function(){
						$(this).before($("<td />").attr("time",item).attr("user",$(this).attr("user")).text("未计划"))
					})
					return item;
				}).value();
				
				
				_(data.list).each(function(item){
					var user = item.id;
					_(item.week).each(function(value,key,list){
						var day = new Date(value.plan).Format('MM-dd');

					    
						$('td[user='+user+'][time='+day+']')
						.eq(0)
						.text(value.plan)
						.addClass("bg-info")
						.attr('key',value.id);
						
					});
				});
				
				$("#user_list tbody td").click(function(){
					var key = $(this).attr("key");
					if(!key){
						return false;
					}
					
					
					if(key==0){
						var plan = prompt("请输入发布 时间 （0-24） ");
						var user = $(this).attr("user");
						if(plan){
							  var today=new Date();     
							    M=Number(today.getMonth())+1;
							    var oneday = parseInt($(this).attr("tomorrow"));
							    oneday +=today.getDate();
							    plan = today.getFullYear()+"-"+M+"-"+oneday+" "+plan+":00:00";
							$.ajax({
								url :base + "/login/add",
								data:{"pid":user,"plan":plan},
								dataType:"json",
								success:function(data){
									console.log(data);
									if(data.ok){
										user_reload();
										alert("添加成功");
									}else{
										alert(data.msg);
									}
								}
							}) ;
						}

			 			
					}
					if(key >0){
						
						if (confirm()) {
							$.ajax({
								url : base + "/login/delete",
								data : {
									"id" : key
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
					}
					return false;
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
		$("#user_add_btn").click(function() {
			$.ajax({
				url : base + "/person/add",
				data : $("#user_add_form").serialize(),
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
			return false;
		});
	});
	function user_update(userId) {
		var passwd = prompt("请输入新的密码");
		if (passwd) {
			$.ajax({
				url : base + "/person/update",
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
				url : base + "/person/delete",
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
		
				
	}

	$(function(){
		$("#rand_tomorrow_btn").click(function(){
			var today = new Date();
			 today.setDate(today.getDate());
			$.ajax({
					url : base + "/login/rand?today="+today.Format('YYYY-M-d'),
					dataType : "json",
					success : function(data) {
						if (data.ok) {
							user_reload();
							alert("计划成功");
						} else {
							alert(data.msg);
						}
					}
				});
				return false;
		});
		$("#today_btn").click(function(){
			if (confirm()) {
				$.ajax({
					url : base + "/login/cleartoday",
					dataType : "json",
					success : function(data) {
						if (data.ok) {
							user_reload();
							alert("清除计划成功");
						} else {
							alert(data.msg);
						}
					}
				});
			}
			return false;
		});
		$("#delete_history_btn").click(function(){
			if (confirm()) {
				$.ajax({
					url : base + "/login/clearhistory",
					dataType : "json",
					success : function(data) {
						if (data.ok) {
							user_reload();
							alert("清除计划成功");
						} else {
							alert(data.msg);
						}
					}
				});
			}
			return false;
		});
		$("#rand_week_btn").click(function(){
			if (confirm()) {
				$.ajax({
					url : base + "/login/randweek",
					dataType : "json",
					success : function(data) {
						if (data.ok) {
							user_reload();
							alert("七日计划成功");
						} else {
							alert(data.msg);
						}
					}
				});
			}
			return false;
		});
		$("#tomorrow_btn").click(function(){
			if (confirm()) {
				$.ajax({
					url : base + "/login/cleartomorrow",
					dataType : "json",
					success : function(data) {
						if (data.ok) {
							user_reload();
							alert("清除计划成功");
						} else {
							alert(data.msg);
						}
					}
				});
			}
			return false;
		});
	});
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
					<li class="nav-item"><a class="nav-link active"
						href="${base}/person/main">用户<span class="sr-only">(current)</span></a></li>
					<li class="nav-item"><a class="nav-link"
						href="${base}/topic/main">帖子</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${base}/plan/main">计划</a></li>
				</ul>

			</nav>

			<main class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 pt-3">
			<h1>用户</h1>
			<p>
			<form class="form-inline mt-2 mt-md-0" action="#"
				id="user_query_form">
						<select name="oldid" >
							<option value="">所有</option>
							<option value="1">非车主</option>
							<option value="2">车主</option>
						</select>
			    <div class="form-group hid">
			    
                 
                </div>
				条件<input type="text" class="form-control mr-sm-2" name="name">
				页数<input type="text" name="pageNumber" value="1"> 共<span id="totalpage"></span>页 <span id="totalcount"></span>条
				<!--每页 -->
				<input type="hidden" name="pageSize" value="100">
				<button id="user_query_btn"  class="btn btn-outline-success my-2 my-sm-0">
					<span class="md-button-label">查询</span>
				</button>

			</form>
			</p>
			<p>

				<button id="delete_history_btn" class="btn btn-outline-info my-2 my-sm-0">
						<span  class="md-button-label">清除未完成计划</span>
				</button>

				<button id="today_btn"  class="btn btn-outline-info my-2 my-sm-0">
						<span class="md-button-label">清除今日计划</span>
				</button>
				<button  id="tomorrow_btn"  class="btn btn-outline-primary my-2 my-sm-0">
						<span class="md-button-label">清除明日计划</span>
				</button>
				<button id="rand_tomorrow_btn" class="btn btn-outline-success my-2 my-sm-0">
						<span  class="md-button-label">明日计划</span>
				</button>
				<button id="rand_week_btn" class="btn btn-outline-info my-2 my-sm-0">
						<span  class="md-button-label">七日计划</span>
				</button>
				
			</p>
			<h2>列表</h2>
			<div class="table-responsive">
				<table class="table table-striped" id="user_list">
					<thead>
						<tr>
							<th>#</th>
							<th>用户名</th>
							<th>本日计划</th>
							<th tomorrow='1'>t+1计划</th>
							<th tomorrow='2'>t+2计划</th>
							<th tomorrow='3'>t+3计划</th>
							<th tomorrow='4'>t+4计划</th>
							<th tomorrow='5'>t+5计划</th>
							<th tomorrow='6'>t+6计划</th>
							<th tomorrow='7'>t+7计划</th>
							<th>方式</th>
							<th>修改密码</th>
							<th>删除</th>
						</tr>
					</thead>
					<tbody>

					</tbody>
				</table>
	<script type="text/template" id="tpl">
        {{_.each(obj,function(user){}}
            <tr> 
                <td> {{=user.id }} </td>
                <td> {{=user.name }} </td>
                <td user="{{=user.id}}" key="{{=user.today.id}}" class="{{=user.today.style}} " tomorrow="0"> {{=user.today.text }} </td>
				{{= user.weekhtml}}
				<td> {{=user.oldid }}  </td>
                <td><button onclick='user_update({{=user.id }});'>修改</button></td>
                <td><button onclick='user_delete({{=user.id }});'>删</button></td>
            </tr>
        {{})}}
    </script>
			</div>
			</main>
		</div>

		<div id="user_add" class="row">
			<nav class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 pt-3">


				<form class="form-inline mt-2 mt-md-0" action="#" id="user_add_form">
					<div class="form-group">
						<input type="text" name="name" placeholder="用户名">
					</div>
					<div class="form-group">
						<input type="text" name="password" onfocus="this.type='password';"
							placeholder="密码">
					</div>
					<div class="form-group">
						<input type="text" name="oldid" onfocus="this.type='oldid';"
							placeholder="oldid" value="2">
					</div>
					<!-- 
					<div class="form-group">
						<select name="oldid" >
							<option value="1">非车主</option>
							<option value="2">车主</option>
						</select>
					</div>
					 -->
				</form>
					<button class="btn btn-outline-success my-2 my-sm-0">
						<span id="user_add_btn" class="md-button-label">新增</span>
					</button>
			</nav>
		</div>

	</div>

</body>
</html>
<<script type="text/javascript">
<!--
Date.prototype.Format = function (formatStr) {
    var str = formatStr;
    var Week = ['日', '一', '二', '三', '四', '五', '六'];

    str = str.replace(/yyyy|YYYY/, this.getFullYear());
    str = str.replace(/yy|YY/, (this.getYear() % 100) > 9 ? (this.getYear() % 100).toString() : '0' + (this.getYear() % 100));
    var month = this.getMonth() + 1;
    str = str.replace(/MM/, month > 9 ? month.toString() : '0' + month);
    str = str.replace(/M/g, month);

    str = str.replace(/w|W/g, Week[this.getDay()]);

    str = str.replace(/dd|DD/, this.getDate() > 9 ? this.getDate().toString() : '0' + this.getDate());
    str = str.replace(/d|D/g, this.getDate());

    str = str.replace(/hh|HH/, this.getHours() > 9 ? this.getHours().toString() : '0' + this.getHours());
    str = str.replace(/h|H/g, this.getHours());
    str = str.replace(/mm/, this.getMinutes() > 9 ? this.getMinutes().toString() : '0' + this.getMinutes());
    str = str.replace(/m/g, this.getMinutes());

    str = str.replace(/ss|SS/, this.getSeconds() > 9 ? this.getSeconds().toString() : '0' + this.getSeconds());
    str = str.replace(/s|S/g, this.getSeconds());
    return str;
}
//-->
</script>
