/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 30/07/2014
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

public class SlidemenuItem {
	private long id;
    private String title;
    private String icon;

    public SlidemenuItem(long id, String title) {
        this.id = id;
        this.title = title;
        this.icon=icon;
    }



	
	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
  

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
