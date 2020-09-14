package com.core.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @ClassName: ValidCodeException 
 * @Description: 分页创建异常
 * @author zq_ja.Zhang
 * @date 2015-12-16 下午6:03:33 
 *
 */
public class ValidCodeException extends AuthenticationException {

    /**   
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）   
	 *   
	 * @since Ver 1.1   
	 */   
	    
	private static final long serialVersionUID = 1L;

	public ValidCodeException(String msg){
        super(msg);
    }
}
