$(function(){
	//标签切换
	$(".menule li").bind("click", function(){
		$(".menule li").removeClass("off");
		var classname = $(this).attr("class");
		$(".menudiv .content").hide();
		$(".menudiv").find("."+classname+"-content").show();
		$(this).addClass("off");
	});
	$(".menule li").eq(0).addClass("off");
	
	//选择头像
	$(".man_tou li").bind("click",function(){
		var s= $(this).find("img").attr("src");
		$("#photoForm").find("img").attr("src",s);
	});
	
	//保存图片
	$("#savePhoto").bind("click",function(){
		if($(".hover-click").length == 1){
			var data = $("#photoForm").serializeObject();
			var path = $(".hover-click img").attr("path");
			data.photoPath=path;
			$.ajax({
				type:"post",
				data:data,
				url:ctx+"/changeUserPhoto",
				success:function(res){
					if(res){
						$("#hintYesOrNo").html("<font style='color: red'>头像保存成功</font>");
						$(".user-photo").attr("src",ctx+"/"+path+"?_"+Math.random);
						jAlert('修改头像成功', '提示' ,"info");
					}else {
						$("#hintYesOrNo").html("<font style='color: red'>头像修改失败，请稍后重试</font>");
					}
				}
			});
		}else{
			alert("亲，您还没选择头像哦!");
		}
		
	});
	
	//上传图片
	$("#self-defind-photo-box").piccutupload({
		ctx:ctx+"/",
        uploadUrl: ctx+"/picCutUploader/dispatch",      //上传URL地址  
        width: 300,                                              //图片显示的宽度
        height: 300,  
        defaultImg: [ctx+'/image/photo/p1.jpg',ctx+'/image/photo/p2.jpg',ctx+'/image/photo/p3.jpg'],//默认占位图片
        previewInfo:[{height:60,width:60}],//需要查看获取的预览图
        successFn: function(data){
        	debugger;
        	alert("修改头像成功");
        	$(".manr_bai img").attr("src",ctx+"/"+data.url);
			$(".nemtn img").attr("src",ctx+"/"+data.url+"?_"+Math.random);
        }
	});
	
	
});