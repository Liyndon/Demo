
package com.core.security.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2014-7-24
 */
public class CacheSessionDAO extends EnterpriseCacheSessionDAO implements SessionDAO {

	@Override
	protected Serializable doCreate(Session session) {
		// TODO Auto-generated method stub
		return super.doCreate(session);
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		// TODO Auto-generated method stub
		return super.doReadSession(sessionId);
	}

	@Override
	protected void doUpdate(Session session) {
		// TODO Auto-generated method stub
		super.doUpdate(session);
	}

	@Override
	protected void doDelete(Session session) {
		// TODO Auto-generated method stub
		super.doDelete(session);
	}

	@Override
	public SessionIdGenerator getSessionIdGenerator() {
		// TODO Auto-generated method stub
		return super.getSessionIdGenerator();
	}

	@Override
	public void setSessionIdGenerator(SessionIdGenerator sessionIdGenerator) {
		// TODO Auto-generated method stub
		super.setSessionIdGenerator(sessionIdGenerator);
	}

	private Logger logger = LoggerFactory.getLogger(getClass());
	
}
