package com.assetsense.assetsoft.ui;

import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.assetsense.assetsoft.dto.ProductDTO;

public class DtoToDao {
	public Product convertToProductDao(ProductDTO productDTO) {
		Product product = new Product();

		product.setProductId(productDTO.getProductId());
		product.setName(productDTO.getName());

		if (product.getParentProduct() != null) {
			product.setParentProduct(convertToProductDao(productDTO.getParentProductDTO()));
		}

		return product;
	}

	public Module convertToModuleDao(ModuleDTO moduleDTO) {
		Module module = new Module();

		module.setModuleId(moduleDTO.getModuleId());
		module.setName(moduleDTO.getName());
		module.setProduct(convertToProductDao(moduleDTO.getProductDTO()));

		if (moduleDTO.getParentModuleDTO() != null) {
			module.setParentModule(convertToModuleDao(moduleDTO.getParentModuleDTO()));
		}

		return module;
	}
}
