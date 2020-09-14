//常用的extjs vtype校验
Ext.form.field.VTypes["hostnameVal1"] = /^[a-zA-Z][-.a-zA-Z0-9]{0,254}$/;
Ext.form.field.VTypes["hostnameVal2"] = /^[a-zA-Z]([-a-zA-Z0-9]{0,61}[a-zA-Z0-9]){0,1}([.][a-zA-Z]([-a-zA-Z0-9]{0,61}[a-zA-Z0-9]){0,1}){0,}$/;
Ext.form.field.VTypes["ipVal"] = /^([1-9][0-9]{0,1}|1[013-9][0-9]|12[0-689]|2[01][0-9]|22[0-3])([.]([1-9]{0,1}[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])){2}[.]([1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-4])$/;
Ext.form.field.VTypes["netmaskVal"] = /^(128|192|224|24[08]|25[245].0.0.0)|(255.(0|128|192|224|24[08]|25[245]).0.0)|(255.255.(0|128|192|224|24[08]|25[245]).0)|(255.255.255.(0|128|192|224|24[08]|252))$/;
Ext.form.field.VTypes["portVal"] = /^(0|[1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$/;
Ext.form.field.VTypes["multicastVal"] = /^((22[5-9]|23[0-9])([.](0|[1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])){3})|(224[.]([1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.](0|[1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])){2})|(224[.]0[.]([1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.](0|[1-9][0-9]{0,1}|1[0-9]{2}|2[0-4][0-9]|25[0-5])))$/;
Ext.form.field.VTypes["usernameVal"] = /^[a-zA-Z][-_.a-zA-Z0-9]{7,15}$/;
Ext.form.field.VTypes["passwordVal"] = /^(?=[`~!@#\$%\^&*\(\)\-_=\+\\\|\[\]\{\}:;\"\',.<>\/\?\d]*[a-zA-Z]+)(?=[a-zA-Z`~!@#\$%\^&*\(\)\-_=\+\\\|\[\]\{\}:;\"\',.<>\/\?]*\d+)[`~!@#\$%\^&*\(\)\-_=\+\\\|\[\]\{\}:;\"\',.<>\/\?\w]{8,16}$/; 

Ext.form.field.VTypes["hostname"] = function(v) {
	if (!Ext.form.field.VTypes["hostnameVal1"].test(v)) {
		Ext.form.field.VTypes["hostnameText"] = "必须以字母开头，不超过255个字符";
		return false;
	}
	Ext.form.field.VTypes["hostnameText"] = "字母开头，以字母或数字结束，不超过63个字符";
	return Ext.form.field.VTypes["hostnameVal2"].test(v);
};

Ext.form.field.VTypes["hostnameText"] = "无效的主机名";
Ext.form.field.VTypes["hostnameMask"] = /[-.a-zA-Z0-9]/;
Ext.form.field.VTypes["ip"] = function(v) {
	return Ext.form.field.VTypes["ipVal"].test(v);
};
Ext.form.field.VTypes["ipText"] = "1.0.0.1 - 223.255.255.254 不包括 127.x.x.x";
Ext.form.field.VTypes["ipMask"] = /[.0-9]/;
Ext.form.field.VTypes["netmask"] = function(v) {
	return Ext.form.field.VTypes["netmaskVal"].test(v);
};
Ext.form.field.VTypes["netmaskText"] = "128.0.0.0 - 255.255.255.252";
Ext.form.field.VTypes["netmaskMask"] = /[.0-9]/;
Ext.form.field.VTypes["port"] = function(v) {
	return Ext.form.field.VTypes["portVal"].test(v);
};
Ext.form.field.VTypes["portText"] = "0 - 65535";
Ext.form.field.VTypes["portMask"] = /[0-9]/;
Ext.form.field.VTypes["multicast"] = function(v) {
	return Ext.form.field.VTypes["multicastVal"].test(v);
};
Ext.form.field.VTypes["multicastText"] = "224.0.1.0 - 239.255.255.255";
Ext.form.field.VTypes["multicastMask"] = /[.0-9]/;
Ext.form.field.VTypes["username"] = function(v) {
	return Ext.form.field.VTypes["usernameVal"].test(v);
};
Ext.form.field.VTypes["usernameText"] = "字母开头，6~16位长度";

/**
 * 用户密码验证扩展
 */
Ext.form.field.VTypes["password"] = function(v) {
	Ext.form.field.VTypes["passwordText"] = "密码必须含字母和和数字，8~16位长度";
	return Ext.form.field.VTypes["passwordVal"].test(v);
};
Ext.form.field.VTypes["passwordText"] = "Invalid Password";

/**
 * 用户新密码+重复验证
 */
Ext.form.field.VTypes["passwordWithrep"] = function(v, f) {
	if(!f.currentValue){
		Ext.MessageBox.show({  
            title: '错误',  
            msg: '发生异常错误，组件未指定currentValue属性',  
            icon: Ext.Msg.ERROR,  
            buttons: Ext.Msg.OK  
        });  
        return false;  
	}
	if(Ext.form.field.VTypes["password"](v)){
		if(md5(sha256_digest(Ext.util.Format.trim(v))) != f.currentValue){
			return true;
		}
		Ext.form.field.VTypes["passwordWithrepText"] = "新密码不能和原密码相同";
		return false;
	}else{
		Ext.form.field.VTypes["passwordWithrepText"] = "密码必须含字母和和数字，8~16位长度";
	}
	return false;
};

/**
 * 用户密码输入确认验证
 */
Ext.form.field.VTypes["passwordConfrim"] = function(v, f) {
	if (f.initialPassField) {
        var pwd = f.up('form').down('#' + f.initialPassField);
        Ext.form.field.VTypes["passwordConfrimText"] = "两次密码输入不一致";
        return (v == pwd.getValue());
    }else{
    	Ext.MessageBox.show({  
            title: '错误',  
            msg: '发生异常错误，组件未指定initialPassField属性',  
            icon: Ext.Msg.ERROR,  
            buttons: Ext.Msg.OK  
        });  
        return false; 
    }
    return false;
};


/**
 * 当前输入密码验证
 */
Ext.form.field.VTypes["checkCurrentPwd"] = function(v, f){
	if(!f.currentValue){
		Ext.MessageBox.show({  
            title: '错误',  
            msg: '发生异常错误，组件未指定currentValue属性',  
            icon: Ext.Msg.ERROR,  
            buttons: Ext.Msg.OK  
        });  
        return false;  
	}
	if(md5(sha256_digest(Ext.util.Format.trim(v))) == f.currentValue){
		return true;
	}
	Ext.form.field.VTypes["checkCurrentPwdText"] = "当前密码输入错误";
	return false;
};

