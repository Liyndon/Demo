<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"  href="${ctx}/css/login.css"/>
<SCRIPT src="${ctx}/js/lib/jquery/jquery-1.9.1.min.js" type="text/javascript"></SCRIPT>
<SCRIPT src="${ctx}/js/lib/security/sha256.js" type="text/javascript"></SCRIPT>
<SCRIPT src="${ctx}/js/adminLogin.js" type="text/javascript"></SCRIPT>
<link rel="icon" href="${ctx}/image/favicon.ico" type="image/x-icon" /> 
<link rel="shortcut icon" href="${ctx}/image/favicon.ico" type="image/x-icon" />
<title>后台管理登录</title>
</head>
<script type="text/javascript">
	var ctx = "${ctx}";
	$(function(){
		Login.init();
	});
</script>
</head>
<body>
	<form id="loginForm" action="${ctx}/login" method="post">
		<div class="top_div"></div>
		<div
			style="background: rgb(255, 255, 255); border-radius: 10px; margin: -100px auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 250px; text-align: center;">
			<div style="width: 165px; height: 96px; position: absolute;">
				<div class="tou"></div>
				<div class="initial_left_hand" id="left_hand"></div>
				<div class="initial_right_hand" id="right_hand"></div>
			</div>
			<p style="padding: 30px 0px 10px; position: relative;">
				<span class="u_logo"></span> <input class="ipt" id="username" type="text" name="userName" placeholder="请输入用户名或邮箱" value="">
			</p>
			<p style="padding: 0px 0px 10px; position: relative;">
				<span class="p_logo"></span> <input class="ipt" id="password" type="password" placeholder="请输入密码" value="">
				<input type="hidden" name="userPassword"/>
			</p>
			<p style="position: relative;">
				<span class="c_logo"></span> <input class="iptx" maxlength="5" id="vcode" name="vcode" type="text" placeholder="请输入验证码" value="">
				<img class="validate_code" onclick="Login.reloadImage(this,'${ctx}/checkcode?width=140&height=36')" alt="验证码加载中" title="看不清点击刷新" 
				src="${ctx}/checkcode?id=1&width=140&height=36">
			</p>
			<div style="clear:both;" class="login_tip">&nbsp;<span id="error-tip">${errorTip}</span></span></div>
			<div
				style="height: 50px; line-height: 50px; margin-top: 10px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
				<p style="margin: 0px 35px 20px 45px;">
					<span style="float: left;">
						<a class="d-font" href="#">忘记密码?</a>
					</span> 
					<span style="float: right;">
						<a class="d-font" style="margin-right: 10px;" href="#">注册</a>
						<a class="login_btn" href="#">登录</a> 
					</span>
				</p>
			</div>
		</div>
	</form>
	<div style="text-align: center;">
		<p class="copyright">
			<a style="color:#2054B2" href="#"></a>&nbsp;&nbsp;&nbsp;后台管理系统
		</p>
	</div>
</body>
</html>