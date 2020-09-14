package com.demo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.core.support.dao.impl.BaseDaoImpl;
import com.demo.dao.PermissionDao;
import com.demo.entity.Permission;

@Repository("permissionDao")
public class PermissionDaoImpl extends BaseDaoImpl<Permission> implements
		PermissionDao {

	@Override
	public List<Permission> getPermissionByRoleId(Integer roleId) {
		return super.findByHql("from Permission p where p.roles.id = ?",
				new Object[] { roleId });
	}

}
