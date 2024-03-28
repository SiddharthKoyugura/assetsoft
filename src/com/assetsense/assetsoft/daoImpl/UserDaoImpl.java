package com.assetsense.assetsoft.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assetsense.assetsoft.dao.UserDao;
import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.UserDTO;

public class UserDaoImpl implements UserDao {
	private SessionFactory sessionFactory;
	private DaoToDto daoToDto = new DaoToDto();

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
	public UserDTO getUserByEmail(String email) {
		User user = null;
		UserDTO userDTO = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<User> query = (Query<User>) session.createQuery("from User where email=:email", User.class);
			query.setParameter("email", email);
			user = query.uniqueResult();
			if (user != null) {
				userDTO = daoToDto.convertToUserDTO(user);
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
		return userDTO;
	}

	public UserDTO getUserByName(String name) {
		UserDTO userDTO = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<User> query = (Query<User>) session.createQuery("from User where name=:name", User.class);
			query.setParameter("name", name);
			User user = query.uniqueResult();
			if (user != null) {
				userDTO = daoToDto.convertToUserDTO(user);
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

		return userDTO;
	}

	// method to return all users
	public List<UserDTO> getUsers() {
		List<UserDTO> userDTOs = new ArrayList<>();
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<User> query = (Query<User>) session.createQuery("from User", User.class);
			List<User> users = query.getResultList();
			for (User user : users) {
				userDTOs.add(daoToDto.convertToUserDTO(user));
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
		return userDTOs;
	}

	// USER-TEAM section
	public void addUserToTeam(User user, Team team) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			user.getTeams().add(team);
			session.update(user);
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

	public List<UserDTO> getUsersFromTeam(Team team) {
		List<UserDTO> usersDTO = new ArrayList<>();
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<User> query = session.createQuery("FROM User u WHERE :team MEMBER OF u.teams", User.class);
			query.setParameter("team", team);
			List<User> users = query.getResultList();
			if (users != null) {
				for (User user : users) {
					usersDTO.add(daoToDto.convertToUserDTO(user));
				}
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

		return usersDTO;
	}

	public List<Team> getTeamsForUser(User user) {
		List<Team> teams = new ArrayList<>();
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Team> query = session.createQuery("SELECT u.teams FROM User u WHERE u = :user", Team.class);
			query.setParameter("user", user);
			for (Team team : query.getResultList()) {
				teams.add(team);
			}
			tx.commit();
		} catch (Exception e) {
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
