package com.core.security.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class MySessionListener implements SessionListener {

	@Override
	public void onExpiration(Session arg0) {
		System.out.println(""+arg0.getId()+" has timeout");
	}

	@Override
	public void onStart(Session arg0) {
		System.out.println("start...."+arg0.getId());
	}

	@Override
	public void onStop(Session arg0) {
		// TODO Auto-generated method stub
		
	}  
    
}  