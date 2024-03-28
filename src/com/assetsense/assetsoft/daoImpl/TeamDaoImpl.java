package com.assetsense.assetsoft.daoImpl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assetsense.assetsoft.dao.TeamDao;
import com.assetsense.assetsoft.domain.Team;

public class TeamDaoImpl implements TeamDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// method to add team
	public Team saveTeam(Team team) {
		Team teamInDb = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.save(team);
			Query<Team> query = session.createQuery("from Team Where name=:name", Team.class);
			query.setParameter("name", team.getName());
			teamInDb = query.getSingleResult();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return teamInDb;
	}

	// method to delete team
	public void deleteTeam(Team team) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.delete(team);
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

	// method to return one team of given id
	public Team getTeamById(long id) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		Team team = null;
		try {
			tx = session.beginTransaction();
			team = session.get(Team.class, id);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return team;
	}

	// method to return all teams
	public List<Team> getTeams() {
		List<Team> teams = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Team> query = (Query<Team>) session.createQuery("from Team ORDER BY id ASC", Team.class);
			teams = query.getResultList();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return teams;
	}

}
