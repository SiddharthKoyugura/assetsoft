package com.assetsense.assetsoft.ui;

import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.service.AuthService;
import com.assetsense.assetsoft.service.AuthServiceAsync;
import com.assetsense.assetsoft.service.TaskService;
import com.assetsense.assetsoft.service.TaskServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Assetsoft implements EntryPoint {
	private final TaskDashboard taskDashboard = new TaskDashboard();
	private final AddEditForm addEditForm = new AddEditForm();
	private final LoginForm loginForm = new LoginForm();

	private final AuthServiceAsync authService = GWT.create(AuthService.class);
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);

	@Override
	public void onModuleLoad() {
		// checkAuthentication();
		// User user = new User();
		// user.setName("sid");
		// user.setEmail("sid@gmail.com");
		// user.setPassword("123");
		// user.setTeams(null);
		// Window.alert("Hello");
		// userService.saveUser(user, new AsyncCallback<Void>(){
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// // TODO Auto-generated method stub
		// Window.alert("Error");
		// }
		//
		// @Override
		// public void onSuccess(Void result) {
		// // TODO Auto-generated method stub
		// Window.alert("NO error");
		// }
		//
		// });
		redirectToLoginPage();
	}

	// private void checkAuthentication() {
	// athService.checkAuthentication(new AsyncCallback<Boolean>() {
	// @Override
	// public void onSuccess(Boolean isAuthenticated) {
	// if (isAuthenticated) {
	// loadMainPage();
	// } else {
	// RootLayoutPanel.get().add(buildLoginForm());
	// }
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// // Handle RPC call failure
	// Window.alert("Failed to authenticate. Please try again later.");
	// redirectToLoginPage();
	// }
	// });
	// }

	private void redirectToLoginPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildLoginForm());
	}

	private void authenticateUser() {
		String email = loginForm.getEmail();
		String password = loginForm.getPassword();

		authService.authenticateUser(email, password, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("DB error");
			}

			@Override
			public void onSuccess(String username) {
				if (username != null) {
					taskDashboard.setNavBtnName(username);
					loadMainPage();
				} else {
					Window.alert("Invalid Credentials");
				}
			}
		});
	}

	private void loadMainPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildTaskPage());
	}

	private void loadAddEditPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildAddEditForm());
	}

	private DockLayoutPanel buildLoginForm() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		VerticalPanel vpanel = new VerticalPanel();

		vpanel.setWidth("100%");

		dpanel.addNorth(taskDashboard.buildNavBar(), 50);

		vpanel.add(loginForm.buildLoginForm());
		
		loginForm.setLoginHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				authenticateUser();
			}
		});

		dpanel.add(vpanel);

		return dpanel;
	}

	private DockLayoutPanel buildAddEditForm() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");

		dpanel.addNorth(taskDashboard.buildNavBar(), 50);

		vpanel.add(addEditForm.buildFormHeader());
		vpanel.add(addEditForm.buildForm());

		addEditForm.setSaveBtnHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveTask();
			}
		});

		addEditForm.setCloseBtnHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadMainPage();
			}

		});

		dpanel.add(vpanel);

		return dpanel;
	}

	private DockLayoutPanel buildTaskPage() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);

		dpanel.setSize("100%", "100%");

		dpanel.addNorth(taskDashboard.buildNavBar(), 48);
		dpanel.addWest(taskDashboard.buildLeftSidebar(), 240);
		dpanel.add(taskDashboard.buildTaskDashboard());

		taskDashboard.setBtnHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadAddEditPage();
			}

		});

		return dpanel;
	}

	private void saveTask() {
		String title = addEditForm.getTitleField().getText();

		int typeIndex = addEditForm.getWorkItemTypeField().getSelectedIndex();
		String type = addEditForm.getWorkItemTypeField().getValue(typeIndex);

		int stepIndex = addEditForm.getWorkFlowStepField().getSelectedIndex();
		String step = addEditForm.getWorkFlowStepField().getValue(stepIndex);

		int assignIndex = addEditForm.getAssignedToField().getSelectedIndex();
		String assign = addEditForm.getAssignedToField().getValue(assignIndex);

		int productIndex = addEditForm.getProductField().getSelectedIndex();
		String product = addEditForm.getAssignedToField().getValue(productIndex);

		int priorityIndex = addEditForm.getPriorityField().getSelectedIndex();
		String priority = addEditForm.getPriorityField().getValue(priorityIndex);

		String percent = addEditForm.getPercentField().getText();
		String initialEst = addEditForm.getInitialEstField().getText();
		String remainEst = addEditForm.getRemainingEstField().getText();
		String dueDate = addEditForm.getDueDateField().getText();
		String description = addEditForm.getDescriptionField().getText();

		Task task = new Task();
		task.setDescription(description);
		task.setTitle(title);
		task.setPercentComplete(percent);
		task.setInitialEstimate(initialEst);
		task.setRemainingEstimate(remainEst);
		task.setDueDate(dueDate);

		taskService.saveTask(task, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("Error at adding task");
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				Window.alert("Task Added");
				loadMainPage();
			}

		});
	}
}