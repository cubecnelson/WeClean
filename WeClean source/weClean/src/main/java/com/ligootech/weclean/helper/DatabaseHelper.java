package com.ligootech.weclean.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ligootech.weclean.model.District;

public class DatabaseHelper extends SQLiteOpenHelper{
	private static final String LOG = "DatabaseHelper";
	
	// Database Version
		private static final int DATABASE_VERSION = 1;

		// Database Name
		private static final String DATABASE_NAME = "wecleanligootech";

		// Table Names
		public static final String TABLE_DISTRICT = "District";
		
		
		// prescription Table - column names
		private static final String DISTRICT_ID = "districtid";
		private static final String DISTRICT = "district";
		
		// Prescription table create statement
		private static final String CREATE_TABLE_DISTRICT = "CREATE TABLE "
				+ TABLE_DISTRICT + "(" + DISTRICT_ID
				+ " INTEGER AUTO_INCREMENT," + DISTRICT + " TEXT" + ")";

		
		

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}




		@Override
		public void onCreate(SQLiteDatabase db) {
			
			// creating required tables
			db.execSQL(CREATE_TABLE_DISTRICT);
		}




		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// on upgrade drop older tables
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRICT);
			
		}
		/*
		 * Creating a prescription
		 */
		public long createdistricttable(District district) {
			SQLiteDatabase db = this.getWritableDatabase();
			

			ContentValues values = new ContentValues();
			values.put(DISTRICT_ID, district.getdistrictid());
			values.put(DISTRICT, district.getdistrict());

			// insert row
			long district_id = db.insert(TABLE_DISTRICT, null, values);

			return district_id;
		}

		/*
		 * get single prescription
		 */
		public District getdistrict(String district) {
			SQLiteDatabase db = this.getReadableDatabase();

			String selectQuery = "SELECT  * FROM " + TABLE_DISTRICT + " WHERE "
					+ DISTRICT + " = '" + district+"'";

			Log.e(LOG, selectQuery);

			Cursor c = db.rawQuery(selectQuery, null);

			if (c != null)
				c.moveToFirst();

			District td = new District();
			td.setdistrictid(c.getInt(c.getColumnIndex(DISTRICT_ID)));
			td.setdistrict((c.getString(c.getColumnIndex(DISTRICT))));
			

			return td;
		}
		
		/**
		 * getting all prescriptions
		 * */
		public List<District> getAlldistrict() {
			List<District> districts = new ArrayList<District>();
			String selectQuery = "SELECT  * FROM " + TABLE_DISTRICT;

			Log.e(LOG, selectQuery);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (c.moveToFirst()) {
				do {
					District td = new District();
					td.setdistrictid(c.getInt(c
							.getColumnIndex(DISTRICT_ID)));
					td.setdistrict((c.getString(c.getColumnIndex(DISTRICT))));
					

					// adding to prescription list
					districts.add(td);
				} while (c.moveToNext());
			}

			return districts;
		}

		/**
		 * /* getting prescription count
		 */
		public int getloginCount() {
			String countQuery = "SELECT  * FROM " + TABLE_DISTRICT;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);

			int count = cursor.getCount();
			cursor.close();

			// return count
			return count;
		}

		/*
		 * Updating a prescription
		 */
		public int updatelogin(District district) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DISTRICT, district.getdistrict());
		

			// updating row
			return db
					.update(TABLE_DISTRICT, values, DISTRICT_ID
							+ " = ?", new String[] { String.valueOf(district
							.getdistrictid()) });
		}

		/*
		 * Deleting a prescription
		 */
		public void deletelogin(long tado_id) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_DISTRICT, DISTRICT_ID + " = ?",
					new String[] { String.valueOf(tado_id) });
		}

		// ------------------------ "tags" table methods ----------------//
		public void deleteTableRows(String table) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("DELETE FROM " + table);
		}

		// closing database
		public void closeDB() {
			SQLiteDatabase db = this.getReadableDatabase();
			if (db != null && db.isOpen())
				db.close();
		}
}
		

