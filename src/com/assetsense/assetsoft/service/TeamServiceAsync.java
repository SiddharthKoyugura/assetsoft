package com.assetsense.assetsoft.service;

import java.util.List;

import com.assetsense.assetsoft.domain.Team;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TeamServiceAsync {
	void saveTeam(Team team, AsyncCallback<Team> callback);

	void deleteTeam(Team team, AsyncCallback<Void> callback);

	void getTeamById(long id, AsyncCallback<Team> callback);

	void getTeams(AsyncCallback<List<Team>> callback);
}
