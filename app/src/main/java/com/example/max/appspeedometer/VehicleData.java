package com.example.max.appspeedometer;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Max on 5/23/2018.
 */

public class VehicleData implements Parcelable {
    //default values
    private String name = "default";
    private String desc = "";
    private float gear1 = 3.1f;
    private float gear2 = 1.9f;
    private float gear3 = 1.3f;
    private float gear4 = 1.0f;
    private float gearFinal = 4.3f;
    private float tireRadius = 11.5f;
    private float weight = 2700;
    private float drag = 0.3f;
    private int currentGear = 2;

    //default constructor
    VehicleData(){
    }

    //create vehicle based on file path
    VehicleData(File file){
        try {
            Scanner sc = new Scanner(file);
            name = sc.nextLine();
            desc = sc.nextLine();
            gear1 = Float.parseFloat(sc.nextLine());
            gear2 = Float.parseFloat(sc.nextLine());
            gear3 = Float.parseFloat(sc.nextLine());
            gear4 = Float.parseFloat(sc.nextLine());
            gearFinal = Float.parseFloat(sc.nextLine());
            tireRadius = Float.parseFloat(sc.nextLine());
            weight = Float.parseFloat(sc.nextLine());
            drag = Float.parseFloat(sc.nextLine());
            currentGear = Integer.parseInt(sc.nextLine());
        }
        catch(Exception e){
            System.out.println("Can't read vehicle file" + e.getMessage());
        }
    }

    //vehicle data for passing through activities
    VehicleData(Parcel in){
        name = in.readString();
        desc = in.readString();
        gear1 = in.readFloat();
        gear2 = in.readFloat();
        gear3 = in.readFloat();
        gear4 = in.readFloat();
        gearFinal = in.readFloat();
        tireRadius = in.readFloat();
        weight = in.readFloat();
        drag = in.readFloat();
        currentGear = in.readInt();
    }

    //for passing through activities
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeFloat(gear1);
        dest.writeFloat(gear2);
        dest.writeFloat(gear3);
        dest.writeFloat(gear4);
        dest.writeFloat(gearFinal);
        dest.writeFloat(tireRadius);
        dest.writeFloat(weight);
        dest.writeFloat(drag);
        dest.writeInt(currentGear);
    }

    //for passing through activities
    @Override
    public int describeContents() {
        return 0;
    }

    //for passing through activities
    public static final Parcelable.Creator<VehicleData> CREATOR = new Parcelable.Creator<VehicleData>(){
        public VehicleData createFromParcel(Parcel in){
            return new VehicleData(in);
        }

        public VehicleData[] newArray(int size) {
            return new VehicleData[size];
        }
    };

    //Return boolean if vehicle file name already exists
    public boolean exists(Context context){
        String fileName = name.replaceAll(" ", "_");
        File testFile = new File(context.getExternalFilesDir(null), "Vehicles"+File.separator+"vehicle_"+fileName+".txt");
        if (testFile.exists())
            return true;
        else
            return false;
    }

    //write vehicle data to file
    public void writeToFile(Context context) {
        try {
            //create Vehicles folder if doesn't exist
            File folder = new File(context.getExternalFilesDir(null), "Vehicles");
            if(!folder.exists()) {
                folder.mkdir();
            }

            // Creates a file in the public internal storage
            String fileName = name.replaceAll(" ", "_");
            File file = new File(context.getExternalFilesDir(null), "Vehicles"+File.separator+"vehicle_"+fileName+".txt");
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }

            // Adds a line to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
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
            MediaScannerConnection.scanFile(context, new String[]{file.toString()},null,null);

            Toast.makeText(context, name+" file saved!" , Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file." + e.getMessage());
        }
    }

    //delete the vehicle
    public void deleteFile(Context context){
        String fileName = "default.txt";
        try {
            //create Vehicles folder if doesn't exist
            File folder = new File(context.getExternalFilesDir(null), "Vehicles");
            if(!folder.exists()) {
                folder.mkdir();
            }

            // Creates a file in the public internal storage
            fileName = name.replaceAll(" ", "_");
            File file = new File(context.getExternalFilesDir(null), "Vehicles"+File.separator+"vehicle_"+fileName+".txt");
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            Log.e("ReadWriteFile", "Unable to delete " + fileName + "; " + e.getMessage());
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

    public int getCurrentGear() {
        return currentGear;
    }

    public void setCurrentGear(int currentGear) {
        this.currentGear = currentGear;
    }
}
