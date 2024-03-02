package com.assetsense.assetsoft.domain;

import java.io.Serializable;
import java.util.Set;

public class Product implements Serializable {
	private long productId;
	private String name;
	private Product parentProduct;

	public Product() {
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getParentProduct() {
		return parentProduct;
	}

	public void setParentProduct(Product parentProduct) {
		this.parentProduct = parentProduct;
	}

	

}
