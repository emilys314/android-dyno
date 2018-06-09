package com.example.max.appspeedometer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EditVehicleActivity extends AppCompatActivity {

    private EditText name;
    private EditText desc;
    private EditText edit1st;
    private EditText edit2nd;
    private EditText edit3rd;
    private EditText edit4th;
    private EditText editFinal;
    private EditText editMass;
    private EditText editRadius;
    private EditText editDrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        String type = getIntent().getStringExtra("CREATION_TYPE"); //todo do stuff with this, ex "edit" "add" "editstrict"

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Vehicle");
        setSupportActionBar(toolbar);getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                onBackPressed();
            }
        });

        name = (EditText) findViewById(R.id.editName);
        desc = (EditText) findViewById(R.id.editDesc);
        edit1st = (EditText) findViewById(R.id.edit1st);
        edit2nd = (EditText) findViewById(R.id.edit2nd);
        edit3rd = (EditText) findViewById(R.id.edit3rd);
        edit4th = (EditText) findViewById(R.id.edit4th);
        editFinal = (EditText) findViewById(R.id.editFinal);
        editMass  = (EditText) findViewById(R.id.editMass);
        editRadius = (EditText) findViewById(R.id.editRadius);
        editDrag = (EditText) findViewById(R.id.editDrag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_vehicle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_edit_vehicles_delete) {
            Toast.makeText(EditVehicleActivity.this, "Action clicked delete", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.menu_edit_vehicles_save) {
            writeToFile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeToFile() {
        VehicleData vehicleData = new VehicleData();
        vehicleData.setName(name.getText().toString());
        vehicleData.setDesc(desc.getText().toString());
        vehicleData.setGear1(Float.parseFloat(edit1st.getText().toString()));
        vehicleData.setGear2(Float.parseFloat(edit2nd.getText().toString()));
        vehicleData.setGear3(Float.parseFloat(edit3rd.getText().toString()));
        vehicleData.setGear4(Float.parseFloat(edit4th.getText().toString()));
        vehicleData.setGearFinal(Float.parseFloat(editFinal.getText().toString()));
        vehicleData.setWeight(Float.parseFloat(editMass.getText().toString()));
        vehicleData.setTireRadius(Float.parseFloat(editRadius.getText().toString()));
        vehicleData.setDrag(Float.parseFloat(editDrag.getText().toString()));

        vehicleData.writeToFile(getApplicationContext());
    }
}
