package com.aws_api.model.accounts.display;


/**
 * Class to support updating credentials
 * 
 * @author kenna
 */
public class UserDisplayUpdate {
	
	private String username, email, password, accountHolder;

	public UserDisplayUpdate() {}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
}
