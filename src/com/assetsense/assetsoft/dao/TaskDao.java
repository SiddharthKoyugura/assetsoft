package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.dto.TaskDTO;

@SuppressWarnings("deprecation")
public class TaskDao {
	private SessionFactory sessionFactory;
	private DaoToDto daoToDto = new DaoToDto();

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// method to add task
	public void saveTask(Task task) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.save(task);
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

	// method to delete task
	public void deleteTask(Task task) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.delete(task);
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

	// method to return one task of given id
	public Task getTaskById(long id) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		Task task = null;
		try {
			tx = session.beginTransaction();
			task = session.get(Task.class, id);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return task;
	}

	// method to return tasks of given user_id
	public List<TaskDTO> getTasksByUserId(long userId) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		List<TaskDTO> taskDTOs = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Query<Task> query = session.createQuery("from Task where user_id=" + userId, Task.class);
			List<Task> tasks = query.getResultList();
			for (Task task : tasks) {
				taskDTOs.add(daoToDto.convertToTaskDTO(task));
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
		return taskDTOs;
	}

	// method to return tasks of given priority_id
	public List<TaskDTO> getTasksByPriroityId(long priorityId) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		List<TaskDTO> taskDTOs = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Query<Task> query = session.createQuery("from Task where priority_id=" + priorityId, Task.class);
			List<Task> tasks = query.getResultList();
			for (Task task : tasks) {
				taskDTOs.add(daoToDto.convertToTaskDTO(task));
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
		return taskDTOs;
	}

	// method to return all tasks
	public List<TaskDTO> getTasks() {
		List<TaskDTO> taskDTOs = new ArrayList<>();
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Task> query = (Query<Task>) session.createQuery("from Task", Task.class);
			List<Task> tasks = query.getResultList();
			for (Task task : tasks) {
				taskDTOs.add(daoToDto.convertToTaskDTO(task));
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
		return taskDTOs;
	}

}
