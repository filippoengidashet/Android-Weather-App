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
import com.androidweatherapp.model.model.WeatherForecast;
import com.androidweatherapp.model.utilities.Utils;

public class CurrentLocationWeatherForecast extends WeatherForecastAbstract {

	private Context context = null;
	private WeatherForecastInterface weatherForecastInterface = null;
	private RequestQueue queue = null;
	private WeatherForecast weatherForecast = null;
	private ArrayList<WeatherForecast> weatherForecasts = new ArrayList<WeatherForecast>();

	public CurrentLocationWeatherForecast(Context context,
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

						weatherForecast = new WeatherForecast();

						try {

							JSONObject jo = response.getJSONObject("data");

							JSONArray results = jo
									.getJSONArray("current_condition");

							for (int i = 0; i < results.length(); i++) {

								JSONObject jsonObjectRos = results
										.getJSONObject(i);

								String observationTime = jsonObjectRos
										.getString("observation_time");
								weatherForecast.setObservationTime(observationTime);

								String celciusTemperature = jsonObjectRos
										.getString("temp_C");
								weatherForecast.setMaxCelciusTemperature(celciusTemperature);

								String fahrenheightTemperature = jsonObjectRos
										.getString("temp_F");
								weatherForecast.setMaxFahrenheightTemperature(fahrenheightTemperature);

								JSONArray weatherDesc = jsonObjectRos
										.getJSONArray("weatherDesc");
								for (int j = 0; j < weatherDesc.length(); j++) {
									JSONObject weaather = weatherDesc
											.getJSONObject(i);

									String forecastDescription = weaather
											.getString("value");
									weatherForecast
											.setForecastDescription(forecastDescription);

								}

								JSONArray weatherIconUrl = jsonObjectRos
										.getJSONArray("weatherIconUrl");
								for (int j = 0; j < weatherIconUrl.length(); j++) {
									JSONObject weaather = weatherIconUrl
											.getJSONObject(i);

									String iconURL = weaather
											.getString("value");
									weatherForecast.setIconURL(iconURL);

								}
							}

							JSONArray results2 = jo.getJSONArray("request");

							for (int i = 0; i < results2.length(); i++) {

								JSONObject jsonObjectRos = results2
										.getJSONObject(i);

								String forecastLocation = jsonObjectRos
										.getString("query");

								weatherForecast
										.setForecastLocation(forecastLocation);

								String requestType = jsonObjectRos
										.getString("type");
								weatherForecast.setRequestType(requestType);
							}

						} catch (JSONException e) {
							Log.e("Volley Error", e.getMessage());
							e.printStackTrace();
						}

						weatherForecasts.add(weatherForecast);
						
						weatherForecastInterface.success(weatherForecasts);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						weatherForecastInterface.error(error.toString());
					}

				});

		queue.add(jsObjRequest);

		System.out.println("URL: " + jsObjRequest.getUrl());
		String response = "Added to Request Queue!";

		return response;
	}

}
