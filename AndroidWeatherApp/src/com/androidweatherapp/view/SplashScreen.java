/*
 * Copyright (c) 2014, Filippo Engidashet
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package com.androidweatherapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

/**
 * SplashScreen.java:
 * 
 * Purpose: To Display the basic Welcome Screen
 * 
 * @author Filippo Engidashet
 * @version 1.0
 * @since 2015-01-24
 */

public class SplashScreen extends Activity {

	private Intent intent = null;

	/**
	 * Represents a spinner declaration for the welcome loading progress.
	 */
	private ProgressBar spinner = null;

	Handler handler = new Handler();
	int progressStatus = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		spinner = (ProgressBar) this.findViewById(R.id.progressBar1);

		/**
		 * Creates a Handler inside Thread to handle modification over the
		 * Progress delay in seconds
		 */

		new Thread(new Runnable() {

			int i = 0;

			public void run() {
				while (progressStatus < 2) {
					progressStatus += doWork();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					handler.post(new Runnable() {
						public void run() {
							spinner.setProgress(progressStatus);
							i++;
						}
					});
				}

				intent = new Intent(SplashScreen.this, MainActivity.class);
				SplashScreen.this.startActivity(intent);
				SplashScreen.this.finish();
			}

			private int doWork() {

				return i++;
			}

		}).start();

	}

}
