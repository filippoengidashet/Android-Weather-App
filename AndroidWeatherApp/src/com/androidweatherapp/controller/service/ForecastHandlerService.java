package com.androidweatherapp.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ForecastHandlerService extends Service {

	ForecastCheckThread forecastCheckThread;

	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		forecastCheckThread = new ForecastCheckThread();
		forecastCheckThread.start();
		return 0;
	}

	@Override
	public void onDestroy() {
		forecastCheckThread.stopThread();
		super.onDestroy();
	}

	class ForecastCheckThread extends Thread {

		public ForecastCheckThread() {
			super();
		}

		public void stopThread() {

		}

		@Override
		public void run() {
			super.run();

			while (true) {

				sendBroadcast(new Intent().putExtra("forecastStatus", true)
						.setAction("statusUpdate"));

				try {
					Thread.sleep(300000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
