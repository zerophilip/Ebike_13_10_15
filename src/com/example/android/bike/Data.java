package com.example.android.bike;

import java.text.DecimalFormat;






import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Data extends Activity implements OnClickListener {
	private Button clean;
	private TextView Distance_label;
	private static TextView total_Distance;
	double[] final_total_km = new double[0];
	double total_km;
	SharedPreferences sharedpreferences;

	private Button kmh;
	private Button mph;

	private TextView topspeed;
	private TextView topspeed_label;
	private TextView wheelsize;
	private TextView wheelsize_label;
	public int wheel;
	private static Handler handler = new Handler();
	private Handler mThreadHandler;
	private HandlerThread mThread;
	private TextView total_Distance_kmORmi;

	float x1, x2;
	float y1, y2;

	String distance_out;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

		// Set up the window layout

		setContentView(R.layout.data);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Distance_label = (TextView) findViewById(R.id.Distance_label);
		total_Distance = (TextView) findViewById(R.id.total_Distance);
		clean = (Button) findViewById(R.id.clean);
		kmh = (Button) findViewById(R.id.kmh);
		mph = (Button) findViewById(R.id.mph);
		topspeed = (TextView) findViewById(R.id.topspeed);
		topspeed_label = (TextView) findViewById(R.id.topspeed_label);
		wheelsize = (TextView) findViewById(R.id.wheelsize);
		wheelsize_label = (TextView) findViewById(R.id.wheelsize_label);
		total_Distance_kmORmi = (TextView) findViewById(R.id.total_Distance_kmORmi);
		// mmtextview = (TextView)findViewById(R.id.speed);

		clean.setOnClickListener(this);
		kmh.setOnClickListener(this);
		mph.setOnClickListener(this);

		mThread = new HandlerThread("name");
		mThread.start();

		mThreadHandler = new Handler();

		mThreadHandler.post(r1);

		Thread thread = new Thread() {
			@Override
			public void run() {
				Message message;
				String obj = "OK";
				message = handler.obtainMessage(1, obj);
				handler.sendMessage(message);
			}
		};
		thread.start();
		thread = null;

		// TODO Auto-generated method stub
	}

	public void onStart() {
		super.onStart();

	}

	private Runnable r1 = new Runnable() {
		public void run() {
			String wheel = sharedpreferences.getString("wheel_value", "100");

			wheelsize.setText(wheel + " cm");

			String top_speed = sharedpreferences.getString("speed_value", "60");

			topspeed.setText(top_speed + " km/HR");

			// int dtt = sharedpreferences.getInt("tt", 00);
			// DecimalFormat df = new DecimalFormat("#");

			// String dttt = df.format(dtt);
			String dtt = sharedpreferences.getString("tt", "00");
			int rrss = sharedpreferences.getInt("int", 00);
			int rrrsss = Integer.parseInt(dtt);

			int totrrss = rrss + rrrsss;

			String rrrssss = Integer.toString(totrrss);

			total_Distance.setText(rrrssss);

		}
	};

	public void onBackPressed() {
		// do nothing.
	}

	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {
		// when user first touches the screen we get x and y coordinate
		case MotionEvent.ACTION_DOWN: {
			x1 = touchevent.getX();
			y1 = touchevent.getY();
			break;
		}
		case MotionEvent.ACTION_UP: {
			x2 = touchevent.getX();
			y2 = touchevent.getY();

			// if left to right sweep event on screen
			if (x1 < x2) {
				this.finish();
			}

			// if right to left sweep event on screen
			if (x1 > x2) {

			}

			// if UP to Down sweep event on screen
			if (y1 < y2) {

			}

			// if Down to UP sweep event on screen
			if (y1 > y2) {

			}
			break;
		}
		}
		return false;
	}

	static Handler myHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			double value_km = (Double) msg.obj;
			
			
			value_km = value_km * 0.000277777778;

			// double tt_value=+value_km;
			System.out.println(value_km);

			// total_Distance.setText(new
			// DecimalFormat("##.##").format(value_km)+" km");

		}
	};

	public double total(double km) {

		total_km = total_km + (km / 60 / 60);

		return total_km;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clean:
			String zero = "0";
			total_km = 0;
			System.out.println(total_km);
			Editor editor5 = sharedpreferences.edit();
			editor5.putString("tt", zero);
			editor5.putInt("int", 0);

			editor5.commit();

			total_Distance.setText("000");
			break;
		case R.id.kmh:
			// Intent intent = new Intent(this,BluetoothChat.class);
			// intent.putExtra("kmm",0);
			// passVaule.setkm(0);
			Editor editor1 = sharedpreferences.edit();
			editor1.putInt("kmORmi", 0);

			editor1.putString("stringkmmi", "km/HR");
			editor1.putString("distance", "km");
			editor1.commit();

			break;
		case R.id.mph:
			// Intent intent1 = new Intent(this ,BluetoothChat.class);
			// intent1.putExtra("kmm",1);
			// passVaule.setkm(1);
			Editor editor = sharedpreferences.edit();
			editor.putInt("kmORmi", 1);
			editor.putString("stringkmmi", "mi/HR");
			editor.putString("distance", "mi");
			editor.commit();
			// mmtextview.setText("00mi/HR");

			break;

		}

	}

}
