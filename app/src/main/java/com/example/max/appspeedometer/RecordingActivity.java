package com.example.max.appspeedometer;

import android.graphics.Color;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class RecordingActivity extends AppCompatActivity {

    private TextView txtSpeed;
    private TextView txtMaxSpeed;
    private TextView txtAccel;
    private TextView txtMaxAccel;
    private Button btnToggle;
    private GPSInfo gps;
    private TextView txtCurrentVehicle;

    private boolean record = true;
    private GraphView graphSpeedAccel;
    private GraphView graphTorqueHP;
    private LineGraphSeries<DataPoint> seriesSpeed;
    private LineGraphSeries<DataPoint> seriesAccel;
    private LineGraphSeries<DataPoint> seriesTorque;
    private LineGraphSeries<DataPoint> seriesHP;
    private double tickCounter = 0d;

    private VehicleData vehicleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        //gettings references to xml objects
        txtSpeed = (TextView) findViewById(R.id.txtSpeed);
        txtMaxSpeed = (TextView) findViewById(R.id.txtMaxSpeed);
        txtAccel = (TextView) findViewById(R.id.txtAccel);
        txtMaxAccel = (TextView) findViewById(R.id.txtMaxAccel);
        btnToggle = (Button) findViewById(R.id.btnToggleTable);
        txtCurrentVehicle = (TextView) findViewById(R.id.textRecordingCurrentVehicle);

        //gps stuff
        gps = new MyGPSInfo();
        gps.startListening(this.getApplicationContext());

        //vehicle data settings
        vehicleData = new VehicleData();
        Globals g = (Globals)getApplication();
        if(g.getCurrentVehicleData() != null) {
            vehicleData = g.getCurrentVehicleData();
            Toast.makeText(getBaseContext(), "Selected " + vehicleData.getName() + " for dyno", Toast.LENGTH_LONG).show();
        }
        txtCurrentVehicle.setText("Vehicle: " + vehicleData.getName());

        ////graph stuff//////
        //speed/accel graph
        graphSpeedAccel = (GraphView) findViewById(R.id.graphSpeed);
        graphSpeedAccel.getGridLabelRenderer().setLabelVerticalWidth(45);
        graphSpeedAccel.getViewport().setXAxisBoundsManual(true);
        graphSpeedAccel.getViewport().setMaxX(40);

        seriesSpeed = new LineGraphSeries<>();
        graphSpeedAccel.addSeries(seriesSpeed);
        //accel series
        seriesAccel = new LineGraphSeries<>();
        seriesAccel.setColor(Color.GREEN);
        graphSpeedAccel.getSecondScale().addSeries(seriesAccel);
        graphSpeedAccel.getSecondScale().setMinY(0);
        graphSpeedAccel.getSecondScale().setMaxY(1);

        //torque graph
        graphTorqueHP = (GraphView) findViewById(R.id.graphAccel);
        graphTorqueHP.getGridLabelRenderer().setLabelVerticalWidth(70);
        graphTorqueHP.getViewport().setXAxisBoundsManual(true);
        graphTorqueHP.getViewport().setMinX(0);
        graphTorqueHP.getViewport().setMaxX(40);

        seriesTorque = new LineGraphSeries<>();
        graphTorqueHP.addSeries(seriesTorque);

        seriesHP = new LineGraphSeries<>();
        seriesHP.setColor(Color.RED);
        graphTorqueHP.getSecondScale().addSeries(seriesHP);
        graphTorqueHP.getSecondScale().setMinY(0);
        graphTorqueHP.getSecondScale().setMaxY(150);

    }

    class MyGPSInfo extends GPSInfo{
        @Override
        void onUpdateLocation() {
            txtSpeed.setText("MPH: " + String.valueOf(round2(currentSpeed, 2)));
            txtMaxSpeed.setText("Max:  " + String.valueOf(round2(maxSpeed, 2)));
            txtAccel.setText("G:   " + String.valueOf(round2((currentAccelG), 2)));
            txtMaxAccel.setText("Max: " + String.valueOf(round2((maxAccelG), 2)));

            if(record == true) {
                seriesSpeed.appendData(new DataPoint(tickCounter, currentSpeed), true, 60);
                seriesAccel.appendData(new DataPoint(tickCounter, (currentAccelG)), true, 40);

                if(currentAccelG >= 0) {
                    float currentTorque = calculateTorque(currentAccelG);

                    seriesTorque.appendData(new DataPoint(tickCounter, currentTorque), true, 40);
                    seriesHP.appendData(new DataPoint(tickCounter, calculateHP(currentTorque, currentSpeed)), true, 40);
                }
                else {
                    seriesTorque.appendData(new DataPoint(tickCounter, 0.0f), true, 40);
                    seriesHP.appendData(new DataPoint(tickCounter, 0.0f), true, 40);
                }
                tickCounter += 1d;
            }
        }
    }

    float calculateRPM(float speed){
        float engineRPM = (vehicleData.getGearFinal()*vehicleData.getGear2()*speed)/(0.00595f*vehicleData.getGearFinal());
        return engineRPM;
    }

    float calculateTorque(float AccelG){
        float wheelTorque = AccelG*vehicleData.getWeight();
        float engineTorque = (wheelTorque*vehicleData.getTireRadius())/(vehicleData.getGearFinal()*vehicleData.getGear2()*12);
        return engineTorque;
    }

    float calculateHP(float torque, float speed){
        float engineHP = calculateRPM(speed)*torque/5252;
        return engineHP;
    }

    public void clearMax(View view) {
        gps.clearMax(view);
    }

    public void Toggle(View view) {
        if(record == false){
            record = true;
            btnToggle.setText("Started");
        }
        else{
            record = false;
            btnToggle.setText("Stopped");
        }
    }

    public void Save(View view) {
        Globals g = (Globals)getApplication();
        VehicleData vehicleData = g.getCurrentVehicleData();
        Toast.makeText(getBaseContext(),"Selected " + vehicleData.getName() + " for dyno", Toast.LENGTH_LONG).show();

        txtCurrentVehicle.setText("Vehicle: " + vehicleData.getName());
    }
}
