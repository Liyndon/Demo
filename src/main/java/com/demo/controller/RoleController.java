
package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.core.util.StringUtils;
import com.demo.entity.Role;
import com.demo.service.RoleService;

/**
 * 角色Controller
 * 
 * @author ThinkGem
 * @version 2013-12-05
 */
@Controller
@RequestMapping(value = "/role/")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@ModelAttribute("role")
	public Role get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return roleService.get(id);
		} else {
			return new Role();
		}
	}

}
