package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Lookup;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LookupServiceAsync {
	void getLookupsByCatId(long catId, AsyncCallback<List<Lookup>> callback);
	void getLookupByValue(String value, AsyncCallback<Lookup> callback);
	void getLookupsByValues(List<String> values, AsyncCallback<List<Lookup>> callback);
}
