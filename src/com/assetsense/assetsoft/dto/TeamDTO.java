package com.assetsense.assetsoft.dto;

import java.io.Serializable;

public class TeamDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long teamId;
	private String name;

	public TeamDTO() {

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
		return "TeamDTO [teamId=" + teamId + ", name=" + name + "]";
	}

}
