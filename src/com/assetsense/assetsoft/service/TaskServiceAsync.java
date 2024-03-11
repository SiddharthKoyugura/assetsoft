package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TaskServiceAsync {
	void saveTask(Task task, AsyncCallback<Void> callback);
	void deleteTask(Task task, AsyncCallback<Void> callback);
	void getTaskById(long id, AsyncCallback<Task> callback);
	void getTasksByUserId(long userId, AsyncCallback<List<TaskDTO>> callback);
	void getTasksByPriorityId(long priorityId, AsyncCallback<List<TaskDTO>> callback);
	void getTasks(AsyncCallback<List<TaskDTO>> callback);
}
