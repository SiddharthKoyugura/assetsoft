package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.daoImpl.ModuleDaoImpl;
import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.assetsense.assetsoft.service.ModuleService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ModuleServiceImpl extends RemoteServiceServlet implements ModuleService {
	private static final long serialVersionUID = 1L;
	private ModuleDaoImpl moduleDao;

	@Override
	public List<ModuleDTO> getModules() {
		moduleDao = (ModuleDaoImpl) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.getModules();
	}

	@Override
	public ModuleDTO saveModule(Module module) {
		moduleDao = (ModuleDaoImpl) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.saveModule(module);
	}

	@Override
	public List<ModuleDTO> getModulesByProductName(String productName) {
		moduleDao = (ModuleDaoImpl) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.getModulesByProductName(productName);
	}

	@Override
	public List<ModuleDTO> getChildModulesByParentName(String parentName) {
		moduleDao = (ModuleDaoImpl) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.getChildModulesByParentName(parentName);
	}

	@Override
	public ModuleDTO getModuleByName(String module) {
		moduleDao = (ModuleDaoImpl) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.getModuleByName(module);
	}

	@Override
	public List<ModuleDTO> getChildModulesByParentId(long id) {
		moduleDao = (ModuleDaoImpl) ApplicationContextListener.applicationContext.getBean("moduleDao");
		return moduleDao.getChildModulesByParentId(id);
	}

}
