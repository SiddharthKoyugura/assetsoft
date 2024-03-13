package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.dto.ProductDTO;

@SuppressWarnings("deprecation")
public class ProductDao {
	private SessionFactory sessionFactory;
	private DaoToDto daoToDto = new DaoToDto();
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// method to add product
	public void saveProduct(Product product) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.save(product);
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

	// method to delete product
	public void deleteProduct(Product product) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			session.delete(product);
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

	// method to return one product of given id
	public Product getProductById(long id) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		Product product = null;
		try {
			tx = session.beginTransaction();
			product = session.get(Product.class, id);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return product;
	}

	// method to return all products
	public List<ProductDTO> getProducts() {
		List<ProductDTO> productDTOs = new ArrayList<>();
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Product> query = (Query<Product>) session.createQuery("from Product", Product.class);
			List<Product> products = query.getResultList();
			for(Product product: products){
				productDTOs.add(daoToDto.convertToProductDTO(product));
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
		return productDTOs;
	}
	
	public ProductDTO getProductByName(String name){
		ProductDTO productDTO = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		
		try {
			tx = session.beginTransaction();
			Query<Product> query = (Query<Product>) session.createQuery("from Product where name=:name", Product.class);
			query.setParameter("name", name);
//			Product product = query.getResultList().get(0);
			Product product = query.uniqueResult();
			productDTO = daoToDto.convertToProductDTO(product);
			tx.commit();
		}catch(HibernateException e){
			if(tx != null){
				tx.rollback();
			}
			e.printStackTrace();
		}finally {
			session.close();
		}
		
		return productDTO;
	}
	
	public List<ProductDTO> getChildProductsByParentId(long id){
		List<ProductDTO> productDTOs = new ArrayList<>();
		Transaction tx =null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Product> query = (Query<Product>) session.createQuery("from Product where parent_product_id=:parent_product_id", Product.class);
			query.setParameter("parent_product_id", id);
			List<Product> products = query.getResultList();
			for(Product product: products) {
				productDTOs.add(daoToDto.convertToProductDTO(product));
			}
			tx.commit();
		}catch(HibernateException e){
			if(tx != null){
				tx.rollback();
			}
			e.printStackTrace();
		}finally {
			session.close();
		}
		return productDTOs;
	}
	
	public List<ProductDTO> getTopMostParentProducts(){
		List<ProductDTO> productDTOs = new ArrayList<>();
		Transaction tx =null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Product> query = (Query<Product>) session.createQuery("from Product where parent_product_id=NULL", Product.class);
			List<Product> products = query.getResultList();
			for(Product product: products) {
				productDTOs.add(daoToDto.convertToProductDTO(product));
			}
			tx.commit();
		}catch(HibernateException e){
			if(tx != null){
				tx.rollback();
			}
			e.printStackTrace();
		}finally {
			session.close();
		}
		return productDTOs;
	}
}
