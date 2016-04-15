package com.ligootech.weclean;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Faqs_custom extends BaseAdapter{
	
	
	ArrayList<String> QUESTION;
	ArrayList<String> ANSWER;
	
	private Context mContext;
	private static LayoutInflater inflater = null;

	public Faqs_custom( Context c,ArrayList<String> QUESTION,
			ArrayList<String> ANSWER) {
		mContext = c;
		this.QUESTION = QUESTION;
		this.ANSWER = ANSWER;
		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return QUESTION.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		return QUESTION.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.faqs_custom, null);
		TextView question = (TextView) vi.findViewById(R.id.question_textView2);
		TextView answer = (TextView) vi.findViewById(R.id.answer_textView4);
		
		question.setText(QUESTION.get(position));
		answer.setText(ANSWER.get(position));
		
		return vi;

	}

}
