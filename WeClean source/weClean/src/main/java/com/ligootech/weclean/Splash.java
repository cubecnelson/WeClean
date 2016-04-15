/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 03/09/2014
 * Version: Initial version
 * Main Functionality:Showing splash screen for couple of seconds
 * 
 * Program description:
 * 1.Showing splash screen for couple of seconds
 * 2.After that will enter into login screen
 * Called Programs:
 * Calling Programs:CustomerLogin.java
 * Modification History:
 * --------------------
 * Changed Date  Description
 *  
 */
package com.ligootech.weclean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.ligootech.weclean.Registration.RegistrationActivity;
import com.ligootech.weclean.utils.CheckConnectivity;

public class Splash extends Activity {
	protected int splashTime = 500;
	CheckConnectivity checkConnectivity;
	Context Context;
	AlertDialog ad;
	Splash m;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		checkConnectivity = CheckConnectivity
				.getInstance(getApplicationContext());

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ad != null)
			if (ad.isShowing()) {
				ad.dismiss();
			}
		if (checkConnectivity.isOnline(Splash.this)) {
			try {
				// fetchImages();
				if (!CheckConnectivity.isfast) {
					showslowconnection(Context);
				} else {
					
					Thread background = new Thread() {
						public void run() {

							try {
								// Thread will sleep for 4
								// seconds
								System.out.println("going1");
								sleep(4 * splashTime);
								// After 4 seconds redirect to another intent
								System.out.println("going2");

								Intent i = new Intent(getBaseContext(),RegistrationActivity.class);
								startActivity(i);
							
								// Remove activity
								finish();

							} catch (Exception e) {

							}
						}
					};
					// start thread
					background.start();

				}
			} catch (Exception e) {
				showNetwrokMessage("",
						getResources().getString(R.string.internet_fail_msg), m);

			}
		} else {
			showNetwrokMessage("",
					getResources().getString(R.string.internet_fail_msg), m);

		}
		
		
		
	}
	
	public void showNetwrokMessage(String title, String message, Context context) {
		ad = new AlertDialog.Builder(Splash.this)
				.setMessage(message)
				.setPositiveButton("Cancel",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();

								// the rest of your stuff
							}
						})
				.setNegativeButton("Go to Setting",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.cancel();
								startActivity(new Intent(
										android.provider.Settings.ACTION_WIFI_SETTINGS));

								System.out.println("flow1");
							}
						}).show();
	}
	
	private void showslowconnection(Context context) {
		ad = new AlertDialog.Builder(Splash.this)
				.setMessage(
						"Your Network connection is slow, Some products may not load properly")
				.setPositiveButton("Ok",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								Thread background = new Thread() {
									public void run() {

										try {
											// Thread will sleep for 4
											// seconds
											System.out.println("going1");
											sleep(4 * splashTime);
											// After 4 seconds redirect to another intent
											System.out.println("going2");

											Intent i = new Intent(getBaseContext(),RegistrationActivity.class);
											startActivity(i);
										
											// Remove activity
											finish();

										} catch (Exception e) {

										}
									}
								};
								// start thread
								background.start();

								
							}

							
						}).show();
	}
}
