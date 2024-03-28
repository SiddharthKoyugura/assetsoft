package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.daoImpl.UserDaoImpl;
import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.UserDTO;
import com.assetsense.assetsoft.service.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {

	private UserDaoImpl userDao;

	@Override
	public void saveUser(User user) {
		userDao = (UserDaoImpl) ApplicationContextListener.applicationContext.getBean("userDao");
		userDao.saveUser(user);
	}

	@Override
	public void deleteUser(User user) {
		userDao = (UserDaoImpl) ApplicationContextListener.applicationContext.getBean("userDao");
		userDao.deleteUser(user);
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		userDao = (UserDaoImpl) ApplicationContextListener.applicationContext.getBean("userDao");
		return userDao.getUserByEmail(email);
	}

	@Override
	public User getUserById(long id) {
		userDao = (UserDaoImpl) ApplicationContextListener.applicationContext.getBean("userDao");
		return userDao.getUserById(id);
	}

	@Override
	public List<UserDTO> getUsers() {
		userDao = (UserDaoImpl) ApplicationContextListener.applicationContext.getBean("userDao");
		return userDao.getUsers();
	}

	@Override
	public UserDTO getUserByName(String name) {
		userDao = (UserDaoImpl) ApplicationContextListener.applicationContext.getBean("userDao");
		return userDao.getUserByName(name);
	}

	@Override
	public void addUserToTeam(User user, Team team) {
		userDao = (UserDaoImpl) ApplicationContextListener.applicationContext.getBean("userDao");
		userDao.addUserToTeam(user, team);
	}

	@Override
	public List<UserDTO> getUsersFromTeam(Team team) {
		userDao = (UserDaoImpl) ApplicationContextListener.applicationContext.getBean("userDao");
		return userDao.getUsersFromTeam(team);
	}

}
