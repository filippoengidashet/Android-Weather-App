package com.androidweatherapp.controller.factory;

import android.content.Context;

import com.androidweatherapp.controller.abstracts.WeatherForecastAbstract;
import com.androidweatherapp.controller.abstracts.WeatherForecastInterface;
import com.androidweatherapp.controller.factory.implementations.CurrentLocationWeatherForecast;
import com.androidweatherapp.controller.factory.implementations.SpecifiedLocationWeatherForecast;
import com.androidweatherapp.controller.service.GPSTrackerService;
import com.androidweatherapp.model.utilities.Utils;

public class WeatherForecastFactory {

	public static WeatherForecastAbstract getForecast(Context context,
			String type, WeatherForecastInterface weatherForecastInterface,
			String city, int numberOfDay) {
		if (type.equalsIgnoreCase("current")) {

			GPSTrackerService gpsTracker = new GPSTrackerService(context);

			Utils.LOCATION = "" + gpsTracker.getLatitude() + ","
					+ gpsTracker.getLongitude() + "";
			Utils.NUM_OF_DAYS = "" + 1;

			return new CurrentLocationWeatherForecast(context,
					weatherForecastInterface);
		}

		else if (type.equalsIgnoreCase("city")) {

			Utils.LOCATION = city;
			Utils.NUM_OF_DAYS = "" + numberOfDay;

			return new SpecifiedLocationWeatherForecast(context,
					weatherForecastInterface);
		}
		return null;
	}
}
