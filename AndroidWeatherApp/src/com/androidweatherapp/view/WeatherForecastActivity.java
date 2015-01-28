package com.androidweatherapp.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidweatherapp.controller.Controller;
import com.androidweatherapp.controller.abstracts.WeatherForecastInterface;
import com.androidweatherapp.model.adapter.WeatherForecastAdapter;
import com.androidweatherapp.model.model.CurrentCondition;
import com.androidweatherapp.model.model.WeatherForecast;
import com.squareup.picasso.Picasso;

public class WeatherForecastActivity extends Activity implements
		WeatherForecastInterface {

	private Controller controller = null;
	private AlertDialog dialog = null;
	private ProgressDialog progressDialog = null;
	private ListView forecastResultList = null;
	private ArrayList<WeatherForecast> myWeatherForecasts = new ArrayList<WeatherForecast>();
	private ArrayAdapter<WeatherForecast> adapter = null;
	private String cityName = "";
	private Button refresh = null;

	private ImageView currentConditionForecastIcon = null;
	private TextView currentConditionForecastCity = null,
			currentConditionForecastTemp = null,
			currentConditionForecastDescription = null,
			currentConditionForecastObservationTime = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forecast);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		initializeValues();

		cityName = getIntent().getExtras().getString("city");

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
				Integer.parseInt(controller.getSettings().getForecastDay()));
	}

	private void initializeValues() {
		controller = Controller
				.getInstanceUsingDoubleLocking(WeatherForecastActivity.this);
		forecastResultList = (ListView) this
				.findViewById(R.id.forecastListView);

		refresh = (Button) this.findViewById(R.id.refreshBtn);

		currentConditionForecastIcon = (ImageView) this
				.findViewById(R.id.forecastCurrentIcon);
		currentConditionForecastCity = (TextView) this
				.findViewById(R.id.forecastCurrentCity);
		currentConditionForecastTemp = (TextView) this
				.findViewById(R.id.forecastCurrentTemp);
		currentConditionForecastDescription = (TextView) this
				.findViewById(R.id.forecastCurrentDescription);
		currentConditionForecastObservationTime = (TextView) this
				.findViewById(R.id.forecastCurrentObservationTime);

		adapter = new WeatherForecastAdapter(WeatherForecastActivity.this,
				myWeatherForecasts, controller);
		forecastResultList.setAdapter(adapter);
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

		if (!myWeatherForecasts.isEmpty()) {
			myWeatherForecasts.clear();
		}

		try {
			CurrentCondition currentCondition = weatherForecasts.get(0)
					.getCurrentCondition();

			if (currentCondition != null) {

				if (controller.getSettings().getShowIcon().equals("true")) {
					Picasso.with(WeatherForecastActivity.this)
							.load(currentCondition.getForecastIconURL())
							.into(currentConditionForecastIcon);
				} else {
					currentConditionForecastIcon.setImageBitmap(null);
				}

				currentConditionForecastCity.setText(currentCondition
						.getForecastCity());
				currentConditionForecastTemp.setText(currentCondition
						.getForecastCelcius()
						+ "° C "
						+ currentCondition.getForecastFahrenheight() + "° F");
				currentConditionForecastDescription.setText(currentCondition
						.getForecastDescription());
				currentConditionForecastObservationTime
						.setText("Observation Time: "
								+ currentCondition.getObservationTime());

			}
		} catch (Exception e) {

		}

		for (WeatherForecast wf : weatherForecasts) {
			myWeatherForecasts.add(wf);
			adapter.notifyDataSetChanged();
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
}