/**
 * @description 给指定组建添加遮罩
 * @date 2013-6-22
 * @author zyyyZQ
 */
(function($){
	$.fn.masklayer = function(deal,options,fn){
		var defaults = {
			maskOpacity:'7',
			textBoxStyle:"border:2px solid #7291B6;background:#fff;font-weight:thin;",
			showText:true,
			zindex:9000,
			showType:'loading',//默认为加载，'loading','progress'
			progressBarWidth:250,
			progressBarValue:0,
			text:'Loading...'
		};
		var options;
		if(typeof deal=='string'){
			switch(deal){
				case 'showMask':
					options = $.extend(defaults, options);
					break;
				case 'cancelMask':break;
				case 'changeText':break;
				case 'progressBar':break;
				default:alert('插件不支持该方法');
			}
			return $.fn.masklayer.methods[deal].call(this,options);
		}
	};
	function showMask(options){
		if($(this).find('.masklayer-maskbg').length>0){
			$(this).find('.masklayer-maskbg').remove();
			$(this).find('.masklayer-masktextbox').remove();
		};
		var html = "<div class='masklayer-maskbg' " +
				"style='height:100%;width:100%;" +
						"background-color:black;opacity:"+options.maskOpacity/100+";filter:Alpha(Opacity="+options.maskOpacity+");" +
								"position:absolute;z-index:"+options.zindex+";top:0;left:0;'></div>";
		var layer = $(html).appendTo(this);
		if(options.showText){
			if("loading"==options.showType){//显示loading
				showContentHtml="<table algin=center><tr><td style='width:\"16px\"'>" +
				"<img src=\""+ctx+"/image/plugins/plugin-loading.gif\">" +
				"</td>" +
				"<td class='masklayer-text'>"+options.text+"</td>" +
				"</tr></table>";
			}else if("progress"==options.showType){//显示进度条
				showContentHtml="<table algin=center>" +
				"<tr>"+
					"<td class='masklayer-text'>"+options.text+"</td>" +
				"</tr>" +
				"<tr>" +
				"<td><div class='masklayer-progressbar' style='width:"+options.progressBarWidth+"px'></div></td>" +
				"</tr>" +
				"</table>";
			}
			var boxhtml = "<span class='masklayer-masktextbox' style='"+options.textBoxStyle+
				"position:absolute;z-index:100001;top:0;left:0;padding:7px 10px 7px 10px;'>" +
				showContentHtml+
				"</span>";
			var box = $(boxhtml).appendTo(this);
			var left = ($(this).outerWidth()-box.outerWidth())/2;
			var top = ($(this).outerHeight()-box.outerHeight())/2;
			box.css({left:left,top:top});
			$(this).find(".masklayer-progressbar").progressbar({value:options.progressBarValue});
		};
		this.resize(function(){
			var left = ($(this).outerWidth()-box.outerWidth())/2;
			var top = ($(this).outerHeight()-box.outerHeight())/2;
			box.css({left:left,top:top});
		});
	};
	$.fn.masklayer.methods = {
		showMask:function(options){
			showMask.call(this,options);
		},
		cancelMask:function(options){
			if($(this).find('.masklayer-maskbg').length>0){
				$(this).find('.masklayer-maskbg').remove();
				$(this).find('.masklayer-masktextbox').remove();
			};
		},
		changeText:function(options){
			if($(this).find('.masklayer-maskbg').length>0){
				$(this).find('.masklayer-text').html(options);
			}
		},
		progressBar:function(){
			if($(this).find('.masklayer-progressbar').length>0){
				return $(this).find('.masklayer-progressbar');
			}else{
				return null;
			}
		}
	};
})(jQuery); 