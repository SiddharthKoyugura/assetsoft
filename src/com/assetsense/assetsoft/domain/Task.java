package com.assetsense.assetsoft.domain;

import java.io.Serializable;

public class Task implements Serializable {
	private static final long serialVersionUID = 1L;
	private long taskId;
	private String title;
	private Lookup type;
	private String description;
	private String initialEstimate;
	private String percentComplete;
	private String remainingEstimate;
	private String startDate;
	private String dueDate;
	private Lookup priority;
	private Lookup status;
	private User user;
	private Product product;
	private Module module;

	public Task() {
	}

	public String getInitialEstimate() {
		return initialEstimate;
	}

	public void setInitialEstimate(String initialEstimate) {
		this.initialEstimate = initialEstimate;
	}

	public String getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(String percentComplete) {
		this.percentComplete = percentComplete;
	}

	public String getRemainingEstimate() {
		return remainingEstimate;
	}

	public void setRemainingEstimate(String remainingEstimate) {
		this.remainingEstimate = remainingEstimate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Lookup getType() {
		return type;
	}

	public void setType(Lookup type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Lookup getPriority() {
		return priority;
	}

	public void setPriority(Lookup priority) {
		this.priority = priority;
	}

	public Lookup getStatus() {
		return status;
	}

	public void setStatus(Lookup status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", type=" + type + ", description=" + description + ", priority=" + priority
				+ ", status=" + status + ", user=" + user + ", product=" + product + "]";
	}

}
