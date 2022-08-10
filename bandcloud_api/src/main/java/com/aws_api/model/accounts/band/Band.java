package com.aws_api.model.accounts.band;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aws_api.model.accounts.display.UserDisplay;
import com.aws_api.model.accounts.manage.User;


/**
 * Class to support creation of band
 * 
 * @author kenna
 */
public class Band {

	
	// Attributes
	private String bandName;
	private Set<UserDisplay> members;
	private List<String> projects;
	private UserDisplay bandAdmin;
	
	
	/**
	 * Construct an empty band
	 */
	public Band() {
		this.members = new HashSet<>();
		this.projects = new ArrayList<>();
	}
	
	
	/**
	 * Construct band for a user given a name
	 * 
	 * @param bandName
	 * @param user
	 */
	public Band(String bandName, User user) {
		this.bandName = bandName;
		this.members = new HashSet<>();
		this.projects = new ArrayList<>();
		this.bandAdmin = new UserDisplay(user);
	}

	
	
	/**
	 * 
	 * Getters & Setters Zone
	 * 
	 */
	
	
	/**
	 * 
	 * @return
	 */
	public String getBandName() {
		return bandName;
	}

	public void setBandName(String bandName) {
		this.bandName = bandName;
	}

	public Set<UserDisplay> getMembers() {
		return members;
	}

	public void setMembers(Set<UserDisplay> members) {
		this.members = members;
	}

	public List<String> getProjects() {
		return projects;
	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
	}

	public UserDisplay getBandAdmin() {
		return bandAdmin;
	}

	public void setBandAdmin(UserDisplay bandAdmin) {
		this.bandAdmin = bandAdmin;
	}
	
	public void setBandAdmin(User user) {
		this.bandAdmin = new UserDisplay(user);
	}

	@Override
	public String toString() {
		return "Band [bandName=" + bandName + ", members=" + members + ", projects=" + projects + ", bandAdmin="
				+ bandAdmin + "]";
	}
}
