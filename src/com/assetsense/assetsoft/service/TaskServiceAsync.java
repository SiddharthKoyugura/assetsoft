package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TaskServiceAsync {
	void saveTask(Task task, AsyncCallback<Void> callback);
	void deleteTask(Task task, AsyncCallback<Void> callback);
	void deleteTasksByIds(List<Long> taskIds, AsyncCallback<Void> callback);
	void getTaskById(long taskIds, AsyncCallback<TaskDTO> callback);
	void getTasksByUserId(long userId, AsyncCallback<List<TaskDTO>> callback);
	void getTasksByPriorityId(long priorityId, AsyncCallback<List<TaskDTO>> callback);
	void getTasks(AsyncCallback<List<TaskDTO>> callback);
	void editTaskTitle(long id, String title, AsyncCallback<Void> callback);
	void editTaskLookup(long id, String name, String value, AsyncCallback<Void> callback);
	void editTaskUser(long id, String username, AsyncCallback<Void> callback);
	void editTaskProduct(long id, String productName, AsyncCallback<Void> callback);
	void editTaskModule(long id, String name, String moduleName, AsyncCallback<Void> callback);
}
