package com.demo.dao;

import java.util.List;

import com.core.support.dao.BaseDao;
import com.demo.entity.Role;


public interface RoleDao extends BaseDao<Role>{
	
	List<Role> getRolesByUserId(Integer userId);

	Role getRoleByName(String name);
	
}