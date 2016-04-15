package com.ligootech.weclean;

public interface Config {

	// used to share GCM regId with application server - using php app server
	static final String APP_SERVER_URL = "http://weclean-env.elasticbeanstalk.com/WeClean/weclean_gcm.php/?shareRegId=1";

	// GCM server using java 
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "933419547943";
	static final String MESSAGE_KEY = "m";

}
