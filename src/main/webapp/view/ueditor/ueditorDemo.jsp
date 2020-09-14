<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
	<head>
		<script type="text/javascript" charset="utf-8" src="${ctx}/js/lib/jquery/jquery-1.8.0.min.js"></script>
<script src="${ctx}/js/lib/plugin/jquery.namespace.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">window.UEDITOR_HOME_URL = "${ctx}/js/lib/ueditor/";
 var ctx="${ctx}";
</script>
<script type="text/javascript" charset="utf-8" src="${ctx}/js/lib/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/js/lib/ueditor/ueditor.all.js"> </script> 
<script type="text/javascript" charset="utf-8" src="${ctx}/js/lib/plugin/ueuploader/jquery.poshytip.min.js"> </script> 
<script type="text/javascript" charset="utf-8">
var UEUPLOADER_PAHT = "${ctx}/js/lib/plugin/ueuploader/";//配置ueuploader的地址
</script>
<script type="text/javascript" charset="utf-8" src="${ctx}/js/lib/plugin/ueuploader/jquery.ueditor.upload.plugin.min.js"> </script> 
<link rel="stylesheet" href="${ctx}/js/lib/plugin/ueuploader/ueuploader.css" type="text/css">
<script type="text/javascript" src="${ctx}/js/front/userCenter/infoModify.js"></script>
<style>
.label-content{
	text-align: right;
}
.label-to-top{
	vertical-align: top;
	padding-top: 8px;
}
.info-tip{
	text-align: left;
	color:#aaa;
} 
.template2{
	display: none;
}
</style>

	</head>
	
	<body>
		
<script type="text/javascript">
	$(function(){
		sg.data.info.initEdit();//初始化页面功能
	});
</script>
<div >
	<div  style="background: #efefef;">
		<div style="width: 900px; margin: 10px auto; min-height: 90%; border:1px solid #ccc; background:#fff; padding:20px;">
			<form id="information-publish-form">
				<input type="hidden" name="id"/>
				<table id="form-table">
					
					
					<tr class="template1">
						<td>
						    <script id="sxcl" type="text/plain" style="width:800px; height:500px;"></script>
						    <script type="text/javascript">
						        var sxcl = UE.getEditor('sxcl',{
						        	toolbars: sg.data.info.ueditorTools1	
						        });
						    </script>
						</td>
						<td class="info-tip">&nbsp;</td>
						<td>&nbsp;</td>
					<tr>
				</table>
			</form>
		</div>
	</div>
</div>
	</body>
</html>

