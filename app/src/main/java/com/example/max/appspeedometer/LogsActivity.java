package com.example.max.appspeedometer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class LogsActivity extends AppCompatActivity {

    ArrayList<DynoRunData> logs;
    ListView listView;
    VehicleListAdapter vehicleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        //toolbar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dyno Logs");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v){
                onBackPressed();
            }
        });

        //list stuff
        ArrayList<String> logNames = getSavedLogNames();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, logNames);
        listView = (ListView) findViewById(R.id.logs_list_view);
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),"Position :"+position+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
            }
        });


    }

    private ArrayList<String> getSavedLogNames(){
        ArrayList<String> logsList = new ArrayList<>();
        File folder = new File(getApplicationContext().getExternalFilesDir(null),"DynoRuns");
        File[] listOfFiles = folder.listFiles();

        for (int i = listOfFiles.length-1; i >= 0; i--){
            if (listOfFiles[i].isFile()) {
                logsList.add(listOfFiles[i].getName().replaceAll(".txt",""));
            }
        }

        return logsList;
    }

}
