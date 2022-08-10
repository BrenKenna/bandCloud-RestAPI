package com.aws_api.model.accounts.manage;

public enum AccountType {

	SILVER {
		
		@Override
		public boolean isType(AccountType type) {
			return type == SILVER;
		}

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("silver");
		}
		
		@Override
		public String toString() {
			return "Silver";
		}
	},
	
	GOLD {
		
		@Override
		public boolean isType(AccountType type) {
			return type == GOLD;
		}

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("gold");
		}
		
		@Override
		public String toString() {
			return "Gold";
		}
	},
	
	PLATINUM {
		
		@Override
		public boolean isType(AccountType type) {
			return type == PLATINUM;
		}

		@Override
		public boolean isType(String type) {
			return type.toLowerCase().equals("platinum");
		}
		
		@Override
		public String toString() {
			return "Platinum";
		}
	};
	
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public abstract boolean isType(AccountType type);
	
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public abstract boolean isType(String type);
	
	
	/**
	 * Method to convert string to an account type
	 * 
	 * @param type
	 * @return
	 */
	public static AccountType getType(String query) {
		
		// Return account type if match
		for(AccountType type : AccountType.values()) {
			if ( type.isType(query) ) {
				return type;
			}
		}
		
		// Otherwise null
		return null;
	}
}
