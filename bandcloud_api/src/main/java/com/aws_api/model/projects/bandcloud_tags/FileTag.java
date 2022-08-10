package com.aws_api.model.projects.bandcloud_tags;


/**
 * 
 * @author kenna
 *
 */
public class FileTag {
	
	// Attributes
	private String owner, lastEditor;
	
	
	/**
	 * 
	 */
	public FileTag() {
	}
	
	
	/**
	 * 
	 * @param owner
	 */
	public FileTag(String owner, String lastEditor) {
		this.owner = owner;
		this.lastEditor = lastEditor;
	}


	/**
	 * 
	 * @return
	 */
	public String getLastEditor() {
		return lastEditor;
	}

	
	/**
	 * 
	 * @return
	 */
	public void setLastEditor(String lastEditor) {
		this.lastEditor = lastEditor;
	}


	/**
	 * 
	 * @return
	 */
	public String getOwner() {
		return owner;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String setOwner(String owner) {
		return this.owner;
	}
}
