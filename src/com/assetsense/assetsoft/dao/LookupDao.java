package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assetsense.assetsoft.domain.Lookup;

public class LookupDao {
	private SessionFactory sessionFactory;

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
	
	public List<Lookup> getLookupsByValues(List<String> values){
		List<Lookup> lookups = new ArrayList<>();
		
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			for(String value: values){
				Query<Lookup> query = (Query<Lookup>) session.createQuery("from Lookup where value=:value", Lookup.class);
				query.setParameter("value", value);
				Lookup lookup = query.uniqueResult();
				lookups.add(lookup);
			}
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
}
