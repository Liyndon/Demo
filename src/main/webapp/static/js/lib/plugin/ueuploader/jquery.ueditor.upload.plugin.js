/**
 * @description 结合jquery+百度ueditor1.4.3.1(修改版)做一个自定义文件上传工具
 * @date 2015-10-11
 * @author 张强
 */
(function($) {
	$.fn.ueuploader = function(options, value) {
		if (typeof options == 'string' && options != "") {
			return $.fn.ueuploader.methods[options].call(this[0], value);// 执行函数
		}
		var config = {
			"image" : {
				btnText : "点击选择图片…",
				eventName : 'insertimage',
				iconCls : "fa fa-image"
			},
			"media" : {
				btnText : "点击选择多媒体音视频…",
				eventName : 'insertvideo',
				iconCls : "fa fa-film"
			},
			"file" : {
				btnText : "点击选择文件上传…",
				eventName : 'attachment',
				iconCls : "fa fa-folder-o"
			}

		};

		var dataOptions, str = $(this).attr("data-options");
		if (str == undefined) {
			dataOptions = {};
		} else {
			dataOptions = eval("({" + str + "})");// 转化成对象
		}

		var defaults = {
			type : 'image',// image、media、file
			btnStyle : 'white',// black、gray、white、orange、blue、red、rosy、green、pink几种颜色主题
			allowFiles : {},// 默认配置为空，引用明细配置的
			config : config,
			width : 500,
			height : 'auto',
			deleteable : true,
			editable : true,
			optPath : UEUPLOADER_PAHT || "js/ueuploader"// 配置文件访问路劲
		};
		var options = $.extend(defaults, dataOptions, options);
		if (!options.config[options.type]) {
			console.error("type=" + options.type
					+ ",类型错误，类型允许[image、media、file]");
			return;
		}
		options.config[options.type].btnText = options.btnText
				|| options.config[options.type].btnText;// 如果外界
		var id = $(this).attr("id");
		options.El = {
			editor : $("#" + id),
			id : id
		};
		$(this).html("");// 清空
		init.call(this[0], options);// 初始化组件
		return this;
	};
	function init(opt) {// 初始化插件
		var fn = this;
		var state = $.data(fn, 'uploader');
		if (state) {
			$.extend(state.options, opt);
		} else {
			state = $.data(fn, 'uploader', {
				options : opt
			});
		}
		$(this).addClass("uploader-waitter-box").addClass(
				opt.btnStyle + "-content").width(opt.width);
		var $btn = $(
				"<div class='uploader-select-btn button " + opt.btnStyle + "'>"
						+ "<i class='" + opt.config[opt.type].iconCls
						+ " '></i>" + "&nbsp;&nbsp;"
						+ opt.config[opt.type].btnText + "</div>").appendTo(
				$(fn));// 添加选择按钮
		var $container = $("<div class='uploader-container no-list'></div>")
				.appendTo($(fn));
		$(
				"<script id='"
						+ opt.El.id
						+ "-editor' type='text/plain' style='display:none;'></script>")
				.appendTo($(fn));
		var $editor = UE.getEditor(opt.El.id + "-editor", {
			toolbars : [ [ opt.config[opt.type].eventName ] ]
		});

		if (opt.height != "auto") {
			$container.css({
				"height" : opt.height + "px",
				"overflow" : "auto"
			});
		}
		;
		if ($.data(fn, 'component')) {
			$.extend($.data(fn, 'component'), {
				btn : $btn,
				container : $container
			});
		} else {
			$.data(fn, 'component', {
				btn : $btn,
				container : $container
			});// 保存对象组件对象
		}
		$editor.ready(function() {
			$editor.hide();
			var listenerName = "beforeInsertImage";
			switch (opt.type) {
			case "media":
				listenerName = "beforeInsertMedia";
				break;
			case "file":
				listenerName = "beforeInsertFile";
				break;
			}
			$editor.addListener(listenerName, function(t, agrs) {
				var uploaders = new Array();
				$.each(agrs, function(index, item) {
					uploaders.push(new uploaderItem(item.url || item.src,
							item.title, opt.editable, opt.deleteable, null,
							null));
				});

				addList.call(fn, uploaders);// 插入列表
				return "stop";
			});
			$btn.bind("click", function() {
				switch (opt.type) {// edui-for-insertvideo
				case "image":
					upImage.call(fn, $editor);
					break;
				case "media":
					upMedia.call(fn, $editor);
					break;
				case "file":
					upFiles.call(fn, $editor);
					break;
				}
			});
		});
		$(this).show();// 显示整个对象
	}
	;
	// 弹出图片上传的对话框
	function upImage(_editor) {
		$(this).find(".edui-for-insertimage .edui-button-body")[0].click();
	}
	// 弹出多媒体音视频上传的对话框
	function upMedia(_editor) {
		$(this).find(".edui-for-insertvideo .edui-button-body")[0].click();
	}
	// 弹出文件上传的对话框
	function upFiles(_editor) {
		$(this).find(".edui-for-attachment .edui-button-body")[0].click();
	}
	function uploaderItem(url, title, editable, deleteable, dom, id) {// 上传对象
		this.id = id;
		this.url = url;
		this.title = title;
		this.editable = editable;
		this.deleteable = deleteable;
		this.dom = dom;
	}
	;
	function addList(list) {
		var fn = this;
		var data = $.data(fn, 'data');// 保存
		if (data) {
			data = $.data(fn, "data", data.concat(list));
		} else {
			data = $.data(fn, "data", list);
		}
		$.each(list,function(index, item) {
			var width = $.data(fn, "component").container.width();
			if ($.data(fn, "uploader").options.height != "auto") {// 自动增长高度
				width = width - 10;
			}
			var dom = $(
					"<div style='width:"
							+ (width - 3)
							+ "px' itemid='uploader-itemid-"
							+ item.id
							+ "' class='uploader-item-box-container "
							+ $.data(fn, "uploader").options.btnStyle
							+ "-item'>"
							+ "<input type='hidden' value='"
							+ item.id
							+ "'/>"
							+ "<div class='uploader-item-url'>"
							+ item.url
							+ "</div>"
							+ "<div class='uploader-item-delbtn'>"
							+ "<i class='fa fa-trash fa-lg'></i>"
							+ "</div>"
							+ "<div class='uploader-item-title'>"
							+ "<input type='text' required=true class='title-input "
							+ $.data(fn, "uploader").options.btnStyle
							+ "-input'/>" + "</div>" + "</div>").appendTo($.data(fn, "component").container);
			item.dom = dom;// 设置dom元素
			if (!$.data(fn, "uploader").options.deleteable) {
				dom.find(".uploader-item-delbtn").hide();// 不能被删除
			} else {
				dom.find(".uploader-item-delbtn").bind("click",function() {
					var data = $.data(fn,"data");
					if (data) {
						$.each(data,function(index,di) {
							if (item == di) {
								data.remove(index);
								$.data(fn,"data",data);
							}
						});
					}
					dom.remove();
				});// 不能被删除
			}
			var $inputbox = dom.find(".uploader-item-title input");
			$inputbox.textbox({value:item.title, onChange:function(nv, ov){
				var md = $.data(fn, 'data');
				$.each(md, function(i, t){
					if(t.id == item.id){
						t.title = nv;
					}
				});
				
			}});
			if (!$.data(fn, "uploader").options) {
				$inputbox.textbox("readonly", true);
				dom.find(".uploader-item-title").find("input").addClass("diabled-input");
			}
		});
	}
	;
	$.fn.ueuploader.methods = {
		loadData : function(jsonObj) {
			addList.call(this, jsonObj);
		},
		getData : function() {
			return $.data(this, "data");
		},
		getValue : function() {
			var data = $.fn.ueuploader.methods.getData.call(this);
			if (data != undefined && data != null && data.length != 0) {
				var array = [];
				$.each(data, function(index, item) {
					array[index] = {
						id : item.id,
						path : item.url,
						title : item.title
					};
				});
				return JSON.stringify(array);
			}
			return null;
		},
		options : function() {
			return $.data(this, "uploader").options;
		}
	};
})(jQuery);
/*
 * (function() {//创建解析器 $("body").find(".ue-uploader").each(function(){//解析
 * $(this).hide();//出示化隐藏 var dataOptions, str = $(this).attr("data-options");
 * if(str == undefined){ dataOptions = {}; }else{ dataOptions =
 * eval("({"+str+"})");//转化成对象 } if($(this).attr("id") == undefined){//没有ID属性
 * $(this).attr("id", "ueuploader-"+Math.uuid());//指定新的id }
 * $(this).ueuploader(dataOptions);//初始化对象 }); })();
 */