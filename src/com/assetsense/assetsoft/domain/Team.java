package com.assetsense.assetsoft.domain;

import java.io.Serializable;

public class Team implements Serializable {
	private static final long serialVersionUID = 1L;
	private long teamId;
	private String name;
//	private List<User> users;

	public Team() {
	}

	public Team(String name) {
		super();
		this.name = name;
	}

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Team [teamId=" + teamId + ", name=" + name + "]";
	}
}
