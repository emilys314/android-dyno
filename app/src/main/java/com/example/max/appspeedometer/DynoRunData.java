package com.example.max.appspeedometer;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Max on 12/27/2018.
 */

public class DynoRunData {

    long startTime; //used to make times a reasonable number

    //parameters to save
    private VehicleData vehicleData;
    private int selectedGear;
    private int passengerWeight;
    private ArrayList<Long> time;
    private ArrayList<Float> Speed;
    private ArrayList<Float> Accel;
    private ArrayList<Float> Torque;
    private ArrayList<Float> Power;

    public DynoRunData(long startTime){
        vehicleData = new VehicleData();
        selectedGear = 0;
        passengerWeight = 0;
        this.startTime = startTime;
        time = new ArrayList<>();
        Speed = new ArrayList<>();
        Accel = new ArrayList<>();
        Torque = new ArrayList<>();
        Power = new ArrayList<>();
    }

    public DynoRunData(File file){
        vehicleData = new VehicleData();
        try {
            Scanner sc = new Scanner(file);
            vehicleData.setName(sc.nextLine());
            vehicleData.setDesc(sc.nextLine());
            vehicleData.setGear1(Float.parseFloat(sc.nextLine()));
            vehicleData.setGear2(Float.parseFloat(sc.nextLine()));
            vehicleData.setGear3(Float.parseFloat(sc.nextLine()));
            vehicleData.setGear4(Float.parseFloat(sc.nextLine()));
            vehicleData.setGearFinal(Float.parseFloat(sc.nextLine()));
            vehicleData.setTireRadius(Float.parseFloat(sc.nextLine()));
            vehicleData.setWeight(Float.parseFloat(sc.nextLine()));
            vehicleData.setDrag(Float.parseFloat(sc.nextLine()));
            selectedGear = Integer.parseInt(sc.nextLine());
            passengerWeight = Integer.parseInt(sc.nextLine());
            String line = sc.nextLine();
            time = stringToListLong(line);
            line = sc.nextLine();
            Speed = stringToListFloat(line);
            line = sc.nextLine();
            Accel = stringToListFloat(line);
            line = sc.nextLine();
            Torque = stringToListFloat(line);
            line = sc.nextLine();
            Power = stringToListFloat(line);
        }
        catch(Exception e){
            System.out.println("Can't read vehicle file" + e.getMessage());
        }
    }

    private ArrayList<Long> stringToListLong(String input){
        String[] elements = input.substring(1, input.length() - 1).split(", ");
        ArrayList<Long> result = new ArrayList<>(elements.length);
        for (String item : elements) {
            result.add(Long.valueOf(item));
        }
        return result;
    }

    private ArrayList<Float> stringToListFloat(String input) {
        String[] elements = input.substring(1, input.length() - 1).split(", ");
        ArrayList<Float> result = new ArrayList<>(elements.length);
        for (String item : elements) {
            result.add(Float.valueOf(item));
        }
        return result;
    }

    public void writeToFile(Context context) {
        try {
            //create Vehicles folder if doesn't exist
            File folder = new File(context.getExternalFilesDir(null), "DynoRuns");
            if(!folder.exists()) {
                folder.mkdir();
            }

            // Creates a file in the public internal storage
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
            String date = dateFormat.format(Calendar.getInstance().getTime());
            String fileName = date+" with "+vehicleData.getName();
            File file = new File(context.getExternalFilesDir(null), "DynoRuns"+File.separator+fileName+".txt");
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }

            // Adds a line to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(vehicleData.getName()+"\r\n");      //'r' and 'n' needed for windows text viewer
            writer.write(vehicleData.getDesc()+"\r\n");
            writer.write(String.valueOf(vehicleData.getGear1())+"\r\n");
            writer.write(String.valueOf(vehicleData.getGear2())+"\r\n");
            writer.write(String.valueOf(vehicleData.getGear3())+"\r\n");
            writer.write(String.valueOf(vehicleData.getGear4())+"\r\n");
            writer.write(String.valueOf(vehicleData.getGearFinal())+"\r\n");
            writer.write(String.valueOf(vehicleData.getTireRadius())+"\r\n");
            writer.write(String.valueOf(vehicleData.getWeight())+"\r\n");
            writer.write(String.valueOf(vehicleData.getDrag())+"\r\n");
            writer.write(selectedGear+"\r\n");
            writer.write(passengerWeight+"\r\n");
            writer.write(time.toString()+"\r\n");
            writer.write(Speed.toString()+"\r\n");
            writer.write(Accel.toString()+"\r\n");
            writer.write(Torque.toString()+"\r\n");
            writer.write(Power.toString()+"\r\n");
            writer.close();
            // Refresh data to see when the device is plugged in computer.
            MediaScannerConnection.scanFile(context, new String[]{file.toString()},null,null);

            Toast.makeText(context, "\""+fileName+".txt\" saved!" , Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write TestFile.txt file." + e.getMessage());
        }
    }

    public void addToLists(long time, float speed, float accel, float torque, float power){
        this.time.add(time-startTime);
        Speed.add(speed);
        Accel.add(accel);
        Torque.add(torque);
        Power.add(power);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public ArrayList<Long> getTime() {
        return time;
    }

    public void setTime(ArrayList<Long> time) {
        this.time = time;
    }

    public VehicleData getVehicleData() {
        return vehicleData;
    }

    public void setVehicleData(VehicleData vehicleData) {
        this.vehicleData = vehicleData;
    }

    public int getSelectedGear() {
        return selectedGear;
    }

    public void setSelectedGear(int selectedGear) {
        this.selectedGear = selectedGear;
    }

    public int getPassengerWeight() {
        return passengerWeight;
    }

    public void setPassengerWeight(int passengerWeight) {
        this.passengerWeight = passengerWeight;
    }

    public ArrayList<Float> getSpeed() {
        return Speed;
    }

    public void setSpeed(ArrayList<Float> speed) {
        Speed = speed;
    }

    public ArrayList<Float> getAccel() {
        return Accel;
    }

    public void setAccel(ArrayList<Float> accel) {
        Accel = accel;
    }

    public ArrayList<Float> getTorque() {
        return Torque;
    }

    public void setTorque(ArrayList<Float> torque) {
        Torque = torque;
    }

    public ArrayList<Float> getPower() {
        return Power;
    }

    public void setPower(ArrayList<Float> power) {
        Power = power;
    }
}
