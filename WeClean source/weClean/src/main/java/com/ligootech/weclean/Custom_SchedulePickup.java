/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 03/09/2014
 * Version: Initial version
 * Main Functionality:Customer Adapter for SchedulePickup
 * 
 * Program description:
 * 1.Customer will place order here
 * 
 * Called Programs:Customer_SchedulePickup.java
 * Calling Programs:Custom_SchedulePickup.java
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
import java.util.HashMap;
import java.util.Locale;

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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.style.ReplacementSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Custom_SchedulePickup extends BaseAdapter {
	ArrayList<String> Dress;
	ArrayList<String> DressC;
	ArrayList<String> Cost;
	ArrayList<String> ItemId;
	ArrayList<String> itemImgName ;
//	ArrayList<String> Itemcolor;
	
	Context context;
	Activity mContext;
	String colorname;
	private static LayoutInflater inflater = null;
	final int[] qty;
	
	double deliveryfee=0;

	Boolean errfl = false;
	StringBuilder sb = null;
	InputStream is = null;
	String Link,discAmount,promoCode;
	Double estimatedCost = 0.0,discountCost=0.0;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	public static HashMap<Integer, String> myQtyList = new HashMap<Integer, String>();
	public static HashMap<Integer, String> myColList = new HashMap<Integer, String>();
	SharedPreferences pref;
	Editor editor;
	String accountName, accountNameSV;
	ProgressDialog progressDialog;
	String DELIVERYTYPE, TIME, PICKFROM, DELIVERYTIME, TYPE;
	String SDELIVERYTYPE, STIME, SPICKFROM, SDELIVERYTIME,SPECIALREQUEST;
	Spinner pickfrom, type;
	String Sdeliverytype, Stime, Spickfrom, Sdeliverytime, Stype;

	SharedPreferences Sdeliverytypepref;
	Editor Sdeliverytypeeditor;
	SharedPreferences Spickfrompref;
	Editor Spickfromeditor;
	SharedPreferences Stimepref;
	Editor Stimeeditor;
	SharedPreferences Sdeliverytimepref;
	Editor Sdeliverytimeeditor;
	SharedPreferences Stypepref;
	Editor Stypeeditor;

	private static final String TAG_OrderDetails = "orderID";
	private static final String TAG_Order_ID = "id";
	private static final String TAG_result = "result";
	private static final String TAG_mobileno = "mobileno";
	String result = null;
	JSONArray jarray = null;
	String OrderID,resultStatus="false",mobileno;
	
	String accountno = "11011956";
	    String pwd="73837986";
	String mobiles;
	String message;
	String mainUrl = "http://api.accessyou.com/sms/sendsms-utf8.php?";
	String encoded_message;
	URLConnection myURLConnection = null;
	URL myURL = null;
	BufferedReader reader = null;
	TextView typeedit;

	public Custom_SchedulePickup(Activity c, ArrayList<String> Dress,ArrayList<String> DressC,
			ArrayList<String> itemImgName , ArrayList<String> Cost,ArrayList<String> ItemId,String discAmount,String promoCode) {

		mContext = c;

		this.Dress = Dress;
		this.DressC = DressC;
		this.Cost = Cost;
		this.ItemId = ItemId;
		this.discAmount=discAmount;
		this.promoCode=promoCode;
		this.itemImgName=itemImgName;
		//Itemcolor =  new ArrayList<String>();
		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		qty = new int[Dress.size()];
		for (int i = 0; i < Dress.size(); i++) {
			myQtyList.put(i, "0");
			//Itemcolor.add("");
			myColList.put(i, "");
		}
			pref = mContext.getSharedPreferences("accountNameSV", 0); // 0
			accountNameSV = pref.getString("accountNameSV", accountName);
			Sdeliverytypepref = mContext.getSharedPreferences("Sdeliverytype",
					0); // 0
			Sdeliverytypeeditor = Sdeliverytypepref.edit();
			Stimepref = mContext.getSharedPreferences("Stime", 0); // 0
			Stimeeditor = Stimepref.edit();
			Spickfrompref = mContext.getSharedPreferences("Spickfrom", 0); // 0
			Spickfromeditor = Spickfrompref.edit();
			Sdeliverytimepref = mContext.getSharedPreferences("Sdeliverytime",
					0); // 0
			Sdeliverytimeeditor = Sdeliverytimepref.edit();
			Stypepref = mContext.getSharedPreferences("Stype", 0); // 0
			Stypeeditor = Stypepref.edit();

			Sdeliverytype = Sdeliverytypepref.getString("Sdeliverytype", "");
			Stime = Stimepref.getString("Stime", "0");
			Spickfrom = Spickfrompref.getString("Spickfrom", "");
			System.out.println("Spickfrom"+Spickfrom);
			Sdeliverytime = Sdeliverytimepref.getString("Sdeliverytime", "");
			Stype = Stypepref.getString("Stype", "");
		

	}

	@Override
	public int getCount() {

		return Dress.size();
	}
	
	@Override
	public Object getItem(int position) {

		return Dress.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.schedulepickup_custom, null);

		LinearLayout loginlayout = (LinearLayout) vi.findViewById(R.id.loginlayout);
		loginlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

			}
		});
		TextView textview = (TextView) vi.findViewById(R.id.dressdescription1);
		final TextView totalItems = (TextView) mContext
				.findViewById(R.id.total_textView2);
		final TextView totalEstDue = (TextView) mContext
				.findViewById(R.id.estimatedDue);
		final TextView Total = (TextView) mContext
				.findViewById(R.id.TotalEstimatedDue2);
		TextView Discount=(TextView) mContext.findViewById(R.id.Discount2);
		Button placeOrder = (Button) mContext.findViewById(R.id.place_order);

		Discount.setText(discAmount+"%");
	
		Locale current = mContext.getResources().getConfiguration().locale;
		if(current.equals(Locale.SIMPLIFIED_CHINESE) && !DressC.get(position).equals("")
		|| current.equals(Locale.TRADITIONAL_CHINESE)&& !DressC.get(position).equals("") )
		{
			textview.setText(DressC.get(position));
		}
		else
		{
			textview.setText(Dress.get(position));
		}
		
		ImageView dressimage = (ImageView) vi.findViewById(R.id.dress1);
	//	dressimage.setImageResource(flag[position]);
		Button add = (Button) vi.findViewById(R.id.add_button1);
		Button minus = (Button) vi.findViewById(R.id.minus_button1);
		final EditText Special_request=(EditText) mContext.findViewById(R.id.Special_request);
		final Spinner duration = (Spinner) mContext
				.findViewById(R.id.time_spinner1);
		pickfrom = (Spinner) mContext.findViewById(R.id.pickup_spinner1);

		final Spinner deliveryTo = (Spinner) mContext
				.findViewById(R.id.delivery_spinner1);
		final Spinner deliveryTime = (Spinner) mContext
				.findViewById(R.id.delivery_time_spinner);


		final TextView orderquantity = (TextView) vi
				.findViewById(R.id.orderquantity1);
		
		Picasso.with(mContext).load(itemImgName.get(position)).into(dressimage);

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				estimatedCost += Double.valueOf(Cost.get(position));
				discountCost=estimatedCost-((Double.valueOf(discAmount)/100)*estimatedCost);

				qty[position]++;
				orderquantity.setText(String.valueOf(qty[position]));
				myQtyList.put(position, String.valueOf(qty[position]).trim());
				int sum = 0;
				for (int i : qty)
					sum += i;
				totalItems.setText(String.valueOf(sum));

				// Double estimatedCost = 0.0;
				//for (int i : qty)
					//estimatedCost += i * Double.valueOf(Cost.get(position));
				totalEstDue.setText(mContext.getResources().getString(R.string.chinesecur)+String.valueOf(estimatedCost));
				Total.setText(mContext.getResources().getString(R.string.chinesecur)+String.valueOf(discountCost));
			}
		});
		minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
//				if(estimatedCost < 0.0){
//					estimatedCost = 0.0;
//				}

				qty[position]--;

				if(qty[position] >= 0)
				{
				estimatedCost -= Double.valueOf(Cost.get(position));
				discountCost=estimatedCost-((Double.valueOf(discAmount)/100)*estimatedCost);
				orderquantity.setText(String.valueOf(qty[position]));
				myQtyList.put(position, String.valueOf(qty[position]).trim());

				int sum = 0;
				for (int i : qty)
					sum += i;
				totalItems.setText(String.valueOf(sum));

				//for (int i : qty)
					//estimatedCost += i * Double.valueOf(Cost.get(position));
				totalEstDue.setText(mContext.getResources().getString(
						R.string.chinesecur)+String.valueOf(estimatedCost));
				Total.setText(mContext.getResources().getString(
						R.string.chinesecur)+String.valueOf(discountCost));
				}
				else{
					qty[position] = 0;
					orderquantity.setText(String.valueOf(qty[position]));
				}
			}
		});

		orderquantity.setText(myQtyList.get(position));
		final TextView typeedit;
		
		typeedit = (TextView) vi.findViewById(R.id.dresstype1_spinner1);
		
		try{
			if(Dress.get(position).substring(0, 7).equalsIgnoreCase("Laundry"))
			{
				typeedit.setVisibility(View.INVISIBLE);
			}
			else
			{
				typeedit.setVisibility(View.VISIBLE);
			}
			}
			catch(Exception e)
			{
				
			}

		
//		if(!myColList.get(position).equals("")) {
//			typeedit.setText(myColList.get(position));
//
//		}
		

		typeedit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showcolorpopup(position,typeedit);
								
			}
		});
	
	
		placeOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean itemempty = true;
				for(int idx =0; idx < myQtyList.size(); idx++){
					if(!myQtyList.get(idx).equals("0") )
						itemempty = false;
				}
				
				if(!itemempty)
				{
				if(estimatedCost<Integer.valueOf(mContext.getResources().getString(R.string.delcharLimit)))
				{
//					estimatedCost=estimatedCost+Double.valueOf(mContext.getResources().getString(R.string.deliveryCharges));
//					discountCost=discountCost+Double.valueOf(mContext.getResources().getString(R.string.deliveryCharges));
//					deliveryfee=Double.valueOf(mContext.getResources().getString(R.string.deliveryCharges));
					new AlertDialog.Builder(mContext)
					.setTitle(mContext.getResources().getString(R.string.ToastDelicharge))

					
					.setNegativeButton(mContext.getString(R.string.Yes),
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int which) {
									// continue with modify
									dothis();
									
								}
							})
							
							.setPositiveButton(mContext.getString(R.string.No),
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							})
							.setIcon(R.drawable.ic_launcher).show();
					
				}
				else
				{
				   deliveryfee=0;
					dothis();
				}
				}
				else
				{
					Toast.makeText(mContext,mContext.getResources().getString(R.string.itemcleaning), Toast.LENGTH_LONG)
					.show();
				}
				
				
				
			}
			

			public void dothis()
			{
				
			TYPE = typeedit.getText().toString();
			System.out.println("type"+TYPE);
			TIME = String.valueOf(duration.getSelectedItemPosition());
			STIME = duration.getSelectedItem().toString();
			
			PICKFROM = String.valueOf(pickfrom
					.getSelectedItemPosition());
			SPICKFROM = pickfrom.getSelectedItem().toString();

			DELIVERYTYPE = String.valueOf(deliveryTo
					.getSelectedItemPosition());
			SDELIVERYTYPE = deliveryTo.getSelectedItem().toString();
			DELIVERYTIME = String.valueOf(deliveryTime
					.getSelectedItemPosition());
			SDELIVERYTIME = deliveryTime.getSelectedItem().toString();
			SPECIALREQUEST=Special_request.getText().toString();
		
			
			
			if (PICKFROM.equals(""))
				Toast.makeText(mContext, mContext.getResources().getString(R.string.EnterPickUpFrom),
						Toast.LENGTH_LONG).show();
			else if (TIME.equals(""))
				Toast.makeText(mContext,mContext.getResources().getString(R.string.EnterPickUpTime),
						Toast.LENGTH_LONG).show();
			else if (DELIVERYTIME.equals(""))
				Toast.makeText(mContext,mContext.getResources().getString(R.string.EnterDeliveryTime),
						Toast.LENGTH_LONG).show();
			else if(SDELIVERYTYPE.equals("")){
				Toast.makeText(mContext, mContext.getResources().getString(R.string.EnterDeliveryTo), Toast.LENGTH_LONG)
				.show();

			}
//			else if(itemempty)
//			{
//				Toast.makeText(mContext,mContext.getResources().getString(R.string.itemcleaning), Toast.LENGTH_LONG)
//				.show();
//			}
			else if (!DELIVERYTYPE.equals("") && !TIME.equals("")
					&& !PICKFROM.equals("") && !DELIVERYTIME.equals("")
					) {

				Sdeliverytypeeditor
						.putString("Sdeliverytype", DELIVERYTYPE);
				Sdeliverytypeeditor.commit();
				Stimeeditor.putString("Stime", TIME);
				Stimeeditor.commit();

				Spickfromeditor.putString("Spickfrom", PICKFROM);
				Spickfromeditor.commit();
				Sdeliverytimeeditor
						.putString("Sdeliverytime", DELIVERYTIME);
				Sdeliverytimeeditor.commit();
				System.out.println("ORDERTYPE" + DELIVERYTIME);
				Stypeeditor.putString("Stype", TYPE);
				Stypeeditor.commit();
				Link = mContext.getResources().getString(R.string.lnk_insert_order);
				TalkToServer myTask = new TalkToServer();
				myTask.execute();
			} 
			
			}
			
		});
		return vi;
	}

	public class TalkToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(mContext, "WeClean",
					"Please Wait...");
			if(estimatedCost<Integer.valueOf(mContext.getResources().getString(R.string.delcharLimit)))
			{
				estimatedCost=estimatedCost+Double.valueOf(mContext.getResources().getString(R.string.deliveryCharges));
				discountCost=discountCost+Double.valueOf(mContext.getResources().getString(R.string.deliveryCharges));
				deliveryfee=Double.valueOf(mContext.getResources().getString(R.string.deliveryCharges));
			}
			
			Replace();
			
			nameValuePairs.add(new BasicNameValuePair("deliveryto",
					SDELIVERYTYPE));
			nameValuePairs.add(new BasicNameValuePair("deliverytime",
					SDELIVERYTIME));
			nameValuePairs.add(new BasicNameValuePair("Pickfrom", SPICKFROM));
			nameValuePairs.add(new BasicNameValuePair("Picktime", STIME));
			nameValuePairs.add(new BasicNameValuePair("CustomerID",
					accountNameSV));
			nameValuePairs.add(new BasicNameValuePair("TotalDue", String
					.valueOf(discountCost)));
			nameValuePairs.add(new BasicNameValuePair("actualDue", String
					.valueOf(estimatedCost)));
			nameValuePairs.add(new BasicNameValuePair("SpecialRequest",SPECIALREQUEST));
			nameValuePairs.add(new BasicNameValuePair("promoCode",promoCode));
			nameValuePairs.add(new BasicNameValuePair("deliveryfee",String.valueOf(deliveryfee)));

			for (int i = 0; i < Dress.size(); i++) {
				System.out.println("Dress.size()" + Dress.size());
				nameValuePairs.add(new BasicNameValuePair("item[]", myQtyList
						.get(i)));
				nameValuePairs.add(new BasicNameValuePair("itemid[]", ItemId
						.get(i)));
				nameValuePairs.add(new BasicNameValuePair("Itemcolor[]", myColList
						.get(i)));
				

			}

		}

		private void Replace() {

			SDELIVERYTYPE=SDELIVERYTYPE.replace(mContext.getString(R.string.Inperson), "In person");
			SDELIVERYTYPE=SDELIVERYTYPE.replace(mContext.getString(R.string.Collectfromtheshop), "Collect from the shop");
			SDELIVERYTYPE=SDELIVERYTYPE.replace(mContext.getString(R.string.Buildingsecurity), "Building security");
			
			SDELIVERYTIME=SDELIVERYTIME.replace(mContext.getString(R.string.anytime), "anytime");
			
			STIME=STIME.replace(mContext.getString(R.string.anytimep), "anytime");
			
			SPICKFROM=SPICKFROM.replace(mContext.getString(R.string.Inpersonp), "In person");
			SPICKFROM=SPICKFROM.replace(mContext.getString(R.string.Hangatdoor), "Hang at door");
			SPICKFROM=SPICKFROM.replace(mContext.getString(R.string.Buildingsecurityp), "Building security");
			
		}

		protected void onProgressUpdate(String... progress) {
			// super.onProgressUpdate(values);

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(Link);
			fetchOrderID();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try {
				progressDialog.dismiss();
			} catch (Exception e) {

			}
			if(resultStatus.equalsIgnoreCase("true"))
			{
				if(!mobileno.equals("0"))
				{
					mobiles=mobileno;
							message = mContext.getResources().getString(R.string.msg_orderreceived)+" ["+OrderID+"]";
					SendVerfCode svc = new SendVerfCode();
			         svc.execute();
					
				}
			Intent i = new Intent(mContext, CustomerOrderDetails.class);
			i.putExtra("OrderID", OrderID);
			mContext.startActivity(i);
			}
			else
			{
				Toast.makeText(mContext,
						mContext.getResources().getString(R.string.tryagain),Toast.LENGTH_LONG)
						.show();
			}

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
						sbPostData.append("&phone="+mContext.getResources().getString(R.string.country_code)+ mobiles);
						sbPostData.append("&pwd=" + pwd);
						sbPostData.append("&accountno=" + accountno);
					//	sbPostData.append("&sender=" + senderId);
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

	private void httppost(String link) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(link);
		//	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
			httppost.setEntity(formEntity);	
			HttpResponse response = httpclient.execute(httppost);
			
//			 httpPut.setEntity(new StringEntity(body, HTTP.UTF_8));
//			    httpPost.setEntity(new StringEntity(body, HTTP.UTF_8));
			
			System.out.println("response" + response);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
			// final Activity activity = Customer_SchedulePickup.this;
			mContext.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext,
mContext.getResources().getString(R.string.Requesttimeout), 8000)
							.show();
				}
			});
			errfl = true;
			mContext.finish();

		}
	}

	private void fetchOrderID() {
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
				jarray = jsonObj.getJSONArray(TAG_OrderDetails);

				try {
					for (int itemlistidx = 0; itemlistidx < jarray.length(); itemlistidx++) {
						JSONObject c = jarray.getJSONObject(itemlistidx);
						OrderID = c.getString(TAG_Order_ID);
						resultStatus=c.getString(TAG_result);
						mobileno=c.getString(TAG_mobileno);
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
	private void showcolorpopup(final int position, final TextView typeedit) {
		LayoutInflater layoutInflater = (LayoutInflater) mContext
				.getBaseContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.colorpopup, null);
		final PopupWindow popupWindow = new PopupWindow(popupView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
	
		Button  white, gray, black, pink, red, orange, yellow, green, darkgreen, blue, darkblue, purple;
		white = (Button) popupView.findViewById(R.id.White);
		gray = (Button) popupView.findViewById(R.id.Gray);
		black = (Button) popupView.findViewById(R.id.Black);
		pink = (Button) popupView.findViewById(R.id.Pink);
		red = (Button) popupView.findViewById(R.id.Red);
		orange = (Button) popupView.findViewById(R.id.Orange);
		yellow = (Button) popupView.findViewById(R.id.Yellow);
		green = (Button) popupView.findViewById(R.id.Green);
		darkgreen = (Button) popupView.findViewById(R.id.DarkGreen);
		blue = (Button) popupView.findViewById(R.id.Blue);
		darkblue = (Button) popupView.findViewById(R.id.DarkBlue);
		purple = (Button) popupView.findViewById(R.id.Purple);
		white.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "white";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				System.out.println("colorhex"+colorname);
				popupWindow.dismiss();
				
			}
		});
		
		gray.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "gray";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		black.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "black";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		pink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "pink";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		red.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "red";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		orange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "orange";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		yellow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "yellow";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		green.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "green";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		darkgreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "darkgreen";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		blue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "blue";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		darkblue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "darkblue";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		purple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				colorname = "purple";
				myColList.put(position,  colorname);
				typeedit.setText(colorname);
				popupWindow.dismiss();
			}
		});
		
		
	}
	
}