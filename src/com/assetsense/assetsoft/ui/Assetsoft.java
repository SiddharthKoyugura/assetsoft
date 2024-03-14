package com.assetsense.assetsoft.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.assetsense.assetsoft.domain.Lookup;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.dto.TaskDTO;
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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Assetsoft implements EntryPoint {
	private final TaskDashboard taskDashboard = new TaskDashboard();
	private final AddEditForm addEditForm = new AddEditForm();
	private final LoginForm loginForm = new LoginForm();
	private final AddProduct addProduct = new AddProduct();

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

	private void loadAddPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildAddForm());
	}

	private void loadEditPage(long id) {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildEditForm(id));
	}

	private void loadAddProductPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildAddProductPage());
	}

	private DockLayoutPanel buildAddProductPage() {
		final DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		VerticalPanel vpanel = new VerticalPanel();

		vpanel.setWidth("100%");
		dpanel.addNorth(taskDashboard.buildNavBar(), 50);

		vpanel.add(addProduct.buildAddProductForm());

		addProduct.setSubmitBtnHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String parentProductText = addProduct.getProductField().getSelectedValue();
				final String newProduct = addProduct.getNewProductField().getText();
				if (newProduct.trim().length() == 0) {
					loadMainPage();
				} else {
					if (parentProductText != "NULL") {
						String[] wordsArray = parentProductText.split(" >> ");
						final String parentProductName = wordsArray[wordsArray.length - 1];

						productService.getProductByName(parentProductName, new AsyncCallback<ProductDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(ProductDTO productDTO) {
								Product parentProduct = convertToProductDao(productDTO);
								Product product = new Product();
								product.setName(newProduct);
								product.setParentProduct(parentProduct);
								saveProduct(product);
							}

						});
					} else {

						Product product = new Product();
						product.setName(newProduct);
						saveProduct(product);
					}
				}
			}

		});

		dpanel.add(vpanel);

		return dpanel;
	}

	private void saveProduct(Product product) {
		productService.saveProduct(product, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				loadMainPage();
			}

		});
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
			public void onSuccess(TaskDTO task) {

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
				dpanel.add(vpanel);
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
		dpanel.add(taskDashboard.buildTaskDashboard());

		taskDashboard.getCheckedBoxes().clear();

		taskDashboard.setAddBtnHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadAddPage();
			}

		});

		taskDashboard.setEditBtnHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Map<Long, Boolean> checkedBoxes = taskDashboard.getCheckedBoxes();
				if (checkedBoxes.size() == 1) {
					Iterator<Long> iterator = checkedBoxes.keySet().iterator();
					loadEditPage(iterator.next());

				} else {
					Window.alert("Please select exactly one checkbox");
				}
			}

		});

		taskDashboard.setDeleteBtnHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				final Map<Long, Boolean> checkedBoxes = taskDashboard.getCheckedBoxes();
				if (checkedBoxes.size() == 0) {
					Window.alert("Please select atleast one checkbox");
				} else {
					Set<Long> keys = checkedBoxes.keySet();
					List<Long> taskIds = new ArrayList<>();
					for (long key : keys) {
						taskIds.add(key);
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

		taskDashboard.setAddProductBtnHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadAddProductPage();
			}

		});

		taskDashboard.setHeaderCheckBoxHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				// TODO Auto-generated method stub
				Map<Long, CheckBox> checkBoxes = taskDashboard.getCheckBoxes();
				Map<Long, Boolean> checkedBoxes = taskDashboard.getCheckedBoxes();
				if (event.getValue()) {
					for (long id : checkBoxes.keySet()) {
						CheckBox cb = checkBoxes.get(id);
						cb.setValue(true);
						checkedBoxes.put(id, true);
					}
				} else {
					for (long id : checkBoxes.keySet()) {
						CheckBox cb = checkBoxes.get(id);
						cb.setValue(false);
						checkedBoxes.remove(id);
					}
				}
			}

		});

		return dpanel;
	}

	private Product convertToProductDao(ProductDTO productDTO) {
		Product product = new Product();

		product.setProductId(productDTO.getProductId());
		product.setName(productDTO.getName());

		if (product.getParentProduct() != null) {
			product.setParentProduct(convertToProductDao(productDTO.getParentProductDTO()));
		}

		return product;
	}

	private void saveTask(long... id) {
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