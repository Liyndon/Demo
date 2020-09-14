package com.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.exception.InvalidLoginPageException;
import com.core.exception.ValidCodeException;
import com.core.util.ValidateCode;
import com.demo.entity.Permission;
import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.security.CustomUsernamePasswordToken;
import com.demo.service.PermissionService;
import com.demo.service.RoleService;
import com.demo.service.UserService;

/**
 * 登录Controller
 * 
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/*@Autowired
	private SessionDAO sessionDAO;*/
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/index")
	public String index(){
		return "login";
	}

	/**
	 * 
	 * @Title: loginValidate
	 * @Description: 登录校验
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loginValidate(User user, String vcode,String validateType,HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession(true);
		result.put("msg", "用户名或者密码错误!");
		result.put("success", "true");
		result.put("target", "username");
		try {
			try {
				Subject subject = SecurityUtils.getSubject();
				// 已登陆则 跳到首页
				if (subject.isAuthenticated()) {
					if (session.getAttribute("userInfo") != null) {
						if (((User) session.getAttribute("userInfo"))
								.getUserName().equals(user.getUserName())) {
							result.put("msg", "用户登录验证通过!");
							result.put("target", "0");
							// 显式调用，执行权限分配
							SecurityUtils.getSubject().getPrincipals();
							return result;
						} else {
							result.put(
									"msg",
									((User) session.getAttribute("userInfo"))
											.getUserName() + "已登录，请退出后再登录新账号!");
							result.put("target", "username");
							return result;
						}
					} else {
						return result;
					}
				} else {
					boolean rememberMe = WebUtils.isTrue(request,
							FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
					String host = request.getRemoteHost();

					String userName = user.getUserName();
					if (userName == null || "".equals(userName)) {
						result.put("msg", "用户名不能为空!");
						result.put("target", "username");
					}
					String password = user.getUserPassword();
					if (password == null || "".equals(password)) {
						result.put("msg", "用户密码不能为空!");
						result.put("target", "password");
					}
					if (vcode == null || "".equals(vcode)) {
						result.put("msg", "用户验证码不能为空!");
						result.put("target", "vcode");
					}

					// 构造登陆令牌环
					CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(
							userName, password.toCharArray(), rememberMe, host,
							vcode, validateType);

					// 发出登陆请求
					SecurityUtils.getSubject().login(token);
					// 获得登录用户信息
					user = userService.getUserByUserName(userName);
					if (user != null) {
						if (SecurityUtils.getSubject().isAuthenticated()) {
							subject = SecurityUtils.getSubject();
							result.put("msg", "用户登录验证通过!");
							result.put("target", "0");
							//通过用户id查询用户拥有角色
							List<Role> roleInfos = roleService
									.getRolesByUserId(user.getId());
							user.setRoles(new HashSet<>(roleInfos));
							Set<Permission> permissions = new HashSet<Permission>();
							//获取每个角色对应的权限
							for (Role role : roleInfos) { // 添加角色
								List<Permission> rolePermissions = permissionService
										.getPermissionByRoleId(role.getId());
								permissions.addAll(rolePermissions);
							}

							//user.setPermissions(permissions);
							
							//初始化角色菜单
							/*List<Menu> treeNodes = new ArrayList<>();
							for (Permission permission : permissions) {
								if (permission.getPermissionInfo().getState() == 1) {
									Menu menu = new Menu(permission.getId(),
											permission.getPermissionInfo()
													.getResName(), permission
													.getPermissionInfo()
													.getResIcon(), permission
													.getPermissionInfo()
													.getResUrl(), permission
													.getPermissionInfo()
													.getResType(),
											permission.getOrder(), "open",
											permission.getPId());
									treeNodes.add(menu);
								}
							}
							Collections.sort(treeNodes);
							user.setMenus(formatterResource(treeNodes));*/
						}
						
						// 把登录成功的用户放入缓存
						session.setAttribute("userInfo", user);
						subject.getSession().setAttribute("userInfo", user);
					}
				}
			} catch (UnknownAccountException e) {
				result.put("msg", e.getMessage());
				logger.debug(e.getMessage());
			} catch (IncorrectCredentialsException e) {
				result.put("msg", e.getMessage());
				result.put("target", "password");
				logger.debug("用户或者密码错误");
			} catch (ExcessiveAttemptsException e) {
				result.put("msg", e.getMessage());
				logger.debug(e.getMessage());
			} catch (ValidCodeException e) {
				result.put("msg", e.getMessage());
				result.put("target", "vcode");
				logger.debug(e.getMessage());
			} catch (InvalidLoginPageException e) {
				result.put("msg", e.getMessage());
				result.put("target", "username");
				logger.debug(e.getMessage());
			} catch (LockedAccountException e) {
				result.put("msg", e.getMessage());
				logger.debug(e.getMessage());
			} catch (AuthenticationException e) {
				result.put("msg", "系统错误");
				logger.debug("系统错误");
				e.printStackTrace();
			}
		} catch (Exception e) {
			result.put("msg", "系统错误");
			e.printStackTrace();
		}
		return result;
	}
	

	/**
	 * 登录成功，进入管理首页
	 */
	@RequestMapping(value = "home")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "Home";
	}

	/**
	 * 
	 * @Title: createCheckCode
	 * @Description: 生成图片验证码
	 * @param @param request
	 * @param @param response
	 * @param @param session
	 * @param @throws IOException 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping("/checkcode")
	public void createCheckCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 设置响应的类型格式为图片格式
		System.out.println(request.getSession() + "=session");
		int width = 120;
		int height = 40;
		int codeCount = 5;
		int lineCount = 20;
		if (request.getParameter("width") != null) {
			width = Integer.parseInt(request.getParameter("width"));
		}
		if (request.getParameter("height") != null) {
			height = Integer.parseInt(request.getParameter("height"));
		}
		if (request.getParameter("codeCount") != null) {
			codeCount = Integer.parseInt(request.getParameter("codeCount"));
		}
		if (request.getParameter("lineCount") != null) {
			height = Integer.parseInt(request.getParameter("lineCount"));
		}
		response.setContentType("image/jpeg");
		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		ValidateCode vCode = new ValidateCode(width, height, codeCount,
				lineCount);
		SecurityUtils.getSubject().getSession()
				.setAttribute("loginCode", vCode.getCode());
		vCode.write(response.getOutputStream());
	}

	
}
