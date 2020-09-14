package com.demo.service;

import java.util.List;

import com.core.support.service.BaseService;
import com.demo.entity.Role;

public interface RoleService extends BaseService<Role> {
	public List<Role> getRolesByUserId(Integer userId);
}
