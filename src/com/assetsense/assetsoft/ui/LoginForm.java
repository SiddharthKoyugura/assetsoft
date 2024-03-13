package com.assetsense.assetsoft.ui;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginForm {
	private TextBox emailField;
	private PasswordTextBox passwordField;
	private Button loginBtn;
	
	public void setLoginHandler(ClickHandler handler) {
        loginBtn.addClickHandler(handler);
    }
	
	public TextBox getEmailField(){
		return emailField;
	}
	
	public String getEmail() {
		return emailField.getText();
	}

	public String getPassword(){
		return passwordField.getText();
	}
 

	public VerticalPanel buildLoginForm(){
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setStyleName("form-container");
		vpanel.setWidth("100%");
		
		Label l1 = new Label("Email:");
		l1.setStyleName("mr-5");
		l1.addStyleName("taskLabel");
		emailField = new TextBox();
		emailField.setStyleName("listBoxStyle");

		Label l2 = new Label("Password:");
		l2.setStyleName("mr-5");
		l2.addStyleName("taskLabel");
		passwordField = new PasswordTextBox();
		passwordField.setStyleName("listBoxStyle");
		
		loginBtn = new Button("Login");
		loginBtn.setStyleName("customBtn");
		
		

		Grid grid = new Grid(5, 2);
		grid.setCellPadding(10);
		grid.getElement().getStyle().setProperty("margin", "100px 0 0 0");
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");
		
		grid.setWidget(0, 0, l1);
		grid.setWidget(1, 0, l2);
		
		grid.setWidget(0, 1, emailField);
		grid.setWidget(1, 1, passwordField);
		grid.setWidget(3, 1, loginBtn);
		
		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");
		
		grid.getCellFormatter().getElement(3, 1).getStyle().setProperty("textAlign", "left");
		
		vpanel.add(grid);
		return vpanel;
	}
}
