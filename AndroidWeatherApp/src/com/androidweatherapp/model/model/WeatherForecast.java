package com.androidweatherapp.model.model;

public class WeatherForecast {

	private String forecastLocation = "", minCelciusTemperature = "",
			minFahrenheightTemperature = "", maxCelciusTemperature = "",
			maxFahrenheightTemperature = "";
	private String iconURL = "", forecastDescription = "", requestType = "",
			observationTime = "", date = "";

	private CurrentCondition currentCondition = null;

	public CurrentCondition getCurrentCondition() {
		return currentCondition;
	}

	public void setCurrentCondition(CurrentCondition currentCondition) {
		this.currentCondition = currentCondition;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getForecastLocation() {
		return forecastLocation;
	}

	public void setForecastLocation(String forecastLocation) {
		this.forecastLocation = forecastLocation;
	}

	public String getMinCelciusTemperature() {
		return minCelciusTemperature;
	}

	public void setMinCelciusTemperature(String minCelciusTemperature) {
		this.minCelciusTemperature = minCelciusTemperature;
	}

	public String getMinFahrenheightTemperature() {
		return minFahrenheightTemperature;
	}

	public void setMinFahrenheightTemperature(String minFahrenheightTemperature) {
		this.minFahrenheightTemperature = minFahrenheightTemperature;
	}

	public String getMaxCelciusTemperature() {
		return maxCelciusTemperature;
	}

	public void setMaxCelciusTemperature(String maxCelciusTemperature) {
		this.maxCelciusTemperature = maxCelciusTemperature;
	}

	public String getMaxFahrenheightTemperature() {
		return maxFahrenheightTemperature;
	}

	public void setMaxFahrenheightTemperature(String maxFahrenheightTemperature) {
		this.maxFahrenheightTemperature = maxFahrenheightTemperature;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	public String getForecastDescription() {
		return forecastDescription;
	}

	public void setForecastDescription(String forecastDescription) {
		this.forecastDescription = forecastDescription;
	}

}
