package com.assetsense.assetsoft.ui;

import java.util.ArrayList;
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
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
				for (ProductDTO product : products) {
					if (product.getParentProductDTO() == null) {
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

					title.addDoubleClickHandler(new DoubleClickHandler() {
						private final int index = rowIndex;

						@Override
						public void onDoubleClick(DoubleClickEvent event) {
							// TODO Auto-generated method stub
							final TextBox textBox = new TextBox();
							textBox.setStyleName("listBoxStyle");
							textBox.getElement().getStyle().setProperty("padding", "2px");
							textBox.getElement().getStyle().setProperty("width", "100%");
							textBox.setText(title.getText());
							flexTable.setWidget(index, 3, textBox);
							textBox.setFocus(true);
							textBox.addKeyUpHandler(new KeyUpHandler() {

								@Override
								public void onKeyUp(KeyUpEvent event) {
									// TODO Auto-generated method stub
									if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
										updateTaskTitle(task.getTaskId(), textBox, index, flexTable, title);
									}
								}

							});
							RootLayoutPanel.get().addDomHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									updateTaskTitle(task.getTaskId(), textBox, index, flexTable, title);
								}

							}, ClickEvent.getType());
						}

					});

					type.addDoubleClickHandler(new DoubleClickHandler() {
						private final int index = rowIndex;

						@Override
						public void onDoubleClick(DoubleClickEvent event) {
							final ListBox listBox = new ListBox();
							listBox.setStyleName("listBoxStyle");
							listBox.getElement().getStyle().setProperty("padding", "2px");
							listBox.getElement().getStyle().setProperty("width", "100px");

							listBox.addItem(type.getText());

							List<String> typeList = new ArrayList<>();
							typeList.add("TASK");
							typeList.add("BUG");
							typeList.add("FEATURE");

							for (String typeItem : typeList) {
								if (!typeItem.equals(type.getText())) {
									listBox.addItem(typeItem);
								}
							}

							flexTable.setWidget(index, 2, listBox);
							listBox.setFocus(true);

							RootLayoutPanel.get().addDomHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									if (!type.getText().equals(listBox.getSelectedValue()))
										updateTaskLookup(task.getTaskId(), "type", listBox, index, 2, flexTable, type);
								}

							}, ClickEvent.getType());
						}

					});

					step.addDoubleClickHandler(new DoubleClickHandler() {
						private final int index = rowIndex;

						@Override
						public void onDoubleClick(DoubleClickEvent event) {
							// TODO Auto-generated method stub
							final ListBox listBox = new ListBox();
							listBox.setStyleName("listBoxStyle");
							listBox.getElement().getStyle().setProperty("padding", "2px");
							listBox.getElement().getStyle().setProperty("width", "100px");

							listBox.addItem(step.getText());

							List<String> stepList = new ArrayList<>();
							stepList.add("NEW");
							stepList.add("APPROVED");
							stepList.add("IN_PROGRESS");
							stepList.add("DEV_COMPLETE");
							stepList.add("READY_FOR_TESTING");

							for (String stepItem : stepList) {
								if (!stepItem.equals(step.getText())) {
									listBox.addItem(stepItem);
								}
							}

							flexTable.setWidget(index, 4, listBox);
							listBox.setFocus(true);

							RootLayoutPanel.get().addDomHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									if (!step.getText().equals(listBox.getSelectedValue()))
										updateTaskLookup(task.getTaskId(), "status", listBox, index, 4, flexTable,
												step);
								}

							}, ClickEvent.getType());
						}

					});

					priority.addDoubleClickHandler(new DoubleClickHandler() {
						private final int index = rowIndex;

						@Override
						public void onDoubleClick(DoubleClickEvent event) {
							// TODO Auto-generated method stub
							final ListBox listBox = new ListBox();
							listBox.setStyleName("listBoxStyle");
							listBox.getElement().getStyle().setProperty("padding", "2px");
							listBox.getElement().getStyle().setProperty("width", "70px");

							listBox.addItem(priority.getText());

							List<String> priorityList = new ArrayList<>();
							priorityList.add("HIGH");
							priorityList.add("MEDIUM");
							priorityList.add("LOW");

							for (String priorItem : priorityList) {
								if (!priorItem.equals(priority.getText())) {
									listBox.addItem(priorItem);
								}
							}

							flexTable.setWidget(index, 5, listBox);
							listBox.setFocus(true);

							RootLayoutPanel.get().addDomHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									if (!priority.getText().equals(listBox.getSelectedValue()))
										updateTaskLookup(task.getTaskId(), "priority", listBox, index, 5, flexTable,
												priority);
								}

							}, ClickEvent.getType());
						}

					});

					assignedTo.addDoubleClickHandler(new DoubleClickHandler() {
						private final int index = rowIndex;

						@Override
						public void onDoubleClick(DoubleClickEvent event) {
							// TODO Auto-generated method stub
							final ListBox listBox = new ListBox();
							listBox.setStyleName("listBoxStyle");
							listBox.getElement().getStyle().setProperty("padding", "2px");
							listBox.getElement().getStyle().setProperty("width", "90px");

							listBox.addItem(assignedTo.getText());

							userService.getUsers(new AsyncCallback<List<UserDTO>>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(List<UserDTO> users) {
									// TODO Auto-generated method stub
									for (UserDTO user : users) {
										if (!user.getName().equals(assignedTo.getText())) {
											listBox.addItem(user.getName());
										}
									}
									flexTable.setWidget(index, 6, listBox);
									listBox.setFocus(true);

									RootLayoutPanel.get().addDomHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											if (!assignedTo.getText().equals(listBox.getSelectedValue()))
												updateTaskUser(task.getTaskId(), listBox, index, 6, flexTable,
														assignedTo);
										}

									}, ClickEvent.getType());
								}

							});

						}
					});

					product.addDoubleClickHandler(new DoubleClickHandler() {
						private final int index = rowIndex;

						@Override
						public void onDoubleClick(DoubleClickEvent event) {
							// TODO Auto-generated method stub
							final ListBox listBox = new ListBox();
							listBox.setStyleName("listBoxStyle");
							listBox.getElement().getStyle().setProperty("padding", "2px");
							listBox.getElement().getStyle().setProperty("width", "50px");

							listBox.addItem(product.getText());

							productService.getTopMostParentProducts(new AsyncCallback<List<ProductDTO>>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(List<ProductDTO> products) {
									for (ProductDTO productDTO : products) {
										if (!productDTO.getName().equals(product.getText())) {
											listBox.addItem(productDTO.getName());
										}
									}

									flexTable.setWidget(index, 7, listBox);
									listBox.setFocus(true);

									RootLayoutPanel.get().addDomHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											if (!product.getText().equals(listBox.getSelectedValue()))
												updateTaskProduct(task.getTaskId(), listBox, index, 7, flexTable, product);
										}

									}, ClickEvent.getType());
								}

							});

						}
					});

					rowIndex++;
				}
			}

		});

		spanel.add(flexTable);
		vpanel.add(spanel);
		return vpanel;
	}

	private void updateTaskProduct(final long id, final ListBox listBox, final int row, final int col,
			final FlexTable flexTable, final Label label) {
		
		final String productName = listBox.getSelectedValue();
		taskService.editTaskProduct(id, productName, new AsyncCallback<Void>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				flexTable.setWidget(row, col, label);
				label.setText(productName);
			}
			
		});
	}

	private void updateTaskUser(final long id, final ListBox listBox, final int row, final int col,
			final FlexTable flexTable, final Label label) {
		final String username = listBox.getSelectedValue();
		taskService.editTaskUser(id, username, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				flexTable.setWidget(row, col, label);
				label.setText(username);
			}

		});
	}

	private void updateTaskLookup(final long id, final String name, final ListBox listBox, final int row, final int col,
			final FlexTable flexTable, final Label label) {
		final String updatedLookup = listBox.getSelectedValue();
		if (updatedLookup.equals(label.getText())) {
			flexTable.setWidget(row, col, label);
		} else {
			taskService.editTaskLookup(id, name, updatedLookup, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(Void result) {
					flexTable.setWidget(row, col, label);
					label.setText(updatedLookup);
				}

			});
		}
	}

	private void updateTaskTitle(final long id, final TextBox textBox, final int index, final FlexTable flexTable,
			final Label label) {

		final String updatedTitle = textBox.getText();
		if (label.getText().equals(updatedTitle)) {
			flexTable.setWidget(index, 3, label);
		} else {
			taskService.editTaskTitle(id, updatedTitle, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {

				}

				@Override
				public void onSuccess(Void result) {
					flexTable.setWidget(index, 3, label);
					label.setText(updatedTitle);
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
