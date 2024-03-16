package com.assetsense.assetsoft.domain;

import java.io.Serializable;

public class Module implements Serializable {
	private static final long serialVersionUID = 1L;
	private long moduleId;
	private String name;
	private Product product;
	private Module parentModule;

	public Module() {
	}

	public long getModuleId() {
		return moduleId;
	}

	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Module getParentModule() {
		return parentModule;
	}

	public void setParentModule(Module parentModule) {
		this.parentModule = parentModule;
	}

	@Override
	public String toString() {
		return "Module [moduleId=" + moduleId + ", name=" + name + ", product=" + product + ", parentModule="
				+ parentModule + "]";
	}

}
