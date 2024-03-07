package com.assetsense.assetsoft.service;

import com.assetsense.assetsoft.domain.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {
	void getUserByEmail(String email, AsyncCallback<User> callback);
	void loadUserByUsername(String email, AsyncCallback<User> callback);
}
