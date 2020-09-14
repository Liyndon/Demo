package com.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {
	/**
	* @Title: parse1
	* @Description: 强制转换成map
	* @param @param path
	* @param @return    设定文件
	* @return Map<String,String>    返回类型
	* @author lxy
	* @throws
	 */
	public static Map<String, String> parse1(String path) {
		Map<String, String> res = null;
		InputStream is = PropertiesUtil.class.getResourceAsStream(path);
		Properties properties = new Properties();
		try {
			try {
				properties.load(is);
				res = new HashMap<String, String>((Map) properties);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	/**
	 * 
	* @Title: parse2
	* @Description: 循环遍历
	* @param @param path
	* @param @return    设定文件
	* @return Map<String,String>    返回类型
	* @author lxy
	* @throws
	 */
	public static Map<String, String> parse2(String path){
		Map<String, String> res = new HashMap<>();
		InputStream is = PropertiesUtil.class.getResourceAsStream(path);
		Properties properties = new Properties();
		try {
			try {
				properties.load(is);
				String key="";
				String value="";
				for(Object name:properties.keySet()){
					key=name.toString().trim();
					value=properties.getProperty(key).toString().trim();
					res.put(key,value);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
}
