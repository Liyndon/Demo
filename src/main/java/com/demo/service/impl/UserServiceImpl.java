package com.demo.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.core.support.dao.BaseDao;
import com.core.support.service.impl.BaseServiceImpl;
import com.demo.dao.UserDao;
import com.demo.entity.User;
import com.demo.service.UserService;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{
	@Resource
	private UserDao userDao;
	
	@Override
	public User getUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return userDao.getUserByUserName(userName);
	}

	@Override
	public BaseDao<User> getBaseDao() {
		// TODO Auto-generated method stub
		return userDao;
	}

}
