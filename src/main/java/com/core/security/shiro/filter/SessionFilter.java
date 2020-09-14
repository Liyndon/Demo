package com.core.security.shiro.filter;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;


public class SessionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest; 
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		
		try{
			SecurityUtils.getSecurityManager();
		}catch(UnavailableSecurityManagerException e){
			String requestUri = request.getRequestURI();
			String contextPath = request.getContextPath();
			response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
			if(requestUri.startsWith(contextPath+"/index")){//访问首页
				if (request.getHeader("x-requested-with") != null    
						&& request.getHeader("x-requested-with").equalsIgnoreCase(//ajax超时处理     
								"XMLHttpRequest")) {     
					response.addHeader("sessionstatus", "timeout"); 
					PrintWriter out = response.getWriter(); 
					out.print("&nbsp;");
					out.close();
				}
	        }
		}
		
		try {
			chain.doFilter(request, response);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		
	}
}
