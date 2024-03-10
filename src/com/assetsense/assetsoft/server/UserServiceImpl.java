package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.dao.UserDao;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.UserDTO;
import com.assetsense.assetsoft.service.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {

	private UserDao userDao;

	@Override
	public void saveUser(User user) {
		userDao = (UserDao) ApplicationContextListener.applicationContext.getBean("userDao");
		userDao.saveUser(user);
	}

	@Override
	public void deleteUser(User user) {
		userDao = (UserDao) ApplicationContextListener.applicationContext.getBean("userDao");
		userDao.deleteUser(user);
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		userDao = (UserDao) ApplicationContextListener.applicationContext.getBean("userDao");
		return userDao.getUserByEmail(email);
	}

	@Override
	public User getUserById(long id) {
		userDao = (UserDao) ApplicationContextListener.applicationContext.getBean("userDao");
		return userDao.getUserById(id);
	}

	@Override
	public List<UserDTO> getUsers() {
		userDao = (UserDao) ApplicationContextListener.applicationContext.getBean("userDao");
		return userDao.getUsers();
	}

}
