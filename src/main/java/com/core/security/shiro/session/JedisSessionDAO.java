package com.core.security.shiro.session;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.core.util.Global;
import com.core.util.JedisUtils;
import com.core.util.Servlets;
import com.core.util.StringUtils;

/**
 * 自定义授权会话管理类
 * 
 * @author ThinkGem
 * @version 2014-7-20
 */
public class JedisSessionDAO extends AbstractSessionDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String sessionKeyPrefix = "shiro_session_";

	@Override
	public void update(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {
			return;
		}

		HttpServletRequest request = Servlets.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不更新SESSION
			if (Servlets.isStaticFile(uri)) {
				return;
			}
			// 如果是视图文件，则不更新SESSION
			if (StringUtils
					.startsWith(uri, Global.getConfig("web.view.prefix"))
					&& StringUtils.endsWith(uri,
							Global.getConfig("web.view.suffix"))) {
				return;
			}
			// 手动控制不更新SESSION
			if (Global.NO.equals(request.getParameter("updateSession"))) {
				return;
			}
		}

		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			// 获取登录者编号
			PrincipalCollection pc = (PrincipalCollection) session
					.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			String principalId = pc != null ? pc.getPrimaryPrincipal()
					.toString() : StringUtils.EMPTY;

			jedis.hset(sessionKeyPrefix, session.getId().toString(),
					principalId + "|" + session.getTimeout() + "|"
							+ session.getLastAccessTime().getTime());
			jedis.set(
					JedisUtils.getBytesKey(sessionKeyPrefix + session.getId()),
					JedisUtils.toBytes(session));

			// 设置超期时间
			int timeoutSeconds = (int) (session.getTimeout() / 1000);
			jedis.expire((sessionKeyPrefix + session.getId()), timeoutSeconds);

			logger.debug("update {} {}", session.getId(),
					request != null ? request.getRequestURI() : "");
		} catch (Exception e) {
			logger.error("update {} {}", session.getId(),
					request != null ? request.getRequestURI() : "", e);
		} finally {
			JedisUtils.returnResource(jedis);
		}
	}

	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			return;
		}
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();

			jedis.hdel(JedisUtils.getBytesKey(sessionKeyPrefix),
					JedisUtils.getBytesKey(session.getId().toString()));
			jedis.del(JedisUtils.getBytesKey(sessionKeyPrefix + session.getId()));

			logger.debug("delete {} ", session.getId());
		} catch (Exception e) {
			logger.error("delete {} ", session.getId(), e);
		} finally {
			JedisUtils.returnResource(jedis);
		}
	}

	@Override
	protected Serializable doCreate(Session session) {
		HttpServletRequest request = Servlets.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不创建SESSION
			if (Servlets.isStaticFile(uri)) {
				return null;
			}
		}
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.update(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session s = null;
		HttpServletRequest request = Servlets.getRequest();
		if (request != null) {
			String uri = request.getServletPath();
			// 如果是静态文件，则不获取SESSION
			if (Servlets.isStaticFile(uri)) {
				return null;
			}
			s = (Session) request.getAttribute("session_" + sessionId);
		}
		if (s != null) {
			return s;
		}

		Session session = null;
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			// if (jedis.exists(sessionKeyPrefix + sessionId)){
			session = (Session) JedisUtils.toObject(jedis.get(JedisUtils
					.getBytesKey(sessionKeyPrefix + sessionId)));
			// }
			logger.debug("doReadSession {} {}", sessionId,
					request != null ? request.getRequestURI() : "");
		} catch (Exception e) {
			logger.error("doReadSession {} {}", sessionId,
					request != null ? request.getRequestURI() : "", e);
		} finally {
			JedisUtils.returnResource(jedis);
		}

		if (request != null && session != null) {
			request.setAttribute("session_" + sessionId, session);
		}

		return session;
	}

	@Override
	public Session readSession(Serializable sessionId)
			throws UnknownSessionException {
		try {
			return super.readSession(sessionId);
		} catch (UnknownSessionException e) {
			return null;
		}
	}

	@Override
	public Collection<Session> getActiveSessions() {
		return null;
	}

}
