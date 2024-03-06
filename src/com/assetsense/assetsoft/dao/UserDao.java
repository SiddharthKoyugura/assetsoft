package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

import com.assetsense.assetsoft.domain.User;

public class UserDao {
	HibernateTemplate template;

	public HibernateTemplate getTemplate() {
		return template;
	}

	public void setTemplate(HibernateTemplate template) {
		this.template = template;
	}

	// method to add user
	public void saveUser(User user){
		template.save(user);
	}

	// method to update user
	public void updateUser(User user){
		template.save(user);
	}

	// method to delete user
	public void deleteUser(User user) {
		template.delete(user);
	}

	// method to return one user of given id
	public User getUserById(long id){
		User user = (User) template.get(User.class, id);
		return user;
	}
	
	// Method to return user of given email
	public User getUserByEmail(String email) {
		User user = (User) template.get(User.class, email);
		return user;
	}

	// method to return all users
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		users = template.loadAll(User.class);
		return users;
	}
}
