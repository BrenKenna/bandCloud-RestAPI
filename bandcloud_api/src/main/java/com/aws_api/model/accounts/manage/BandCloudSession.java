package com.aws_api.model.accounts.manage;


import java.util.HashMap;
import java.util.Map;

import com.aws_api.utils.site.DateUtility;
import com.aws_api.utils.site.LoginUtility;


/**
 * 
 * @author kenna
 */
public class BandCloudSession {

	// Attributes
	private String startDate, expireDate, authToken;
	private final DateUtility dateUtils = new DateUtility();
	private final LoginUtility loginUtils = new LoginUtility();

	
	/**
	 * Create session for user 
	 */
	public BandCloudSession() {
		this.startDate = dateUtils.getNowString();
		this.expireDate = dateUtils.getExpireString(startDate);
		this.authToken = loginUtils.generateToken();
	}
	
	
	/**
	 * Create session from Dynamo record
	 * 
	 * @param sessionMap
	 */
	public BandCloudSession(Map<String, String> sessionMap) {
		this.startDate = sessionMap.get("issueDate");
		this.expireDate = sessionMap.get("expireDate");
		this.authToken = sessionMap.get("authToken");
	}
	
	
	/**
	 * Check if active session has expired
	 * 
	 * @return true/false
	 */
	public boolean sessionExpired() {
		
		// Check if session has expired
		return dateUtils.isExpired(expireDate);
	}
	
	
	/**
	 * Refresh a session
	 */
	public void refresh() {
		this.startDate = dateUtils.getNowString();
		this.expireDate = dateUtils.getExpireString(startDate);
		this.authToken = loginUtils.generateToken();
	}

	
	/**
	 * Check whether input token matches this
	 * 
	 * @param token
	 * @return
	 */
	public boolean queryToken(String token) {
		return authToken.equals(token);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getStartDate() {
		return startDate;
	}


	/**
	 * 
	 * @param startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	/**
	 * 
	 * @return
	 */
	public String getExpireDate() {
		return expireDate;
	}


	/**
	 * 
	 * @param expireDate
	 */
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}


	/**
	 * 
	 * @return
	 */
	public String getAuthToken() {
		return authToken;
	}


	/**
	 * 
	 * @param authToken
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> sessionAsMap(){
		Map<String, String> output = new HashMap<>();
		output.put("authToken", authToken);
		output.put("issueDate", startDate);
		output.put("expireDate", expireDate);
		return output;
	}
	
	
	/**
	 * Validate input token data against this session
	 * 
	 * @param inKey
	 * @param inEndDate
	 * @return true/false
	 */
	public boolean compareSession(String inKey, String inEndDate) {
		
		// Check null data
		if( inKey.equals("") | inEndDate.equals("") ) {
			System.out.println("Token data is null");
			return false;
		}
		
		// Check date
		if ( dateUtils.isExpired(inEndDate) ) {
			System.out.println("Token has expired");
			return false;
		}
		
		// Check authKey
		if ( !authToken.equals(inKey) ) {
			System.out.println("Token fails key");
			return false;
		}
		
		// Otherwise valid
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "BandCloudSession [startDate=" + startDate + ", expireDate=" + expireDate + ", authToken=" + authToken
				+ "]";
	}
}
