package com.example.android.bike;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by Philip on 23/3/2016.
 */
public class BaseFragmentActivity extends FragmentActivity {


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
