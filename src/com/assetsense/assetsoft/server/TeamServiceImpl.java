package com.assetsense.assetsoft.server;

import java.util.List;

import com.assetsense.assetsoft.dao.TeamDao;
import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.service.TeamService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TeamServiceImpl extends RemoteServiceServlet implements TeamService {
	
	private TeamDao teamDao;

	@Override
	public void saveTeam(Team team) {
		// TODO Auto-generated method stub
		teamDao = (TeamDao) ApplicationContextListener.applicationContext.getBean("teamDao");
		teamDao.saveTeam(team);
	}

	@Override
	public void deleteTeam(Team team) {
		// TODO Auto-generated method stub
		teamDao = (TeamDao) ApplicationContextListener.applicationContext.getBean("teamDao");
		teamDao.deleteTeam(team);
	}

	@Override
	public Team getTeamById(long id) {
		// TODO Auto-generated method stub
		teamDao = (TeamDao) ApplicationContextListener.applicationContext.getBean("teamDao");
		return teamDao.getTeamById(id);
	}

	@Override
	public List<Team> getTeams() {
		// TODO Auto-generated method stub
		teamDao = (TeamDao) ApplicationContextListener.applicationContext.getBean("teamDao");
		return teamDao.getTeams();
	}

}
