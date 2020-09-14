package com.demo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.core.support.dao.impl.BaseDaoImpl;
import com.demo.dao.RoleDao;
import com.demo.entity.Role;

@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {

	@Override
	public List<Role> getRolesByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return super.findByHql("from Role r where r.users.id",
				new Object[] { userId });
	}

	@Override
	public Role getRoleByName(String name) {
		List<Role> list = super.findByHql("from Role r where r.users.id",
				new Object[] { name });
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
