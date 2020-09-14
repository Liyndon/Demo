//扩展validatebox 
var msg="日期格式输入不正确";
$.extend($.fn.validatebox.defaults.rules,{
	chs: {
		validator: function (value) { 
			return /^[\u0391-\uFFE5]+$/.test(value);
		},
		message: '只能输入汉字'
	},
	numeric:{   
	     validator:function(value,param){   
	        if (value){   
	             return /^[0-9]*(\.[0-9]+)?$/.test(value);   
	        } else {   
	           return true;   
	       }   
	     },   
	     message:'只能输入数字.'  
	},  
	validateOldPassword:{
		validator:function(value,param){   
			if(md5(sha256_digest(value)) == param[0]){
				return true;
			}
			return false;
	    },
	    message:'旧密码输入错误'
	},
	password:{   
	     validator:function(value,param){   
	        if (value){   
	             return (/^(?=[`~!@#\$%\^&*\(\)\-_=\+\\\|\[\]\{\}:;\"\',.<>\/\?\d]*[a-zA-Z]+)(?=[a-zA-Z`~!@#\$%\^&*\(\)\-_=\+\\\|\[\]\{\}:;\"\',.<>\/\?]*\d+)[`~!@#\$%\^&*\(\)\-_=\+\\\|\[\]\{\}:;\"\',.<>\/\?\w]{8,16}$/.test(value));   
	        } else {   
	           return true;   
	        }   
	     },   
	     message:'密码必须同时含字母和数字，8~16位长度.'  
	},
	passwordWithRep:{   
	     validator:function(value,param){   
	        if (value){   
	             if(/^(?=[`~!@#\$%\^&*\(\)\-_=\+\\\|\[\]\{\}:;\"\',.<>\/\?\d]*[a-zA-Z]+)(?=[a-zA-Z`~!@#\$%\^&*\(\)\-_=\+\\\|\[\]\{\}:;\"\',.<>\/\?]*\d+)[`~!@#\$%\^&*\(\)\-_=\+\\\|\[\]\{\}:;\"\',.<>\/\?\w]{8,16}$/.test(value)){
	            	 if(md5(sha256_digest(value)) == param[0]){
	            		 $.fn.validatebox.defaults.rules.passwordWithRep.message = '新密码和旧密码不能相同';
	            		 return false;
	            	 }else{
	            		 return true;
	            	 }
	             }else{
	            	 $.fn.validatebox.defaults.rules.passwordWithRep.message = '密码必须同时含字母和数字，8~16位长度';
	            	 return false;
	             }
	        } else {   
	           return false;   
	        }   
	     },   
	     message:''  
	},
	account: {//登录名验证
		validator: function (value, param) {
			if (value.length < param[3] || value.length > param[4]) {
				$.fn.validatebox.defaults.rules.account.message = '登录名字符长度：' + param[3] + '-' + param[4] + ''; 
				return false;
			} else { 
				if (!/^[\w]+$/.test(value)) {
					$.fn.validatebox.defaults.rules.account.message = '登录名只能数字、字母、下划线组成.'; 
					return false;
				} else{
					var postdata = {};
		            postdata[param[1]] = value;
		            var m_result =$.ajax({ 
		            	type:"POST",     //http请求方式
		                url:param[0],    //服务器段url地址
		                data:postdata,   //发送给服务器段的数据
		                dataType:'json', //告诉JQuery返回的数据格式
		                async:false
		            }).responseText;
		            if (m_result == 'false') {
		                $.fn.validatebox.defaults.rules.account.message = param[2];
		                return false;
		            }else {
		                return true;
		            }
				}
			}
		}, 
		message: ''
	},
    byteLength: {//字节长度，中文两个字节
        validator: function(value, params) {
        	var length = value.length;    
          	for(var i = 0; i < value.length; i++){    
        	   	if(value.charCodeAt(i) > 127){    
        	    	length++;    
        	   	}    
        	}
          	return  (length >= params[0] && length <= params[1] );  
        },
        message: '字符长度{0}-{1}'
    },
    equalTo:{
    	 validator: function(value, param) {
    		 return value == $(param[0]).val();
    	 },
    	 message:'两次输入的字符不一至'
    },
    remoteValid: {
        validator: function(value, param) {
            var postdata = {};
            postdata[param[1]] = value;
            if(param.length >= 4){
            	$.extend(postdata, param[3]);//第4个参数为参数
            }
            var m_result =$.ajax({ 
            	type:"POST",     //http请求方式
                url:param[0],    //服务器段url地址
                data:postdata,   //发送给服务器段的数据
                dataType:'json', //告诉JQuery返回的数据格式
                async:false
            }).responseText;
            if (m_result == 'false') {
                $.fn.validatebox.defaults.rules.remoteValid.message = param[2];
                return false;
            }else {
                return true;
            }
        },
        message:''
    },
    date: {
        validator: function(value, param) {
        	var date=/^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/;
        	return date.test(value);
        },
        message:'日期格式输入不正确'
    }, 
    latterDate: {
        validator: function(value, param) {
        	var date=/^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/;
        	if(date.test(value)){
        		var startTime=$("#" + param).datebox('getValue');
        		if(value<startTime){
        			return false;
        		}
        		return true;
        	}else{
        		return false;
        	}
        },
        message:"格式不正确或小于开始时间"
    },
    notAllow: {
    	validator: function(value, param) {
    		var category = $("#simpling_res").combobox("getValue");
    		//执行验证功能
    		var simpTypeItem = $("#simpling_type").combobox('getValue');
        	if((category=='1'||category=='2'||category=='3')&&(simpTypeItem=='4'||simpTypeItem=='6')){
        		return false;
        	}
        	return true;
        },
        message:"门急诊不能执行转化糖药物抽样和I类切口手术抽样"
	},
	uniqueField: {
		//功能：校验唯一字段
		//value - 是指文本输入框的值
		//param - 参数，多个参数以一个数组返回
		//[0]- 后台ajax提交验证的请求地址URL;
		//[1]- 后台接收数据的参数名;
		//[2]- 验证失败后的提示信息;
		//[3]- 最小长度;
		//[4]- 最大长度; 
		validator: function (value,param) {
			//alert(param);
			if (value.length < param[3] || value.length > param[4]) {
				$.fn.validatebox.defaults.rules.uniqueField.message = '文本内容长度：' + param[3] + '-' + param[4] + ''; 
				return false;
			} else { 
				var postdata = {};
	            postdata[param[1]] = value;
	            var m_result =$.ajax({ 
	            	type:"POST",     //http请求方式
	                url:param[0],    //服务器段url地址
	                data:postdata,   //发送给服务器段的数据
	                dataType:'json', //告诉JQuery返回的数据格式
	                async:false
	            }).responseText;
	            if (m_result == 'false') {
	                $.fn.validatebox.defaults.rules.uniqueField.message = param[2];
	                return false;
	            }else {
	                return true;
	            }
			}
		}, 
		message: ''
	},
	checkUniqueValue: {
		//功能：校验唯一字段
		//value - 是指文本输入框的值
		//param - 参数，多个参数以一个数组返回
		//[0]- 后台ajax提交验证的请求地址URL;
		//[1]- 后台接收数据的参数名;
		//[2]- 验证失败后的提示信息;
		//[3]- 最小长度;
		//[4]- 最大长度; 
		//[5]- 第2个参数的参数名;
		//[6]- 第2个参数的参数值;
		validator: function (value,param) {
			//alert(value);
			//alert(param);
			if (value.length < param[3] || value.length > param[4]) {
				$.fn.validatebox.defaults.rules.checkUniqueValue.message = '文本内容长度：' + param[3] + '-' + param[4] + ''; 
				return false;
			} else { 
				var postdata = {};
	            postdata[param[1]] = value;
	            postdata[param[5]] = param[6];
	            postdata[param[7]] = param[8];
	            var m_result =$.ajax({ 
	            	type:"POST",     //http请求方式
	                url:param[0],    //服务器段url地址
	                data:postdata,   //发送给服务器段的数据
	                dataType:'json', //告诉JQuery返回的数据格式
	                async:false
	            }).responseText;
	            if (m_result == 'false') {
	                $.fn.validatebox.defaults.rules.checkUniqueValue.message = param[2];
	                return false;
	            }else {
	                return true;
	            }
			}
		}, 
		message: ''
	},
	minLength : { // 判断最小长度
		validator : function(value, param) {
			return value.length >= param[0];
		},
		message : '最少输入 {0} 个字符'
	},
	length : { // 长度
		validator : function(value, param) {
			var len = $.trim(value).length;
			return len >= param[0] && len <= param[1];
		},
		message : "输入内容长度必须介于{0}和{1}之间"
	},
	phone : {// 验证电话号码
		validator : function(value) {
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message : '格式不正确,请使用下面格式:020-88888888'
	},
	mobile : {// 验证手机号码
		validator : function(value) {
			return /^(13|15|18)\d{9}$/i.test(value);
		},
		message : '手机号码格式不正确'
	},
	phoneAndMobile : {// 电话号码或手机号码
		validator : function(value) {
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value) || /^(13|15|18)\d{9}$/i.test(value);
		},
		message : '电话号码或手机号码格式不正确'
	},
	idcard : {// 验证身份证
		validator : function(value) {
			return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value) || /^\d{18}(\d{2}[A-Za-z0-9])?$/i.test(value);
		},
		message : '身份证号码格式不正确'
	},
	intOrFloat : {// 验证整数或小数
		validator : function(value) {
			return /^\d+(\.\d+)?$/i.test(value);
		},
		message : '请输入数字，并确保格式正确'
	},
	currency : {// 验证货币
		validator : function(value) {
			return /^\d+(\.\d+)?$/i.test(value);
		},
		message : '货币格式不正确'
	},
	qq : {// 验证QQ,从10000开始
		validator : function(value) {
			return /^[1-9]\d{4,9}$/i.test(value);
		},
		message : 'QQ号码格式不正确'
	},
	integer : {// 验证整数
		validator : function(value) {
			return /^[+]?[1-9]+\d*$/i.test(value);
		},
		message : '请输入整数'
	},
	chinese : {// 验证中文
		validator : function(value) {
			return /^[\u0391-\uFFE5]+$/i.test(value);
		},
		message : '请输入中文'
	},
	chineseAndLength : {// 验证中文及长度
		validator : function(value) {
			var len = $.trim(value).length;
			if (len >= param[0] && len <= param[1]) {
				return /^[\u0391-\uFFE5]+$/i.test(value);
			}
		},
		message : '请输入中文'
	},
	english : {// 验证英语
		validator : function(value) {
			return /^[A-Za-z]+$/i.test(value);
		},
		message : '请输入英文'
	},
	englishAndLength : {// 验证英语及长度
		validator : function(value, param) {
			var len = $.trim(value).length;
			if (len >= param[0] && len <= param[1]) {
				return /^[A-Za-z]+$/i.test(value);
			}
		},
		message : '请输入英文'
	},
	englishUpperCase : {// 验证英语大写
		validator : function(value) {
			return /^[A-Z]+$/.test(value);
		},
		message : '请输入大写英文'
	},
	unnormal : {// 验证是否包含空格和非法字符
		validator : function(value) {
			return /.+/i.test(value);
		},
		message : '输入值不能为空和包含其他非法字符'
	},
	username : {// 验证用户名
		validator : function(value) {
			return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
		},
		message : '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
	},
	faxno : {// 验证传真
		validator : function(value) {
			return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
		},
		message : '传真号码不正确'
	},
	zip : {// 验证邮政编码
		validator : function(value) {
			return /^[1-9]\d{5}$/i.test(value);
		},
		message : '邮政编码格式不正确'
	},
	ip : {// 验证IP地址
		validator : function(value) {
			return /d+.d+.d+.d+/i.test(value);
		},
		message : 'IP地址格式不正确'
	},
	name : {// 验证姓名，可以是中文或英文
		validator : function(value) {
			return /^[\u0391-\uFFE5]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
		},
		message : '请输入姓名'
	},
	engOrChinese : {// 可以是中文或英文
		validator : function(value) {
			return /^[\u0391-\uFFE5]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
		},
		message : '请输入中文'
	},
	engOrChineseAndLength : {// 可以是中文或英文
		validator : function(value) {
			var len = $.trim(value).length;
			if (len >= param[0] && len <= param[1]) {
				return /^[\u0391-\uFFE5]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
			}
		},
		message : '请输入中文或英文'
	},
	carNo : {
		validator : function(value) {
			return /^[\u4E00-\u9FA5][\da-zA-Z]{6}$/.test(value);
		},
		message : '车牌号码无效（例：粤B12350）'
	},
	carenergin : {
		validator : function(value) {
			return /^[a-zA-Z0-9]{16}$/.test(value);
		},
		message : '发动机型号无效(例：FG6H012345654584)'
	},
	same : {
		validator : function(value, param) {
			if ($("#" + param[0]).val() != "" && value != "") {
				return $("#" + param[0]).val() == value;
			} else {
				return true;
			}
		},
		message : '两次输入的密码不一致!'
	}
});