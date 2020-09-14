package com.demo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.plugin.uploadcutpic.BeanHelper;
import com.core.plugin.uploadcutpic.CuterParam;
import com.core.plugin.uploadcutpic.UpLoadActionHandler;




/**
 * 
 * @ClassName: PicCutUploaderController 
 * @Description: 头像上传
 * @author zq_ja.Zhang
 * @date 2015-11-23 下午6:39:37 
 *
 */
@Controller
@RequestMapping("/picCutUploader")
public class PicCutUploaderController {
	
	@RequestMapping(value="/dispatch", produces= "text/plain;charset=UTF-8")
	@ResponseBody
	public String config(HttpServletRequest request, HttpServletResponse response, CuterParam cuterParam) {
		response.setHeader("Content-Type" , "text/html");
		Map<String, Object> param = BeanHelper.transBean2Map(new Object());
		String exec = new UpLoadActionHandler(request, cuterParam, param).excuteCmd();
		System.out.println(exec+"--------");
		return exec;
	}


	
	
}
