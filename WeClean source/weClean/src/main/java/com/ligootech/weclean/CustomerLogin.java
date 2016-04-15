/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 03/09/2014
 * Version: Initial version
 * Main Functionality:Singnup and Login
 * 
 * Program description:
 * 
 * Called Programs:Splash.java
 * Calling Programs:CustomerRegistration.java
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class CustomerLogin extends SherlockActivity {
	EditText mobileno, password;
	Button login, howwork;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	String Link, MOBILENO, PASSWORD, name = "", SystemFlag;
	InputStream is = null;
	Boolean errfl = false;
	StringBuilder sb = null;
	JSONArray jimage = null;
	String result = null;
	private static final String TAG_VALIDITY = "firstname";
	private static String TAG_FIRSTNAME = "FirstName";
	private static String TAG_CUSTOMERID = "CustomerID";

	ImageView signup;
	public static SlidingMenu slidingMenu;
	private Spinner spinner;
	int customerid;
	SharedPreferences pref, langpref;
	Editor editor, langeditor;
	int langprefpos;
	String  accountNameSV;
	TextView forget_pwd;
	ProgressDialog progressDialog;
	Spinner langPref;

	// notification
	GoogleCloudMessaging gcm;
	Context Context;
	String regId;

	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	static final String TAG = "Register Activity";
	SharedPreferences regpref;
	Editor regeditor;
	Boolean reg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		mobileno = (EditText) findViewById(R.id.mobile_editText2);
		password = (EditText) findViewById(R.id.password_editText3);
		login = (Button) findViewById(R.id.login_button1);
		signup = (ImageView) findViewById(R.id.signup_imageView1);
		SystemFlag = getResources().getString(R.string.SystemFlag);
		howwork = (Button) findViewById(R.id.howwork_imageView1);
		forget_pwd = (TextView) findViewById(R.id.forget_textView1);
		langPref = (Spinner) findViewById(R.id.language_spinner1);
		LinearLayout loginlayout = (LinearLayout) findViewById(R.id.loginlayout);
		
		Locale current = getResources().getConfiguration().locale;
		
		if(current.equals(Locale.SIMPLIFIED_CHINESE) || current.equals(Locale.TRADITIONAL_CHINESE))
		{
			
			signup.setImageResource(R.drawable.login_c18);
		}

		
		loginlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			}
		});
		getActionBar().hide();
		// Spinner
		pref = getApplicationContext().getSharedPreferences("accountNameSV", 0); // 0
		editor = pref.edit();
		accountNameSV = pref.getString("accountNameSV", "0");
		System.out.println("account name"+accountNameSV);
		
		langpref = getApplicationContext().getSharedPreferences("langprefpos",
				0); // 0
		langeditor = pref.edit();
		langprefpos = pref.getInt("langprefpos", 0);
		langPref.setSelection(langprefpos);
		
		if(accountNameSV.equalsIgnoreCase("")||accountNameSV.equalsIgnoreCase("0"))
		{
		regpref = getApplicationContext().getSharedPreferences("reg", 0); // 0
		regeditor = regpref.edit();
		reg = regpref.getBoolean("reg", true);
		if (reg) // register only once when app is installed
		{
			System.out.println("inside kkkk");
			Context = getApplicationContext();
			if (TextUtils.isEmpty(regId)) {
				regId = registerGCM();
				Log.d("RegisterActivity", "GCM RegId: " + regId);
			}

		}

		addListenerOnSpinnerItemSelection();
		if (SystemFlag.equals("true")) {
			System.out.println("ccccccccfetch1");
		}
		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),
						CustomerRegistration.class);
				startActivity(i);

			}
		});

		howwork.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getBaseContext(),
						Faqs.class);
				startActivity(i);
			}
		});

		forget_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						ForgetPassword.class);
				startActivity(i);
			}
		});
		langPref.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int pos = langPref.getSelectedItemPosition();
				langeditor.putInt("langprefpos", pos);
				langeditor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Login1");
				MOBILENO = mobileno.getText().toString();
				PASSWORD = password.getText().toString();

				if (!MOBILENO.equals("") && !PASSWORD.equals("")) {

					System.out.println("Login5");
					Link = getResources().getString(R.string.loginconfirmlink);
					TalkToServer myTask = new TalkToServer(); // call to fetch
																// data
					// from
					// server in background
					// thread
					myTask.execute();
				}

				else if (!MOBILENO.equals("") && PASSWORD.equals("")) {
					PASSWORD = "pass1234";
					Link = getResources().getString(R.string.loginconfirmlink);
					TalkToServer myTask = new TalkToServer(); // call to fetch
					myTask.execute();
				} else {

					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.EnterMsgMobPass), 8000).show();
				}

			}
		});
		}
		else{
			Intent i = new Intent(getApplicationContext(),
					Customer_History.class);
			i.putExtra("regId", regId);
			startActivity(i);
			finish();
		}

	}

	public class TalkToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(CustomerLogin.this, "WeClean",
					"Logging in...");

			nameValuePairs.add(new BasicNameValuePair("mobileno1", mobileno
					.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("password", PASSWORD));
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(Link);

			fetchresult2();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (!name.isEmpty()) {
				// putting value
				editor.putString("accountNameSV", String.valueOf(customerid));
				editor.commit();
				Intent i = new Intent(getApplicationContext(),
						Customer_History.class);
				i.putExtra("regId", regId);
				startActivity(i);
				finish();
			} else {
				Toast.makeText(getApplicationContext(),
						"Enter corect mobile no and password",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	private void fetchresult2() {
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
			System.out.println("result" + result);

			if (result != null) {

				JSONObject jsonObj = new JSONObject(result);

				// Getting JSON Array node
				jimage = jsonObj.getJSONArray(TAG_VALIDITY);

				try {
					// looping through All Contacts
					for (int i = 0; i < jimage.length(); i++) {
						JSONObject c = jimage.getJSONObject(i);

						System.out.println("jimage.length()" + jimage.length());
						customerid = Integer.parseInt(c
								.getString(TAG_CUSTOMERID));
						name = c.getString(TAG_FIRSTNAME);		

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			else {

				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

		} catch (Exception exception) {
		}
	}

	private void httppost(String link) {
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(link);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
			final Activity activity = CustomerLogin.this;
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity,
							getResources().getString(R.string.Requesttimeout), 8000)
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

	public void addListenerOnSpinnerItemSelection() {

		spinner = (Spinner) findViewById(R.id.language_spinner1);
		spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		ArrayAdapter<CharSequence> foodadapter = ArrayAdapter
				.createFromResource(this, R.array.Language_arrays,
						R.layout.spinner_layout);
		// foodadapter.setDropDownViewResource(R.layout.spinner_layout);
		spinner.setAdapter(foodadapter);

	}

	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(Context);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.d("RegisterActivity",
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
		} else {
			// Toast.makeText(getApplicationContext(),
			// "RegId already available. RegId: " + regId,
			// Toast.LENGTH_LONG).show();
		}
		return regId;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				CustomerLogin.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("RegisterActivity",
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(Context);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					Log.d("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(Context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				// Toast.makeText(getApplicationContext(),
				// "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
				// .show();
			}
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				CustomerLogin.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

}
