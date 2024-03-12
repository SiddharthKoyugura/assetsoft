package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.dao.LookupDao;
import com.assetsense.assetsoft.domain.Lookup;
import com.assetsense.assetsoft.service.LookupService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LookupServiceImpl extends RemoteServiceServlet implements LookupService {

	private LookupDao lookupDao;

	@Override
	public List<Lookup> getLookupsByCatId(long catId) {
		lookupDao = (LookupDao) ApplicationContextListener.applicationContext.getBean("lookupDao");
		return lookupDao.getLookupsByCatId(catId);
	}

	@Override
	public Lookup getLookupByValue(String value) {
		lookupDao = (LookupDao) ApplicationContextListener.applicationContext.getBean("lookupDao");
		return lookupDao.getLookupByValue(value);
	}

	@Override
	public List<Lookup> getLookupsByValues(List<String> values) {
		// TODO Auto-generated method stub
		lookupDao = (LookupDao) ApplicationContextListener.applicationContext.getBean("lookupDao");
		return lookupDao.getLookupsByValues(values);
	}
}
