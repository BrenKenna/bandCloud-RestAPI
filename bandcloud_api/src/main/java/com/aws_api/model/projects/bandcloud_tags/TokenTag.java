package com.aws_api.model.projects.bandcloud_tags;

import java.net.URL;
import java.util.Date;


/**
 * 
 * @author kenna
 *
 */
public class TokenTag {
	
	// Attributes
	private URL url;
	private Date expire;

	
	/**
	 * 
	 */
	public TokenTag() {
	}
	
	
	/**
	 * 
	 * @param url
	 * @param expire
	 */
	public TokenTag(URL url, Date expire) {
		this.url = url;
		this.expire = expire;
	}


	/**
	 * 
	 * @return
	 */
	public URL getUrl() {
		return url;
	}
	
	
	/**
	 * 
	 * @param url
	 */
	public void setUrl(URL url) {
		this.url = url;
	}


	/**
	 * 
	 * @return
	 */
	public Date getExpire() {
		return expire;
	}
	
	
	/**
	 * 
	 * @param expire
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}
}
