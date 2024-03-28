package com.assetsense.assetsoft.dao;

import java.util.List;

import com.assetsense.assetsoft.domain.Team;

public interface TeamDao {
	Team saveTeam(Team team);

	void deleteTeam(Team team);

	Team getTeamById(long id);

	List<Team> getTeams();
}
