package com.assetsense.assetsoft.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Assetsoft implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		TaskDashboard taskDashboard = new TaskDashboard();
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		
		dpanel.setSize("100%", "100%");

		dpanel.addNorth(taskDashboard.buildNavBar(), 48);
		dpanel.addWest(taskDashboard.buildLeftSidebar(), 240);
		dpanel.add(taskDashboard.buildTaskDashboard());
		
		RootLayoutPanel.get().add(dpanel);
	}
}