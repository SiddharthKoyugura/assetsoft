package com.assetsense.assetsoft.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;

public class TaskDashboard {


	public HorizontalPanel buildNavBar() {
		HorizontalPanel navbar = new HorizontalPanel();
		navbar.setWidth("100%");
		
		Label l1 = new Label("Assetsoft");
		l1.setStyleName("navLabel");
		
		Button btn = new Button("Sai kiran");
		btn.setStyleName("navBtn");
		
		
		navbar.add(l1);
		navbar.add(btn);

		// Style the navbar
		navbar.setStyleName("navbar");
		return navbar;
	}
	
	public Tree buildTree(){
		Tree tree = new Tree();
		
		
        
        return tree;
	}
	
}
