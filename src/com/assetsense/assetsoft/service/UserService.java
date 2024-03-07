package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.User;
import com.google.gwt.user.client.rpc.RemoteService;

public interface UserService extends RemoteService {
	
	void saveUser(User user);
	void deleteUser(User user);
	User getUserByEmail(String email);
	User getUserById(long id);
	List<User> getUsers();
}
