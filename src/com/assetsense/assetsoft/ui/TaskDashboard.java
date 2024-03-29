package com.assetsense.assetsoft.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.assetsense.assetsoft.domain.Lookup;
import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.assetsense.assetsoft.dto.TeamDTO;
import com.assetsense.assetsoft.dto.UserDTO;
import com.assetsense.assetsoft.service.LookupService;
import com.assetsense.assetsoft.service.LookupServiceAsync;
import com.assetsense.assetsoft.service.ModuleService;
import com.assetsense.assetsoft.service.ModuleServiceAsync;
import com.assetsense.assetsoft.service.ProductService;
import com.assetsense.assetsoft.service.ProductServiceAsync;
import com.assetsense.assetsoft.service.TaskService;
import com.assetsense.assetsoft.service.TaskServiceAsync;
import com.assetsense.assetsoft.service.TeamService;
import com.assetsense.assetsoft.service.TeamServiceAsync;
import com.assetsense.assetsoft.service.UserService;
import com.assetsense.assetsoft.service.UserServiceAsync;
import com.assetsense.assetsoft.util.TypeConverter;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaskDashboard {
	private final TypeConverter typeConverter = new TypeConverter();

	private final UserServiceAsync userService = GWT.create(UserService.class);
	private final TeamServiceAsync teamService = GWT.create(TeamService.class);
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	private final ProductServiceAsync productService = GWT.create(ProductService.class);
	private final ModuleServiceAsync moduleService = GWT.create(ModuleService.class);
	private final LookupServiceAsync lookupService = GWT.create(LookupService.class);

	private AddEditForm addEditForm;

	private Button navBtn;
	private Button addBtn;
	private Button editBtn;
	private Button deleteBtn;
	private Button addProductBtn;
	private Button addModuleBtn;

	private TreeItem productRootTree;
	private TreeItem selectedProductItem;
	private ProductDTO selectedProduct;

	private TreeItem moduleRootTree;
	private TreeItem selectedModuleItem;
	private ModuleDTO selectedModule;
	private ProductDTO selectedModuleProduct;

	private TreeItem userRootTree;
	private Boolean allTasksRendered = false;
	private String selectedUserName;
	private Boolean isTeamSelected = false;
	private Team selectedTeam;

	private TextBox searchField;
	private ListBox itemSearch;
	private String attrName;

	private List<TaskDTO> currentStateTasks;

	// Task Table section
	private DoubleClickTable flexTable;
	private PopupPanel popupPanel;
	private Boolean isIdASCOrder = true;
//	private Boolean isLookupASCOrder = true;

	private int rowIndex = 1;
	private Set<Integer> selectedRows = new HashSet<>();
	private int editableRow = -1;

	public void setNavBtnName(String name) {
		getNavBtn().setText(name);
	}

	private Button getNavBtn() {
		if (navBtn == null) {
			navBtn = new Button();
		}
		return navBtn;
	}

	private void resetEditableRow() {
		editableRow = -1;
	}

	private void resetFilters() {
		selectedUserName = null;
		allTasksRendered = false;
	}

	Tree.Resources customTreeResources = new Tree.Resources() {
		@Override
		public ImageResource treeClosed() {
			return MyTreeResources.INSTANCE.treeClosed();
		}

		@Override
		public ImageResource treeLeaf() {
			return MyTreeResources.INSTANCE.treeLeaf();
		}

		@Override
		public ImageResource treeOpen() {
			return MyTreeResources.INSTANCE.treeOpen();
		}
	};

	public void loadTaskPage() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(buildTaskPage());
	}

	private DockLayoutPanel buildTaskPage() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		addEditForm = new AddEditForm();

		dpanel.setSize("100%", "100%");

		dpanel.addNorth(buildNavBar(), 48);
		dpanel.addWest(buildLeftSidebar(), 240);
		resetFilters();
		dpanel.add(buildTaskDashboard());
		selectedRows.clear();
		resetEditableRow();

		addBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addEditForm.loadAddPage();
			}

		});

		flexTable.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.preventDefault();
				if (editableRow == -1) {
					Cell cell = flexTable.getCellForEvent(event);
					if (cell != null) {
						int row = cell.getRowIndex();
						if (row != 0) {
							long id = Long.parseLong(flexTable.getText(row, 0));
							addEditForm.loadEditPage(id);
						}
					}
				}
			}
		});

		deleteBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedRows.size() == 0) {
					Window.alert("Please select atleast one task");
				} else {
					List<Long> taskIds = new ArrayList<>();
					for (int row : selectedRows) {
						taskIds.add(Long.parseLong(flexTable.getText(row, 0)));
					}
					taskService.deleteTasksByIds(taskIds, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(Void result) {
							Window.alert("Task[s] deleted");
							loadTaskPage();
						}
					});
				}
			}

		});

		return dpanel;
	}

	public HorizontalPanel buildNavBar() {
		HorizontalPanel navbar = new HorizontalPanel();
		navbar.setWidth("100%");

		Label l1 = new Label("Assetsoft");
		l1.setStyleName("navLabel");

		navbar.add(l1);

		if (navBtn != null) {
			navBtn.setStyleName("navBtn");
			navbar.add(navBtn);
		}

		// Style the navbar
		navbar.setStyleName("navbar");
		return navbar;
	}

	// ################ Left Side bar #####################

	// Modules section
	private Tree buildModulesTree() {
		final Tree treeWidget = new Tree(customTreeResources);

		treeWidget.setStyleName("parentTree");

		Label label = new Label("All Modules");
		label.getElement().getStyle().setProperty("cursor", "pointer");
		moduleRootTree = new TreeItem(label);
		moduleRootTree.setStyleName("treeHeading");

		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearSelection(productRootTree);
				clearSelection(userRootTree);
			}
		});

		treeWidget.addItem(moduleRootTree);

		productService.getProducts(new AsyncCallback<List<ProductDTO>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(List<ProductDTO> products) {
				// TODO Auto-generated method stub
				for (final ProductDTO product : products) {
					if (product.getParentProductDTO() == null) {
						Label label = new Label(product.getName());
						label.getElement().getStyle().setProperty("cursor", "pointer");
						final TreeItem rootItem = new TreeItem(label);
						rootItem.setStyleName("treeHeading");

						label.addClickHandler(moduleLabelClickHandler(rootItem, null, product));

						moduleService.getModulesByProductName(product.getName(), new AsyncCallback<List<ModuleDTO>>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Hierarchy failed");
							}

							@Override
							public void onSuccess(List<ModuleDTO> moduleDTOs) {
								for (final ModuleDTO module : moduleDTOs) {
									if (module.getParentModuleDTO() == null) {
										Label label = new Label(module.getName());
										label.getElement().getStyle().setProperty("cursor", "pointer");
										final TreeItem moduleItem = new TreeItem(label);
										moduleItem.setStyleName("treeHeading");
										label.addClickHandler(
												moduleLabelClickHandler(moduleItem, module, module.getProductDTO()));
										buildSubModulesTree(module, moduleItem);
										rootItem.addItem(moduleItem);
										moduleItem.setState(true);
									}
								}
								rootItem.setState(true);
							}

						});

						moduleRootTree.addItem(rootItem);
					}
				}

				moduleRootTree.setState(true);
			}

		});
		return treeWidget;
	}

	private void buildSubModulesTree(final ModuleDTO module, final TreeItem parentItem) {
		moduleService.getChildModulesByParentId(module.getModuleId(), new AsyncCallback<List<ModuleDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ModuleDTO> modules) {
				for (final ModuleDTO child : modules) {
					Label label = new Label(child.getName());
					label.getElement().getStyle().setProperty("cursor", "pointer");
					final TreeItem childItem = new TreeItem(label);
					label.addClickHandler(moduleLabelClickHandler(childItem, module, module.getProductDTO()));
					parentItem.addItem(childItem);
					buildSubModulesTree(child, childItem);
				}
			}

		});
	}

	private void addModuleHandler(final TreeItem treeItem, final ModuleDTO moduleDTO, final ProductDTO productDTO) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setAnimationEnabled(true);

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setStyleName("form-container");
		vpanel.setWidth("100%");

		Label l2 = new Label("New Module:");
		l2.setStyleName("mr-5");
		l2.addStyleName("taskLabel");
		final TextBox newModuleField = new TextBox();
		newModuleField.setStyleName("listBoxStyle");

		addModuleBtn = new Button("Submit");
		addModuleBtn.setStyleName("customBtn");

		addModuleBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final String moduleName = newModuleField.getText();
				if (moduleName.trim().length() > 0) {
					Module module = new Module();
					module.setName(moduleName);
					if (moduleDTO != null) {
						Module parentModule = typeConverter.convertToModuleDao(moduleDTO);
						if (parentModule != null) {
							module.setParentModule(parentModule);
						}
					} else {
						module.setParentModule(null);
					}
					module.setProduct(typeConverter.convertToProductDao(productDTO));
					moduleService.saveModule(module, new AsyncCallback<ModuleDTO>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(final ModuleDTO module) {
							Label label = new Label(moduleName);
							label.getElement().getStyle().setProperty("cursor", "pointer");
							final TreeItem item = new TreeItem(label);
							label.addClickHandler(moduleLabelClickHandler(item, module, module.getProductDTO()));
							treeItem.addItem(item);
							dialogBox.hide();
						}

					});
				}
				dialogBox.hide();
			}
		});

		final Grid grid = new Grid(5, 2);
		grid.setCellPadding(10);
		grid.getElement().getStyle().setProperty("margin", "100px 0 0 0");
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		grid.setWidget(0, 0, l2);

		grid.setWidget(0, 1, newModuleField);
		grid.setWidget(2, 1, addModuleBtn);

		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");

		grid.getCellFormatter().getElement(3, 1).getStyle().setProperty("textAlign", "left");

		vpanel.add(grid);

		dialogBox.add(vpanel);
		dialogBox.center();
	}

	private ClickHandler moduleLabelClickHandler(final TreeItem item, final ModuleDTO module,
			final ProductDTO product) {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clearSelection(productRootTree);
				clearSelection(userRootTree);
				selectedModuleItem = item;
				selectedModule = module;
				selectedModuleProduct = product;
			}
		};
	}

	// Products Section
	private Tree buildProductsTree() {
		final Tree treeWidget = new Tree(customTreeResources);

		treeWidget.setStyleName("parentTree");

		Label label = new Label("All Products");
		label.getElement().getStyle().setProperty("cursor", "pointer");
		productRootTree = new TreeItem(label);

		label.addClickHandler(productLabelClickHandler(productRootTree, null));

		productRootTree.setStyleName("treeHeading");
		treeWidget.addItem(productRootTree);

		productService.getProducts(new AsyncCallback<List<ProductDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ProductDTO> products) {
				for (final ProductDTO product : products) {
					if (product.getParentProductDTO() == null) {
						Label label = new Label(product.getName());
						label.getElement().getStyle().setProperty("cursor", "pointer");
						final TreeItem rootItem = new TreeItem(label);
						rootItem.getElement().getStyle().setProperty("display", "");
						rootItem.setStyleName("treeHeading");

						label.addClickHandler(productLabelClickHandler(rootItem, product));

						buildSubProductsTree(product, rootItem);
						productRootTree.addItem(rootItem);
					}
				}
				productRootTree.setState(true);
			}

		});

		return treeWidget;
	}

	private void buildSubProductsTree(final ProductDTO product, final TreeItem parentItem) {

		productService.getChildProductsByParentId(product.getProductId(), new AsyncCallback<List<ProductDTO>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(List<ProductDTO> products) {
				// TODO Auto-generated method stub
				for (final ProductDTO child : products) {
					Label label = new Label(child.getName());
					label.getElement().getStyle().setProperty("cursor", "pointer");
					final TreeItem childItem = new TreeItem(label);

					label.addClickHandler(productLabelClickHandler(childItem, child));

					parentItem.addItem(childItem);
					buildSubProductsTree(child, childItem);
					childItem.setState(true);
				}
				parentItem.setState(true);
			}

		});
	}

	private void addProductHandler(final TreeItem treeItem, final ProductDTO productDTO) {
		final DialogBox dialogBox = new DialogBox();
		// dialogBox.setText("Hey");
		dialogBox.setAnimationEnabled(true);

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setStyleName("form-container");
		vpanel.setWidth("100%");

		Label l1 = new Label("Select Parent:");
		l1.setStyleName("mr-5");
		l1.addStyleName("taskLabel");

		Label l2 = new Label("New Product:");
		l2.setStyleName("mr-5");
		l2.addStyleName("taskLabel");
		final TextBox newProductField = new TextBox();
		newProductField.setStyleName("listBoxStyle");

		addProductBtn = new Button("Submit");
		addProductBtn.setStyleName("customBtn");

		addProductBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final String productName = newProductField.getText();
				if (productName.trim().length() > 0) {
					Product product = new Product();
					product.setName(productName);
					if (productDTO != null) {
						product.setParentProduct(typeConverter.convertToProductDao(productDTO));
					}
					productService.saveProduct(product, new AsyncCallback<ProductDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(final ProductDTO product) {
							Label label = new Label(productName);
							label.getElement().getStyle().setProperty("cursor", "pointer");
							final TreeItem item = new TreeItem(label);
							label.addClickHandler(productLabelClickHandler(item, product));
							treeItem.addItem(item);
							treeItem.setSelected(false);
							dialogBox.hide();
						}

					});
				}
				dialogBox.hide();
			}
		});

		final Grid grid = new Grid(5, 2);
		grid.setCellPadding(10);
		grid.getElement().getStyle().setProperty("margin", "100px 0 0 0");
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		grid.setWidget(0, 0, l2);

		grid.setWidget(0, 1, newProductField);
		grid.setWidget(2, 1, addProductBtn);

		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");

		grid.getCellFormatter().getElement(3, 1).getStyle().setProperty("textAlign", "left");

		vpanel.add(grid);

		dialogBox.add(vpanel);
		dialogBox.center();
	}

	private ClickHandler productLabelClickHandler(final TreeItem item, final ProductDTO product) {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				clearSelection(moduleRootTree);
				clearSelection(userRootTree);
				selectedProductItem = item;
				selectedProduct = product;
			}
		};
	}

	private void clearSelection(TreeItem rootItem) {
		if (rootItem != null) {
			rootItem.setSelected(false);
			for (int i = 0; i < rootItem.getChildCount(); i++) {
				clearSelection(rootItem.getChild(i));
			}
		}
		selectedModuleItem = null;
		selectedModule = null;
		selectedModuleProduct = null;

		selectedProductItem = null;
		selectedProduct = null;
	}

	// user Section
	private Tree buildUsersTree() {
		Tree tree = new Tree(customTreeResources);

		tree.setStyleName("parentTree");

		Label label = new Label("Users & Teams");
		label.getElement().getStyle().setProperty("cursor", "pointer");
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				userLabelClickHandler();
				selectedUserName = null;
				filterTasks();
			}

		});
		userRootTree = new TreeItem(label);
		userRootTree.setStyleName("treeHeading");

		Label label1 = new Label("All Users");
		label1.getElement().getStyle().setProperty("cursor", "pointer");
		label1.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				userLabelClickHandler();
				selectedUserName = null;
				filterTasks();
			}

		});

		Button createUserBtn = new Button("+");
		createUserBtn.setStyleName("smallBtn");

		final TreeItem item1 = new TreeItem(createToolbarPanel(label1, createUserBtn));
		item1.getElement().getStyle().setProperty("width", "150px");
		createUserBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addUserHandler(item1);
			}
		});

		Label label2 = new Label("All Teams");
		label2.getElement().getStyle().setProperty("cursor", "pointer");
		label2.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				userLabelClickHandler();
				selectedUserName = null;
				filterTasks();
			}
		});

		Button createTeamBtn = new Button("+");
		createTeamBtn.setStyleName("smallBtn");

		final TreeItem item2 = new TreeItem(createToolbarPanel(label2, createTeamBtn));
		createTeamBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addTeamHandler(item2);
			}
		});

		userService.getUsers(new AsyncCallback<List<UserDTO>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(List<UserDTO> users) {
				// TODO Auto-generated method stub
				for (final UserDTO user : users) {
					Label label = new Label(user.getName());
					label.getElement().getStyle().setProperty("cursor", "pointer");
					label.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							userLabelClickHandler();
							selectedUserName = user.getName();
							filterTasks();
						}

					});
					;
					TreeItem sub = new TreeItem(label);
					item1.addItem(sub);
				}
				item1.setState(true);
			}

		});

		teamService.getTeams(new AsyncCallback<List<Team>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(List<Team> teams) {
				for (final Team team : teams) {
					Label label = new Label(team.getName());
					label.getElement().getStyle().setProperty("cursor", "pointer");
					label.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							userLabelClickHandler();
							selectedUserName = null;
							isTeamSelected = true;
							selectedTeam = team;
							filterTasks();
						}
					});
					Button btn = new Button("+");
					btn.setStyleName("smallBtn");
					final TreeItem sub = new TreeItem(createToolbarPanel(label, btn));
					btn.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							addUserToTeamHandler(sub, team);
						}
					});
					userService.getUsersFromTeam(team, new AsyncCallback<List<UserDTO>>() {

						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(List<UserDTO> usersDTO) {
							for (final UserDTO user : usersDTO) {
								Label label = new Label(user.getName());
								label.getElement().getStyle().setProperty("cursor", "pointer");
								label.addClickHandler(new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {
										userLabelClickHandler();
										selectedUserName = user.getName();
										filterTasks();
									}

								});
								TreeItem treeItem = new TreeItem(label);
								sub.addItem(treeItem);
								treeItem.setState(true);
							}
							item2.addItem(sub);
							sub.setState(true);
							item2.setState(true);
						}

					});
				}
			}
		});

		userRootTree.addItem(item1);
		userRootTree.addItem(item2);
		userRootTree.setState(true);

		tree.addItem(userRootTree);
		return tree;
	}

	private void addUserHandler(final TreeItem treeItem) {
		final DialogBox dialogBox = new DialogBox();
		// dialogBox.setText("Hey");
		dialogBox.setAnimationEnabled(true);

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setStyleName("form-container");
		vpanel.setWidth("100%");

		Label label1 = new Label("Name:");
		label1.setStyleName("mr-5");
		label1.addStyleName("taskLabel");
		final TextBox nameField = new TextBox();
		nameField.setStyleName("listBoxStyle");

		Label label2 = new Label("Email:");
		label2.setStyleName("mr-5");
		label2.addStyleName("taskLabel");
		final TextBox emailField = new TextBox();
		emailField.setStyleName("listBoxStyle");

		Label label3 = new Label("Password:");
		label3.setStyleName("mr-5");
		label3.addStyleName("taskLabel");
		final TextBox passwordField = new TextBox();
		passwordField.setStyleName("listBoxStyle");

		Button addUserBtn = new Button("Submit");
		addUserBtn.setStyleName("customBtn");

		addUserBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final String username = nameField.getText();
				final String password = passwordField.getText();
				final String email = emailField.getText();
				if (username.trim().length() == 0 || password.trim().length() == 0 || email.trim().length() == 0) {

				} else {
					final User user = new User();
					user.setEmail(email);
					user.setName(username);
					user.setPassword(password);
					userService.saveUser(user, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onSuccess(Void result) {
							Label label = new Label(username);
							label.getElement().getStyle().setProperty("cursor", "pointer");
							TreeItem item = new TreeItem(label);
							label.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									// TODO Auto-generated method stub
									userLabelClickHandler();
									selectedUserName = user.getName();
									filterTasks();
								}

							});
							treeItem.addItem(item);
							treeItem.setSelected(false);
							dialogBox.hide();
						}

					});
				}
				dialogBox.hide();
			}
		});

		final Grid grid = new Grid(5, 2);
		grid.setCellPadding(10);
		grid.getElement().getStyle().setProperty("margin", "100px 0 0 0");
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		grid.setWidget(0, 0, label1);
		grid.setWidget(1, 0, label2);
		grid.setWidget(2, 0, label3);

		grid.setWidget(0, 1, nameField);
		grid.setWidget(1, 1, emailField);
		grid.setWidget(2, 1, passwordField);
		grid.setWidget(3, 1, addUserBtn);

		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");

		grid.getCellFormatter().getElement(0, 1).getStyle().setProperty("textAlign", "left");
		grid.getCellFormatter().getElement(1, 1).getStyle().setProperty("textAlign", "left");
		grid.getCellFormatter().getElement(2, 1).getStyle().setProperty("textAlign", "left");
		grid.getCellFormatter().getElement(3, 1).getStyle().setProperty("textAlign", "left");

		vpanel.add(grid);

		dialogBox.add(vpanel);
		dialogBox.center();
	}

	private void addTeamHandler(final TreeItem treeItem) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setAnimationEnabled(true);

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setStyleName("form-container");
		vpanel.setWidth("100%");

		Label label1 = new Label("Name:");
		label1.setStyleName("mr-5");
		label1.addStyleName("taskLabel");
		final TextBox nameField = new TextBox();
		nameField.setStyleName("listBoxStyle");

		Button addTeamBtn = new Button("Submit");
		addTeamBtn.setStyleName("customBtn");

		addTeamBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final String name = nameField.getText();
				if (name.trim().length() != 0) {
					Team team = new Team();
					team.setName(name);
					teamService.saveTeam(team, new AsyncCallback<Team>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(final Team team) {
							Label label = new Label(name);
							label.getElement().getStyle().setProperty("cursor", "pointer");
							label.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									userLabelClickHandler();
									selectedUserName = null;
									isTeamSelected = true;
									selectedTeam = team;
									filterTasks();
								}

							});
							Button btn = new Button("+");
							btn.setStyleName("smallBtn");

							final TreeItem item = new TreeItem(createToolbarPanel(label, btn));
							btn.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									addUserToTeamHandler(item, team);
								}
							});

							treeItem.addItem(item);
							treeItem.setSelected(false);
							dialogBox.hide();
						}

					});
				}
				dialogBox.hide();
			}
		});

		final Grid grid = new Grid(5, 2);
		grid.setCellPadding(10);
		grid.getElement().getStyle().setProperty("margin", "100px 0 0 0");
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		grid.setWidget(0, 0, label1);

		grid.setWidget(0, 1, nameField);
		grid.setWidget(1, 1, addTeamBtn);

		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");

		grid.getCellFormatter().getElement(0, 1).getStyle().setProperty("textAlign", "left");
		grid.getCellFormatter().getElement(1, 1).getStyle().setProperty("textAlign", "left");
		grid.getCellFormatter().getElement(2, 1).getStyle().setProperty("textAlign", "left");
		grid.getCellFormatter().getElement(3, 1).getStyle().setProperty("textAlign", "left");

		vpanel.add(grid);

		dialogBox.add(vpanel);
		dialogBox.center();
	}

	private void addUserToTeamHandler(final TreeItem treeItem, final Team team) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setAnimationEnabled(true);

		final VerticalPanel vpanel = new VerticalPanel();
		vpanel.setStyleName("form-container");
		vpanel.setWidth("100%");

		Label label1 = new Label("Select User:");
		label1.setStyleName("mr-5");
		label1.addStyleName("taskLabel");
		final ListBox userField = new ListBox();
		userField.setStyleName("listBoxStyle");
		userField.addItem("<Select>");

		final Button updateTeam = new Button("Submit");
		updateTeam.setStyleName("customBtn");

		updateTeam.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final String username = userField.getSelectedValue();
				userService.getUserByName(username, new AsyncCallback<UserDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(UserDTO userDTO) {
						final User user = typeConverter.convertToUserDao(userDTO);
						if (!username.equals("<Select>")) {
							userService.addUserToTeam(user, team, new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {

								}

								@Override
								public void onSuccess(Void result) {
									Label label = new Label(user.getName());
									label.getElement().getStyle().setProperty("cursor", "pointer");
									TreeItem item = new TreeItem(label);
									label.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											userLabelClickHandler();
											selectedUserName = user.getName();
											filterTasks();
										}
									});
									treeItem.addItem(item);
									treeItem.setSelected(false);
									item.setState(true);
									dialogBox.hide();
								}

							});
						}
					}

				});
				dialogBox.hide();
			}
		});

		final Grid grid = new Grid(5, 2);
		grid.setCellPadding(10);
		grid.getElement().getStyle().setProperty("margin", "100px 0 0 0");
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		grid.setWidget(0, 0, label1);

		userService.getUsers(new AsyncCallback<List<UserDTO>>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(final List<UserDTO> users) {
				userService.getUsersFromTeam(team, new AsyncCallback<List<UserDTO>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<UserDTO> usersInTeam) {
						List<String> names = new ArrayList<>();
						for (UserDTO userDTO : usersInTeam) {
							names.add(userDTO.getName());
						}
						for (UserDTO user : users) {
							if (!names.contains(user.getName())) {
								userField.addItem(user.getName());
							}
						}
						grid.setWidget(0, 1, userField);
						grid.setWidget(1, 1, updateTeam);

						grid.getCellFormatter().setStyleName(0, 0, "text-right");
						grid.getCellFormatter().setStyleName(1, 0, "text-right");
						grid.getCellFormatter().setStyleName(2, 0, "text-right");

						grid.getCellFormatter().getElement(0, 1).getStyle().setProperty("textAlign", "left");
						grid.getCellFormatter().getElement(1, 1).getStyle().setProperty("textAlign", "left");
						grid.getCellFormatter().getElement(2, 1).getStyle().setProperty("textAlign", "left");
						grid.getCellFormatter().getElement(3, 1).getStyle().setProperty("textAlign", "left");

						vpanel.add(grid);

						dialogBox.add(vpanel);
						dialogBox.center();
					}

				});
			}
		});
	}

	private void userLabelClickHandler() {
		clearSelection(moduleRootTree);
		clearSelection(productRootTree);
	}

	private VerticalPanel buildLeftSidebar() {
		VerticalPanel vpanel = new VerticalPanel();

		ScrollPanel spanel = new ScrollPanel();
		spanel.setSize("100%", "100%");

		vpanel.setStyleName("leftSidebar");

		Button addProductBtn = new Button("Add Product");
		addProductBtn.setStyleName("customBtn");
		addProductBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedProductItem != null) {
					addProductHandler(selectedProductItem, selectedProduct);
					clearSelection(productRootTree);
				} else {
					Window.alert("Please select a product");
				}
			}
		});
		vpanel.add(addProductBtn);

		vpanel.add(buildProductsTree());

		vpanel.add(new HTML("<div class='hr' />"));

		Button addModuleBtn = new Button("Add Module");
		addModuleBtn.setStyleName("customBtn");
		addModuleBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (selectedModuleItem != null) {
					addModuleHandler(selectedModuleItem, selectedModule, selectedModuleProduct);
					clearSelection(moduleRootTree);
				} else {
					Window.alert("Please select a module");
				}
			}

		});
		vpanel.add(addModuleBtn);

		vpanel.add(buildModulesTree());

		vpanel.add(new HTML("<div class='hr' />"));

		vpanel.add(buildUsersTree());

		spanel.add(vpanel);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHeight("100%");
		mainPanel.add(spanel);
		return mainPanel;
	}

	// ################ Task table section #####################
	private VerticalPanel buildTaskDashboard() {
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");

		vpanel.add(buildHeaderPanel());
		vpanel.add(buildTaskTable());

		return vpanel;
	}

	private VerticalPanel buildTaskTable() {
		VerticalPanel vpanel = new VerticalPanel();
		HorizontalPanel hpanel = new HorizontalPanel();

		vpanel.setWidth("100%");
		hpanel.setWidth(("100%"));
		hpanel.setStyleName("taskHeading");

		ScrollPanel spanel = new ScrollPanel();
		spanel.setSize("100vw-800px", "100vh");
		spanel.getElement().getStyle().setProperty("overflow", "scroll");

		flexTable = new DoubleClickTable();
		flexTable.getElement().getStyle().setProperty("borderLeft", "1px solid black");
		flexTable.getElement().getStyle().setProperty("borderCollapse", "collapse");
		flexTable.setWidth("100%");
		RowFormatter flexTableRow = flexTable.getRowFormatter();
		flexTableRow.setStyleName(0, "taskHeading");
		flexTableRow.getElement(0).getStyle().setProperty("cursor", "pointer");
		flexTableRow.getElement(0).getStyle().setProperty("textAlign", "left");
		flexTableRow.getElement(0).getStyle().setProperty("padding", "10px");

		Label label = new Label("ID");
		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isIdASCOrder = !isIdASCOrder;
				if (isIdASCOrder) {
					Collections.sort(currentStateTasks, new Comparator<TaskDTO>() {
						@Override
						public int compare(TaskDTO task1, TaskDTO task2) {
							return Long.compare(task1.getTaskId(), task2.getTaskId());
						}
					});
				} else {
					Collections.sort(currentStateTasks, new Comparator<TaskDTO>() {
						@Override
						public int compare(TaskDTO task1, TaskDTO task2) {
							return Long.compare(task2.getTaskId(), task1.getTaskId());
						}
					});
				}
				loadTableRows();
			}
		});

		flexTable.setWidget(0, 0, label);
		flexTable.setWidget(0, 1, createLabelFilterPanel("Type"));
		flexTable.setText(0, 2, "Title");
		flexTable.setWidget(0, 3, createLabelFilterPanel("Work flow step"));
		flexTable.setWidget(0, 4, createLabelFilterPanel("Priority"));
		flexTable.setWidget(0, 5, createLabelFilterPanel("Assigned to"));
		flexTable.setWidget(0, 6, createLabelFilterPanel("Project"));
		flexTable.setWidget(0, 7, createLabelFilterPanel("Module"));

		filterTasks();

		spanel.add(flexTable);
		vpanel.add(spanel);
		return vpanel;
	}

	private void loadTableRows() {
		clearFlexTableRows();
		editableRow = -1;
		selectedRows.clear();
		for (final TaskDTO task : currentStateTasks) {
			flexTable.getRowFormatter().setStyleName(rowIndex, "taskCell");
			int col = 0;

			final Label type = new Label(task.getType() != null ? task.getType().getValue() : "NULL");
			final Label title = new Label(task.getTitle() != null ? task.getTitle() : "NULL");
			final Label step = new Label(task.getStatus() != null ? task.getStatus().getValue() : "NULL");
			final Label priority = new Label(task.getPriority() != null ? task.getPriority().getValue() : "NULL");
			final Label assignedTo = new Label(task.getUser() != null ? task.getUser().getName() : "NULL");
			final Label product = new Label(task.getProduct() != null ? task.getProduct().getName() : "NULL");
			final Label module = new Label(task.getModule() != null ? task.getModule().getName() : "NULL");

			flexTable.setText(rowIndex, col++, String.valueOf(task.getTaskId()));
			flexTable.setWidget(rowIndex, col++, type);
			flexTable.setWidget(rowIndex, col++, title);
			flexTable.setWidget(rowIndex, col++, step);
			flexTable.setWidget(rowIndex, col++, priority);
			flexTable.setWidget(rowIndex, col++, assignedTo);
			flexTable.setWidget(rowIndex, col++, product);
			flexTable.setWidget(rowIndex, col++, module);

			flexTable.getRowFormatter().getElement(rowIndex).getStyle().setProperty("cursor", "pointer");
			flexTable.getRowFormatter().addStyleName(rowIndex, "row-border-top");

			rowIndex++;
		}
		flexTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				Cell cell = flexTable.getCellForEvent(event);
				if (cell != null) {
					final int row = cell.getRowIndex();
					if (row != 0) {
						if (selectedRows.contains(row)) {
							flexTable.getRowFormatter().removeStyleName(row, "selected-row");
							selectedRows.remove(row);
						} else if (editableRow != row) {
							if (editableRow != -1) {
								revertFieldsToNormalState(flexTable);
							}
							flexTable.getRowFormatter().addStyleName(row, "selected-row");
							selectedRows.add(row);
						}
					}
				}
			}

		});

		RootLayoutPanel.get().addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Element eventTarget = Element.as(event.getNativeEvent().getEventTarget());
				if (!flexTable.getElement().isOrHasChild(eventTarget)) {
					revertFieldsToNormalState(flexTable);
				}
			}

		}, ClickEvent.getType());
	}

	private void filterTasks() {
		if (!isTeamSelected) {
			// Clear all tasks
			if (selectedUserName != null || !allTasksRendered) {
				clearFlexTableRows();
			}
			if (selectedUserName != null) {
				taskService.getTasksByUsername(selectedUserName, new AsyncCallback<List<TaskDTO>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(List<TaskDTO> tasks) {
						currentStateTasks = tasks;
						loadTableRows();
						allTasksRendered = false;
						selectedUserName = null;
					}

				});
			} else {
				if (!allTasksRendered) {
					taskService.getTasks(new AsyncCallback<List<TaskDTO>>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(List<TaskDTO> tasks) {
							currentStateTasks = tasks;
							loadTableRows();
							allTasksRendered = true;
						}

					});
				}
			}
		} else {
			// Clear all tasks
			clearFlexTableRows();
			userService.getUsersFromTeam(selectedTeam, new AsyncCallback<List<UserDTO>>() {

				@Override
				public void onFailure(Throwable caught) {

				}

				@Override
				public void onSuccess(List<UserDTO> users) {
					for (UserDTO user : users) {
						taskService.getTasksByUsername(user.getName(), new AsyncCallback<List<TaskDTO>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(List<TaskDTO> tasks) {
								currentStateTasks = tasks;
								loadTableRows();
								allTasksRendered = false;
								isTeamSelected = false;
							}

						});
					}
				}

			});
		}
	}

	private void clearFlexTableRows() {
		rowIndex = 1;
		while (flexTable.getRowCount() != 1) {
			flexTable.removeRow(1);
		}
	}

	// Method to convert cells in a row to editable fields
	private void convertRowToEditable(final int row, final FlexTable flexTable, List<UserDTO> users,
			List<ProductDTO> products) {

		TextBox titleTextBox = new TextBox();

		titleTextBox.setStyleName("listBoxStyle");
		titleTextBox.getElement().getStyle().setProperty("padding", "2px");
		titleTextBox.getElement().getStyle().setProperty("width", "100%");

		titleTextBox.setText(((Label) flexTable.getWidget(row, 2)).getText());
		flexTable.setWidget(row, 2, titleTextBox);

		ListBox typeListBox = new ListBox();

		typeListBox.setStyleName("listBoxStyle");
		typeListBox.getElement().getStyle().setProperty("padding", "2px");
		typeListBox.getElement().getStyle().setProperty("width", "100px");

		Label type = (Label) flexTable.getWidget(row, 1);
		typeListBox.addItem(type.getText());
		List<String> typeList = new ArrayList<>();
		typeList.add("TASK");
		typeList.add("BUG");
		typeList.add("FEATURE");

		for (String typeItem : typeList) {
			if (!typeItem.equals(type.getText())) {
				typeListBox.addItem(typeItem);
			}
		}
		flexTable.setWidget(row, 1, typeListBox);

		ListBox stepListBox = new ListBox();

		stepListBox.setStyleName("listBoxStyle");
		stepListBox.getElement().getStyle().setProperty("padding", "2px");
		stepListBox.getElement().getStyle().setProperty("width", "100px");

		Label step = (Label) flexTable.getWidget(row, 3);
		stepListBox.addItem(step.getText());

		List<String> stepList = new ArrayList<>();
		stepList.add("NEW");
		stepList.add("APPROVED");
		stepList.add("IN_PROGRESS");
		stepList.add("DEV_COMPLETE");
		stepList.add("READY_FOR_TESTING");

		for (String stepItem : stepList) {
			if (!stepItem.equals(step.getText())) {
				stepListBox.addItem(stepItem);
			}
		}
		flexTable.setWidget(row, 3, stepListBox);

		ListBox priorityListBox = new ListBox();
		priorityListBox.setStyleName("listBoxStyle");
		priorityListBox.getElement().getStyle().setProperty("padding", "2px");
		priorityListBox.getElement().getStyle().setProperty("width", "70px");

		Label priority = (Label) flexTable.getWidget(row, 4);
		priorityListBox.addItem(priority.getText());

		List<String> priorityList = new ArrayList<>();
		priorityList.add("HIGH");
		priorityList.add("MEDIUM");
		priorityList.add("LOW");

		for (String priorItem : priorityList) {
			if (!priorItem.equals(priority.getText())) {
				priorityListBox.addItem(priorItem);
			}
		}
		flexTable.setWidget(row, 4, priorityListBox);

		ListBox assignedToListBox = new ListBox();
		assignedToListBox.setStyleName("listBoxStyle");
		assignedToListBox.getElement().getStyle().setProperty("padding", "2px");
		assignedToListBox.getElement().getStyle().setProperty("width", "90px");

		Label assignedTo = (Label) flexTable.getWidget(row, 5);
		assignedToListBox.addItem(assignedTo.getText());
		for (UserDTO user : users) {
			if (!user.getName().equals(assignedTo.getText())) {
				assignedToListBox.addItem(user.getName());
			}
		}
		flexTable.setWidget(row, 5, assignedToListBox);

		final ListBox productListBox = new ListBox();
		productListBox.setStyleName("listBoxStyle");
		productListBox.getElement().getStyle().setProperty("padding", "2px");
		productListBox.getElement().getStyle().setProperty("width", "50px");

		Label product = (Label) flexTable.getWidget(row, 6);
		productListBox.addItem(product.getText());

		for (ProductDTO productDTO : products) {
			if (!productDTO.getName().equals(product.getText())) {
				productListBox.addItem(productDTO.getName());
			}
		}
		flexTable.setWidget(row, 6, productListBox);

		// Module section
		final ListBox moduleListBox = new ListBox();
		moduleListBox.setStyleName("listBoxStyle");
		moduleListBox.getElement().getStyle().setProperty("padding", "2px");
		moduleListBox.getElement().getStyle().setProperty("width", "100px");

		final Label module = (Label) flexTable.getWidget(row, 7);
		moduleListBox.addItem(module.getText());

		moduleService.getModulesByProductName(product.getText(), new AsyncCallback<List<ModuleDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ModuleDTO> modules) {
				// TODO Auto-generated method stub
				for (ModuleDTO moduleDTO : modules) {
					if (!moduleDTO.getName().equals(module.getText())) {
						moduleListBox.addItem(moduleDTO.getName());
					}
				}
				flexTable.setWidget(row, 7, moduleListBox);
			}

		});

		titleTextBox.setFocus(true);

		// Event Handlers
		productListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				moduleService.getModulesByProductName(productListBox.getSelectedValue(),
						new AsyncCallback<List<ModuleDTO>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(List<ModuleDTO> modules) {
								// TODO Auto-generated method stub
								moduleListBox.clear();
								moduleListBox.addItem("NULL");
								for (ModuleDTO moduleDTO : modules) {
									if (!moduleDTO.getName().equals(module.getText())) {
										moduleListBox.addItem(moduleDTO.getName());
									}
								}
							}

						});
			}

		});
	}

	// Method to revert fields to normal state with updated text
	private void revertFieldsToNormalState(final FlexTable flexTable) {
		int row = editableRow;
		if (row != 1) {
			try {
				final String title = ((TextBox) flexTable.getWidget(row, 2)).getText();

				ListBox typeListBox = (ListBox) flexTable.getWidget(row, 1);
				final String type = typeListBox.getSelectedValue();

				ListBox stepListBox = (ListBox) flexTable.getWidget(row, 3);
				final String step = stepListBox.getSelectedValue();

				ListBox priorityListBox = (ListBox) flexTable.getWidget(row, 4);
				final String priority = priorityListBox.getSelectedValue();

				ListBox assignedToListBox = (ListBox) flexTable.getWidget(row, 5);
				final String assign = assignedToListBox.getSelectedValue();

				ListBox productListBox = (ListBox) flexTable.getWidget(row, 6);
				final String product = productListBox.getSelectedValue();

				ListBox moduleListBox = (ListBox) flexTable.getWidget(row, 7);
				final String module = moduleListBox.getSelectedValue();

				final long id = Long.parseLong(flexTable.getText(row, 0));
				final int index = row;

				taskService.getTaskById(id, new AsyncCallback<TaskDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Got error at id: " + id);
					}

					@Override
					public void onSuccess(TaskDTO taskDTO) {
						List<String> values = new ArrayList<>();
						values.add(type);
						values.add(step);
						values.add(priority);

						final Task task = new Task();
						task.setTaskId(taskDTO.getTaskId());
						task.setDescription(taskDTO.getDescription());
						task.setTitle(title);
						task.setPercentComplete(taskDTO.getPercentComplete());
						task.setInitialEstimate(taskDTO.getInitialEstimate());
						task.setRemainingEstimate(taskDTO.getRemainingEstimate());
						task.setDueDate(taskDTO.getDueDate());

						editableRow = -1;

						updateTask(task, values, assign, product, module, index);
					}

				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void updateTask(final Task task, final List<String> values, final String assign, final String product,
			final String module, final int row) {

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
						if (userDTO.getTeams() != null && userDTO.getTeams().size() > 0) {
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
										// TODO
										// Auto-generated
										// method stub
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
												flexTable.setWidget(row, 1, new Label(task.getType().getValue()));
												flexTable.setWidget(row, 2, new Label(task.getTitle()));
												flexTable.setWidget(row, 3, new Label(task.getStatus().getValue()));
												flexTable.setWidget(row, 4, new Label(task.getPriority().getValue()));
												flexTable.setWidget(row, 5, new Label(task.getUser().getName()));
												flexTable.setWidget(row, 6, new Label(task.getProduct().getName()));
												flexTable.setWidget(row, 7, new Label(module));
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

	private DockLayoutPanel buildHeaderPanel() {

		DockLayoutPanel headerPanel = new DockLayoutPanel(Unit.PX);
		HorizontalPanel hpanel = new HorizontalPanel();

		headerPanel.getElement().getStyle().setProperty("backgroundColor", "#EFF6FF");
		headerPanel.setWidth("100%");
		headerPanel.setHeight("50px");

		Label l1 = new Label("List View");
		l1.setStyleName("taskLabel");

		Image icon = new Image("public/caret-down.png");
		icon.setStyleName("icon");
		icon.setPixelSize(15, 15);

		hpanel.getElement().getStyle().setPadding(15, Unit.PX);
		hpanel.add(l1);
		hpanel.add(icon);

		Button resetBtn = new Button("Reset");
		addBtn = new Button("Add");
		editBtn = new Button("Edit");
		deleteBtn = new Button("Delete");

		resetBtn.setStyleName("customBtn");
		addBtn.setStyleName("customBtn");
		editBtn.setStyleName("customBtn");
		deleteBtn.setStyleName("customBtn");

		resetBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isIdASCOrder = true;
				allTasksRendered = false;
				filterTasks();
			}

		});

		editBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedRows.size() == 1) {
					userService.getUsers(new AsyncCallback<List<UserDTO>>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(final List<UserDTO> users) {
							// TODO Auto-generated method stub
							productService.getTopMostParentProducts(new AsyncCallback<List<ProductDTO>>() {

								@Override
								public void onFailure(Throwable caught) {

								}

								@Override
								public void onSuccess(List<ProductDTO> products) {
									int row = (int) selectedRows.toArray()[0];
									flexTable.getRowFormatter().removeStyleName(row, "selected-row");
									editableRow = row;
									convertRowToEditable(row, flexTable, users, products);
									selectedRows.clear();
								}
							});
						}

					});
				} else {
					Window.alert("Please select exactly one task");
				}

			}

		});

		headerPanel.addWest(hpanel, 300);
		headerPanel.addEast(createButtonsPanel(resetBtn, addBtn, editBtn, deleteBtn), 400);
		headerPanel.add(createSearchPanel());

		return headerPanel;
	}

	// Custom hpanels
	private HorizontalPanel createSearchPanel() {
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.getElement().getStyle().setPadding(10, Unit.PX);

		searchField = new TextBox();
		searchField.getElement().setAttribute("placeHolder", "Search");
		searchField.addStyleName("listBoxStyle");

		itemSearch = new ListBox();
		itemSearch.addStyleName("listBoxStyle");

		itemSearch.addItem("ID");
		itemSearch.addItem("Type");
		itemSearch.addItem("Title");
		itemSearch.addItem("Work flow step");
		itemSearch.addItem("Priority");
		itemSearch.addItem("Assigned to");
		itemSearch.addItem("Project");
		itemSearch.addItem("Module");

		attrName = itemSearch.getSelectedValue();

		itemSearch.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				attrName = itemSearch.getSelectedValue();
				if (attrName != null) {
					if (attrName.contains("step")) {
						attrName = "status";
					} else if (attrName.contains("Assigned")) {
						attrName = "user";
					} else if (attrName.equals("Project")) {
						attrName = "product";
					}
				}
			}
		});

		searchField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				final String searchValue = searchField.getText();
				if (searchValue.length() > 0) {
					taskService.getTasksBySearchString(attrName, searchValue, new AsyncCallback<List<TaskDTO>>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(List<TaskDTO> tasks) {
							clearFlexTableRows();
							if (tasks != null) {
								currentStateTasks = tasks;
								loadTableRows();
							}
						}

					});
				} else {
					isIdASCOrder = true;
					allTasksRendered = false;
					filterTasks();
				}
			}

		});

		itemSearch.getElement().getStyle().setMarginLeft(5, Unit.PX);
		itemSearch.getElement().getStyle().setProperty("cursor", "pointer");

		hpanel.add(searchField);
		hpanel.add(itemSearch);

		return hpanel;
	}

	private HorizontalPanel createButtonsPanel(Button... buttons) {
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.getElement().getStyle().setProperty("padding", "10px");
		buttonsPanel.setWidth("100%");

		for (Button button : buttons) {
			buttonsPanel.add(button);
		}

		return buttonsPanel;
	}

	private HorizontalPanel createToolbarPanel(Label label, Button button) {
		HorizontalPanel hpanel = new HorizontalPanel();

		HTML spacer = new HTML("&nbsp;");

		hpanel.add(label);
		hpanel.add(spacer);
		hpanel.add(button);

		return hpanel;
	}

	private HorizontalPanel createLabelFilterPanel(final String text) {
		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.addStyleName("set-pad-zero");

		final HTML filterIcon = new HTML("<i class='bi bi-funnel-fill'></i>");
		filterIcon.getElement().getStyle().setProperty("marginLeft", "10px");

		filterIcon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showPopup(text, filterIcon);
			}

		});

		Label label = new Label(text);
//		lookupSortClickHandler(label);

		hpanel.add(label);
		hpanel.add(filterIcon);

		return hpanel;
	}

	private void showPopup(String name, HTML filterIcon) {
		popupPanel = new PopupPanel(true);
		popupPanel.setStyleName("popupPanelStyle");

		final VerticalPanel menuPanel = new VerticalPanel();
		name = name.toLowerCase();
		if (name.equals("type")) {
			Label l1 = new Label("TASK");
			Label l2 = new Label("BUG");
			Label l3 = new Label("FEATURE");

			l1.addStyleName("subMenuItem");
			l2.addStyleName("subMenuItem");
			l3.addStyleName("subMenuItem");

			lookupFilterClickHandler(name, l1);
			lookupFilterClickHandler(name, l2);
			lookupFilterClickHandler(name, l3);

			menuPanel.add(l1);
			menuPanel.add(l2);
			menuPanel.add(l3);
		} else if (name.equals("work flow step")) {
			Label l1 = new Label("NEW");
			Label l2 = new Label("APPROVED");
			Label l3 = new Label("IN_PROGRESS");
			Label l4 = new Label("DEV_COMPLETE");
			Label l5 = new Label("READY_FOR_TESTING");

			l1.addStyleName("subMenuItem");
			l2.addStyleName("subMenuItem");
			l3.addStyleName("subMenuItem");
			l4.addStyleName("subMenuItem");
			l5.addStyleName("subMenuItem");

			lookupFilterClickHandler("status", l1);
			lookupFilterClickHandler("status", l2);
			lookupFilterClickHandler("status", l3);
			lookupFilterClickHandler("status", l4);
			lookupFilterClickHandler("status", l5);

			menuPanel.add(l1);
			menuPanel.add(l2);
			menuPanel.add(l3);
			menuPanel.add(l4);
			menuPanel.add(l5);
		} else if (name.equals("priority")) {
			Label l1 = new Label("HIGH");
			Label l2 = new Label("MEDIUM");
			Label l3 = new Label("LOW");

			l1.addStyleName("subMenuItem");
			l2.addStyleName("subMenuItem");
			l3.addStyleName("subMenuItem");

			lookupFilterClickHandler(name, l1);
			lookupFilterClickHandler(name, l2);
			lookupFilterClickHandler(name, l3);

			menuPanel.add(l1);
			menuPanel.add(l2);
			menuPanel.add(l3);
		} else if (name.equals("assigned to")) {
			userService.getUsers(new AsyncCallback<List<UserDTO>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(List<UserDTO> userDTOs) {
					for (UserDTO user : userDTOs) {
						Label label = new Label(user.getName());
						label.addStyleName("subMenuItem");
						userFilterClickHandler(label);
						menuPanel.add(label);
					}
				}

			});
		} else if (name.equals("project")) {
			productService.getTopMostParentProducts(new AsyncCallback<List<ProductDTO>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(List<ProductDTO> products) {
					for (ProductDTO product : products) {
						Label label = new Label(product.getName());
						label.addStyleName("subMenuItem");
						productFilterClickHandler(label);
						menuPanel.add(label);
					}
				}

			});
		} else if (name.equals("module")) {
			moduleService.getModules(new AsyncCallback<List<ModuleDTO>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(List<ModuleDTO> modules) {
					for (ModuleDTO module : modules) {
						Label label = new Label(module.getName());
						label.addStyleName("subMenuItem");
						moduleFilterClickHandler(label);
						menuPanel.add(label);
					}
				}

			});
		}

		int left = filterIcon.getAbsoluteLeft();
		int top = filterIcon.getAbsoluteTop() + filterIcon.getOffsetHeight();
		popupPanel.setPopupPosition(left, top);
		popupPanel.add(menuPanel);
		popupPanel.show();
	}

	// Filter section
	private void lookupFilterClickHandler(final String name, final Label label) {
		label.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String value = label.getText();
				popupPanel.hide();
				
				if(name.equals("type")){
					List<TaskDTO> tasksWithTypeTask = new ArrayList<>();

			        for (TaskDTO task : currentStateTasks) {
			            if (task.getType() != null && value.equals(task.getType().getValue())) {
			                tasksWithTypeTask.add(task);
			            }
			        }
			        
			        currentStateTasks = tasksWithTypeTask;
			        loadTableRows();
				}else if(name.equals("status")){
					List<TaskDTO> tasksWithStatusTask = new ArrayList<>();

			        for (TaskDTO task : currentStateTasks) {
			            if (task.getStatus() != null && value.equals(task.getStatus().getValue())) {
			                tasksWithStatusTask.add(task);
			            }
			        }
			        
			        currentStateTasks = tasksWithStatusTask;
			        loadTableRows();
				}else if(name.equals("priority")){
					List<TaskDTO> tasksWithPriorityTask = new ArrayList<>();

			        for (TaskDTO task : currentStateTasks) {
			            if (task.getPriority() != null && value.equals(task.getStatus().getValue())) {
			                tasksWithPriorityTask.add(task);
			            }
			        }
			        
			        currentStateTasks = tasksWithPriorityTask;
			        loadTableRows();
				}
			}

		});
	}

	private void userFilterClickHandler(final Label label) {
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popupPanel.hide();
				String value = label.getText();
				
				List<TaskDTO> tasksWithFilteredUserName = new ArrayList<>();
				for(TaskDTO task: currentStateTasks){
					if(task.getUser() != null && value.equals(task.getUser().getName())){
						tasksWithFilteredUserName.add(task);
					}
				}
				currentStateTasks = tasksWithFilteredUserName;
				loadTableRows();
			}
		});
	}

	private void productFilterClickHandler(final Label label) {
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String value = label.getText();
				popupPanel.hide();

				List<TaskDTO> tasksWithFilteredProductName = new ArrayList<>();
				for(TaskDTO task: currentStateTasks){
					if(task.getPriority() != null && value.equals(task.getProduct().getName())){
						tasksWithFilteredProductName.add(task);
					}
				}
				currentStateTasks = tasksWithFilteredProductName;
				loadTableRows();
			}

		});
	}

	private void moduleFilterClickHandler(final Label label) {
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String value = label.getText();
				popupPanel.hide();

				List<TaskDTO> tasksWithFilteredModuleName = new ArrayList<>();
				for(TaskDTO task: currentStateTasks){
					if(task.getModule() != null && value.equals(task.getModule().getName())){
						tasksWithFilteredModuleName.add(task);
					}
				}
				currentStateTasks = tasksWithFilteredModuleName;
				loadTableRows();
			}

		});
	}

	// Sort section
//	private void lookupSortClickHandler(final Label label) {
//		final String lookupName = label.getText().toLowerCase().contains("step") ? "status"
//				: label.getText().toLowerCase();
//
//		if (lookupName.equals("type") || lookupName.equals("status") || lookupName.equals("priority")) {
//			label.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					taskService.getTasksByLookupOrder(lookupName, isLookupASCOrder, new AsyncCallback<List<TaskDTO>>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//
//						}
//
//						@Override
//						public void onSuccess(List<TaskDTO> tasks) {
//							clearFlexTableRows();
//							currentStateTasks = tasks;
//							loadTableRows();
//							isLookupASCOrder = !isLookupASCOrder;
//						}
//
//					});
//				}
//
//			});
//		}
//	}
}

class DoubleClickTable extends FlexTable {
	class MyCell extends Cell {
		protected MyCell(int rowIndex, int cellIndex) {
			super(rowIndex, cellIndex);
		}
	}

	public Cell getCellForEvent(MouseEvent<? extends EventHandler> event) {
		Element td = getEventTargetCell(Event.as(event.getNativeEvent()));
		if (td == null) {
			return null;
		}

		int row = TableRowElement.as(td.getParentElement()).getSectionRowIndex();
		int column = TableCellElement.as(td).getCellIndex();
		return new MyCell(row, column);
	}
}
