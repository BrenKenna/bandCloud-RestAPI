package com.aws_api.utils.aws;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;
import com.aws_api.model.projects.bandcloud_tags.TokenTag;
import com.aws_api.utils.site.DateUtility;


/**
 * Class to support smaller aws stuff
 * 
 * For uploading a file to S3
 * 	https://medium.com/oril/uploading-files-to-aws-s3-bucket-using-spring-boot-483fcb6f8646
 * 
 * @author kenna
 */
public class AWS_Utils {

	// Attributes
	private static AWS_Utils instance = null;
	private final AWSCredentials credentials;
	private final AmazonS3 s3client;
	private final AmazonEC2 ec2client;
	private final DateUtility dateUtility = new DateUtility();
	private final String sampleText, sampleAudio;
	
	
	/**
	 * 
	 */
	private AWS_Utils() {
		this.credentials = new BasicAWSCredentials("", "");
		this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.EU_WEST_1)
				  .build();
		this.ec2client = AmazonEC2ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.EU_WEST_1)
				  .build();
		this.sampleAudio = "test_data/site_audio_acoustic.mp3";
		this.sampleText = "test_data/todo.txt";
	}

	
	/**
	 * 
	 * @return
	 */
	public static AWS_Utils getInstance() {
		if(instance == null) {
			instance = new AWS_Utils();
		}
		return instance;
	}
	
	
	/**
	 * 
	 * @param bucket
	 * @return
	 */
	public List<S3ObjectSummary> listObjects(String bucket) {
		ObjectListing data = s3client.listObjects(bucket);
		return data.getObjectSummaries();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public AmazonS3 getS3client() {
		return s3client;
	}
	
	
	/**
	 * 
	 * 
	 * @param bucket
	 * @param path
	 * @param tagKey
	 * @param tagVal
	 */
	public void addTag(String bucket, String path, String tagKey, String tagVal) {
		
		// Create tag
		List<Tag> tags = new ArrayList<>();
		tags.add(new Tag(tagKey, tagVal));
		ObjectTagging tag = new ObjectTagging(tags);
		
		// Set object tags
		SetObjectTaggingRequest tagRequest = new SetObjectTaggingRequest(bucket, path, tag);
		s3client.setObjectTagging(tagRequest);
	}
	
	
	/**
	 * Get object tags
	 * 
	 * @param bucket
	 * @param path
	 * @return
	 */
	public Map<String, String> getTags(String bucket, String path) {
		
		
		return null;
	}
	
	
	/**
	 * 
	 * @param bucket
	 * @param path
	 * @return
	 */
	public TokenTag getSignedURL(String bucket, String path) {
		Date expire = dateUtility.getExpireDate(dateUtility.getNow());
		URL url = s3client.generatePresignedUrl(bucket, bucket, expire);
		return new TokenTag(url, expire);
	}
	
	
	/**
	 * 
	 * @param bucket
	 * @param path
	 * @param file
	 */
	public Map<String, PutObjectResult> putFile(String bucket) {
		
		// Load file from resources
		Map<String, PutObjectResult> output = new HashMap<>();
		File audioFile, textFile;
		audioFile = new File( getClass().getClassLoader().getResource(sampleAudio).getFile() );
		textFile = new File( getClass().getClassLoader().getResource(sampleText).getFile() );
		
		
		// Return null if files cannot be loaded
		if (!audioFile.exists() | !textFile.exists()) {
			return null;
		}
		
		
		// Push data
		String path = "test/site_acoustic_guitar.mp3";
		output.put("audio", s3client.putObject(bucket, path, audioFile));
		path = "test/todo.txt";
		output.put("text", s3client.putObject(bucket, path, textFile));
		return output;
	}
	
	
	/**
	 * 
	 * @param bucket
	 * @param bucketKey
	 * @return
	 */
	public List<S3ObjectSummary> listObjects(String bucket, String bucketKey) {
		ObjectListing data = s3client.listObjects(bucket, bucketKey);
		return data.getObjectSummaries();
	}
	
	
	
	/**
	 * 
	 * @return
	
	public List<InfoVMs> listVMs() {
		List<InfoVMs> output = new ArrayList<>();
		boolean allData = false;
		DescribeInstancesResult data = ec2client.describeInstances();
		do {
			// Add results
			for (Reservation reservation : data.getReservations() ) {
				for(Instance instance : reservation.getInstances()) {
					output.add(new InfoVMs(instance));
				}
			}
			
			// Handle next page
			if (data.getNextToken() == null) {
				allData = true;
			}
			else {
				data.setNextToken(data.getNextToken());
			}
			
		} while(!allData);
		
		return output;
	}
	**/
}
