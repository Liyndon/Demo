package com.demo.security;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.core.exception.InvalidLoginPageException;
import com.core.exception.ValidCodeException;
import com.demo.entity.Permission;
import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.service.PermissionService;
import com.demo.service.RoleService;
import com.demo.service.UserService;


/**
 * 用户身份验证,授权 Realm 组件
 * @ClassName: SecurityRealm 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zq_ja.Zhang
 * @date 2015-07-18 下午7:49:22 
 *
 */
@Component(value = "securityRealm")
public class SecurityRealm extends AuthorizingRealm{

	@Resource(name="userService")
	private UserService userService;
	
	@Resource 
	private RoleService roleService;

	@Resource 
	private PermissionService permissionService;
	

	/**
	 * 权限检查
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

		String username = String.valueOf(principals.getPrimaryPrincipal());

		final User user = userService.getUserByUserName(username);
		if(user != null){
			final List<Role> roleInfos = roleService.getRolesByUserId(user.getId());
			for (Role role : roleInfos) { // 添加角色 System.err.println(role);
				authorizationInfo.addRole(role.getRoleCode());
				final List<Permission> permissions = permissionService.getPermissionByRoleId(role.getId());
				for (Permission permission : permissions) { // 添加权限
					authorizationInfo.addStringPermission(permission.getResources().getResCode());
				}
			}
		}
		return authorizationInfo;
	}

	/**
	 * 登录验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) {
		CustomUsernamePasswordToken login_token = (CustomUsernamePasswordToken) token;
		if(!login_token.isAutoLogin()){//判断自动登录
			String userInputValidCode = login_token.getValidCode();
			// 取得真实的正确校验码
			String realRightValidCode = (String) SecurityUtils.getSubject()
					.getSession().getAttribute("loginCode");
			//验证码判断
			if (null == userInputValidCode
					|| !userInputValidCode.equalsIgnoreCase(realRightValidCode)) {
				throw new ValidCodeException("验证码输入错误");
			}
			//登录用户名
			String loginValidFiled = (String) token.getPrincipal();
			//查询数据库
			User user = userService.getUserByUserName(loginValidFiled);
			if (user == null) {
				throw new UnknownAccountException("账号不存在");// 没找到帐号
			}
			//判断用户状态
			if (user.getState() == null || "".equals(user.getState())) {
				throw new LockedAccountException("账号状态异常，请联系管理员"); // 帐号未激活
			}else if (user.getState() == 0) {
				throw new LockedAccountException("账号已锁定"); // 帐号锁定
			} else if (user.getState() == -1) {
				throw new LockedAccountException("账号还未激活"); // 帐号未激活
			}
			//用户类型
			String validateType = login_token.getValidateType();
			boolean f = false;
			//获取用户拥有的角色
			List<Role> roleInfos = roleService
					.getRolesByUserId(user.getId());
			for(Role role : roleInfos){
				if(role.getRoleCode().contains("admin")){//管理员
					f = true;
					break;
				}
			}
			if("admin".equals(validateType)){//管理登录页面
				if(!f){
					throw new InvalidLoginPageException("该账户无权登录平台管理系统");
				}
			}else{
				if(f){
					throw new InvalidLoginPageException("管理员账户只能登录平台管理系统");
				}
			}
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
					user.getUserName(), user.getUserPassword(), getName());
			return authenticationInfo;
		}else{
			return new SimpleAuthenticationInfo("", "", getName());//构造空验证
		}
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

}
