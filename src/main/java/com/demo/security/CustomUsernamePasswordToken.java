package com.demo.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CustomUsernamePasswordToken extends UsernamePasswordToken {

	/**   
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）   
	 *   
	 * @since Ver 1.1   
	 */   
	    
	private static final long serialVersionUID = 1L;
	// 用于存储用户输入的校验码
	private String validCode;
	//用户登录类型，admin=管理员后台登录，null=前台登录
	private String validateType;
	
	private boolean autoLogin = false;

	public String getValidateType() {
		return validateType;
	}

	public void setValidateType(String validateType) {
		this.validateType = validateType;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public CustomUsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String validCode, String validateType) {
		// 调用父类的构造函数
		super(username, password, rememberMe, host);
		this.validCode = validCode;
		this.validateType = validateType;
	}
	
	//不需要帐户密码登录
	public CustomUsernamePasswordToken(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
}
