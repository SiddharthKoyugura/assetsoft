package com.assetsense.assetsoft.ui;

import java.util.List;

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
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaskDashboard {

	private final UserServiceAsync userService = GWT.create(UserService.class);
	private final TeamServiceAsync teamService = GWT.create(TeamService.class);
	private final TaskServiceAsync taskService = GWT.create(TaskService.class);
	private final ProductServiceAsync productService = GWT.create(ProductService.class);

	private int rowIndex = 1;

	private Button navBtn;
	private Button addBtn;
	private Button editBtn;

	public void setNavBtnName(String name) {
		getNavBtn().setText(name);
	}

	private Button getNavBtn() {
		if (navBtn == null) {
			navBtn = new Button();
		}
		return navBtn;
	}

	public void setBtnHandler(ClickHandler handler) {
		addBtn.addClickHandler(handler);
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
		final Tree tree = new Tree(customTreeResources);

		tree.setStyleName("parentTree");
		
		productService.getProducts(new AsyncCallback<List<ProductDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<ProductDTO> products) {
				// TODO Auto-generated method stub
				for(ProductDTO product : products){
					if(product.getParentProductDTO() == null){
						TreeItem rootItem = new TreeItem(createProductWidget(product));
						rootItem.setStyleName("treeHeading");
		                buildSubProductsTree(product, rootItem);
		                tree.addItem(rootItem);
					}
				}
			}
			
		});

		return tree;
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
		        }
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
		item1.setState(true);
		item1.setText("All Users");

		final TreeItem item2 = new TreeItem();
		item2.setState(true);
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

		CheckBox headerCheckBox = new CheckBox();
		flexTable.setWidget(0, 0, headerCheckBox);
		flexTable.setText(0, 1, "ID");
		flexTable.setText(0, 2, "Type");
		flexTable.setText(0, 3, "Title");
		flexTable.setText(0, 4, "Work flow step");
		flexTable.setText(0, 5, "Priority");
		flexTable.setText(0, 6, "Assigned to");
		flexTable.setText(0, 7, "Project");

		taskService.getTasks(new AsyncCallback<List<TaskDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(List<TaskDTO> tasks) {
				for (TaskDTO task : tasks) {
					// addRow(flexTable, rowIndex++,
					// String.valueOf(task.getTaskId()),
					// task.getType().getValue(),
					// task.getTitle(), task.getPriority().getValue(),
					// task.getUser().getName());
					flexTable.getRowFormatter().setStyleName(rowIndex, "taskCell");
					int col = 0;
					CheckBox cb = new CheckBox();
					flexTable.setWidget(rowIndex, col++, cb);

					flexTable.setText(rowIndex, col++, String.valueOf(task.getTaskId()));
					flexTable.setText(rowIndex, col++, task.getType() != null ? task.getType().getValue() : "NULL");
					flexTable.setText(rowIndex, col++, task.getTitle() != null ? task.getTitle() : "NULL");
					flexTable.setText(rowIndex, col++, task.getStatus() != null ? task.getStatus().getValue() : "NULL");
					flexTable.setText(rowIndex, col++,
							task.getPriority() != null ? task.getPriority().getValue() : "NULL");
					flexTable.setText(rowIndex, col++, task.getUser() != null ? task.getUser().getName() : "NULL");
					flexTable.setText(rowIndex, col++,
							task.getProduct() != null ? task.getProduct().getName() : "NULL");

					rowIndex++;
				}
			}

		});

		// addRow(flexTable, 1, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 2, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 3, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 4, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 5, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 6, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 7, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 8, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 9, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 10, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 11, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 12, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 13, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 14, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 15, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");
		// addRow(flexTable, 16, "236", "Bug", "Hibernate Issue", "High",
		// "Siddhardha Koyugura", "C2");

		spanel.add(flexTable);
		vpanel.add(spanel);
		return vpanel;
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

		addBtn.setStyleName("customBtn");
		editBtn.setStyleName("customBtn");

		headerPanel.addWest(hpanel, 300);
		headerPanel.addEast(createButtonsPanel(addBtn, editBtn), 200);

		return headerPanel;
	}

	private HorizontalPanel createButtonsPanel(Button... buttons) {
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.getElement().getStyle().setProperty("padding", "10px");
		buttonsPanel.setWidth("100%");

		for (Button button : buttons) {
			buttonsPanel.add(button);
		}

		buttonsPanel.setCellHorizontalAlignment(buttons[0], HasHorizontalAlignment.ALIGN_RIGHT);
		return buttonsPanel;
	}
}
