package com.assetsense.assetsoft.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.assetsense.assetsoft.domain.Lookup;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.dto.TeamDTO;
import com.assetsense.assetsoft.dto.UserDTO;
import com.assetsense.assetsoft.service.AuthService;
import com.assetsense.assetsoft.service.AuthServiceAsync;
import com.assetsense.assetsoft.service.LookupService;
import com.assetsense.assetsoft.service.LookupServiceAsync;
import com.assetsense.assetsoft.service.ProductService;
import com.assetsense.assetsoft.service.ProductServiceAsync;
import com.assetsense.assetsoft.service.TaskService;
import com.assetsense.assetsoft.service.TaskServiceAsync;
import com.assetsense.assetsoft.service.UserService;
import com.assetsense.assetsoft.service.UserServiceAsync;
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
	private final LookupServiceAsync lookupService = GWT.create(LookupService.class);
	private final UserServiceAsync userService = GWT.create(UserService.class);
	private final ProductServiceAsync productService = GWT.create(ProductService.class);

	@Override
	public void onModuleLoad() {
		redirectToLoginPage();
	}

	private void redirectToLoginPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildLoginForm());
		loginForm.getEmailField().setFocus(true);
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
	
	
	private Product convertToProductDao(ProductDTO productDTO){
		Product product = new Product();
		
		product.setProductId(productDTO.getProductId());
		product.setName(productDTO.getName());
		
		if(product.getParentProduct() != null){
			product.setParentProduct(convertToProductDao(productDTO.getParentProductDTO()));
		}
		
		return product;
	}
	
	

	private void saveTask() {
		String title = addEditForm.getTitleField().getText();

		int typeIndex = addEditForm.getWorkItemTypeField().getSelectedIndex();
		String type = addEditForm.getWorkItemTypeField().getValue(typeIndex);

		int stepIndex = addEditForm.getWorkFlowStepField().getSelectedIndex();
		final String step = addEditForm.getWorkFlowStepField().getValue(stepIndex);

		int assignIndex = addEditForm.getAssignedToField().getSelectedIndex();
		final String assign = addEditForm.getAssignedToField().getValue(assignIndex);

		int productIndex = addEditForm.getProductField().getSelectedIndex();
		final String product = addEditForm.getProductField().getValue(productIndex);

		int priorityIndex = addEditForm.getPriorityField().getSelectedIndex();
		final String priority = addEditForm.getPriorityField().getValue(priorityIndex);

		String percent = addEditForm.getPercentField().getText();
		String initialEst = addEditForm.getInitialEstField().getText();
		String remainEst = addEditForm.getRemainingEstField().getText();
		String dueDate = addEditForm.getDueDateField().getText();
		String description = addEditForm.getDescriptionField().getText();

		final Task task = new Task();
		task.setDescription(description);
		task.setTitle(title);
		task.setPercentComplete(percent);
		task.setInitialEstimate(initialEst);
		task.setRemainingEstimate(remainEst);
		task.setDueDate(dueDate);

		List<String> values = new ArrayList<>();
		values.add(type);
		values.add(step);
		values.add(priority);

		lookupService.getLookupsByValues(values, new AsyncCallback<List<Lookup>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("Error at lookupservice");
			}

			@Override
			public void onSuccess(List<Lookup> lookups) {
				// TODO Auto-generated method stub
				task.setType(lookups.get(0));
				task.setStatus(lookups.get(1));
				task.setPriority(lookups.get(2));

				userService.getUserByName(assign, new AsyncCallback<UserDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						Window.alert("Error at userService");
					}

					@Override
					public void onSuccess(UserDTO userDTO) {
						// TODO Auto-generated method stub
						User user = new User();
						user.setUserId(userDTO.getUserId());
						user.setName(userDTO.getName());
						user.setEmail(userDTO.getEmail());
						user.setPassword(userDTO.getPassword());

						if (user.getTeams() != null) {
							Set<Team> teams = new HashSet<>();

							for (TeamDTO team : userDTO.getTeams()) {
								Team teamDao = new Team();
								teamDao.setTeamId(team.getTeamId());
								teamDao.setName(team.getName());
								teams.add(teamDao);
							}

							user.setTeams(teams);
						}

						task.setUser(user);

						String[] wordsArray = product.split(" >> ");
						String productName = wordsArray[wordsArray.length - 1];

						productService.getProductByName(productName, new AsyncCallback<ProductDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								Window.alert("Error at product Service");
							}

							@Override
							public void onSuccess(ProductDTO productDTO) {
								Product product = convertToProductDao(productDTO.findTopMostParent());
								task.setProduct(product.findTopMostParent());

								taskService.saveTask(task, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										Window.alert("Error at adding task");
									}

									@Override
									public void onSuccess(Void result) {
										loadMainPage();
									}

								});
							}
						});
					}

				});

			}
		});

	}
}