package com.example.max.appspeedometer;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class RecordingActivity extends AppCompatActivity {

    //xml gui objects
    private EditText editGear;
    private EditText editWeight;
    private TextView txtSpeed;
    private TextView txtMaxSpeed;
    private TextView txtAccel;
    private TextView txtMaxAccel;
    private TextView txtTorque;
    private TextView txtMaxTorque;
    private TextView txtPower;
    private TextView txtMaxPower;
    private Button btnToggle;
    private GPSInfo gps;
    private TextView txtCurrentVehicle;

    private boolean record = false;
    private boolean refreshGraph = true;
    private GraphView graphSpeedAccel;
    private GraphView graphTorquePower;
    private LineGraphSeries<DataPoint> seriesSpeed;
    private LineGraphSeries<DataPoint> seriesAccel;
    private LineGraphSeries<DataPoint> seriesTorque;
    private LineGraphSeries<DataPoint> seriesPower;
    private double tickCounter = 0d;

    private VehicleData vehicleData;        //vehicle data object
    private DynoRunData dynoRunData;

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        //gettings references to xml objects
        editGear = (EditText) findViewById(R.id.editRecordingGear);
        editWeight = (EditText) findViewById(R.id.editRecordingWeight);
        txtSpeed = (TextView) findViewById(R.id.txtSpeed);
        txtMaxSpeed = (TextView) findViewById(R.id.txtMaxSpeed);
        txtAccel = (TextView) findViewById(R.id.txtAccel);
        txtMaxAccel = (TextView) findViewById(R.id.txtMaxAccel);
        txtTorque = (TextView) findViewById(R.id.txtTorque);
        txtMaxTorque = (TextView) findViewById(R.id.txtMaxTorque);
        txtPower = (TextView) findViewById(R.id.txtPower);
        txtMaxPower = (TextView) findViewById(R.id.txtMaxPower);
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
        if (vehicleData.getName().length() > 0)
            txtCurrentVehicle.setText("Vehicle: " + vehicleData.getName());
        else
            txtCurrentVehicle.setText("No vehicle selected!");

        ////graph stuff//////
        //speed/accel graph
        graphSpeedAccel = (GraphView) findViewById(R.id.graphSpeed);
        graphSpeedAccel.getGridLabelRenderer().setLabelVerticalWidth(45);
        graphSpeedAccel.getViewport().setXAxisBoundsManual(true);
        graphSpeedAccel.getViewport().setMaxX(40);

        seriesSpeed = new LineGraphSeries<>();
        seriesSpeed.setColor(ContextCompat.getColor(this, R.color.Speed));
        graphSpeedAccel.addSeries(seriesSpeed);
        //accel series
        seriesAccel = new LineGraphSeries<>();
        seriesAccel.setColor(ContextCompat.getColor(this, R.color.Accel));
        graphSpeedAccel.getSecondScale().addSeries(seriesAccel);
        graphSpeedAccel.getSecondScale().setMinY(0);
        graphSpeedAccel.getSecondScale().setMaxY(1);

        //torque graph
        graphTorquePower = (GraphView) findViewById(R.id.graphAccel);
        graphTorquePower.getGridLabelRenderer().setLabelVerticalWidth(70);
        graphTorquePower.getViewport().setXAxisBoundsManual(true);
        graphTorquePower.getViewport().setMinX(0);
        graphTorquePower.getViewport().setMaxX(40);

        seriesTorque = new LineGraphSeries<>();
        seriesTorque.setColor(ContextCompat.getColor(this, R.color.Torque));
        graphTorquePower.addSeries(seriesTorque);

        seriesPower = new LineGraphSeries<>();
        seriesPower.setColor(ContextCompat.getColor(this, R.color.Power));
        //seriesPower.setColor(Color.parseColor(R.color.Power));
        graphTorquePower.getSecondScale().addSeries(seriesPower);
        graphTorquePower.getSecondScale().setMinY(0);
        graphTorquePower.getSecondScale().setMaxY(150);

    }

    //onResume refreshes vehicle data
    @Override
    protected void onResume() {
        super.onResume();

        //vehicle data settings
        Globals g = (Globals)getApplication();
        if(g.getCurrentVehicleData() != null) {
            vehicleData = g.getCurrentVehicleData();
            Toast.makeText(getBaseContext(), "Selected " + vehicleData.getName() + " for dyno", Toast.LENGTH_LONG).show();
        }
        if (vehicleData.getName().length() > 0)
            txtCurrentVehicle.setText("Vehicle: " + vehicleData.getName());
        else
            txtCurrentVehicle.setText("No vehicle selected!");
    }

    //gps info accessor class
    class MyGPSInfo extends GPSInfo{
        float currentTorque = 0.0f;
        float maxTorque = 0.0f;
        float currentPower = 0.0f;
        float maxPower = 0.0f;

        //update data when gps signal refreshes
        @Override
        void onUpdateLocation() {
            if(refreshGraph) {
                seriesSpeed.appendData(new DataPoint(tickCounter, currentSpeed), true, 60);
                seriesAccel.appendData(new DataPoint(tickCounter, currentAccelG), true, 40);

                if(currentAccelG >= 0) {
                    currentTorque = calculateTorque(currentAccelG);
                    currentPower = calculateHP(currentTorque, currentSpeed);

                    seriesTorque.appendData(new DataPoint(tickCounter, currentTorque), true, 40);
                    seriesPower.appendData(new DataPoint(tickCounter, currentPower), true, 40);
                }
                else {
                    seriesTorque.appendData(new DataPoint(tickCounter, 0.0f), true, 40);
                    seriesPower.appendData(new DataPoint(tickCounter, 0.0f), true, 40);
                }
                tickCounter += 1d;

                txtSpeed.setText("MPH: " + String.valueOf(round2(currentSpeed, 2)));
                txtMaxSpeed.setText("Max:  " + String.valueOf(round2(maxSpeed, 2)));
                txtAccel.setText("G:   " + String.valueOf(round2(currentAccelG, 2)));
                txtMaxAccel.setText("Max: " + String.valueOf(round2((maxAccelG), 2)));

                if (currentTorque > maxTorque) maxTorque = currentTorque;
                if (currentPower > maxPower) maxPower = currentPower;
                txtTorque.setText("Torque: " + String.valueOf(round2(currentTorque, 2)));
                txtMaxTorque.setText("Max:  " + String.valueOf(round2(maxTorque, 2)));
                txtPower.setText("Power:   " + String.valueOf(round2(currentPower, 2)));
                txtMaxPower.setText("Max: " + String.valueOf(round2((maxPower), 2)));

                if(record){
                    //put stuff into dyno run file
                    dynoRunData.addToLists(getCurrTime(), currentSpeed, currentAccelG, currentTorque, currentPower);
                }
            }
        }
    }

    //returns RPM based on speed and local vehicle data
    float calculateRPM(float speed){
        float engineRPM = (vehicleData.getGearFinal()*vehicleData.getGear2()*speed)/(0.00595f*vehicleData.getGearFinal());
        return engineRPM;
    }

    //return ft*lbs of torque based on G's and local vehicle data
    float calculateTorque(float AccelG){
        float wheelTorque = AccelG*vehicleData.getWeight();
        float engineTorque = (wheelTorque*vehicleData.getTireRadius())/(vehicleData.getGearFinal()*vehicleData.getGear2()*12);
        return engineTorque;
    }

    //return Horsepower based on given torque and speed and local vehicle data
    float calculateHP(float torque, float speed){
        float engineHP = calculateRPM(speed)*torque/5252;
        return engineHP;
    }

    //edit currently selected vehicle from within the dyno recording activity
    public void recordingEditVehicle(View view) {
        if (vehicleData != null && !vehicleData.getName().equals(null) && vehicleData.getName().length() > 0) {
            Toast.makeText(getApplicationContext(), "Editing " + vehicleData.getName(), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), EditVehicleActivity.class);
            intent.putExtra("vehicle_data_extra", vehicleData);
            view.getContext().startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "No vehicle to edit!", Toast.LENGTH_LONG).show();
        }
    }

    //change the currently selected vehicle from within the dyno recording activity
    public void recordingChangeVehicle(View view) {
        Intent vehicles = new Intent(this, VehiclesActivity.class   );
        startActivity(vehicles);
    }

    //listens for selected gear input to change
    private TextWatcher gearTextWatcher() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }
    public void clearMax(View view) {
        gps.clearMax(view);
    }

    //onClick function for starting / saving dyno runs
    public void onClickRecord(View view) {
        //start the dyno run
        if(record == false){
            record = true;
            dynoRunData = new DynoRunData(gps.getCurrTime());
            dynoRunData.setVehicleData(vehicleData);
            dynoRunData.setSelectedGear(Integer.parseInt(editGear.getText().toString()));
            dynoRunData.setPassengerWeight(Integer.parseInt(editWeight.getText().toString()));

            btnToggle.setText("Stop and Save");
        }
        //stop the dyno run
        else{
            record = false;
            dynoRunData.writeToFile(this);
            btnToggle.setText("Start Run");
        }
    }

    public void Test(View view) {
        Globals g = (Globals)getApplication();
        VehicleData vehicleData = g.getCurrentVehicleData();
        Toast.makeText(getBaseContext(),"Selected " + vehicleData.getName() + " for dyno", Toast.LENGTH_LONG).show();

        txtCurrentVehicle.setText("Vehicle: " + vehicleData.getName());
    }
}
