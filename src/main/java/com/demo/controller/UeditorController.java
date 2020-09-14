package com.demo.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.ueditor.ActionEnter;


/**
 * 
 * @ClassName: UeditorController
 * @Description: ueditor的控制器
 * @author zq_ja.Zhang
 * @date 2015-10-8 下午1:37:36
 * 
 */
@Controller
@RequestMapping(value = "/ueditor")
public class UeditorController {
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param action
	 * @return
	 */
	@RequestMapping("/dispatch")
	@ResponseBody
	public String config(HttpServletRequest request,
			HttpServletResponse response, String action) {
		response.setHeader("Content-Type" , "text/html");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String exec = new ActionEnter(request, rootPath).exec();
		return exec;
	}
	
	/**
	 * 
	* @Title: delete
	* @Description: 删除上传的文件
	* @param @param request
	* @param @param response
	* @param @param action
	* @param @return    设定文件
	* @return String    返回类型
	* @author lxy
	* @throws
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(HttpServletRequest request,
			HttpServletResponse response,String action){
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String realPath = rootPath+request.getParameter("fileUrl");
		File file = new File(realPath);
		if(file.exists()){
			file.delete();
		}else{
			return "file don't exists..";
		}
		return "SUCCESS";
	}
	
}
