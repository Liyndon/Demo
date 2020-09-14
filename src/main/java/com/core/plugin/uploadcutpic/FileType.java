package com.core.plugin.uploadcutpic;

import java.util.HashMap;
import java.util.Map;

public class FileType {

	public static final String JPG = "JPG";

	private static final Map<String, String> types = new HashMap<String, String>() {
		/**   
		 * serialVersionUID:TODO（用一句话描述这个变量表示什么）   
		 *   
		 * @since Ver 1.1   
		 */   
		    
		private static final long serialVersionUID = 1L;

		{

			put(FileType.JPG, ".jpg");

		}
	};

	public static String getSuffix(String key) {
		return FileType.types.get(key);
	}

	/**
	 * 根据给定的文件名,获取其后缀信息
	 * 
	 * @param filename
	 * @return
	 */
	public static String getSuffixByFilename(String filename) {

		return filename.substring(filename.lastIndexOf(".")).toLowerCase();

	}

}
