/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 03/09/2014
 * Version: Initial version
 * Main Functionality:
 * 
 * Program description:
 * 
 * Called Programs:
 * Calling Programs:
 * Modification History:
 * --------------------
 * Changed Date  Description
 *  
 */
package com.ligootech.weclean;

import java.util.ArrayList;
import java.util.List;

public class Slidemenu {
	private String title; 
	
    private List<SlidemenuItem> SlidemenuItems = new ArrayList<SlidemenuItem>();

    public Slidemenu(String string) {
        this.title = string;
       
    }


	public void addSlidemenuItem(long id, String title) {
        this.SlidemenuItems.add( new SlidemenuItem(id, title));
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
       
    public List<SlidemenuItem> getSlidemenuItems() {
        return SlidemenuItems;
    }
    
    public void setSlidemenuItems(List<SlidemenuItem> SlidemenuItems) {
        this.SlidemenuItems = SlidemenuItems;
    }


}
