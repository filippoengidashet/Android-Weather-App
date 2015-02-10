package com.androidweatherapp.model.model;

import java.util.ArrayList;

public class Setting {

	private ArrayList<String> cities = null;
	private String forecastDay = "";
	private String showIcon = "";
	private String displayType = "";

	public ArrayList<String> getCities() {
		return cities;
	}

	public void setCities(ArrayList<String> cities) {
		this.cities = cities;
	}

	public String getForecastDay() {
		return forecastDay;
	}

	public void setForecastDay(String forecastDay) {
		this.forecastDay = forecastDay;
	}

	public String getShowIcon() {
		return showIcon;
	}

	public void setShowIcon(String showIcon) {
		this.showIcon = showIcon;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
}