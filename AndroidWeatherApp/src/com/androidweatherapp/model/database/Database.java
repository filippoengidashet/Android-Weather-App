package com.androidweatherapp.model.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.androidweatherapp.model.model.Setting;

public class Database extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "WeatherForecast.db";
	private static final String CITY_TABLE_NAME = "cities";
	private static final String SETTING_TABLE_NAME = "settings";
	private static final int DATABASE_VERSION = 8;
	private Setting setting = null;
	private SQLiteDatabase database = null;

	private static final String CREATE_CITIES_TABLE = "CREATE TABLE IF NOT EXISTS `"
			+ CITY_TABLE_NAME
			+ "` ("
			+ "`city` varchar(100) DEFAULT NULL,"
			+ "`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);";

	private static final String CREATE_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS `"
			+ SETTING_TABLE_NAME
			+ "` ("
			+ "`forecastDayNumber` varchar(100) DEFAULT NULL,"
			+ "`showIcon` varchar(100) DEFAULT NULL,"
			+ "`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.database = this.getWritableDatabase();
	}

	public void onCreate(SQLiteDatabase db) {

		Log.d("STATUS", "Oncreate");

		try {
			db.execSQL(CREATE_CITIES_TABLE);
			db.execSQL("INSERT INTO " + CITY_TABLE_NAME
					+ "(city) values ('Dublin')");
			db.execSQL("INSERT INTO " + CITY_TABLE_NAME
					+ "(city) values ('London')");
			db.execSQL("INSERT INTO " + CITY_TABLE_NAME
					+ "(city) values ('New York')");
			db.execSQL("INSERT INTO " + CITY_TABLE_NAME
					+ "(city) values ('Barcelona')");

			db.execSQL(CREATE_SETTINGS_TABLE);
			db.execSQL("INSERT INTO " + SETTING_TABLE_NAME
					+ "(forecastDayNumber, showIcon) values ('5', 'true')");
		} catch (SQLException msg) {
			Log.d("SQL ERROR", msg.getMessage());
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + SETTING_TABLE_NAME);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	public Setting getSettings() throws SQLException {

		Log.d("STATUS", "Inside getSetting");

		setting = new Setting();
		ArrayList<String> cityList = getCityList();
		setting.setCities(cityList);

		Cursor cur = database.query(true, SETTING_TABLE_NAME, new String[] {
				"forecastDayNumber", "showIcon" }, null, null, null, null,
				null, null);

		if (cur.moveToNext()) {
			setting.setForecastDay(cur.getString(cur
					.getColumnIndex("forecastDayNumber")));
			setting.setShowIcon(cur.getString(cur.getColumnIndex("showIcon")));
		}

		return setting;
	}

	private ArrayList<String> getCityList() throws SQLException {

		Cursor cur = database.query(true, CITY_TABLE_NAME,
				new String[] { "city" }, null, null, null, null, null, null);

		ArrayList<String> cityList = new ArrayList<String>();
		String city = "";
		while (cur.moveToNext()) {
			city = cur.getString(cur.getColumnIndex("city"));
			cityList.add(city);
		}
		return cityList;
	}

	public boolean addCity(String city) {
		boolean bool = false;

		database.execSQL("INSERT INTO " + CITY_TABLE_NAME + "(city) values ('"
				+ city + "')");

		return bool;
	}

	public void removeCity(String city) {

		database.execSQL("DELETE FROM " + CITY_TABLE_NAME + " WHERE city = '"
				+ city + "'");
	}

	public void updateCheckBox(String bool) {
		database.execSQL("UPDATE " + SETTING_TABLE_NAME + " SET showIcon = '"
				+ bool + "'");
	}
	
	public void updateForcastDay(String day) {
		database.execSQL("UPDATE " + SETTING_TABLE_NAME + " SET forecastDayNumber = '"
				+ day + "'");
	}
	
	public void resetDefaultValues() {
		database.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS " + SETTING_TABLE_NAME);
		onCreate(database);
	}

}