package com.ligootech.weclean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import com.actionbarsherlock.app.ActionBar;




import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Shopdetails extends Activity {
	TextView phoneno, address, shopname;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	InputStream is = null;
	Boolean errfl = false;
	StringBuilder sb = null;
	// shopShopdetails
	private static final String TAG_SHOPARRAY = "Shopdetails";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_SHOPNAME = "shopname";
	private static final String TAG_ADDRESSC = "addressC";
	private static final String TAG_SHOPNAMEC = "shopnameC";
	private static final String TAG_ITEMNAMEC = "ItemNameChinese";
	private static final String TAG_ITEMNAME = "ItemName";
	private static final String TAG_PRICE = "Price";
	JSONArray jversion = null;
	String result = null;
	String PHONE, ADDRESS, Link3, SHOPNAME, ITEMNAME, PRICE;
	String value;
	ListView listprice;
	ArrayList<String> Values = new ArrayList<String>();
	Handler handler;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopdetails);
		//set action bar
		final android.app.ActionBar actionBar = this.getActionBar();
		// actionBar.setCustomView(R.layout.action_home);

		actionBar.setTitle(getString(R.string.ShopDetails)); // change it to actual
															// title

		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		centerActionBarTitle();
		
		
		
		phoneno = (TextView) findViewById(R.id.phonetextView2);
		address = (TextView) findViewById(R.id.addresstextView4);
		shopname = (TextView) findViewById(R.id.shopname);
		listprice = (ListView) findViewById(R.id.listprice);
		adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1,
				android.R.id.text1, Values);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			value = extras.getString("variable");
			System.out.println("Shopvalue" + value);
		}
		Link3 = getString(R.string.lnk_shop_details);
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				

				listprice.setAdapter(adapter);

				phoneno.setText(PHONE);
				address.setText(ADDRESS);
				shopname.setText(SHOPNAME);
			}
			
		};
		
		ShopToServer myTask1 = new ShopToServer();
		myTask1.execute();

	}

	public class ShopToServer extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(Shopdetails.this, "WeClean",
					"Processing...");

			nameValuePairs.add(new BasicNameValuePair("shopid", value));
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
			progressDialog.dismiss();
			handler.sendEmptyMessage(0);
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

			if (result != null) {

				JSONObject jsonObj = new JSONObject(result);

				// Getting JSON Array node
				jversion = jsonObj.getJSONArray(TAG_SHOPARRAY);
				Locale current = getResources().getConfiguration().locale;
				try {
					// looping through All product items
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);

						PHONE = (c.getString(TAG_PHONE));
						
						
						if(current.equals(Locale.SIMPLIFIED_CHINESE) || current.equals(Locale.TRADITIONAL_CHINESE))
						{
							ADDRESS = (c.getString(TAG_ADDRESSC));
							SHOPNAME = (c.getString(TAG_SHOPNAMEC));
							ITEMNAME = (c.getString(TAG_ITEMNAMEC)) + "  -  "
									+ (c.getString(TAG_PRICE)) + getResources().getString(R.string.chinesecur);
							Values.add(ITEMNAME);	
						}
						else
						{
							ADDRESS = (c.getString(TAG_ADDRESS));
							SHOPNAME = (c.getString(TAG_SHOPNAME));
							ITEMNAME = (c.getString(TAG_ITEMNAME)) + "  -  "
									+ (c.getString(TAG_PRICE)) + getResources().getString(R.string.chinesecur);
							Values.add(ITEMNAME);
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
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
			final Activity activity = Shopdetails.this;
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity,
							"Request timeout, please try again!!!!", Toast.LENGTH_LONG)
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

}
