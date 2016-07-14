package com.example.android.bike;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MMap extends BaseFragmentActivity implements OnClickListener,
		LocationListener, android.location.LocationListener,
		SensorEventListener {
    private passSpeed speedpass;
	private GoogleMap mMap;
	private EditText etLocation;
	MarkerOptions markerOptions;
	MarkerOptions markerOptions1;
	private Marker markerMe;
	LatLng latLng1;
	LatLng myPosition;
	double lat = 0.0, lng = 0.0;
	private String provider;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private float[] mRotationMatrix = new float[16];
	public float getDeclination;
	private GeomagneticField geomagneticfield;
	protected TextView tit_tx;
	//final Handler myHandler = new Handler();
	public int bluetoothvalue;
	public int kmmi;
	protected Button back;
	SharedPreferences information;
	//private TextView kmmi_map;
	//private TextView speed_map;
	//private TextView wbatt_map;
	//private TextView batt_map;
	public int passb1v;
	public int passb2v;
	public int passb3v;

    private GetSpeedHandler getspeedhandler = null;


    int batt_information = 11;


    private MyAPP mAPP = null;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_map);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title4);
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		if (getIntent().getBooleanExtra("EXIT", false)) {
		    finish();
		    information = getSharedPreferences("settingpage",
					Context.MODE_PRIVATE);
			kmmi = information.getInt("unit", 1);
		}

		setUpMapIfNeeded();
		
		Button btn_find = (Button) findViewById(R.id.btn_find);
		tit_tx = (TextView) findViewById(R.id.tit_tx);
		back = (Button)findViewById(R.id.back);
		
		/*kmmi_map = (TextView)findViewById(R.id.kmmi_map);
		speed_map = (TextView)findViewById(R.id.speed_map);
		wbatt_map = (TextView)findViewById(R.id.wbatt_map);
		batt_map = (TextView)findViewById(R.id.batt_map);*/
		back.setOnClickListener(this);

		tit_tx.setText("NAVIGATION");
		
		
		SharedPreferences icons = getSharedPreferences("3icon",
				Context.MODE_PRIVATE);

		bluetoothvalue = icons.getInt("bluetoothicon", 0);

		
		

		// Defining button click event listener for the find button
		btn_find.setOnClickListener(this);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// set 1 sec and 1 meter update
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000L, 1.0f, this);
		boolean isGPS = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		/*Timer myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				UpdateGUI();
			}
		}, 0, 1000);*/



        mAPP = (MyAPP) getApplication();
        getspeedhandler = new GetSpeedHandler();

        mAPP.setHandler(getspeedhandler);




    }

	public void SensorActivity() {
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.btn_find:
			EditText etLocation = (EditText) findViewById(R.id.et_location);

			// Getting user input location
			String location = etLocation.getText().toString();

			if (location != null && !location.equals("")) {
				
				try {
					new GeocoderTask().execute(location);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			break;
			
			//press it than back
		case R.id.back:
			Intent intent2 = new Intent(this, BluetoothChat.class);
			intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent2);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// setUpMapIfNeeded();
	    information = getSharedPreferences("settingpage",
				Context.MODE_PRIVATE);
		kmmi = information.getInt("unit", 1);
	}

	// An AsyncTask class for accessing the GeoCoding Web Service
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getBaseContext());
			List<Address> addresses = null;
			System.out.println("address" + addresses);

			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
				System.out.println("address" + addresses);

			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getBaseContext(), "No Location found",
						Toast.LENGTH_SHORT).show();
			}

			// Clears all the existing markers on the map
			mMap.clear();

			// Adding Markers on Google Map for each matching address
			try {
				for (int i = 0; i < addresses.size(); i++) {

					Address address = (Address) addresses.get(i);

					// Creating an instance of GeoPoint, to display in Google Map
					latLng1 = new LatLng(address.getLatitude(),
							address.getLongitude());

					String addressText = String.format(
							"%s, %s",
							address.getMaxAddressLineIndex() > 0 ? address
									.getAddressLine(0) : "", address
									.getCountryName());

					markerOptions1 = new MarkerOptions();
					markerOptions1.position(latLng1);
					markerOptions1.title("finish");

					mMap.addMarker(markerOptions1);
					if (latLng1 != null) {

						String url1 = getDirectionsUrl(myPosition, latLng1);

						DownloadTask downloadTask = new DownloadTask();

						// Start downloading json data from Google Directions API
						downloadTask.execute(url1);
						// getDirectionsUrl(myPosition,latLng1);
					}
					// Locate the first location
					if (i == 0)
						mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
	}


	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}


	public void setUpMap() {
		mMap.setMyLocationEnabled(true);

		Criteria criteria = new Criteria();
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		provider = locationManager.getBestProvider(criteria, true);

		provider = LocationManager.GPS_PROVIDER;

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;

		}

		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;

		}

		Location location = locationManager.getLastKnownLocation(provider);

		System.out.println(location);

		if (location != null) {
			onLocationChanged(location);
		}
		locationManager.requestLocationUpdates(provider, 5000, 0, this);

	}

	public void onLocationChanged(Location location) {

		// Getting latitude of the current location
		double latitude = location.getLatitude();

		// Getting longitude of the current location
		double longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		myPosition = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));

		// Zoom in the Google Map
		mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
		if (markerMe != null) {
			markerMe.remove();
		}
		markerOptions = new MarkerOptions();
		markerOptions.position(myPosition);
		markerOptions.title("start");

		markerMe = mMap.addMarker(markerOptions);

		// Setting latitude and longitude in the TextView tv_location
		if (latLng1 != null) {

			String url2 = getDirectionsUrl(myPosition, latLng1);

			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url2);

		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			SensorManager.getRotationMatrixFromVector(mRotationMatrix,
					event.values);
			float[] orientation = new float[3];
			SensorManager.getOrientation(mRotationMatrix, orientation);
			float bearing = (float) (Math.toDegrees(orientation[0]) + geomagneticfield
					.getDeclination());
			updateCamera(bearing);
		}
	}

	private void updateCamera(float bearing) {
		CameraPosition oldPos = mMap.getCameraPosition();

		CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing)
				.build();
		mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Travelling Mode
		String mode = "mode=driving";

		String avoid = "highways";

		// waypoints,116.32885,40.036675

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
				+ mode + "&" + avoid;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;
		System.out.println("getDerectionsURL--->: " + url);
		return url;

	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service

			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions

				lineOptions.addAll(points);
				lineOptions.width(5);
				lineOptions.color(Color.RED);
			}

			// Drawing polyline in the Google Map for the i-th route
			try {
				mMap.addPolyline(lineOptions);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	/*private void UpdateGUI() {

		// tv.setText(String.valueOf(i));
		myHandler.post(myRunnable);
	}

	final Runnable myRunnable = new Runnable() {
		public void run() {
			
			if(kmmi == 1){
				kmmi_map.setText("KM/HR");
			}else{
				kmmi_map.setText("M/HR");
			}
			
			information = getSharedPreferences("information",
					Context.MODE_PRIVATE);
			String currkm = information.getString("currspeed", "0");
			SharedPreferences batteryshare = getSharedPreferences("battery",
					Context.MODE_PRIVATE);
			passb1v = batteryshare.getInt("b1", 0);
			passb2v = batteryshare.getInt("b2", 0);
			passb3v = batteryshare.getInt("b3", 0);


            //Intent intent = getIntent();
            //speedpass = (passSpeed) intent.getExtras().getSerializable("com.example.android.bike");

            ///speed_map.setText(speedpass.getSpeed());



*//*
			if (passb1v >= 111) {
				

				passb1v = passb1v - 128;
				speed_map.setText(passb1v);
				wbatt_map.setText("BATT1");

			}

			if (passb2v >= 111) {
			
				passb2v = passb2v - 128;
				speed_map.setText(passb2v);
				wbatt_map.setText("BATT2");

			}

			if (passb3v >= 111) {
			

				passb3v = passb3v - 128;
				
				speed_map.setText(passb3v);
				wbatt_map.setText("BATT3");

			}
			*//*

		}
	};*/
	@Override
	public synchronized void onPause() {
		super.onPause();
		System.out.println("onPause");
		
	}

	@Override
	public void onStop() {
		super.onStop();
		System.out.println("onStop");
	

	}
	// press back key do nothing
		 public void onBackPressed() {  }



    final class GetSpeedHandler extends Handler{
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            //Object[] data = new Object[2];

             //data[0] = (Object)msg.obj;

            ArrayList getdata = new ArrayList();

           getdata = (ArrayList)msg.obj;


            if(msg.what == 10){

/*
               speed_map.setText((String)getdata.get(0));

               kmmi_map.setText((String)getdata.get(1));*/
//                Log.d("msg",getSpeed);
               // System.out.println("map speed"+getSpeed);
            }

            if(msg.what == batt_information){

             //  String battery = Integer.toString((String)getdata.get(1));
/*
             wbatt_map.setText((String)getdata.get(0));
                batt_map.setText((String)getdata.get(1)+"%");*/



            }



        }
    }
}