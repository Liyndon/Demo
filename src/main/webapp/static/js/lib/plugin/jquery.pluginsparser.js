/**
 * @author sl.liu	2012.08.11
 * @description	插件解析器
 */
(function($){
	
	$.pluginsparser = {};
	$.extend($.pluginsparser,$.parser,{
		auto:true,
		onComplete : function(context){},
		plugins : ['areatrigger','triggerfield'],//能被解析的插件
		parse:function(context){
			var aa = [];
			for(var i=0; i<$.pluginsparser.plugins.length; i++){
				var name = $.pluginsparser.plugins[i];
				var r = $('.pluginsui-' + name, context);
				if (r.length){
					if (r[name]){
						r[name]();
					} else {
						aa.push({name:name,jq:r});
					}
				}
			}
			if (aa.length && window.easyloader){
				var names = [];
				for(var i=0; i<aa.length; i++){
					names.push(aa[i].name);
				}
				easyloader.load(names, function(){
					for(var i=0; i<aa.length; i++){
						var name = aa[i].name;
						var jq = aa[i].jq;
						jq[name]();
					}
					$.pluginsparser.onComplete.call($.pluginsparser, context);
				});
			} else {
				$.pluginsparser.onComplete.call($.pluginsparser, context);
			}
		}
//		parseOptions : function(target,options){}
	});
	
	$(function(){
		if(!window.easyloader&&$.pluginsparser.auto){
			$.pluginsparser.parse();
		}
	});
})(jQuery);