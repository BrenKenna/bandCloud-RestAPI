package com.aws_api.model.accounts.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aws_api.model.accounts.band.MemberType;
import com.aws_api.model.accounts.manage.AccountType;
import com.aws_api.model.accounts.manage.User;


/**
 * Class to represent a user object to frontend app without having to send salts etc
 * 
 * @author kenna
 */
public class UserDisplay {

	private String userID, username, email;
	private AccountType accountType;
	private List<String> bands;
	private Map<String, MemberType> bandRoles;
	
	
	public UserDisplay() {}
	
	
	/**
	 * 
	 * 
	 * @param userID
	 * @param username
	 * @param email
	 * @param accountType
	 */
	public UserDisplay(String userID, String username, String email, AccountType accountType) {
		this.userID = userID;
		this.username = username;
		this.email = email;
		this.accountType = accountType;
		this.bands = new ArrayList<>();
		this.bandRoles = new HashMap<>();
	}

	
	/**
	 * 
	 * 
	 * @param user
	 */
	public UserDisplay(User user) {
		this.userID = user.getUserID();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.accountType = user.getAccountType();
		this.bands = new ArrayList<>();
		this.bandRoles = new HashMap<>();
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
	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public AccountType getAccountType() {
		return accountType;
	}


	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}


	public List<String> getBands() {
		return bands;
	}


	public void setBands(List<String> bands) {
		this.bands = bands;
	}


	public Map<String, MemberType> getBandRoles() {
		return bandRoles;
	}


	public void setBandRoles(Map<String, MemberType> bandRoles) {
		this.bandRoles = bandRoles;
	}

	@Override
	public String toString() {
		return "UserDisplay [userID=" + userID + ", username=" + username + ", email=" + email + ", accountType="
				+ accountType + ", bands=" + bands + ", bandRoles=" + bandRoles + "]";
	}
}
