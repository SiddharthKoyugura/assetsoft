package com.assetsense.assetsoft.dao;

import java.util.List;

import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.UserDTO;

public interface UserDao {
	void saveUser(User user);

	void deleteUser(User user);

	UserDTO getUserByEmail(String email);

	User getUserById(long id);

	List<UserDTO> getUsers();

	UserDTO getUserByName(String name);

	void addUserToTeam(User user, Team team);

	List<UserDTO> getUsersFromTeam(Team team);
}
