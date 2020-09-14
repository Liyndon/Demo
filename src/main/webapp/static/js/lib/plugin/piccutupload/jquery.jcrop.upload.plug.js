/**
 * 扩展jquery组件，基于jcrop截图插件实现的图片剪切上传组件，需要引用指定的jar包
 */
(function ($) {  
    $.extend($.fn, {  
        piccutupload: function (settings) {  
        	var _self = this,
            _this = $(this),
            imgs,
            api = null,
            previewSize = new Array(),
		    boundx,
		    boundy;
        	
            browserCfg = {};
            var options = $.extend({  
            	ctx:"/",
                fileType: "gif|jpg|jpeg|png|bmp",                       //允许的文件格式  
                uploadUrl: "/ajax/handler.action",      //上传URL地址  
                defaultImg: ['image/defult-upload-photo.png','image/defult-upload-photo-preview.png','image/defult-upload-photo-preview.png'],//默认占位图片
                width: 300,                                              //图片显示的宽度
                height: 300,                                            //图片显示的高度
                properties:'piccutuploader',                            //配置文件名
                previewInfo:[{height:120,width:120},{height:60,width:60}],//需要查看获取的预览图
                defaultJcropSize:[30,30,120,120],                       //默认剪切框大小
                beforeSubmitFn: "beforeUpload",                         //上传前执行的方法 原型 beforeSubmit(arr, $form, options);  
                successFn: "uploadSuccess",                             //上传成功后执行的方法 uploadSuccess(response, statusText, xhr, $this)  
                errorFn: "uploadError"                                  //上传失败后执行的方法  
            }, settings || {});
            //上传准备函数  
            var methods = {
            	//初始化函数，获得浏览器信息
            	init: function(){
            		var ua=window.navigator.userAgent;
            		if (ua.indexOf("MSIE")>=1){//IE9及以下
            			browserCfg.ie = true;
            		}else if(ua.indexOf("Firefox")>=1){//FF
            			browserCfg.firefox = true;
            		}else if(ua.indexOf("Chrome")>=1){//google
            			browserCfg.chrome = true;
            		}else if(ua.indexOf("Apple")>=1){//苹果浏览器
            			browserCfg.mac = true;
            		}else{//其他浏览器
            			browserCfg.other = true;
            		}
            		_this.addClass("pic-cut-uploader-content");
            	},
                //验证文件格式  
                checkFile: function (fileType) { 
                    if (typeof options.fileType !== 'string'){ 
                    	options.fileType = "gif|jpg|jpeg|png|bmp"; 
                    }  
                    fileType = fileType.toLocaleLowerCase();
                    var re = new RegExp("\(" + options.fileType + ")$");  
                    var flag = re.test(fileType);
                    return flag;
                },  
                //创建表单  
                createForm: function () {  
                    var $form = document.createElement("form");  
                    $form.action = options.uploadUrl;  
                    $form.method = "post";  
                    $form.enctype = "multipart/form-data";  
                    //将表单加当document上，  
                    _this.html("").append($form);
                    return $($form);  
                },  
                getFileType: function(filepath){
                	//为了避免转义反斜杠出问题，这里将对其进行转换
                	var re = /(\\+)/g; 
                	var filename=filepath.replace(re,"#");
                	//对路径字符串进行剪切截取
                	var one=filename.split("#");
                	//获取数组中最后一个，即文件名
                	var two=one[one.length-1];
                	//再对文件名进行截取，以取得后缀名
                	var three=two.split(".");
                	 //获取截取的最后一个字符串，即为后缀名
                	var fileType=three[three.length-1];
                	//添加需要判断的后缀名类型
                	return fileType;
                },
                //创建组件内容  
                initContent: function ($form) {  
                	var state = $.data(_this, 'previewArr');
                	if(!state){state = $.data(_this, 'previewArr', new Array());}
                	var $ul = $("<ul class='img-container'></ul>").appendTo($form);
                	var $jcropli = $("<li></li>").appendTo($ul);
                	var $jcropImgPanel = $("<div class='jcrop-panel'><div>").appendTo($jcropli);
                	var $jcropImgBtn = $("<div class='jcrop-btn'>" +
                			"<div class='upload-btn selecter'>"+
								"<a class='my-button c5 uploadfile-btn'>上传图片</a>"+
								"<input type='file' class='file-value' name='uploadfile'/>" +
							"</div>" +
							"<div class='upload-btn uploader'>" +
								"<a class='my-button c1 cut-submit-btn'>剪切上传</a>"+
							"</div>" +
                			"<div>").appendTo($jcropli);
                	$jcropImgPanel.width(options.width).height(options.height).css("position","relative");
                	var overlay = $("<div class='uploader-cut-overlay'><div class='overlay-tip'><span class='inout-icon-loader'></span><span class='tip-content'><span></div></div>").appendTo($jcropImgPanel);
                	overlay.hide().css({width:options.width+2,height:options.height+2});
                	$jcropImgBtn.width(options.width);
                	var $jcropImg = $("<img alt='' src='"+options.defaultImg[0]+"' class='target'>").appendTo($jcropImgPanel)
                		.css({"max-width":$jcropImgPanel.width()+"px","max-height":$jcropImgPanel.height()+"px"});
                    var pi = options.previewInfo;
                    $.each(pi, function(index, item){
                    	var $prejcropli = $("<li></li>").appendTo($ul);
                    	var $previewPanel = $("<div class='preview-panel'><div class='preview-container'>" +
                    			"<img src='"+options.defaultImg[index+1]+"' class='jcrop-preview' alt='Preview' />" +
                    			"</div></div>").appendTo($prejcropli);
                    	$("<p class='presize'>"+options.previewInfo[index].width+"*"+options.previewInfo[index].height+"</p>").insertAfter($previewPanel);
                    	var container = $previewPanel.find(".preview-container");
                    	container.width(options.previewInfo[index].width).height(options.previewInfo[index].height);
                    	container.find("img").css("width","100%").css("height","100%");
                    	$.data(_this, 'previewArr').push(container.find(".jcrop-preview"));
                    });
                },
                checkSelectFile: function($form){
                	return $form.find("[name=purl]").length > 0;
                },
                updatePreview: function(c){
                	for(var i = 1; i<imgs.length; i++){
                		var previewer = imgs.eq(i);
                		var rx = previewSize[i-1].width / c.w;                                             
    					var ry = previewSize[i-1].height / c.h;
                		previewer.css({
      					  width: Math.round(boundx*previewSize[i-1].width/c.w) + 'px',
      					  height: Math.round(boundy*previewSize[i-1].height/c.h) + 'px',
      					  marginLeft: '-' + Math.round(rx * c.x) + 'px',
      					  marginTop: '-' + Math.round(ry * c.y) + 'px'
      					});
                		methods.updateParam(c);
                	}
                },
                updateParam: function(c){
                	$('input[name=x]').val(c.x);
                    $('input[name=y]').val(c.y);
                    $('input[name=w]').val(c.w);
                    $('input[name=h]').val(c.h);
                },
                loadImage: function($form){
                	methods.showProgress($form, "加载中...");
                	$form.ajaxSubmit({
                		type: 'post',  
                		dataType:"json",
                        url: options.uploadUrl+"?action=uploadimage&configer="+options.properties, 
                        beforeSubmit: function() {} ,  
                        success: function(data){ 
                        	methods.hideProgress($form);
                        	if(data.state != "SUCCESS"){
                        		alert(data.state);
                        		return;
                        	}
                        	imgs = $form.find("img");
                        	for(var i = 1; i< imgs.length; i++){
                        		previewSize[i-1] = {width:imgs.eq(i).parent(".preview-container").width(),height:imgs.eq(i).parent(".preview-container").height()};
                        	}
                        	imgs.attr("src",options.ctx+data.url+"?rand="+Math.random());
                        	imgs.eq(0).load(function(){
                        		var _t = $(this);
                    			_t.Jcrop({
                    				allowSelect: false,
                    				onChange: methods.updatePreview,
                    				onSelect: methods.updatePreview,
                    				aspectRatio: 1
                    			},function(){
                    				var bounds = this.getBounds();
                    				boundx = bounds[0];
                    				boundy = bounds[1];
                    				api = this;
                    				api.animateTo(options.defaultJcropSize);
                    				if($("#cuterparam-divbox").length == 0){
                    					$("<div id='cuterparam-divbox'><input type='hidden' name='purl' value='"+data.url+"'/>" +
                    							"<input type='hidden' name='scale' value='"+data.width/boundx+"'/>" +
                    							"<input type='hidden' name='x'/>" +
                    							"<input type='hidden' name='y'/>" +
                    							"<input type='hidden' name='w'/>" +
                    					"<input type='hidden' name='h'/></div>").insertAfter($form.find("[name=uploadfile]"));
                    				}
                    				$("input[name=uploadfile]").val("");//清空文件选择
                    			});
                        	});
                        }
                	});
                },
                cutpic:function($form){
                	methods.showProgress($form, "保存中...");
                	$.ajax({
                		type: 'post',  
                		dataType:"json",
                		data:$form.serialize(),
                        url: options.uploadUrl+"?action=imagecut&configer="+options.properties,  
                        beforeSubmit: function() {} ,  
                        success: function(data){  
                        	if(data.state != "SUCCESS"){
                        		alert("头像修改失败，"+data.info);
                        	}else{
                        		methods.hideProgress($form);
                        		var successFn;  
                                try { 
                                	successFn = eval(options.successFn); 
                                } catch (err) {
                                	//没有成功回调函数
                                };  
                                if (successFn) {  
                                    $.ajaxSetup({ cache: false });//这个不能少，否则ie下会提示下载  
                                    var $pcon = $form.find(".target").parent();
                                    if(api){
                            			api.destroy();
                            			var murl = $form.find(".target").attr("src");
                            			$form.find(".target").remove();
                            			var $pimg = $("<img alt='' src='"+murl+"' class='target'>").prependTo($pcon);
                            			$pimg.css({"max-width":$pcon.width()+"px","max-height":$pcon.height()+"px"});
                            			var imgs = $form.find("img");
                            			$.each(imgs, function(index, item){
                            				$(item).attr("src", options.defaultImg[index]);
                            				$(item).removeAttr("style");
                            				if(index > 0){
                            					$(item).css(options.previewInfo[index-1]);
                            				}
                            			});
                            		}
                                    successFn(data);  
                                } 
                        	}
                        },  
                        error: function(XmlHttpRequest, textStatus, errorThrown){  
                            alert( "ajax system error...");  
                        }
                	});
                },
                showProgress:function($form, msg){
                	$form.find(".uploader-cut-overlay").fadeIn(500).find(".tip-content").html(msg);;
                },
                hideProgress:function($form){
                	$form.find(".uploader-cut-overlay").hide();
                }
            };  
            
            //上传主函数  
            this.each(function () {
            	//初始化
                methods.init();
                var $this = $(this); 
                var $form = methods.createForm();
                methods.initContent($form);
                $this.find(".file-value").bind("change", function(){
                	var fileType = methods.getFileType($(this).val());
                	if(!methods.checkFile(fileType)){
                		alert("上传文件的格式不正确");
                		return;
                	}else{
                		var $pcon = $form.find(".target").parent();
                		if(api){
                			api.destroy();
                			var murl = $form.find(".target").attr("src");
                			$form.find(".target").remove();
                			var $pimg = $("<img alt='' src='"+murl+"' class='target'>").prependTo($pcon);
                			$pimg.css({"max-width":$pcon.width()+"px","max-height":$pcon.height()+"px"});
                		}
            			methods.loadImage.call(this, $form);
                	}
                });
                $this.find(".uploadfile-btn").bind("click", function () {  
                    var $fileInput = $(this).next("input:file");  
                    $fileInput.click();
                });  
                $this.find(".cut-submit-btn").bind("click", function () {
                	if(methods.checkSelectFile($form)){
                		methods.cutpic.call(this, $form);
                	}else{
                		alert("请选择需要上传的图片");
                	};
                });
            });  
        }  
    });  
})(jQuery); 