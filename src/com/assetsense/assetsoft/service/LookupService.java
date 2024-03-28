package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Lookup;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("lookup")
public interface LookupService extends RemoteService {
	List<Lookup> getLookupsByCatId(long catId);

	Lookup getLookupByValue(String value);

	List<Lookup> getLookupsByValues(List<String> values);
}
