package com.core.security.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

/**
 * 自定义WEB会话管理类
 */
public class SessionManager extends DefaultWebSessionManager {

	@Override
	public Cookie getSessionIdCookie() {
		// TODO Auto-generated method stub
		return super.getSessionIdCookie();
	}

	@Override
	public void setSessionIdCookie(Cookie sessionIdCookie) {
		// TODO Auto-generated method stub
		super.setSessionIdCookie(sessionIdCookie);
	}

	@Override
	public boolean isSessionIdCookieEnabled() {
		// TODO Auto-generated method stub
		return super.isSessionIdCookieEnabled();
	}

	@Override
	public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled) {
		// TODO Auto-generated method stub
		super.setSessionIdCookieEnabled(sessionIdCookieEnabled);
	}

	@Override
	protected void onStart(Session session, SessionContext context) {
		// TODO Auto-generated method stub
		super.onStart(session, context);
	}

	@Override
	public Serializable getSessionId(SessionKey key) {
		// TODO Auto-generated method stub
		return super.getSessionId(key);
	}

	@Override
	protected void onExpiration(Session s, ExpiredSessionException ese,
			SessionKey key) {
		// TODO Auto-generated method stub
		super.onExpiration(s, ese, key);
	}

	@Override
	protected void onInvalidation(Session session, InvalidSessionException ise,
			SessionKey key) {
		// TODO Auto-generated method stub
		super.onInvalidation(session, ise, key);
	}

	@Override
	protected void onStop(Session session, SessionKey key) {
		// TODO Auto-generated method stub
		super.onStop(session, key);
	}

	@Override
	public boolean isServletContainerSessions() {
		// TODO Auto-generated method stub
		return super.isServletContainerSessions();
	}

}