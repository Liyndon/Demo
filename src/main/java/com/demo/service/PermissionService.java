package com.demo.service;

import java.util.List;

import com.core.support.service.BaseService;
import com.demo.entity.Permission;

public interface PermissionService extends BaseService<Permission> {

	List<Permission> getPermissionByRoleId(Integer roleId);
	
}
