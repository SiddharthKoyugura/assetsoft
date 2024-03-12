package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProductServiceAsync {
	void saveProduct(Product product, AsyncCallback<Void> callback);
	void deleteProduct(Product product, AsyncCallback<Void> callback);
	void getProductById(long id, AsyncCallback<Product> callaback);
	void getProducts(AsyncCallback<List<ProductDTO>> callaback);
	void getProductByName(String name, AsyncCallback<ProductDTO> callback);
	void getChildProductsByParentId(long id, AsyncCallback<List<ProductDTO>> callback);
}
