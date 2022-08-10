package com.aws_api.model.projects;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author kenna
 */
public class Project {

	// Attributes
	private String projectName;
	private long projectSize;
	private List<BandCloudFile_MetaData> rawAudio;
	private BandCloudFile_MetaData mixed;
	
	
	/**
	 * 
	 */
	public Project() {
		this.rawAudio = new ArrayList<>();
	}
	
	
	/**
	 * 
	 * @param projectName
	 * @param projectSize
	 * @param rawAudio
	 * @param mixed
	 */
	public Project(
			String projectName,
			long projectSize,
			List<BandCloudFile_MetaData> rawAudio,
			BandCloudFile_MetaData mixed
	) {
		this.projectName = projectName;
		this.projectSize = projectSize;
		this.rawAudio = rawAudio;
		this.mixed = mixed;
	}


	/**
	 * Update project size
	 */
	public void updateProjectSize() {
		
		// Add mixed size if present
		if ( this.mixed != null ) {
			this.projectSize += this.mixed.getFileSize();
		}
		
		// Add raw audio sizes if present
		if ( this.rawAudio != null) {
			if( this.rawAudio.size() >= 1 ) {
				for( BandCloudFile_MetaData rawFile : this.rawAudio ) {
					this.projectSize += rawFile.getFileSize();
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
	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public long getProjectSize() {
		return projectSize;
	}


	public void setProjectSize(int projectSize) {
		this.projectSize = projectSize;
	}


	public List<BandCloudFile_MetaData> getRawAudio() {
		return rawAudio;
	}


	public void setRawAudio(List<BandCloudFile_MetaData> rawAudio) {
		this.rawAudio = rawAudio;
	}


	public BandCloudFile_MetaData getMixed() {
		return mixed;
	}


	public void setMixed(BandCloudFile_MetaData mixed) {
		this.mixed = mixed;
	}


	@Override
	public String toString() {
		return "Project [projectName=" + projectName + ", projectSize=" + projectSize + ", rawAudio=" + rawAudio
				+ ", mixed=" + mixed + "]";
	}
}
