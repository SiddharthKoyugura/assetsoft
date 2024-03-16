package com.assetsense.assetsoft.dto;

import java.io.Serializable;

public class TaskDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private long taskId;
	private String title;
	private LookupDTO type;
	private String description;
	private String initialEstimate;
	private String percentComplete;
	private String remainingEstimate;
	private String dueDate;
	private LookupDTO priority;
	private LookupDTO status;
	private UserDTO user;
	private ProductDTO product;
	private ModuleDTO module;
	private ModuleDTO subSystem;

	public TaskDTO() {

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

	public LookupDTO getType() {
		return type;
	}

	public void setType(LookupDTO type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public LookupDTO getPriority() {
		return priority;
	}

	public void setPriority(LookupDTO priority) {
		this.priority = priority;
	}

	public LookupDTO getStatus() {
		return status;
	}

	public void setStatus(LookupDTO status) {
		this.status = status;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public ModuleDTO getModule() {
		return module;
	}

	public void setModule(ModuleDTO module) {
		this.module = module;
	}

	public ModuleDTO getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(ModuleDTO subSystem) {
		this.subSystem = subSystem;
	}

	@Override
	public String toString() {
		return "TaskDTO [taskId=" + taskId + ", title=" + title + ", type=" + type + ", description=" + description
				+ ", initialEstimate=" + initialEstimate + ", percentComplete=" + percentComplete
				+ ", remainingEstimate=" + remainingEstimate + ", dueDate=" + dueDate + ", priority=" + priority
				+ ", status=" + status + ", user=" + user + ", product=" + product + "]";
	}

}
