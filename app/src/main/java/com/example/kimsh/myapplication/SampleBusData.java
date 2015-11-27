package com.example.kimsh.myapplication;

import java.util.ArrayList;

/**
 * Created by KIMSH on 2015-08-22.
 */
public class SampleBusData
{
    ArrayList<BusData> buses;

    public SampleBusData() {
        buses = new ArrayList<>();

        BusData temp = new BusData("3627", "Екатеринбург", 1, 1);
        buses.add(temp);


    }

    public ArrayList<BusData> getBuses() {
        return buses;
    }

    public void setBuses(ArrayList<BusData> buses) {
        this.buses = buses;
    }
}
