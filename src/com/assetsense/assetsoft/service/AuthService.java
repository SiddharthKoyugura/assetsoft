package com.assetsense.assetsoft.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("auth")
public interface AuthService extends RemoteService {
//	boolean checkAuthentication();
	String authenticateUser(String email, String password);
}
