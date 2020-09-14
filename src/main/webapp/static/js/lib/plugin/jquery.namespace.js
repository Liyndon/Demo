/**
 * @author Johnathan.zhang
 * @description 命名空间函数，做开发时，在自己的功能函数前加上命名空间，防止函数重名，同时按照面向对象的方式，代码可读性增强
 * ns是namespace的简写，推荐使用ns的方式，代码简练
 */


(function($){
	$.namespace = function(){
	    var o, d;
        $.each(arguments, function(i, v) {
            d = v.split(".");
            o = window[d[0]] = window[d[0]] || {};
            $.each(d.slice(1), function(i2, v2){
                o = o[v2] = o[v2] || {};
            });
        });
        return o;
	};
    $.ns = $.namespace;
})
(jQuery);