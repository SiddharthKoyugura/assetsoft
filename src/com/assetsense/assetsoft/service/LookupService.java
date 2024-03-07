package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Lookup;
import com.google.gwt.user.client.rpc.RemoteService;

public interface LookupService extends RemoteService {
	List<Lookup> getLookupsByCatId(long catId);
}
