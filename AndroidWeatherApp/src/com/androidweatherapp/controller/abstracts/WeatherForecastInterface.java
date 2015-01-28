package com.androidweatherapp.controller.abstracts;

import java.util.ArrayList;

import android.content.Context;

import com.androidweatherapp.model.model.WeatherForecast;

public interface WeatherForecastInterface {

	public void success(ArrayList<WeatherForecast> weatherForecasts);
	
	public void error(String errorMsg);

	public Context getWeatherForecastContext();

}
