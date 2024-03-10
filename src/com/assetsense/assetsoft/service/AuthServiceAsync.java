package com.assetsense.assetsoft.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthServiceAsync {
//	void checkAuthentication(AsyncCallback<Boolean> callback);
	void authenticateUser(String email, String password, AsyncCallback<String> callback);
}
