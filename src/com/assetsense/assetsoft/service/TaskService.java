package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Task;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {
	void saveTask(Task task);
	void deleteTask(Task task);
	Task getTaskById(long id);
	List<Task> getTasksByUserId(long userId);
	List<Task> getTasksByPriorityId(long priorityId);
	List<Task> getTasks();
}
