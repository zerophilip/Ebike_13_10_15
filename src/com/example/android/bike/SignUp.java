package com.example.android.bike;









import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class SignUp extends Activity implements OnTouchListener, OnClickListener{
	LoginDataBaseAdapter loginDataBaseAdapter;
	private EditText name_ed;
	private EditText email_ed;
	private EditText password_ed;
	private EditText password_ed2;
	private TextView tit_tx;
	
	public String name;
	public String email;
	public String password;
	public String password2;
	
	
	
	
protected ImageView back;
private Button register;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_sign_up);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title2);
		//back = (ImageView) findViewById(R.id.back);
		
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();
		
		
	
		
		name_ed = (EditText)findViewById(R.id.name_ed);
		email_ed = (EditText)findViewById(R.id.email_ed);
		password_ed = (EditText)findViewById(R.id.password_ed);
		password_ed2 = (EditText)findViewById(R.id.password_ed2);
		register = (Button)findViewById(R.id.register);
		tit_tx = (TextView)findViewById(R.id.tit_tx);
		
		tit_tx.setText("LOGIN");
		
		back.setOnTouchListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			
		}
		
		return false;
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		switch(v.getId()){
		case R.id.register:
			name = name_ed.getText().toString();
			System.out.println("name: "+name);
			email = email_ed.getText().toString();
			System.out.println("email: "+email);
			password = password_ed.getText().toString();
			System.out.println("password: "+password);
			password2 = password_ed2.getText().toString();
			System.out.println();
			System.out.println("password2: "+password2);
			if (name.matches("")||email.matches("")||password.matches("")||password2.matches("")) {
			    Toast.makeText(this, "Plase input all data", Toast.LENGTH_SHORT).show();
			    return;
			}else{
				if (password.matches(password2)){
					
					loginDataBaseAdapter.insertEntry(name,email, password);
				    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
					
					finish();
					
					
				}else{
					 Toast.makeText(this, "password not match", Toast.LENGTH_SHORT).show();
				}
			}
			
		}
	}
}
