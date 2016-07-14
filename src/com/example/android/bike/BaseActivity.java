package com.example.android.bike;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Philip on 23/3/2016.
 */
public class BaseActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Log.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);




    }

    protected  void onDestroy(){
        super.onDestroy();

        ActivityCollector.removeActivity(this);
        Log.d("BaseActivity","off");
    }
}
