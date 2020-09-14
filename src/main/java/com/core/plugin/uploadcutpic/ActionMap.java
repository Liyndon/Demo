package com.core.plugin.uploadcutpic;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: ActionMap 
 * @Description: action地图
 * @author zq_ja.Zhang
 * @date 2015-11-23 下午6:32:14 
 *
 */
public class ActionMap {

	public static final Map<String, Integer> mapping;
	// 获取配置请求
	public static final int UPLOAD_IMAGE = 1;
	public static final int IMAGE_CUT = 2;
	
	static {
		mapping = new HashMap<String, Integer>(){			    
		private static final long serialVersionUID = -468090088187720581L;
		{
			put( "uploadimage", ActionMap.UPLOAD_IMAGE );
			put( "imagecut", ActionMap.IMAGE_CUT );
		}};
	}
	
	public static int getType ( String key ) {
		return ActionMap.mapping.get( key );
	}
}
