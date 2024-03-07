package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Product;
import com.google.gwt.user.client.rpc.RemoteService;

public interface ProductService extends RemoteService {
	void saveProduct(Product product);
	void deleteProduct(Product product);
	Product getProductById(long id);
	List<Product> getProducts();
}
