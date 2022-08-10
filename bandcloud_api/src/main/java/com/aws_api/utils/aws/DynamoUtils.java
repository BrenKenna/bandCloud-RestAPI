package com.aws_api.utils.aws;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.aws_api.utils.site.DateUtility;



/**
 * 
 * @author kenna
 *
 */
public class DynamoUtils {

	// Attributes
	private static DynamoUtils instance = null;
	private final String table_name = "BandCloud_User";
	private final AWSCredentials credentials;
	private final AmazonDynamoDB dynamoClient;
	// private final DateUtility dateUtils = new DateUtility();

	
	/**
	 * 
	 */
	private DynamoUtils() {
		this.credentials = new BasicAWSCredentials("", "");
		this.dynamoClient = AmazonDynamoDBClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.EU_WEST_1).build();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static DynamoUtils getInstance() {
		if (instance == null) {
			instance = new DynamoUtils();
		}
		return instance;
	}
	
	
	
	/**
	 * 
	 * @param username
	 * @param email
	 */
	public void importItem(String username, String email) {
		
		// Create an item to import
		Map<String, AttributeValue> item_values = new HashMap<>();
		item_values.put("Username", new AttributeValue(username));
		item_values.put("Email", new AttributeValue(email));
		
		// Put item
		dynamoClient.putItem(table_name, item_values);
	}
	
	
	/**
	 * 
	 * 
	 * Consider .withProjectionExpression() later
	 * 	=> String for one or more attributes to get
	 * 
	 */
	public void getItem(String username, String email) {
		
		// Send query request
		HashMap<String,AttributeValue> key_to_get = new HashMap<String,AttributeValue>();
		key_to_get.put("Username", new AttributeValue(username));
		GetItemRequest request = new GetItemRequest().withKey(key_to_get).withTableName(table_name);
		
		
		// Get results
		Map<String,AttributeValue> returned_item = dynamoClient.getItem(request).getItem();
		if ( returned_item == null ) {
			System.out.println("No data found for username = " + username);
		}
		else {
			System.out.println("\nFetching data for user");
			for(String key : returned_item.keySet() ) {
				System.out.println( returned_item.get(key).getS() );
			}
		}
	}
}
