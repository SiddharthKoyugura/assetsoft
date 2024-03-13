package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("product")
public interface ProductService extends RemoteService {
	void saveProduct(Product product);
	void deleteProduct(Product product);
	Product getProductById(long id);
	List<ProductDTO> getProducts();
	ProductDTO getProductByName(String name);
	List<ProductDTO> getChildProductsByParentId(long id);
	List<ProductDTO> getTopMostParentProducts();
}
