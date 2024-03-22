package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.dao.ModuleDao;
import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.assetsense.assetsoft.service.ModuleService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ModuleServiceImpl extends RemoteServiceServlet implements ModuleService {
	private static final long serialVersionUID = 1L;
	private ModuleDao moduleDao;
	
	@Override
	public void saveModule(Module module) {
		moduleDao = (ModuleDao) ApplicationContextListener.applicationContext.getBean("moduleDao");
		moduleDao.saveModule(module);
	}

	@Override
	public List<ModuleDTO> getModulesByProductName(String productName) {
		moduleDao = (ModuleDao) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.getModulesByProductName(productName);
	}

	@Override
	public List<ModuleDTO> getChildModulesByParentName(String parentName) {
		moduleDao = (ModuleDao) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.getChildModulesByParentName(parentName);
	}

	@Override
	public List<ModuleDTO> getModulesByNames(List<String> moduleNames) {
		moduleDao = (ModuleDao) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.getModulesByNames(moduleNames);
	}

	@Override
	public List<ModuleDTO> getChildModulesByParentId(long id) {
		moduleDao = (ModuleDao) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.getChildModulesByParentId(id);
	}

}
