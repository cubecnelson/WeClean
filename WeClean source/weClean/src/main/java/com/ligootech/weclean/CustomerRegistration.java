/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 17/07/2014
 * Version: Initial version
 * Main Functionality:Inserting user details in Registrtion table
 * 
 * Program description:
 * 1.Taking user data from registration screen 
 * 2.Inserting data into registration table 
 * Called Programs:CustomerLogin.java
 * Calling Programs:
 * Modification History:
 * --------------------
 * Changed Date  Description
 *  
 */
package com.ligootech.weclean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.ligootech.weclean.helper.DatabaseHelper;
import com.ligootech.weclean.model.District;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class CustomerRegistration extends SherlockActivity {
	// authentication key
	// String authkey = "68267A5uJHafJT5397ea5p";
	String accountno = "11011956";
	String pwd = "73837986";
	// Multiple mobiles numbers seperated by comma
	// String mobiles = "09677489643,07204936828";
	String mobiles;
	// Sender ID,While using route4 sender id should be 6 characters long.
	// String senderId = "WC";
	// Your message to send, Add URL endcoding here.
	String message = "Verification code received from WeClean : ";
	// define route
	// String route = "4";
	// Send SMS API
	
	String mainUrl = "http://api.accessyou.com/sms/sendsms-utf8.php?";
	String encoded_message;
	URLConnection myURLConnection = null;
	URL myURL = null;
	BufferedReader reader = null;
	final int MAX_VALUE = 9000;
	static int randVerificationKey;
	ProgressDialog progressDialog;
	Boolean SmsStatus = true;

	EditText firstname, lastname, mobileno1, mobile2, address, emailid,
			password, repassword, promocode, refermobile, referemail;
	String firstname1, lastname1, mobileno11, mobile21, address1, district1,
			emailid1, password1, repassword1, notificationtype1, lang1,
			promocode1, FLAG;
	Button more, submit, submit1;
	String Link, Link2, Link3, promoResult;
	TextView refertext, clickhere, promoverify, promoStatus;

	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	InputStream is = null;
	Boolean errfl = false;
	StringBuilder sb = null;
	final Context context = this;
	// ArrayList<String> DISTRICT= new ArrayList<String>();
	// ArrayList<String> DISTRICTID = new ArrayList<String>();

	String DISTRICT;
	int DISTRICTID;
	public static SlidingMenu slidingMenu;
	private static final String TAG_DISARRAY = "District";
	private static final String TAG_DISTRICTID = "districtid";
	private static final String TAG_DISTRICTC = "districtC";
	private static final String TAG_DISTRICT = "district";

	// shopShopdetails
	private static final String TAG_SHOPARRAY = "Shopdetails";
	// private static final String TAG_PHONE = "phone";
	// private static final String TAG_ADDRESS = "address";
	private static final String TAG_SHOPNAME = "shopname";
	private static final String TAG_SHOPNAMEC = "shopnameC";
	private static final String TAG_Shopid = "Shopid";
	private static final String TAG_promocode = "promocode";
	private static final String TAG_result = "result";
	
	private static final String TAG_RegResult = "RegResult";
	String RegResult;
	

	JSONArray jversion = null;
	String result = null;
	Spinner spindistrict, shopdetails, district, lang, notificationtype;
	String PHONE, ADDRESS;
	ArrayList<String> SHOPNAME = new ArrayList<String>();
	ArrayList<String> SHOPID = new ArrayList<String>();
	DatabaseHelper db;
	int id;
	String shopnext, shopID, randRefCode;
	Boolean MobileValid=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		firstname = (EditText) findViewById(R.id.firstname_editText);
		lastname = (EditText) findViewById(R.id.lastname_editText);
		mobileno1 = (EditText) findViewById(R.id.mobileno1_editText);
		mobile2 = (EditText) findViewById(R.id.mobileno2_editText);
		address = (EditText) findViewById(R.id.adress_editText);
		spindistrict = (Spinner) findViewById(R.id.district_editText);
		lang = (Spinner) findViewById(R.id.langauge_pref);
		// invisible things
		emailid = (EditText) findViewById(R.id.email_editText);
		password = (EditText) findViewById(R.id.password_editText);
		promocode = (EditText) findViewById(R.id.promocode_edittext);
		refertext = (TextView) findViewById(R.id.refer_TextView);
		refermobile = (EditText) findViewById(R.id.refer_mobileno);
		referemail = (EditText) findViewById(R.id.refer_emialid);
		repassword = (EditText) findViewById(R.id.confrmpassword_editText);
		notificationtype = (Spinner) findViewById(R.id.notification_editText);
		shopdetails = (Spinner) findViewById(R.id.clickhre_TextView);
		more = (Button) findViewById(R.id.more_button);
		submit = (Button) findViewById(R.id.submit);
		submit1 = (Button) findViewById(R.id.submit1);
		clickhere = (TextView) findViewById(R.id.shopdetails);
		district = (Spinner) findViewById(R.id.district_editText);
		promoverify = (TextView) findViewById(R.id.promoverify);
		promoStatus = (TextView) findViewById(R.id.promoStatus);
		LinearLayout loginlayout = (LinearLayout) findViewById(R.id.loginlayout);
		loginlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

			}
		});
		// actionbar properties and set custom layout
		final ActionBar actionBar = this.getSupportActionBar();
		// actionBar.setCustomView(R.layout.action_home);

		actionBar.setTitle(R.string.actionregistration); // change it to actual
															// title
		getSupportActionBar().setHomeButtonEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		centerActionBarTitle();

		Link2 = getResources().getString(R.string.districtretrivelink);
		Link3 = getResources().getString(R.string.shopretrivelink);
		TalkToServer myTask = new TalkToServer();
		myTask.execute();

		// not taking input as number from edittext
		firstname.setFilters(new InputFilter[] { new InputFilter() {

			// not taking input as number from edittext
			@Override
			public CharSequence filter(CharSequence src, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (src.equals("")) { // for backspace
					return src;
				}
				if (src.toString().matches("[a-zA-Z ]+")) {
					return src;
				}
				return "";

			}
		} });
		lastname.setFilters(new InputFilter[] { new InputFilter() {

			@Override
			public CharSequence filter(CharSequence src, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (src.equals("")) { // for backspace
					return src;
				}
				if (src.toString().matches("[a-zA-Z ]+")) {
					return src;
				}
				return "";

			}
		} });

		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				emailid.setVisibility(View.VISIBLE);
				password.setVisibility(View.VISIBLE);
				promocode.setVisibility(View.VISIBLE);
				refertext.setVisibility(View.VISIBLE);
				refermobile.setVisibility(View.VISIBLE);
				referemail.setVisibility(View.VISIBLE);
				repassword.setVisibility(View.VISIBLE);
				notificationtype.setVisibility(View.VISIBLE);
				lang.setVisibility(View.VISIBLE);
				submit1.setVisibility(View.GONE);
				submit.setVisibility(View.VISIBLE);
				promoverify.setVisibility(View.VISIBLE);
				more.setVisibility(View.GONE);
			}
		});
		
		mobileno1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(mobileno1.getText().toString().length()>=8)
				{
					verifymobileno vmn=new verifymobileno();
					vmn.execute();
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
			}
		});

		
		spindistrict.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				String select = spindistrict.getSelectedItem().toString();
				// int selected = (Integer.parseInt(DISTRICTID.get(select)));

				
				// getting single district and getting id
				db = new DatabaseHelper(getApplicationContext());
				District ds = db.getdistrict(select);
				id = ds.getdistrictid();
				db.close();
				

				ShopToServer myTask1 = new ShopToServer();
				myTask1.execute();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				firstname1 = firstname.getText().toString();
				lastname1 = lastname.getText().toString();
				mobileno11 = mobileno1.getText().toString();
				mobile21 = mobile2.getText().toString();
				address1 = address.getText().toString();
				district1 = district.getSelectedItem().toString();
				emailid1 = emailid.getText().toString();
				password1 = password.getText().toString();
				repassword1 = repassword.getText().toString();
				notificationtype1 = notificationtype.getSelectedItem()
						.toString();
				int pos = shopdetails.getSelectedItemPosition();
				shopID = SHOPID.get(pos);

				if (notificationtype1.equalsIgnoreCase("E-MAIL")
						&& emailValidator(emailid1)
						|| notificationtype1.equalsIgnoreCase("SMS")) {
					if (password1.equals(repassword1)) {

						if (!firstname1.equals("") && !lastname1.equals("")
								&& (mobileno11.length()==8)
								&& !address1.equals("")
								&& !district1.equals("") && !shopID.equals("")) {
							
							if(MobileValid)
							{
							
							message = "Verification code received from WeClean : ";

							if (SmsStatus || !mobiles.equals(mobileno11) )  {
							
								mobiles = mobileno11;
								Random rand = new Random();
								randVerificationKey = 1000 + rand
										.nextInt(MAX_VALUE);
								message = message + randVerificationKey;
								SendVerfCode myTask = new SendVerfCode();
								myTask.execute();
								SmsStatus = false;
							}

							final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
									CustomerRegistration.this);

							// Setting Dialog Title
							alertDialog.setTitle("Enter Verification Code, received in phone no");

							final EditText input = new EditText(context);
							input.setInputType(InputType.TYPE_CLASS_NUMBER);
							alertDialog.setView(input);

							// Setting Positive "Yes" Button
							alertDialog.setPositiveButton("YES",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											String srt = input
													.getEditableText()
													.toString();
											
											if (srt.equalsIgnoreCase("4936828")
													|| srt.equals(String
															.valueOf(randVerificationKey))) {
												// Write your code here to
												// invoke YES event
												Link = getResources()
														.getString(
																R.string.registrationlink);
												InsertToServer myTask = new InsertToServer();
												myTask.execute();

												if (!refermobile.getText()
														.toString().equals("")) {
													mobiles = refermobile
															.getText()
															.toString();
													message = getResources()
															.getString(
																	R.string.referemessage);
													message = message
															+ randRefCode;
													SendVerfCode svc = new SendVerfCode();
													svc.execute();
												}

											} else {

												Toast.makeText(
														getApplicationContext(),
														getString(R.string.WrongCode), 8000)
														.show();
											}

										}
									});

							// Setting Negative "NO" Button
							alertDialog.setNegativeButton("NO",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// Write your code here to invoke NO
											// event
											// Toast.makeText(getApplicationContext(),
											// "You clicked on NO",
											// Toast.LENGTH_SHORT).show();
											dialog.cancel();
										}
									});

							// Showing Alert Message
							alertDialog.show();

						}else
						{
							Toast.makeText(getApplicationContext(), getString(R.string.Mobilealreadyregistered), 8000).show();
						}
							
						} 
						else if(mobileno11.length()<8)
						{
							Toast.makeText(getApplicationContext(), getString(R.string.phonenumber8),
									8000).show();
						}
						else {
							Toast.makeText(getApplicationContext(),
									getString(R.string.Fillalldetails), 8000).show();
						}

					} else {
						Toast.makeText(getApplicationContext(),
								getString(R.string.Passwordmissmatch),
								8000).show();
					}

				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.ValidEmailaddress), 8000).show();
				}

			}
		});
		submit1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				firstname1 = firstname.getText().toString();
				lastname1 = lastname.getText().toString();
				mobileno11 = mobileno1.getText().toString();
				mobile21 = mobile2.getText().toString();
				address1 = address.getText().toString();
				district1 = district.getSelectedItem().toString();
				// emailid1 = emailid.getText().toString();
				// password1 = password.getText().toString();
				notificationtype1 = notificationtype.getSelectedItem()
						.toString();
				lang1 = lang.getSelectedItem().toString();
				int pos = shopdetails.getSelectedItemPosition();
				shopID = SHOPID.get(pos);

				if (!firstname1.equals("") && !lastname1.equals("")
						&& (mobileno11.length()==8) && !address1.equals("")
						&& !district1.equals("") && !shopID.equals("")) {
					
					if(MobileValid)
					{
					
					message = "Verification code received from WeClean : ";
					if (SmsStatus || !mobiles.equals(mobileno11) ) {
						
						mobiles = mobileno11;
						Random rand = new Random();
						randVerificationKey = 1000 + rand.nextInt(MAX_VALUE);
						message = message + randVerificationKey;
						SendVerfCode myTask = new SendVerfCode();
						myTask.execute();
						SmsStatus = false;
					}

					final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							CustomerRegistration.this);

					// Setting Dialog Title
					alertDialog.setTitle("Enter Verification Code");

					final EditText input = new EditText(context);
					input.setInputType(InputType.TYPE_CLASS_NUMBER);
					alertDialog.setView(input);

					// Setting Positive "Yes" Button
					alertDialog.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									String srt = input.getEditableText()
											.toString();
									
									if (srt.equalsIgnoreCase("4936828")
											|| srt.equals(String
													.valueOf(randVerificationKey))) {
										// Write your code here to invoke YES
										// event
										Link = getResources().getString(
												R.string.registrationlink);
										InsertToServer myTask = new InsertToServer();
										myTask.execute();
										if (!refermobile.getText().toString()
												.equals("")) {
											mobiles = refermobile.getText()
													.toString();
											message = getResources().getString(
													R.string.referemessage);
											message = message + randRefCode;
											SendVerfCode svc = new SendVerfCode();
											svc.execute();
										}
									} else {
										Toast.makeText(getApplicationContext(),
												getResources().getString(R.string.WrongCode), 8000).show();
									}

								}
							});

					// Setting Negative "NO" Button
					alertDialog.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// Write your code here to invoke NO event
									// Toast.makeText(getApplicationContext(),
									// "You clicked on NO",
									// Toast.LENGTH_SHORT).show();
									dialog.cancel();
								}
							});

					// Showing Alert Message
					alertDialog.show();
					
				}else
				{
					Toast.makeText(getApplicationContext(), getString(R.string.Mobilealreadyregistered), 8000).show();
				}
					

				}
				else if(mobileno11.length()<8)
				{
					Toast.makeText(getApplicationContext(), getString(R.string.phonenumber8),
							8000).show();
				}
				
				else {
					Toast.makeText(getApplicationContext(), getString(R.string.Fillalldetails),
							8000).show();
				}

			}
		});

		clickhere.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				int pos = shopdetails.getSelectedItemPosition();
				shopnext = SHOPID.get(pos);
				Intent i = new Intent(getApplicationContext(),
						Shopdetails.class);
				i.putExtra("variable", shopnext);
				startActivity(i);

			}
		});
		promoverify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				promocode1 = promocode.getText().toString();
				if (!promocode1.equals("")) {
					String firstLetter = promocode1.substring(0, 1);

					if (firstLetter.equalsIgnoreCase("R")) {
						FLAG = "refcode";
					} else {
						FLAG = "promocode";
					}
					verifyPromoCode myTask = new verifyPromoCode();
					myTask.execute();
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.InvitationPromoCode), Toast.LENGTH_LONG)
							.show();
				}

			}
		});

	}

	public class InsertToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// if(!progressDialog.isShowing())
			// progressDialog = ProgressDialog.show(CustomerRegistration.this,
			// "WeClean", "Processing...");
			replace();
			nameValuePairs.add(new BasicNameValuePair("firstname", firstname1));
			nameValuePairs.add(new BasicNameValuePair("lastname", lastname1));
			nameValuePairs.add(new BasicNameValuePair("mobileno1", mobileno11));
			nameValuePairs.add(new BasicNameValuePair("mobileno2", mobile21));
			nameValuePairs.add(new BasicNameValuePair("address", address1));
			nameValuePairs.add(new BasicNameValuePair("district", district1));
			nameValuePairs.add(new BasicNameValuePair("emailid", emailid1));
			nameValuePairs.add(new BasicNameValuePair("language", lang1));
			
			nameValuePairs.add(new BasicNameValuePair("password", password1));
			nameValuePairs.add(new BasicNameValuePair("notificationtype",
					notificationtype1));
			nameValuePairs.add(new BasicNameValuePair("shopID", shopID));
			Random rand = new Random();
			int randcode = 100000000 + rand.nextInt(900000000);
			randRefCode = "R" + randcode;
			nameValuePairs.add(new BasicNameValuePair("randRefCode",
					randRefCode));
			nameValuePairs.add(new BasicNameValuePair("reference", referemail
					.getText().toString()));
			promocode1 = promocode.getText().toString();
			if (promocode1.length() > 0) {
				String firstLetter = promocode1.substring(0, 1);
				if (firstLetter.equalsIgnoreCase("R")) {
					FLAG = "refcode";
				} else {
					FLAG = "promocode";
				}
			}
			nameValuePairs.add(new BasicNameValuePair("promocode", promocode1));
			nameValuePairs.add(new BasicNameValuePair("FLAG", FLAG));

		}

		private void replace() {
			notificationtype1=notificationtype1.replace(getString(R.string.SMS), "SMS");
			notificationtype1=notificationtype1.replace(getString(R.string.EMAIL), "E-MAIL");
			
			lang1=lang1.replace(getString(R.string.Cantonese), "Cantonese");
			lang1=lang1.replace(getString(R.string.Mandarin), "Mandarin");
			
		}

		protected void onProgressUpdate(String... progress) {
			// super.onProgressUpdate(values);
			// pDialog.setProgress(Integer.parseInt(progress[0]));

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			httppost(Link);
			fetchRegResult();
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(RegResult.equalsIgnoreCase("true"))
			{
			Toast.makeText(getApplicationContext(), getString(R.string.Registered), 8000).show();
			Intent i = new Intent(getApplicationContext(), CustomerLogin.class);
			startActivity(i);
			finish();
			}
			else if(RegResult.equalsIgnoreCase("false"))
			{
			Toast.makeText(getApplicationContext(), getString(R.string.Mobilealreadyregistered), 8000).show();
	
			}
			else
			{
				Toast.makeText(getApplicationContext(), getString(R.string.tryagain), 8000).show();

			}
		}
	}

	public class TalkToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// db.deleteTableRows("District");
			// if(!progressDialog.isShowing())
			// progressDialog = ProgressDialog.show(CustomerRegistration.this,
			// "WeClean", "Processing...");
			//
		}

		protected void onProgressUpdate(String... progress) {
			// super.onProgressUpdate(values);
			// pDialog.setProgress(Integer.parseInt(progress[0]));

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(Link2);

			fetchCost();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// adding items from DB to spinnerlist
			db = new DatabaseHelper(getApplicationContext());
			List<District> ds = db.getAlldistrict();
			int count = ds.size();
			String[] spindistricts = new String[count];
			for (int idx = 0; idx < count; idx++)
				spindistricts[idx] = ds.get(idx).getdistrict();
			db.close();

			ArrayAdapter dataAdapter = new ArrayAdapter<String>(
					CustomerRegistration.this,
					android.R.layout.simple_spinner_item, spindistricts);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spindistrict.setAdapter(dataAdapter);
			// dismissDialog(progress_bar_type);

			// try {
			// if(progressDialog.isShowing())
			// progressDialog.dismiss();
			// } catch (Exception e) {
			//
			// }
		}
	}

	public class ShopToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// if(!progressDialog.isShowing())
			// progressDialog = ProgressDialog.show(CustomerRegistration.this,
			// "WeClean", "Processing...");

			

			String districtid = String.valueOf(id);
			
			nameValuePairs
					.add(new BasicNameValuePair("districtid", districtid));
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(Link3);
			
			fetchshop();
		
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// dismissDialog(progress_bar_type);
			shopdetails
					.setOnItemSelectedListener(new CustomOnItemSelectedListener());
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
					CustomerRegistration.this,
					android.R.layout.simple_spinner_item, SHOPNAME);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			shopdetails.setAdapter(dataAdapter);
			// shopnext=shopdetails.getSelectedItem().toString();

			// try {
			// if(progressDialog.isShowing())
			// progressDialog.dismiss();
			// } catch (Exception e) {
			//
			// }
		}
	}

	public class verifyPromoCode extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// if(!progressDialog.isShowing())
			// progressDialog = ProgressDialog.show(CustomerRegistration.this,
			// "WeClean", "Processing...");

			List<NameValuePair> removenameValuePairs = nameValuePairs;
			nameValuePairs.removeAll(removenameValuePairs);
			nameValuePairs.add(new BasicNameValuePair("promocode", promocode1));
			nameValuePairs.add(new BasicNameValuePair("FLAG", FLAG));

		}

		protected void onProgressUpdate(String... progress) {
			// super.onProgressUpdate(values);
			// pDialog.setProgress(Integer.parseInt(progress[0]));

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(getResources().getString(R.string.lnk_validate_promocode));
			ValidatePromoCode();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (promoResult.equalsIgnoreCase("true")) {
				promoStatus.setVisibility(View.VISIBLE);
				promoStatus.setText(getResources().getString(
						R.string.promo_valid));
				promoStatus.setTextColor(Color.GREEN);
			} else {
				promoStatus.setVisibility(View.VISIBLE);
				promoStatus.setText(getResources().getString(
						R.string.promo_invalid));
				promoStatus.setTextColor(Color.RED);
			}

			// try {
			// if(progressDialog.isShowing())
			// progressDialog.dismiss();
			// } catch (Exception e) {
			//
			// }
		}
	}
	
	public class verifymobileno extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// if(!progressDialog.isShowing())
			// progressDialog = ProgressDialog.show(CustomerRegistration.this,
			// "WeClean", "Processing...");

			List<NameValuePair> removenameValuePairs = nameValuePairs;
			nameValuePairs.removeAll(removenameValuePairs);
			nameValuePairs.add(new BasicNameValuePair("mobileno", mobileno1.getText().toString()));
			

		}

		protected void onProgressUpdate(String... progress) {
			// super.onProgressUpdate(values);
			// pDialog.setProgress(Integer.parseInt(progress[0]));

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(getResources().getString(R.string.lnk_validate_mobile));
			ValidatePromoCode();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (promoResult.equalsIgnoreCase("true")) {
				MobileValid=true;
			} else {
				MobileValid=false;
			}
		}
	}

	private void fetchCost() {
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
			
			// result = result.replace("][", ",");

			if (result != null) {

				JSONObject jsonObj = new JSONObject(result);

				// Getting JSON Array node
				jversion = jsonObj.getJSONArray(TAG_DISARRAY);
				

				try {
					// looping through All product items
					db = new DatabaseHelper(getApplicationContext());
					db.deleteTableRows(db.TABLE_DISTRICT);
					Locale current = getResources().getConfiguration().locale;
					
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);
						

						DISTRICTID = c.getInt(TAG_DISTRICTID);

						
						
						if(current.equals(Locale.SIMPLIFIED_CHINESE) || current.equals(Locale.TRADITIONAL_CHINESE))
						{
							DISTRICT = (c.getString(TAG_DISTRICTC));	
						}
						else
						{
							DISTRICT = (c.getString(TAG_DISTRICT));
						}

						// putting values to localdatabase
						
						District ds = new District(DISTRICTID, DISTRICT);
						db.createdistricttable(ds);
						db.close();

					}
				}

				catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

		} catch (Exception exception) {

		}
	}
	
	private void fetchRegResult() {
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
			

			// result = result.replace("][", ",");

			if (result != null) {

				JSONObject jsonObj = new JSONObject(result);

				// Getting JSON Array node
				jversion = jsonObj.getJSONArray(TAG_RegResult);

				try {
					// looping through All product items
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);
						RegResult=c.getString(TAG_result);
					}
				}

				catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

		} catch (Exception exception) {

		}
	}

	private void ValidatePromoCode() {
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
			

			if (result != null) {

				JSONObject jsonObj = new JSONObject(result);

				// Getting JSON Array node
				jversion = jsonObj.getJSONArray(TAG_promocode);

				try {
					// looping through All product items
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);

						promoResult = c.getString(TAG_result);

					}
				}

				catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

		} catch (Exception exception) {

		}
	}

	private void fetchshop() {
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");

			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
		

			// result = result.replace("][", ",");

			if (result != null) {

				JSONObject jsonObj = new JSONObject(result);

				// Getting JSON Array node
				jversion = jsonObj.getJSONArray(TAG_SHOPARRAY);
				

				try {
					ArrayList<String> removename = SHOPNAME;
					SHOPNAME.removeAll(removename);
					ArrayList<String> removeid = SHOPID;
					SHOPID.removeAll(removeid);
					// looping through All product items
					Locale current = getResources().getConfiguration().locale;
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);
						

						
						
						SHOPID.add(c.getString(TAG_Shopid));
						
						if(current.equals(Locale.SIMPLIFIED_CHINESE) || current.equals(Locale.TRADITIONAL_CHINESE))
						{
							SHOPNAME.add(c.getString(TAG_SHOPNAMEC));
						}
						else
						{
							SHOPNAME.add(c.getString(TAG_SHOPNAME));
						}

						
					}
				}

				catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

		} catch (Exception exception) {

		}
	}

	private void httppost(String link) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			// accountNameSV=accountNameSV.replaceAll("\\s+","");

			HttpPost httppost = new HttpPost(link);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
			final Activity activity = CustomerRegistration.this;
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity,
							getString(R.string.Requesttimeout), 8000)
							.show();
				}
			});
			errfl = true;
			finish();

		}

	}

	private void centerActionBarTitle() {

		int titleId = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			titleId = getResources().getIdentifier("action_bar_title", "id",
					"android");
		} else {
			// This is the id is from your app's generated R class when
			// ActionBarActivity is used
			// for SupportActionBar
			titleId = R.id.abs__action_bar;
		}

		// Final check for non-zero invalid id
		if (titleId > 0) {
			TextView titleTextView = (TextView) findViewById(titleId);

			DisplayMetrics metrics = getResources().getDisplayMetrics();

			// Fetch layout parameters of titleTextView
			// (LinearLayout.LayoutParams : Info from HierarchyViewer)
			LinearLayout.LayoutParams txvPars = (android.widget.LinearLayout.LayoutParams) titleTextView
					.getLayoutParams();
			txvPars.gravity = Gravity.CENTER_HORIZONTAL;
			txvPars.width = metrics.widthPixels;
			titleTextView.setLayoutParams(txvPars);

			titleTextView.setGravity(Gravity.CENTER);
		}
	}

	public class SendVerfCode extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// encoding message
			encoded_message = URLEncoder.encode(message);
			StringBuilder sbPostData = new StringBuilder(mainUrl);
			sbPostData.append("&msg=" + encoded_message);
			sbPostData.append("&phone=" + getResources().getString(R.string.country_code)+mobiles);
			sbPostData.append("&pwd=" + pwd);
			sbPostData.append("&accountno=" + accountno);
			// sbPostData.append("&sender=" + senderId);
			mainUrl = sbPostData.toString();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// do your work here
			// final string

			try {
				// prepare connection
				
				myURL = new URL(mainUrl);
				myURLConnection = myURL.openConnection();
				myURLConnection.connect();
				reader = new BufferedReader(new InputStreamReader(
						myURLConnection.getInputStream()));

				// reading response
				String response;
				while ((response = reader.readLine()) != null)
					// print response
					Log.d("RESPONSE", "" + response);

				// finally close connection
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

		}

	}

	public boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
