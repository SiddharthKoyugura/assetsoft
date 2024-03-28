package com.assetsense.assetsoft.dao;

import java.util.List;

import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.dto.TaskDTO;

public interface TaskDao {
	void saveTask(Task task);

	void deleteTask(Task task);

	void deleteTasksByIds(List<Long> taskIds);

	TaskDTO getTaskById(long id);

	List<TaskDTO> getTasksByUserId(long userId);

	List<TaskDTO> getTasksByPriorityId(long priorityId);

	List<TaskDTO> getTasksByUsername(String username);

	List<TaskDTO> getTasksByLookupValue(String name, String value);

	List<TaskDTO> getTasks();

	void editTaskTitle(long id, String title);

	void editTaskLookup(long id, String name, String value);

	void editTaskUser(long id, String username);

	void editTaskProduct(long id, String productName);

	void editTaskModule(long id, String name, String moduleName);
}
