package com.demo.security;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import com.core.security.shiro.PasswordHelper;

/**
 * 
 * @ClassName: RetryLimitSimpleCredentialsMatcher
 * @Description: 自定义密码验证
 * @author Johnathan.Zhang
 * @date 2015-7-13 上午11:21:54
 * 
 */
public class RetryLimitSimpleCredentialsMatcher extends
		SimpleCredentialsMatcher {
	// 允许连续输入错误次数
	private int retryNum = 4;

	private AtomicInteger lockTime = new AtomicInteger(60);

	private Cache<String, AtomicInteger> passwordRetryCache;

	private PasswordHelper passwordHelper;

	/*
	 * @Resource private UserService userService;
	 */

	public RetryLimitSimpleCredentialsMatcher(CacheManager cacheManager) {
		passwordRetryCache = cacheManager.getCache("passwordRetryCache");
	}

	public void setPasswordHelper(PasswordHelper passwordHelper) {
		this.passwordHelper = passwordHelper;
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		CustomUsernamePasswordToken login_token = (CustomUsernamePasswordToken) token;
		if (login_token.isAutoLogin()) {// 自动登录了
			return true;
		}
		final String username = (String) token.getPrincipal();
		AtomicInteger retryCount = passwordRetryCache.get(username);
		if (retryCount == null) {
			retryCount = new AtomicInteger(0);
			passwordRetryCache.put(username, retryCount);
		}
		//登录密码
		String user_input_login_pass = passwordHelper.encryptPassword(String
				.valueOf(login_token.getPassword()));
		//数据库密码
		Object db_login_password = getCredentials(info);
		//判断密码出错次数
		if (passwordRetryCache.get(username + "-lock-time") != null) {
			throw new ExcessiveAttemptsException("连续错误" + retryNum + "次，请"
					+ (passwordRetryCache.get(username + "-lock-time").get())
					+ "秒钟后再试或者找回密码");
		}
		// 将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
		boolean matches = super
				.equals(user_input_login_pass, db_login_password);
		if (matches) {
			// clear retry count
			passwordRetryCache.remove(username);
			/*
			SecurityUtils
					.getSubject()
					.getSession()
					.setAttribute("subject",
							userService.getUserByUserName(username));*/// 存放安全user
		} else {
			String msg = "用户名或密码错误";
			if (retryCount.incrementAndGet() == retryNum) {
				Timer timer = new Timer();
				passwordRetryCache.put(username + "-lock-time", lockTime);
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						int relay = passwordRetryCache.get(
								username + "-lock-time").decrementAndGet();
						passwordRetryCache.put(username + "-lock-time",
								new AtomicInteger(relay));
						System.out.println(relay);
						if (relay <= 0) {
							passwordRetryCache.remove(username);
							passwordRetryCache.remove(username + "-lock-time");
							this.cancel();
						}
					}
				}, 2000, 1000);
				throw new ExcessiveAttemptsException(
						"连续错误"
								+ retryNum
								+ "次，请"
								+ (passwordRetryCache.get(username
										+ "-lock-time").get()) + "秒钟后再试或者找回密码");
			} else if (retryCount.get() > retryNum) {
				throw new ExcessiveAttemptsException(
						"连续错误"
								+ retryNum
								+ "次，请"
								+ (passwordRetryCache.get(username
										+ "-lock-time").get()) + "秒钟后再试或者找回密码");
			}
			;
			if (retryCount.get() >= 2) {
				msg += "，你还有" + (retryNum - retryCount.get()) + "次机会";
			}
			throw new IncorrectCredentialsException(msg);
		}
		return matches;
	}
}
