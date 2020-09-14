<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>

<link rel="icon" href="${ctx}/image/favicon.ico" type="image/x-icon" /> 
<link rel="shortcut icon" href="${ctx}/image/favicon.ico" type="image/x-icon" />



<link rel="stylesheet" type="text/css" href="${ctx}/css/style/base.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/style/style.css">


	


<link rel="stylesheet"
	href="${ctx}/js/lib/plugin/Jcrop/css/jquery.Jcrop.min.css" />
<link rel="stylesheet"
	href="${ctx}/js/lib/plugin/piccutupload/jquery.jcrop.upload.plug.css" />
	
<script type="text/javascript"
	src="${ctx}/js/lib/jquery/jquery-1.8.0.min.js"></script>
	<script type="text/javascript"
	src="${ctx}/js/lib/jquery/jquery.form.js"></script>
<script type="text/javascript"
	src="${ctx}/js/lib/plugin/Jcrop/js/jquery.Jcrop.js"></script>
<script type="text/javascript"
	src="${ctx}/js/lib/plugin/piccutupload/jquery.jcrop.upload.plug.js"></script>
<script type="text/javascript"
	src="${ctx}/js/front/userCenter/uploadphoto.js"></script>
</head>
<script type="text/javascript">
			var ctx="${ctx}";
	</script>
<body>

	<div id="content">
		<div class="tab1" id="tab1">
			<div class="menule">
				<ul>
					<li class="tab1"><a href="#">系统头像</a></li>
					<li class="tab2"><a href="#">自定义头像</a></li>
				</ul>
			</div>
			<div class="menudiv">
				<div class="tab1-content content">
					<div class="man_tou">
						<dd>可以选择喜欢的头像进行更换</dd>
						<ul class="man_tou_pu">
							<li>
								<div>
									<img alt="." src="${ctx}/image/photo/p1.jpg"/>
								</div>
								<p>哈哈</p>
							</li>
							<li>
								<div>
									<img alt="." src="${ctx}/image/photo/p2.jpg"/>
								</div>
								<p>哈哈</p>
							</li>
							<li>
								<div>
									<img alt="." src="${ctx}/image/photo/p3.jpg"/>
								</div>
								<p>哈哈</p>
							</li>
							<li>
								<div>
									<img alt="." src="${ctx}/image/photo/p4.jpg"/>
								</div>
								<p>哈哈</p>
							</li>
							<li>
								<div>
									<img alt="." src="${ctx}/image/photo/p5.jpg"/>
								</div>
								<p>哈哈</p>
							</li>
							<li>
								<div>
									<img alt="." src="${ctx}/image/photo/p6.jpg"/>
								</div>
								<p>哈哈</p>
							</li>
							<li>
								<div>
									<img alt="." src="${ctx}/image/photo/p5.jpg"/>
								</div>
								<p>哈哈</p>
							</li>
							<li>
								<div>
									<img alt="." src="${ctx}/image/photo/p6.jpg"/>
								</div>
								<p>哈哈</p>
							</li>
							
						</ul>
					</div>
					<div class="man_chuan">
						<dd>预览：</dd>
						<ul class="man_chuan_da">
							<form id="photoForm">
								<li class="manr_bai">
									<div>
										<img src="${ctx}/image/photo/p1.jpg" name="photoPath"/>
									</div>
									<p>大尺寸头像 100X100</p>
								</li>
								<li class="nemtn">
									<div>
										<img src="${ctx}/image/photo/p1.jpg" name="photoPath"/>
									</div>
									<p>30X30</p>
								</li>
								<li class="nemtn">
									<div>
										<input type="hidden" name="photoType" value="1" />
									</div>
								</li>
								<li class="nemtn">
									<div>
										<input type="hidden" name="photoState" value="1" />
									</div>
								</li>
							</form>
						</ul>
						<p class="input_kan">
							<a id="savePhoto" href="#">保存</a>
						</p>
						<p id="hintYesOrNo" class="input_kan"></p>
					</div>
				</div>
				<div class="tab2-content content" hidden="true">
					<div id="self-defind-photo-box"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>



