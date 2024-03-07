package com.assetsense.assetsoft.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assetsense.assetsoft.domain.User;

@SuppressWarnings("deprecation")
public class UserDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// method to add user
	public void saveUser(User user) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	// method to delete user
	public void deleteUser(User user) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.delete(user);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	// method to return one user of given id
	public User getUserById(long id) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		User user = null;
		try {
			tx = session.beginTransaction();
			user = session.get(User.class, id);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return user;
	}

	// Method to return user of given email
	public User getUserByEmail(String email) {
		User user = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			user = session.get(User.class, email);
			tx.commit();
		} catch(HibernateException e) {
			if(tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return user;
	}

	// method to return all users
	public List<User> getUsers() {
		List<User> users = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			@SuppressWarnings({ "unchecked" })
			Query<User> query = (Query<User>) session.createQuery("from user_");
			users = query.getResultList();
			tx.commit();
		}catch(HibernateException e){
			if(tx != null){
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return users;
	}
}
