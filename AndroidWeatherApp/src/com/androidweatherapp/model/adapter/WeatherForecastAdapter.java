package com.androidweatherapp.model.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidweatherapp.controller.Controller;
import com.androidweatherapp.model.model.WeatherForecast;
import com.androidweatherapp.view.R;
import com.squareup.picasso.Picasso;

public class WeatherForecastAdapter extends ArrayAdapter<WeatherForecast> {

	private List<WeatherForecast> lists = null;
	private Context context = null;
	private Controller controller = null;

	public WeatherForecastAdapter(Context context,
			List<WeatherForecast> objects, Controller controller) {
		super(context, R.layout.weather_forecast_row, objects);
		this.lists = objects;
		this.context = context;
		this.controller = controller;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(this.context).inflate(
					R.layout.weather_forecast_row, parent, false);

			holder = new ViewHolder();

			holder.forecastIcon = (ImageView) convertView
					.findViewById(R.id.forecastImgIcon);
			holder.forecastDate = (TextView) convertView
					.findViewById(R.id.forecastDateTxt);
			holder.forecastDescription = (TextView) convertView
					.findViewById(R.id.forecastDescripionTxt);
			holder.forecastMinC = (TextView) convertView
					.findViewById(R.id.forecastMinCelcius);

			holder.forecastMaxC = (TextView) convertView
					.findViewById(R.id.forecastMaxCelcius);
			holder.forecastAverageC = (TextView) convertView
					.findViewById(R.id.forecastCelciusAverage);
			holder.forecastMinF = (TextView) convertView
					.findViewById(R.id.forecastMinFah);
			holder.forecastMaxF = (TextView) convertView
					.findViewById(R.id.forecastMaxFah);

			holder.forecastAverageF = (TextView) convertView
					.findViewById(R.id.forecastFahAverage);

			convertView.setTag(holder);
			convertView.setTag(R.id.forecastImgIcon, holder.forecastIcon);
			convertView.setTag(R.id.forecastDateTxt, holder.forecastDate);
			convertView.setTag(R.id.forecastDescripionTxt,
					holder.forecastDescription);
			convertView.setTag(R.id.forecastMinCelcius, holder.forecastMinC);
			convertView.setTag(R.id.forecastMaxCelcius, holder.forecastMaxC);
			convertView.setTag(R.id.forecastCelciusAverage,
					holder.forecastAverageC);
			convertView.setTag(R.id.forecastMinFah, holder.forecastMinF);
			convertView.setTag(R.id.forecastMaxFah, holder.forecastMaxF);
			convertView
					.setTag(R.id.forecastFahAverage, holder.forecastAverageF);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		WeatherForecast currentWeatherForecast = lists.get(position);

		if (controller.getSettings().getShowIcon().equals("true")) {
			Picasso.with(context).load(currentWeatherForecast.getIconURL())
					.into(holder.forecastIcon);
		} else {
			holder.forecastIcon.setImageBitmap(null);
		}

		holder.forecastDate
				.setText("Date: " + currentWeatherForecast.getDate());
		holder.forecastDescription.setText(currentWeatherForecast
				.getForecastDescription());
		holder.forecastMinC.setText("Min:"
				+ currentWeatherForecast.getMinCelciusTemperature() + "° C");

		holder.forecastMaxC.setText("Max: "
				+ currentWeatherForecast.getMaxCelciusTemperature() + "° C");

		double celsiusAverage = (Double.parseDouble(currentWeatherForecast
				.getMaxCelciusTemperature()) + Double
				.parseDouble(currentWeatherForecast.getMinCelciusTemperature())) / 2;

		holder.forecastAverageC.setText("Average: " + celsiusAverage + "° C");
		holder.forecastMinF.setText("Min: "
				+ currentWeatherForecast.getMinFahrenheightTemperature()
				+ "° F");
		holder.forecastMaxF.setText("Max: "
				+ currentWeatherForecast.getMaxFahrenheightTemperature()
				+ "° F");

		double fahAverage = (Double.parseDouble(currentWeatherForecast
				.getMaxFahrenheightTemperature()) + Double
				.parseDouble(currentWeatherForecast
						.getMinFahrenheightTemperature())) / 2;

		holder.forecastAverageF.setText("Average: " + fahAverage + "° F");

		return convertView;
	}

	public static class ViewHolder {
		public ImageView forecastIcon;
		public TextView forecastDate, forecastDescription;
		public TextView forecastMinC, forecastMaxC;
		public TextView forecastMinF, forecastMaxF;
		public TextView forecastAverageC, forecastAverageF;

	}
}
