package com.example.max.appspeedometer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Max on 6/20/2018.
 */

public class VehicleListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<VehicleData> mDataSource;
    private VehicleData vehicleData;

    public VehicleListAdapter(Context context, ArrayList<VehicleData> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.activity_vehicles_list_item, parent, false);
        TextView titleTextView = (TextView) rowView.findViewById(R.id.vehicles_list_item_title);
        ImageButton editImageButton = (ImageButton) rowView.findViewById(R.id.vehicles_list_item_button);

        //fill in data
        vehicleData = (VehicleData) getItem(position);
        titleTextView.setText(vehicleData.getName());

        editImageButton.setOnClickListener(new View.OnClickListener(){  //todo fix it so clicked item corresponds to clicked item haha
            @Override
            public void onClick(View view){
                Toast.makeText(mContext,"Editing " + vehicleData.getName(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(mContext, EditVehicleActivity.class);
                intent.putExtra("vehicle_data_extra", vehicleData);
                view.getContext().startActivity(intent);
            }
        });

        return rowView;
    }
}
