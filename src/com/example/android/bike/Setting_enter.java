package com.example.android.bike;

import java.util.Arrays;



import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Setting_enter extends Activity implements OnClickListener {
	private Button wheel_button;
	private EditText edit_wheel;
	private TextView wheel;
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	private TextView top_speed;
	private EditText edit_speed;
	private TextView dop;
	private EditText edit_dd;
	public String head = "Ac";

	private static Handler handler = new Handler();

	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	// password
	public String pw = "123456";
	private BluetoothChatService mChatService = null;
	private StringBuffer mOutStringBuffer;
	private String mConnectedDeviceName = null;
	private ArrayAdapter<String> mConversationArrayAdapter;
	private BluetoothAdapter mBluetoothAdapter = null;

	private static final String TAG = "BluetoothChat";
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	private static final boolean D = true;
	SharedPreferences sharedpreferences;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_enter);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		wheel_button = (Button) findViewById(R.id.wheel_button);
		edit_wheel = (EditText) findViewById(R.id.edit_wheel);
		wheel = (TextView) findViewById(R.id.wheel);

		dop = (TextView) findViewById(R.id.dop);
		edit_dd = (EditText) findViewById(R.id.edit_dd);
		wheel_button.setOnClickListener(this);
		sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
		mChatService = new BluetoothChatService(this, mHandler);
		top_speed = (TextView) findViewById(R.id.top_speed);
		edit_speed = (EditText) findViewById(R.id.edit_speed);
		// TODO Auto-generated method stub
	}

	public void onstart() {
		super.onStart();
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}
	}

	private final void setStatus(int resId) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(resId);
	}

	private final void setStatus(CharSequence subTitle) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(subTitle);
	}

	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);

		}
	}

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

	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	private void setupChat() {
		Log.d(TAG, "setupChat()");

		// Initialize the array adapter for the conversation thread
		mConversationArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.message);

		Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),
				"dig.ttf");

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					setStatus(getString(R.string.title_connected_to,
							mConnectedDeviceName));
					// mConversationArrayAdapter.clear();
					break;
				case BluetoothChatService.STATE_CONNECTING:
					setStatus(R.string.title_connecting);
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					setStatus(R.string.title_not_connected);
					break;
				}
				break;
			/*
			 * case MESSAGE_WRITE: byte[] writeBuf = (byte[]) msg.obj; //
			 * construct a string from the buffer String writeMessage = new
			 * String(writeBuf); //mConversationArrayAdapter.add("Me:  " +
			 * writeMessage); break; case MESSAGE_READ: byte[] readBuf =
			 * (byte[]) msg.obj;
			 * 
			 * System.out.println("byte[0]: "+readBuf[0]);
			 * System.out.println("byte[1]: "+readBuf[1]);
			 * System.out.println("byte[2]: "+readBuf[2]);
			 * System.out.println("byte[3]: "+readBuf[3]); // construct a string
			 * from the valid bytes in the buffer String readMessage = new
			 * String(readBuf, 0, msg.arg1); System.out.println(readMessage);
			 * //mConversationArrayAdapter.add(mConnectedDeviceName+":  " +
			 * readMessage); Arrays.fill( readBuf, (byte) 0 ); break;
			 */
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				break;

			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	private void connectDevice(Intent data, boolean secure) {
		// get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the Device
		mChatService.connect(device, secure);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wheel_button:
			Editor editor3 = sharedpreferences.edit();

			String getwheel = edit_wheel.getText().toString();
			editor3.putString("wheel_value", getwheel);
			// editor3.putInt("wheel_value",Integer.parseInt(edit_wheel.getText().toString()));

			// top speed
			String getspeed = edit_speed.getText().toString();
			editor3.putString("speed_value", getspeed);

			// "Date of Purchase
			String getdop = edit_dd.getText().toString();
			editor3.putString("dop_value", getdop);

			head = head.concat(pw);
			head = head.concat(getdop);
			head = head.concat(getwheel);
			head = head.concat(getspeed);

			System.out.println(head);

			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = Message.obtain();
					msg.what = 1;
					msg.obj = head;
					handler.sendMessage(msg);
				}
			}).start();

			editor3.putString("head", head);
			editor3.putInt("select", 1);

			editor3.commit();

			byte[] value = new byte[18];
			byte[] b = head.getBytes();
			value = b;

			this.finish();
			break;

		}

	}

}
