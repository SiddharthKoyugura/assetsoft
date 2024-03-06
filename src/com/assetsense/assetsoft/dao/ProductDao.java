package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

import com.assetsense.assetsoft.domain.Product;


public class ProductDao {
	HibernateTemplate template;

	public HibernateTemplate getTemplate() {
		return template;
	}

	public void setTemplate(HibernateTemplate template) {
		this.template = template;
	}

	// method to add product
	public void saveProduct(Product product) {
		template.save(product);
	}

	// method to update product
	public void updateProduct(Product product) {
		template.save(product);
	}

	// method to delete product
	public void deleteProduct(Product product) {
		template.delete(product);
	}

	// method to return one product of given id
	public Product getproductById(long id) {
		Product product = (Product) template.get(Product.class, id);
		return product;
	}

	// method to return all products
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<Product>();
		products = template.loadAll(Product.class);
		return products;
	}
}
