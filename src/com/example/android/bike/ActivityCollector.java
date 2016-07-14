package com.example.android.bike;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philip on 23/3/2016.
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();


    public static void addActivity(Activity activity){
        activities.add(activity);

    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);


    }
    public static void finishAll(){

        for(Activity activity: activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }

    }

    public static void finishActivity(Activity activity){
        activity.finish();
    }
}
