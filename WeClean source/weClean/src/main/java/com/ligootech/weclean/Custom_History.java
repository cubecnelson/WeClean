/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 10/09/2014
 * Version: Initial version
 * Main Functionality:This Custom Adapter for Customer_History.java
 * 
 * Program description:
 * 1.This Custom Adapter for Customer_History.java
 * Called Programs:Customer_History.java
 * Calling Programs:
 * Modification History:
 * --------------------
 * Changed Date  Description
 *  
 */
package com.ligootech.weclean;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Custom_History extends BaseAdapter {
	ArrayList<String> ORDERID;
	ArrayList<String> TIME;
	ArrayList<String> STATUS;
	ArrayList<String> PAY_STATUS;
	Context context;
	private Context mContext;
	private static LayoutInflater inflater = null;

	public Custom_History(Context c, ArrayList<String> ORDERID,
			ArrayList<String> STATUS, ArrayList<String> PAY_STATUS,
			ArrayList<String> TIME) {

		mContext = c;

		this.ORDERID = ORDERID;
		this.STATUS = STATUS;
		this.PAY_STATUS = PAY_STATUS;
		this.TIME = TIME;
		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ORDERID.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ORDERID.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.custom_orderstatus, null);

		TextView orderid = (TextView) vi.findViewById(R.id.orderlist_textView1);
		TextView status = (TextView) vi.findViewById(R.id.complete_textView2);
		TextView paymentstatus = (TextView) vi
				.findViewById(R.id.deliver_textView3);
		TextView time = (TextView) vi.findViewById(R.id.timetextView1);

		orderid.setText(ORDERID.get(position));
		if (STATUS.get(position).equalsIgnoreCase("waiting") || STATUS.get(position).equalsIgnoreCase(mContext.getString(R.string.cwaiting))) {
			status.setTextColor(Color.parseColor("#ffff0000"));

		}
		else if (STATUS.get(position).equalsIgnoreCase("picked up") || STATUS.get(position).equalsIgnoreCase(mContext.getString(R.string.cpickedup))) {
			status.setTextColor(Color.parseColor("#fea120"));

		}
		else if (STATUS.get(position).equalsIgnoreCase("delivered")|| STATUS.get(position).equalsIgnoreCase(mContext.getString(R.string.cdelivered))) {
			status.setTextColor(Color.parseColor("#00a11f"));

		}
		else if (STATUS.get(position).equalsIgnoreCase("completed")|| STATUS.get(position).equalsIgnoreCase(mContext.getString(R.string.ccompleted))) {
			status.setTextColor(Color.parseColor("#a1ffb3"));

		}
		
		Locale current = mContext.getResources().getConfiguration().locale;
		if(current.equals(Locale.SIMPLIFIED_CHINESE) || current.equals(Locale.TRADITIONAL_CHINESE))
		{
		STATUS.set(position,STATUS.get(position).replace("waiting",mContext.getString(R.string.cwaiting)));
		STATUS.set(position,STATUS.get(position).replace("picked up",mContext.getString(R.string.cpickedup)));
		STATUS.set(position,STATUS.get(position).replace("completed",mContext.getString(R.string.ccompleted)));
		STATUS.set(position,STATUS.get(position).replace("delivered",mContext.getString(R.string.cdelivered)));
		
		}

		status.setText(STATUS.get(position));
	
		if (PAY_STATUS.get(position).equalsIgnoreCase("not paid") || PAY_STATUS.get(position).equalsIgnoreCase(mContext.getString(R.string.notpaid))) {
			paymentstatus.setTextColor(Color.parseColor("#ffff0000"));
			System.out.println("PAY_STATUS.get(position)"
					+ PAY_STATUS.get(position));

		}
		else if (PAY_STATUS.get(position).equalsIgnoreCase("paid")|| PAY_STATUS.get(position).equalsIgnoreCase(mContext.getString(R.string.paid))) {
			paymentstatus.setTextColor(Color.parseColor("#00a11f"));
			System.out.println("PAY_STATUS.get(position)"
					+ PAY_STATUS.get(position));

		}
		
		
		if(current.equals(Locale.SIMPLIFIED_CHINESE) || current.equals(Locale.TRADITIONAL_CHINESE))
		{
			PAY_STATUS.set(position,PAY_STATUS.get(position).replace("not paid",mContext.getString(R.string.notpaid)));
			PAY_STATUS.set(position,PAY_STATUS.get(position).replace("paid",mContext.getString(R.string.paid)));
		
		}

		paymentstatus.setText(PAY_STATUS.get(position));
		time.setText(TIME.get(position));
		System.out.println("TIME.get(position)" + TIME.get(position));
		return vi;
		
	
			
			
		

	}

	

}
