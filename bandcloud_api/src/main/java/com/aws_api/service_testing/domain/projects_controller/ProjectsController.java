package com.aws_api.service_testing.domain.projects_controller;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Decoder;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.aws_api.model.projects.*;
import com.aws_api.service_testing.domain.account_controller.Users;
import com.aws_api.utils.aws.AWS_Utils;



/**
 * Controller class to support client requests to s3 objects
 * 
 * @author kenna
 */
@RestController
@RequestMapping("/projects")
public class ProjectsController {

	
	// Attributes
	private Users users;
	private AWS_Utils awsUtils;
	private AmazonS3 s3client;
	
	
	/**
	 * 
	 */
	public ProjectsController() {
		this.users = Users.getInstance();
		this.awsUtils = AWS_Utils.getInstance();
		this.s3client = awsUtils.getS3client();
	}
	
	
	// List all projects
	@GetMapping("listProjects")
	public ResponseEntity<?> list_Projects() {
		
		// Make request
		System.out.println("A list projects request has come in");
		String userID = "test";
		Projects userProjects = new Projects(userID);
		return new ResponseEntity<>(userProjects.fetch_Projects(), HttpStatus.OK);
	}
	
	
	
	// List a project
	@GetMapping("listProject")
	public ResponseEntity<?> listProject(@RequestParam String projectName) {
		
		// Fetch project
		System.out.println("A list project request has come in");
		String userID = "test";
		Projects userProjects = new Projects(userID);
		Project userProject = userProjects.fetchProject("data/" + userID + "/" + projectName);


		// Handle output
		if ( userProject == null ) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
		return new ResponseEntity<>(userProject, HttpStatus.OK);
	}
	
	
	// Post a file
	@PostMapping("post-recording")
	public ResponseEntity<?> postRecording(@RequestBody String baseSixFourData) {
		
		// Sanity check
		System.out.println(
				"\n\nMessage recieved:\nFirst Flag = " + baseSixFourData.split(",")[0]
				+ "\nSecond Flag (100 chars): \n" + baseSixFourData.split(",")[1].substring(0, 50)
		);
		Decoder msgDecoder = Base64.getDecoder();
		try {
			
			// Read byte array & build put request
			byte[] audioBuffer = msgDecoder.decode(baseSixFourData.split(",")[1]);
			InputStream audioStream = new ByteArrayInputStream(audioBuffer);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(baseSixFourData.split(",")[0].split(":")[1]);


			// Push object
			PutObjectResult result = s3client.putObject("bandcloud", "test/recording.ogg", audioStream, meta);
			return new ResponseEntity<>("{\"msg\": \"Recieved sample of size = " + audioBuffer.length + "\"}", HttpStatus.ACCEPTED);
		}
		catch (Exception ex) {
			System.out.println("Nope:\n" + ex.getMessage());
			return new ResponseEntity<>("{ \"msg\": \"Nopesies\" }", HttpStatus.ACCEPTED);
		}
	}
}
