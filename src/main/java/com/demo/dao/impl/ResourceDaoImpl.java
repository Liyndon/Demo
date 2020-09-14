package com.demo.dao.impl;

import org.springframework.stereotype.Repository;

import com.core.support.dao.impl.BaseDaoImpl;
import com.demo.dao.ResourceDao;
import com.demo.entity.Resource;

@Repository("resourceDao")
public class ResourceDaoImpl extends BaseDaoImpl<Resource> implements ResourceDao {
	
}
