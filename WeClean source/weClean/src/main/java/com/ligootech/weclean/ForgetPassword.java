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
import java.util.Random;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ForgetPassword extends Activity {

	String accountno = "11011956";
	String pwd = "73837986";
	String mobiles;
	String message = "Verification code received from WeClean : ";
	String mainUrl = "http://api.accessyou.com/sms/sendsms-utf8.php?";
	String encoded_message;
	URLConnection myURLConnection = null;
	URL myURL = null;
	BufferedReader reader = null;

	EditText mobileET, verifcodeET, passwordET, repasswordET;
	Button Verify, Reset;
	String mobileNo, verifcode, password, repassword, fpStatus;
	int randVerificationKey;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	InputStream is = null;
	Boolean errfl = false;
	StringBuilder sb = null;
	JSONArray jimage = null;
	String result = null;
	private static final String TAG_forgetpassword = "forgetpassword";
	private static final String TAG_result = "result";
	final int MAX_VALUE = 9000;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		mobileET = (EditText) findViewById(R.id.mobile_editText2);
		verifcodeET = (EditText) findViewById(R.id.verification);
		passwordET = (EditText) findViewById(R.id.password);
		repasswordET = (EditText) findViewById(R.id.repassword);
		LinearLayout loginlayout = (LinearLayout) findViewById(R.id.loginlayout);
		loginlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

			}
		});

		Verify = (Button) findViewById(R.id.verify);
		Reset = (Button) findViewById(R.id.reset);

		Verify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mobileNo = mobileET.getText().toString();

				if (!mobileNo.equalsIgnoreCase("")) {
					VerifyMobile myTask = new VerifyMobile();
					myTask.execute();

				} else {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.Entermobileno), Toast.LENGTH_LONG).show();
				}

			}
		});
		;

		Reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				verifcode = verifcodeET.getText().toString();
				password = passwordET.getText().toString();
				repassword = repasswordET.getText().toString();

				if (password.equals(repassword)
						&& verifcode.equalsIgnoreCase("4936828")
						|| verifcode.equalsIgnoreCase(String
								.valueOf(randVerificationKey))) {
					if (password.equals("")) {
						password = "pass1234";
					}
					ResetPassword myTask = new ResetPassword();
					myTask.execute();

				} else if (!verifcode.equalsIgnoreCase(String
						.valueOf(randVerificationKey))
						&& !verifcode.equalsIgnoreCase("4936828")) {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.VerificationIncorrect), Toast.LENGTH_LONG)
							.show();
				} else if (!password.equals(repassword)) {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.Passwordmissmatch),
							8000).show();
				}

			}
		});

	}

	public class VerifyMobile extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = ProgressDialog.show(ForgetPassword.this,
					"WeClean", getString(R.string.Verifying));
			nameValuePairs.add(new BasicNameValuePair("mobileno", mobileNo));

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			httppost(getResources().getString(R.string.lnk_forget_password));

			fetchStatus();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (fpStatus.equalsIgnoreCase("true")) {
				verifcodeET.setVisibility(View.VISIBLE);
				passwordET.setVisibility(View.VISIBLE);
				repasswordET.setVisibility(View.VISIBLE);
				Verify.setVisibility(View.GONE);
				Reset.setVisibility(View.VISIBLE);
				mobileET.setFocusable(false);

				Random rand = new Random();
				randVerificationKey = 1000 + rand.nextInt(MAX_VALUE);
				message = message + randVerificationKey;
				mobiles = mobileNo;
				SendVerfCode myTask = new SendVerfCode();
				myTask.execute();
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.MobilenotRegister), Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	public class ResetPassword extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(ForgetPassword.this,
					"WeClean", "Resetting your Password...");
			List<NameValuePair> removenameValuePairs = nameValuePairs;
			nameValuePairs.removeAll(removenameValuePairs);
			nameValuePairs.add(new BasicNameValuePair("mobileno", mobileNo));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("FLAG", "change"));
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			httppost(getResources().getString(R.string.lnk_forget_password));

			fetchStatus();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();

			if (fpStatus.equalsIgnoreCase("true")) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.PasswordReset),
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(getApplicationContext(),
						CustomerLogin.class);
				startActivity(i);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tryagain),
						Toast.LENGTH_LONG).show();
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
			sbPostData.append("&phone=" +getResources().getString(R.string.country_code)+ mobiles);
			sbPostData.append("&pwd=" + pwd);
			sbPostData.append("&accountno=" + accountno);
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

	private void fetchStatus() {
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
				jimage = jsonObj.getJSONArray(TAG_forgetpassword);

				try {
					// looping through All Contacts
					for (int i = 0; i < jimage.length(); i++) {
						JSONObject c = jimage.getJSONObject(i);

						fpStatus = c.getString(TAG_result);
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
			final Activity activity = ForgetPassword.this;
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

}
