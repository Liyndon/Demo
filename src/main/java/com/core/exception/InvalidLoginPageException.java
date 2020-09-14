package com.core.exception;

import org.apache.shiro.authc.AuthenticationException;

public class InvalidLoginPageException extends AuthenticationException {

    /**   
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）   
	 *   
	 * @since Ver 1.1   
	 */   
	    
	private static final long serialVersionUID = 1L;

	public InvalidLoginPageException(String msg){
        super(msg);
    }
}
