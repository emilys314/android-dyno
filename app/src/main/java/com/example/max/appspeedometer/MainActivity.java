package com.example.max.appspeedometer;

import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickDyno(View view) {
        Intent viewRecord = new Intent(MainActivity.this, RecordingActivity.class   );
        startActivity(viewRecord);
    }

    public void onClickMonitor(View view) {
    }

    public void onClickLogs(View view) {
    }

    public void onClickVehicles(View view) {
        Intent vehicles = new Intent(MainActivity.this, VehiclesActivity.class   );
        startActivity(vehicles);
    }

    public void onClickSettings(View view) {
    }

    public void onClickCalculator(View view) {
    }

}
