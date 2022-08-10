package com.aws_api.model.accounts.manage;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.aws_api.utils.site.LoginUtility;


/**
 * 
 * @author kenna
 */
public class User {

	// Attributes
	private String userID, username, salt, password, email, accountHolder;
	private AccountType accountType;
	private BandCloudSession session;
	private int logins = 0;
	
	
	// Utilities to help manage sessions
	private final LoginUtility loginUtils = new LoginUtility();
	
	
	/**
	 * 
	 */
	public User() {}
	
	
	
	/**
	 * 
	 * 
	 * @param username
	 * @param salt
	 * @param password
	 * @param email
	 * @param accountType
	 */
	public User(String username, String email, String password, String salt, AccountType accountType) {
		this.userID = loginUtils.generateUserID();
		this.username = username;
		this.email = email;
		this.password = password;
		this.salt = salt;
		this.accountType = accountType;
	}

	
	/**
	 * 
	 * @param userData
	 */
	public User(Map<String, AttributeValue> userData) {
		
		// Main account info
		this.userID = userData.get("userID").getS();
		this.username = userData.get("Username").getS();
		this.email = userData.get("Email").getS();
		this.setAccountHolder(userData.get("AccountType").getS());
		
		// Credentials
		this.password = userData.get("Credentials").getM().get("Password").getS();
		this.salt = userData.get("Credentials").getM().get("Salt").getS();
		
		// Session
		Map<String, String> sessionMap = new HashMap<>();
		sessionMap.put("authToken", userData.get("Session").getM().get("authToken").getS());
		sessionMap.put("issueDate", userData.get("Session").getM().get("issueDate").getS());
		sessionMap.put("expireDate", userData.get("Session").getM().get("expireDate").getS());
		this.session = new BandCloudSession(sessionMap);
	}
	
	/**
	 * 
	 * @param inputPass
	 * @return
	 */
	private boolean checkPassword(String inputPass) {
		String hashedInp = loginUtils.getHash("SHA-256", inputPass, fetchSalt());
		return hashedInp.equals(fetchPassword());
	}
	
	
	/**
	 * 
	 * @param inputName
	 * @param inputEmail
	 * @param inputPass
	 * @return
	 */
	public boolean login(String inputName, String inputEmail, String inputPass) {
		
		// Return null on failed login attempt
		if ( !inputName.equals(getUsername()) | !inputEmail.equals(getEmail()) | !checkPassword(inputPass) ) {
			System.out.println("Usernames: " + inputName + ", " + getUsername() + "\nEmails:" + inputEmail + ", " + getEmail()); 
			logins++;
			return false;
		}
		
		// Return session
		logins = 0;
		setSession();
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @return
	 */
	public LoginValidationTypes validateData(String username, String email, String password) {
		
		// Empty
		if (username.isBlank() | email.isBlank() | password.isBlank()) {
			return LoginValidationTypes.EMPTY;
		}
		
		// Lengths
		if ( username.length() < 6 | password.length() < 8 ) {
			return LoginValidationTypes.LENGTHS;
		}
		
		// Spaces
		if (username.contains(" ") | email.contains(" ")) {
			return LoginValidationTypes.SPACES;
		}
		
		// Email format
		if ( !email.matches(".*@[A-Za-z]+.[a-zA-z]+") ) {
			return LoginValidationTypes.EMAIL;
		}
		
		// Password capital letter, numbers & special characters
		if ( !password.matches(".*[a-zA-Z0-9].*") | password.matches("^[a-zA-Z0-9]+$") ) {
			return LoginValidationTypes.PASSWORD;
		}
		
		// Otherwise good
		return LoginValidationTypes.VALID;
	}

	
	/**
	 * Configure user salt & commited password
	 */
	public void configValidUser() {
		
		// Initialize output
		String salt = loginUtils.generateSalt();
		String hashPass = loginUtils.getHash("SHA-256", fetchPassword(), salt);
				
		// Update created users properties as IDs are auto-incremented
		setSalt(salt);
		setPassword(hashPass);
		
		// Create session
		if (session == null) {
			session = new BandCloudSession();
		}
		else {
			session.refresh();
		}
	}
	
	
	/**
	 * 
	 * @param newPass
	 * @return
	 */
	public LoginValidationTypes updatePassword(String newPass) {
		
		// Validate password
		if (newPass.length() < 8) {
			return LoginValidationTypes.LENGTHS;
		}
		
		// Password capital letter, numbers & special characters
		if ( !newPass.matches(".*[a-zA-Z0-9].*") | newPass.matches("^[a-zA-Z0-9]+$") ) {
			return LoginValidationTypes.PASSWORD;
		}
		
		// Initialize output
		String salt = loginUtils.generateSalt();
		String hashPass = loginUtils.getHash("SHA-256", newPass, salt);
				
		// Update created users properties as IDs are auto-incremented
		setSalt(salt);
		setPassword(hashPass);
		
		// Create session
		if (session == null) {
			session = new BandCloudSession();
		}
		else {
			session.refresh();
		}
		return LoginValidationTypes.VALID;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> fetchSession(){
		
		// Create session if none
		if (session == null) {
			session = new BandCloudSession();
		}
		
		// Fetch session
		Map<String, String> output = session.sessionAsMap();
		output.put("userID", userID);
		return output;
	}
	
	
	/**
	 * 
	 */
	public void setUserID() {
		if (this.userID == null) {
			this.userID = loginUtils.generateUserID();
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getUserID() {
		return userID;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * 
	 * @return
	 */
	public String fetchSalt() {
		return salt;
	}
	public String getSalt() {
		return salt;
	}

	/**
	 * 
	 * @param salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}


	/**
	 * 
	 * @return
	 */
	public String fetchPassword() {
		return password;
	}
	public String getPassword() {
		return password;
	}


	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * 
	 * @return
	 */
	public boolean nullAccountType() {
		return accountHolder == null;
	}
	
	/**
	 * 
	 * @param email
	 */
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private String getAccountHolder() {
		return accountHolder;
	}


	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return
	 */
	public AccountType getAccountType() {
		return accountType;
	}


	/**
	 * 
	 * @param accountType
	 */
	public void setAccountType() {
		this.accountType = AccountType.getType(accountHolder);
	}


	/**
	 * 
	 * @return
	 */
	public BandCloudSession getSession() {
		return session;
	}


	/**
	 * 
	 * @param session
	 */
	public void setSession() {
		this.session = new BandCloudSession();
	}

	
	/**
	 * Return credentials map
	 * 
	 * @return
	 */
	public Map<String, String> fetchCredentials() {
		Map<String, String> creds = new HashMap<>();
		creds.put("Password", this.password);
		creds.put("Salt", this.salt);
		return creds;
	}
	
	
	/**
	 * Set user credentials from credMap
	 * 
	 * @param credMap
	 * @return
	 */
	public boolean parseCredsMap(Map<String, String> credMap) {
		if ( !credMap.containsKey("Password") | !credMap.containsKey("Salt")) {
			return false;
		}
		
		// Update
		this.password = credMap.get("Password");
		this.salt = credMap.get("Salt");
		return true;
	}

	
	/**
	 * Set session from map, cookie authentication
	 * 
	 * @param sessionMap
	 */
	public void setSessionFromMap(Map<String, String> sessionMap) {
		BandCloudSession session = new BandCloudSession(sessionMap);
		this.session = session;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "User [userID=" + userID + ", username=" + username + ", salt=" + salt + ", password=" + password
				+ ", email=" + email + ", accountHolder=" + accountHolder + ", accountType=" + accountType + 
				", session=" + session + "]";
	}
}
