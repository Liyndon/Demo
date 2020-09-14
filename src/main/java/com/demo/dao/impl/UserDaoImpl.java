package com.demo.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.core.support.dao.impl.BaseDaoImpl;
import com.demo.dao.UserDao;
import com.demo.entity.User;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Override
	public User getUserByUserName(String userName) {
		List<User> list = super.findByHql("from User u where u.userName = ? ",
				new Object[] { userName });
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
