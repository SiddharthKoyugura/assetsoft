package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("module")
public interface ModuleService extends RemoteService {
	void saveModule(Module module);
	List<ModuleDTO> getModulesByProductName(String productName);
	List<ModuleDTO> getChildModulesByParentName(String parentName);
	List<ModuleDTO> getModulesByNames(List<String> moduleNames);
	List<ModuleDTO> getChildModulesByParentId(long id);
}
