package com.assetsense.assetsoft.dto;

import java.io.Serializable;

public class ModuleDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private long moduleId;
	private String name;
	private ProductDTO productDTO;
	private ModuleDTO parentModuleDTO;

	public ModuleDTO() {
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

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	public ModuleDTO getParentModuleDTO() {
		return parentModuleDTO;
	}

	public void setParentModuleDTO(ModuleDTO parentModuleDTO) {
		this.parentModuleDTO = parentModuleDTO;
	}

	@Override
	public String toString() {
		return "ModuleDTO [moduleId=" + moduleId + ", name=" + name + ", productDTO=" + productDTO
				+ ", parentModuleDTO=" + parentModuleDTO + "]";
	}

}
