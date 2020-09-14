package com.demo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.core.support.dao.BaseDao;
import com.core.support.service.impl.BaseServiceImpl;
import com.demo.dao.PermissionDao;
import com.demo.entity.Permission;
import com.demo.service.PermissionService;

@Service("permissionService")
public class PermissionServiceImpl extends BaseServiceImpl<Permission>
		implements PermissionService {
	@Resource
	private PermissionDao permissionDao;

	@Override
	public List<Permission> getPermissionByRoleId(Integer roleId) {
		return permissionDao.getPermissionByRoleId(roleId);
	}

	@Override
	public BaseDao<Permission> getBaseDao() {
		// TODO Auto-generated method stub
		return permissionDao;
	}

}
