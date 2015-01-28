package com.androidweatherapp.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;

import com.androidweatherapp.controller.abstracts.ForecastDialogInterface;
import com.androidweatherapp.controller.abstracts.WeatherForecastAbstract;
import com.androidweatherapp.controller.abstracts.WeatherForecastInterface;
import com.androidweatherapp.controller.factory.WeatherForecastFactory;
import com.androidweatherapp.model.database.Database;
import com.androidweatherapp.model.model.Setting;
import com.androidweatherapp.view.R;
import com.androidweatherapp.view.SettingActivity;
import com.androidweatherapp.view.WeatherForecastActivity;

public class Controller {

	private static Controller instance;
	private Setting setting = null;
	private Database weatherDatabase = null;
	private Context context = null;
	private AlertDialog dialog = null;
	private Intent intent = null;
	private ForecastDialogInterface forecastDialogInterface = null;

	private Controller(Context context) {
		weatherDatabase = new Database(context);
		this.context = context;
	}

	/**
	 * This method takes no parameter and double checked locking principle is
	 * used. In this approach, the synchronized block is used inside if
	 * condition with an additional check to ensure that only one instance of
	 * singleton class is created.
	 * 
	 * @return Controller
	 */

	public static Controller getInstanceUsingDoubleLocking(Context context) {
		if (instance == null) {
			synchronized (Controller.class) {
				if (instance == null) {
					instance = new Controller(context);
				}
			}
		}
		return instance;
	}

	public Setting getSettings() {
		try {
			return weatherDatabase.getSettings();
		} catch (Exception e) {
			Log.d("Error HERE", e.getMessage());
		}
		return setting;
	}

	public void getWeatherForecast(String type, String city,
			WeatherForecastInterface weatherForecastInterface, int numOfDay) {
		WeatherForecastAbstract forecast = WeatherForecastFactory.getForecast(
				context, type, weatherForecastInterface, city, numOfDay);

		Log.d("Number of Days: ", ""+numOfDay);

		forecast.execute();
	}

	public void setAction(String action) {
		if (action.equals("about")) {
			openAbout();
		}
		if (action.equals("settings")) {
			openSettings();
		}
	}

	public void addCity(String city) {
		weatherDatabase.addCity(city);
	}

	public void removeCity(String city) {
		weatherDatabase.removeCity(city);
	}

	public void updateCheckBox(String val) {
		weatherDatabase.updateCheckBox(val);
	}

	public void updateDay(String val) {
		weatherDatabase.updateForcastDay(val);
	}

	public void reset() {
		weatherDatabase.resetDefaultValues();
	}

	private void openAbout() {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setTitle("About App Version 1.0");
		builder.setIcon(R.drawable.ic_about);
		builder.setMessage("Android Weather App helps people get information"
				+ " about weather forecast.Android Weather App application allows"
				+ " users to select a city from a list of cities and view weather"
				+ " forecast for the selected city.");

		builder.setPositiveButton("Close",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		dialog = builder.create();

		dialog.getWindow().getAttributes().windowAnimations =

		R.style.DialogAnimation;

		dialog.show();
	}

	private void openSettings() {

		intent = new Intent(context, SettingActivity.class);
		context.startActivity(intent);
	}

	public void displayAlertDialog(ForecastDialogInterface f) {

		this.forecastDialogInterface = f;

		new AlertDialog.Builder(context).setTitle("Really Exit?")
				.setMessage("Are you sure you want to exit?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes, new OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						forecastDialogInterface.warn(false);
					}
				}).create().show();
	}

	public void viewWeatherForcastFor(String selectedCity) {
		intent = new Intent(context, WeatherForecastActivity.class);
		intent.putExtra("city", selectedCity);
		context.startActivity(intent);
	}
}
