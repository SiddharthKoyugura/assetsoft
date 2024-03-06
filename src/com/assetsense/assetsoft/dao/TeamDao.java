package com.assetsense.assetsoft.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;
import com.assetsense.assetsoft.domain.Team;

public class TeamDao {
	HibernateTemplate template;

	public HibernateTemplate getTemplate() {
		return template;
	}

	public void setTemplate(HibernateTemplate template) {
		this.template = template;
	}

	// method to add team
	public void saveTeam(Team team) {
		template.save(team);
	}

	// method to update team
	public void updateTeam(Team team) {
		template.save(team);
	}

	// method to delete team
	public void deleteTeam(Team team) {
		template.delete(team);
	}

	// method to return one team of given id
	public Team getTeamById(long id) {
		Team team = (Team) template.get(Team.class, id);
		return team;
	}

	// method to return all teams
	public List<Team> getTasks() {
		List<Team> teams = new ArrayList<Team>();
		teams = template.loadAll(Team.class);
		return teams;
	}
}
