package com.assetsense.assetsoft.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.assetsense.assetsoft.dto.TeamDTO;
import com.assetsense.assetsoft.dto.UserDTO;
import com.assetsense.assetsoft.domain.Lookup;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.ModuleDTO;
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
import com.assetsense.assetsoft.util.TypeConverter;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddEditForm {
	private final TaskDashboard taskDashboard = new TaskDashboard();

	private final TypeConverter typeConverter = new TypeConverter();

	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	private final UserServiceAsync userService = GWT.create(UserService.class);
	private final ProductServiceAsync productService = GWT.create(ProductService.class);
	private final ModuleServiceAsync moduleService = GWT.create(ModuleService.class);
	private final LookupServiceAsync lookupService = GWT.create(LookupService.class);

	private Button saveBtn;
	private Button closeBtn;

	private TextArea descriptionField;
	private ListBox workItemTypeField;
	private ListBox workFlowStepField;
	private ListBox assignedToField;
	private ListBox moduleField;
	private TextBox initialEstField;
	private TextBox dueDateField;
	private TextBox startDateField;
	private TextBox titleField;

	private ListBox productField;
	private ListBox priorityField;
	private TextBox remainingEstField;
	private IntegerBox percentField;

	public void loadAddPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildAddForm());
	}

	public void loadEditPage(long id) {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildEditForm(id));
	}

	private DockLayoutPanel buildAddForm() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");

		dpanel.addNorth(taskDashboard.buildNavBar(), 50);

		vpanel.add(buildFormHeader());
		vpanel.add(buildForm());

		saveBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveTask();
			}
		});

		closeBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				taskDashboard.loadTaskPage();
			}

		});

		dpanel.add(vpanel);

		return dpanel;
	}

	private DockLayoutPanel buildEditForm(final long id) {
		final DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		final VerticalPanel vpanel = new VerticalPanel();

		vpanel.setWidth("100%");

		dpanel.addNorth(taskDashboard.buildNavBar(), 50);

		vpanel.add(buildFormHeader());
		vpanel.add(buildForm());

		saveBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveTask(id);
			}
		});
		closeBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				taskDashboard.loadTaskPage();
			}
		});

		taskService.getTaskById(id, new AsyncCallback<TaskDTO>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(final TaskDTO task) {

				descriptionField.setText(task.getDescription());
				initialEstField.setText(task.getInitialEstimate());
				startDateField.setText(task.getStartDate());
				dueDateField.setText(task.getDueDate());
				titleField.setText(task.getTitle());
				remainingEstField.setText(task.getRemainingEstimate());
				percentField.setText(task.getPercentComplete());

				String selectedWorkType = task.getType().getValue();
				selectListBoxItem(workItemTypeField, selectedWorkType);

				String selectedStatus = task.getStatus().getValue();
				selectListBoxItem(workFlowStepField, selectedStatus);

				String selectedUser = task.getUser().getName();
				selectListBoxItem(assignedToField, selectedUser);

				String selectedProduct = "";
				
				ProductDTO product = task.getProduct();
				String topMostParentName = product.getTopMostParent().getName();
				String childName = product.getName();
				if (topMostParentName.equals(childName)) {
					selectedProduct += childName;
				} else {
					selectedProduct += topMostParentName + " >> " + childName;
				}
				
				selectListBoxItem(productField, selectedProduct);

				String selectedPriority = task.getPriority().getValue();
				selectListBoxItem(priorityField, selectedPriority);
				
				String productName = task.getProduct().getTopMostParent().getName();

				moduleService.getModulesByProductName(productName, new AsyncCallback<List<ModuleDTO>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<ModuleDTO> modules) {
						for (ModuleDTO module : modules) {
							moduleField.addItem(module.getName());
						}
						if (task.getModule() != null) {
							String selectedModule = task.getModule().getName();
							selectListBoxItem(moduleField, selectedModule);
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

	private void saveTask(long... id) {
		String title = titleField.getText();

		String type = workItemTypeField.getSelectedValue();

		final String step = workFlowStepField.getSelectedValue();
		final String assign = assignedToField.getSelectedValue();
		final String product = productField.getSelectedValue();
		final String priority = priorityField.getSelectedValue();
		final String module = moduleField.getSelectedValue();

		String percent = percentField.getText();
		String initialEst = initialEstField.getText();
		String remainEst = remainingEstField.getText();
		String startDate = startDateField.getText();
		String dueDate = dueDateField.getText();
		String description = descriptionField.getText();

		final Task task = new Task();
		if (id.length == 1) {
			task.setTaskId(id[0]);
		}
		task.setDescription(description);
		task.setTitle(title);
		task.setPercentComplete(percent);
		task.setInitialEstimate(initialEst);
		task.setRemainingEstimate(remainEst);
		task.setStartDate(startDate);
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
						
						String[] wordsArray = product.split(" >> ");
						String productName = wordsArray[wordsArray.length - 1];

						productService.getProductByName(productName, new AsyncCallback<ProductDTO>() {

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
												taskDashboard.loadTaskPage();
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

	private DockLayoutPanel buildFormHeader() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		dpanel.getElement().getStyle().setProperty("boxShadow", "0 2px 2px -2px rgba(0,0,0,.4)");
		dpanel.setHeight("50px");
		dpanel.setWidth("100%");

		Label l1 = new Label("Add item page");
		l1.addStyleName("taskLabel");
		l1.getElement().getStyle().setProperty("padding", "15px");
		l1.getElement().getStyle().setProperty("fontSize", "18px");

		saveBtn = new Button("Save");
		saveBtn.addStyleName("customBtn");
		closeBtn = new Button("Close");
		closeBtn.addStyleName("customBtn");

		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.getElement().getStyle().setProperty("padding", "10px");
		hpanel.add(saveBtn);
		hpanel.add(closeBtn);

		dpanel.addWest(l1, 300);
		dpanel.addEast(hpanel, 200);

		return dpanel;
	}

	private VerticalPanel buildForm() {
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");

		vpanel.add(buildFieldPanels());
		vpanel.add(buildDescField());

		return vpanel;
	}

	private VerticalPanel buildDescField() {
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		vpanel.getElement().getStyle().setProperty("padding", "50px 0 0 50px");

		Label l1 = new Label("Description:");
		l1.setStyleName("taskLabel");
		l1.getElement().getStyle().setProperty("marginBottom", "10px");
		descriptionField = new TextArea();
		descriptionField.setStyleName("listBoxStyle");
		descriptionField.setHeight("150px");
		descriptionField.setWidth("90vw");

		vpanel.add(l1);
		vpanel.add(descriptionField);

		return vpanel;
	}

	private HorizontalPanel buildFieldPanels() {
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.getElement().getStyle().setProperty("padding", "60px 100px 0 100px");
		hpanel.setWidth("100%");

		hpanel.add(buildLeftPanel());
		hpanel.add(buildRightPanel());

		return hpanel;
	}

	private VerticalPanel buildLeftPanel() {
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");

		Label l1 = new Label("Work Item Type:");
		l1.setStyleName("mr-5");
		workItemTypeField = new ListBox();
		workItemTypeField.setStyleName("listBoxStyle");
		workItemTypeField.addItem("TASK");
		workItemTypeField.addItem("BUG");
		workItemTypeField.addItem("FEATURE");

		Label l2 = new Label("Workflow step:");
		l2.setStyleName("mr-5");
		workFlowStepField = new ListBox();
		workFlowStepField.setStyleName("listBoxStyle");
		workFlowStepField.addItem("NEW");
		workFlowStepField.addItem("APPROVED");
		workFlowStepField.addItem("IN_PROGRESS");
		workFlowStepField.addItem("DEV_COMPLETE");
		workFlowStepField.addItem("READY_FOR_TESTING");

		Label l3 = new Label("Assigned To:");
		l3.setStyleName("mr-5");

		Label l4 = new Label("Initial Estimate:");
		l4.setStyleName("mr-5");
		initialEstField = new TextBox();
		initialEstField.setStyleName("listBoxStyle");

		Label l5 = new Label("Title:");
		l5.setStyleName("mr-5");
		titleField = new TextBox();
		titleField.setStyleName("listBoxStyle");

		Label l6 = new Label("Start Date:");
		l6.setStyleName("mr-5");
		startDateField = new TextBox();
		startDateField.setStyleName("listBoxStyle");

		l1.setStyleName("taskLabel");
		l2.setStyleName("taskLabel");
		l3.setStyleName("taskLabel");
		l4.setStyleName("taskLabel");
		l5.setStyleName("taskLabel");
		l6.setStyleName("taskLabel");

		l1.addStyleName("imp");
		l2.addStyleName("imp");
		l5.addStyleName("imp");

		final Grid grid = new Grid(6, 2);
		grid.setCellPadding(5);
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		grid.setWidget(0, 0, l5);
		grid.setWidget(1, 0, l1);
		grid.setWidget(2, 0, l2);
		grid.setWidget(3, 0, l3);
		grid.setWidget(4, 0, l4);
		grid.setWidget(5, 0, l6);

		grid.setWidget(0, 1, titleField);
		grid.setWidget(1, 1, workItemTypeField);
		grid.setWidget(2, 1, workFlowStepField);
		grid.setWidget(4, 1, initialEstField);
		grid.setWidget(5, 1, startDateField);

		assignedToField = new ListBox();
		assignedToField.setStyleName("listBoxStyle");

		userService.getUsers(new AsyncCallback<List<UserDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("error at users lookup");
			}

			@Override
			public void onSuccess(List<UserDTO> users) {
				for (UserDTO user : users) {
					assignedToField.addItem(user.getName());
				}
				grid.setWidget(3, 1, assignedToField);
			}

		});

		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");
		grid.getCellFormatter().setStyleName(3, 0, "text-right");
		grid.getCellFormatter().setStyleName(4, 0, "text-right");
		grid.getCellFormatter().setStyleName(5, 0, "text-right");

		vpanel.add(grid);

		return vpanel;
	}

	private VerticalPanel buildRightPanel() {
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");

		productField = new ListBox();
		productField.addItem("<Select>");
		productField.setStyleName("listBoxStyle");

		Label l1 = new Label("Product:");
		l1.setStyleName("mr-5");

		Label l2 = new Label("Priority:");
		l2.setStyleName("mr-5");
		priorityField = new ListBox();
		priorityField.setStyleName("listBoxStyle");
		priorityField.addItem("HIGH");
		priorityField.addItem("MEDIUM");
		priorityField.addItem("LOW");

		Label l3 = new Label("Remaining Estimate:");
		l3.setStyleName("mr-5");
		remainingEstField = new TextBox();
		remainingEstField.setStyleName("listBoxStyle");

		Label l4 = new Label("Percent Complete:");
		l4.setStyleName("mr-5");
		percentField = new IntegerBox();
		percentField.setStyleName("listBoxStyle");

		Label l5 = new Label("Module:");
		l5.setStyleName("mr-5");
		moduleField = new ListBox();
		moduleField.addItem("<Select>");
		moduleField.setStyleName("listBoxStyle");

		Label l6 = new Label("Due Date:");
		l6.setStyleName("mr-5");
		dueDateField = new TextBox();
		dueDateField.setStyleName("listBoxStyle");

		l1.setStyleName("taskLabel");
		l2.setStyleName("taskLabel");
		l3.setStyleName("taskLabel");
		l4.setStyleName("taskLabel");
		l5.setStyleName("taskLabel");
		l6.setStyleName("taskLabel");

		final Grid grid = new Grid(6, 2);
		grid.setCellPadding(5);
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		grid.setWidget(0, 0, l1);
		grid.setWidget(1, 0, l5);
		grid.setWidget(2, 0, l2);
		grid.setWidget(3, 0, l3);
		grid.setWidget(4, 0, l4);
		grid.setWidget(5, 0, l6);

		grid.setWidget(1, 1, moduleField);
		grid.setWidget(2, 1, priorityField);
		grid.setWidget(3, 1, remainingEstField);
		grid.setWidget(4, 1, percentField);
		grid.setWidget(5, 1, dueDateField);

		productService.getProducts(new AsyncCallback<List<ProductDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ProductDTO> products) {
				for (ProductDTO product : products) {
					String topMostParentName = product.getTopMostParent().getName();
					String childName = product.getName();
					if (topMostParentName.equals(childName)) {
						productField.addItem(childName);
					} else {
						productField.addItem(topMostParentName + " >> " + childName);
					}
				}
				grid.setWidget(0, 1, productField);
			}

		});

		productField.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				updateModuleField();
			}
		});

		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");
		grid.getCellFormatter().setStyleName(3, 0, "text-right");
		grid.getCellFormatter().setStyleName(4, 0, "text-right");
		grid.getCellFormatter().setStyleName(5, 0, "text-right");

		vpanel.add(grid);

		return vpanel;
	}

	private void updateModuleField() {
		String product = productField.getSelectedValue();
		String[] wordsArray = product.split(" >> ");
		String productName = wordsArray[0];
		moduleField.clear();
		moduleField.addItem("<Select>");
		moduleService.getModulesByProductName(productName, new AsyncCallback<List<ModuleDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ModuleDTO> modules) {
				for (ModuleDTO module : modules) {
					moduleField.addItem(module.getName());
				}
			}

		});
	}

}
