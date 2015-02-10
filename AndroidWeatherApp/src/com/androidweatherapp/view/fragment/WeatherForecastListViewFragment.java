package com.androidweatherapp.view.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidweatherapp.controller.Controller;
import com.androidweatherapp.model.adapter.WeatherForecastAdapter;
import com.androidweatherapp.model.model.CurrentCondition;
import com.androidweatherapp.model.model.WeatherForecast;
import com.androidweatherapp.view.R;
import com.androidweatherapp.view.activity.WeatherForecastActivity;
import com.squareup.picasso.Picasso;

public class WeatherForecastListViewFragment extends Fragment {

	private View view = null;

	private Controller controller = null;

	private ListView forecastResultList = null;
	private ArrayList<WeatherForecast> myWeatherForecasts = new ArrayList<WeatherForecast>();
	private ArrayAdapter<WeatherForecast> adapter = null;

	private ImageView currentConditionForecastIcon = null;
	private TextView currentConditionForecastCity = null,
			currentConditionForecastTemp = null,
			currentConditionForecastDescription = null,
			currentConditionForecastObservationTime = null;

	private WeatherForecastActivity parentActivity = null;
	private ArrayList<WeatherForecast> weatherForecasts = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		parentActivity = (WeatherForecastActivity) getActivity();

		view = inflater.inflate(R.layout.fragment_forecast_list, container, false);

		initializeValue();

		weatherForecasts = parentActivity.getForecasts();

		displayResult(weatherForecasts);

		return view;
	}

	private void initializeValue() {

		controller = Controller.getInstanceUsingDoubleLocking(getActivity());
		forecastResultList = (ListView) view
				.findViewById(R.id.forecastListView);

		currentConditionForecastIcon = (ImageView) view
				.findViewById(R.id.forecastCurrentIcon);
		currentConditionForecastCity = (TextView) view
				.findViewById(R.id.forecastCurrentCity);
		currentConditionForecastTemp = (TextView) view
				.findViewById(R.id.forecastCurrentTemp);
		currentConditionForecastDescription = (TextView) view
				.findViewById(R.id.forecastCurrentDescription);
		currentConditionForecastObservationTime = (TextView) view
				.findViewById(R.id.forecastCurrentObservationTime);

		adapter = new WeatherForecastAdapter(getActivity(), myWeatherForecasts,
				controller);
		forecastResultList.setAdapter(adapter);
	}

	public void displayResult(ArrayList<WeatherForecast> weatherForecasts) {

		if (!myWeatherForecasts.isEmpty()) {
			myWeatherForecasts.clear();
		}

		try {
			CurrentCondition currentCondition = weatherForecasts.get(0)
					.getCurrentCondition();

			if (currentCondition != null) {

				if (controller.getSettings().getShowIcon().equals("true")) {
					Picasso.with(getActivity())
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

	}
}
