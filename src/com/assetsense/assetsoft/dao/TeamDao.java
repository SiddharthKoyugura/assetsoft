package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.dto.TeamDTO;

@SuppressWarnings("deprecation")
public class TeamDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// method to add team
	public void saveTeam(Team team) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.save(team);
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
	public List<TeamDTO> getTeams() {
		List<TeamDTO> teamDTOs = new ArrayList<>();
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Team> query = (Query<Team>) session.createQuery("from Team", Team.class);
			List<Team> teams = query.getResultList();
			for(Team team: teams){
				TeamDTO teamDTO = new TeamDTO();
				teamDTO.setName(team.getName());
				teamDTO.setTeamId(team.getTeamId());
				teamDTOs.add(teamDTO);
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
		return teamDTOs;
	}

}
