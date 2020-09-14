package com.core.exception;

/**
 * ServiceException : 封装业务层发生的异常
 *
 * @author StarZou
 * @since 2014-09-27 18:09
 */
public class ServiceException extends UserException {

	public ServiceException(String msg) {
		super(msg);
	}

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	

}
