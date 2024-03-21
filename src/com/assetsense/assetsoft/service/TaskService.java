package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("task")
public interface TaskService extends RemoteService {
	void saveTask(Task task);
	void deleteTask(Task task);
	void deleteTasksByIds(List<Long> taskIds);
	TaskDTO getTaskById(long id);
	List<TaskDTO> getTasksByUserId(long userId);
	List<TaskDTO> getTasksByPriorityId(long priorityId);
	List<TaskDTO> getTasks();
	void editTaskTitle(long id, String title);
	void editTaskLookup(long id, String name, String value);
	void editTaskUser(long id, String username);
	void editTaskProduct(long id, String productName);
	void editTaskModule(long id, String name, String moduleName);
}
