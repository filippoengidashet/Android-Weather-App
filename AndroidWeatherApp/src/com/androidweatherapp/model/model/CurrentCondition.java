package com.androidweatherapp.model.model;

public class CurrentCondition {

	private String forecastCity = "", forecastIconURL = "",
			forecastCelcius = "", forecastFahrenheight = "",
			forecastDescription = "", observationTime = "";

	public String getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}

	public String getForecastCity() {
		return forecastCity;
	}

	public void setForecastCity(String forecastCity) {
		this.forecastCity = forecastCity;
	}

	public String getForecastIconURL() {
		return forecastIconURL;
	}

	public void setForecastIconURL(String forecastIconURL) {
		this.forecastIconURL = forecastIconURL;
	}

	public String getForecastCelcius() {
		return forecastCelcius;
	}

	public void setForecastCelcius(String forecastCelcius) {
		this.forecastCelcius = forecastCelcius;
	}

	public String getForecastFahrenheight() {
		return forecastFahrenheight;
	}

	public void setForecastFahrenheight(String forecastFahrenheight) {
		this.forecastFahrenheight = forecastFahrenheight;
	}

	public String getForecastDescription() {
		return forecastDescription;
	}

	public void setForecastDescription(String forecastDescription) {
		this.forecastDescription = forecastDescription;
	}

}
