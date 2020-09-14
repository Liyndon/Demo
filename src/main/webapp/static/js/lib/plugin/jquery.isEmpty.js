/**
 * @author sl.liu
 * @description  jQuery没有判断对象是否为空的插件
 * Returns true if the passed value is empty.
	The value is deemed to be empty if it is
	null
	undefined
	an empty array
	a zero length string (Unless the allowBlank parameter is true)
	Parameters:
	value : Mixed
	The value to test
	allowBlank : Boolean
	(optional) true to allow empty strings (defaults to false)
	Returns:
	Boolean
 */


(function($){
	$.isEmpty = function(v, allowBlank){
        return v === null || v === undefined || (($.isArray(v) && !v.length)) || (!allowBlank ? v === '' : false);
    };
})
(jQuery);


