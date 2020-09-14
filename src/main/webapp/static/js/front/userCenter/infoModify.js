$.ns("sg.data.info");
sg.data.info = {
		//自定义初始化的菜单*
	ueditorTools1 : [ [ 
	                    'source', //源代码
                      '|',
                      'undo', //撤销
                      'redo', //重做
                      '|',
                      'bold', //加粗
                      'indent', //首行缩进
                      'italic', //斜体
                      'underline', //下划线
                      'strikethrough', //删除线
                      '|',
                      'insertorderedlist', //有序列表
                      'insertunorderedlist', //无序列表
                      '|',
                      'fontborder', //字符边框
                      'subscript', //下标
                      'superscript', //上标
                      'touppercase', //字母大写
                      'tolowercase', //字母小写
                      'justifyleft', //居左对齐
                      'justifyright', //居右对齐
                      'justifycenter', //居中对齐
                      'justifyjustify', //两端对齐
                      'forecolor', //字体颜色
                      'autotypeset', //自动排版
                      'fullscreen', //全屏
                      'imagenone', //默认
                      'imageleft', //左浮动
                      'imageright', //右浮动
                      'imagecenter', //居中
                      'lineheight', //行间距
                      '|',
                      'formatmatch', //格式刷
                      'pasteplain', //纯文本粘贴模式
                      'selectall', //全选
                      'horizontal', //分隔线
                      'removeformat', //清除格式
                      'date', //日期
                      'time', //时间
                      'cleardoc', //清空文档
                      'fontfamily', //字体
                      'fontsize', //字号
                      'paragraph', //段落格式
                      'link', //超链接
                      'unlink', //取消链接
                      'emotion', //表情
                      'spechars', //特殊字符
                      '|',
                      'simpleupload', //单图上传
                      'insertimage', //多图上传
                      'map', //Baidu地图
                      'insertvideo', //视频
                      'music', //音乐
                      'attachment', //附件
                      'scrawl', //涂鸦
                      'wordimage', //图片转存
                      'customstyle', //自定义标题
                      '|',
                      'print', //打印
                      'preview', //预览
                      'snapscreen', //截图
                      'backcolor', //背景色
                      'background', //背景
                      'template', //模板
                      'searchreplace', //查询替换
                      'drafts' // 从草稿箱加载
	] ],
	initEdit: function(){//初始化编辑
		/*$("#template-switch").switchbutton({
            checked: true,
            onChange: function(checked){
            	$(".template1").toggle();
            	$(".template2").toggle();
            }
        });*/
	},
	submit: function(){//提交编辑
		var formData = $.urlDecode($("#information-publish-form").serialize());
		if(sg.data.info.validate(formData)){
			$.ajax({
				url:ctx+"/publish/saveInformationMain",
				data:formData,
				type:'post',
				success:function(data){
					var msg = "";
					if(data != null){
						msg = "<br>发布信息成功！";
					}else{
						msg = "<br>发现信息失败,稍后重试！";
						type = "error";
					}
					alert(msg);
				}
			}); 
		};
	},
	validate: function(formData){//验证
		if($("#information-publish-form").form("validate")){//表单验证
			if(formData.infoType == "on"){//文本模式
				var content = "";
				if($.trim(sxcl.getContent()) == ""){
					content = "所需材料";
					$.messager.alert("提示",'<br>'+content+"不能为空！","warning", function(){
						setTimeout(function(){
							sxcl.focus();
						},500);
					});
					return false;
				}else if($.trim(bllc.getContent()) == ""){
					content = "办理流程";
					$.messager.alert("提示",'<br>'+content+"不能为空！","warning", function(){
						setTimeout(function(){
							bllc.focus();
						},500);
					});
					return false;
				}
				formData.materialNeeded = sg.data.info.warpperText(sxcl);
				formData.handlProcess = sg.data.info.warpperText(bllc);
				formData.handlConditions = sg.data.info.warpperText(bltj);
				formData.lawAccord = sg.data.info.warpperText(flyj);
			}else{//多媒体模式
				var picture = $("#pic-guide").ueuploader("getData");
				var media = $("#media-guide").ueuploader("getData");
				
				if((picture == undefined || picture == {}) && (media == undefined || media == {})){
					$.messager.alert("提示","<br>图片指南和多媒体指南不能同时为空！","warning");
					return false;
				}
				formData.picResPath = $("#pic-guide").ueuploader("getValue");
				formData.mediaResPath = $("#media-guide").ueuploader("getValue");
			}
			formData.linkFile = $("#link-file").ueuploader("getValue");
			formData.sampelFile = $("#sample-pic").ueuploader("getValue");
			return true;
		}else{//验证不通过
			return false;
		}
	},
	warpperText: function(editor){
		var warpper;
		if($.trim(editor.getContent() != "")){
			warpper = {html:editor.getContent(),text:editor.getContentTxt()};
		}
		return warpper || {};
	}
	
};