package com.assetsense.assetsoft.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Assetsoft implements EntryPoint {
	private final TaskDashboard taskDashboard = new TaskDashboard();
	private final AddEditForm addEditForm = new AddEditForm();
	private final LoginForm loginForm = new LoginForm();

	@Override
	public void onModuleLoad() {		
		// Home Page
//		RootLayoutPanel.get().add(buildTaskPage());
			
		// Add Edit form page
//		RootLayoutPanel.get().add(buildAddEditForm());
		
//		RootLayoutPanel.get().add(buildLoginForm());
		String page = Window.Location.getParameter("page");
		
		if("add".equals(page)){
			RootLayoutPanel.get().add(buildAddEditForm());
		}else if("home".equals(page)){
			RootLayoutPanel.get().add(buildTaskPage());
		}else{
			RootLayoutPanel.get().add(buildLoginForm());
		}
		
	}
	
	private DockLayoutPanel buildLoginForm() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		VerticalPanel vpanel = new VerticalPanel();
		
		vpanel.setWidth("100%");
		
		dpanel.addNorth(taskDashboard.buildNavBar(), 50);
		
		vpanel.add(loginForm.buildLoginForm());
		
		dpanel.add(vpanel);

		return dpanel;
	}
	
	private DockLayoutPanel buildAddEditForm() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		
		dpanel.addNorth(taskDashboard.buildNavBar(), 50);
		
		vpanel.add(addEditForm.buildFormHeader());
		vpanel.add(addEditForm.buildForm());
		dpanel.add(vpanel);
		
		return dpanel;
	}
	
	private DockLayoutPanel buildTaskPage() {
		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
		
		dpanel.setSize("100%", "100%");

		dpanel.addNorth(taskDashboard.buildNavBar(), 48);
		dpanel.addWest(taskDashboard.buildLeftSidebar(), 240);
		dpanel.add(taskDashboard.buildTaskDashboard());
		
		return dpanel;
	}

}