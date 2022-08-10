package com.aws_api.model.projects;

import com.aws_api.model.projects.bandcloud_tags.FileTag;
import com.aws_api.model.projects.bandcloud_tags.TokenTag;


/**
 * 
 * @author kenna
 */
public class BandCloudFile_MetaData {

	// Attributes
	private String path;
	private long fileSize;
	private FileTag fileTag;
	private TokenTag tokenTag;
	
	
	/**
	 * 
	 */
	public BandCloudFile_MetaData() {}
	
	
	/**
	 * 
	 * @param path
	 * @param fileTag
	 * @param tokenTag
	 */
	public BandCloudFile_MetaData(String path, long fileSize, FileTag fileTag, TokenTag tokenTag) {
		this.path = path;
		this.fileSize = fileSize;
		this.fileTag = fileTag;
		this.tokenTag = tokenTag;
	}

	
	

	/**
	 * Getters & Setters Zone
	*/
	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public FileTag getFileTag() {
		return fileTag;
	}


	public void setFileTag(FileTag fileTag) {
		this.fileTag = fileTag;
	}


	public TokenTag getTokenTag() {
		return tokenTag;
	}


	public void setTokenTag(TokenTag tokenTag) {
		this.tokenTag = tokenTag;
	}


	public long getFileSize() {
		return fileSize;
	}


	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}


	@Override
	public String toString() {
		return "BandCloudFile_MetaData [path=" + path + ", fileSize=" + fileSize + ", fileTag=" + fileTag
				+ ", tokenTag=" + tokenTag + "]";
	}
}
