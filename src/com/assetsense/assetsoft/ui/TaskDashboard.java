package com.assetsense.assetsoft.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaskDashboard {
	private final DtoToDao typeConverter = new DtoToDao();

	private final UserServiceAsync userService = GWT.create(UserService.class);
	private final TeamServiceAsync teamService = GWT.create(TeamService.class);
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	private final ProductServiceAsync productService = GWT.create(ProductService.class);
	private final ModuleServiceAsync moduleService = GWT.create(ModuleService.class);
	private final LookupServiceAsync lookupService = GWT.create(LookupService.class);

	private int rowIndex = 1;

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

	private CheckBox headerCheckBox = new CheckBox();
	private final Map<Long, CheckBox> taskCheckBoxes = new HashMap<>();

	private Map<Long, Boolean> checkedBoxes = new HashMap<>();

	public void setNavBtnName(String name) {
		getNavBtn().setText(name);
	}

	private Button getNavBtn() {
		if (navBtn == null) {
			navBtn = new Button();
		}
		return navBtn;
	}

	public void setAddBtnHandler(ClickHandler handler) {
		addBtn.addClickHandler(handler);
	}

	public void setEditBtnHandler(ClickHandler handler) {
		editBtn.addClickHandler(handler);
	}

	public void setDeleteBtnHandler(ClickHandler handler) {
		deleteBtn.addClickHandler(handler);
	}

	public void setHeaderCheckBoxHandler(ValueChangeHandler<Boolean> handler) {
		headerCheckBox.addValueChangeHandler(handler);
	}

	public Map<Long, CheckBox> getCheckBoxes() {
		return taskCheckBoxes;
	}

	public Map<Long, Boolean> getCheckedBoxes() {
		return checkedBoxes;
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

	private Tree buildModulesTree() {
		final Tree treeWidget = new Tree(customTreeResources);

		treeWidget.setStyleName("parentTree");

		Label label = new Label("All Modules");
		label.getElement().getStyle().setProperty("cursor", "pointer");
		moduleRootTree = new TreeItem(label);
		moduleRootTree.setStyleName("treeHeading");
		
		label.addClickHandler(new ClickHandler(){
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
									Label label = new Label(module.getName());
									label.getElement().getStyle().setProperty("cursor", "pointer");
									final TreeItem moduleItem = new TreeItem(label);
									moduleItem.setStyleName("treeHeading");
									label.addClickHandler(moduleLabelClickHandler(moduleItem, module, module.getProductDTO()));
									
									buildSubModulesTree(module, moduleItem);
									rootItem.addItem(moduleItem);
									moduleItem.setState(true);
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
					}else{
						module.setParentModule(null);
					}
					module.setProduct(typeConverter.convertToProductDao(productDTO));
					Window.alert(module.toString());
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
	
	private ClickHandler moduleLabelClickHandler(final TreeItem item, final ModuleDTO module,final ProductDTO product){
		return new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				clearSelection(productRootTree);
				clearSelection(userRootTree);
				selectedModuleItem = item;
				selectedModule = module;
				selectedModuleProduct =product;
			}
		};
	}

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
//		dialogBox.setText("Hey");
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
	
	private ClickHandler productLabelClickHandler(final TreeItem item, final ProductDTO product){
		return new ClickHandler(){

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
	private Tree buildUsersTree() {
		Tree tree = new Tree(customTreeResources);

		tree.setStyleName("parentTree");

		Label label = new Label("Users & Teams");
		label.getElement().getStyle().setProperty("cursor", "pointer");
		userRootTree = new TreeItem(label);
		userRootTree.setStyleName("treeHeading");

		Label label1 = new Label("All Users");
		label1.getElement().getStyle().setProperty("cursor", "pointer");
		final TreeItem item1 = new TreeItem(label1);

		Label label2 = new Label("All Teams");
		label2.getElement().getStyle().setProperty("cursor", "pointer");
		final TreeItem item2 = new TreeItem(label2);

		userService.getUsers(new AsyncCallback<List<UserDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<UserDTO> users) {
				// TODO Auto-generated method stub
				for (UserDTO user : users) {
					TreeItem sub = new TreeItem();
					sub.setText(user.getName());
					item1.addItem(sub);
				}
				item1.setState(true);
			}

		});

		teamService.getTeams(new AsyncCallback<List<TeamDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<TeamDTO> teams) {
				// TODO Auto-generated method stub
				for (TeamDTO team : teams) {
					GWT.log("IM here");
					TreeItem sub = new TreeItem();
					sub.setText(team.getName());
					item2.addItem(sub);
				}
				item2.setState(true);
			}

		});

		userRootTree.addItem(item1);
		userRootTree.addItem(item2);
		userRootTree.setState(true);

		tree.addItem(userRootTree);
		return tree;
	}

	public VerticalPanel buildLeftSidebar() {
		VerticalPanel vpanel = new VerticalPanel();

		ScrollPanel spanel = new ScrollPanel();
		spanel.setSize("100%", "100%");

		vpanel.setStyleName("leftSidebar");
		
		Button addProductBtn = new Button("Add Product");
		addProductBtn.setStyleName("customBtn");
		addProductBtn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(selectedProductItem != null){
					addProductHandler(selectedProductItem, selectedProduct);
					clearSelection(productRootTree);
				}else{
					Window.alert("Please select a product");
				}
			}
		});
		vpanel.add(addProductBtn);
		
		vpanel.add(buildProductsTree());

		vpanel.add(new HTML("<div class='hr' />"));

		Button addModuleBtn = new Button("Add Module");
		addModuleBtn.setStyleName("customBtn");
		addModuleBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if(selectedModuleItem != null){
					addModuleHandler(selectedModuleItem, selectedModule, selectedModuleProduct);
					clearSelection(moduleRootTree);
				}else{
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

	public VerticalPanel buildTaskDashboard() {
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

		final FlexTable flexTable = new FlexTable();
		flexTable.getElement().getStyle().setProperty("borderLeft", "1px solid black");
		flexTable.getElement().getStyle().setProperty("borderCollapse", "collapse");
		flexTable.setWidth("100%");
		flexTable.getRowFormatter().setStyleName(0, "taskHeading");

		flexTable.setWidget(0, 0, headerCheckBox);
		flexTable.setText(0, 1, "ID");
		flexTable.setText(0, 2, "Type");
		flexTable.setText(0, 3, "Title");
		flexTable.setText(0, 4, "Work flow step");
		flexTable.setText(0, 5, "Priority");
		flexTable.setText(0, 6, "Assigned to");
		flexTable.setText(0, 7, "Project");
		flexTable.setText(0, 8, "Module");
		flexTable.setText(0, 9, "Sub-System");

		headerCheckBox.setValue(false);

		taskService.getTasks(new AsyncCallback<List<TaskDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(List<TaskDTO> tasks) {
				for (final TaskDTO task : tasks) {
					flexTable.getRowFormatter().setStyleName(rowIndex, "taskCell");
					int col = 0;
					CheckBox cb = new CheckBox();

					final Label type = new Label(task.getType() != null ? task.getType().getValue() : "NULL");
					final Label title = new Label(task.getTitle() != null ? task.getTitle() : "NULL");
					final Label step = new Label(task.getStatus() != null ? task.getStatus().getValue() : "NULL");
					final Label priority = new Label(
							task.getPriority() != null ? task.getPriority().getValue() : "NULL");
					final Label assignedTo = new Label(task.getUser() != null ? task.getUser().getName() : "NULL");
					final Label product = new Label(task.getProduct() != null ? task.getProduct().getName() : "NULL");
					final Label module = new Label(task.getModule() != null ? task.getModule().getName() : "NULL");
					final Label subSystem = new Label(
							task.getSubSystem() != null ? task.getSubSystem().getName() : "NULL");

					flexTable.setWidget(rowIndex, col++, cb);
					flexTable.setText(rowIndex, col++, String.valueOf(task.getTaskId()));
					flexTable.setWidget(rowIndex, col++, type);
					flexTable.setWidget(rowIndex, col++, title);
					flexTable.setWidget(rowIndex, col++, step);
					flexTable.setWidget(rowIndex, col++, priority);
					flexTable.setWidget(rowIndex, col++, assignedTo);
					flexTable.setWidget(rowIndex, col++, product);
					flexTable.setWidget(rowIndex, col++, module);
					flexTable.setWidget(rowIndex, col++, subSystem);

					taskCheckBoxes.put(task.getTaskId(), cb);

					cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						private final long id = task.getTaskId();

						@Override
						public void onValueChange(ValueChangeEvent<Boolean> event) {
							// TODO Auto-generated method stub
							if (event.getValue()) {
								checkedBoxes.put(id, true);
							} else {
								checkedBoxes.remove(id);
							}
						}
					});

					flexTable.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Cell cell = flexTable.getCellForEvent(event);
							if (cell != null) {
								final int row = cell.getRowIndex();
								if (row > 0) {
									userService.getUsers(new AsyncCallback<List<UserDTO>>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onSuccess(final List<UserDTO> users) {
											// TODO Auto-generated method stub
											productService
													.getTopMostParentProducts(new AsyncCallback<List<ProductDTO>>() {

														@Override
														public void onFailure(Throwable caught) {
															// TODO
															// Auto-generated
															// method stub

														}

														@Override
														public void onSuccess(List<ProductDTO> products) {
															// TODO
															// Auto-generated
															// method stub
															convertRowToEditable(row, flexTable, users, products);
														}

													});
										}

									});

								}
							} else {
								revertFieldsToNormalState(flexTable);
							}
						}
					});

					rowIndex++;
				}
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

		});

		spanel.add(flexTable);
		vpanel.add(spanel);
		return vpanel;
	}

	// Method to convert cells in a row to editable fields
	private void convertRowToEditable(final int row, final FlexTable flexTable, List<UserDTO> users,
			List<ProductDTO> products) {

		TextBox titleTextBox = new TextBox();

		titleTextBox.setStyleName("listBoxStyle");
		titleTextBox.getElement().getStyle().setProperty("padding", "2px");
		titleTextBox.getElement().getStyle().setProperty("width", "100%");

		titleTextBox.setText(((Label) flexTable.getWidget(row, 3)).getText());
		flexTable.setWidget(row, 3, titleTextBox);

		ListBox typeListBox = new ListBox();

		typeListBox.setStyleName("listBoxStyle");
		typeListBox.getElement().getStyle().setProperty("padding", "2px");
		typeListBox.getElement().getStyle().setProperty("width", "100px");

		Label type = (Label) flexTable.getWidget(row, 2);
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
		flexTable.setWidget(row, 2, typeListBox);

		ListBox stepListBox = new ListBox();

		stepListBox.setStyleName("listBoxStyle");
		stepListBox.getElement().getStyle().setProperty("padding", "2px");
		stepListBox.getElement().getStyle().setProperty("width", "100px");

		Label step = (Label) flexTable.getWidget(row, 4);
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
		flexTable.setWidget(row, 4, stepListBox);

		ListBox priorityListBox = new ListBox();
		priorityListBox.setStyleName("listBoxStyle");
		priorityListBox.getElement().getStyle().setProperty("padding", "2px");
		priorityListBox.getElement().getStyle().setProperty("width", "70px");

		Label priority = (Label) flexTable.getWidget(row, 5);
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
		flexTable.setWidget(row, 5, priorityListBox);

		ListBox assignedToListBox = new ListBox();
		assignedToListBox.setStyleName("listBoxStyle");
		assignedToListBox.getElement().getStyle().setProperty("padding", "2px");
		assignedToListBox.getElement().getStyle().setProperty("width", "90px");

		Label assignedTo = (Label) flexTable.getWidget(row, 6);
		assignedToListBox.addItem(assignedTo.getText());
		for (UserDTO user : users) {
			if (!user.getName().equals(assignedTo.getText())) {
				assignedToListBox.addItem(user.getName());
			}
		}
		flexTable.setWidget(row, 6, assignedToListBox);

		final ListBox productListBox = new ListBox();
		productListBox.setStyleName("listBoxStyle");
		productListBox.getElement().getStyle().setProperty("padding", "2px");
		productListBox.getElement().getStyle().setProperty("width", "50px");

		Label product = (Label) flexTable.getWidget(row, 7);
		productListBox.addItem(product.getText());

		for (ProductDTO productDTO : products) {
			if (!productDTO.getName().equals(product.getText())) {
				productListBox.addItem(productDTO.getName());
			}
		}
		flexTable.setWidget(row, 7, productListBox);

		// Module section
		final ListBox moduleListBox = new ListBox();
		moduleListBox.setStyleName("listBoxStyle");
		moduleListBox.getElement().getStyle().setProperty("padding", "2px");
		moduleListBox.getElement().getStyle().setProperty("width", "100px");

		final Label module = (Label) flexTable.getWidget(row, 8);
		moduleListBox.addItem(module.getText());

		final ListBox subSystemListBox = new ListBox();
		subSystemListBox.setStyleName("listBoxStyle");
		subSystemListBox.getElement().getStyle().setProperty("padding", "2px");
		subSystemListBox.getElement().getStyle().setProperty("width", "100px");

		final Label subSystem = (Label) flexTable.getWidget(row, 9);
		subSystemListBox.addItem(subSystem.getText());

		moduleService.getModulesByProductName(product.getText(), new AsyncCallback<List<ModuleDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ModuleDTO> modules) {
				// TODO Auto-generated method stub
				for (ModuleDTO moduleDTO : modules) {
					if (moduleDTO.getParentModuleDTO() == null && !moduleDTO.getName().equals(module.getText())) {
						moduleListBox.addItem(moduleDTO.getName());
					} else if (moduleDTO.getParentModuleDTO() != null
							&& !moduleDTO.getName().equals(subSystem.getText())) {
						subSystemListBox.addItem(moduleDTO.getName());
					}
				}
				flexTable.setWidget(row, 8, moduleListBox);
				flexTable.setWidget(row, 9, subSystemListBox);
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
								subSystemListBox.clear();
								subSystemListBox.addItem("NULL");
								for (ModuleDTO moduleDTO : modules) {
									if (moduleDTO.getParentModuleDTO() == null
											&& !moduleDTO.getName().equals(module.getText())) {
										moduleListBox.addItem(moduleDTO.getName());
									}
								}
							}

						});
			}

		});

		moduleListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				moduleService.getChildModulesByParentName(moduleListBox.getSelectedValue(),
						new AsyncCallback<List<ModuleDTO>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(List<ModuleDTO> modules) {
								subSystemListBox.clear();
								subSystemListBox.addItem("NULL");
								for (ModuleDTO moduleDTO : modules) {
									subSystemListBox.addItem(moduleDTO.getName());
								}
							}

						});

			}

		});
	}

	// Method to revert fields to normal state with updated text
	private void revertFieldsToNormalState(final FlexTable flexTable) {
		for (int row = 1; row < flexTable.getRowCount(); row++) {
			try {

				final String title = ((TextBox) flexTable.getWidget(row, 3)).getText();

				ListBox typeListBox = (ListBox) flexTable.getWidget(row, 2);
				final String type = typeListBox.getSelectedValue();

				ListBox stepListBox = (ListBox) flexTable.getWidget(row, 4);
				final String step = stepListBox.getSelectedValue();

				ListBox priorityListBox = (ListBox) flexTable.getWidget(row, 5);
				final String priority = priorityListBox.getSelectedValue();

				ListBox assignedToListBox = (ListBox) flexTable.getWidget(row, 6);
				final String assign = assignedToListBox.getSelectedValue();

				ListBox productListBox = (ListBox) flexTable.getWidget(row, 7);
				final String product = productListBox.getSelectedValue();

				ListBox moduleListBox = (ListBox) flexTable.getWidget(row, 8);
				final String module = moduleListBox.getSelectedValue();

				ListBox subSystemListBox = (ListBox) flexTable.getWidget(row, 9);
				final String subSystem = subSystemListBox.getSelectedValue();

				final long id = Long.parseLong(flexTable.getText(row, 1));
				final int index = row;

				taskService.getTaskById(id, new AsyncCallback<TaskDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Got error");
					}

					@Override
					public void onSuccess(TaskDTO taskDTO) {
						List<String> values = new ArrayList<>();
						values.add(type);
						values.add(step);
						values.add(priority);

						List<String> moduleNames = new ArrayList<>();
						moduleNames.add(module);
						moduleNames.add(subSystem);

						final Task task = new Task();
						task.setTaskId(taskDTO.getTaskId());
						task.setDescription(taskDTO.getDescription());
						task.setTitle(title);
						task.setPercentComplete(taskDTO.getPercentComplete());
						task.setInitialEstimate(taskDTO.getInitialEstimate());
						task.setRemainingEstimate(taskDTO.getRemainingEstimate());
						task.setDueDate(taskDTO.getDueDate());

						updateTask(task, values, assign, product, moduleNames, index, flexTable);
					}

				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void updateTask(final Task task, final List<String> values, final String assign, final String product,
			final List<String> moduleNames, final int row, final FlexTable flexTable) {

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

						productService.getProductByName(product, new AsyncCallback<ProductDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Error at product Service");
							}

							@Override
							public void onSuccess(ProductDTO productDTO) {
								Product product = typeConverter.convertToProductDao(productDTO);
								task.setProduct(product);
								moduleService.getModulesByNames(moduleNames, new AsyncCallback<List<ModuleDTO>>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO
										// Auto-generated
										// method stub
										Window.alert("Error at moduleService");
									}

									@Override
									public void onSuccess(List<ModuleDTO> moduleDTOs) {
										if (moduleDTOs != null && moduleDTOs.size() > 0) {
											if (moduleDTOs.size() == 2) {
												task.setModule(typeConverter.convertToModuleDao(moduleDTOs.get(0)));
												task.setSubSystem(typeConverter.convertToModuleDao(moduleDTOs.get(1)));
											} else {
												task.setModule(typeConverter.convertToModuleDao(moduleDTOs.get(0)));
											}
										} else {
											task.setModule(null);
											task.setSubSystem(null);
										}

										taskService.saveTask(task, new AsyncCallback<Void>() {

											@Override
											public void onFailure(Throwable caught) {
												Window.alert("Error at adding task");
											}

											@Override
											public void onSuccess(Void result) {
												flexTable.setWidget(row, 2, new Label(task.getType().getValue()));
												flexTable.setWidget(row, 3, new Label(task.getTitle()));
												flexTable.setWidget(row, 4, new Label(task.getStatus().getValue()));
												flexTable.setWidget(row, 5, new Label(task.getPriority().getValue()));
												flexTable.setWidget(row, 6, new Label(task.getUser().getName()));
												flexTable.setWidget(row, 7, new Label(task.getProduct().getName()));
												flexTable.setWidget(row, 8, new Label(moduleNames.get(0)));
												flexTable.setWidget(row, 9, new Label(moduleNames.get(1)));
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

		addBtn = new Button("Add");
		editBtn = new Button("Edit");
		deleteBtn = new Button("Delete");

		addBtn.setStyleName("customBtn");
		editBtn.setStyleName("customBtn");
		deleteBtn.setStyleName("customBtn");

		headerPanel.addWest(hpanel, 300);
		headerPanel.addEast(createButtonsPanel(addBtn, editBtn, deleteBtn), 300);

		return headerPanel;
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
}
