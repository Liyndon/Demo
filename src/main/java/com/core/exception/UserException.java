package com.core.exception;

/**
 * UserException : 用户自定义异常
 *
 * @author StarZou
 * @since 2014-07-27 18:12
 */
public class UserException extends RuntimeException {

    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	/**
     * 异常发生时间
     */
	
	
    private long date = System.currentTimeMillis();

	public UserException(String msg) {
		super(msg);
	}


	public long getDate() {
        return date;
    }
}
