package com.assetsense.assetsoft.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.assetsense.assetsoft.dto.TeamDTO;
import com.assetsense.assetsoft.dto.UserDTO;
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
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

	private final UserServiceAsync userService = GWT.create(UserService.class);
	private final TeamServiceAsync teamService = GWT.create(TeamService.class);
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	private final ProductServiceAsync productService = GWT.create(ProductService.class);

	private int rowIndex = 1;
	private Boolean isAdmin = false;

	private Button navBtn;
	private Button addBtn;
	private Button editBtn;
	private Button deleteBtn;
	private Button adminBtn;
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

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
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

	public void setAdminBtnHandler(ClickHandler handler) {
		adminBtn.addClickHandler(handler);
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

	private Tree buildProductsTree() {
		final Tree treeWidget = new Tree(customTreeResources);
		
		treeWidget.setStyleName("parentTree");
		
		final TreeItem tree = new TreeItem(new Label("All Products"));
		tree.setStyleName("treeHeading");
		treeWidget.addItem(tree);
		

		productService.getProducts(new AsyncCallback<List<ProductDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ProductDTO> products) {
				// TODO Auto-generated method stub
				for (ProductDTO product : products) {
					if (product.getParentProductDTO() == null) {
						TreeItem rootItem = new TreeItem(createProductWidget(product));
						rootItem.setStyleName("treeHeading");
						buildSubProductsTree(product, rootItem);
						tree.addItem(rootItem);
					}
				}
				tree.setState(true);
			}

		});

		return treeWidget;
	}

	private void buildSubProductsTree(final ProductDTO product, final TreeItem parentItem) {

		productService.getChildProductsByParentId(product.getProductId(), new AsyncCallback<List<ProductDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ProductDTO> products) {
				// TODO Auto-generated method stub
				for (ProductDTO child : products) {
					TreeItem childItem = new TreeItem(createProductWidget(child));
					parentItem.addItem(childItem);
					buildSubProductsTree(child, childItem);
					childItem.setState(true);
				}
				parentItem.setState(true);
			}

		});
	}

	private Label createProductWidget(ProductDTO product) {
		Label label = new Label(product.getName());
		return label;
	}

	private Tree buildUsersTree() {
		Tree tree = new Tree(customTreeResources);

		tree.setStyleName("parentTree");

		TreeItem mainItem = new TreeItem();
		mainItem.setText("Users & Teams");
		mainItem.setStyleName("treeHeading");

		final TreeItem item1 = new TreeItem();
		item1.setText("All Users");

		final TreeItem item2 = new TreeItem();
		item2.setText("All Teams");

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

		mainItem.addItem(item1);
		mainItem.addItem(item2);
		mainItem.setState(true);

		tree.addItem(mainItem);
		return tree;
	}

	public VerticalPanel buildLeftSidebar() {
		VerticalPanel vpanel = new VerticalPanel();

		vpanel.setStyleName("leftSidebar");
		vpanel.add(buildProductsTree());
		HTML hrLine = new HTML("<div class='hr' />");
		vpanel.add(hrLine);
		vpanel.add(buildUsersTree());

		return vpanel;
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
		// Grid headerGrid = new Grid(17, 7);
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

					flexTable.setWidget(rowIndex, col++, cb);
					flexTable.setText(rowIndex, col++, String.valueOf(task.getTaskId()));
					flexTable.setWidget(rowIndex, col++, type);
					flexTable.setWidget(rowIndex, col++, title);
					flexTable.setWidget(rowIndex, col++, step);
					flexTable.setWidget(rowIndex, col++, priority);
					flexTable.setWidget(rowIndex, col++, assignedTo);
					flexTable.setWidget(rowIndex, col++, product);

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
					            	userService.getUsers(new AsyncCallback<List<UserDTO>>(){

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											
										}

										@Override
										public void onSuccess(final List<UserDTO> users) {
											// TODO Auto-generated method stub
											productService.getTopMostParentProducts(new AsyncCallback<List<ProductDTO>>(){

												@Override
												public void onFailure(Throwable caught) {
													// TODO Auto-generated method stub
													
												}

												@Override
												public void onSuccess(List<ProductDTO> products) {
													// TODO Auto-generated method stub
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
				RootLayoutPanel.get().addDomHandler(new ClickHandler(){

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
	private void convertRowToEditable(int row, FlexTable flexTable, List<UserDTO> users, List<ProductDTO> products) {

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

	    Label step =(Label) flexTable.getWidget(row, 4);
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

	    ListBox productListBox = new ListBox();
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


	    titleTextBox.setFocus(true);
	}

	// Method to revert fields to normal state with updated text
	private void revertFieldsToNormalState(final FlexTable flexTable) {
		for (int row = 1; row < flexTable.getRowCount(); row++) {
	        try {
	        	
	        	final Label titleLabel = new Label(((TextBox) flexTable.getWidget(row, 3)).getText());
	        	
		        ListBox typeListBox = (ListBox) flexTable.getWidget(row, 2);
		        final Label typeLabel = new Label(typeListBox.getSelectedValue());

		        ListBox stepListBox = (ListBox) flexTable.getWidget(row, 4);
		        final Label stepLabel = new Label(stepListBox.getSelectedValue());

		        ListBox priorityListBox = (ListBox) flexTable.getWidget(row, 5);
		        final Label priorityLabel = new Label(priorityListBox.getSelectedValue());
		        
		        ListBox assignedToListBox = (ListBox) flexTable.getWidget(row, 6);
		        final Label assignedToLabel = new Label(assignedToListBox.getSelectedValue());
		        
		        ListBox productListBox = (ListBox) flexTable.getWidget(row, 7);
		        final Label productLabel = new Label(productListBox.getValue(productListBox.getSelectedIndex()));
		        
		        final long id = Long.parseLong(flexTable.getText(row, 1));
	        	final int index = row;
	        	
	        	taskService.getTaskById(id, new AsyncCallback<TaskDTO>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Got error");
					}

					@Override
					public void onSuccess(TaskDTO taskDTO) {
						updateTaskTitle(id, taskDTO.getTitle(), index, flexTable, titleLabel);
						updateTaskLookup(id, "type", taskDTO.getType().getValue(), index, 2, flexTable, typeLabel);
						updateTaskLookup(id, "status", taskDTO.getStatus().getValue(), index, 4, flexTable, stepLabel);
						updateTaskLookup(id, "priority", taskDTO.getPriority().getValue(), index, 5, flexTable, priorityLabel);
						updateTaskUser(id, taskDTO.getUser().getName(), index, 6, flexTable, assignedToLabel);
						updateTaskProduct(id, taskDTO.getProduct().getName(), index, 7, flexTable, productLabel);
					}
	        		
	        	});
	        	
		        
	        }catch(Exception e){
	        	
	        }
	    }
	}

	private void updateTaskProduct(final long id, final String previousProductName, final int row, final int col,
			final FlexTable flexTable, final Label label) {
		String updatedProductName = label.getText();
		if(previousProductName.equals(updatedProductName)){
			flexTable.setWidget(row, col, label);
			return;
		}
		taskService.editTaskProduct(id, updatedProductName, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Void result) {
				flexTable.setWidget(row, col, label);
			}

		});
	}

	private void updateTaskUser(final long id, final String previousUsername, final int row, final int col,
			final FlexTable flexTable, final Label label) {
		final String updatedUsername = label.getText();
		if(updatedUsername.equals(previousUsername)){
			flexTable.setWidget(row, col, label);
			return;
		}
		taskService.editTaskUser(id, updatedUsername, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Void result) {
				flexTable.setWidget(row, col, label);
			}

		});
	}

	private void updateTaskLookup(final long id, final String name, String previousLookup, final int row, final int col,
			final FlexTable flexTable, final Label label) {
		final String updatedLookup = label.getText();
		if (updatedLookup.equals(previousLookup)) {
			flexTable.setWidget(row, col, label);
		} else {
			taskService.editTaskLookup(id, name, updatedLookup, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(Void result) {
					flexTable.setWidget(row, col, label);
				}

			});
		}
	}

	private void updateTaskTitle(final long id, String previousTitle, final int index, final FlexTable flexTable,
			final Label label) {
		String updatedTitle = label.getText();
		if (previousTitle.equals(updatedTitle)) {
			flexTable.setWidget(index, 3, label);
		} else {
			taskService.editTaskTitle(id, updatedTitle, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Failed");
				}

				@Override
				public void onSuccess(Void result) {
					flexTable.setWidget(index, 3, label);
				}

			});
		}
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

		adminBtn = new Button("Admin Page");
		adminBtn.setStyleName("customBtn");

		addBtn = new Button("Add");
		editBtn = new Button("Edit");
		deleteBtn = new Button("Delete");

		addBtn.setStyleName("customBtn");
		editBtn.setStyleName("customBtn");
		deleteBtn.setStyleName("customBtn");

		headerPanel.addWest(hpanel, 300);
		if (isAdmin) {
			headerPanel.addEast(createButtonsPanel(adminBtn, addBtn, editBtn, deleteBtn), 420);
		} else {
			headerPanel.addEast(createButtonsPanel(addBtn, editBtn, deleteBtn), 300);
		}
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
