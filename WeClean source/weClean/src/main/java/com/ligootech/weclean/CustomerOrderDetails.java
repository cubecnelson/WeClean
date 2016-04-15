/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 10/09/2014
 * Version: Initial version
 * Main Functionality:Displaying Order Details and schedule pickup details.
 * 
 * Program description:
 * 1.Here user can order id,order cost
 * 2.Expected delivery and payable option is also available
 * 3.User can make select delivery type in this page
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
import java.math.BigDecimal;
import java.util.ArrayList;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class CustomerOrderDetails extends SherlockActivity {
	public static SlidingMenu slidingMenu;
	JSONArray jversion = null;
	private static final String TAG_ORDERARRAY = "Orders";
	// private static final String TAG_ORDERID = "OrderID";
	private static final String TAG_EstimatedPickupTime = "EstimatedPickupTime";
	private static final String TAG_EstimatedDeliveryTime = "EstimatedDeliveryTime";
	private static final String TAG_PickUpTimepreference = "PickUpTimepreference";
	private static final String TAG_DeliveryTimePreference = "DeliveryTimePreference";
	private static final String TAG_PickedUpTime = "PickedUpTime";
	private static final String TAG_DeliveredTime = "DeliveredTime";
	private static final String TotalDue = "TotalDue";
	private static final String Tag_noItems = "noItems";
	private static final String Tag_deliveryfee = "deliveryfee";
	private static final String TAG_paymentStatus = "paymentStatus";
	private static final String TAG_Status = "Status";
	String Link;
	JSONArray jimage = null;
	InputStream is = null;
	Boolean errfl = false;
	StringBuilder sb = null;
	String result = null;
	ArrayList<String> Cost = new ArrayList<String>();
	String total;
	TextView orderdeltextview,camebytextView,pickuptextView1,TotalItems,deliverycharge,estimatecost, order, pickup_order_date, order_delivery_date,payment_text;
	String STATUS,PickedUpTIME,DeliveredTIME,orderid,noItems,deliveryfee, EstimatedPickupTime, EstimatedDeliveryTime,paymentStatus,PickUpTimepreference,DeliveryTimePreference;
	SharedPreferences pref;
	String accountName, accountNameSV;
	Spinner delivery_type, delivery_time,payment_method;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	Button change,paynow_button1,paylater;
	String deliveryType, deliveryTime;
	ProgressDialog progressDialog;
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

	// note that these credentials will differ between live & sandbox
	// environments.
	private static String CONFIG_CLIENT_ID ;

	private static final int REQUEST_CODE_PAYMENT = 1;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	private static final int REQUEST_CODE_PROFILE_SHARING = 3;
	private static final String TAG = "weclean payment";

//	private static PayPalConfiguration config = new PayPalConfiguration()
//			.environment(CONFIG_ENVIRONMENT)
//			.clientId(CONFIG_CLIENT_ID)
//			// The following are only used in PayPalFuturePaymentActivity.
//			.merchantName("choi@ligootech.com")
//			.merchantPrivacyPolicyUri(
//					Uri.parse("https://www.example.com/privacy"))
//			.merchantUserAgreementUri(
//					Uri.parse("https://www.example.com/legal"));

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_confirm);
		
		CONFIG_CLIENT_ID=getString(R.string.CONFIG_CLIENT_ID);
		
		PayPalConfiguration config = new PayPalConfiguration()
		.environment(CONFIG_ENVIRONMENT)
		.clientId(CONFIG_CLIENT_ID)
		// The following are only used in PayPalFuturePaymentActivity.
		.merchantName("choi@ligootech.com")
		.merchantPrivacyPolicyUri(
				Uri.parse("https://www.example.com/privacy"))
		.merchantUserAgreementUri(
				Uri.parse("https://www.example.com/legal"));
		
	    Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
		estimatecost = (TextView) findViewById(R.id.estimated_textView2);
		//deliverycharge=(TextView) findViewById(R.id.deliverycharge);
		TotalItems=(TextView) findViewById(R.id.TotalItems);
		payment_text=(TextView) findViewById(R.id.payment_textView1);
		order = (TextView) findViewById(R.id.orderidtextView2);
		pickup_order_date = (TextView) findViewById(R.id.pickup_order_date);
		order_delivery_date = (TextView) findViewById(R.id.order_delivery_date);
		delivery_type = (Spinner) findViewById(R.id.delivery_type);
		delivery_time = (Spinner) findViewById(R.id.delivery_time);
	//	payment_method = (Spinner) findViewById(R.id.credit_spinner1);
		change = (Button) findViewById(R.id.change);
	    paynow_button1 = (Button) findViewById(R.id.paynow_button1); 
	    paylater = (Button) findViewById(R.id.payafter_button2); 
	    pickuptextView1=(TextView) findViewById(R.id.pickuptextView1);
	    camebytextView=(TextView) findViewById(R.id.camebytextView4);
	    orderdeltextview=(TextView) findViewById(R.id.expect_textView1);
	    
		paynow_button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);


			        Intent intent = new Intent(CustomerOrderDetails.this, PaymentActivity.class);

			        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

			        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
				
			}
		});
		
		paylater.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}
		});
		
		// configure the SlidingMenu
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		slidingMenu.setMenu(R.layout.slidemenu);

		// actionbar properties and set custom layout
		final ActionBar actionBar = this.getSupportActionBar();
		// actionBar.setCustomView(R.layout.action_home);

		actionBar.setTitle(R.string.pickuptitle); // change it to actual title
		getSupportActionBar().setHomeButtonEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setLogo(R.drawable.sidebar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		centerActionBarTitle();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			orderid = extras.getString("OrderID");
		}
		order.setText(orderid);

		pref = getApplicationContext().getSharedPreferences("accountNameSV", 0); // 0
		accountNameSV = pref.getString("accountNameSV", accountName);

		Link = getResources().getString(R.string.orderdetailslink);
		TalkToServer myTask = new TalkToServer();
		myTask.execute();

		change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deliveryType = delivery_type.getSelectedItem().toString();
				deliveryTime = delivery_time.getSelectedItem().toString();
				ChangedeliveryDetails myTask = new ChangedeliveryDetails();
				myTask.execute();
			}
		});
		
		delivery_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String selItem=delivery_type.getSelectedItem().toString();
				
				if(selItem.equalsIgnoreCase(getString(R.string.Collectfromtheshop)))
				{
					delivery_time.setSelection(0);
					delivery_time.setEnabled(false);
				}
				else
				{
					delivery_time.setEnabled(true);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	}
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(estimatecost.getText().toString()), "HKD", "WeClean service charges",
                paymentIntent);
    }
	public class TalkToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(CustomerOrderDetails.this,
					"WeClean", "Please Wait...");
			nameValuePairs.add(new BasicNameValuePair("OrderID", orderid));
		}

		protected void onProgressUpdate(String... progress) {

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(Link);
			fetchOrderDeatails();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// dismissDialog(progress_bar_type);
			try {
				
				if(paymentStatus.equalsIgnoreCase("paid"))
				{
					paynow_button1.setVisibility(View.INVISIBLE);
					paylater.setVisibility(View.INVISIBLE);
					
					Locale current = getResources().getConfiguration().locale;
					if(current.equals(Locale.SIMPLIFIED_CHINESE) || current.equals(Locale.TRADITIONAL_CHINESE))
					{
						payment_text.setText(getString(R.string.paid));
					}
					else
						payment_text.setText("paid");
					
				} 
				String EPT = EstimatedPickupTime.substring(0, 2);
				String EDT = EstimatedDeliveryTime.substring(0, 2);
				estimatecost.setText(total);
				TotalItems.setText(noItems);
				//deliverycharge.setText(deliveryfee);
				if (!EPT.equalsIgnoreCase("00")) {
					
					EstimatedPickupTime=EstimatedPickupTime.substring(0,10)+" "+PickUpTimepreference;
					pickup_order_date.setText(EstimatedPickupTime);

				} else {
					pickup_order_date.setText(getResources().getString(
							R.string.not_assigned));
				}

				if (!EDT.equalsIgnoreCase("00")) {
					EstimatedDeliveryTime=EstimatedDeliveryTime.substring(0,10)+" "+DeliveryTimePreference;
					order_delivery_date.setText(EstimatedDeliveryTime);
				} else {
					order_delivery_date.setText(getResources().getString(
							R.string.not_assigned));
				}
				
				if(!STATUS.equalsIgnoreCase("waiting"))
				{
					pickuptextView1.setText(getString(R.string.pickedupyourorder));
					pickup_order_date.setText(PickedUpTIME);
					camebytextView.setVisibility(View.INVISIBLE);
				}
				if(STATUS.equalsIgnoreCase("delivered"))
				{
					orderdeltextview.setText(getString(R.string.deliveryyourorder)+" ");
					order_delivery_date.setText(DeliveredTIME);
					change.setEnabled(false);
				}
				
				
			} catch (Exception e) {

			}

			try {
				progressDialog.dismiss();
			} catch (Exception e) {

			}
		}
	}
	
	public class PaymentDoneUpdate extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(CustomerOrderDetails.this,
					"WeClean", "Please Wait...");
			List<NameValuePair> removenameValuePairs = nameValuePairs;
			   nameValuePairs.removeAll(removenameValuePairs);
			nameValuePairs.add(new BasicNameValuePair("OrderID", orderid));
			nameValuePairs.add(new BasicNameValuePair("CustomerID",
					accountNameSV));
		}

		protected void onProgressUpdate(String... progress) {

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(getResources().getString(R.string.lnk_customer_paypal_successful_modify));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				progressDialog.dismiss();
			} catch (Exception e) {

			}
		}
	}

	public class ChangedeliveryDetails extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(CustomerOrderDetails.this,
					"WeClean", "Please Wait...");
			Replace();
			nameValuePairs.add(new BasicNameValuePair("OrderID", orderid));
			nameValuePairs.add(new BasicNameValuePair("deliveryto",
					deliveryType));
			nameValuePairs.add(new BasicNameValuePair("deliverytime",
					deliveryTime));

		}

		private void Replace() {
			deliveryType=deliveryType.replace(getString(R.string.Inperson), "In person");
			deliveryType=deliveryType.replace(getString(R.string.Collectfromtheshop), "Collect from the shop");
			deliveryType=deliveryType.replace(getString(R.string.Buildingsecurity), "Building security");	
			deliveryTime=deliveryTime.replace(getString(R.string.anytime), "anytime");
		}

		protected void onProgressUpdate(String... progress) {
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(getResources().getString(R.string.lnk_modify_deli_details));
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// dismissDialog(progress_bar_type);
			Toast.makeText(getBaseContext(), "Delivery Details Changed",
					Toast.LENGTH_LONG).show();

			try {
				progressDialog.dismiss();
			} catch (Exception e) {

			}
		}
	}

	private void fetchOrderDeatails() {
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

				try {
					// looping through All product items
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);
						System.out.println("flow");

						total = c.getString(TotalDue);
						if(c.getString(Tag_noItems)!=null && c.getString(Tag_noItems)!="" )
						{
						noItems=c.getString(Tag_noItems);
						}
						else
						{
							noItems="0";
						}
					//	deliveryfee=c.getString(Tag_deliveryfee);
						EstimatedDeliveryTime = c
								.getString(TAG_EstimatedDeliveryTime);
						EstimatedPickupTime = c
								.getString(TAG_EstimatedPickupTime);
						paymentStatus = c
								.getString(TAG_paymentStatus);
						PickUpTimepreference=c
								.getString(TAG_PickUpTimepreference);
						DeliveryTimePreference=c
								.getString(TAG_DeliveryTimePreference);
						PickedUpTIME=c
								.getString(TAG_PickedUpTime);
						DeliveredTIME=c
								.getString(TAG_DeliveredTime);
						STATUS=c
								.getString(TAG_Status);
						
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

			HttpPost httppost = new HttpPost(link);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
			final Activity activity = CustomerOrderDetails.this;
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity,
							"Request timeout, please try again!!!!", 8000)
							.show();
				}
			});
			errfl = true;
			finish();

		}

	}

	// Actoion bar centering
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

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	  @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == REQUEST_CODE_PAYMENT) {
	            if (resultCode == Activity.RESULT_OK) {
	                PaymentConfirmation confirm =
	                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
	                if (confirm != null) {
	                    try {
	                        Log.i(TAG, confirm.toJSONObject().toString(4));
	                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
	                        /**
	                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
	                         * or consent completion.
	                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
	                         * for more details.
	                         *
	                         * For sample mobile backend interactions, see
	                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
	                         */
	                        PaymentDoneUpdate pdu=new PaymentDoneUpdate();
	                        pdu.execute();
	                        Toast.makeText(
	                                getApplicationContext(),
	                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
	                                .show();

	                    } catch (JSONException e) {
	                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i(TAG, "The user canceled.");
	            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        TAG,
	                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
	            }
	        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
	            if (resultCode == Activity.RESULT_OK) {
	                PayPalAuthorization auth =
	                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
	                if (auth != null) {
	                    try {
	                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

	                        String authorization_code = auth.getAuthorizationCode();
	                        Log.i("FuturePaymentExample", authorization_code);

	                        sendAuthorizationToServer(auth);
	                        Toast.makeText(
	                                getApplicationContext(),
	                                "Future Payment code received from PayPal", Toast.LENGTH_LONG)
	                                .show();

	                    } catch (JSONException e) {
	                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i("FuturePaymentExample", "The user canceled.");
	            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        "FuturePaymentExample",
	                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
	            } 
	        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
	            if (resultCode == Activity.RESULT_OK) {
	                PayPalAuthorization auth =
	                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
	                if (auth != null) {
	                    try {
	                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

	                        String authorization_code = auth.getAuthorizationCode();
	                        Log.i("ProfileSharingExample", authorization_code);

	                        sendAuthorizationToServer(auth);
	                        Toast.makeText(
	                                getApplicationContext(),
	                                "Profile Sharing code received from PayPal", Toast.LENGTH_LONG)
	                                .show();

	                    } catch (JSONException e) {
	                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
	                    }
	                }
	            } else if (resultCode == Activity.RESULT_CANCELED) {
	                Log.i("ProfileSharingExample", "The user canceled.");
	            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
	                Log.i(
	                        "ProfileSharingExample",
	                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
	            }
	        }
	    }

	    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

	        /**
	         * TODO: Send the authorization response to your server, where it can
	         * exchange the authorization code for OAuth access and refresh tokens.
	         * 
	         * Your server must then store these tokens, so that your server code
	         * can execute payments for this user in the future.
	         * 
	         * A more complete example that includes the required app-server to
	         * PayPal-server integration is available from
	         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
	         */

	    }
}
