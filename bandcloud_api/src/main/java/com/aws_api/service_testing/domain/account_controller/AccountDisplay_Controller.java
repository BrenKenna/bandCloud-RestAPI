package com.aws_api.service_testing.domain.account_controller;


import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aws_api.model.accounts.display.UserDisplay;
import com.aws_api.model.accounts.display.UserDisplayUpdate;
import com.aws_api.model.accounts.manage.LoginValidationTypes;
import com.aws_api.model.accounts.manage.User;
import com.aws_api.utils.site.CustomSet;


/**
 * Class to supporting retrieving & editing user account data
 * 
 * @author kenna
 */
@RestController
@RequestMapping("/account/display")
public class AccountDisplay_Controller {
	
	// Work with list of users first
	private final Dynamo_Users dynamoUsers;
	private Users users;
	private final int sessionTime = (3 * 24 * 60 * 60);


	/**
	 * 
	 */
	public AccountDisplay_Controller() {
		this.dynamoUsers = new Dynamo_Users();
		this.users = Users.getInstance();
	}

	
	/**
	 * Get endpoint to return a users account data
	 * 
	 * @param cookieUid
	 * @param cookieToken
	 * @param cookieDone
	 * @param response
	 * @return
	 */
	@ResponseBody
	@GetMapping("view")
	public ResponseEntity<?> getView(
			 @CookieValue(name = "userID", defaultValue = "") String cookieUid,
			 @CookieValue(name = "authToken", defaultValue = "") String cookieToken,
			 @CookieValue(name = "expireDate", defaultValue = "") String cookieDone
	)
	{
		
		// Respond with error if no cookie
		if (!dynamoUsers.validateCookie(cookieUid, cookieToken, cookieDone)) {
			return new ResponseEntity<>(LoginValidationTypes.CREDENTIALS, HttpStatus.UNAUTHORIZED);
		}
		
		// Create user display
		User user = dynamoUsers.getUser_ByID(cookieUid);
		UserDisplay output = new UserDisplay(user);
		user.setAccountType();
		output.setAccountType(user.getAccountType());
		return new ResponseEntity<>(output, HttpStatus.OK);
	}
	
	
	/**
	 * 
	 * @param updateName
	 * @param updateEmail
	 * @param updateAuth
	 * @param userDisplay
	 * @param cookieUid
	 * @param cookieUname
	 * @param cookieToken
	 * @param cookieDone
	 * @param response
	 * @return
	 */
	@ResponseBody
	@PostMapping("update")
	public ResponseEntity<?> updateData(
			 @RequestParam boolean updateName,
			 @RequestParam boolean updateEmail,
			 @RequestParam boolean updateAuth,
			 @RequestParam boolean updateAccount,
			 @RequestBody UserDisplayUpdate userDisplay,
			 @CookieValue(name = "userID", defaultValue = "") String cookieUid,
			 @CookieValue(name = "authToken", defaultValue = "") String cookieToken,
			 @CookieValue(name = "expireDate", defaultValue = "") String cookieDone,
			 HttpServletResponse response
	)
	{
		// Respond with error if no cookie
		if (!dynamoUsers.validateCookie(cookieUid, cookieToken, cookieDone)) {
			return new ResponseEntity<>(LoginValidationTypes.CREDENTIALS, HttpStatus.UNAUTHORIZED);
		}

		// Create user display
		User user = dynamoUsers.getUser_ByID(cookieUid);
		System.out.println("\n\nUser data:\n" + user.toString() + "\n\n");
		
		// Apply new username
		CustomSet<LoginValidationTypes> output = new CustomSet<>();
		if(updateName) {
			user.setUsername(userDisplay.getUsername());
		}
		
		// Apply new email
		if(updateEmail) {
			user.setEmail(userDisplay.getEmail()); // Have the set return validation type
		}
		
		// Apply new password
		if(updateAuth) {
			output.addItem(user.updatePassword(userDisplay.getPassword()));
		}
		
		// Apply new account holder
		if (updateAccount) {
			user.setAccountHolder(userDisplay.getAccountHolder());
			user.setAccountType();
		}

		// Check set
		if ( output.getSize() > 1 | !output.hasItem(LoginValidationTypes.VALID) ) {
			return new ResponseEntity<>(output, HttpStatus.METHOD_NOT_ALLOWED);
		}
		
		// Create new session & update dynamo
		Map<String, String> sessionMap = user.fetchSession();
		dynamoUsers.updateUserData(user);
		
		// Set cookie data
		for (String key : sessionMap.keySet()) {
			Cookie respCookie = new Cookie(key, sessionMap.get(key));
			respCookie.setMaxAge(sessionTime);
			respCookie.setPath("/");
			respCookie.setHttpOnly(true);
			response.addCookie(respCookie);
		}
		return new ResponseEntity<>(LoginValidationTypes.VALID, HttpStatus.OK);
	}
}
