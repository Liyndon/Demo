package com.demo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.core.support.dao.BaseDao;
import com.core.support.service.impl.BaseServiceImpl;
import com.demo.dao.RoleDao;
import com.demo.entity.Role;
import com.demo.service.RoleService;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements
		RoleService {
	@Resource
	private RoleDao roleDao;

	@Override
	public List<Role> getRolesByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return roleDao.getRolesByUserId(userId);
	}

	@Override
	public BaseDao<Role> getBaseDao() {
		// TODO Auto-generated method stub
		return roleDao;
	}

}
