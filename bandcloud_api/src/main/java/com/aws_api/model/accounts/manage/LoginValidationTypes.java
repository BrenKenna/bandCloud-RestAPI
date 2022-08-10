package com.aws_api.model.accounts.manage;

public enum LoginValidationTypes {

	USERNAME {
		
		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("username");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == USERNAME;
		}

		@Override
		public boolean isValid() {
			return false;
		}

		@Override
		public String toString() {
			return "Username";
		}
	},
	
	EMAIL {
		
		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("email");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == EMAIL;
		}

		@Override
		public boolean isValid() {
			return false;
		}

		@Override
		public String toString() {
			return "Email";
		}
	},
	
	PASSWORD {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("password");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == PASSWORD;
		}

		@Override
		public boolean isValid() {
			return false;
		}

		@Override
		public String toString() {
			return "Password";
		}
	},
	
	EMPTY {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("empty");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == EMPTY;
		}

		@Override
		public boolean isValid() {
			return false;
		}

		@Override
		public String toString() {
			return "Empty";
		}
	},
	
	LENGTHS {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("lengths");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == LENGTHS;
		}

		@Override
		public boolean isValid() {
			return false;
		}

		@Override
		public String toString() {
			return "Lengths";
		}
	},
	
	SPACES {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("spaces");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == SPACES;
		}

		@Override
		public boolean isValid() {
			return false;
		}

		@Override
		public String toString() {
			return "Spaces";
		}
	},
	
	ACCOUNT_TYPE {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("account type");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == ACCOUNT_TYPE;
		}

		@Override
		public boolean isValid() {
			return false;
		}

		@Override
		public String toString() {
			return "Account Type";
		}
		
	}, 
	
	USER_EXISTS {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("user exists");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == USER_EXISTS;
		}

		@Override
		public boolean isValid() {
			return false;
		}
		
		@Override
		public String toString() {
			return "User exists";
		}
		
	}, 
	
	CREDENTIALS {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("credentials");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == CREDENTIALS;
		}

		@Override
		public boolean isValid() {
			return false;
		}
		
		@Override
		public String toString() {
			return "Credentials";
		}
		
	}, 
	
	USER_NOT_EXISTS {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("user does not exist");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == USER_NOT_EXISTS;
		}

		@Override
		public boolean isValid() {
			return false;
		}
		
		@Override
		public String toString() {
			return "User does not exist";
		}
		
	}, 
	
	NO_COOKIE {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("No cookie");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == NO_COOKIE;
		}

		@Override
		public boolean isValid() {
			return false;
		}
		
		@Override
		public String toString() {
			return "No Cookie";
		}
		
	},
	
	VALID {

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("valid");
		}

		@Override
		public boolean isType(LoginValidationTypes validType) {
			return validType == VALID;
		}

		@Override
		public boolean isValid() {
			return true;
		}

		@Override
		public String toString() {
			return "Valid";
		}

	};
	
	
	/**
	 * 
	 */
	public abstract boolean isType(String type);
	
	/**
	 * Check whether types match
	 */
	public abstract boolean isType(LoginValidationTypes validType);
	
	
	/**
	 * Method to return whether type is valid
	 */
	public abstract boolean isValid();
}
