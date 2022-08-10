package com.aws_api.service_testing.domain.account_controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.aws_api.model.accounts.manage.LoginValidationTypes;
import com.aws_api.model.accounts.manage.User;


/**
 * 
 * @author kenna
 */
public class Dynamo_Users {

	
	// Attributes
	private final String table_name = "Users";
	private final AmazonDynamoDB dynamoClient;
	// private final DynamoDB = dynamoDB;
	
	public Dynamo_Users() {
		BasicAWSCredentials credentials = new BasicAWSCredentials("", "");
		this.dynamoClient = AmazonDynamoDBClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.EU_WEST_1).build();
		// this.dynamoDB = new DynamoDB(this.dynamoClient);
	}
	
	
	/**
	 * 
	 * @param user
	 */
	public void putNewUser(User user) {
		
		// Configure put
		Map<String, AttributeValue> itemVals = new HashMap<>();
		itemVals.put("userID", new AttributeValue(user.getUserID()));
		itemVals.put("Username", new AttributeValue(user.getUsername()));
		itemVals.put("Email", new AttributeValue(user.getEmail()));
		itemVals.put("AccountType", new AttributeValue( user.getAccountType().toString() ));
		
		// Configure credentials
		AttributeValue userMap = new AttributeValue();
		for ( String key : user.fetchCredentials().keySet() ) {
			userMap.addMEntry(key, new AttributeValue(user.fetchCredentials().get(key)));
		} 
		itemVals.put("Credentials", userMap);
		
		
		// Configure session
		userMap = new AttributeValue();
		for ( String key : user.fetchSession().keySet() ) {
			if ( !key.equals("userID") ) {
				userMap.addMEntry(key, new AttributeValue(user.fetchSession().get(key)));
			}
		} 
		itemVals.put("Session", userMap);
		
		
		// Put user
		dynamoClient.putItem(table_name, itemVals);
	}
	
	
	/**
	 * Determine if username is already used
	 * 
	 * @param user
	 * @return
	 */
	public boolean hasUsername(User user) {
		QueryResult results = queryUsername(user.getUsername());
		return results.getCount() > 0;
	}
	
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public boolean hasUserID(User user) {
		return getUserID(user.getUserID()) == null;
	}
	
	
	/**
	 * 
	 * @param userID
	 * @return
	 */
	public User getUser_ByID(String userID) {
		
		// Initialize output and get user
		Map<String, AttributeValue> userData = getUserID(userID);
		if ( userData == null ) {
			return null;
		}
		
		// Configure user
		return new User(userData);
	}
	
	
	/**
	 * 
	 * @param userID
	 * @return
	 */
	public User getUser_ByUsername(String username) {
		
		// Initialize output and get user
		User output = null;
		QueryResult data = queryUsername(username);
		if ( data.getCount() == 0 ) {
			return output;
		}
		
		// Configure user
		Map<String, AttributeValue> userData = data.getItems().get(0);
		output = new User(userData);
		return output;
	}
	
	
	/**
	 * Get user by userID
	 * 	=> Need to a method to map these data to a user
	 * 
	 * @param userID
	 * @return
	 */
	public Map<String, AttributeValue> getUserID(String userID) {
		
		// Initialize output & get request
		HashMap<String,AttributeValue> getKey = new HashMap<String,AttributeValue>();
		getKey.put("userID", new AttributeValue(userID));
		GetItemRequest request = new GetItemRequest()
				.withTableName(table_name)
				.withKey(getKey);
		
		// Send request
		Map<String, AttributeValue> result = new HashMap<>();
		result = dynamoClient.getItem(request).getItem();
		
		// Return output
		return result;
	}
	
	
	/**
	 * Get user by username
	 * 
	 * @param username: band-tommy
	 * @return
	 */
	public QueryResult queryUsername(String username) {
		
		// Config query
		Map<String, String> attributeName = new HashMap<>();
		attributeName.put("#usernameCol", "Username");
		attributeName.put("#bcSession", "Session");
		Map<String, AttributeValue> attributeVal = new HashMap<>();
		attributeVal.put(":usernameVal", new AttributeValue(username));
		
		// Config request
		QueryRequest req = new QueryRequest()
				.withTableName(table_name)
				.withIndexName("UsernameIndex")
				.withExpressionAttributeNames(attributeName)
				.withExpressionAttributeValues(attributeVal)
				.withKeyConditionExpression("#usernameCol = :usernameVal")
				.withProjectionExpression("#usernameCol, userID, Email, AccountType, #bcSession, Credentials");


		// Send request
		QueryResult output = null;
		try {
			output = dynamoClient.query(req);
		}
		catch(Exception ex) {
			System.out.println("Error on query: " + ex);
		}
		return output;
	}
	
	
	/**
	 * 
	 * @param userID
	 * @return
	 */
	public void dropItem(String userID) {
		
		// Configure 
		Map<String, AttributeValue> delKey = new HashMap<>();
		delKey.put("userID", new AttributeValue(userID));
		
		// Delete and set flag
		dynamoClient.deleteItem(table_name, delKey);
	}
	
	
	/**
	 * Validate user data on Dynamo against cookie
	 * 
	 * @param userID
	 * @param authKey
	 * @param endDate
	 * @return true/false
	 */
	public boolean validateCookie(String userID, String authKey, String endDate) {
		
		// Respond with error if no cookie
		if (userID.equals("")) {
			System.out.println("Null userID");
			return false;
		}
		
		// Get user data
		User userData = getUser_ByID(userID);
		if (userData == null) {
			System.out.println("User not found");
			return false;
		}
		
		// Validate authenticity of token
		return userData.getSession().compareSession(authKey, endDate);
	}
	
	
	
	/**
	 * 
	 * @param userData
	 */
	public void updateSession(User userData) {
		
		// Fetch userID & session
		String userID = userData.getUserID();
		Map<String, String> newSession = userData.fetchSession();


		// Configure update request params
		Map<String, String> expressionAttributeNames = new HashMap<String, String>();
		expressionAttributeNames.put("#bcSession", "Session");
		HashMap<String, AttributeValue> priKey = new HashMap<String,AttributeValue>();
		priKey.put("userID", new AttributeValue(userID));


		// Configure new map
		Map<String, AttributeValue> newVals = new HashMap<>();
		AttributeValue userMap = new AttributeValue();
		for ( String key : newSession.keySet() ) {
			if ( !key.equals("userID") ) {
				userMap.addMEntry(key, new AttributeValue(newSession.get(key)));
			}
		} 
		newVals.put(":newSession", userMap);
		
		
		// Create update request
		UpdateItemRequest request = new UpdateItemRequest()
				.withTableName(table_name)
				.withKey(priKey)
				.withExpressionAttributeNames(expressionAttributeNames)
				.withExpressionAttributeValues(newVals)
				.withUpdateExpression("SET #bcSession = :newSession");
		
		
		// Post request
		try {
			dynamoClient.updateItem(request);
			//System.out.println("\n\nSomething happened:\n" + result.toString());
		}
		catch(Exception ex) {
			System.out.println("\n\nError updating user session: " + userID + "\n" + ex.getMessage());
		}
	}
	
	
	public void updateUserData(User userData) {
		
		// Fetch fields
		Map<String, String> session = userData.getSession().sessionAsMap();
		String salt = userData.getSalt();
		String password = userData.getPassword();
		
		// Configure primary key
		Map<String, AttributeValue> priKey = new HashMap<>();
		priKey.put("userID", new AttributeValue(userData.getUserID()));
		
		// Configure attribute name aliases
		Map<String, String> attNameAliases = new HashMap<>();
		attNameAliases.put("#bcUsername", "Username");
		attNameAliases.put("#bcEmail", "Email");
		attNameAliases.put("#bcAccountType", "AccountType");
		attNameAliases.put("#bcCredentials", "Credentials");
		attNameAliases.put("#bcSession", "Session");
		
		// Configure string value aliases
		Map<String, AttributeValue> newVals = new HashMap<>();
		newVals.put(":newName", new AttributeValue(userData.getUsername()));
		newVals.put(":newEmail", new AttributeValue(userData.getEmail()));
		newVals.put(":newAcctType", new AttributeValue(userData.getAccountType().toString()));

		// Configure new credentials map
		AttributeValue credsMap = new AttributeValue();
		credsMap.addMEntry("Password", new AttributeValue(userData.getPassword()));
		credsMap.addMEntry("Salt", new AttributeValue(userData.getSalt()));
		newVals.put(":newCreds", credsMap);
		
		// Configure new session map
		AttributeValue newSess = new AttributeValue();
		for ( String key : session.keySet() ) {
			if ( !key.equals("userID") ) {
				newSess.addMEntry(key, new AttributeValue(session.get(key)));
			}
		}
		newVals.put(":newSession", newSess);
		
		
		// Create update request
		UpdateItemRequest request = new UpdateItemRequest()
				.withTableName(table_name)
				.withKey(priKey)
				.withExpressionAttributeNames(attNameAliases)
				.withExpressionAttributeValues(newVals)
				.withUpdateExpression(
						"SET #bcUsername = :newName, "
						+ "#bcEmail = :newEmail, "
						+ "#bcAccountType = :newAcctType, "
						+ "#bcCredentials = :newCreds, "
						+ "#bcSession = :newSession"
				);
		
		// Post request
		try {
			UpdateItemResult result = dynamoClient.updateItem(request);
			System.out.println("\n\nSomething happened:\n" + result.toString());
		} catch (Exception ex) {
			System.out.println("\n\nError updating user session: " + userData.getUserID() + "\n" + ex.getMessage());
		}
	}
}
