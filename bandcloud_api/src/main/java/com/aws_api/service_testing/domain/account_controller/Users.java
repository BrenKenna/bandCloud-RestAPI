package com.aws_api.service_testing.domain.account_controller;

import java.util.ArrayList;
import java.util.List;
import com.aws_api.model.accounts.manage.User;


/**
 * Singleton users list to facilitate development.
 * 	=> Allows holding off on DynamoDB things while account controller is being developed
 * 
 * @author kenna
 */
public class Users {

	// Attributes
	private static Users instance = null;
	private static List<User> users;
	
	
	/**
	 * Private construction of user list
	 */
	private Users() {
		users = new ArrayList<>();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static Users getInstance() {
		if (instance == null) {
			instance = new Users();
		}
		return instance;
	}
	
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public boolean addUser(User user) {
		
		// Return false if user already exists
		User userData = getByUsername(user.getUsername());
		if (userData != null) {
			return false;
		}
		
		// Otherwise add
		users.add(user);
		return true;
	}
	
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public int userIndex(User user) {
		return users.indexOf(user);
	}
	
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public boolean dropUser(User user) {
		
		// Drop user if present
		int userIndex = users.indexOf(user);
		if (userIndex < 0) {
			return false;
		}
		users.remove(userIndex);
		return true;
	}

	
	/**
	 * 
	 * @return
	 */
	public int nUsers() {
		return users.size();
	}
	
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public User getByUsername(String username) {
		
		// Initialize output & linear search
		User output = null;
		int counter = 0;
		boolean found = false;

		// Search while space & not found
		while (!found & counter < users.size()) {

			// Get current username and compare to query
			User current = users.get(counter);
			if (current.getUsername().equals(username)) {
				found = true;
				output = current;
			}
			counter++;
			System.out.println("Username Search Counter: " + counter);
		}

		// Return result
		return output;
	}
	
	
	/**
	 * 
	 * @param userID
	 * @return
	 */
	public User getByUserID(String userID) {
		
		// Initialize output & linear search
		User output = null;
		int counter = 0;
		boolean found = false;

		// Search while space & not found
		while (!found & counter < users.size()) {

			// Get current username and compare to query
			User current = users.get(counter);
			if (current.getUserID().equals(userID)) {
				found = true;
				output = current;
			}
			counter++;
			System.out.println("UserID Search Counter: " + counter);
		}

		// Return result
		return output;
	}
}
