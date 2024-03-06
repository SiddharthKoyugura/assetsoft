package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

import com.assetsense.assetsoft.domain.Task;

public class TaskDao {
	HibernateTemplate template;

	public HibernateTemplate getTemplate() {
		return template;
	}

	public void setTemplate(HibernateTemplate template) {
		this.template = template;
	}

	// method to add task
	public void saveTask(Task task) {
		template.save(task);
	}

	// method to update task
	public void updateTask(Task task) {
		template.save(task);
	}

	// method to delete task
	public void deleteTask(Task task) {
		template.delete(task);
	}

	// method to return one task of given id
	public Task getTaskById(long id) {
		Task task = (Task) template.get(Task.class, id);
		return task;
	}

	// method to return all tasks
	public List<Task> getTasks() {
		List<Task> tasks = new ArrayList<Task>();
		tasks = template.loadAll(Task.class);
		return tasks;
	}
}
