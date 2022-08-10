package com.aws_api.model.projects;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;


import com.aws_api.model.projects.bandcloud_tags.*;
import com.aws_api.utils.aws.AWS_Utils;


/**
 * Class to support interactions with projects
 * 
 * @author kenna
 */
public class Projects {

	// Attributes
	private final AWS_Utils awsUtils;
	private final AmazonS3 s3client;
	private final List<Project> projects;
	private String bucket, userID;
	private long storageUsage = 0;
	
	
	/**
	 * Construct empty project collection
	 */
	public Projects() {
		this.awsUtils = AWS_Utils.getInstance();
		this.projects = new ArrayList<>();
		this.s3client = awsUtils.getS3client();
		this.bucket = "bandcloud";
	}
	
	
	/**
	 * 
	 * @param userID
	 */
	public Projects(String userID) {
		this.awsUtils = AWS_Utils.getInstance();
		this.projects = new ArrayList<>();
		this.s3client = awsUtils.getS3client();
		this.bucket = "bandcloud";
		this.userID = userID;
	}
	
	
	/**
	 * Return list of project names
	 * @return
	 */
	public List<String> fetchProjectNames() {
		
		// Initialize output & request
		List<String> output = new ArrayList<>();
		ListObjectsV2Request req = new ListObjectsV2Request()
				.withBucketName("bandcloud")
				.withDelimiter("/")
				.withPrefix("data/test/");
		
		// Return results
		ListObjectsV2Result queryResults = s3client.listObjectsV2(req);
		output = queryResults.getCommonPrefixes();
		return output;
	}
	
	
	/**
	 * List project names for a user
	 * 
	 * @param userID
	 * @return List-Project names
	 */
	public List<String> fetchProjectNames(String userID) {
		
		// Initialize output & request
		List<String> output = new ArrayList<>();
		ListObjectsV2Request req = new ListObjectsV2Request()
				.withBucketName("bandcloud")
				.withDelimiter("/")
				.withPrefix("data/" + userID + "/");
		
		// Return results
		ListObjectsV2Result queryResults = s3client.listObjectsV2(req);
		output = queryResults.getCommonPrefixes();
		return output;
	}
	
	
	/**
	 * Return a list of object summaries for a project
	 * 
	 * @param projectName
	 * @return
	 */
	public List<S3ObjectSummary> fetchProjectObjectSummaries(String projectName) {
		List<S3ObjectSummary> results = s3client.listObjects(bucket, projectName).getObjectSummaries();
		return results;
	}
	
	
	/**
	 * Parse the mixed audio data from object summaries
	 * 
	 * @param projectData
	 */
	public BandCloudFile_MetaData parseMixedAudio(List<S3ObjectSummary> projectData) {
		
		// Scan for the mixed audio
		S3ObjectSummary mixSummary = null;
		for(S3ObjectSummary data: projectData) {
			if( !data.getKey().contains("raw") ) {
				mixSummary = data;
			}
		}
		
		// Determine presence
		if(mixSummary == null) {
			return null;
		}
		
		// Handle mix
		String path = mixSummary.getKey();
		long fileSize = mixSummary.getSize();
		BandCloudFile_MetaData mixedTrack = new BandCloudFile_MetaData(path, fileSize, new FileTag(), new TokenTag());
		return mixedTrack;
	}
	
	
	/**
	 * Return metadata for a raw audio track
	 * 
	 * @param track
	 * @return
	 */
	public BandCloudFile_MetaData parse_RawAudioTrack(S3ObjectSummary track) {
		
		// Get data
		String path = track.getKey();
		long fileSize = track.getSize();
		FileTag fileTag = new FileTag();
		TokenTag tokenTag = new TokenTag();
		
		
		// Return raw audio metadata
		return new BandCloudFile_MetaData(path, fileSize, fileTag, tokenTag);
	}
	
	
	/**
	 * Parse the list of raw audio tracks from s3 object summary
	 * 
	 * @param projectData
	 * @return
	 */
	public List<BandCloudFile_MetaData> parseAll_RawAudio(List<S3ObjectSummary> projectData) {
		
		// Initialize output & parse project data
		List<BandCloudFile_MetaData> output = new ArrayList<>();
		for ( S3ObjectSummary track : projectData ) {
			if(track.getKey().contains("raw")) {
				output.add( parse_RawAudioTrack(track) );
			}
		}
		
		// Return results
		return output;
	}
	

	/**
	 * Return project data for a project
	 * 
	 * @param projectName
	 * @return
	 */
	public Project fetchProject(String projectName) {
		
		// Fetch project data
		List<S3ObjectSummary> projectData = fetchProjectObjectSummaries(projectName);
		List<BandCloudFile_MetaData> rawAudio = parseAll_RawAudio(projectData);
		BandCloudFile_MetaData mixedAudio = parseMixedAudio(projectData);
		
		// Return project
		Project output = new Project(projectName, -1, rawAudio, mixedAudio);
		output.updateProjectSize();
		return output;
	}
	
	
	/**
	 * Fetch projects
	 * 
	 * @return
	 */
	public List<Project> fetch_Projects() {
		
		// Initialize data and fetch project names
		List<Project> projects = new ArrayList<>();
		List<String> projectNames = fetchProjectNames();
		
		// Handle null
		if( projectNames.size() == 0 ) {
			return null;
		}
		
		// Create project summary objects
		for(String projectName : projectNames ) {
			Project current = fetchProject(projectName);
			projects.add(current);
		}
		
		// Return results
		return projects;
	}
	
	
	/**
	 * Fetch projects by userID
	 * 
	 * @param userID
	 * @return
	 */
	public List<Project> fetch_Projects(String userID) {
		
		// Initialize data and fetch project names
		List<Project> projects = new ArrayList<>();
		List<String> projectNames = fetchProjectNames(userID);
		
		// Handle null
		if( projectNames.size() == 0 ) {
			return null;
		}
		
		// Create project summary objects
		for(String projectName : projectNames ) {
			Project current = fetchProject(projectName);
			projects.add(current);
		}
		
		// Return results
		return projects;
	}
	
	
	/**
	 * Measure sizes of all projects
	 */
	public void updateProjectSize() {
		this.storageUsage = 0;
		if ( this.projects != null ) {
			if ( this.projects.size() >= 1 ) {
				for( Project project : this.projects ) {
					this.storageUsage+=project.getProjectSize();
				}
			}
		}
	}
	
	/**
	 * 
	 * Getters & Setters Zone
	 * 
	 */

	
	/**
	 * 
	 * @return
	 */
	public List<Project> getProjects() {
		return projects;
	}


	public String getBucket() {
		return bucket;
	}


	public String getUserID() {
		return userID;
	}

	public long getStorageUse() {
		return this.storageUsage;
	}
	
	public void setStorageUse(long storageUsage) {
		this.storageUsage = storageUsage;
	}

	@Override
	public String toString() {
		return "Projects [projects=" + projects + ", bucket=" + bucket + ", userID=" + userID + "]";
	}
}
