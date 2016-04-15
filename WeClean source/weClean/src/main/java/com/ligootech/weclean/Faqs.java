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
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Faqs extends SherlockActivity {
	TextView question, answer;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	JSONArray jversion = null;
	private static final String TAG_FAQARRAY = "faq";
	private static final String TAG_QUESTION = "question";
	private static final String TAG_ANSWER = "answer";

	private static final String TotalDue = "TotalDue";
	String Link, abouttext;
	private ProgressDialog pDialog;
	public static final int progress_bar_type = 0;
	JSONArray jimage = null;
	InputStream is = null;
	Boolean errfl = false;
	StringBuilder sb = null;
	String result = null;
	ArrayList<String> QUESTION = new ArrayList<String>();
	ArrayList<String> ANSWER = new ArrayList<String>();
	ListView listview;
	public static SlidingMenu slidingMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faqs);

		// configure the SlidingMenu
				System.out.println("mflow1");
				slidingMenu = new SlidingMenu(this);
				slidingMenu.setMode(SlidingMenu.LEFT);
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
				slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
				slidingMenu.setMenu(R.layout.slidemenu);

		listview = (ListView) findViewById(R.id.faq_listView1);

		Link = getResources().getString(R.string.faqslink);
		TalkToServer myTask = new TalkToServer();
		myTask.execute();
	}

	public class TalkToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
			
			Locale current = getResources().getConfiguration().locale;
			String Lang;
			if(current.equals(Locale.SIMPLIFIED_CHINESE) || current.equals(Locale.TRADITIONAL_CHINESE))
			{
				Lang="chinese";
			}
			else
			{
				Lang="english";	
			}
			nameValuePairs.add(new BasicNameValuePair("Lang",
					Lang));

		}

		protected void onProgressUpdate(String... progress) {
			// super.onProgressUpdate(values);
			pDialog.setProgress(Integer.parseInt(progress[0]));

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			httppost(Link);
			System.out.println("ooooLink");
			fetchOrderDeatails();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// dismissDialog(progress_bar_type);

			listview.setAdapter(new Faqs_custom(Faqs.this, QUESTION, ANSWER));
			try {
				dismissDialog(progress_bar_type);
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
				jversion = jsonObj.getJSONArray(TAG_FAQARRAY);
				System.out.println(" jversion  " + jversion);
				System.out.println(" jversion .length() " + jversion.length());

				try {
					// looping through All product items
					for (int itemlistidx = 0; itemlistidx < jversion.length(); itemlistidx++) {
						JSONObject c = jversion.getJSONObject(itemlistidx);
						System.out.println("flow");

						QUESTION.add(c.getString(TAG_QUESTION));
						ANSWER.add(c.getString(TAG_ANSWER));

						System.out.println("QUESTION" + QUESTION);
						System.out.println("ANSWER" + ANSWER);
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
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
			final Activity activity = Faqs.this;
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			slidingMenu.toggle();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
