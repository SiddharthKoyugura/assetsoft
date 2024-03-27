package com.assetsense.assetsoft.ui;

import com.google.gwt.core.client.EntryPoint;

public class Assetsoft implements EntryPoint {
	private final LoginForm loginForm = new LoginForm();

	@Override
	public void onModuleLoad() {
		loginForm.loadLoginPage();
	}

}