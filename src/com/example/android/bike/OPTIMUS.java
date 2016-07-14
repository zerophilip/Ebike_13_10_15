package com.example.android.bike;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class OPTIMUS extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		    //Remove notification bar
		   // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_opening);
		
		
		new CountDownTimer(2000, 1000) {

		     public void onTick(long millisUntilFinished) {
		        
		     }

		     public void onFinish() {
		    	//Intent intent2 = new Intent(OPTIMUS.this, BluetoothChat.class);
				//	startActivity(intent2);
					finish();
		     }
		  }.start();
		
		
	}
}
