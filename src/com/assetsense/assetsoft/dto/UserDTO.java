package com.assetsense.assetsoft.dto;

import java.io.Serializable;
import java.util.Set;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long userId;
	private String name;
	private String email;
	private String password;
	private Set<TeamDTO> teams;

	public UserDTO() {
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<TeamDTO> getTeams() {
		return teams;
	}

	public void setTeams(Set<TeamDTO> teams) {
		this.teams = teams;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", name=" + name + ", email=" + email + ", teams=" + teams + "]";
	}


}
