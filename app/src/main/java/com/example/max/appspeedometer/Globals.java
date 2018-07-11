package com.example.max.appspeedometer;

import android.app.Application;

/**
 * Created by Max on 7/6/2018.
 */

public class Globals extends Application {
    private VehicleData currentVehicleData;     //vehicle data to use for dyno

    public VehicleData getCurrentVehicleData() {
        return currentVehicleData;
    }

    public void setCurrentVehicleData(VehicleData currentVehicleData) {
        this.currentVehicleData = currentVehicleData;
    }


}
