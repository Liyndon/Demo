package com.core.plugin.uploadcutpic;

/**
 * @ClassName: State 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zq_ja.Zhang
 * @date 2015-11-23 下午5:15:15 
 *
 */
public interface State {
	public boolean isSuccess ();
	
	public void putInfo( String name, String val );
	
	public void putInfo ( String name, long val );
	
	public String toJSONString ();
}
