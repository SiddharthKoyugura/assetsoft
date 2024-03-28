package com.assetsense.assetsoft.dao;

import java.util.List;

import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.dto.ProductDTO;

public interface ProductDao {
	ProductDTO saveProduct(Product product);

	void deleteProduct(Product product);

	Product getProductById(long id);

	List<ProductDTO> getProducts();

	ProductDTO getProductByName(String name);

	List<ProductDTO> getChildProductsByParentId(long id);

	List<ProductDTO> getTopMostParentProducts();
}
