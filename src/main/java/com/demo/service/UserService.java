package com.demo.service;

import com.core.support.service.BaseService;
import com.demo.entity.User;

public interface UserService extends BaseService<User> {
	public User getUserByUserName(String userName);
	
}
