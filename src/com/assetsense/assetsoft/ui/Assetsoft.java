package com.assetsense.assetsoft.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Assetsoft implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		TaskDashboard taskDashboard = new TaskDashboard();
		
		
		RootPanel.get().add(taskDashboard.buildNavBar());
		
		RootPanel.get().add(taskDashboard.buildLeftSidebar());
	}
}