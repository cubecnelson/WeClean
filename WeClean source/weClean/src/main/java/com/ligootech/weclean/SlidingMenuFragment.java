/*******************************************************************************************************************
 * Author: @ligootech
 * Date: 30/07/2014
 * Version: Initial version
 * Main Functionality:
 * 
 * Program description:
 * 
 * Called Programs:
 * Calling Programs:
 * Modification History:
 * --------------------
 * Changed Date  Description
 *  
 */
package com.ligootech.weclean;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class SlidingMenuFragment extends Fragment implements
		ExpandableListView.OnGroupClickListener {

	public static ExpandableListView SlidemenuListView;
	public static int grouppositionclicked = 0;
	static SlidingListAdapter SlidemenuListAdapter;
	private Activity m;
	int width;
	DisplayMetrics metrics;

	List<Slidemenu> SlidemenuList;

	public SlidingMenuFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		m = this.getActivity();

		SlidemenuList = createMenu();
		
		View view = inflater.inflate(R.layout.slidingmenu_fragment, container,
				false);

		SlidemenuListView = (ExpandableListView) view
				.findViewById(R.id.slidingmenu_view);
		SlidemenuListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		System.out.println("SlidemenuList" + SlidemenuList.size());
		SlidemenuListAdapter = new SlidingListAdapter(this.getActivity(),
				SlidemenuList);
		SlidemenuListView.setAdapter(SlidemenuListAdapter);
		metrics = new DisplayMetrics();
		width = metrics.widthPixels;

		// this code for adjusting the group indicator into right side of the
		// view
		SlidemenuListView.setIndicatorBounds(700, 800);
		SlidemenuListView.setGroupIndicator(null);

		SlidemenuListView.setOnGroupClickListener(this);
		m = this.getActivity();
		// SlidemenuListView.setOnGroupClickListener((OnGroupClickListener)
		// this);
		int count = SlidemenuListAdapter.getGroupCount();
		System.out.println("pos count" + count);
		// for (int position = 0; position < count; position++) {
		// System.out.println("pos count" + position);
		// SlidemenuListView.expandGroup(position);
		// }
		// SlidemenuListView.setTranscriptMode(ExpandableListView.TRANSCRIPT_MODE_DISABLED);

		return view;
	}

	public int GetDipsFromPixel(float pixels) {

		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 5.5f);
	}

	private List<Slidemenu> createMenu() {
		List<Slidemenu> SlidemenuList = new ArrayList<Slidemenu>();

		Slidemenu menu2 = new Slidemenu(m.getResources().getString(R.string.PlaceanOrder));

		SlidemenuList.add(menu2);

		Slidemenu menu3 = new Slidemenu(m.getResources().getString(R.string.OrderList));

		SlidemenuList.add(menu3);

		Slidemenu menu5 = new Slidemenu(m.getResources().getString(R.string.AccountSetting));

		SlidemenuList.add(menu5);
	
		return SlidemenuList;
	}

	@Override
	public void onResume() {

		super.onResume();

	}

	@Override
	public void onPause() {

		super.onPause();

	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		System.out.println("inside groupclick" + groupPosition);

		final int groupCount = parent.getExpandableListAdapter()
				.getGroupCount();

		 if (groupPosition == 0) {
			Customer_History.slidingMenu.toggle();
			Intent i = new Intent(v.getContext(), Customer_SchedulePickup.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		} else if (groupPosition == 1) // check if groupcount not
		// equal
		// groupposition clicked
		{
			Customer_History.slidingMenu.toggle();
			Intent i = new Intent(v.getContext(), Customer_History.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		} else if (groupPosition == 2) {
			Customer_History.slidingMenu.toggle();
			Intent i = new Intent(v.getContext(), AccountSetting.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);

		}

		// MainActivity.slidingMenu.toggle();
		return false;
	}

}