/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 03/09/2014
 * Version: Initial version
 * Main Functionality:Customer will put order here
 * 
 * Program description:
 * 1.Customer will place order here
 * 
 * Called Programs:CustomerLogin.java
 * Calling Programs:Custom_SchedulePickup.java
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
import java.util.Calendar;

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

import android.R.string;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Customer_SchedulePickup extends SherlockActivity {
	public static SlidingMenu slidingMenu;
	JSONArray jimage = null;
	InputStream is = null;
	Boolean errfl = false;
	StringBuilder sb = null;
	String result = null;
	ListView listview;
	ArrayList<String> Dress = new ArrayList<String>();
	ArrayList<String> DressC = new ArrayList<String>();
	ArrayList<String> Image = new ArrayList<String>();
	ArrayList<String> Cost = new ArrayList<String>();
	ArrayList<String> ItemId = new ArrayList<String>();
	ArrayList<String> itemImgName = new ArrayList<String>();
	int[] flag;
	// TextView startdate;
	private int pYear;
	private int pMonth;
	private int pDay;
	Boolean enddate = false;
	static final int DATE_DIALOG_ID = 0;
	Spinner pickFrom, duration, deliveryTo, deliveryTime;

	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	JSONArray jversion = null;
	private final String TAG_Itemcost = "Itemcost";
	private final String TAG_ItemName = "ItemName";
	private final String TAG_ItemNameChinese = "ItemNameChinese";
	private final String TAG_Cost = "Price";
	private final String TAG_ItemID = "ItemID";
	private final String TAG_disc = "disc";
	private final String TAG_DiscountAmount = "Discount";
	private final String TAG_PromoCode = "promoCode";
	private final String TAG_itemImgName="itemImgName";
	
	String Link,discAmount,promoCode;
	SharedPreferences pref;
	String accountName, accountNameSV;
	SharedPreferences Stimepref;
	Editor Stimeeditor;
	String Stime, Spickfrom, Sordertype, Sdeliverytype, Sdeliverytime;

	SharedPreferences Spickfrompref;
	Editor Spickfromeditor;

	SharedPreferences Sdeliverytypepref;
	Editor Sdeliverytypeeditor;

	SharedPreferences Sdeliverytimepref;
	Editor Sdeliverytimeeditor;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_pickup);
		deliveryTo = (Spinner) findViewById(R.id.delivery_spinner1);
		deliveryTime = (Spinner) findViewById(R.id.delivery_time_spinner);
		listview = (ListView) findViewById(R.id.customerlistView1);

		duration = (Spinner) findViewById(R.id.time_spinner1);
		pickFrom = (Spinner) findViewById(R.id.pickup_spinner1);

		Link = getResources().getString(R.string.lnk_retrive_cost);
		Button placeOrder = (Button) findViewById(R.id.place_order);

		pref = getApplicationContext().getSharedPreferences("accountNameSV", 0); // 0
		accountNameSV = pref.getString("accountNameSV", accountName);

		// call to fetch data from server

		// configure the SlidingMenu
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		slidingMenu.setMenu(R.layout.slidemenu);

		// actionbar properties and set custom layout
		final ActionBar actionBar = this.getSupportActionBar();

		actionBar.setTitle(R.string.pickuptitle); // change it to actual title
		getSupportActionBar().setHomeButtonEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setLogo(R.drawable.sidebar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		centerActionBarTitle();

		Stimepref = getSharedPreferences("Stime", 0); // 0
		Stime = Stimepref.getString("Stime", "0");
		

		Spickfrompref = getSharedPreferences("Spickfrom", 0); // 0
		Spickfromeditor = Spickfrompref.edit();
		Spickfrom = Spickfrompref.getString("Spickfrom", "0");

		Sdeliverytypepref = getSharedPreferences("Sdeliverytype", 0); // 0
		Sdeliverytypeeditor = Sdeliverytypepref.edit();
		Sdeliverytype = Sdeliverytypepref.getString("Sdeliverytype", "0");
		Sdeliverytimepref = getSharedPreferences("Sdeliverytime", 0); // 0
		Sdeliverytimeeditor = Sdeliverytimepref.edit();
		Sdeliverytime = Sdeliverytimepref.getString("Sdeliverytime", "0");
		deliveryTo
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		ArrayAdapter<CharSequence> deliveryToAdp = ArrayAdapter
				.createFromResource(this, R.array.deliveryto,
						R.layout.spinner_layout);
		deliveryToAdp.setDropDownViewResource(R.layout.spinner_layout);
		deliveryTo.setAdapter(deliveryToAdp);
		System.out.println("Sdeliverytype"+Sdeliverytype);
		deliveryTo.setSelection(Integer.valueOf(Sdeliverytype));
		LinearLayout loginlayout = (LinearLayout) findViewById(R.id.loginlayout);
		loginlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

			}
		});
		deliveryTime
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		ArrayAdapter<CharSequence> deliveryTimeAdp = ArrayAdapter
				.createFromResource(this, R.array.duration_array,
						R.layout.spinner_layout);
		deliveryTimeAdp.setDropDownViewResource(R.layout.spinner_layout);
		deliveryTime.setAdapter(deliveryTimeAdp);
		deliveryTime.setSelection(Integer.valueOf(Sdeliverytime));

		pickFrom.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		ArrayAdapter<CharSequence> pickFromAdp = ArrayAdapter
				.createFromResource(this, R.array.Pickupfrom,
						R.layout.spinner_layout);
		pickFromAdp.setDropDownViewResource(R.layout.spinner_layout);
		pickFrom.setAdapter(pickFromAdp);
		if(Spickfrom != "0")
		pickFrom.setSelection(Integer.valueOf(Spickfrom));
	
		placeOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(getApplicationContext(),
						CustomerOrderDetails.class);
				startActivity(i);

			}
		});

		flag = new int[] { R.drawable.shirt, R.drawable.trouser,
				R.drawable.skirt, R.drawable.tshirt, R.drawable.suits,
				R.drawable.loundry, R.drawable.jackets, R.drawable.special };

		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 3);
		pYear = cal.get(Calendar.YEAR);
		pMonth = cal.get(Calendar.MONTH);
		pDay = cal.get(Calendar.DAY_OF_MONTH);

		duration.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		ArrayAdapter<CharSequence> durAdapterAdp = ArrayAdapter
				.createFromResource(this, R.array.duration_array,
						R.layout.spinner_layout);
		durAdapterAdp.setDropDownViewResource(R.layout.spinner_layout);
		duration.setAdapter(durAdapterAdp);
		duration.setSelection(Integer.valueOf(Stime));
		TalkToServer myTask = new TalkToServer();
		myTask.execute();
		
		deliveryTo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String selItem=deliveryTo.getSelectedItem().toString();
				
				if(selItem.equalsIgnoreCase(getString(R.string.Collectfromtheshop)))
				{
					deliveryTime.setSelection(0);
					deliveryTime.setEnabled(false);
				}
				else
				{
					deliveryTime.setEnabled(true);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	}// oncreate end

	public class TalkToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(Customer_SchedulePickup.this,
					"WeClean", "Processing...");
			nameValuePairs.add(new BasicNameValuePair("CustomerID",
					accountNameSV));
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(Link);
			fetchCost();
			httppost(getResources().getString(R.string.lnk_disc_retrival));
			fetchDiscount();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// dismissDialog(progress_bar_type);

			listview.setAdapter(new Custom_SchedulePickup(
					Customer_SchedulePickup.this, Dress,DressC,itemImgName, Cost, ItemId,discAmount,promoCode));
			try {
				progressDialog.dismiss();
			} catch (Exception e) {

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
			System.out.println("result++" + result);

			// result = result.replace("][", ",");

			if (result != null) {

				JSONObject jsonObj = new JSONObject(result);

				// Getting JSON Array node
				jversion = jsonObj.getJSONArray(TAG_Itemcost);

				try {
					// looping through All product items
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);
						Dress.add(c.getString(TAG_ItemName));
						DressC.add(c.getString(TAG_ItemNameChinese));
						Cost.add(c.getString(TAG_Cost));
						ItemId.add(c.getString(TAG_ItemID));
						if(c.getString(TAG_itemImgName)!=null && c.getString(TAG_itemImgName)!="")
						itemImgName.add(getResources().getString(R.string.lnk_image)+c.getString(TAG_itemImgName));
						else
						itemImgName.add(getResources().getString(R.string.lnk_image)+getResources().getString(R.string.defaultImg));

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
	
	private void fetchDiscount() {
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

			if (result != null) {

				JSONObject jsonObj = new JSONObject(result);

				// Getting JSON Array node
				jversion = jsonObj.getJSONArray(TAG_disc);

				try {
					// looping through All product items
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);
						discAmount=c.getString(TAG_DiscountAmount);
					    promoCode=c.getString(TAG_PromoCode);
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
			final Activity activity = Customer_SchedulePickup.this;
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

	// private DatePickerDialog.OnDateSetListener pDateSetListener = new
	// DatePickerDialog.OnDateSetListener() {
	//
	// public void onDateSet(DatePicker view, int year, int monthOfYear,
	// int dayOfMonth) {
	//
	// pYear = year;
	// pMonth = monthOfYear;
	// pDay = dayOfMonth;
	// // your edit text
	//
	// pYear = year;
	// pMonth = monthOfYear;
	// pDay = dayOfMonth;
	// // your edit text
	// if (!enddate)
	// startdate.setText(new StringBuilder()
	// // Month is 0 based so add 1
	// .append(pYear).append("-").append(pMonth + 1)
	// .append("-").append(pDay).append(" "));

	// else

	// duration.setText(new StringBuilder()
	// // Month is 0 based so add 1
	// .append(pYear).append("-").append(pMonth + 1)
	// .append("-").append(pDay).append(" "));

	// }
	// };
	//
	// protected Dialog onCreateDialog(int id) {
	//
	// return new DatePickerDialog(this, pDateSetListener, pYear, pMonth, pDay);
	//
	// }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			slidingMenu.toggle();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
