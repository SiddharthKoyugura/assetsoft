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
	public List<TaskDTO> getTasksByModuleName(String name) {
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
	@Override
	public List<TaskDTO> getTasksByLookupOrder(String lookupName, Boolean asc) {
		lookupName = lookupName.toLowerCase();
		List<TaskDTO> taskDTOs = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			List<Lookup> lookups = null;
			Criteria criteria = session.createCriteria(Lookup.class);
			if (lookupName.equals("type")) {
				criteria.add(Restrictions.eq("catId", 1000L));
			} else if (lookupName.equals("priority")) {
				criteria.add(Restrictions.eq("catId", 2000L));
			} else if (lookupName.equals("status")) {
				criteria.add(Restrictions.eq("catId", 3000L));
			}

			if (asc) {
				criteria.addOrder(Order.asc("lookupId"));
			} else {
				criteria.addOrder(Order.desc("lookupId"));
			}

			lookups = criteria.list();

			for (Lookup lookup : lookups) {
				criteria = session.createCriteria(Task.class);
				criteria.add(Restrictions.eq(lookupName, lookup));
				List<Task> tasks = criteria.list();
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

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<TaskDTO> getTasksBySearchString(String attrName, String searchValue) {
		attrName = attrName.toLowerCase();
		searchValue = searchValue.toLowerCase();
		List<TaskDTO> taskDTOs = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Query<Task> taskQuery = null;

			if (attrName.equals("id")) {
				taskQuery = session.createQuery("From Task WHERE CAST(task_id AS string) LIKE :searchValue",
						Task.class);
				taskQuery.setParameter("searchValue", "%" + searchValue + "%");

				List<Task> tasks = taskQuery.getResultList();

				for (Task task : tasks) {
					taskDTOs.add(typeConverter.convertToTaskDTO(task));
				}

			} else if (attrName.equals("title")) {
				taskQuery = session.createQuery("From Task WHERE LOWER(title) LIKE :searchValue", Task.class);
				taskQuery.setParameter("searchValue", "%" + searchValue + "%");

				List<Task> tasks = taskQuery.getResultList();

				for (Task task : tasks) {
					taskDTOs.add(typeConverter.convertToTaskDTO(task));
				}

			} else if (attrName.equals("user")) {
				Query<User> userQuery = session.createQuery("FROM User WHERE LOWER(name) LIKE :searchValue", User.class);
				userQuery.setParameter("searchValue", "%" + searchValue + "%");

				List<User> users = userQuery.getResultList();

				for (User user : users) {
					taskQuery = session.createQuery("FROM Task WHERE user_id = :userId", Task.class);
					taskQuery.setParameter("userId", user.getUserId());

					List<Task> tasks = taskQuery.getResultList();

					for (Task task : tasks) {
						taskDTOs.add(typeConverter.convertToTaskDTO(task));
					}
				}
			} else if (attrName.equals("product")) {
				Query<Product> productQuery = session.createQuery("FROM Product WHERE LOWER(name) LIKE :searchValue",
						Product.class);
				productQuery.setParameter("searchValue", "%" + searchValue + "%");

				List<Product> products = productQuery.getResultList();

				for (Product product : products) {
					taskQuery = session.createQuery("FROM Task WHERE product_id = :productId", Task.class);
					taskQuery.setParameter("productId", product.getProductId());

					List<Task> tasks = taskQuery.getResultList();

					for (Task task : tasks) {
						taskDTOs.add(typeConverter.convertToTaskDTO(task));
					}
				}
			} else if (attrName.equals("module")) {
				Query<Module> moduleQuery = session.createQuery("FROM Module WHERE LOWER(name) LIKE :searchValue",
						Module.class);
				moduleQuery.setParameter("searchValue", "%" + searchValue + "%");
				List<Module> modules = moduleQuery.getResultList();
				for (Module module : modules) {
					taskQuery = session.createQuery("FROM Task WHERE moduleid = :moduleId", Task.class);
					taskQuery.setParameter("moduleId", module.getModuleId());

					List<Task> tasks = taskQuery.getResultList();

					for (Task task : tasks) {
						taskDTOs.add(typeConverter.convertToTaskDTO(task));
					}
				}
			} else {
				// TODO: COMplete this lookup filtering part
				List<Lookup> lookups = null;
				Criteria criteria = session.createCriteria(Lookup.class);
				if (attrName.equals("type")) {
					criteria.add(Restrictions.eq("catId", 1000L));
				} else if (attrName.equals("priority")) {
					criteria.add(Restrictions.eq("catId", 2000L));
				} else if (attrName.equals("status")) {
					criteria.add(Restrictions.eq("catId", 3000L));
				}

				lookups = criteria.list();
				searchValue = searchValue.toUpperCase();
				for (Lookup lookup : lookups) {
					if (lookup.getValue().contains(searchValue)) {
						criteria = session.createCriteria(Task.class);
						criteria.add(Restrictions.eq(attrName, lookup));
						List<Task> tasks = criteria.list();
						for (Task task : tasks) {
							taskDTOs.add(typeConverter.convertToTaskDTO(task));
						}
					}
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
