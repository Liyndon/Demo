/**
 * 扩展jquery的属性上传组件
	<form id="form1">  
		<div style="width: 100%; float: left;">  
		    <input type="hidden" id="hfThumbnail" value="image/login/test.png" />  
		    <div class="imgdiv"></div>  
		    <span class="img_span">  
		        <input type="file" name="file" />  
		    </span>  
		       
		    <input type="button" value="上传" class="upload" />  
		</div>  
		<div style="width: 100%; float: left;">  
		    <input type="hidden" id="hfThumbnail" value="image/login/test.png" />  
		    <div class="imgdiv"></div>  
		    <span class="img_span">  
		        <input type="file" name="file" />  
		    </span>  
		       
		    <input type="button" value="上传" class="upload" />  
		</div>
	</form>  
	<script type="text/javascript">  
	    $(function () {  
	        $(".upload").upload({  
	        	uploadUrl:"${ctx}/ueditor/dispatch?action=uploadimage"
	        });  
	    });  
	</script>
 */
(function ($) {  
    $.extend($.fn, {  
        upload: function (settings) {  
            var options = $.extend({
				rootPath: "",
                fileType: {"image":"gif|jpg|jpeg|png|bmp","file":"png|jpg|jpeg|gif|bmp|flv|swf|mkv|avi|rm|rmvb|mpeg|mpg|ogg|ogv|mov|wmv|mp4|webm|mp3|wav|mid|rar|zip|tar|gz|7z|bz2|cab|iso|war|doc|docx|xls|xlsx|ppt|pptx|pdf|txt|md|xml"},                       //允许的文件格式  
                iconType: ["image","file-video-o","file-audio-o","file-zip-o","file-word-o","file-excel-o","file-powerpoint-o","file-pdf-o","file-text-o","file-code-o"],
                iconCatogray:["gif|jpg|jpeg|png|bmp","flv|swf|mkv|avi|rm|rmvb|mpeg|mpg|ogg|ogv|mov|wmv|mp4|webm","mp3|wav|mid","rar|zip|tar|gz|7z|bz2|cab|iso|war","doc|docx","xls|xlsx","ppt|pptx","pdf","txt|md","xml"],
                pluginType: "file",                                //上传的类型image和file
                uploadUrl: "/ajax/handler?action=uploadfile",      //上传URL地址  
                deleteUrl: "/ajax/handler?action=deletefile",      //删除URL地址
                width: 300,                                            //显示的宽度  
                height: 150,                                            //显示的高度  
                imgSelector: ".imgdiv",                                  //图片选择器  
                fileSelector: ".filediv",                                //文件选择器
                uploadData: {},                                         //上传时需要附加的参数  
                deleteData: {}, 
				fileUrlPrefix: "/",                         //删除时需要附加的参数  
                deleteFn: function ($parent, showMessage) {             //删除图片的方法(默认方法使用POST提交)  
                    methods.deleteFile($parent, showMessage);  
                },  
                beforeSubmitFn: "beforeUpload",                         //上传前执行的方法 原型 beforeSubmit(arr, $form, options);  
                successFn: "uploadSuccess",                             //上传成功后执行的方法 uploadSuccess(response, statusText, xhr, $this)  
                errorFn: "uploadError"                                  //上传失败后执行的方法  
            }, settings);  
			
			var _this = $(this);
			var myfileBox = $(this).parent().find(".img_span");
			var myImageDelBtn = $(this).parent().find(".deleteImg");
            //上传准备函数  
            var methods = {  
                //验证文件格式  
                checkFile: function (filename) {
                	var str1 = methods.getSuffix(filename);
                    var re = new RegExp("\.(" + options.fileType[options.pluginType] + ")$");  
                    return re.test(str1);  
                },
                getSuffix:function(filename){
                    var pos = filename.lastIndexOf(".");  
                    var str = filename.substring(pos, filename.length);  
                    return str.toLowerCase();  
                },
                //创建表单  
                createForm: function () {  
                    var $form = document.createElement("form");  
                    $form.action = options.uploadUrl+(options.pluginType=="file"?"?action=uploadfile":"?action=uploadimage");  
                    $form.method = "post";  
                    $form.enctype = "multipart/form-data";  
                    $form.style.display = "none";  
                    //将表单加当document上，  
                    document.body.appendChild($form);  //创建表单后一定要加上这句否则得到的form不能上传。document后要加上body,否则火狐下不行。  
                    return $($form);  
                },  
                //创建图片  
                createImage: function () {  
                    //不能用 new Image() 来创建图片，否则ie下不能改变img 的宽高  
                    var img = $(document.createElement("img"));  
                    if (options.width !== "") {  
                        img.attr({ "width": options.width });  
                    }  
                    if (options.height !== "") {  
                        img.attr({ "height": options.height });  
                    }
					img.hide();  
                    return img;  
                },  
                //创建文件  
                createFile: function () {  
                    //不能用 new Image() 来创建图片，否则ie下不能改变img 的宽高  
                    var file = $(document.createElement("div"));
                    file.width(options.width).css({"white-space":"nowrap","text-overflow":"ellipsis","overflow":"hidden"});
                    $("<i class='fa-lg'></i>&nbsp;<span><span>").appendTo(file);
					file.hide();  
                    return file;  
                },  
                showImage: function (filePath, $parent) {  
                    var $img = methods.createImage();  
                    $parent.find(options.imgSelector).find("img").remove();  
                    //要先append再给img赋值，否则在ie下不能缩小宽度。  
                    $img.appendTo($parent.find(options.imgSelector)); 
                    $img.attr("src", options.fileUrlPrefix+filePath);  
                    this.bindDelete($parent);
                }, 
                showFile: function(fileName, $parent){
                	var $file = methods.createFile();  
                    $parent.find(options.fileSelector).find("div").remove();  
                    //要先append再给img赋值，否则在ie下不能缩小宽度。  
                    $file.appendTo($parent.find(options.fileSelector)); 
                    $file.find("span").html(fileName);
                    var suffix = methods.getSuffix(fileName);
                    for(var i=0; i<options.iconCatogray.length; i++){
                    	if(new RegExp("\.(" + options.iconCatogray[i] + ")$").test(suffix)){
                    		$file.find("i").addClass("fa fa-"+options.iconType[i]);
                    		break;
                    	};  
                    }
                    this.bindDelete($parent);
                },
				toggleUploadAndShowDelete: function(){
					_this.toggle();
					myfileBox.toggle();
					myImageDelBtn.toggle();
					myfileBox.parent().find(options.imgSelector).find("img").toggle();
					myfileBox.parent().find(options.fileSelector).find("div").toggle();
				},
                bindDelete: function ($parent) {  
					myImageDelBtn.unbind("click").bind("click", function () {  
                        options.deleteFn($parent, true);
                    });  
                },  
                deleteFile: function ($parent, showMessage) {  
                    var $fileInput = $parent.find(".file_url_hidden");  
                    if ($fileInput.val() !== "") {  
                        var data = $.extend(options.deleteData, { filePath: $fileInput.val(), t: Math.random() });  
  						$.ajax({
							url:options.deleteUrl,
							type:'post',
							data:{fileUrl:$fileInput.val()},
							success:function(result){
								if(result == "SUCCESS"){
									methods.toggleUploadAndShowDelete.call($parent);
									$fileInput.val("");
								}else{
									alert("delete file faild!");
								}
							}
						});
                    }  
                },  
                onload: function ($parent) {  
                    var hiddenInput = $parent.find(".file_url_hidden");  
                    if (typeof hiddenInput !== "undefined" && hiddenInput.val() !== "") {  
                        var img = methods.createImage();  
                        if ($parent.find(options.imgSelector).find("img").length > 0) { $parent.find(options.imgSelector).find("img").remove(); }  
                        //要先append再给img赋值，否则在ie下不能缩小宽度。   
                        img.appendTo($parent.find(options.imgSelector));  
                        img.attr("src", hiddenInput.val());  
                        methods.bindDelete($parent);  
                    }  
                }  
            };  
            //上传主函数  
            this.each(function () {  
                var $this = $(this);  
                methods.onload($this.parent()); 
                $this.bind("click", function () { 
                    var $fileInput = $(this).parent().find("input:file");  
                    var fileBox = $fileInput.parent();  
                    
                    if ($fileInput.val() === "") {  
                        alert("请选择要上传的文件！");  
                        return false;  
                    }else   
                    //验证文件格式
                    if (!methods.checkFile($fileInput.val())) {  
                        alert("不支持上传文件的格式！");  
                        return false;  
                    }else  
                    //若隐藏域中有文件，先删除文件  
                    if ($fileInput.val() !== "") {  
                        options.deleteFn($this.parent(), false);  
                       // methods.deleteImage($this.parent(), false);  
                    }  
  
                    //创建表单  
                    var $form = methods.createForm();  
  
                    //把上传控件附加到表单  
                    $fileInput.appendTo($form);  
                    fileBox.html("<img src='"+options.rootPath+"/image/loaderc.gif' />   正在上传...  ");  
                    $this.attr("disabled", true);  
  
                    //构建ajaxSubmit参数  
                    var data = {};  
                    data.data = options.uploadData;  
                    data.type = "POST";  
                    data.dataType = "JSON";  
                    //上传前  
                    data.beforeSubmit = function (arr, $form, options) {  
                        var beforeSubmitFn;  
                        try { beforeSubmitFn = eval(options.beforeSubmitFn); } catch (err) { };  
                        if (beforeSubmitFn) {  
                            var $result = beforeSubmitFn(arr, $form, options);  
                            if (typeof ($result) == "boolean")  
                                return $result;  
                        }  
                    };  
                    //上传失败  
                    data.error = function (response, statusText, xhr, $form) {  
                        var errorFn;  
                        try { errorFn = eval(options.errorFn); } catch (err) { };  
                        if (errorFn) {  
                            errorFn(response.state, statusText, xhr, $this);  
                        }  
                    };  
                    //上传成功  
                    data.success = function (response, statusText, xhr, $form) {  
                    	var $parent = $this.parent();
                        //response = eval("(" + response + ")");  
                    	if(options.pluginType == "file"){
                    		if (response.state == "SUCCESS") {  
                                methods.showFile(response.title, $parent);  
                                $this.parent().find(".file_url_hidden").val(response.url);  
                            } else {  
                                alert(response.state);  
                            }  
                    	}else{
                    		if (response.state == "SUCCESS") {  
                                methods.showImage(response.url, $parent);  
                                $this.parent().find(".file_url_hidden").val(response.url);  
                            } else {  
                                alert(response.state);  
                            }  
                    	}
                    	var successFn;  
                        try { successFn = eval(options.successFn); } catch (err) { };  
                        if (successFn) {  
                            $.ajaxSetup({ cache: false });//这个不能少，否则ie下会提示下载  
                            successFn(response, statusText, xhr, $this);  
                        }  
                        $this.attr("disabled", false);  
						fileBox.html("<input type=\"file\" name=\"uploadFile\" />");
                        methods.toggleUploadAndShowDelete.call($parent);
                    };  
  
                    try {  
                        //开始ajax提交表单  
                        $form.ajaxSubmit(data);
                    } catch (e) {  
                        alert(e.message);  
                    }  
                });  
            });  
        }  
    });  
})(jQuery); 