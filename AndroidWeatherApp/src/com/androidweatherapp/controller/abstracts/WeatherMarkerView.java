package com.androidweatherapp.controller.abstracts;

import android.content.Context;
import android.widget.TextView;

import com.androidweatherapp.view.R;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MarkerView;
import com.github.mikephil.charting.utils.Utils;

public class WeatherMarkerView extends MarkerView {

	private TextView tvContent;

	public WeatherMarkerView(Context context, int layoutResource) {
		super(context, layoutResource);

		tvContent = (TextView) findViewById(R.id.tvContent);
	}

	@Override
	public void refreshContent(Entry e, int dataSetIndex) {

		if (e instanceof CandleEntry) {

			CandleEntry ce = (CandleEntry) e;

			tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
		} else {

			tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
		}
	}

	@Override
	public float getXOffset() {
		// this will center the marker-view horizontally
		return -(getWidth() / 2);
	}

	@Override
	public float getYOffset() {
		// this will cause the marker-view to be above the selected value
		return -getHeight();
	}
}
