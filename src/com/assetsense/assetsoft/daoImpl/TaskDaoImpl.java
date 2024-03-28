package com.assetsense.assetsoft.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import com.assetsense.assetsoft.dao.TaskDao;
import com.assetsense.assetsoft.domain.Lookup;
import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.assetsense.assetsoft.util.TypeConverter;

public class TaskDaoImpl implements TaskDao {
	private SessionFactory sessionFactory;
	private TypeConverter typeConverter = new TypeConverter();

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// method to add or update task
	/**
	 * 
	 * @param task
	 */
	public void saveTask(Task task) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			if (task.getTaskId() != 0L) {
				// Update the task
				Task taskInDB = session.get(Task.class, task.getTaskId());
				taskInDB.setTitle(task.getTitle());
				taskInDB.setType(task.getType());
				taskInDB.setDescription(task.getDescription());
				taskInDB.setInitialEstimate(task.getInitialEstimate());
				taskInDB.setPercentComplete(task.getPercentComplete());
				taskInDB.setRemainingEstimate(task.getRemainingEstimate());
				taskInDB.setDueDate(task.getDueDate());
				taskInDB.setPriority(task.getPriority());
				taskInDB.setStatus(task.getStatus());
				taskInDB.setUser(task.getUser());
				taskInDB.setProduct(task.getProduct());
				taskInDB.setModule(task.getModule());
				session.update(taskInDB);
			} else {
				// Add the task
				session.save(task);
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

	}

	// method to delete task
	/**
	 * 
	 * @param task
	 */
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

	public void deleteTasksByIds(List<Long> taskIds) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();

			for (long taskId : taskIds) {
				Task task = session.get(Task.class, taskId);
				if (task != null) {
					session.delete(task);
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
	}

	// method to return one task of given id
	public TaskDTO getTaskById(long id) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		TaskDTO taskDTO = null;
		try {
			tx = session.beginTransaction();
			Task task = session.get(Task.class, id);
			taskDTO = typeConverter.convertToTaskDTO(task);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (NullPointerException e) {

		} finally {
			session.close();
		}
		return taskDTO;
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
				taskDTOs.add(typeConverter.convertToTaskDTO(task));
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
	public List<TaskDTO> getTasksByPriorityId(long priorityId) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		List<TaskDTO> taskDTOs = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Query<Task> query = session.createQuery("from Task where priority_id=" + priorityId, Task.class);
			List<Task> tasks = query.getResultList();
			for (Task task : tasks) {
				taskDTOs.add(typeConverter.convertToTaskDTO(task));
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

	// Method to return tasks of given username
	public List<TaskDTO> getTasksByUsername(String username) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		List<TaskDTO> taskDTOs = new ArrayList<>();
		try {
			tx = session.beginTransaction();
			Query<User> userQuery = session.createQuery("from User where name=:name", User.class);
			userQuery.setParameter("name", username);
			User user = userQuery.getSingleResult();
			if (user != null) {
				Query<Task> query = session.createQuery("from Task where user_id=:userId", Task.class);
				query.setParameter("userId", user.getUserId());
				List<Task> tasks = query.getResultList();
				for (Task task : tasks) {
					taskDTOs.add(typeConverter.convertToTaskDTO(task));
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
		return taskDTOs;
	}

	// Method to return tasks by lookup value
	public List<TaskDTO> getTasksByLookupValue(String name, String value) {
		name = name.toLowerCase();
		value = value.toUpperCase();
		List<TaskDTO> taskDTOs = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query<Lookup> lookupQuery = session.createQuery("from Lookup Where value=:value", Lookup.class);
			lookupQuery.setParameter("value", value);
			Lookup lookup = lookupQuery.getSingleResult();

			Query<Task> query = session.createQuery("from Task Where " + name + "id = :id ORDER BY id ASC", Task.class);
			query.setParameter("id", lookup.getLookupId());

			List<Task> tasks = query.getResultList();
			for (Task task : tasks) {
				taskDTOs.add(typeConverter.convertToTaskDTO(task));
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

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<TaskDTO> getTasksByProductName(String name) {
		List<TaskDTO> taskDTOs = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Product.class);
			criteria.add(Restrictions.eq("name", name));

			Product product = (Product) criteria.list().get(0);

			if (product != null) {
				Criteria taskCriteria = session.createCriteria(Task.class);
				taskCriteria.add(Restrictions.eq("product", product));
				taskCriteria.addOrder(Order.asc("taskId"));

				List<Task> tasks = taskCriteria.list();
				for (Task task : tasks) {
					taskDTOs.add(typeConverter.convertToTaskDTO(task));
				}
			}

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
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<TaskDTO> getTasksByModuleName(String name){
		List<TaskDTO> taskDTOs = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Module.class);
			criteria.add(Restrictions.eq("name", name));

			Module Module = (Module) criteria.list().get(0);

			if (Module != null) {
				Criteria taskCriteria = session.createCriteria(Task.class);
				taskCriteria.add(Restrictions.eq("module", Module));
				taskCriteria.addOrder(Order.asc("taskId"));

				List<Task> tasks = taskCriteria.list();
				for (Task task : tasks) {
					taskDTOs.add(typeConverter.convertToTaskDTO(task));
				}
			}

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
			Query<Task> query = (Query<Task>) session.createQuery("from Task ORDER BY id ASC", Task.class);
			List<Task> tasks = query.getResultList();
			for (Task task : tasks) {
				taskDTOs.add(typeConverter.convertToTaskDTO(task));
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

	public void editTaskTitle(long id, String title) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Task> query = (Query<Task>) session.createQuery("from Task where task_id=:id", Task.class);
			query.setParameter("id", id);
			Task task = (Task) query.uniqueResult();
			task.setTitle(title);
			session.update(task);
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

	public void editTaskLookup(long id, String name, String value) {
		Transaction tx = null;
		name = name.toLowerCase();
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Task> query = (Query<Task>) session.createQuery("from Task where task_id=:id", Task.class);
			query.setParameter("id", id);
			Task task = (Task) query.uniqueResult();

			Query<Lookup> lookupQuery = (Query<Lookup>) session.createQuery("from Lookup where value=:value",
					Lookup.class);
			lookupQuery.setParameter("value", value);
			Lookup lookup = lookupQuery.getSingleResult();

			if ("type".equals(name)) {
				task.setType(lookup);
			} else if ("priority".equals(name)) {
				task.setPriority(lookup);
			} else if ("status".equals(name)) {
				task.setStatus(lookup);
			}
			session.update(task);
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

	public void editTaskModule(long id, String name, String moduleName) {
		Transaction tx = null;
		name = name.toLowerCase();
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();

			Query<Task> query = (Query<Task>) session.createQuery("from Task where task_id=:id", Task.class);
			query.setParameter("id", id);
			Task task = (Task) query.uniqueResult();

			Query<Module> moduleQuery = (Query<Module>) session.createQuery("from Module where name=:name",
					Module.class);
			moduleQuery.setParameter("name", moduleName);
			Module module = moduleQuery.getSingleResult();

			if ("module".equals(name)) {
				task.setModule(module);
			} else if ("subSystem".equals(name)) {
			}
			session.update(task);
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

	public void editTaskUser(long id, String username) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Task> query = (Query<Task>) session.createQuery("from Task where task_id=:id", Task.class);
			query.setParameter("id", id);
			Task task = (Task) query.uniqueResult();

			Query<User> userQuery = (Query<User>) session.createQuery("from User where name=:name", User.class);
			userQuery.setParameter("name", username);
			User user = (User) userQuery.getResultList().get(0);

			task.setUser(user);
			session.update(task);
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

	public void editTaskProduct(long id, String productName) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Task> query = (Query<Task>) session.createQuery("from Task where task_id=:id", Task.class);
			query.setParameter("id", id);
			Task task = (Task) query.uniqueResult();

			Query<Product> productQuery = (Query<Product>) session.createQuery("from Product where name=:name",
					Product.class);
			productQuery.setParameter("name", productName);
			Product product = (Product) productQuery.getResultList().get(0);

			task.setProduct(product);
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

}
