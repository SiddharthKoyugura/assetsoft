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
		// TODO Auto-generated method stub
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.saveTask(task);
	}

	@Override
	public void deleteTask(Task task) {
		// TODO Auto-generated method stub
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		taskDao.deleteTask(task);
	}

	@Override
	public Task getTaskById(long id) {
		// TODO Auto-generated method stub
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTaskById(id);
	}

	@Override
	public List<TaskDTO> getTasksByUserId(long userId) {
		// TODO Auto-generated method stub
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByUserId(userId);
	}

	@Override
	public List<TaskDTO> getTasksByPriorityId(long priorityId) {
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasksByPriroityId(priorityId);
	}

	@Override
	public List<TaskDTO> getTasks() {
		// TODO Auto-generated method stub
		taskDao = (TaskDao) ApplicationContextListener.applicationContext.getBean("taskDao");
		return taskDao.getTasks();
	}
}
