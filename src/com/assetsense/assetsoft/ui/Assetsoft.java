package com.assetsense.assetsoft.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Assetsoft implements EntryPoint {

	@Override
	public void onModuleLoad() {

		TaskDashboard taskDashboard = new TaskDashboard();
		AddEditForm addEditForm = new AddEditForm();
		
//		RootLayoutPanel.get().add(taskDashboard.getTaskDashboard());
		
		
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		
		dpanel.addNorth(taskDashboard.buildNavBar(), 50);
		
		vpanel.add(addEditForm.buildFormHeader());
		vpanel.add(addEditForm.buildForm());
		dpanel.add(vpanel);
		RootLayoutPanel.get().add(dpanel);
	}
}