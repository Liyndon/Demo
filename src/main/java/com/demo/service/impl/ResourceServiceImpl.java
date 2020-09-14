package com.demo.service.impl;

import org.springframework.stereotype.Service;

import com.core.support.dao.BaseDao;
import com.core.support.service.impl.BaseServiceImpl;
import com.demo.dao.ResourceDao;
import com.demo.entity.Resource;
import com.demo.service.ResourceService;

@Service("resourceService")
public class ResourceServiceImpl extends BaseServiceImpl<Resource> implements
		ResourceService {
	@javax.annotation.Resource
	private ResourceDao resourceDao;

	@Override
	public BaseDao<Resource> getBaseDao() {
		// TODO Auto-generated method stub
		return resourceDao;
	}

}
