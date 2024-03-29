package com.assetsense.assetsoft.domain;

import java.io.Serializable;

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
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

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", parentProduct=" + parentProduct + "]";
	}

	public Product findTopMostParent() {
	    if (parentProduct == null) {
	        return this;
	    } else {
	        return parentProduct.findTopMostParent();
	    }
	}

}
