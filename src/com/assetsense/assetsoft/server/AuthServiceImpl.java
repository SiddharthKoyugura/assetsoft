package com.assetsense.assetsoft.server;

import com.assetsense.assetsoft.daoImpl.UserDaoImpl;
import com.assetsense.assetsoft.dto.UserDTO;
import com.assetsense.assetsoft.service.AuthService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class AuthServiceImpl extends RemoteServiceServlet implements AuthService {
	
	private UserDaoImpl userDao;

//	@Override
//	public boolean checkAuthentication() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		
//		if(authentication != null && authentication.isAuthenticated()){
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			
//			System.out.println("Authenticated User: " + userDetails.getUsername());
//			
//			return true;
//		}
//		return false;
//	}

	@Override
	public UserDTO authenticateUser(String email, String password) {
		userDao = (UserDaoImpl) ApplicationContextListener.applicationContext.getBean("userDao");
		UserDTO user = userDao.getUserByEmail(email);
		if(user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}
	
}
