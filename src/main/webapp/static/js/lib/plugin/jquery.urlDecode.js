/**
 * @author zhangqiang
 * @description  jQuery只提供了将参数进行编码，JavaScript标准编码函数为encodeURIComponent，jQuery1.81源代码 7065行，现在补充解码的函数
 * 主要是在表单将表单中的参数先调用jQuery的serialize的方法将表单中的值编码，再调用我的插件，解码，用于datagrid的查询中，datagrid不能接受
 * 编码后的参数
 * overwrite是解码后覆盖的值
 */


(function($){
	$.urlDecode = function(string, overwrite){
        if($.isEmpty(string)){
            return {};
        }
        var obj = {},
            pairs = string.split('&'),
            d = decodeURIComponent,
            name,
            value;
        $.each(pairs, function(i,pair) {
            pair = pair.split('=');
            name = d(pair[0]);
            value = d(pair[1]);
            obj[name] = overwrite || !obj[name] ? value :
                        [].concat(obj[name]).concat(value);
        });
        return obj;
    };
})(jQuery);


