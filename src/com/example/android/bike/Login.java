package com.example.android.bike;









import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener,OnTouchListener{
	private EditText email_ilogin;
	private EditText password_ilogin;
	public String email;
	public String password;
	private Button signin;
	protected ImageView back;
	
	LoginDataBaseAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title2);
		
		email_ilogin = (EditText)findViewById(R.id.email_ilogin);
		password_ilogin = (EditText)findViewById(R.id.password_ilogin);
		signin = (Button) findViewById(R.id.signin);
		//back = (ImageView) findViewById(R.id.back);
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
	     loginDataBaseAdapter=loginDataBaseAdapter.open();
		signin.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.signin:
			email = email_ilogin.getText().toString();	
			password = password_ilogin.getText().toString();
			
			String storedPassword=loginDataBaseAdapter.getSinlgeEntry(email);
			
			
			System.out.println("storedPassword: "+ storedPassword);
			
			if(password.equals(storedPassword))
			{
				Toast.makeText(Login.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
				
				Intent intent1 = new Intent(this, BluetoothChat.class);
				startActivity(intent1);
			
			}
			else
			{
				Toast.makeText(Login.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
			}
		
		}
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			
		}
		
		return false;
}
	
	
}