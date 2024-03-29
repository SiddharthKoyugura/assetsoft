package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TaskServiceAsyncImpl implements TaskServiceAsync {

	@Override
	public void saveTask(Task task, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTask(Task task, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTasksByIds(List<Long> taskIds, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTaskById(long taskIds, AsyncCallback<TaskDTO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTasksByUserId(long userId, AsyncCallback<List<TaskDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTasksByPriorityId(long priorityId, AsyncCallback<List<TaskDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTasksByUsername(String username, AsyncCallback<List<TaskDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTasksByLookupValue(String name, String value, AsyncCallback<List<TaskDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTasksByProductName(String name, AsyncCallback<List<TaskDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTasksByModuleName(String name, AsyncCallback<List<TaskDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTasksByLookupOrder(String LookupName, Boolean asc, AsyncCallback<List<TaskDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTasksBySearchString(String attrName, String searchValue, AsyncCallback<List<TaskDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTasks(AsyncCallback<List<TaskDTO>> callback) {
		
	}

	@Override
	public void editTaskTitle(long id, String title, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editTaskLookup(long id, String name, String value, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editTaskUser(long id, String username, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editTaskProduct(long id, String productName, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editTaskModule(long id, String name, String moduleName, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

}
