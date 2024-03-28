package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.daoImpl.ProductDaoImpl;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.service.ProductService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ProductServiceImpl extends RemoteServiceServlet implements ProductService {

	private ProductDaoImpl productDao;

	@Override
	public ProductDTO saveProduct(Product product) {
		// TODO Auto-generated method stub
		productDao = (ProductDaoImpl) ApplicationContextListener.applicationContext.getBean("productDao");
		return productDao.saveProduct(product);
	}

	@Override
	public void deleteProduct(Product product) {
		// TODO Auto-generated method stub
		productDao = (ProductDaoImpl) ApplicationContextListener.applicationContext.getBean("productDao");
		productDao.deleteProduct(product);
	}

	@Override
	public Product getProductById(long id) {
		// TODO Auto-generated method stub
		productDao = (ProductDaoImpl) ApplicationContextListener.applicationContext.getBean("productDao");
		return productDao.getProductById(id);
	}

	@Override
	public List<ProductDTO> getProducts() {
		// TODO Auto-generated method stub
		productDao = (ProductDaoImpl) ApplicationContextListener.applicationContext.getBean("productDao");
		return productDao.getProducts();
	}

	@Override
	public ProductDTO getProductByName(String name) {
		// TODO Auto-generated method stub
		productDao = (ProductDaoImpl) ApplicationContextListener.applicationContext.getBean("productDao");
		return productDao.getProductByName(name);
	}

	@Override
	public List<ProductDTO> getChildProductsByParentId(long id) {
		productDao = (ProductDaoImpl) ApplicationContextListener.applicationContext.getBean("productDao");
		return productDao.getChildProductsByParentId(id);
	}

	@Override
	public List<ProductDTO> getTopMostParentProducts() {
		productDao = (ProductDaoImpl) ApplicationContextListener.applicationContext.getBean("productDao");
		return productDao.getTopMostParentProducts();
	}

}
