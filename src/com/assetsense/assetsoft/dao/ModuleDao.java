package com.assetsense.assetsoft.dao;

import java.util.List;

import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.dto.ModuleDTO;

public interface ModuleDao {
	ModuleDTO saveModule(Module module);

	List<ModuleDTO> getModules();

	List<ModuleDTO> getModulesByProductName(String productName);

	List<ModuleDTO> getChildModulesByParentName(String parentName);

	ModuleDTO getModuleByName(String module);

	List<ModuleDTO> getChildModulesByParentId(long id);
}
