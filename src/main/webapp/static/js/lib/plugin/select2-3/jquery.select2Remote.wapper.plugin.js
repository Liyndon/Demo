/**
 * 包装select2，使用select2版本为3.5.4
 * @author zq_ja
 */
$.fn.select2Remote = function(options){  
    var opts = $.extend({},$.fn.select2Remote.defaults, options);
    var options = {};
    options.allowClear = opts.allowClear;
    options.multiple = opts.multiple;
    options.placeholder = opts.blankMsg;
    options.type = opts.type;
    options.val = opts.val;
    options.url = opts.url;
    options.minimumInputLength = opts.minLength;
    options.id = function(obj){return obj[opts.valueField];};
    options.text = (opts.groupField == "" ? function(obj){return obj[opts.textField];} : function(obj){return obj[opts.textField] || obj[opts.groupField];});
    options.ajax = {
		url: opts.url,  
        dataType: 'json',  
        quietMillis: opts.delay ,  
        data: opts.ajaxQuery,
        type:opts.type,
        results: function (data, page) { return {results: data};}  	
    };
    options.showSearch = opts.showSearch;
    options.escapeMarkup = function (m) { return m; } ;
    options.dropdownCssClass = 'bigdrop';
    options.multiple = opts.multiple;
    if(opts.formatSelection){
    	options.formatSelection = opts.formatSelection;
    }
    if(opts.formatResult){
    	options.formatResult = opts.formatResult;
    }
    var select = this.select2(options);
    if(options.val != null && options.val != ""){
    	$.ajax({
    		url:options.url,
    		data:opts.ajaxQuery.call(),
    		dataType:'json',
    		type:opts.type,
    		success:function(list){
    			var setValue = {};
    			setValue[opts.textField] = "数据不正确";
    			if(opts.groupField == "" || opts.groupField == null){
    				if(list.length > 0 ){
        				for(var i=0; i<list.length; i++){
        					if(list[i][opts.valueField] == options.val){
        						setValue = list[i];                              
        						break;
        					}
        				}
        			}
    			}else{
    				if(list.length > 0 ){
        				for(var i=0; i<list.length; i++){
        					var children = list[i].children;
        					for(var j=0; j<children.length; j++){
        						
        						if(children[j][opts.valueField] == options.val){
        							setValue = children[j];
            						break;
        						}
        					}
        				}
        			}
    			}
    			select.select2("data",setValue);
    		}
    	});
    }
    return select;
}; 
$.fn.select2Remote.defaults = {  
    blankMsg: "请选择", 
    allowClear:false,
    multiple:false,
    minLength: 0,  
    url:'',  
    delay:0,  
    valueField:'id',  
    textField:'text',
    groupField:'',
    type:"GET",
    val:null,
    showSearch:true,
    ajaxQuery:function (term, page) {return {query: term};}
};