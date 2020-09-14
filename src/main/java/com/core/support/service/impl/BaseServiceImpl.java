package com.core.support.service.impl;

import java.io.Serializable;
import java.util.List;

import com.core.support.dao.BaseDao;
import com.core.support.service.BaseService;

/**
 * BaseServiceImpl
 * 
 * @author LXY
 * 
 * @param <T>
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

	public abstract BaseDao<T> getBaseDao();

	@Override
	public T get(Serializable id) {
		return getBaseDao().get(id);
	}

	@Override
	public List<T> getAll() {
		return this.getBaseDao().getAll();
	}

	@Override
	public List<T> findBySql(String sql, Object[] param) {
		return this.getBaseDao().findBySql(sql, param);
	}

	@Override
	public List<T> findByHql(String hql, Object[] param) {
		return this.getBaseDao().findByHql(hql, param);
	}

	@Override
	public boolean delete(Serializable id) {
		return this.getBaseDao().delete(id);
	}

	@Override
	public boolean delete(T t) {
		return this.getBaseDao().delete(t);
	}

	@Override
	public boolean update(T t) {
		return this.getBaseDao().update(t);
	}

	@Override
	public boolean save(T t) {
		// TODO Auto-generated method stub
		return this.getBaseDao().save(t);
	}

	@Override
	public Object findSingleResult(String sql, Object[] param) {
		// TODO Auto-generated method stub
		return findSingleResult(sql, param);
	}

}
