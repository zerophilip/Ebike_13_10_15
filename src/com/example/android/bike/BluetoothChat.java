package com.example.android.bike;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//km/h = 2*pi*r*RPS(WHAT I GET)*3600/100000

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends BaseActivity implements OnClickListener,
		OnTouchListener {
	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;
	public int oldfoo = 0;
    public passSpeed speedpass ;
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final double pi = 3.14159265359;
	public String wheel = "100";
	public int inwheel = 100;
	int numberOfTimesToBlink = 200;
	long blinkInterval = 500;
	double rrss = 0;
	byte[] c = new byte[10];

	// sharedPreferences setting
	private SharedPreferences settings;
	public int km_m = 0;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	public String maxspeeds;
	public String maxspeedmis;
	Handler error = new Handler();

	// new layout
	public CountDownTimer cruiseLis;
	private Button bt_map;
	private Button bt_battery;
	private ImageView cruise;
	private Button less;
	private Button add;
	public int batteryselect;
    private Button reverse;

	private Button bt_information;
	protected ImageView connect1;
	private TextView batt;
	private TextView batt_value;
	private TextView text1;

	public double maxspeed;


	public double t1;
	public double t2;

	public int b1in = 0;
	public int b2in = 0;
	public int b3in = 0;
	public int connect = 0;
	public int a = 0;
	public int b = 0;

	Handler cruisestart = new Handler();
	Handler cursecheck = new Handler();
	public int settingon = 0;
	public int battselect;

    public int reversed = 1;

	final Handler myHandler = new Handler();
	Handler textface = new Handler();
	Handler sendAc = new Handler();
	Handler endbattery = new Handler();

	// Layout Views
	// private ListView mConversationView;
	private EditText mOutEditText;
	// private Button mSendButton;
	Handler checkab = new Handler();
	Handler timehandler = new Handler();
	Handler resumedo = new Handler();
	Handler startdis = new Handler();
	Handler gps = new Handler();
	Handler sendinfo = new Handler();
    private static Handler getSpeed_battery;
	SharedPreferences setting;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;
	// private TextView time;

	private TextView mmtextview;

	Handler settingRun;
	private Button bt_set;

	public int b1volt_t;
	public int b2volt_t;
	public int b3volt_t;

	public String aa;
	private TextView kmORmi;

	private double lastpassvalue = 0;

	// sharedpreferences setting
	SharedPreferences sharedpreferences;

	SharedPreferences information;

	SharedPreferences icons;
	// show speed
	private TextView distance;
	private TextView distance_miORkm;


	// battery
	public int b_total;
	public int b2_total;
	public int b3_total;

	public int x = 0;

	public int speedkmmi;
	public int settingonoff = 0;

	Handler sendsetting;

	// for timer counting
	public int sec;
	public int mins;
	public int hours;
	Timer myTimer = new Timer();

	public int cruiseonoff = 0;
	public double maxspeedmi = 0;

	public String nowspeed;

	public int cruiseorreal = 0;
	public int unitvalue;

	public int cruiseselection = 0;

	private Intent mIntent;
	private int requestCode;

	public int logout = 0;
	public int startapp;
	public int checkgps;
	public int informationset = 1;



    public String ans_km1;

    private MyAPP mAPP = null;

    private MMap.GetSpeedHandler getspeedhandler = null;

    public boolean error_switch = false;

    private int AaCount = 0;


	@Override
	// start the activity
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");

		settings = getSharedPreferences("tt", Context.MODE_PRIVATE);

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.page1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title3);
		Intent intent2 = new Intent(BluetoothChat.this, OPTIMUS.class);
		startActivity(intent2);
		km_m = passVaule.getkm();

		// link up id
		textface.post(facetext);
		bt_information = (Button) findViewById(R.id.bt_d);
		bt_map = (Button) findViewById(R.id.bt_map);
		bt_battery = (Button) findViewById(R.id.bt_battery);
		connect1 = (ImageView) findViewById(R.id.connect1);
		batt = (TextView) findViewById(R.id.batt);

		batt_value = (TextView) findViewById(R.id.batt_value);

		text1 = (TextView) findViewById(R.id.text1);

	
		mmtextview = (TextView) findViewById(R.id.speed);

		kmORmi = (TextView) findViewById(R.id.kmORmi);

		distance = (TextView) findViewById(R.id.distance);

		distance_miORkm = (TextView) findViewById(R.id.distance_miORkm);

		bt_set = (Button) findViewById(R.id.bt_set);

		cruise = (ImageView) findViewById(R.id.cruise);
		less = (Button) findViewById(R.id.less);
		add = (Button) findViewById(R.id.add);
        reverse = (Button)findViewById(R.id.reverse);





		cruise.setOnTouchListener(this);
		less.setOnTouchListener(this);
		add.setOnTouchListener(this);

		bt_map.setOnClickListener(this);
		bt_battery.setOnClickListener(this);
		bt_set.setOnClickListener(this);
		bt_information.setOnClickListener(this);
		connect1.setOnTouchListener(this);
		add.setOnClickListener(this);
		less.setOnClickListener(this);
        reverse.setOnClickListener(this);

		mIntent = new Intent();
        mIntent.setClass(BluetoothChat.this, Set.class);

        //check bluetooth
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		gps.post(gpscheck);
		// when the apps start, check the unit setting

		setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
		// show km text
		/*speedkmmi = setting.getInt("unit", 1);
		try {

			if (speedkmmi == 1) {
				kmORmi.setText("KM/HR");
				distance_miORkm.setText("km");
			} else {

				// unit is mph
				kmORmi.setText("MPH");
				distance_miORkm.setText("mph");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("speed: " + speedkmmi);*/

        // check km or mi selection
       setKmOrMi();

		cruiseselection = setting.getInt("cruise", 1);
		// check the setting, and selection the cruise button icon
		if (cruiseselection == 1) {
			cruise.setImageResource(R.drawable.cruiseauto);
		} else {
			cruise.setImageResource(R.drawable.cruiseoff);
		}

		sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
		wheel = sharedpreferences.getString("wheel_value", "20");
		inwheel = Integer.parseInt(wheel);

		icons = getSharedPreferences("3icon", Context.MODE_PRIVATE);

		// battery
		SharedPreferences batteryshare = getSharedPreferences("battery",
				Context.MODE_PRIVATE);

		IntentFilter filter1 = new IntentFilter(
				BluetoothDevice.ACTION_ACL_CONNECTED);
		IntentFilter filter2 = new IntentFilter(
				BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		IntentFilter filter3 = new IntentFilter(
				BluetoothDevice.ACTION_ACL_DISCONNECTED);
		this.registerReceiver(mReceiver, filter1);
		this.registerReceiver(mReceiver, filter2);
		this.registerReceiver(mReceiver, filter3);

		SharedPreferences information = getSharedPreferences("information",
				Context.MODE_PRIVATE);
		Editor editor = information.edit();
		editor.putString("maxspeed", maxspeeds);
		editor.putString("currspeed", "0");

		editor.commit();

		startdis.post(disstart);


        cruise.setEnabled(false);

        reverse.setEnabled(false);


       mAPP = (MyAPP) getApplication();









	}

	// onclick listener
	public void onClick(View view) {

		switch (view.getId()) {

		// click the bt_map button, start the MMap activity
		case R.id.bt_map:
			if (checkgps == 1) {
				Intent intent1 = new Intent(this, MMap.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

				startActivity(intent1);
				SharedPreferences information = getSharedPreferences(
						"information", Context.MODE_PRIVATE);
				Editor editor = information.edit();
				editor.putInt("map", 1);

				editor.commit();

			} else {
				Toast.makeText(this, "This device have no gps",
						Toast.LENGTH_LONG).show();
			}

			break;

		case R.id.bt_battery:
			// battery activity button setting

			Intent intent2 = new Intent(this, Battery.class);
			startActivity(intent2);

			break;

		case R.id.bt_set:
			// setting activity button

			requestCode = 4;
			startActivityForResult(mIntent, requestCode);

			break;

		case R.id.bt_d:
			Intent intent4 = new Intent(this, Information.class);
			startActivity(intent4);
			break;
		case R.id.add:

			// cruseonoff = 1, than when the user click the button, will send Aw
			// to bike
			if (cruiseonoff == 1) {

				add.setEnabled(false);
				add.setBackgroundResource(R.drawable.addpress);
				Timer buttonTimer = new Timer();
				// wait 1 sec,than do
				buttonTimer.schedule(new TimerTask() {

					@Override
					public void run() {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								add.setEnabled(true);
								byte[] start = new byte[2];
								start[0] = (byte) 65;
								start[1] = (byte) 119;
								mChatService.write(start);
								add.setBackgroundResource(R.drawable.addon);

							}
						});
					}
				}, 1000);
			}

			break;
		case R.id.less:
			if (cruiseonoff == 1) {

				less.setEnabled(false);
				less.setBackgroundResource(R.drawable.lesspress);
				Timer buttonTimer = new Timer();
				buttonTimer.schedule(new TimerTask() {

					@Override
					public void run() {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								less.setEnabled(true);
								byte[] start = new byte[2];
								start[0] = (byte) 65;
								start[1] = (byte) 118;
								mChatService.write(start);
								less.setBackgroundResource(R.drawable.lesson);

							}
						});
					}
				}, 1000);
			}

            case R.id.reverse:

                if(reversed == 1){

                    reverse.setBackgroundResource(R.drawable.r2);

                    reversed = 2;

                    sendMessage("Ar1");




                }

                else if (reversed == 2){

                    reverse.setBackgroundResource(R.drawable.r1);

                    reversed = 1;

                    sendMessage("Ar2");

                }

		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult

		wheel = sharedpreferences.getString("wheel_value", "100");

		inwheel = Integer.parseInt(wheel);

	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
		SharedPreferences batteryshare = getSharedPreferences("battery",
				Context.MODE_PRIVATE);

		Handler settingRun = new Handler();
		// do runsetting
		settingRun.post(runsetting);

		Handler sendsetting = new Handler();
		sendsetting.post(settingsend);

		resumedo.post(doresume);

	}

	final private Runnable runsetting = new Runnable() {

		@Override
		public void run() {

			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			setKmOrMi();

			//System.out.println("speed: " + speedkmmi);

		}

	};

	final private Runnable settingsend = new Runnable() {

		@Override
		public void run() {

			// when setting activity exit, go back to this activity, than get
			// the setting sharedpreferences, and int to string

			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			//System.out.println("test1:" + cruiseonoff);
			settingonoff = setting.getInt("settingonoff", 0);
			if (settingonoff == 1) {

				startdis.post(disstart);

				unitvalue = setting.getInt("unit", 1);
				String unitvalue_s = Integer.toString(unitvalue);
				cruiseselection = setting.getInt("cruise", 1);
				String cruiselection_s = Integer.toString(cruiseselection);
				//System.out.println("test cruise " + cruiseselection);
				int modevalue = setting.getInt("mode", 1);
				String modevalue_s = Integer.toString(modevalue);
				int wheelvalue = setting.getInt("wheelsize", 20);
				String wheelvalue_s = Integer.toString(wheelvalue);
				int autoselectvalue = setting.getInt("autose", 1);
				String autoselectvalue_s = Integer.toString(autoselectvalue);
				int autoswitch = setting.getInt("autosw", 1);
				String autoswitch_s = Integer.toString(autoswitch);
				int chargevalue = setting.getInt("charge", 1);
				String chargevalue_s = Integer.toString(chargevalue);

				String startsend = "Ac";
				startsend = startsend.concat(unitvalue_s);
				startsend = startsend.concat(cruiselection_s);
				startsend = startsend.concat(modevalue_s);
				startsend = startsend.concat(wheelvalue_s);
				startsend = startsend.concat(autoselectvalue_s);
				startsend = startsend.concat(autoswitch_s);
				startsend = startsend.concat(chargevalue_s);

				//System.out.println(startsend);
				// send the setting information to bike
				byte[] value = new byte[10];
				byte[] b = startsend.getBytes();

				value = b;

				if (connect == 1) {
					mChatService.write(value);
				}
				settingonoff = 0;
				Editor editor = setting.edit();
				editor.putInt("settingonoff", 0);
				editor.commit();

				if (cruiseselection == 1 && cruiseonoff == 0) {
					cruise.setImageResource(R.drawable.cruiseauto);

				}
				if (cruiseselection == 2 && cruiseonoff == 0) {
					cruise.setImageResource(R.drawable.cruiseoff);

				}

				else if (cruiseselection == 1 && cruiseonoff == 1) {
					cruise.setImageResource(R.drawable.cruiseon);
				} else if (cruiseselection == 2 && cruiseonoff == 1) {
					cruise.setImageResource(R.drawable.cruiseon);
				}

			}

		}

	};

	private void setupChat() {
		Log.d(TAG, "setupChat()");

		mConversationArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.message);

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");

	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");

	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");

		// unregisterReceiver(mReceiver);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
		Editor editor1 = sharedpreferences.edit();
		// editor1.putString("tt", "0");
		editor1.putInt("int", 0);

		editor1.commit();

		timehandler.removeCallbacks(myRunnable);
		endbattery.post(batteryend);
		unregisterReceiver(mReceiver);
		// mChatService.stop();

	}

	// battery information reset to 0;
	final private Runnable batteryend = new Runnable() {

		@Override
		public void run() {
			SharedPreferences batteryshare = getSharedPreferences("battery",
					Context.MODE_PRIVATE);
			Editor editor = batteryshare.edit();
			editor.putInt("onoff1", 0);
			editor.putInt("onoff2", 0);
			editor.putInt("onoff3", 0);
			editor.putInt("charge", 0);

			editor.putInt("b1", 0);
			editor.putInt("b2", 0);
			editor.putInt("b3", 0);

			editor.putInt("b1v", 0);
			editor.putInt("b2v", 0);
			editor.putInt("b3v", 0);
			editor.commit();
		}

	};

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discover"
					+ "able");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);

		}
	}

	// press back key do nothing
	public void onBackPressed() {
	}

	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			//Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
		//			.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			//mOutEditText.setText(mOutStringBuffer);
		}
	}

	// The action listener for the EditText widget, to listen for the return key
	private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {
			// If the action is a key-up event on the return key, send the
			// message
			if (actionId == EditorInfo.IME_NULL
					&& event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			if (D)
				Log.i(TAG, "END onEditorAction");
			return true;
		}
	};

	private final void setStatus(int resId) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(resId);
	}

	private final void setStatus(CharSequence subTitle) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(subTitle);
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:

					// when the apps start, send Aa to bike
                    cruise.setEnabled(true);
                    reverse.setEnabled(true);
					// timer start when connect the bluetooth
					try {

						checkab.postDelayed(abcheck, 100);

					} catch (Exception e) {
						// TODO: handle exception

                        //cruise.setEnabled(true);
					}

					/*
					 * new CountDownTimer(10000,1000){ public void onTick(long
					 * millisUntilFinished){ while(true){ new
					 * CountDownTimer(1000, 1000) {
					 * 
					 * public void onTick(long millisUntilFinished) { }
					 * 
					 * public void onFinish() { byte[] start = new byte[2];
					 * start[0] = (byte) 65; start[1] = (byte) 97;
					 * mChatService.write(start); } }.start();
					 * 
					 * if(b==2){ break; } } } public void onFinish(){
					 * 
					 * } }.start();
					 */

					// byte[] start = new byte[2];
					// start[0] = (byte) 65;
					// start[1] = (byte) 97;
					// mChatService.write(start);

					Editor editor = icons.edit();

					editor.putInt("bluetoothicon", 1);
					editor.commit();
					// icons = getSharedPreferences("3icon",
					// Context.MODE_PRIVATE);
					startapp = icons.getInt("bluetoothicon", 0);

					// timer start when connect the bluetooth
					try {

						timehandler.postDelayed(myRunnable, 1000);
					} catch (Exception e) {
						// TODO: handle exception
					}

					break;

				case BluetoothChatService.STATE_CONNECTING:

					timehandler.removeCallbacks(myRunnable);

					break;
				case BluetoothChatService.STATE_LISTEN:

					Editor editor1 = icons.edit();
					timehandler.removeCallbacks(myRunnable);

					editor1.putInt("bluetoothicon", 0);
					editor1.commit();
					startapp = icons.getInt("bluetoothicon", 0);
					break;

				case BluetoothChatService.STATE_NONE:

					Editor editor2 = icons.edit();

					editor2.putInt("bluetoothicon", 0);
					editor2.commit();
                    cruise.setEnabled(false);
                    reverse.setEnabled(false);
					break;
				}

				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				//mConversationArrayAdapter.add(writeMessage);

				break;
			case MESSAGE_READ:
				String readBuf = (String) msg.obj;
				String hander = readBuf.substring(0, 2);


                if(error_switch == true) {
                    Error.fa.finish();
                    error_switch = false;

                }
				if (hander.equals("Ag")) {

					String Sunit = readBuf.substring(2, 3);
					String Scruise = readBuf.substring(3, 4);
					String Smode = readBuf.substring(4, 5);
					String Swheel = readBuf.substring(5, 7);
					String Sautose = readBuf.substring(7, 8);
					String Sautosw = readBuf.substring(8, 9);
					String Scharge = readBuf.substring(9, 10);

					int unit = Integer.valueOf(Sunit);
					int cruise = Integer.valueOf(Scruise);
					int mode = Integer.valueOf(Smode);
					int wheel = Integer.valueOf(Swheel);

					int autose = Integer.valueOf(Sautose);
					int autosw = Integer.valueOf(Sautosw);
					int charge = Integer.valueOf(Scharge);

					setting = getSharedPreferences("settingpage",
							Context.MODE_PRIVATE);
					Editor editor1 = setting.edit();
					editor1.putInt("unit", unit);
					editor1.putInt("cruise", cruise);
					editor1.putInt("mode", mode);
					editor1.putInt("wheelsize", wheel);
					editor1.putInt("autose", autose);
					editor1.putInt("autosw", autosw);
					editor1.putInt("charge", charge);

					editor1.commit();
					setting = getSharedPreferences("settingpage",
							Context.MODE_PRIVATE);
                    cruiseselection = cruise;
					speedkmmi = setting.getInt("unit", 1);


                     setKmOrMi();

					cursecheck.post(checkcurse);

				}

				if (hander.equals("Ab")) {

					// Battery 1 ascii to int
					b = 2;

					String Sbatt1 = readBuf.substring(2, 5);
					String Sind1 = readBuf.substring(5, 6);
					String Sb1volt1 = readBuf.substring(6, 9);

					b1volt_t = Integer.valueOf(Sb1volt1);
					int ind1 = Integer.valueOf(Sind1);
					b_total = Integer.valueOf(Sbatt1);

					// get battery1 data when the apps connect the bike

					if (b_total > 100) {

						b_total = b_total - 128;
						batt.setText("BATT1");
						batt_value.setText(b_total + "%");
						// control the battery display
						final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar2);
						RoundProgressBar.onProgressChange1(b_total);

						b_total = b_total + 128;



                        String batt1 = "BATT1";


                        batt_msg_map(batt1,b_total);








					}

					SharedPreferences batteryshare = getSharedPreferences(
							"battery", Context.MODE_PRIVATE);

					String Sbatt2 = readBuf.substring(9, 12);
					String Sind2 = readBuf.substring(12, 13);
					String Sb1volt2 = readBuf.substring(13, 16);

					b2volt_t = Integer.valueOf(Sb1volt2);
					b2_total = Integer.valueOf(Sbatt2);
					int ind2 = Integer.valueOf(Sind2);

					// get battery1 data when the apps connect the bike

					if (b2_total > 100) {
						b2_total = b2_total - 128;
						batt.setText("BATT2");
						batt_value.setText(b2_total + "%");
						final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar2);
						RoundProgressBar.onProgressChange1(b2_total);
						b2_total = b2_total + 128;

                        String batt2 = "BATT22";


                        batt_msg_map(batt2,b2_total);


					}

					String Sbatt3 = readBuf.substring(16, 19);
					String Sind3 = readBuf.substring(19, 20);
					String Sb1volt3 = readBuf.substring(20, 23);

					b3volt_t = Integer.valueOf(Sb1volt3);
					int ind3 = Integer.valueOf(Sind3);
					b3_total = Integer.valueOf(Sbatt3);

					// get battery1 data when the apps connect the bike

					if (b3_total > 100) {
						b3_total = b3_total - 128;
						batt.setText("BATT3");
						batt_value.setText(b3_total + "%");

						final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar2);
						RoundProgressBar.onProgressChange1(b3_total);

						b3_total = b3_total + 128;


                        String batt3 = "BATT3";


                        batt_msg_map(batt3,b3_total);


					}

					String Sdistance = readBuf.substring(23, 28);

					double distance_t = Integer.valueOf(Sdistance);

					// total distance

					double distance_total2 = distance_t / 10;

					DecimalFormat dff = new DecimalFormat("####.#");
					String distance_string = dff.format(distance_total2);

					information = getSharedPreferences("information",
							Context.MODE_PRIVATE);
					Editor editorr = information.edit();
					editorr.putString("distance", distance_string);
					editorr.commit();

					// read the reset distance

					Editor editor = batteryshare.edit();
					editor.putInt("b1", b_total);
					editor.putInt("onoff1", ind1);

					editor.putInt("b2", b2_total);
					editor.putInt("onoff2", ind2);
					editor.putInt("b3", b3_total);
					editor.putInt("onoff3", ind3);

					editor.putInt("b1v", b1volt_t);
					editor.putInt("b2v", b2volt_t);
					editor.putInt("b3v", b3volt_t);

					editor.commit();

					String Smin = readBuf.substring(28, 30);
					String Shour = readBuf.substring(30, 33);

					int min1 = Integer.valueOf(Smin);
					int hour1 = Integer.valueOf(Shour);

					String maxspeedd = readBuf.substring(33, 35);

					// int maxspeed1 = Integer.valueOf(maxspeed);

					// String maxspeedd = String.valueOf(maxspeed1);

					String Strip = readBuf.substring(35,40);
					
					double tripint = Integer.valueOf(Strip);
					
					
					double trip = tripint / 10;

					//System.out.println("double" + trip);
					DecimalFormat dfff = new DecimalFormat("####.#");
					String tripdistances = dfff.format(trip);
					//System.out.println(tripdistances);

					information = getSharedPreferences("information",
							Context.MODE_PRIVATE);

					Editor editorhaha = information.edit();
					editorhaha.putInt("min", min1);
					editorhaha.putInt("hour", hour1);
					editorhaha.putString("maxspeed", maxspeedd);
					editorhaha.putString("tt", tripdistances);
					editorhaha.commit();

				}

				if (hander.equals("Ah")) {
					
					

					String Sbatt1 = readBuf.substring(2, 5);
					String Sind1 = readBuf.substring(5, 6);
					String Sb1volt1 = readBuf.substring(6, 9);

					b1volt_t = Integer.valueOf(Sb1volt1);
					int ind1 = Integer.valueOf(Sind1);
					b_total = Integer.valueOf(Sbatt1);

					// get battery1 data when the apps connect the bike

					if (b_total > 100) {

						b_total = b_total - 128;
						batt.setText("BATT1");
						batt_value.setText(b_total + "%");
						// control the battery display
						final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar2);
						RoundProgressBar.onProgressChange1(b_total);

						b_total = b_total + 128;
                        String batt1 = "BATT1";


                        batt_msg_map(batt1,b_total);

					}

					SharedPreferences batteryshare = getSharedPreferences(
							"battery", Context.MODE_PRIVATE);

					String Sbatt2 = readBuf.substring(9, 12);
					String Sind2 = readBuf.substring(12, 13);
					String Sb1volt2 = readBuf.substring(13, 16);

					b2volt_t = Integer.valueOf(Sb1volt2);
					b2_total = Integer.valueOf(Sbatt2);
					int ind2 = Integer.valueOf(Sind2);

					// get battery1 data when the apps connect the bike

					if (b2_total > 100) {
						b2_total = b2_total - 128;
						batt.setText("BATT2");
						batt_value.setText(b2_total + "%");
						final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar2);
						RoundProgressBar.onProgressChange1(b2_total);
						b2_total = b2_total + 128;

                        String batt2 = "BATT2";


                        batt_msg_map(batt2,b2_total);

					}

					String Sbatt3 = readBuf.substring(16, 19);
					String Sind3 = readBuf.substring(19, 20);
					String Sb1volt3 = readBuf.substring(20, 23);

					b3volt_t = Integer.valueOf(Sb1volt3);
					int ind3 = Integer.valueOf(Sind3);
					b3_total = Integer.valueOf(Sb1volt3);

					// get battery1 data when the apps connect the bike

					if (b3_total > 100) {
						b3_total = b3_total - 128;
						batt.setText("BATT3");
						batt_value.setText(b3_total + "%");

						final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar2);
						RoundProgressBar.onProgressChange1(b3_total);

						b3_total = b3_total + 128;


                        String batt3 = "BATT3";


                        batt_msg_map(batt3,b3_total);
					}

					Editor editor = batteryshare.edit();
					editor.putInt("b1", b_total);
					editor.putInt("onoff1", ind1);

					editor.putInt("b2", b2_total);
					editor.putInt("onoff2", ind2);
					editor.putInt("b3", b3_total);
					editor.putInt("onoff3", ind3);

					editor.putInt("b1v", b1volt_t);
					editor.putInt("b2v", b2volt_t);
					editor.putInt("b3v", b3volt_t);

					editor.commit();

				}
				// get AK
				if (hander.equals("Ak")) {


                    AaCount++;

                    if(AaCount == 60){

                       sendAa();

                        AaCount = 0;

                    }




					String Srps = readBuf.substring(2,5);
					int rps = Integer.valueOf(Srps);
					
					
					setting = getSharedPreferences("settingpage",
							Context.MODE_PRIVATE);
					inwheel = setting.getInt("wheelsize", 20);

					//System.out.println("wheel: " + inwheel);
					km_m = sharedpreferences.getInt("kmORmi", 1);
					final double ans_km = rps * inwheel * 0.0113;
					information = getSharedPreferences("information",
							Context.MODE_PRIVATE);
					String dtt = information.getString("tt", "00.0");
					String totalDistance = information.getString("distance",
							"0");
					double distancetotal = Double.parseDouble(totalDistance);
					double dttt = Double.parseDouble(dtt);
					//System.out.println("anskm" + ans_km);

					double reset_km = ans_km / 3600;

					dttt = dttt + reset_km;
					distancetotal = distancetotal + reset_km;

					information = getSharedPreferences("information",
							Context.MODE_PRIVATE);
					Editor editor5 = information.edit();

					// editor5.putInt("int", rrrsss);
					String kmpoint = String.valueOf(dttt);
					String distanceString = String.valueOf(distancetotal);
					editor5.putString("tt", kmpoint);
					editor5.putString("distance", distanceString);
					editor5.commit();

					DecimalFormat dff = new DecimalFormat("###.#");
					String reset_km_ttt = dff.format(dttt);
					String rrsss = dff.format(rrss);

					// double rrrsss = Integer.parseInt(rrsss);
					//System.out.println("dtt" + reset_km_ttt);

					double ans_mph = ans_km * 0.62137;

					if (maxspeed < ans_km) {
						maxspeed = ans_km;
						DecimalFormat maxformat = new DecimalFormat("#");
						maxspeeds = maxformat.format(maxspeed);
						information = getSharedPreferences("information",
								Context.MODE_PRIVATE);

					}

					if (speedkmmi == 1) {

						DecimalFormat df = new DecimalFormat("#");
						 ans_km1 = df.format(ans_km);

						int foo = Integer.parseInt(ans_km1);

						final RoundProgressBar RoundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar1);

						if (cruiseorreal == 0) {

							RoundProgressBar.onProgressChange(foo);
							mmtextview.setText(ans_km1 + "");
							//System.out.println("speed: " + ans_km1);
                            //passSpeed speedpass = new passSpeed();
                            //speedpass.setSpeed(ans_km1);

							nowspeed = ans_km1;

                          //  new Thread(new sendMsgToMap()).start();
                            getspeedhandler = mAPP.getHandler();

                            try{
                                // private static Handler getSpeed_battery;
                                Message msgs = Message.obtain();

                                ArrayList sendSpeed = new ArrayList();

                                sendSpeed.add(0,ans_km1);
                                sendSpeed.add(1,"KM/HR");

                                msgs.obj = sendSpeed;


                                //Object[] speed = new Object[1];
                                //speed[0] = new String(ans_km1);

                                //msgs.obj = speed;

                                msgs.what = 10;

                                getspeedhandler.sendMessage(msgs);




                            }catch(Exception e){

                            }



                        }

					}

					if (speedkmmi == 2) {

						DecimalFormat df = new DecimalFormat("#");
						String ans_m1 = df.format(ans_mph);

						int foo = Integer.parseInt(ans_m1);
						if (cruiseorreal == 0) {
							mmtextview.setText(ans_m1 + "");
							nowspeed = ans_m1;

                            getspeedhandler = mAPP.getHandler();


                            try{
                                // private static Handler getSpeed_battery;
                                Message msgs = Message.obtain();
                                //Object[] speed = new Object[1];
                                //speed[0] = new String(ans_m1);
                                ArrayList sendSpeed = new ArrayList();

                                sendSpeed.add(0,ans_m1);
                                sendSpeed.add(1,"M/HR");

                                msgs.obj = sendSpeed;

                                msgs.what = 10;

                                getspeedhandler.sendMessage(msgs);


                            }catch(Exception e){

                            }


                            //passSpeed speedpass = new passSpeed();
                            //speedpass.setSpeed(ans_km1);
						}
						// km_m = passVaule.getkm();

						final RoundProgressBar RoundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
						RoundProgressBar.onProgressChange(foo);

					}

					if (speedkmmi == 1) {
						DecimalFormat df = new DecimalFormat("#");
						String ans_km1 = df.format(ans_km);
						Editor editor = information.edit();
						editor.putString("maxspeed", maxspeeds);
						editor.putString("currspeed", ans_km1);
						editor.commit();
					} else {

						if (maxspeedmi < ans_mph) {
							maxspeedmi = ans_mph;
							DecimalFormat maxformat = new DecimalFormat("#");
							maxspeedmis = maxformat.format(maxspeedmi);
							information = getSharedPreferences("information",
									Context.MODE_PRIVATE);

						}

						DecimalFormat df = new DecimalFormat("#");
						String ans_km1 = df.format(ans_km);
						Editor editor = information.edit();
						editor.putString("maxspeed", maxspeedmis);

						String ans_m1 = df.format(ans_mph);
						editor.putString("currspeed", ans_m1);
						editor.commit();
					}
				}

				if (hander.equals("Ay")) {
					
					String Scharge = readBuf.substring(2,3);
					int charge = Integer.valueOf(Scharge);

					SharedPreferences batteryshare = getSharedPreferences(
							"battery", Context.MODE_PRIVATE);

					Editor editor = batteryshare.edit();
					editor.putInt("charge", charge);
					editor.commit();

					//System.out.println("charge: " + charge);

				}

				if (hander.equals("Az")) {

					error.post(errorrunnable);
					if (cruiseonoff == 1 && cruiseselection == 2) {

						// change the cruise button image button to
						// cruiseoff.png
						cruise.setImageResource(R.drawable.cruiseoff);
						add.setBackgroundResource(R.drawable.add);
						less.setBackgroundResource(R.drawable.less);

						cruiseonoff = 0;

					} else if (cruiseonoff == 1 && cruiseselection == 1) {
						cruise.setImageResource(R.drawable.cruiseauto);
						add.setBackgroundResource(R.drawable.add);
						less.setBackgroundResource(R.drawable.less);

						cruiseonoff = 0;

					}

				}

				if (hander.equals("Ax")) {

					cruiseonoff = 1;
					cruise.setImageResource(R.drawable.cruiseon);
					add.setBackgroundResource(R.drawable.addon);
					less.setBackgroundResource(R.drawable.lesson);

				}

				if (hander.equals("Ap")) {

					if (cruiseonoff == 1 && cruiseselection == 2) {

						// change the cruise button image button to
						// cruiseoff.png
						cruise.setImageResource(R.drawable.cruiseoff);
						add.setBackgroundResource(R.drawable.add);
						less.setBackgroundResource(R.drawable.less);

						cruiseonoff = 0;

					} else if (cruiseonoff == 1 && cruiseselection == 1) {
						cruise.setImageResource(R.drawable.cruiseauto);
						add.setBackgroundResource(R.drawable.add);
						less.setBackgroundResource(R.drawable.less);

						cruiseonoff = 0;

					}

				}

			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);

				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				try {
					connectDevice(data, false);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, false);
			}
			break;

		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();

				finish();

			}
			break;
		case 4:

			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			// sendsetting.post(settingsend);
			logout = setting.getInt("logout", 0);

			if (logout == 1) {
				// endbattery.post(batteryend);
				// mChatService.stop();
				Editor editor = setting.edit();
				editor.putInt("logout", 0);
				editor.commit();
				try {

					finish();

				} catch (Exception e) {
					// TODO: handle exception
				}

				// System.exit(0);
			}

		}
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		try {
			mChatService.connect(device, secure);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// mChatService.connect(device, secure);
		// bt.setImageResource(R.drawable.bt);

		if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
			Animation myFadeInAnimation = AnimationUtils.loadAnimation(
					BluetoothChat.this, R.anim.bt_am);

			return;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;

		case R.id.menu_map:
			Intent intent2 = new Intent(this, MMap.class);
			startActivity(intent2);
			return true;

		case R.id.menu_exit:
			Editor editor1 = sharedpreferences.edit();
			// editor1.putString("tt", "0");
			editor1.putInt("int", 0);

			editor1.commit();
			android.os.Process.killProcess(android.os.Process.myPid());

			return true;

		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Intent serverIntent = null;
		switch (v.getId()) {
		case R.id.connect1:
			// Intent serverIntent1= null;
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

				// Otherwise, setup the chat session
			} else {
				if (mChatService == null)
					setupChat();
				serverIntent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(serverIntent,
						REQUEST_CONNECT_DEVICE_SECURE);

			}

			// serverIntent = new Intent(this, DeviceListActivity.class);
			// startActivityForResult(serverIntent,
			// REQUEST_CONNECT_DEVICE_SECURE);

			break;

		case R.id.cruise:

			if (cruiseselection == 1 && cruiseonoff == 0) {
				byte[] callcruise = new byte[2];
				callcruise[0] = (byte) 65;
				callcruise[1] = (byte) 120;
				mChatService.write(callcruise);
				try {
					cruiseLis = new CountDownTimer(10000, 1000) {

						@Override
						public void onTick(long millisUntilFinished) {
							cruise.setEnabled(false);
							if (cruiseonoff == 1) {

								cruiseLis.cancel();
								cruise.setEnabled(true);
								//System.out.println("cruise:" + cruiseonoff);

							}

						}

						@Override
						public void onFinish() {
							cruise.setEnabled(true);
							if (cruiseonoff == 0) {
								byte[] callcruise = new byte[2];
								callcruise[0] = (byte) 65;
								callcruise[1] = (byte) 105;
								mChatService.write(callcruise);

								cruise.setImageResource(R.drawable.cruiseauto);
								//System.out.println("end");
							}
						}
					}.start();
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (cruiseselection == 2 && cruiseonoff == 0
					&& startapp == 1) {
				cruise.setImageResource(R.drawable.cruiseauto);
				byte[] callcruise = new byte[2];
				callcruise[0] = (byte) 65;
				callcruise[1] = (byte) 120;
				mChatService.write(callcruise);

				try {
					cruiseLis = new CountDownTimer(10000, 1000) {

						@Override
						public void onTick(long millisUntilFinished) {
							cruise.setEnabled(false);
							if (cruiseonoff == 1) {

								cruiseLis.cancel();
								cruise.setEnabled(true);

							}

						}

						@Override
						public void onFinish() {
							cruise.setEnabled(true);
							if (cruiseonoff == 0) {
								byte[] callcruise = new byte[2];
								callcruise[0] = (byte) 65;
								callcruise[1] = (byte) 105;
								mChatService.write(callcruise);
								cruise.setImageResource(R.drawable.cruiseoff);
								//System.out.println("end");
							}
						}
					}.start();
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (cruiseonoff == 1 && cruiseselection == 2) {

				// change the cruise button image button to cruiseoff.png
				cruise.setImageResource(R.drawable.cruiseoff);
				add.setBackgroundResource(R.drawable.add);
				less.setBackgroundResource(R.drawable.less);

				cruiseonoff = 0;
				byte[] start = new byte[2];
				start[0] = (byte) 65;
				start[1] = (byte) 112;

				mChatService.write(start);
				
				
			/*	
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();

				}
				*/
				
				sendAc.post(acsend);

			} else if (cruiseonoff == 1 && cruiseselection == 1) {
				cruise.setImageResource(R.drawable.cruiseauto);
				add.setBackgroundResource(R.drawable.add);
				less.setBackgroundResource(R.drawable.less);

				cruiseonoff = 0;
				byte[] start = new byte[2];
				start[0] = (byte) 65;
				start[1] = (byte) 112;

				mChatService.write(start);
				
				
				/*try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();

				}
				*/
				sendAc.post(acsend);

			}

			break;

		}
		return false;
	}

	// detect the Bluetooth device state
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {

			} else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				connect = 1;

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {

			} else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED
					.equals(action)) {

			} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				// mChatService.stop();
				// mChatService.start();
				endbattery.post(batteryend);
				batt.setText("BATT1");
				batt_value.setText("0" + "%");
				// control the battery display
				final RoundProgressBar2 RoundProgressBar = (RoundProgressBar2) findViewById(R.id.roundProgressBar2);
				RoundProgressBar.onProgressChange1(0);
				Intent serviceIntent = new Intent();
				// restart the service, change to listen mode
				serviceIntent
						.setAction("com.example.android.BluetoothChatService");
				startService(serviceIntent);
				//System.out.println("test");

				if (cruiseselection == 1 && cruiseonoff == 1) {
					cruise.setImageResource(R.drawable.cruiseauto);
					cruiseonoff = 0;
				} else if (cruiseselection == 2 && cruiseonoff == 1) {
					cruise.setImageResource(R.drawable.cruiseoff);
					cruiseonoff = 0;
					connect = 0;
				}

				// stop the timer
				timehandler.removeCallbacks(myRunnable);
				// myTimer.cancel();
				// myTimer.purge();

				Toast.makeText(context, "disconnected", Toast.LENGTH_LONG)
						.show();
				// go back to bluetoothchat activity
				Intent intent2 = new Intent(BluetoothChat.this,
						BluetoothChat.class);
				intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent2);

				try {
					mChatService.start();
				} catch (Exception e) {
					// TODO: handle exception
				}

				Intent serverIntent = null;
				serverIntent = new Intent(BluetoothChat.this,
						DeviceListActivity.class);
				startActivityForResult(serverIntent,
						REQUEST_CONNECT_DEVICE_SECURE);

			}
		}
	};

	private Runnable myRunnable = new Runnable() {

		public void run() {
			information = getSharedPreferences("information",
					Context.MODE_PRIVATE);
			hours = information.getInt("hour", 0);
			mins = information.getInt("min", 0);
			// settingRun.post(runsetting);

			sec++;
			if (sec > 60) {
				sec = 0;
				mins++;

				// send info to bike
				sendinfo.post(infosend);

			}
			if (mins > 60) {
				mins = 0;
				hours++;
			}
			information = getSharedPreferences("information",
					Context.MODE_PRIVATE);

			informationset = information.getInt("set0", 1);

			if (informationset == 0) {

				byte[] start = new byte[2];
				start[0] = (byte) 65;
				start[1] = (byte) 109;
				maxspeed = 0;
				maxspeeds = "0";
				mChatService.write(start);
				distance.setText("0");

				information = getSharedPreferences("information",
						Context.MODE_PRIVATE);

				Editor editor1 = information.edit();
				editor1.putInt("set0", 1);
				editor1.putString("maxspeed", "0");

				editor1.commit();

			}

			String dtt = information.getString("tt", "00.0");

			double dttt = Double.parseDouble(dtt);

			double dtttmi = dttt * 0.62137;

			if (speedkmmi == 1) {
				DecimalFormat dff = new DecimalFormat("###.#");
				String tripdistances = dff.format(dttt);
				distance.setText(tripdistances);
			} else if (speedkmmi == 2) {
				DecimalFormat dff = new DecimalFormat("###.#");
				String tripdistances = dff.format(dtttmi);
				distance.setText(tripdistances);
			}

			/*System.out.println("hour: " + hours + " " + "min: " + mins + " "
					+ "sec: " + sec);
*/
			Editor editor = information.edit();
			editor.putInt("min", mins);
			editor.putInt("hour", hours);
			editor.commit();
			// 1 second
			try {
				timehandler.postDelayed(this, 1000);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	private Runnable facetext = new Runnable() {

		@Override
		public void run() {
			Typeface tf = Typeface.createFromAsset(getAssets(),
					"fonts/gotham.ttf");
			batt.setTypeface(tf);
			batt_value.setTypeface(tf);
			text1.setTypeface(tf);
			mmtextview.setTypeface(tf);
			kmORmi.setTypeface(tf);
			distance.setTypeface(tf);
			distance_miORkm.setTypeface(tf);

		}

	};

	private Runnable doresume = new Runnable() {

		@Override
		public void run() {

			int select1 = sharedpreferences.getInt("select", 0);

			if (select1 == 1) {
				String total = sharedpreferences.getString("head", "zcxv");
				byte[] value = new byte[18];
				byte[] b = total.getBytes();
				//System.out.println("2" + total);
				value = b;

				mChatService.write(value);

			}
			Editor editor3 = sharedpreferences.edit();
			editor3.putInt("select", 0);

			editor3.commit();

			// Performing this check in onResume() covers the case in which BT
			// was
			// not enabled during onStart(), so we were paused to enable it...
			// onResume() will be called when ACTION_REQUEST_ENABLE activity
			// returns.

			SharedPreferences settonoff = getSharedPreferences("set",
					Context.MODE_PRIVATE);
			settingon = settonoff.getInt("setonoff", 0);

			//System.out.println("settonoff " + settingon);

		/*	try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
		
			SharedPreferences batteryshare = getSharedPreferences("battery",
					Context.MODE_PRIVATE);
			battselect = batteryshare.getInt("battselect", 0);
			if (settingon == 1) {
				if (battselect == 1) {
					byte[] start = new byte[3];
					start[0] = (byte) 65;
					start[1] = (byte) 102;
					start[2] = (byte) 49;
					if (connect == 1) {
						mChatService.write(start);
					}
					Editor editor = batteryshare.edit();
					editor.putInt("battselect", 0);
					editor.commit();

				} else if (battselect == 2) {
					byte[] start = new byte[3];
					start[0] = (byte) 65;
					start[1] = (byte) 102;
					start[2] = (byte) 50;
					if (connect == 1) {
						mChatService.write(start);
					}
					Editor editor = batteryshare.edit();
					editor.putInt("battselect", 0);
					editor.commit();
				} else if (battselect == 3) {
					byte[] start = new byte[3];
					start[0] = (byte) 65;
					start[1] = (byte) 102;
					start[2] = (byte) 51;
					if (connect == 1) {
						mChatService.write(start);
					}
					Editor editor = batteryshare.edit();
					editor.putInt("battselect", 0);
					editor.commit();
				}

				settingon = 0;
			}
			//System.out.println("setting value" + settingon);
		}

	};

	final private Runnable errorrunnable = new Runnable() {

		@Override

		public void run() {

            if(error_switch == true){

              Error.fa.finish();
                error_switch = false;
            }
            error_switch = true;
			Intent intent4 = new Intent(BluetoothChat.this, Error.class);
			startActivity(intent4);

		}

	};

	// detect the phone state

	private class CallStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {

			case TelephonyManager.CALL_STATE_RINGING:
				// called when someone is ringing to this phone
				// send message to bike turn off the cruise

				// Ax1
				byte[] start = new byte[2];
				start[0] = (byte) 65;
				start[1] = (byte) 112;

				mChatService.write(start);

				if (cruiseselection == 1 && cruiseonoff == 1) {
					cruise.setImageResource(R.drawable.cruiseauto);
					cruiseonoff = 0;
				} else if (cruiseselection == 2 && cruiseonoff == 1) {
					cruise.setImageResource(R.drawable.cruiseoff);
					cruiseonoff = 0;
				}

				break;
			}
		}

	}

	// check gps device
	public boolean hasGPSDevice(Context context) {
		final LocationManager mgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (mgr == null)
			return false;
		final List<String> providers = mgr.getAllProviders();
		if (providers == null)
			return false;
		return providers.contains(LocationManager.GPS_PROVIDER);
	}

	private final Runnable disstart = new Runnable() {

		@Override
		public void run() {
			SharedPreferences information = getSharedPreferences("information",
					Context.MODE_PRIVATE);
			String dtt = information.getString("tt", "00.0");

			double dttt = Double.parseDouble(dtt);

			double dtttmi = dttt * 0.62137;

			if (speedkmmi == 1) {
				DecimalFormat dff = new DecimalFormat("###.#");
				String tripdistances = dff.format(dttt);
				distance.setText(tripdistances);
			} else if (speedkmmi == 2) {
				DecimalFormat dff = new DecimalFormat("###.#");
				String tripdistances = dff.format(dtttmi);
				distance.setText(tripdistances);
			}
		}

	};
	private final Runnable gpscheck = new Runnable() {

		@Override
		public void run() {
			Context context = BluetoothChat.this;
			PackageManager packageManager = context.getPackageManager();

			if (packageManager
					.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
				checkgps = 1;
			} else {
				checkgps = 0;

			}

		}

	};

	private final Runnable infosend = new Runnable() {

		@Override
		public void run() {

			String infostart = "Ae";

			information = getSharedPreferences("information",
					Context.MODE_PRIVATE);

			int mins = information.getInt("min", 0);
			int hours = information.getInt("hour", 0);
			String maxspeed = information.getString("maxspeed", "0");
			String tripd = information.getString("tt", "0");
			String totaldistance = information.getString("distance", "0");

			int maxspeedd = Integer.parseInt(maxspeed);

			double tripdd = Double.valueOf(tripd);
			double totdis = Double.valueOf(totaldistance);

			tripdd = tripdd * 10;
			totdis = totdis * 10;

			DecimalFormat formatter = new DecimalFormat("00");
			DecimalFormat formatter2 = new DecimalFormat("000");
			DecimalFormat formatter3 = new DecimalFormat("00000");
			String min = formatter.format(mins);
			String hour = formatter2.format(hours);
			String maxspeeddd = formatter.format(maxspeedd);

			String tripddstring = formatter3.format(tripdd);
			totaldistance = formatter3.format(totdis);

			infostart = infostart.concat(min);
			infostart = infostart.concat(hour);
			infostart = infostart.concat(maxspeeddd);
			infostart = infostart.concat(tripddstring);
			infostart = infostart.concat(totaldistance);

			int stringlen = infostart.length();

			byte[] value = new byte[stringlen];

			value = infostart.getBytes();
			//System.out.println(value);
			mChatService.write(value);

		}

	};

	public Runnable abcheck = new Runnable() {

		@Override
		public void run() {

			if (b != 2) {
				/*byte[] start = new byte[2];
				start[0] = (byte) 65;
				start[1] = (byte) 97;

				mChatService.write(start);*/

                sendMessage("Aa");
				try {
					checkab.postDelayed(this, 3000);
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (b == 2) {
				timehandler.removeCallbacks(abcheck);
			}

		}

	};
	public Runnable acsend = new Runnable() {

		@Override
		public void run() {
			setting = getSharedPreferences("settingpage", Context.MODE_PRIVATE);
			unitvalue = setting.getInt("unit", 1);
			String unitvalue_s = Integer.toString(unitvalue);
			cruiseselection = setting.getInt("cruise", 1);
			String cruiselection_s = Integer.toString(cruiseselection);
			//System.out.println("test cruise " + cruiseselection);
			int modevalue = setting.getInt("mode", 1);
			String modevalue_s = Integer.toString(modevalue);
			int wheelvalue = setting.getInt("wheelsize", 20);
			String wheelvalue_s = Integer.toString(wheelvalue);
			int autoselectvalue = setting.getInt("autose", 1);
			String autoselectvalue_s = Integer.toString(autoselectvalue);
			int autoswitch = setting.getInt("autosw", 1);
			String autoswitch_s = Integer.toString(autoswitch);
			int chargevalue = setting.getInt("charge", 1);
			String chargevalue_s = Integer.toString(chargevalue);

			String startsend = "Ac";
			startsend = startsend.concat(unitvalue_s);
			startsend = startsend.concat(cruiselection_s);
			startsend = startsend.concat(modevalue_s);
			startsend = startsend.concat(wheelvalue_s);
			startsend = startsend.concat(autoselectvalue_s);
			startsend = startsend.concat(autoswitch_s);
			startsend = startsend.concat(chargevalue_s);

			//System.out.println(startsend);
			// send the setting information to bike
			byte[] value = new byte[10];
			byte[] b = startsend.getBytes();

			value = b;
			if (connect == 1) {
				mChatService.write(value);
			}
		}

	};
	public Runnable checkcurse = new Runnable() {

		@Override
		public void run() {

			if (cruiseselection == 1 && cruiseonoff == 0) {
				cruise.setImageResource(R.drawable.cruiseauto);
			}
			if (cruiseselection == 2 && cruiseonoff == 0) {
				cruise.setImageResource(R.drawable.cruiseoff);
			}

			else if (cruiseselection == 1 && cruiseonoff == 1) {
				cruise.setImageResource(R.drawable.cruiseon);
			} else if (cruiseselection == 2 && cruiseonoff == 1) {
				cruise.setImageResource(R.drawable.cruiseon);
			}

		}

	};


    void setKmOrMi (){
        try {
            speedkmmi = setting.getInt("unit", 1);
            if (speedkmmi == 1) {
                kmORmi.setText("KM/HR");
                distance_miORkm.setText("km");
            } else {

                // unit is mph
                kmORmi.setText("MPH");
                distance_miORkm.setText("mph");
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println("speed: " + speedkmmi);
    }

    public class sendMsgToMap implements Runnable{

        @Override
        public void run() {
          /*  try{
              // private static Handler getSpeed_battery;
                Message msg = Message.obtain();
                msg.obj = ans_km1;

                msg.what = 10;

                getspeedhandler.sendMessage(msg);

               Log.d("msg",ans_km1);
            }catch(Exception e){

            }*/
        }
    }


    private void batt_msg_map(String batt, int value_batt) {
        getspeedhandler = mAPP.getHandler();
        int batt_information = 11;

       // Object[] batt_map = new Object[1];

       // batt_map[0] = new String(batt);
       // batt_map[1] = new int[value_batt];

        ArrayList sendBattData = new ArrayList();

        String battery = Integer.toString(value_batt);

        sendBattData.add(0,batt);
        sendBattData.add(1,battery);

        try {
            Message msgs = Message.obtain();

            msgs.obj = sendBattData;

            msgs.what = batt_information;


            getspeedhandler.sendMessage(msgs);
        } catch (Exception e) {


        }

    }

    public void sendAa(){
        sendMessage("Aa");
    }
}
