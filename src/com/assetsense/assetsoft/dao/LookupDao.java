package com.assetsense.assetsoft.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assetsense.assetsoft.domain.Lookup;

@SuppressWarnings("deprecation")
public class LookupDao {
	private SessionFactory sessionFactory;
	private DaoToDto daoToDto = new DaoToDto();

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<Lookup> getLookupsByCatId(long catId) {
		List<Lookup> lookups = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Lookup> query = (Query<Lookup>) session.createQuery("from Lookup where catId=:catId", Lookup.class);
			query.setParameter("catId", catId);
			lookups = query.getResultList();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return lookups;
	}

	public Lookup getLookupByValue(String value) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		Lookup lookup = null;
		try {
			tx = session.beginTransaction();
			Query<Lookup> query = (Query<Lookup>) session.createQuery("from Lookup where value=:value", Lookup.class);
			query.setParameter("value", value);
			lookup = query.uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return lookup;
	}
}
