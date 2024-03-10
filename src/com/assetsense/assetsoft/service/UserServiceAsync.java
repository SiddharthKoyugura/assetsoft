package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.UserDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {
	
	void saveUser(User user, AsyncCallback<Void> callback);
	void deleteUser(User user, AsyncCallback<Void> callback);
	void getUserByEmail(String email, AsyncCallback<UserDTO> callback);
	void getUserById(long id, AsyncCallback<User> callback);
	void getUsers(AsyncCallback<List<UserDTO>> callback);
}
