package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Product;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProductServiceAsync {
	void saveProduct(Product product, AsyncCallback<Void> callback);
	void deleteProduct(Product product, AsyncCallback<Void> callback);
	void getProductById(long id, AsyncCallback<Product> callaback);
	void getProducts(AsyncCallback<List<Product>> callaback);
}
