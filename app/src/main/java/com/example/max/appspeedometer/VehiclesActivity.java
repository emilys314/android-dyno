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
import java.util.List;

public class VehiclesActivity extends AppCompatActivity {

    ArrayList<VehicleData> vehicles;
    ListView listView;
    VehicleListAdapter vehicleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Vehicles");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                onBackPressed();
            }
        });

        vehicles = getSavedVehicles();

        //list view stuff
        vehicleListAdapter = new VehicleListAdapter(this, vehicles);
        listView = (ListView) findViewById(R.id.vehicles_list_view);
        listView.setAdapter(vehicleListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Globals g = (Globals)getApplication();
                g.setCurrentVehicleData(vehicles.get(position));
                Toast.makeText(getBaseContext(),"Selected " + vehicles.get(position).getName() + " for dyno", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        vehicles.clear();
        vehicles.addAll(getSavedVehicles());    //has to refresh this way to detect change
        vehicleListAdapter.notifyDataSetChanged();

        Toast.makeText(getApplicationContext(),"onResume", Toast.LENGTH_LONG).show();
    }

    private ArrayList<VehicleData> getSavedVehicles(){
        ArrayList<VehicleData> vehicleList = new ArrayList<>();
        File folder = new File(getApplicationContext().getExternalFilesDir(null),"Vehicles");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++){
            if (listOfFiles[i].isFile()) {
                vehicleList.add(new VehicleData(listOfFiles[i]));
            }
        }

        return vehicleList;
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
            //intent.putExtra("CREATION_TYPE", "new");
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
