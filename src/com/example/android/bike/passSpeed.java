package com.example.android.bike;

import java.io.Serializable;

/**
 * Created by rikoli on 8/4/2016.
 */
public class passSpeed implements Serializable {
   // private static final long serialVersionUID = -6919461967497580385L;

    private String map_speed;

   public void setSpeed(String map_speed){

       this.map_speed = map_speed;

   }


    public String getSpeed(){
        return map_speed;
    }

}
