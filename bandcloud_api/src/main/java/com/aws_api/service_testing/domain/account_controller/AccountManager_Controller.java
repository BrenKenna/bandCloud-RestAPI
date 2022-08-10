package com.aws_api.service_testing.domain.account_controller;


// From bandCloud
import com.aws_api.model.accounts.manage.*;



// Java
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


// Supporting from Spring
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Rest controller to support the creation, deletion and authentication of user accounts
 * 
 * @author kenna
 */
@RestController
@RequestMapping("/account/manage")
public class AccountManager_Controller {

	
	// Work with list of users first
	private Users users;
	private final int sessionTime = (3 * 24 * 60 * 60);
	private final Dynamo_Users dynamoUsers;
	
	
	/**
	 * 
	 */
	public AccountManager_Controller() {
		this.users = Users.getInstance();
		this.dynamoUsers = new Dynamo_Users();
	}

	
	/**
	 * Post endpoint to register new users
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "register") // , consumes = "application/x-www-form-urlencoded"
	public ResponseEntity<?> registerUser(@RequestBody User user, HttpServletResponse response) {
		
		// Validate user input
		LoginValidationTypes validType = user.validateData(user.getUsername(), user.getEmail(), user.fetchPassword());
		if (!validType.isValid()) {
			return new ResponseEntity<>(validType, HttpStatus.OK);
		}
		
		// Check if username is already takes
		if ( dynamoUsers.hasUsername(user) ) {
			return new ResponseEntity<>(LoginValidationTypes.USER_EXISTS, HttpStatus.OK);
		}

		// Check account type: Provided and one of: Silver, Gold or Platinum
		if (user.nullAccountType()) {
			return new ResponseEntity<>(LoginValidationTypes.ACCOUNT_TYPE, HttpStatus.OK);
		}
		user.setAccountType();
		if (user.getAccountType() == null) {
			System.out.println("Account Type");
			return new ResponseEntity<>(user, HttpStatus.OK);
		}

		// Otherwise add user and set response cookie
		user.setUserID();
		user.configValidUser();
		Map<String, String> sessionMap = user.fetchSession();
		for (String key : sessionMap.keySet()) {
			Cookie respCookie = new Cookie(key, sessionMap.get(key));
			respCookie.setMaxAge(sessionTime);
			respCookie.setPath("/");
			respCookie.setHttpOnly(true);
			response.addCookie(respCookie);
		}
		
		// Put user
		dynamoUsers.putNewUser(user);
		
		// Return OK
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	
	/**
	 * Post endpoint to support authentication
	 * 	=> Should update session
	 * 
	 * @param user
	 * @param response
	 * @return
	 */
	@ResponseBody
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
		
		// Get queried user and validate that way
		User userData = dynamoUsers.getUser_ByUsername(user.getUsername()); 
		if (userData == null) {
			return new ResponseEntity<>(LoginValidationTypes.USER_NOT_EXISTS, HttpStatus.NOT_FOUND);
		}
		
		// Validate login
		if ( !userData.login(user.getUsername(), user.getEmail(), user.fetchPassword()) ) {
			return new ResponseEntity<>(LoginValidationTypes.CREDENTIALS, HttpStatus.UNAUTHORIZED);
		}
		
		// Set new cookie
		Map<String, String> sessionMap = userData.fetchSession();
		dynamoUsers.updateSession(userData);
		for (String key : sessionMap.keySet()) {
			Cookie respCookie = new Cookie(key, sessionMap.get(key));
			respCookie.setMaxAge(sessionTime);
			respCookie.setPath("/");
			respCookie.setHttpOnly(true);
			response.addCookie(respCookie);
		}
		
		
		// Return an accepted login
		return new ResponseEntity<>(LoginValidationTypes.VALID, HttpStatus.ACCEPTED);
	}
	
	
	/**
	 * Post endpoint to support deletion of user account
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping("deregister")
	public ResponseEntity<?> deregisterAccount(@RequestBody User user, HttpServletResponse response) {
		
		// Get queried user and validate that way
		User userData = dynamoUsers.getUser_ByUsername(user.getUsername());
		if (userData == null) {
			return new ResponseEntity<>(LoginValidationTypes.USER_NOT_EXISTS, HttpStatus.NOT_FOUND);
		}
		
		
		// Validate login
		if ( !userData.login(user.getUsername(), user.getEmail(), user.fetchPassword()) ) {
			return new ResponseEntity<>(LoginValidationTypes.CREDENTIALS, HttpStatus.UNAUTHORIZED);
		}
		
		
		// Allow deletion
		dynamoUsers.dropItem(userData.getUserID());
		return new ResponseEntity<>(LoginValidationTypes.VALID, HttpStatus.ACCEPTED);
	}
	
	
	/**
	 * Validate cookie data
	 * 
	 * 
	 * @param cookieUid
	 * @param cookieUname
	 * @param cookieToken
	 * @param cookieDone
	 * @return
	 */
	@GetMapping("validate-cookie")
	public ResponseEntity<?> validateCookie(
		@CookieValue(name = "userID", defaultValue = "") String cookieUid,
	 	@CookieValue(name = "authToken", defaultValue = "") String cookieToken,
	 	@CookieValue(name = "expireDate", defaultValue = "") String cookieDone
	)
	{
		// Respond with error if no cookie
		if ( !dynamoUsers.validateCookie(cookieUid, cookieToken, cookieDone) ) {
			return new ResponseEntity<>(LoginValidationTypes.CREDENTIALS, HttpStatus.UNAUTHORIZED);
		}

		// Otherwise ok
		else {
			return new ResponseEntity<>(LoginValidationTypes.VALID, HttpStatus.ACCEPTED);
		}
	}
}
