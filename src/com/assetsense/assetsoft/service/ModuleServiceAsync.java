package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ModuleServiceAsync {
	void saveModule(Module module, AsyncCallback<ModuleDTO> callback);
	void getModules(AsyncCallback<List<ModuleDTO>> callback);
	void getModulesByProductName(String productName, AsyncCallback<List<ModuleDTO>> callback);
	void getChildModulesByParentName(String parentName, AsyncCallback<List<ModuleDTO>> callback);
	void getModuleByName(String module, AsyncCallback<ModuleDTO> callback);
	void getChildModulesByParentId(long id, AsyncCallback<List<ModuleDTO>> callback);
}
