package com.assetsense.assetsoft.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.assetsense.assetsoft.domain.User;
import com.google.gwt.user.client.rpc.RemoteService;

public interface UserService extends RemoteService, UserDetailsService {
	User getUserByEmail(String email);
	
}
