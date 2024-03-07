package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.dao.ProductDao;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.service.ProductService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProductServiceImpl extends RemoteServiceServlet implements ProductService {
	
	private ProductDao productDao;
	
	@Override
	public void saveProduct(Product product) {
		// TODO Auto-generated method stub
		productDao = (ProductDao) ApplicationContextListener.applicationContext.getBean("productDao");
		productDao.saveProduct(product);
	}

	@Override
	public void deleteProduct(Product product) {
		// TODO Auto-generated method stub
		productDao = (ProductDao) ApplicationContextListener.applicationContext.getBean("productDao");
		productDao.deleteProduct(product);
	}

	@Override
	public Product getProductById(long id) {
		// TODO Auto-generated method stub
		productDao = (ProductDao) ApplicationContextListener.applicationContext.getBean("productDao");
		return productDao.getProductById(id);
	}

	@Override
	public List<Product> getProducts() {
		// TODO Auto-generated method stub
		productDao = (ProductDao) ApplicationContextListener.applicationContext.getBean("productDao");
		return productDao.getProducts();
	}

}
