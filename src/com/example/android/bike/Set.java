package com.example.android.bike;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Set extends BaseActivity implements OnClickListener {

	private TextView general;
	private TextView Unit;
	private Button unit_button;
	private TextView Mode;
	private Button mode_button;
	private TextView wheelsize;
	private TextView wheelset;
	private TextView cm;
	private Button up;
	private Button down;
	private TextView auto;
	private Button selection;
	private TextView battery_tit;
	private TextView auto2;
	private Button switch_button;
	private TextView charg;
	private Button charge;
	private TextView Firmware_tit;
	private TextView Firmware;
	private Button update;
	private Button logout;
	private TextView cruisetext;
	private Button cruisesele;

	Handler textch;
	Handler shere;
	Handler buttonImage;
	
	//title
	private TextView tit_tx;
	private Button back;
	
	

	// save the shared values
	public int unitvalue;
	public int modevalue;
	public int wheelvalue;
	public int autoselectvalue;
	public int autoswitch;
	public int chargevalue;
    public int kmmi;
    private Intent mIntent;  
    private int requestCode;
   public int cruiseselection;
    
	SharedPreferences setting;
	private int resultCode = 4;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title4);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		general = (TextView) findViewById(R.id.general);
		Unit = (TextView) findViewById(R.id.Unit);
		Mode = (TextView) findViewById(R.id.Mode);
		wheelsize = (TextView) findViewById(R.id.wheelsize);
		wheelset = (TextView) findViewById(R.id.wheelset);
		cm = (TextView) findViewById(R.id.cm);
		auto = (TextView) findViewById(R.id.auto);
		battery_tit = (TextView) findViewById(R.id.battery_tit);
		auto2 = (TextView) findViewById(R.id.auto2);
		charg = (TextView) findViewById(R.id.charg);
		Firmware_tit = (TextView) findViewById(R.id.Firmware_tit);
		Firmware = (TextView) findViewById(R.id.Firmware);
		cruisetext = (TextView) findViewById(R.id.cruisetext);
		cruisesele = (Button)findViewById(R.id.cruisesele);
        
		
		mode_button = (Button)findViewById(R.id.mode_button);
		unit_button = (Button) findViewById(R.id.unit_button);
		up = (Button) findViewById(R.id.up);
		down = (Button) findViewById(R.id.down);
		selection = (Button) findViewById(R.id.selection);
		switch_button = (Button) findViewById(R.id.switch_button);
		charge = (Button)findViewById(R.id.charge);
		update = (Button) findViewById(R.id.update);
		logout = (Button) findViewById(R.id.logout);
		
		
		//title
		tit_tx = (TextView)findViewById(R.id.tit_tx);
		back = (Button)findViewById(R.id.back);
		
		

		// set buttons
		mode_button.setOnClickListener(this);
		unit_button.setOnClickListener(this);
		up.setOnClickListener(this);
		down.setOnClickListener(this);
		selection.setOnClickListener(this);
		switch_button.setOnClickListener(this);
		update.setOnClickListener(this);
		logout.setOnClickListener(this);
		charge.setOnClickListener(this);
		back.setOnClickListener(this);
		logout.setOnClickListener(this);
		cruisesele.setOnClickListener(this);
		
		tit_tx.setText("SETTING");

		Handler textch = new Handler();
		textch.post(chtext);

		Handler share = new Handler();
		share.post(sharefunction);

		Handler buttonImage = new Handler();
		buttonImage.post(buttonchange);
		
		 mIntent = new Intent();  
	        mIntent.setClass(Set.this,  
 BluetoothChat.class);  

	}

	// change the typeface
	final private Runnable chtext = new Runnable() {

		@Override
		public void run() {
			Typeface tf = Typeface.createFromAsset(getAssets(),
					"fonts/gotham.ttf");
			general.setTypeface(tf);
			Unit.setTypeface(tf);
			wheelsize.setTypeface(tf);
			wheelset.setTypeface(tf);
			cm.setTypeface(tf);
			auto.setTypeface(tf);
			battery_tit.setTypeface(tf);
			auto2.setTypeface(tf);
			charg.setTypeface(tf);
			Firmware_tit.setTypeface(tf);
			Firmware.setTypeface(tf);
			Mode.setTypeface(tf);
			cruisetext.setTypeface(tf);

		}

	};

	final private Runnable sharefunction = new Runnable() {
// get the setting record when this activity start 
		@Override
		public void run() {

			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			
			
			
			unitvalue = setting.getInt("unit", 1);
			modevalue = setting.getInt("mode", 1);
			cruiseselection = setting.getInt("cruise", 1);
			
			wheelvalue = setting.getInt("wheelsize", 20);
			autoselectvalue = setting.getInt("autose", 1);
			autoswitch = setting.getInt("autosw", 1);
			chargevalue = setting.getInt("charge", 1);

			System.out.println(unitvalue + "<----------");

		}

	};

	final private Runnable buttonchange = new Runnable() {

		@Override
		public void run() {
			if (unitvalue > 2) {
				unitvalue = 1;
			}

			if (unitvalue == 1) {
				unit_button.setBackgroundResource(R.drawable.km);

			} else {
				unit_button.setBackgroundResource(R.drawable.miles);
			}
			
			
			if(cruiseselection == 1){
				cruisesele.setBackgroundResource(R.drawable.on);
			}else if(cruiseselection == 2){
				cruisesele.setBackgroundResource(R.drawable.off);
			}
			
			if (modevalue == 1) {
				mode_button.setBackgroundResource(R.drawable.eco);

			} else if(modevalue == 2){
				mode_button.setBackgroundResource(R.drawable.normal);
			}else if (modevalue == 3){
				mode_button.setBackgroundResource(R.drawable.turbo);
			}
			
			String a = Integer.toString(wheelvalue);
			
			wheelset.setText(a);
			
			if (autoselectvalue == 1) {
				selection.setBackgroundResource(R.drawable.on);

			} else {
				selection.setBackgroundResource(R.drawable.off);
			}


			if (autoselectvalue == 1) {
				selection.setBackgroundResource(R.drawable.on);

			} else {
				selection.setBackgroundResource(R.drawable.off);
			}
			
			
			if (autoswitch == 1) {
				switch_button.setBackgroundResource(R.drawable.on);

			} else {
				switch_button.setBackgroundResource(R.drawable.off);
			}
			
			if (chargevalue == 1) {
				charge.setBackgroundResource(R.drawable.on);

			} else {
				charge.setBackgroundResource(R.drawable.off);
			}
			
			
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.unit_button:

			unitvalue++;

			if (unitvalue > 2) {
				unitvalue = 1;
			}

			if (unitvalue == 1) {
				unit_button.setBackgroundResource(R.drawable.km);

			} else {
				unit_button.setBackgroundResource(R.drawable.miles);
			}
			
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editor = setting.edit();
			editor.putInt("unit", unitvalue);
			editor.commit();

			break;
			
		case R.id.cruisesele:
			
			cruiseselection++;
			
			
			if (cruiseselection>2){
				cruiseselection = 1;
			}
			
			if(cruiseselection == 1){
				cruisesele.setBackgroundResource(R.drawable.on);
			}else if(cruiseselection == 2){
				cruisesele.setBackgroundResource(R.drawable.off);
			}
	
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editora = setting.edit();
			editora.putInt("cruise", cruiseselection);
			editora.commit();
			
			break;
			
		case R.id.mode_button:
			modevalue++;
			if (modevalue > 3) {
				modevalue = 1;
			}

			if (modevalue == 1) {
				mode_button.setBackgroundResource(R.drawable.eco);

			} else if(modevalue == 2){
				mode_button.setBackgroundResource(R.drawable.normal);
			}else if (modevalue == 3){
				mode_button.setBackgroundResource(R.drawable.turbo);
			}
			
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editor2 = setting.edit();
			editor2.putInt("mode", modevalue);
			editor2.commit();
			break;
			
		case R.id.up:
			if (wheelvalue<60){
			wheelvalue++;
			
			String a = Integer.toString(wheelvalue);
			
			wheelset.setText(a);
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editor3 = setting.edit();
			editor3.putInt("wheelsize", wheelvalue);
			editor3.commit();

			}
			break;
			
		case R.id.down:
			if(wheelvalue>9){
				wheelvalue--;

				String a = Integer.toString(wheelvalue);
				
				wheelset.setText(a);
			}
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editor4 = setting.edit();
			editor4.putInt("wheelsize", wheelvalue);
			editor4.commit();
			break;
			
		case R.id.selection:
			autoselectvalue++;

			if (autoselectvalue > 2) {
				autoselectvalue = 1;
			}

			if (autoselectvalue == 1) {
				selection.setBackgroundResource(R.drawable.on);

			} else {
				selection.setBackgroundResource(R.drawable.off);
			}

			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editor5 = setting.edit();
			editor5.putInt("autose", autoselectvalue);
			editor5.commit();
			
			break;
			
		case R.id.switch_button:
			autoswitch++;

			if (autoswitch > 2) {
				autoswitch = 1;
			}

			if (autoswitch == 1) {
				switch_button.setBackgroundResource(R.drawable.on);

			} else {
				switch_button.setBackgroundResource(R.drawable.off);
			}
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editor6 = setting.edit();
			editor6.putInt("autosw", autoswitch);
			editor6.commit();
			
			break;
			
		case R.id.charge:
			chargevalue++;

			if (chargevalue > 2) {
				chargevalue = 1;
			}

			if (chargevalue == 1) {
				charge.setBackgroundResource(R.drawable.on);

			} else {
				charge.setBackgroundResource(R.drawable.off);
			}
			
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editor10 = setting.edit();
			editor10.putInt("charge", chargevalue);
			editor10.commit();
			break;
			
		case R.id.back:
			
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editor1 = setting.edit();
			editor1.putInt("unit", unitvalue);
			editor1.putInt("cruise", cruiseselection);
			editor1.putInt("mode", modevalue);
			editor1.putInt("wheelsize", wheelvalue);
			editor1.putInt("autose", autoselectvalue);
			editor1.putInt("autosw", autoswitch);
			editor1.putInt("charge", chargevalue);
			editor1.putInt("settingonoff", 1);

			editor1.commit();
			
			
			finish();
			break;
			
		case R.id.logout:
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			Editor editor3 = setting.edit();
			editor3.putInt("logout",1);
			editor3.commit();
			SharedPreferences information = getSharedPreferences("information",
					Context.MODE_PRIVATE);
		   int map = information.getInt("map", 0);
			
			Intent mIntent = new Intent();  
			this.setResult(resultCode, mIntent);  
			if (map == 1){
			Intent intent = new Intent(getApplicationContext(), MMap.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			Editor editorz = information.edit();
			editorz.putInt("map", 0);

			editorz.commit();
			
			// close this activity, end the apps
			startActivity(intent);
			//finish();
                ActivityCollector.finishAll();

			}else{
			//	finish();
                ActivityCollector.finishAll();
			}
			
			
			
		}
		

	}

	public void onBackPressed() {

	}
	

}
