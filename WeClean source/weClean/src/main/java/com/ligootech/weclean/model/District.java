package com.ligootech.weclean.model;

public class District {
	int districtid;
	String district;
	
	// constructors
			public District() {

			}
			
			public District( int districtid,String district ) {
				this.districtid = districtid;
				
				this.district=district;
				
			}
		
			// setter
			public void setdistrictid(int districtid) {
				this.districtid = districtid;
			}

			public void setdistrict(String district) {
				this.district = district;
			}
			

			
			// getter
			public int getdistrictid() {
				return this.districtid;
			}
			public String getdistrict() {
				return this.district;
			}
			
}
