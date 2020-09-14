package com.core.support.service;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {
	T get(Serializable id);

	List<T> getAll();

	List<T> findBySql(String sql, Object[] param);

	List<T> findByHql(String hql, Object[] param);

	boolean delete(Serializable id);

	boolean delete(T t);

	boolean update(T t);

	boolean save(T t);

	Object findSingleResult(String sql, Object[] param);
}
