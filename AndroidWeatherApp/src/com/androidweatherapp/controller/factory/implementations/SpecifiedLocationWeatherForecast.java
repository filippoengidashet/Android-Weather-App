package com.androidweatherapp.controller.factory.implementations;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidweatherapp.controller.abstracts.WeatherForecastAbstract;
import com.androidweatherapp.controller.abstracts.WeatherForecastInterface;
import com.androidweatherapp.model.model.CurrentCondition;
import com.androidweatherapp.model.model.WeatherForecast;
import com.androidweatherapp.model.utilities.Utils;

public class SpecifiedLocationWeatherForecast extends WeatherForecastAbstract {

	private Context context = null;
	private WeatherForecastInterface weatherForecastInterface = null;
	private RequestQueue queue = null;
	private WeatherForecast weatherForecast = null;
	private ArrayList<WeatherForecast> forecastList = new ArrayList<WeatherForecast>();
	private CurrentCondition currentCondition = null;
	private String location = "", requestType = "", iconURL = "",
			description = "", observationTime = "", date = "", minC = "",
			maxC = "", minF = "", maxF = "";

	public SpecifiedLocationWeatherForecast(Context context,
			WeatherForecastInterface weatherForecastInterface) {
		super();
		this.context = context;
		this.weatherForecastInterface = weatherForecastInterface;
	}

	@Override
	public String execute() {

		queue = Volley.newRequestQueue(context);

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.POST, Utils.getCompleteURL(), null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						try {

							JSONObject jo = response.getJSONObject("data");

							JSONArray results = jo
									.getJSONArray("current_condition");
							
							currentCondition = new CurrentCondition();

							for (int i = 0; i < results.length(); i++) {							

								JSONObject jsonObjectRos = results
										.getJSONObject(i);

								observationTime = jsonObjectRos
										.getString("observation_time");
								currentCondition.setObservationTime(observationTime);

								maxC = jsonObjectRos.getString("temp_C");
								currentCondition.setForecastCelcius(maxC);

								maxF = jsonObjectRos.getString("temp_F");
								currentCondition.setForecastFahrenheight(maxF);

								JSONArray weatherDesc = jsonObjectRos
										.getJSONArray("weatherDesc");

								for (int j = 0; j < weatherDesc.length(); j++) {
									JSONObject weaather = weatherDesc
											.getJSONObject(i);

									description = weaather.getString("value");
									currentCondition.setForecastDescription(description);
								}

								JSONArray weatherIconUrl = jsonObjectRos
										.getJSONArray("weatherIconUrl");
								for (int j = 0; j < weatherIconUrl.length(); j++) {
									JSONObject weaather = weatherIconUrl
											.getJSONObject(i);

									iconURL = weaather.getString("value");
									currentCondition.setForecastIconURL(iconURL);

								}
							}

							JSONArray results2 = jo.getJSONArray("request");

							for (int i = 0; i < results2.length(); i++) {

								JSONObject jsonObjectRos = results2
										.getJSONObject(i);

								location = jsonObjectRos.getString("query");
								currentCondition.setForecastCity(location);

								requestType = jsonObjectRos.getString("type");
							}

							JSONArray weatherresults = jo
									.getJSONArray("weather");

							for (int i = 0; i < weatherresults.length(); i++) {

								JSONObject jsonObjectRos = weatherresults
										.getJSONObject(i);

								date = jsonObjectRos.getString("date");

								JSONArray hourly = jsonObjectRos
										.getJSONArray("hourly");

								for (int j = 0; j < hourly.length(); j++) {
									JSONObject weaather = hourly
											.getJSONObject(j);

									JSONArray weatherDesc = weaather
											.getJSONArray("weatherDesc");

									for (int h = 0; h < weatherDesc.length(); h++) {

										JSONObject start = weatherDesc
												.getJSONObject(h);

										description = start.getString("value");

									}

									JSONArray weatherIconUrl = weaather
											.getJSONArray("weatherIconUrl");

									for (int k = 0; k < weatherIconUrl.length(); k++) {

										JSONObject start = weatherIconUrl
												.getJSONObject(k);

										iconURL = start.getString("value");

									}

								}

								maxC = jsonObjectRos.getString("maxtempC");
								maxF = jsonObjectRos.getString("maxtempF");
								minC = jsonObjectRos.getString("mintempC");
								minF = jsonObjectRos.getString("mintempF");

								weatherForecast = new WeatherForecast();

								weatherForecast.setDate(date);
								weatherForecast
										.setForecastDescription(description);
								weatherForecast.setForecastLocation(location);
								weatherForecast.setIconURL(iconURL);
								weatherForecast.setMaxCelciusTemperature(maxC);
								weatherForecast.setMinCelciusTemperature(minC);
								weatherForecast
										.setMaxFahrenheightTemperature(maxF);
								weatherForecast
										.setMinFahrenheightTemperature(minF);
								weatherForecast
										.setObservationTime(observationTime);
								weatherForecast.setRequestType(requestType);
								
								weatherForecast.setCurrentCondition(currentCondition);

								forecastList.add(weatherForecast);

							}

						} catch (JSONException e) {
							Log.e("Volley Error", e.getMessage());
							e.printStackTrace();
						}

						weatherForecastInterface.success(forecastList);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						weatherForecastInterface.error(error.toString());
					}
				});

		System.out.println("URL : " + jsObjRequest.getUrl());

		queue.add(jsObjRequest);

		String response = "Added to Request Queue!";

		return response;
	}

}
