package com.example.android.bike;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Battery extends Activity implements OnTouchListener,
		OnClickListener {

	private TextView t1;
	private TextView t2;
	private TextView t3;
	
	
	private TextView batt1_v;
	private TextView batt2_v;
	private TextView batt3_v;
	
	// battery voltage display
	private TextView batt1_vo;
	private TextView batt2_vo;
	private TextView batt3_vo;
	protected Button back;
	public int passb1v;
	public int passb2v;
	public int passb3v;
	public int b1onoff;
	public int b2onoff;
	public int b3onoff;
	final Handler myHandler = new Handler();
	public int bluetoothvalue;

	public double b1vot;
	public double b2vot;
	public double b3vot;
	public int charge;
	public int chargeadd = 0;
	Handler chargean = new Handler();

	// on change

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_battery);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title4);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //fonts setting

		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/gotham.ttf");



		final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar3);
		RoundProgressBar.setOnTouchListener(this);
		final RoundProgressBar2 RoundProgressBar2 = (RoundProgressBar2) findViewById(R.id.roundProgressBar4);
		RoundProgressBar2.setOnTouchListener(this);
		final RoundProgressBar2 RoundProgressBar3 = (RoundProgressBar2) findViewById(R.id.roundProgressBar5);
		RoundProgressBar3.setOnTouchListener(this);
		back = (Button) findViewById(R.id.back);

		back.setOnClickListener(this);

		SharedPreferences batteryshare = getSharedPreferences("battery",
				Context.MODE_PRIVATE);

        //fonts setting

		t1 = (TextView) findViewById(R.id.t1);
		t1.setTypeface(tf);
		t2 = (TextView) findViewById(R.id.t2);
		t2.setTypeface(tf);
		t3 = (TextView) findViewById(R.id.t3);
		t3.setTypeface(tf);
		batt1_v = (TextView) findViewById(R.id.batt1_v);
		batt1_v.setTypeface(tf);
		batt2_v = (TextView) findViewById(R.id.batt2_v);
		batt2_v.setTypeface(tf);
		batt3_v = (TextView) findViewById(R.id.batt3_v);
		batt3_v.setTypeface(tf);
		batt1_vo = (TextView) findViewById(R.id.batt1_vo);
		batt1_vo.setTypeface(tf);
		batt2_vo = (TextView) findViewById(R.id.batt2_vo);
		batt2_vo.setTypeface(tf);
		batt3_vo = (TextView) findViewById(R.id.batt3_vo);
		batt3_vo.setTypeface(tf);

        //get values in sharedpreferences
		b1onoff = batteryshare.getInt("onoff1", 0);
		b2onoff = batteryshare.getInt("onoff2", 0);
		b3onoff = batteryshare.getInt("onoff3", 0);
		charge = batteryshare.getInt("charge", 0);

		passb1v = batteryshare.getInt("b1", 0);
		passb2v = batteryshare.getInt("b2", 0);
		passb3v = batteryshare.getInt("b3", 0);

		b1vot = batteryshare.getInt("b1v", 0);
		b2vot = batteryshare.getInt("b2v", 0);
		b3vot = batteryshare.getInt("b3v", 0);
 
		if (passb1v >= 111) {
            //the value over than 111 means the battery 1 is selected.
			RoundProgressBar.onProgressColor("#acfafd");

			RoundProgressBar2.onProgressColor("#084366");

			RoundProgressBar3.onProgressColor("#084366");

			passb1v = passb1v - 128;

		}

		if (passb2v >= 111) {
			RoundProgressBar.onProgressColor("#084366");

			RoundProgressBar2.onProgressColor("#acfafd");

			RoundProgressBar3.onProgressColor("#084366");

			passb2v = passb2v - 128;

		}

		if (passb3v >= 111) {
			RoundProgressBar.onProgressColor("#084366");

			RoundProgressBar2.onProgressColor("#084366");

			RoundProgressBar3.onProgressColor("#acfafd");

			passb3v = passb3v - 128;

		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (b1onoff == 1) {

			batt1_v.setText(passb1v + "%");
			RoundProgressBar.onProgressChange1(passb1v);
		} else if (b1onoff == 0) {
			RoundProgressBar.onProgressChange1(0);
			batt1_v.setText("Undetected");
		}

		if (b2onoff == 1) {

			batt2_v.setText(passb2v + "%");

			RoundProgressBar2.onProgressChange1(passb2v);
		} else if (b2onoff == 0) {
			RoundProgressBar2.onProgressChange1(0);
			batt2_v.setText("Undetected");
		}

		if (b3onoff == 1) {

			batt3_v.setText(passb3v + "%");
			RoundProgressBar3.onProgressChange1(passb3v);
		} else if (b3onoff == 0) {
			RoundProgressBar3.onProgressChange1(0);
			batt3_v.setText("Undetected");
		}

		b1vot = b1vot / 10;
		b2vot = b2vot / 10;
		b3vot = b3vot / 10;

		DecimalFormat df = new DecimalFormat("##.#");

		String vo1 = df.format(b1vot);
		batt1_vo.setText(vo1 + "V");

		String vo2 = df.format(b2vot);
		batt2_vo.setText(vo2 + "V");

		String vo3 = df.format(b3vot);
		batt3_vo.setText(vo3 + "V");

		Editor editor = batteryshare.edit();
		editor.clear();
// use the timer to update the UI,every 2 sec
		Timer myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				UpdateGUI();
			}
		}, 0, 2000);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.roundProgressBar3:
			if (event.getAction() == MotionEvent.ACTION_UP)

			{
				try {if (b1onoff == 1) {

                    //when the battery 1 has been press, the color will be change.

					final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar3);

					RoundProgressBar.onProgressColor("#acfafd");

					final RoundProgressBar2 RoundProgressBar2 = (RoundProgressBar2) findViewById(R.id.roundProgressBar4);
					RoundProgressBar2.onProgressColor("#084366");

					final RoundProgressBar2 RoundProgressBar3 = (RoundProgressBar2) findViewById(R.id.roundProgressBar5);

					RoundProgressBar3.onProgressColor("#084366");

					SharedPreferences batteryshare = getSharedPreferences(
							"battery", Context.MODE_PRIVATE);
                    //save the value, battselect = 1 means battery 1 has been selected
					Editor editor = batteryshare.edit();
					editor.putInt("battselect", 1);
					editor.commit();

				}
					
				} catch (Exception e) {
					
				}
				
			}

			break;

		case R.id.roundProgressBar4:
			if (event.getAction() == MotionEvent.ACTION_UP) {
				
				try {
					if (b2onoff == 1) {
						//passb2v = passb2v - 128;
						final RoundProgressBar2 RoundProgressBar2 = (RoundProgressBar2) findViewById(R.id.roundProgressBar4);
						RoundProgressBar2.onProgressColor("#acfafd");

						final RoundProgressBar2 RoundProgressBar3 = (RoundProgressBar2) findViewById(R.id.roundProgressBar5);
						RoundProgressBar3.onProgressColor("#084366");

						final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar3);

						RoundProgressBar.onProgressColor("#084366");
						SharedPreferences batteryshare = getSharedPreferences(
								"battery", Context.MODE_PRIVATE);
						Editor editor = batteryshare.edit();
						editor.putInt("battselect", 2);
						editor.commit();

					}
					
				} catch (Exception e) {
					
				}

			
			}
			break;
		case R.id.roundProgressBar5:
			if (event.getAction() == MotionEvent.ACTION_UP) {
				try {
					if (b3onoff == 1) {
						//passb3v = passb3v - 128;
						final RoundProgressBar2 RoundProgressBar3 = (RoundProgressBar2) findViewById(R.id.roundProgressBar5);
						RoundProgressBar3.onProgressColor("#acfafd");

						final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar3);

						RoundProgressBar.onProgressColor("#084366");
						final RoundProgressBar2 RoundProgressBar2 = (RoundProgressBar2) findViewById(R.id.roundProgressBar4);
						RoundProgressBar2.onProgressColor("#084366");
						SharedPreferences batteryshare = getSharedPreferences(
								"battery", Context.MODE_PRIVATE);
						Editor editor = batteryshare.edit();
						editor.putInt("battselect", 3);
						editor.commit();
					}
					
				} catch (Exception e) {
					
				}

				
			}
			break;
		case R.id.back:

			finish();
			break;

		}
		return true;

	}

	private void UpdateGUI() {

		// tv.setText(String.valueOf(i));
		myHandler.post(myRunnable);

		if (charge == 1) {
			new Thread(new Runnable() {
				public void run() {
					chargeadd = passb1v;
					while (chargeadd < 100) {
						chargeadd += 1;
						chargean.post(new Runnable() {
							public void run() {
								final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar3);
								RoundProgressBar.onProgressChange1(chargeadd);

							}
						});
						try {
							// Sleep for 200 milliseconds.
							// Just to display the progress slowly
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

		}
		if (charge == 2) {
			new Thread(new Runnable() {
				public void run() {
					chargeadd = passb2v;
					while (chargeadd < 100) {
						chargeadd += 1;
						chargean.post(new Runnable() {
							public void run() {
								final RoundProgressBar2 RoundProgressBar2 = (RoundProgressBar2) findViewById(R.id.roundProgressBar4);
								RoundProgressBar2.onProgressChange1(chargeadd);

							}
						});
						try {
							// Sleep for 200 milliseconds.
							// Just to display the progress slowly
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

		}
		if (charge == 3) {
			new Thread(new Runnable() {
				public void run() {
					chargeadd = passb3v;
					while (chargeadd < 100) {
						chargeadd += 1;
						chargean.post(new Runnable() {
							public void run() {
								final RoundProgressBar2 RoundProgressBar3 = (RoundProgressBar2) findViewById(R.id.roundProgressBar5);
								RoundProgressBar3.onProgressChange1(chargeadd);

							}
						});
						try {
							// Sleep for 200 milliseconds.
							// Just to display the progress slowly
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

		}
		
		if(charge == 0){
			
		}

	}

	final Runnable myRunnable = new Runnable() {

		public void run() {

			SharedPreferences icons = getSharedPreferences("3icon",
					Context.MODE_PRIVATE);

			SharedPreferences batteryshare = getSharedPreferences("battery",
					Context.MODE_PRIVATE);

			bluetoothvalue = icons.getInt("bluetoothicon", 0);

			b1onoff = batteryshare.getInt("onoff1", 0);
			b2onoff = batteryshare.getInt("onoff2", 0);
			b3onoff = batteryshare.getInt("onoff3", 0);
			charge = batteryshare.getInt("charge", 0);
			b1vot = batteryshare.getInt("b1v", 0);
			b2vot = batteryshare.getInt("b2v", 0);
			b3vot = batteryshare.getInt("b3v", 0);

			passb1v = batteryshare.getInt("b1", 0);

			final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar3);

			final RoundProgressBar2 RoundProgressBar2 = (RoundProgressBar2) findViewById(R.id.roundProgressBar4);

			final RoundProgressBar2 RoundProgressBar3 = (RoundProgressBar2) findViewById(R.id.roundProgressBar5);

			passb1v = batteryshare.getInt("b1", 0);
			if (passb1v >= 111) {

				passb1v = passb1v - 128;

			}

			if (passb2v >= 111) {

				passb2v = passb2v - 128;

			}

			if (passb3v >= 111) {

				passb3v = passb3v - 128;

			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (b1onoff == 1) {

				batt1_v.setText(passb1v + "%");
				RoundProgressBar.onProgressChange1(passb1v);
			} else if (b1onoff == 0) {
				RoundProgressBar.onProgressChange1(0);
				batt1_v.setText("Undetected");
			}

			passb2v = batteryshare.getInt("b2", 0);

			if (b2onoff == 1) {
				if (passb2v > 100) {
					passb2v = passb2v - 128;
				}

				batt2_v.setText(passb2v + "%");

				RoundProgressBar2.onProgressChange1(passb2v);
			} else if (b2onoff == 0) {
				RoundProgressBar2.onProgressChange1(0);
				batt2_v.setText("Undetected");
			}
			passb3v = batteryshare.getInt("b3", 0);

			if (b3onoff == 1) {

				if (passb3v > 100) {
					passb3v = passb3v - 128;
				}
				batt3_v.setText(passb3v + "%");
				RoundProgressBar3.onProgressChange1(passb3v);

			} else if (b3onoff == 0) {
				RoundProgressBar3.onProgressChange1(0);
				batt3_v.setText("Undetected");
			}

			b1vot = b1vot / 10;
			b2vot = b2vot / 10;
			b3vot = b3vot / 10;

			DecimalFormat df = new DecimalFormat("##.#");

			String vo1 = df.format(b1vot);
			batt1_vo.setText(vo1 + "V");

			String vo2 = df.format(b2vot);
			batt2_vo.setText(vo2 + "V");

			String vo3 = df.format(b3vot);
			batt3_vo.setText(vo3 + "V");

			Editor editor = batteryshare.edit();
			editor.clear();

		}

	};

	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		SharedPreferences settonoff = getSharedPreferences("set",
				Context.MODE_PRIVATE);
		Editor editor = settonoff.edit();
		editor.putInt("setonoff", 1);
		editor.commit();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}
	// press back key do nothing
			 public void onBackPressed() {  }

}
