package com.core.plugin.uploadcutpic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
/**
 * @ClassName: UpLoadActionHandler 
 * @Description: 头像上传Action处理 
 * @author zq_ja.Zhang
 * @date 2015-11-23 下午6:29:24 
 *
 */
public class UpLoadActionHandler {
	private HttpServletRequest request = null;//request
	private String actionType = null;//action类型
	private CuterParam cuterParam = null;//剪切参数
	private Map<String, Object> param = null;//剪切参数
	private String propertiesName = "";//参数配置文件名称
	
	public UpLoadActionHandler ( HttpServletRequest request, CuterParam cuterParam, Map<String, Object> param) {
		this.request = request;
		this.actionType = request.getParameter( "action" );
		this.propertiesName = request.getParameter("configer");
		this.cuterParam = cuterParam;
		this.param = param;
	}
	
	public String excuteCmd(){
		if ( actionType == null || !ActionMap.mapping.containsKey( actionType ) ) {
			return new BaseState( false, AppInfo.INVALID_ACTION ).toJSONString();
		}
		State state = null;
		int actionCode = ActionMap.getType( this.actionType );
		Properties conf = null;
		try {
			conf = getProperties(propertiesName == null ? "piccutuploader" : propertiesName);//读取配置文件
			conf.put("rootPath", request.getSession().getServletContext().getRealPath("/"));
		} catch (IOException e) {
			return new BaseState( false, AppInfo.CONFIG_ERROR ).toJSONString();
		}
		switch ( actionCode ) {
			case ActionMap.UPLOAD_IMAGE:
				state = new Uploader(request, conf, param).doExec();
				break;
			case ActionMap.IMAGE_CUT:
				state = new Cuter(conf, cuterParam, param).doExec();
				break;
		}
		return state.toJSONString();
	}
	/**
	 * @Title: getProperties 
	 * @Description: 读取配置文件
	 * @param @param url
	 * @param @return
	 * @param @throws IOException    设定文件 
	 * @return Properties    返回类型 
	 * @throws
	 */
	public static Properties getProperties(String url) throws IOException {
		InputStream in = UpLoadActionHandler.class.getResourceAsStream("/"+url+".properties");
		Properties p = new Properties();
		p.load(in);
		return p;
	}
}
