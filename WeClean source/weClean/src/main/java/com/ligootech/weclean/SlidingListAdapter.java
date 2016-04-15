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

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlidingListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private LayoutInflater inflater;

	List<Slidemenu> Slidingmenus;

	public SlidingListAdapter(Context context, List<Slidemenu> Slidingmenus) {
		this.Slidingmenus = Slidingmenus;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return Slidingmenus.get(groupPosition).getSlidemenuItems()
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return Slidingmenus.get(groupPosition).getSlidemenuItems()
				.get(childPosition).getId();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return Slidingmenus.get(groupPosition).getSlidemenuItems().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return Slidingmenus.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return Slidingmenus.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.slidingmenu_sectionview,
					parent, false);
		}

		TextView imgView = (TextView) convertView
				.findViewById(R.id.slidingmenu_section_title);
		imgView.setText(((Slidemenu) getGroup(groupPosition)).getTitle());
		if (groupPosition == 0) {
			ImageView image = (ImageView) convertView
					.findViewById(R.id.slidingmenu_section_icon);
			image.setImageResource(R.drawable.sideorder);
		}
		else if (groupPosition == 1) {
			ImageView image = (ImageView) convertView
					.findViewById(R.id.slidingmenu_section_icon);
			image.setImageResource(R.drawable.sideorderlist);
		}
		else if (groupPosition == 2) {
			ImageView image = (ImageView) convertView
					.findViewById(R.id.slidingmenu_section_icon);
			image.setImageResource(R.drawable.sidsetting);
		}
		// imgView.setText("menu4");

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.slidingmenu_sectionitem,
					parent, false);
		}
		View divider = (View) convertView.findViewById(R.id.divider);

		SlidemenuItem oSlidingmenuItem = this.Slidingmenus.get(groupPosition)
				.getSlidemenuItems().get(childPosition);
		RelativeLayout Rlsidemenumainitem = (RelativeLayout) convertView
				.findViewById(R.id.sidemenumainitem);

		TextView textlabel = (TextView) convertView
				.findViewById(R.id.slidingmenu_sectionitem_label);
		textlabel.setText(oSlidingmenuItem.getTitle());

		final ImageView itemIcon = (ImageView) convertView
				.findViewById(R.id.slidingmenu_sectionitem_icon);
		System.out.println("checked counte"
				+ SlidingMenuFragment.SlidemenuListView
						.getCheckedItemPosition() + " " + childPosition);

		return convertView;
	}

	public static Drawable getDrawableByName(String name, Context context) {
		int drawableResource = context.getResources().getIdentifier(name,
				"drawable", context.getPackageName());
		if (drawableResource == 0) {
			throw new RuntimeException("Can't find drawable with name: " + name);
		}
		return context.getResources().getDrawable(drawableResource);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
}
