package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.dto.ModuleDTO;

public class ModuleDao {
	private SessionFactory sessionFactory;
	private DaoToDto daoToDto = new DaoToDto();

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<ModuleDTO> getModulesByProductName(String productName) {
		List<ModuleDTO> moduleDTOs = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Query<Product> query = (Query<Product>) session.createQuery("from Product where name=:name", Product.class);
			query.setParameter("name", productName);
			Product product = query.getSingleResult();

			if (product != null) {
				Query<Module> moduleQuery = (Query<Module>) session
						.createQuery("from Module where product_id=:id AND parent_module_id=NULL", Module.class);
				moduleQuery.setParameter("id", product.getProductId());
				List<Module> modules = moduleQuery.getResultList();
				for (Module module : modules) {
					moduleDTOs.add(daoToDto.convertToModuleDTO(module));
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
		return moduleDTOs;
	}

	public List<ModuleDTO> getChildModulesByParentName(String parentName) {
		List<ModuleDTO> moduleDTOs = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Query<Module> query = (Query<Module>) session.createQuery("from Module where name=:name", Module.class);
			query.setParameter("name", parentName);
			Module module = query.getSingleResult();

			if (module != null) {
				Query<Module> moduleQuery = (Query<Module>) session
						.createQuery("from Module where parent_module_id=:id", Module.class);
				moduleQuery.setParameter("id", module.getModuleId());
				List<Module> modules = moduleQuery.getResultList();
				for (Module moduleDAO : modules) {
					moduleDTOs.add(daoToDto.convertToModuleDTO(moduleDAO));
				}

			}

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (NoResultException e) {
//			e.printStackTrace();
		} finally {
			session.close();
		}
		return moduleDTOs;
	}

	public List<ModuleDTO> getChildModulesByParentId(long id) {
		List<ModuleDTO> moduleDTOs = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Query<Module> moduleQuery = (Query<Module>) session.createQuery("from Module where parent_module_id=:id",
					Module.class);
			moduleQuery.setParameter("id", id);
			List<Module> modules = moduleQuery.getResultList();
			for (Module moduleDAO : modules) {
				moduleDTOs.add(daoToDto.convertToModuleDTO(moduleDAO));
			}

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (NoResultException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return moduleDTOs;
	}

	public List<ModuleDTO> getModulesByNames(List<String> moduleNames) {
		List<ModuleDTO> moduleDTOs = new ArrayList<>();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			for (String name : moduleNames) {
				Query<Module> query = (Query<Module>) session.createQuery("from Module where name=:name", Module.class);
				query.setParameter("name", name);
				Module module = query.getSingleResult();
				if (module != null) {
					moduleDTOs.add(daoToDto.convertToModuleDTO(module));
				}
			}

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} catch (NoResultException e) {
		} finally {
			session.close();
		}
		return moduleDTOs;
	}
}
