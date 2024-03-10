package com.assetsense.assetsoft.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
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
	
	private Button saveBtn;
	private Button closeBtn;
	
	private TextArea descriptionField;
	private ListBox workItemTypeField;
	private ListBox workFlowStepField;
	private ListBox assignedToField;
	private TextBox initialEstField;
	// Todo: Need to update
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
		hpanel.getElement().getStyle().setProperty("padding", "80px 100px 0 100px");
		hpanel.setWidth("100%");
		
		hpanel.add(buildLeftPanel());
		hpanel.add(buildRightPanel());
		
		return hpanel;
	}
	
	private VerticalPanel buildLeftPanel(){
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		
		Label l1 = new Label("Work Item Type:");
		l1.setStyleName("mr-5");
		workItemTypeField = new ListBox();
		workItemTypeField.setStyleName("listBoxStyle");
		workItemTypeField.addItem("Hello");
		workItemTypeField.addItem("Hey");
		workItemTypeField.addItem("Hello dude");

		Label l2 = new Label("Workflow step:");
		l2.setStyleName("mr-5");
		workFlowStepField = new ListBox();
		workFlowStepField.setStyleName("listBoxStyle");
		workFlowStepField.addItem("Hello");
		workFlowStepField.addItem("Hey");
		workFlowStepField.addItem("Hello dude");

		
		Label l3 = new Label("Assigned To:");
		l3.setStyleName("mr-5");
		assignedToField = new ListBox();
		assignedToField.setStyleName("listBoxStyle");
		assignedToField.addItem("Hello");
		assignedToField.addItem("Hey");
		assignedToField.addItem("Hello dude");


		Label l4 = new Label("Initial Estimate:");
		l4.setStyleName("mr-5");
		initialEstField = new TextBox();
		initialEstField.setStyleName("listBoxStyle");


		Label l5 = new Label("Title:");
		l5.setStyleName("mr-5");
		titleField = new TextBox();
		titleField.setStyleName("listBoxStyle");
		
		l1.setStyleName("taskLabel");
		l2.setStyleName("taskLabel");
		l3.setStyleName("taskLabel");
		l4.setStyleName("taskLabel");
		l5.setStyleName("taskLabel");
		
		l1.addStyleName("imp");
		l2.addStyleName("imp");
		l5.addStyleName("imp");

		Grid grid = new Grid(5, 2);
		grid.setCellPadding(5);
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");
		
		grid.setWidget(0, 0, l5);
		grid.setWidget(1, 0, l1);
		grid.setWidget(2, 0, l2);
		grid.setWidget(3, 0, l3);
		grid.setWidget(4, 0, l4);
		
		grid.setWidget(0, 1, titleField);
		grid.setWidget(1, 1, workItemTypeField);
		grid.setWidget(2, 1, workFlowStepField);
		grid.setWidget(3, 1, assignedToField);
		grid.setWidget(4, 1, initialEstField);
		
		
		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");
		grid.getCellFormatter().setStyleName(3, 0, "text-right");
		grid.getCellFormatter().setStyleName(4, 0, "text-right");

		vpanel.add(grid);

		return vpanel;
	}
	
	private VerticalPanel buildRightPanel(){
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		
		Label l1 = new Label("Product:");
		l1.setStyleName("mr-5");
		productField = new ListBox();
		productField.setStyleName("listBoxStyle");
		productField.addItem("Hello");
		productField.addItem("Hey");
		productField.addItem("Hello dude");

		Label l2 = new Label("Priority:");
		l2.setStyleName("mr-5");
		priorityField = new ListBox();
		priorityField.setStyleName("listBoxStyle");
		priorityField.addItem("Hello");
		priorityField.addItem("Hey");
		priorityField.addItem("Hello dude");


		Label l3 = new Label("Remaining Estimate:");
		l3.setStyleName("mr-5");
		remainingEstField = new TextBox();
		remainingEstField.setStyleName("listBoxStyle");


		Label l4 = new Label("Percent Complete:");
		l4.setStyleName("mr-5");
		percentField = new IntegerBox();
		percentField.setStyleName("listBoxStyle");
		
		Label l5 = new Label("Due Date:");
		l5.setStyleName("mr-5");
		dueDateField = new TextBox();
		dueDateField.setStyleName("listBoxStyle");
		
		l1.setStyleName("taskLabel");
		l2.setStyleName("taskLabel");
		l3.setStyleName("taskLabel");
		l4.setStyleName("taskLabel");
		l5.setStyleName("taskLabel");

		Grid grid = new Grid(5, 2);
		grid.setCellPadding(5);
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");
		
		grid.setWidget(0, 0, l1);
		grid.setWidget(1, 0, l2);
		grid.setWidget(3, 0, l3);
		grid.setWidget(2, 0, l4);
		grid.setWidget(4, 0, l5);
		
		grid.setWidget(0, 1, productField);
		grid.setWidget(1, 1, priorityField);
		grid.setWidget(3, 1, remainingEstField);
		grid.setWidget(2, 1, percentField);
		grid.setWidget(4, 1, dueDateField);
		
		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");
		grid.getCellFormatter().setStyleName(3, 0, "text-right");
		grid.getCellFormatter().setStyleName(4, 0, "text-right");
		

		vpanel.add(grid);

		return vpanel;
	}

}
