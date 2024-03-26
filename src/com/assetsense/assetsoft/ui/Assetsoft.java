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
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.assetsense.assetsoft.dto.TeamDTO;
import com.assetsense.assetsoft.dto.UserDTO;
import com.assetsense.assetsoft.service.AuthService;
import com.assetsense.assetsoft.service.AuthServiceAsync;
import com.assetsense.assetsoft.service.LookupService;
import com.assetsense.assetsoft.service.LookupServiceAsync;
import com.assetsense.assetsoft.service.ModuleService;
import com.assetsense.assetsoft.service.ModuleServiceAsync;
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
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class Assetsoft implements EntryPoint {
	private final TaskDashboard taskDashboard = new TaskDashboard();
	private final AddEditForm addEditForm = new AddEditForm();
	private final LoginForm loginForm = new LoginForm();

	private final DtoToDao typeConverter = new DtoToDao();

	private final AuthServiceAsync authService = GWT.create(AuthService.class);
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	private final LookupServiceAsync lookupService = GWT.create(LookupService.class);
	private final UserServiceAsync userService = GWT.create(UserService.class);
	private final ProductServiceAsync productService = GWT.create(ProductService.class);
	private final ModuleServiceAsync moduleService = GWT.create(ModuleService.class);

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

		authService.authenticateUser(email, password, new AsyncCallback<UserDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("DB error");
			}

			@Override
			public void onSuccess(UserDTO user) {
				if (user != null) {
					taskDashboard.setNavBtnName(user.getName());
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

	private void loadAddPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildAddForm());
	}

	private void loadEditPage(long id) {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildEditForm(id));
	}

	private DockLayoutPanel buildEditForm(final long id) {
		final DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		final VerticalPanel vpanel = new VerticalPanel();

		vpanel.setWidth("100%");

		dpanel.addNorth(taskDashboard.buildNavBar(), 50);

		vpanel.add(addEditForm.buildFormHeader());
		vpanel.add(addEditForm.buildForm());

		addEditForm.setSaveBtnHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveTask(id);
			}
		});
		addEditForm.setCloseBtnHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadMainPage();
			}
		});

		taskService.getTaskById(id, new AsyncCallback<TaskDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(final TaskDTO task) {

				addEditForm.getDescriptionField().setText(task.getDescription());
				addEditForm.getInitialEstField().setText(task.getInitialEstimate());
				addEditForm.getDueDateField().setText(task.getDueDate());
				addEditForm.getTitleField().setText(task.getTitle());
				addEditForm.getRemainingEstField().setText(task.getRemainingEstimate());
				addEditForm.getPercentField().setText(task.getPercentComplete());

				String selectedWorkType = task.getType().getValue();
				selectListBoxItem(addEditForm.getWorkItemTypeField(), selectedWorkType);

				String selectedStatus = task.getStatus().getValue();
				selectListBoxItem(addEditForm.getWorkFlowStepField(), selectedStatus);

				String selectedUser = task.getUser().getName();
				selectListBoxItem(addEditForm.getAssignedToField(), selectedUser);

				String selectedProduct = task.getProduct().getName();
				selectListBoxItem(addEditForm.getProductField(), selectedProduct);

				String selectedPriority = task.getPriority().getValue();
				selectListBoxItem(addEditForm.getPriorityField(), selectedPriority);

				moduleService.getModulesByProductName(selectedProduct, new AsyncCallback<List<ModuleDTO>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<ModuleDTO> modules) {
						for (ModuleDTO module : modules) {
							addEditForm.getModuleField().addItem(module.getName());
						}
						if (task.getModule() != null) {
							String selectedModule = task.getModule().getName();
							selectListBoxItem(addEditForm.getModuleField(), selectedModule);
						}
						dpanel.add(vpanel);
					}

				});

			}

		});

		return dpanel;
	}

	private void selectListBoxItem(ListBox listBox, String value) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.getValue(i).equals(value)) {
				listBox.setSelectedIndex(i);
				break;
			}
		}
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

	private DockLayoutPanel buildAddForm() {
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
		taskDashboard.resetFilters();
		dpanel.add(taskDashboard.buildTaskDashboard());
		taskDashboard.getSelectedRows().clear();
		taskDashboard.resetEditableRow();

		taskDashboard.setAddBtnHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadAddPage();
			}

		});

		taskDashboard.getFlexTable().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.preventDefault();
				if (taskDashboard.getEditableRow() == -1) {
					Cell cell = taskDashboard.getFlexTable().getCellForEvent(event);
					if (cell != null) {
						int row = cell.getRowIndex();
						if (row != 0) {
							long id = Long.parseLong(taskDashboard.getFlexTable().getText(row, 0));
							loadEditPage(id);
						}
					}
				}
			}
		});

		taskDashboard.setDeleteBtnHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final Set<Integer> selectedRows = taskDashboard.getSelectedRows();
				if (selectedRows.size() == 0) {
					Window.alert("Please select atleast one task");
				} else {
					List<Long> taskIds = new ArrayList<>();
					for (int row : selectedRows) {
						taskIds.add(Long.parseLong(taskDashboard.getFlexTable().getText(row, 0)));
					}
					taskService.deleteTasksByIds(taskIds, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(Void result) {
							Window.alert("Task[s] deleted");
							loadMainPage();
						}
					});
				}
			}

		});

		return dpanel;
	}

	private void saveTask(long... id) {
		String title = addEditForm.getTitleField().getText();

		String type = addEditForm.getWorkItemTypeField().getSelectedValue();

		final String step = addEditForm.getWorkFlowStepField().getSelectedValue();
		final String assign = addEditForm.getAssignedToField().getSelectedValue();
		final String product = addEditForm.getProductField().getSelectedValue();
		final String priority = addEditForm.getPriorityField().getSelectedValue();
		final String module = addEditForm.getModuleField().getSelectedValue();

		String percent = addEditForm.getPercentField().getText();
		String initialEst = addEditForm.getInitialEstField().getText();
		String remainEst = addEditForm.getRemainingEstField().getText();
		String dueDate = addEditForm.getDueDateField().getText();
		String description = addEditForm.getDescriptionField().getText();

		final Task task = new Task();
		if (id.length == 1) {
			task.setTaskId(id[0]);
		}
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
				Window.alert("Error at lookupservice");
			}

			@Override
			public void onSuccess(List<Lookup> lookups) {
				task.setType(lookups.get(0));
				task.setStatus(lookups.get(1));
				task.setPriority(lookups.get(2));

				userService.getUserByName(assign, new AsyncCallback<UserDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error at userService");
					}

					@Override
					public void onSuccess(UserDTO userDTO) {
						User user = new User();
						user.setUserId(userDTO.getUserId());
						user.setName(userDTO.getName());
						user.setEmail(userDTO.getEmail());
						user.setPassword(userDTO.getPassword());

						if (user.getTeams() != null && user.getTeams().size() > 0) {
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

						productService.getProductByName(product, new AsyncCallback<ProductDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Error at product Service");
							}

							@Override
							public void onSuccess(ProductDTO productDTO) {
								Product product = typeConverter.convertToProductDao(productDTO);
								task.setProduct(product);
								moduleService.getModuleByName(module, new AsyncCallback<ModuleDTO>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										Window.alert("Error at moduleService");
									}

									@Override
									public void onSuccess(ModuleDTO moduleDTO) {
										if (moduleDTO != null) {
											task.setModule(typeConverter.convertToModuleDao(moduleDTO));

										} else {
											task.setModule(null);
										}

										taskService.saveTask(task, new AsyncCallback<Void>() {

											@Override
											public void onFailure(Throwable caught) {
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
		});
	}
}