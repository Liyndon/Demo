package com.demo.listener;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;


public class WebContextListener extends org.springframework.web.context.ContextLoaderListener {
	
	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		System.out.println("WebContextListener-----");
		return super.initWebApplicationContext(servletContext);
	}
}
