package com.assetsense.assetsoft.service;

import com.assetsense.assetsoft.domain.User;
import com.google.gwt.user.client.rpc.RemoteService;

public interface UserService extends RemoteService {
	User getUserByEmail(String email);
}