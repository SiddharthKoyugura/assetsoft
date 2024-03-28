package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.daoImpl.TaskDaoImpl;
import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.assetsense.assetsoft.service.TaskService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TaskServiceImpl extends RemoteServiceServlet implements TaskService {

	private TaskDaoImpl taskDao;

	@Override
	public void saveTask(Task task) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.saveTask(task);
	}

	@Override
	public void deleteTask(Task task) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.deleteTask(task);
	}

	@Override
	public TaskDTO getTaskById(long id) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTaskById(id);
	}

	@Override
	public List<TaskDTO> getTasksByUserId(long userId) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByUserId(userId);
	}

	@Override
	public List<TaskDTO> getTasksByPriorityId(long priorityId) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByPriorityId(priorityId);
	}

	@Override
	public List<TaskDTO> getTasksByUsername(String username) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByUsername(username);
	}

	@Override
	public List<TaskDTO> getTasksByLookupValue(String name, String value) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByLookupValue(name, value);
	}

	@Override
	public List<TaskDTO> getTasksByProductName(String name) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByProductName(name);
	}

	@Override
	public List<TaskDTO> getTasksByModuleName(String name) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByModuleName(name);
	}
	
	@Override
	public List<TaskDTO> getTasksByLookupOrder(String lookupName, Boolean asc) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByLookupOrder(lookupName, asc);
	}

	@Override
	public List<TaskDTO> getTasks() {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasks();
	}

	@Override
	public void editTaskTitle(long id, String title) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskTitle(id, title);
	}

	@Override
	public void editTaskLookup(long id, String name, String value) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskLookup(id, name, value);
	}

	@Override
	public void editTaskUser(long id, String username) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskUser(id, username);
	}

	@Override
	public void editTaskProduct(long id, String productName) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskProduct(id, productName);
	}

	@Override
	public void deleteTasksByIds(List<Long> taskIds) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.deleteTasksByIds(taskIds);
	}

	@Override
	public void editTaskModule(long id, String name, String moduleName) {
		taskDao = (TaskDaoImpl) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskModule(id, name, moduleName);
	}

}
