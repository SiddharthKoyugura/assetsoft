package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Team;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("team")
public interface TeamService extends RemoteService {
	Team saveTeam(Team team);
	void deleteTeam(Team team);
	Team getTeamById(long id);
	List<Team> getTeams(); 
}
