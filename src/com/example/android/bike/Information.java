package com.example.android.bike;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Information extends Activity implements OnClickListener{

	private TextView curr_titt;
	private TextView currspeed;
	private TextView currspeedvalue;
	private TextView kmmih;
	private TextView avg;
	private TextView avgspeeed;
	private TextView max;
	private TextView maxspeed;
	private TextView trip;
	private TextView tripdistance;
	private TextView km;
	private TextView tripdur;
	private TextView hr;
	private TextView hrvalue;
	private TextView minvalue;
	private TextView min;
	private TextView alltriptit;
	private TextView totalmi;
	private TextView totaldistance;
	private Button resetbutton;
	private TextView tit_tx;
	private TextView kmmih2;
	private TextView kmmih3;
	private TextView km2;
	public double tripkm;
	Handler startview;
	public double total_min;
	protected Button back;
	public int kmmi;
	public int bluetoothvalue;

	
	Handler changetext;
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	

	public double currentkm;
	final Handler myHandler = new Handler();
	Handler textface = new Handler();
	SharedPreferences information;
	private BluetoothChatService mChatService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title4);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		curr_titt = (TextView) findViewById(R.id.curr_titt);
		currspeed = (TextView) findViewById(R.id.currspeed);
		currspeedvalue = (TextView) findViewById(R.id.currspeedvalue);
		kmmih = (TextView) findViewById(R.id.kmmih);
		avg = (TextView) findViewById(R.id.avg);
		avgspeeed = (TextView) findViewById(R.id.avgspeeed);
		max = (TextView) findViewById(R.id.max);
		maxspeed = (TextView) findViewById(R.id.maxspeed);
		trip = (TextView) findViewById(R.id.trip);
		tripdistance = (TextView) findViewById(R.id.tripdistance);
		km = (TextView) findViewById(R.id.km);
		tripdur = (TextView) findViewById(R.id.tripdur);
		hr = (TextView) findViewById(R.id.hr);
		minvalue = (TextView) findViewById(R.id.minvalue);
		min = (TextView) findViewById(R.id.min);
		resetbutton = (Button) findViewById(R.id.resetbutton);
		alltriptit = (TextView) findViewById(R.id.alltriptit);
		totalmi = (TextView) findViewById(R.id.totalmi);
		totaldistance = (TextView) findViewById(R.id.totaldistance);
		tit_tx = (TextView) findViewById(R.id.tit_tx);
		hrvalue = (TextView) findViewById(R.id.hrvalue);
		back = (Button)findViewById(R.id.back);
		kmmih2 = (TextView)findViewById(R.id.kmmih2);
		kmmih3 = (TextView)findViewById(R.id.kmmih3);
		km2 = (TextView)findViewById(R.id.km2);
		
		
		SharedPreferences icons = getSharedPreferences("3icon",
				Context.MODE_PRIVATE);
		bluetoothvalue = icons.getInt("bluetoothicon", 0);

		

		resetbutton.setOnClickListener(this);
		back.setOnClickListener(this);

		textface.post(facetext);
		tit_tx.setText("TRIP SUMMARY");
		
		information = getSharedPreferences("settingpage",
				Context.MODE_PRIVATE);
		kmmi = information.getInt("unit", 1);
        Handler changetext = new Handler();
        changetext.post(textchange);
		Handler startview = new Handler();
		startview.post(viewstart);
try {
	Timer myTimer = new Timer();
	myTimer.schedule(new TimerTask() {
		@Override
		public void run() {
			UpdateGUI();
		}
	}, 0, 2000);
	
} catch (Exception e) {
	// TODO: handle exception
}
		
		


		

	}
	
	final private Runnable textchange = new Runnable(){

		@Override
		public void run() {
			if (kmmi ==1 ){
				kmmih.setText("km/h");
				kmmih2.setText("km/h");
				kmmih3.setText("km/h");
				km.setText("km");
				km2.setText("km");
			}else if (kmmi == 2){
				kmmih.setText("mph");
				kmmih2.setText("mph");
				kmmih3.setText("mph");
				km.setText("mph");
				km2.setText("mph");
			}
			
		}
		
	};

	final private Runnable viewstart = new Runnable() {

		@Override
		public void run() {
            avgspeeed.setText("0");
			information = getSharedPreferences("information",
					Context.MODE_PRIVATE);

			int min = information.getInt("min", 0);
			int hour = information.getInt("hour", 0);
			String currkm = information.getString("currspeed", "0");
			String maxkm = information.getString("maxspeed", "0");
			String resetdis = information.getString("total", "0");
			String total_distance_output = information.getString("distance",
					"0");
			

			

			String mins = Integer.toString(min);
			String hours = Integer.toString(hour);

			hrvalue.setText(hours);
			minvalue.setText(mins);
			currspeedvalue.setText(currkm);
			maxspeed.setText(maxkm);

			currentkm = Double.parseDouble(currkm);
			tripkm = Double.parseDouble(resetdis);
			String dtt = information.getString("tt", "0.0");
			double dttt = Double.parseDouble(dtt); //

		    
		    
		    
		    
		    
			
			if(kmmi == 2){
			dttt = dttt* 0.62137;
			}
			
			
			DecimalFormat dff = new DecimalFormat("###.#"); //
			String tripdistances = dff.format(dttt);
			tripdistance.setText(tripdistances);
		
			
			double tripdistancess = Double.parseDouble(resetdis);
			
			if(total_min>=1){
			total_min = (min + (hour * 60));
			double total_min2 = total_min/60;
			//if(total_min>=1){
			System.out.println("total_min"+total_min2);

			double avgspeedvaule = dttt / total_min2;
			System.out.println("avgspeedvaul"+avgspeedvaule);

			DecimalFormat dfff = new DecimalFormat("###");

			String finalavgspeedoutput = dfff.format(avgspeedvaule);
			
			avgspeeed.setText(finalavgspeedoutput);
			}
			double totaldistanced = Double.valueOf(total_distance_output);
			if(kmmi==2){
				
				totaldistanced = totaldistanced* 0.62137;
				
				
			}
			DecimalFormat vvv = new DecimalFormat("####.#");
			total_distance_output= vvv.format(totaldistanced);
			
			
			totaldistance.setText(total_distance_output);
			
			
			tripdistance.setText(tripdistances);

		}

		

	};

	private Runnable facetext = new Runnable() {

		@Override
		public void run() {

			Typeface tf = Typeface.createFromAsset(getAssets(),
					"fonts/gotham.ttf");
			curr_titt.setTypeface(tf);
			currspeed.setTypeface(tf);
			currspeedvalue.setTypeface(tf);
			kmmih.setTypeface(tf);
			avg.setTypeface(tf);
			avgspeeed.setTypeface(tf);
			max.setTypeface(tf);
			maxspeed.setTypeface(tf);
			trip.setTypeface(tf);
			tripdistance.setTypeface(tf);
			km.setTypeface(tf);
			tripdur.setTypeface(tf);
			hr.setTypeface(tf);
			minvalue.setTypeface(tf);
			min.setTypeface(tf);
			resetbutton.setTypeface(tf);
			alltriptit.setTypeface(tf);
			totalmi.setTypeface(tf);
			totaldistance.setTypeface(tf);
			hrvalue.setTypeface(tf);
			kmmih2.setTypeface(tf);
			kmmih3.setTypeface(tf);
			km2.setTypeface(tf);

		}

	};

	private void UpdateGUI() {

		// tv.setText(String.valueOf(i));
		myHandler.post(myRunnable);
		
		
		
		
	}

	final Runnable myRunnable = new Runnable() {
		public void run() {

			information = getSharedPreferences("information",
					Context.MODE_PRIVATE);

			int min = information.getInt("min", 0);
			int hour = information.getInt("hour", 0);
			String currkm = information.getString("currspeed", "0");
			String maxkm = information.getString("maxspeed", "0");
			String resetdis = information.getString("total", "0");
			String total_distance_output = information.getString("distance",
					"0");

			

			String mins = Integer.toString(min);
			String hours = Integer.toString(hour);

			hrvalue.setText(hours);
			minvalue.setText(mins);
			currspeedvalue.setText(currkm);
			maxspeed.setText(maxkm);

			currentkm = Double.parseDouble(currkm);
			tripkm = Double.parseDouble(resetdis);
			String dtt = information.getString("tt", "0.0");
			double dttt = Double.parseDouble(dtt); //
			
			if(kmmi == 2){
			dttt = dttt* 0.62137;
			}
			
			
			DecimalFormat dff = new DecimalFormat("###.#"); //
			String tripdistances = dff.format(dttt);
			tripdistance.setText(tripdistances);
			
			SharedPreferences icons = getSharedPreferences("3icon",
					Context.MODE_PRIVATE);
			bluetoothvalue = icons.getInt("bluetoothicon", 0);

		
		
			
			double tripdistancess = Double.parseDouble(resetdis);
			

			total_min = (min + (hour * 60));
			double total_min2 = total_min/60;
			if(total_min>0){
			System.out.println("total_min"+total_min2);

			double avgspeedvaule = dttt / total_min2;
			System.out.println("avgspeedvaul"+avgspeedvaule);

			DecimalFormat dfff = new DecimalFormat("###");

			String finalavgspeedoutput = dfff.format(avgspeedvaule);
			
			avgspeeed.setText(finalavgspeedoutput);
			}
			
			double totaldistanced = Double.valueOf(total_distance_output);
			if(kmmi==2){
				
				totaldistanced = totaldistanced* 0.62137;
				
				
			}
			DecimalFormat vvv = new DecimalFormat("####.#");
			total_distance_output= vvv.format(totaldistanced);
			
			
			totaldistance.setText(total_distance_output);
			
			
			tripdistance.setText(tripdistances);

		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.resetbutton:
		
			information = getSharedPreferences("information",
					Context.MODE_PRIVATE);
			Editor editor = information.edit();
			editor.putInt("min",0);
			editor.putInt("hour", 0);
			editor.putString("currspeed", "0");
			editor.putString("maxspeed", "0");
			editor.putString("total", "0");
			editor.putString("tt", "0.0");
			editor.putInt("set0", 0);
			editor.commit();
			
			hrvalue.setText("0");
			minvalue.setText("0");
			currspeedvalue.setText("0");
			maxspeed.setText("0");
			tripdistance.setText("0.0");
			avgspeeed.setText("0");
			break;
		case R.id.back:
			finish();
			break;
		
			
			//Intent intent2 = new Intent(this, BluetoothChatService.class);
			//startActivity(intent2);
			
	
			
	
		default: 
			break;
		}
	}


	// press back key do nothing
			 public void onBackPressed() {  }
	


}
