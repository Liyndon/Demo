//扩展startwith
String.prototype.startWith = function(str) {
	var reg = new RegExp("^" + str);
	return reg.test(this);
};
$.fn.serializeObject = function() {//序列化表单
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

/**
 * 时间对象的格式化
 */
Date.prototype.format = function(format) {
	/*
	 * format="yyyy-MM-dd hh:mm:ss";
	 */
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	};
	return format;
};

Array.prototype.remove=function(dx){ 
    if(isNaN(dx)||dx>this.length){return false;} 
    for(var i=0,n=0;i<this.length;i++) 
    { 
        if(this[i]!=this[dx]) 
        { 
            this[n++]=this[i]; 
        } 
    } 
    this.length-=1; 
}; 

$.ajaxSetup({
	timeout : 60000,// 过期时间60s
	type : 'post',
	error : function(xhr, status, e) {
		$.messager.alert("错误", "<br/>ajax请求失败，<br>错误代码：" + xhr.status
				+ "；错误内容：" + xhr.statusText, "error");
	},
	complete:function(XMLHttpRequest,textStatus){
		var sessionStatus = XMLHttpRequest.getResponseHeader('sessionstatus');
        if(sessionStatus=="timeout"){
//             $.messager.alert('提示信息', "<br/>登陆超时！请重新登陆！", 'info',function(){
//             });
             window.location.href = ctx+'/';
        }
  }
});// ajax请求定义全局超时时长3s

$.extend($.fn.panel.defaults, {
	loadingMessage : '数据加载中...'
});


// 屏蔽右键
$("body").bind("contextmenu", function(e) {
	return false;
});

$.ns("common.util.fn");
common.util.fn = {
	$dialogEl : $("<div/>"),
	zebra : function(table){//表格设置斑马纹
		var start = 0;
		$("#"+table).attr("cellspacing", "0").attr("cellpadding", "5").find("tr").each(function(index, ele){
			if($.trim($(ele).html()) != ""){
				start++;
				if(start % 2 > 0){
					$(ele).addClass("zebra");
				}
			}
		});
	},
	clearForm : function(formId) {
		$(formId).form("reset");
	},

	submitForm : function(formId, params) {
		$(formId).submit(params);
	},

	// 绑定回车事件
	bindEnterKey : function(target, bindFn, params) {
		target.bind("keydown", function(e) {
			if (e.keyCode == "13") {
				bindFn.call(this, params);
			}
		});
	},

	/**
	 * 对easyui dialog 封装
	 */
	dialog : function(options) {
		var opts = $.extend({
			modal : true,
			onClose : function() {
				$(this).dialog('destroy');
			}
		}, options);
		return common.util.fn.$dialogEl.dialog(opts);
	},
	
	dateFormatter: function(val, row, index, fp){//格式化日期
		if(val == '' || val == null){
			return "";
		}
		return new Date(val).format(fp.dateType);
	},
	
	dictFormatter: function(val, row, index, fp){//利用字典格式化(通过ajax 获取服务器字典)
		var uuid = Math.uuid();
		$.ajax({
			url:ctx+'/dictManager/formatterDict',
			data:{dictCode : fp.dictCode, detailCode : val},
			type:'post',
			cache:true, 
			success:function(res){
				$("#"+uuid).html(res);
			}
		});
		return "<span id='"+uuid+"'></span>";
	},
	
	resultAlert: function(msg, type){
		switch(type){
			case 1: type = "info"; break;
			case 2: return "warning"; break;
			case 3: return "error"; break;
		}
		$.messager.alert("提示", "<br>"+msg, type);
	},
	
	submitProgress: function(){
		$.messager.progress({
			text:'数据提交中，请等待...',
			interval:100
		});
	},
	
	submitProgressClose: function(){
		$.messager.progress("close");
	},
	waitProgress: function(){
		$.messager.progress({
			text:'数据加载中，请等待...',
			interval:100
		});
	},
	
	waitProgressClose: function(){
		$.messager.progress("close");
	}
	
};
