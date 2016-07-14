package com.example.android.bike;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button signup;
	private Button signin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		signup = (Button)findViewById(R.id.signup);
		signin = (Button)findViewById(R.id.signin);
		
		
		signup.setOnClickListener(this);
		signin.setOnClickListener(this);
		

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.signup:
			Intent intent1 = new Intent(this, SignUp.class);
			startActivity(intent1);
			
			break;
			
		case R.id.signin:
			Intent intent2 = new Intent(this,Login.class);
			startActivity(intent2);

		
		}
	}
	// press back key
	public void onBackPressed() {
		
		// clean the map activity when it's on stop
		Intent intent = new Intent(getApplicationContext(), MMap.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);
		
		
		// close this activity, end the apps
		startActivity(intent);
		finish();

	}

}
