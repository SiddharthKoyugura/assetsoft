package com.assetsense.assetsoft.ui;

import java.util.List;

import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.dto.UserDTO;
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.assetsense.assetsoft.service.ModuleService;
import com.assetsense.assetsoft.service.ModuleServiceAsync;
import com.assetsense.assetsoft.service.ProductService;
import com.assetsense.assetsoft.service.ProductServiceAsync;
import com.assetsense.assetsoft.service.UserService;
import com.assetsense.assetsoft.service.UserServiceAsync;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddEditForm {
	private final UserServiceAsync userService = GWT.create(UserService.class);
	private final ProductServiceAsync productService = GWT.create(ProductService.class);
	private final ModuleServiceAsync moduleService = GWT.create(ModuleService.class);

	private Button saveBtn;
	private Button closeBtn;

	private TextArea descriptionField;
	private ListBox workItemTypeField;
	private ListBox workFlowStepField;
	private ListBox assignedToField;
	private ListBox moduleField;
	private TextBox initialEstField;
	private TextBox dueDateField;
	private TextBox titleField;

	private ListBox productField;
	private ListBox priorityField;
	private TextBox remainingEstField;
	private IntegerBox percentField;

	public void setSaveBtnHandler(ClickHandler handler) {
		saveBtn.addClickHandler(handler);
	}

	public void setCloseBtnHandler(ClickHandler handler) {
		closeBtn.addClickHandler(handler);
	}

	public Button getSaveBtn() {
		return saveBtn;
	}

	public Button getCloseBtn() {
		return closeBtn;
	}

	public TextArea getDescriptionField() {
		return descriptionField;
	}

	public ListBox getWorkItemTypeField() {
		return workItemTypeField;
	}

	public ListBox getWorkFlowStepField() {
		return workFlowStepField;
	}

	public ListBox getAssignedToField() {
		return assignedToField;
	}

	public TextBox getInitialEstField() {
		return initialEstField;
	}

	public TextBox getDueDateField() {
		return dueDateField;
	}

	public TextBox getTitleField() {
		return titleField;
	}

	public ListBox getProductField() {
		return productField;
	}

	public ListBox getPriorityField() {
		return priorityField;
	}

	public TextBox getRemainingEstField() {
		return remainingEstField;
	}

	public IntegerBox getPercentField() {
		return percentField;
	}

	public ListBox getModuleField() {
		return moduleField;
	}

	public DockLayoutPanel buildFormHeader() {
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

	public VerticalPanel buildForm() {
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
		grid.setWidget(5, 1, dueDateField);

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

		l1.setStyleName("taskLabel");
		l2.setStyleName("taskLabel");
		l3.setStyleName("taskLabel");
		l4.setStyleName("taskLabel");
		l5.setStyleName("taskLabel");

		final Grid grid = new Grid(6, 2);
		grid.setCellPadding(5);
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		grid.setWidget(0, 0, l1);
		grid.setWidget(1, 0, l5);
		grid.setWidget(2, 0, l2);
		grid.setWidget(3, 0, l3);
		grid.setWidget(4, 0, l4);

		grid.setWidget(1, 1, moduleField);
		grid.setWidget(2, 1, priorityField);
		grid.setWidget(3, 1, remainingEstField);
		grid.setWidget(4, 1, percentField);
		

		productService.getTopMostParentProducts(new AsyncCallback<List<ProductDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(List<ProductDTO> products) {
				for (ProductDTO product : products) {
					productField.addItem(product.getName());
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
	
	
	public void updateModuleField(){
		String productName = productField.getSelectedValue();
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
