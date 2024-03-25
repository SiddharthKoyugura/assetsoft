package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.dao.TaskDao;
import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.assetsense.assetsoft.service.TaskService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TaskServiceImpl extends RemoteServiceServlet implements TaskService {
	
	private TaskDao taskDao;
	
	@Override
	public void saveTask(Task task) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.saveTask(task);
	}

	@Override
	public void deleteTask(Task task) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.deleteTask(task);
	}

	@Override
	public TaskDTO getTaskById(long id) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTaskById(id);
	}

	@Override
	public List<TaskDTO> getTasksByUserId(long userId) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByUserId(userId);
	}

	@Override
	public List<TaskDTO> getTasksByPriorityId(long priorityId) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByPriroityId(priorityId);
	}
	
	@Override
	public List<TaskDTO> getTasksByUsername(String username) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByUsername(username);
	}

	@Override
	public List<TaskDTO> getTasks() {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasks();
	}

	@Override
	public void editTaskTitle(long id, String title) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskTitle(id, title);
	}

	@Override
	public void editTaskLookup(long id, String name, String value) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskLookup(id, name, value);
	}

	@Override
	public void editTaskUser(long id, String username) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskUser(id, username);
	}

	@Override
	public void editTaskProduct(long id, String productName) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskProduct(id, productName);
	}

	@Override
	public void deleteTasksByIds(List<Long> taskIds) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.deleteTasksByIds(taskIds);	
	}

	@Override
	public void editTaskModule(long id, String name, String moduleName) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.editTaskModule(id, name, moduleName);
	}

}
