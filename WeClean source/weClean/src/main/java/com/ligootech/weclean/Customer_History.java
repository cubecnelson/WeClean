/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 10/09/2014
 * Version: Initial version
 * Main Functionality:Displaying Customer History in this page.
 * 
 * Program description:
 * 1.Here user can order id,order Date
 * 2.Andalso user can see status and payment status.
 * Called Programs:Customer_SchedulePickup.java
 * Calling Programs:
 * Modification History:
 * --------------------
 * Changed Date  Description
 *  
 */
package com.ligootech.weclean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Customer_History extends SherlockActivity {
	@Override
	protected void onResume() {
		commentflag = false;
		Link = getResources().getString(R.string.orderhistorylink);
		TalkToServer myTask = new TalkToServer(); // call to fetch data from
		// server in background
		// thread

		myTask.execute();
		super.onResume();
	}
	Editor editor;
	public static SlidingMenu slidingMenu;
	JSONArray jimage = null;
	InputStream is = null;
	Boolean errfl = false;
	StringBuilder sb = null;
	String result = null;
	String Link;
	ListView listview;
	JSONArray jversion = null;
	Button placeorder;
	private static final String TAG_ORDERARRAY = "history";
	private static final String TAG_ORDERID = "OrderID";
	private static final String TAG_Time = "Time1";
	private static final String ORDER_STATUS = "Status";
	private static final String PAYMENT_STATUS = "paymentStatus";

	ArrayList<String> ORDERID = new ArrayList<String>();
	ArrayList<String> TIME = new ArrayList<String>();
	ArrayList<String> STATUS = new ArrayList<String>();
	ArrayList<String> PAY_STATUS = new ArrayList<String>();
	ArrayList<String> RORDERID = new ArrayList<String>();
	ArrayList<String> RTIME = new ArrayList<String>();
	ArrayList<String> RSTATUS = new ArrayList<String>();
	ArrayList<String> RPAY_STATUS = new ArrayList<String>();
	Button submit_button1;
	EditText editcomment;
	SharedPreferences pref;
	String accountName, accountNameSV;
	Boolean commentflag = false;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	ProgressDialog progressDialog;

	ShareExternalServer appUtil;
	String regId;
	AsyncTask<Void, Void, String> shareRegidTask;
	SharedPreferences regpref;
	Editor regeditor;
	Boolean reg;
	LinearLayout container;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_orderstatus);

		placeorder = (Button) findViewById(R.id.place_button1);

		// configure the SlidingMenu
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		slidingMenu.setMenu(R.layout.slidemenu);
		slidingMenu.setSlidingEnabled(true);

		// actionbar properties and set custom layout
		final ActionBar actionBar = this.getSupportActionBar();
		// actionBar.setCustomView(R.layout.action_home);

		actionBar.setTitle(getResources().getString(R.string.OrderStatus)); // change it to actual title
		getSupportActionBar().setHomeButtonEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setLogo(R.drawable.sidebar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		centerActionBarTitle();

		pref = getApplicationContext().getSharedPreferences("accountNameSV", 0); // 0
		editor = pref.edit();
		accountNameSV = pref.getString("accountNameSV", accountName);
		submit_button1 = (Button) findViewById(R.id.submit_button1);
		editcomment = (EditText) findViewById(R.id.editcomment);
		container = (LinearLayout) findViewById(R.id.container);
		regpref = getApplicationContext().getSharedPreferences("reg", 0); // 0
		regeditor = regpref.edit();
		reg = regpref.getBoolean("reg", true);

		if (reg) { // share only once when app is installed
			System.out.println("inside kkkk2");

			// pop up notification
			appUtil = new ShareExternalServer();

			regId = getIntent().getStringExtra("regId");
			Log.d("MainActivity", "regId: " + regId);

			final Context context = this;
			shareRegidTask = new AsyncTask<Void, Void, String>() {
				@Override
				protected String doInBackground(Void... params) {
					String result = appUtil.shareRegIdWithAppServer(context,
							regId, accountNameSV);
					return result;
				}

				@Override
				protected void onPostExecute(String result) {
					shareRegidTask = null;
				}

			};
			shareRegidTask.execute(null, null, null);

			regeditor.putBoolean("reg", false);
			regeditor.commit();

		}
		
		container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

			}
		});

		placeorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						Customer_SchedulePickup.class);
				startActivity(i);

			}
		});
		editcomment.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(editcomment.getText().toString().trim().length() >= 1)
				{
					submit_button1.setVisibility(View.VISIBLE);
				}
				else
				{
					submit_button1.setVisibility(View.INVISIBLE);
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
		submit_button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("editcomment.getText().toString().trim()"
						+ editcomment.getText().toString().trim());
				if (editcomment.getText().toString().trim().equals("")) {
					Toast.makeText(getBaseContext(),
							getResources().getString(R.string.fillcomments), Toast.LENGTH_LONG)
							.show();
				} else {
					commentflag = true;
				//	Link = getResources().getString(R.string.commentlink);
					TalkToServer myTask = new TalkToServer(); // call to fetch
																// data from
																// server in
																// background
																// thread

					myTask.execute();
				}

			}
		});
		listview = (ListView) findViewById(R.id.addlistView1);
		Link = getResources().getString(R.string.orderhistorylink);
//		TalkToServer myTask = new TalkToServer(); // call to fetch data from
//													// server in background
//													// thread
//
//		myTask.execute();

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// ListView Clicked item value
				final String itemValue = (String) listview
						.getItemAtPosition(position);
				String msg;
				System.out.print("STATUS.get(position)"+STATUS.get(position));
				if(STATUS.get(position).equalsIgnoreCase("waiting") ||
						STATUS.get(position).equalsIgnoreCase(getString(R.string.cwaiting)))
					msg=getString(R.string.ModifyOrder); 
				 else
					 msg=getString(R.string.viewOrderDetails); 
				
				if (itemValue.contains("empty")) {
					Toast.makeText(getApplicationContext(),
							"Nothing to display list is empty",
							Toast.LENGTH_LONG).show();

				} else {
					new AlertDialog.Builder(Customer_History.this)
							.setTitle(getString(R.string.Selectoption))

							.setPositiveButton(msg,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// continue with delete
											Intent i = new Intent(
													getApplicationContext(),
													Modify_SchedulePickup.class);
											i.putExtra("OrderID", itemValue);
											startActivity(i);
											

										}
									})
							.setNegativeButton(getString(R.string.ViewOrderstatus),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent i = new Intent(
													getApplicationContext(),
													CustomerOrderDetails.class);
											i.putExtra("OrderID", itemValue);
											startActivity(i);
										}
									}).setIcon(R.drawable.ic_launcher).show();

				}

			}

		});
	}

	public class TalkToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(Customer_History.this,
					"WeClean", "Processing...");

			nameValuePairs.add(new BasicNameValuePair("CustomerID",
					accountNameSV));
			System.out.println("accountNameSV" + accountNameSV);
			if (commentflag) {
				nameValuePairs.add(new BasicNameValuePair("comment",
						editcomment.getText().toString()));
			}
		}

		@Override
		protected Void doInBackground(Void... arg0) {
            
			
			if (commentflag)
			{
				httppost(getResources().getString(R.string.commentlink));
			}else{
				httppost(Link);
				fetchHistory();}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			if (!commentflag)
				listview.setAdapter(new Custom_History(Customer_History.this,
						ORDERID, STATUS, PAY_STATUS, TIME));
			progressDialog.dismiss();
			if (commentflag)
			{
				commentflag=false;
				Toast.makeText(getBaseContext(), getResources().getString(R.string.CommentPostedsuccess),
						Toast.LENGTH_LONG).show();
			editcomment.setText("");
			}
		}
	}

	private void fetchHistory() {
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
			System.out.println("result++" + result);

			// result = result.replace("][", ",");

			if (result != null) {

				JSONObject jsonObj = new JSONObject(result);

				// Getting JSON Array node
				jversion = jsonObj.getJSONArray(TAG_ORDERARRAY);
				System.out.println(" jversion  " + jversion);
				System.out.println(" jversion .length() " + jversion.length());
				RORDERID= ORDERID;
				ORDERID.removeAll(RORDERID);
				
				RSTATUS = STATUS;
				STATUS.removeAll(RSTATUS);
				RPAY_STATUS = PAY_STATUS;
				PAY_STATUS.removeAll(PAY_STATUS);
				RTIME = TIME;
				TIME.removeAll(RTIME);
				try {
					// looping through All product items
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);
						System.out.println("flow");
						ORDERID.add(c.getString(TAG_ORDERID));
						STATUS.add(c.getString(ORDER_STATUS));
						PAY_STATUS.add(c.getString(PAYMENT_STATUS));
						TIME.add((c.getString(TAG_Time)));

						System.out.println("HHHHSTATUS" + STATUS);
					}
				}

				catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

		} catch (Exception exception) {
			System.out.println("exception" + exception);
		}
	}

	private void httppost(String link) {
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(link);

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
			final Activity activity = Customer_History.this;
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidingMenu.toggle();
			break;
		  case R.id.logout_icon:
			  editor.putString("accountNameSV","0");
				editor.commit();
			  Intent i = new Intent(
						getApplicationContext(),
						CustomerLogin.class);
			  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			   break;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	 @Override
	 public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

	  // putting the icons in the action bar
	  com.actionbarsherlock.view.MenuInflater mif = this
	    .getSupportMenuInflater();
	  mif.inflate(R.menu.main_activity_action, menu);
	  return true;

	 }
}
