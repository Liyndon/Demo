package com.core.support.dao;

import java.io.Serializable;
import java.util.List;

/**
 * BaseDao
 * 
 * @author LXY
 * 
 */
public interface BaseDao<T> {

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
