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

}
