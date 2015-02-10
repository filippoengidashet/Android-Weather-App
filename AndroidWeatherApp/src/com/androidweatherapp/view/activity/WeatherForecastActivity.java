package com.androidweatherapp.view.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.androidweatherapp.controller.Controller;
import com.androidweatherapp.controller.abstracts.WeatherForecastInterface;
import com.androidweatherapp.model.model.Setting;
import com.androidweatherapp.model.model.WeatherForecast;
import com.androidweatherapp.view.R;
import com.androidweatherapp.view.fragment.WeatherForecastBarChartFragment;
import com.androidweatherapp.view.fragment.WeatherForecastListViewFragment;

public class WeatherForecastActivity extends Activity implements
		WeatherForecastInterface {

	private Controller controller = null;
	private AlertDialog dialog = null;
	private ProgressDialog progressDialog = null;
	private String cityName = "";
	private Button refresh = null;

	private Setting settings = null;
	private FragmentTransaction fragmentTransaction = null;

	private ArrayList<WeatherForecast> forecasts = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forecast);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		initializeValues();

		cityName = getIntent().getExtras().getString("city");
		settings = controller.getSettings();

		fetchWeatherForecasts();

		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				fetchWeatherForecasts();
			}
		});

	}

	private void getForecastInformation() {
		controller.getWeatherForecast("city", cityName,
				WeatherForecastActivity.this,
				Integer.parseInt(settings.getForecastDay()));
	}

	private void initializeValues() {

		controller = Controller
				.getInstanceUsingDoubleLocking(WeatherForecastActivity.this);

		refresh = (Button) this.findViewById(R.id.refreshBtn);

	}

	private void fetchWeatherForecasts() {

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Please Wait");
		progressDialog.setMessage("Retrieving Weather Forecast...");
		progressDialog.setIndeterminate(false);
		progressDialog.setMax(100);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.show();

		getForecastInformation();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			WeatherForecastActivity.super.onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		warn();
	}

	private void warn() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Really Exit?");
		builder.setIcon(R.drawable.ic_warning);

		builder.setMessage("Are you sure you want to exit?");

		builder.setNegativeButton("Cancel", null);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				WeatherForecastActivity.super.onBackPressed();
			}
		});

		dialog = builder.create();

		dialog.getWindow().getAttributes().windowAnimations =

		R.style.DialogAnimation;

		dialog.show();

	}

	@Override
	public void success(ArrayList<WeatherForecast> weatherForecasts) {
		// TODO Auto-generated method stub

		setForecasts(weatherForecasts);

		fragmentTransaction = getFragmentManager().beginTransaction();

		if (settings.getDisplayType().equalsIgnoreCase("ListView")) {
			fragmentTransaction.setCustomAnimations(
					R.animator.enter, R.animator.exit);
			fragmentTransaction.replace(R.id.content,
					new WeatherForecastListViewFragment());

			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		} else if (settings.getDisplayType().equalsIgnoreCase("BarChart")) {
			fragmentTransaction.setCustomAnimations(
					R.animator.enter, R.animator.exit);
			fragmentTransaction.replace(R.id.content,
					new WeatherForecastBarChartFragment());
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		} else {
			Toast.makeText(getApplicationContext(),
					"Please Select Display Option in Settings!",
					Toast.LENGTH_LONG).show();
		}
		progressDialog.dismiss();

	}

	@Override
	public void error(String errorMsg) {

		progressDialog.dismiss();

		Toast.makeText(getApplicationContext(),
				"Sorry Can't complete the selected option! " + errorMsg,
				Toast.LENGTH_LONG).show();

	}

	@Override
	public Context getWeatherForecastContext() {
		// TODO Auto-generated method stub
		return getApplicationContext();
	}

	public ArrayList<WeatherForecast> getForecasts() {
		return forecasts;
	}

	public void setForecasts(ArrayList<WeatherForecast> forecasts) {
		this.forecasts = forecasts;
	}
}