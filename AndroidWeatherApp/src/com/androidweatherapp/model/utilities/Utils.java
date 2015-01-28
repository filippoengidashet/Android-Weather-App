package com.androidweatherapp.model.utilities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

	public static String BASE_URL = "http://api.worldweatheronline.com/free/v2/weather.ashx?";
	public static String KEY = "00783f701193d402a6db50af63ab3";
	public static String LOCATION = "";
	public static String FORMAT = "json";
	public static String NUM_OF_DAYS = "";
	public static String RESULT_TYPE = "24";

	public static String getCompleteURL() {
		String temp = BASE_URL + "q=" + LOCATION + "&format=" + FORMAT
				+ "&num_of_days=" + NUM_OF_DAYS + "&tp="+RESULT_TYPE+"&key=" + KEY;
		return temp;
	}

	public static String getAddress(Context context, double latitude,
			double longitude) throws IOException {
		Geocoder myLocation = new Geocoder(context, Locale.getDefault());
		List<Address> myList = myLocation.getFromLocation(latitude, longitude,
				1);

		String addressStr = "";

		Address address = (Address) myList.get(0);

		addressStr += address.getAddressLine(0) + ", ";
		addressStr += address.getCountryName();

		return addressStr;
	}

	/**
	 * This method takes one parameters and returns true if network is available
	 * 
	 * @param context
	 *            set the ConnectivityManager application context
	 * @return boolean
	 * @see NetworkInfo
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
