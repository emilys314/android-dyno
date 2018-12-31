package com.example.max.appspeedometer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Max on 3/21/2018.
 */

public abstract class GPSInfo implements LocationListener {

    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    LocationManager locManager;

    float toMPH = 2.237f;
    float prevSpeed = 0;
    float currentSpeed = 0;
    float maxSpeed = 0;
    float currentAccel = 0;
    float maxAccel = 0;
    float currentAccelG = 0;
    float maxAccelG = 0;

    long prevTime = 0;
    long currTime = 0;

    public GPSInfo(){}

    abstract void onUpdateLocation();

    public void startListening(Context baseContext) {
        locManager = (LocationManager) baseContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) baseContext, new String[] {Manifest.permission.ACCESS_FINE_LOCATION  }, MY_PERMISSION_ACCESS_FINE_LOCATION );
            return;
        }
        else {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        prevSpeed = currentSpeed;
        currentSpeed = loc.getSpeed()*toMPH;
        if(currentSpeed > maxSpeed){
            maxSpeed = currentSpeed;
        }

        prevTime = currTime;
        currTime = System.currentTimeMillis();
        if(prevTime > 0){
            currentAccel = 1000*((currentSpeed-prevSpeed)/(currTime-prevTime));
            currentAccelG = currentAccel/21.94f;
            if(currentAccel > maxAccel){
                maxAccel = currentAccel;
                maxAccelG = currentAccel/21.94f;
            }
        }

        onUpdateLocation();
    }
    @Override
    public void onProviderDisabled(String arg0) {}
    @Override
    public void onProviderEnabled(String arg0) {}
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

    public void clearMax(View view) {
        maxSpeed = 0;
        maxAccel = 0;
    }

    public static float round2(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
    }

    public long getCurrTime() {
        return currTime;
    }
}
