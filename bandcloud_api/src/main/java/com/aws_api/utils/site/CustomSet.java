package com.aws_api.utils.site;

/**
*
* @author kenna
* @param <DataType>
*/
public class CustomSet<DataType>{
   
   // Array of objects treated as set
   private Object[] items;
   private int next, size;
   private int SET_SIZE = 10;
   
   
   /**
    * Empty constructor allows set to be built
    */
   public CustomSet() {
       this.items = new Object[SET_SIZE];
       this.next = 0;
       this.size = 0;
   }
   
   
   /**
    * Rebuild a set
    * 
    * @param borrowing 
    */
   public CustomSet(int[] borrowing) {
       this.items = new Object[SET_SIZE];
       int counter = 0;
       for( Object i : borrowing) {
           this.items[counter] = i;
           this.next++;
           this.size++;
       }
   }
   
   /**
    * Build set from reading a string
    * 
    * @param itemString
    * @param delim 
    */
   public CustomSet(String itemString, String delim) {
       this.items = new Object[SET_SIZE];
       int counter = 0;
       for(String i : itemString.split(delim)) {
           this.items[counter] = Integer.parseInt(i);
           this.next++;
           this.size++;
       }
   }

   /**
    * Set books student is borrowing
    * 
    * @param borrowing 
    */
   public void setItems(Object[] items) {
       this.items = items;
   }
   
   
   /**
    * Get array of book IDs student is borrowing
    * 
    * @return String[Book ID] 
    */
   public Object[] getItems() {
       return this.items;
   }
   
   /**
    * Check if object exists in object set
    * 
    * @param item
    * @return 
    */
   public boolean hasItem(Object item) {
       for(Object i : items) {
           if ( item.equals(i) ) {
               return true;
           }
       }
       return false;
   }
   
   
   /**
    * Attempt to add item to set, otherwise return false
    * 
    * @param item
    * @return boolean
    */
   public boolean addItem(Object item) {
       
       // Skip if array is full or item already in set
       if ( !canAdd(item) ) {
           return false;
       }
       
       // Otherwise add
       items[ this.next ] = item;
       this.next++;
       this.size++;
       return true;
   }
   
   
   /**
    * Drop item from set
    * 
    * @param item
    * @return 
    */
   public boolean dropItem(Object item) {
       
       // Initalize counter and scan array
       int counter = 0;
       for(Object i : items) {
           if ( item.equals(i) ) {
               
               // Drop item
               items[counter] = null;
               this.next--;
               this.size--;
               
               
               // Return state
               resolvePositions();
               return true;
           }
       }
       
       // Return state
       return false;
   }
   
   
   /**
    * Method to check if item can be added to set
    * 
    * @param item
    * @return boolean
    */
   public boolean canAdd(Object item) {
       
       // Skip if array size is breached
       if (this.next >= items.length | hasItem(item)) {
           return false;
       }
       
       // Otherwise can add
       else {
           return true;
       }
   }
   
   /**
    * Reset complete list
    */
   public void clearItems() {
       this.items = new Object[SET_SIZE];
   }

   
   /**
    * Check whether space in queue
    * 
    * @return boolean
    */
   public boolean hasSpace() {
       return this.next >= items.length;
   }
   
   
   /**
    * Method to resolve null positions
    * 
    */
   private void resolvePositions() {
       
       // Initalize counter and new array
       int counter = 0;
       Object[] newBorrowing = new Object[SET_SIZE];
       
       // Copy non-null values into new array
       for ( Object i : items ) {
           if ( i != null ) {
               newBorrowing[counter] = i;
               this.next++;
               this.size++;
           }
       }
       
       // Update array
       this.items = newBorrowing;
   }
   
   
   /**
    * Return whether the set is empty
    * 
    * @return boolean
    */
   public boolean isEmpty() {
       return this.size < this.items.length;
   }
   
   
   /**
    * Return number of filled elements
    * 
    * @return
    */
   public int getSize() {
	   return this.size;
   }
   
   /**
    * Return borrowing as a string
    * @return 
    */
   @Override
   public String toString(){
       
       if (this.items != null) {
           String output = "\"";
           for (Object i : this.items ) {
               output += i + ";";
           }
           output += " \"";
           output = output.replace("; ", "").replace(" ", "");
           return output;
       }
       else {
           return "";
       }
   }
}