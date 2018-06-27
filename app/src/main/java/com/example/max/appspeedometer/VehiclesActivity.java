package com.example.max.appspeedometer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class VehiclesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Vehicles");
        setSupportActionBar(toolbar);getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                onBackPressed();
            }
        });

        //list view array stuff
        ArrayList<VehicleData> vehicles = new ArrayList<>();
        VehicleData vehicle1 = new VehicleData();
        vehicle1.setName("Miata");
        vehicles.add(vehicle1);
        VehicleData vehicle2 = new VehicleData();
        vehicle2.setName("FRS");
        vehicles.add(vehicle2);

        //list view stuff
        VehicleListAdapter vehicleListAdapter = new VehicleListAdapter(this, vehicles);
        ListView listView = (ListView) findViewById(R.id.vehicles_list_view);
        listView.setAdapter(vehicleListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"Click ListItem Number " + position, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vehicles, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            Toast.makeText(VehiclesActivity.this, "Action clicked add", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(VehiclesActivity.this, EditVehicleActivity.class);
            intent.putExtra("CREATION_TYPE", "new"); //todo should also make "edit" option
            startActivity(intent);

            return true;
        }
        if (id == R.id.menu_sort) {
            Toast.makeText(VehiclesActivity.this, "Action clicked sort", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
