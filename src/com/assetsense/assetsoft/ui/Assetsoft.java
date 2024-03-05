package com.assetsense.assetsoft.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Assetsoft implements EntryPoint {

	@Override
	public void onModuleLoad() {

		TaskDashboard taskDashboard = new TaskDashboard();
		
		RootLayoutPanel.get().add(taskDashboard.getTaskDashboard());
	}
}