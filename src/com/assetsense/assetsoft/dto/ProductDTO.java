package com.assetsense.assetsoft.dto;

import java.io.Serializable;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private long productId;
	private String name;
	private ProductDTO parentProductDTO;

	public ProductDTO() {

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

	public ProductDTO getParentProductDTO() {
		return parentProductDTO;
	}

	public void setParentProductDTO(ProductDTO parentProductDTO) {
		this.parentProductDTO = parentProductDTO;
	}

	@Override
	public String toString() {
		return "ProductDTO [productId=" + productId + ", name=" + name + ", parentProductDTO=" + parentProductDTO + "]";
	}

	public ProductDTO getTopMostParent() {
		if (parentProductDTO == null) {
			return this;
		} else {
			return parentProductDTO.getTopMostParent();
		}
	}

}
