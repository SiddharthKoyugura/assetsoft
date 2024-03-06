package com.assetsense.assetsoft.server;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.assetsense.assetsoft.dao.UserDao;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.service.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserServiceImpl extends RemoteServiceServlet implements UserService  {

	private static final long serialVersionUID = 1L;
	
	
	Resource r = new ClassPathResource("applicationContext.xml");
	BeanFactory factory = new XmlBeanFactory(r);
	
	UserDao dao = (UserDao) factory.getBean("userDao");

	@Override
	public User getUserByEmail(String email) {
		return dao.getUserByEmail(email);
	}


}
