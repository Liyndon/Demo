package com.core.plugin.uploadcutpic;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class Uploader {
	private HttpServletRequest request = null;
	private Properties conf = null;
	private Map<String, Object> param = null;

	public Uploader(HttpServletRequest request, Properties conf, Map<String, Object> param) {
		this.request = request;
		this.conf = conf;
		this.param = param;
	}

	public final State doExec() {
		String filedName = (String) this.conf.get("filename");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf, param);
		} else {
			state = BinaryUploader.save(this.request, this.conf, param);
		}
		return state;
	}
}
