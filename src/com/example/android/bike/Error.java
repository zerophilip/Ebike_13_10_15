package com.example.android.bike;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class Error extends BaseActivity implements OnTouchListener {
	
private TextView errorexit;
    public static Activity fa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_error);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		errorexit = (TextView)findViewById(R.id.errorexit);
		
		errorexit.setOnTouchListener(this);
		
		fa=this;

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.errorexit:
		finish();
			break;

            default:
			break;
		}
		return false;
	}


}
