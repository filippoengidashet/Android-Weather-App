package com.androidweatherapp.view;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidweatherapp.controller.Controller;
import com.androidweatherapp.controller.abstracts.WeatherForecastInterface;
import com.androidweatherapp.controller.service.ForecastHandlerService;
import com.androidweatherapp.model.model.WeatherForecast;
import com.androidweatherapp.model.utilities.Utils;
import com.squareup.picasso.Picasso;

/**
 * MainActivity.java:
 * 
 * Purpose: To Display the basic Main Screen
 * 
 * @author Filippo Engidashet
 * @version 1.0
 * @since 2015-01-25
 */

public class MainActivity extends Activity implements WeatherForecastInterface {

	private Button retrieveBtn = null;
	private Controller controller = null;
	private Spinner cityListSpinner = null;
	private ArrayAdapter<String> adapter = null;
	private ForecastCheckReciver netReceiver = null;
	private Intent forecastIntent = null;
	private ImageView forecastIcon = null;
	private TextView homeWeatherStatusTxt, homeWeatherCelTxt,
			homeWeatherFahTxt, homeWeatherCityTxt, homeObservationTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initialize();

		netReceiver = new ForecastCheckReciver();

		IntentFilter filter = new IntentFilter();
		filter.addAction("statusUpdate");
		registerReceiver(netReceiver, filter);

		forecastIntent = new Intent(getApplicationContext(),
				ForecastHandlerService.class);
		startService(forecastIntent);

		retrieveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (Utils.isNetworkAvailable(MainActivity.this)) {
					String selectedCity = cityListSpinner.getSelectedItem()
							.toString();

					controller.viewWeatherForcastFor(selectedCity);
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Sorry, can't get the Weather Forecast Information! No Network Available Error Code - 1 ",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	private void initialize() {
		controller = Controller
				.getInstanceUsingDoubleLocking(MainActivity.this);
		retrieveBtn = (Button) this.findViewById(R.id.homeRetrieveBtn);
		cityListSpinner = (Spinner) this.findViewById(R.id.settingDaysSpinner);

		forecastIcon = (ImageView) this.findViewById(R.id.homeWeatherIconImg);
		homeWeatherStatusTxt = (TextView) this
				.findViewById(R.id.homeWeatherStatusTxt);
		homeWeatherCelTxt = (TextView) this
				.findViewById(R.id.homeWeatherCelTxt);
		homeWeatherFahTxt = (TextView) this
				.findViewById(R.id.homeWeatherFahTxt);
		homeWeatherCityTxt = (TextView) this
				.findViewById(R.id.homeWeatherCityTxt);
		homeObservationTxt = (TextView) this
				.findViewById(R.id.homeObservationTxt);

		try {
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, controller
							.getSettings().getCities());
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			cityListSpinner.setAdapter(adapter);

		} catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			controller.setAction("settings");
			return true;
		case R.id.action_about:
			controller.setAction("about");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void success(ArrayList<WeatherForecast> weatherForecasts) {

		Log.d("Success", "Sucess");
		try {

			// if (controller.getSettings().getShowIcon().equals("true")) {
			// Picasso.with(MainActivity.this)
			// .load(weatherForecast.getIconURL()).into(forecastIcon);
			// } else {
			// forecastIcon.setImageBitmap(null);
			// }
			WeatherForecast weatherForecast = weatherForecasts.get(0);

			Picasso.with(MainActivity.this).load(weatherForecast.getIconURL())
					.into(forecastIcon);

			homeWeatherStatusTxt.setText(weatherForecast
					.getForecastDescription());
			homeWeatherCelTxt.setText(weatherForecast
					.getMaxCelciusTemperature() + "° C");
			homeWeatherFahTxt.setText(weatherForecast
					.getMaxFahrenheightTemperature() + "° F");

			String location = weatherForecast.getForecastLocation();

			StringTokenizer st = new StringTokenizer(location);
			boolean bool = false;
			String flag = "";

			double latitude = 0.0;
			double longitude = 0.0;

			while (st.hasMoreTokens()) {

				String temp = st.nextToken();

				if (bool) {
					if (flag.equalsIgnoreCase("Lat")) {
						latitude = Double.parseDouble(temp);
					} else {
						longitude = Double.parseDouble(temp);
					}
					bool = false;
				}

				if (temp.equalsIgnoreCase("Lat")) {
					bool = true;
					flag = "Lat";
				}
				if (temp.equalsIgnoreCase("Lon")) {
					bool = true;
					flag = "Lon";
				}
			}

			String address = Utils.getAddress(getApplicationContext(),
					latitude, longitude);

			homeWeatherCityTxt.setText(address);
			homeObservationTxt.setText("Observation Time: "
					+ weatherForecast.getObservationTime());

		} catch (Exception e) {
			Log.e("error", e.toString());
		}

	}

	@Override
	public void error(String errorMsg) {

		Log.d("Error", "error: " + errorMsg);

		Toast.makeText(getApplicationContext(),
				"Oops! Something went wrong!!! " + errorMsg, Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public Context getWeatherForecastContext() {
		return getApplicationContext();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshCityList();
	}

	private void refreshCityList() {
		adapter.clear();
		try {
			try {
				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, controller
								.getSettings().getCities());
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				cityListSpinner.setAdapter(adapter);
				adapter.notifyDataSetChanged();

			} catch (Exception e) {
			}

		} catch (Exception e) {
		}
	}

	class ForecastCheckReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			try {

				if (Utils.isNetworkAvailable(MainActivity.this)) {

					controller.getWeatherForecast("current", cityListSpinner
							.getSelectedItem().toString(), MainActivity.this,
							Integer.parseInt(controller.getSettings()
									.getForecastDay()));

				} else {
					Toast.makeText(
							getApplicationContext(),
							"Sorry, can't get the Weather Forecast Information! No Network Available Error Code - 1 ",
							Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				Log.e("error", e.toString());
			}

		}
	}
}
