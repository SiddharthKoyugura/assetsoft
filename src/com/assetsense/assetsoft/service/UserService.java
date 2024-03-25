package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.UserDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {
	
	void saveUser(User user);
	void deleteUser(User user);
	UserDTO getUserByEmail(String email);
	User getUserById(long id);
	List<UserDTO> getUsers();
	UserDTO getUserByName(String name);
	void addUserToTeam(User user, Team team);
	List<UserDTO> getUsersFromTeam(Team team);
}
