package com.demo.dao;

import com.core.support.dao.BaseDao;
import com.demo.entity.User;



public interface UserDao extends BaseDao<User>{
    
    User getUserByUserName(String userName);
}