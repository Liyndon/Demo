package com.demo.dao;

import java.util.List;

import com.core.support.dao.BaseDao;
import com.demo.entity.Permission;

public interface PermissionDao extends BaseDao<Permission> {

	List<Permission> getPermissionByRoleId(Integer roleId);
}