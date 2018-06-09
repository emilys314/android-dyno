package com.example.max.appspeedometer;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Max on 5/23/2018.
 */

public class VehicleData {
    private String name = "";
    private String desc = "";
    private float gear1 = 3.1f;
    private float gear2 = 1.9f;
    private float gear3 = 1.3f;
    private float gear4 = 1.0f;
    private float gearFinal = 4.3f;
    private float tireRadius = 11.5f;
    private float weight = 2300;
    private float drag = 0.3f;
    private short currentGear = 2;

    VehicleData(){
    }

    public void writeToFile(Context context) {
        try {
            //create Vehicles folder if doesn't exist
            File folder = new File(context.getExternalFilesDir(null), "Vehicles");
            if(!folder.exists()) {
                folder.mkdir();
            }

            // Creates a file in the public internal storage
            String fileName = name.replaceAll(" ", "_");
            File testFile = new File(context.getExternalFilesDir(null), "Vehicles"+File.separator+"vehicle_"+fileName+".txt");
            if (!testFile.exists()) {
                testFile.createNewFile();
            } else {
                Toast.makeText(context, "Vehicle file already exists!" , Toast.LENGTH_LONG).show();
            }

            // Adds a line to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write(name+"\r\n");      //'r' and 'n' needed for windows text viewer
            writer.write(desc+"\r\n");
            writer.write(String.valueOf(gear1)+"\r\n");
            writer.write(String.valueOf(gear2)+"\r\n");
            writer.write(String.valueOf(gear3)+"\r\n");
            writer.write(String.valueOf(gear4)+"\r\n");
            writer.write(String.valueOf(gearFinal)+"\r\n");
            writer.write(String.valueOf(tireRadius)+"\r\n");
            writer.write(String.valueOf(weight)+"\r\n");
            writer.write(String.valueOf(drag)+"\r\n");
            writer.write(String.valueOf(currentGear)+"\r\n");
            writer.close();
            // Refresh data to see when the device is plugged in computer.
            MediaScannerConnection.scanFile(context, new String[]{testFile.toString()},null,null);
            Toast.makeText(context, "Vehicle file saved!" , Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file." + e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getGear1() {
        return gear1;
    }

    public void setGear1(float gear1) {
        this.gear1 = gear1;
    }

    public float getGear2() {
        return gear2;
    }

    public void setGear2(float gear2) {
        this.gear2 = gear2;
    }

    public float getGear3() {
        return gear3;
    }

    public void setGear3(float gear3) {
        this.gear3 = gear3;
    }

    public float getGear4() {
        return gear4;
    }

    public void setGear4(float gear4) {
        this.gear4 = gear4;
    }

    public float getGearFinal() {
        return gearFinal;
    }

    public void setGearFinal(float gearFinal) {
        this.gearFinal = gearFinal;
    }

    public float getTireRadius() {
        return tireRadius;
    }

    public void setTireRadius(float tireRadius) {
        this.tireRadius = tireRadius;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getDrag() {
        return drag;
    }

    public void setDrag(float drag) {
        this.drag = drag;
    }

    public short getCurrentGear() {
        return currentGear;
    }

    public void setCurrentGear(short currentGear) {
        this.currentGear = currentGear;
    }
}
