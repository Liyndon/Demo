/**
 * version:contener 1.0
 * author:zyyyzq
 * param:deal,options
 * time:2013-5-20 14:15
 * desc:容器
 */
(function($){
	var status=true;
	$.fn.contener = function(options){
		var defaults={
			disabled:false,//默认是否禁用，不禁用
			title:'容器标题',//必要参数
			animate:true,//是否有动画
			closed:true //是否为关闭状态
		};
		if(typeof options=='string'&&options!=""){
			return $.fn.contener.methods[options].call(this[0]);//执行函数
		}
		var options=$.extend(defaults, options);
		this.each(function() {
			var conter_html=$(this).html();
			$(this).html("");
			var state = $.data(this, 'contener');
			if(state){
				$.extend(state.options, options);
			}else{
				state = $.data(this, 'contener', {options:options});
			}
			var header = $("<div class='contener-header'></div>").appendTo($(this));
			var titleText = $("<span class='contener-title'>"+
					options.title+"</span>").appendTo(header);
			$("<div class='contener-details'>"+conter_html+"</div>").insertAfter(header);
			titleText.mouseenter(function(){
				  $(this).css("color","#000");
			}).mouseleave(function(){
				  $(this).css("color","#444");
			});
			if(options.closed){
				$(this).children(".contener-details").hide();
				$(this).children(".contener-header").css("background","url('"+ctx+"/image/plugins/arrow-close.png') no-repeat")
				status=true;
			}else{
				$(this).children(".contener-header").css("background","url('"+ctx+"/image/plugins/arrow-opend.png') no-repeat")
				status=false;
			}
			init.call(this,options);
		});
	};
	function init(options){
		$(this).find(".contener-title").bind("click",function(){
			changeStatu.call(this,options);
		});
		if(options.disabled){
			disable.call(this);
			alert(1);
//			$.fn.contener.methods[options].call(this);
//			alert(2);
		}
	};
	function disable(){
		var head = $(this).find(".contener-title").parent(".contener-header");
		if(!status){
			head.next(".contener-details").hide();
			status=true;
		}
		$(this).find(".contener-title").css({color:"#aaa",cursor:'default'});
		$(this).find(".contener-title").mouseenter(function(){
			  $(this).css("color","#aaa");
		}).mouseleave(function(){
			  $(this).css("color","#aaa");
		});
		head.css("background","url('"+ctx+"/image/plugins/arrow-close-disable.png') no-repeat");
		$(this).find(".contener-title").unbind("click");
	};
	$.fn.contener.methods={
		isClosed:function(){
			return status;
		},
		disable:function(){
			disable.call(this);
		},
		enable:function(){
			var head = $(this).find(".contener-title").parent(".contener-header");
			var options = $.data(this, 'contener').options;
			$(this).find(".contener-title").bind("click",function(){
				changeStatu.call(this,options);
			});
			$(this).find(".contener-title").css({color:"#444",cursor:'pointer'});
			head.css("background","url('"+ctx+"/image/plugins/arrow-close.png') no-repeat");
			$(this).find(".contener-title").mouseenter(function(){
				  $(this).css("color","#000");
			}).mouseleave(function(){
				  $(this).css("color","#444");
			});
		}
	};
	function changeStatu(options){//点击修改闭开
		//此时这里面的this指向contener-title把他转向head
		var head = $(this).parent(".contener-header");
		if(status){
			head.css("background","url('"+ctx+"/image/plugins/arrow-opend.png') no-repeat")
			if(options.animate){
				head.next(".contener-details").slideDown();
			}else{
				head.next(".contener-details").show();
			}
			status=false;
		}else{
			head.css("background","url('"+ctx+"/image/plugins/arrow-close.png') no-repeat")
			if(options.animate){
				head.next(".contener-details").slideUp();
			}else{
				head.next(".contener-details").hide();
			}
			status=true;
		}
	};
})(jQuery);