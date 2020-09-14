package com.core.support.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.core.support.dao.BaseDao;

/**
 * BaseDaoImpl
 * 
 * @author LXY
 * 
 * @param <T>
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

	@Resource(name = "sessionFactory")
	protected SessionFactory sessionFactory;

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		ParameterizedType parameterizedType = (ParameterizedType) this
				.getClass().getGenericSuperclass();
		entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	public Session getSession() {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			e.printStackTrace();
			session = sessionFactory.openSession();
		}
		return session;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Serializable id) {
		return (T) this.getSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {
		return this.getSession().createQuery("from " + entityClass).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findBySql(String sql, Object[] param) {
		Query query = this.getSession().createSQLQuery(sql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				query.setParameter(i, param[i]);
			}
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByHql(String hql, Object[] param) {
		Query query = this.getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				query.setParameter(i, param[i]);
			}
		}
		return query.list();
	}

	@Override
	public boolean delete(Serializable id) {
		T t = this.get(id);
		try {
			getSession().delete(t);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean save(T t) {
		if (t != null) {
			getSession().save(t);
			return true;
		}
		return false;
	}

	@Override
	public Object findSingleResult(String sql, Object[] param) {
		Query query = getSession().createSQLQuery(sql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				query.setParameter(i, param[i]);
			}
		}
		return query.uniqueResult();
	}

	@Override
	public boolean delete(T t) {
		try {
			this.getSession().delete(t);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean update(T t) {
		try {
			this.getSession().update(t);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
