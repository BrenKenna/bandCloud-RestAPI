package com.aws_api.utils.site;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Utility class to simplify date interactions away from main application
 * 
 * @author kenna
 */
public class DateUtility {
	
	// Standardize date format for the app
	private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private final int EXPIRATION_DAYS = 1; // Constant to set expiration date of tokens
	
	
	/**
	 * Get current date
	 * 
	 * @return Date - dd/MM/yyyy
	 */
	public Date getNow() {
		return new Date((new Date()).getTime());
	}
	
	
	/**
	 * Get numerical representation of current time
	 * 
	 * @return
	 */
	public long getTimeNow() {
		return new Date().getTime();
	}
	
	
	/**
	 * Return current date as string
	 * 
	 * @return String - dd/MM/yyyy
	 */
	public String getNowString() {
		return dateFormat.format(getNow());
	}

	
	/**
	 * Format input date to string
	 * 
	 * @param date
	 * @return String - dd/MM/yyyy
	 */
	public String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	
	/**
	 * Parse input date string
	 * 
	 * @param date - dd/MM/yyyy
	 * @return Date
	 */
	public Date parseDate(String date) {
		
		// Try format input date string
		Date output = null;
		try {
			output = dateFormat.parse(date);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Return result
		// Null handled externally
		return output;
	}
	
	
	/**
	 * Get default expiration date for input date
	 * 
	 * @param queryDate
	 * @return Date
	 */
	public Date getExpireDate(Date inputDate) {
		
		// Set calendar to input
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate); 
        
        // Add the expiration time
        calendar.add(Calendar.DAY_OF_YEAR, EXPIRATION_DAYS);
        return calendar.getTime();
	}
	
	
	/**
	 * Get expiration date from until until time
	 * 
	 * @param inputDate
	 * @param expire
	 * @return
	 */
	public Date getExpireDate(Date inputDate, int expire) {
		
		// Set calendar to input
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate); 
        
        // Add the expiration time
        calendar.add(Calendar.DAY_OF_YEAR, expire);
        return calendar.getTime();
	}
	
	
	/**
	 * Return an expirary date from input string
	 * 
	 * @param inputDate
	 * @return String - dd/MM/yyyy
	 */
	public String getExpireString(String inputDate, int expire) {
		Date date = parseDate(inputDate);
		Date expireDate = getExpireDate(date, expire);
		return formatDate(expireDate);
	}
	
	
	/**
	 * Return an expirary date from input string
	 * 
	 * @param inputDate
	 * @return String - dd/MM/yyyy
	 */
	public String getExpireString(String inputDate) {
		Date date = parseDate(inputDate);
		Date expireDate = getExpireDate(date);
		return formatDate(expireDate);
	}
	
	/**
	 * Return whether input date is before now
	 * 
	 * @param inputDate
	 * @return boolean
	 */
	public boolean hasExceeded(Date inputDate) {
		return getNow().getTime() > inputDate.getTime();
	}
	
	
	/**
	 * Validate whether queried date has expired
	 * 
	 * @param queryDate
	 * @return boolean
	 */
	public boolean isExpired(String queryDate) {
		Date inputDate = parseDate(queryDate);
		if (inputDate != null) {
			return hasExceeded(inputDate);
			
		}
		else {
			return false; // Should really be an exception
		}
	}
}
