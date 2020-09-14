/**
 * 扩展jquery分页插件，
 */
(function($) {
	$.extend($.fn, {
		pager : function(options) {
			var $this = $(this);
			if(typeof options == "string"){
				return $.fn.pager.methods[options].call(this[0]);
			}
			var options = $.extend({
				currentPage:1,			//当前页
				totalPage:1,			//默认总共就1页
				showBoth: false,		//显示两边，就是始终显示第一页和最后一页，就不用显示首页和末页的按钮
				queryParam: {},			//分页时需要的参数
				onClick: function(pageIndex){},	//执行点击事件函数,
				showLoad: {contentBox:'',show:false},
				pageRange:[1]			//页面范围
			}, $this.attr('data-options') ? eval("({"+$this.attr('data-options')+"})"):{}, options || {});
			// 内部函数
			var methods = {
				//初始化函数
				init: function(){
					$this.addClass("pager-plugin-box");
					$this.data("options", options);
					methods.createList.call($this, options.pageRange, options.totalPage, options.currentPage);
					if(options.callback){//执行回调函数
						options.callback.call(options.data); //
					}
				},	
				createList: function(pageRange, totalPage, currentPage){
					var $ul = $(this).find("ul.pageul");
					var opts = $(this).data("options");
					if($ul.length == 0){
						$ul = $("<ul class='pageul'><ul>").appendTo($(this));
					}else{
						$ul.empty();
					}
					if(opts.showBoth){//显示两边页码
						$("<li class='upper-page page-btn'>上一页</li>").appendTo($ul);
						if(pageRange.length >= 1 && pageRange[0] >= 2){//可以显示首页
							$("<li class='page-item' val='1'>1</li>").appendTo($ul);
							if(pageRange[0] > 2){
								$("<li class='ellipsis'>...</li>").appendTo($ul);
							} 
						}
						methods.createPageItem(pageRange).appendTo($ul);
						if(pageRange.length >= 1 && pageRange[pageRange.length-1] <= totalPage-1){//显示末页
							if(pageRange[pageRange.length-1] < totalPage-1){
								$("<li class='ellipsis'>...</li>").appendTo($ul);
							} 
							$("<li class='page-item' val='"+totalPage+"'>"+totalPage+"</li>").appendTo($ul);
						}
						$("<li class='next-page page-btn'>下一页</li>").appendTo($ul);
					}else{//显示首末页按钮
						if(pageRange.length <= 1){
							$("<li class='first-page page-btn disabled'>首页</li>").appendTo($ul);
							$("<li class='upper-page page-btn disabled'>上一页</li>").appendTo($ul);
							methods.createPageItem(pageRange).appendTo($ul);
							$("<li class='next-page page-btn disabled'>下一页</li>").appendTo($ul);
							$("<li class='last-page page-btn disabled'>末页</li>").appendTo($ul);
						}else{
							$("<li class='first-page page-btn'>首页</li>").appendTo($ul);
							$("<li class='upper-page page-btn'>上一页</li>").appendTo($ul);
							methods.createPageItem(pageRange).appendTo($ul);
							$("<li class='next-page page-btn'>下一页</li>").appendTo($ul);
							$("<li class='last-page page-btn'>末页</li>").appendTo($ul);
						}
					}
					if(currentPage == 1){
						$ul.find(".upper-page").addClass("disabled");
						$ul.find(".first-page").addClass("disabled");
					}
					if(currentPage == pageRange[pageRange.length-1]){
						$ul.find(".next-page").addClass("disabled");
						$ul.find(".last-page").addClass("disabled");
					}
					$ul.find(".page-btn").bind("click", methods.btnClickFn);
					$ul.find(".page-item").bind("click", methods.itemClickFn);
				},
				createPageItem:function(pageRange){
					var items = "";
					for(var i=0; i< pageRange.length; i++){
						if(options.currentPage == pageRange[i]){//创建当前页
							items += "<li class='page-item page-current-page' val='"+pageRange[i]+"'>"+pageRange[i]+"</li>";
						}else{
							items += "<li class='page-item' val='"+pageRange[i]+"'>"+pageRange[i]+"</li>";
						}
					}
					return $(items);
				},
				btnClickFn:function(){
					if(options.onClick){//有点击事件
						if(!$(this).hasClass("disabled")){//没有被禁用
							if($(this).hasClass("first-page")){//跳转首页，第一页
								options.onClick(1);
							}else if($(this).hasClass("upper-page")){
								options.onClick(options.currentPage-1);
							}else if($(this).hasClass("next-page")){
								options.onClick(parseInt(options.currentPage)+1);
							}else if($(this).hasClass("last-page")){
								options.onClick(options.totalPage);
							}
						};
					}
				},
				itemClickFn:function(){
					if(options.onClick){//有点击事件
						if(!$(this).hasClass("page-current-page")){//不是当前页可以被点击
							//$(".page-current-page").removeClass("page-current-page");
							$(this).addClass("page-current-page");
							options.onClick($(this).attr("val"));
						}
					}
				}
			};
			$.fn.pager.methods = {
					showLoad:function(){
						var opt = $(this).data("options");
						if(opt.showLoad.show){
							var $this = $(opt.showLoad.contentBox);
							var $overlay = $this.find(".pager-loader-overlay");
							var parent = null;
							if($overlay.length > 0){
								parent = $overlay.parent();
								$overlay.height(parent.height()).width(parent.width()).css({"top":parent.offset().top+"px","left":parent.offset().left+"px"});
								$overlay.show();
							}else{
								$overlay = $("<div class='pager-loader-overlay' style='display:none;'><div class='pager-loader-overlay-bg'></div><div class='loader-text-box'></div></div>").appendTo($(opt.showLoad.contentBox));
								var parent = $overlay.parent();
								$overlay.height(parent.height()).width(parent.width()).css({"top":parent.offset().top+"px","left":parent.offset().left+"px"});
								$overlay.show();
							}
						}
					},
					hideLoad:function(){
						var opt = $(this).data("options");
						var $this = $(opt.showLoad.contentBox);
						var $overlay = $this.find(".pager-loader-overlay");
						if($overlay.length > 0){//存在并可见
							$overlay.hide();
						}
					}	
			};
			// 主函数
			this.each(function() {
				methods.init();
			});
		}
	});
})(jQuery);