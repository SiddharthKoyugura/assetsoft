package com.assetsense.assetsoft.dao;

import java.util.List;

import com.assetsense.assetsoft.domain.Lookup;

public interface LookupDao {
	List<Lookup> getLookupsByCatId(long catId);

	Lookup getLookupByValue(String value);

	List<Lookup> getLookupsByValues(List<String> values);
}
