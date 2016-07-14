package com.example.android.bike;

import android.app.Application;
/**
 * Created by Philip on 11/4/2016.
 */
public class MyAPP extends Application {

    private MMap.GetSpeedHandler getspeedhandler = null;

    public void setHandler(MMap.GetSpeedHandler getspeedhandler){
        this.getspeedhandler = getspeedhandler;
    }

    public  MMap.GetSpeedHandler getHandler(){
        return getspeedhandler;
    }


}
