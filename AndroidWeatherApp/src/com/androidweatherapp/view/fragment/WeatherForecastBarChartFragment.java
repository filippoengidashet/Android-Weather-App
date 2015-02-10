package com.androidweatherapp.view.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidweatherapp.controller.Controller;
import com.androidweatherapp.controller.abstracts.WeatherMarkerView;
import com.androidweatherapp.model.model.CurrentCondition;
import com.androidweatherapp.model.model.WeatherForecast;
import com.androidweatherapp.view.R;
import com.androidweatherapp.view.activity.WeatherForecastActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.squareup.picasso.Picasso;

public class WeatherForecastBarChartFragment extends Fragment {

	private View view = null;
	private BarChart mChart;
	private WeatherForecastActivity parentActivity = null;
	private int index = 0;

	private Controller controller = null;
	private ImageView currentConditionForecastIcon = null;
	private TextView currentConditionForecastCity = null,
			currentConditionForecastTemp = null,
			currentConditionForecastDescription = null,
			currentConditionForecastObservationTime = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		parentActivity = (WeatherForecastActivity) getActivity();

		view = inflater.inflate(R.layout.fragment_forecast_chart, container,
				false);

		intitializeViews();

		mChart.setDescription("");

		// disable the drawing of values
		mChart.setDrawYValues(true);

		// scaling can now only be done on x- and y-axis separately
		mChart.setPinchZoom(true);
		mChart.setValueFormatter(new LargeValueFormatter());

		mChart.setDrawBarShadow(false);

		mChart.setDrawGridBackground(true);
		mChart.setDrawHorizontalGrid(true);

		displayResult(parentActivity.getForecasts());

		// create a custom MarkerView (extend MarkerView) and specify the layout
		// to use for it
		WeatherMarkerView mv = new WeatherMarkerView(getActivity(),
				R.layout.custom_marker_view);

		// define an offset to change the original position of the marker
		// (optional)
		// mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());

		// set the marker to the chart
		mChart.setMarkerView(mv);

		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
				"OpenSans-Regular.ttf");

		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
		l.setTypeface(tf);

		XLabels xl = mChart.getXLabels();
		xl.setCenterXLabelText(true);
		xl.setTypeface(tf);

		YLabels yl = mChart.getYLabels();
		yl.setTypeface(tf);
		yl.setFormatter(new LargeValueFormatter());

		mChart.setValueTypeface(tf);

		return view;
	}

	private void intitializeViews() {
		controller = Controller.getInstanceUsingDoubleLocking(getActivity());

		mChart = (BarChart) view.findViewById(R.id.chart1);

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
	}

	private void displayResult(ArrayList<WeatherForecast> weatherForecasts) {

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

		ArrayList<String> xVals = new ArrayList<String>();

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
		ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

		for (WeatherForecast wf : weatherForecasts) {

			xVals.add(wf.getDate());
			yVals1.add(new BarEntry(Float.parseFloat(wf
					.getMinCelciusTemperature()), index));
			yVals2.add(new BarEntry(Float.parseFloat(wf
					.getMaxCelciusTemperature()), index));
			yVals3.add(new BarEntry(Float.parseFloat(wf
					.getMinFahrenheightTemperature()), index));
			yVals4.add(new BarEntry(Float.parseFloat(wf
					.getMaxFahrenheightTemperature()), index));

			index++;

		}

		BarDataSet set1 = new BarDataSet(yVals1, "Min Celsius");
		set1.setColor(Color.rgb(40, 176, 50));
		BarDataSet set2 = new BarDataSet(yVals2, "Max Celsius");
		set2.setColor(Color.rgb(255, 240, 0));
		BarDataSet set3 = new BarDataSet(yVals3, "Min Fahrenheit");
		set3.setColor(Color.rgb(31, 87, 200));
		BarDataSet set4 = new BarDataSet(yVals4, "Max Fahrenheit");
		set4.setColor(Color.rgb(231, 29, 7));

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);
		dataSets.add(set2);
		dataSets.add(set3);
		dataSets.add(set4);

		BarData data = new BarData(xVals, dataSets);

		// add space between the dataset groups in percent of bar-width
		data.setGroupSpace(110f);

		mChart.setData(data);
		mChart.invalidate();

	}

}
