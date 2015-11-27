package com.example.kimsh.myapplication;

/**
 * Created by KIMSH on 2015-08-22.
 */
public class BusData
{
    String busno;
    String nowStation;
    int leftStation;
    int leftMinute;

    public BusData(String busno, String nowStation, int leftStation, int leftMinute) {
        this.busno = busno;
        this.nowStation = nowStation;
        this.leftStation = leftStation;
        this.leftMinute = leftMinute;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getNowStation() {
        return nowStation;
    }

    public void setNowStation(String nowStation) {
        this.nowStation = nowStation;
    }

    public int getLeftStation() {
        return leftStation;
    }

    public void setLeftStation(int leftStation) {
        this.leftStation = leftStation;
    }

    public int getLeftMinute() {
        return leftMinute;
    }

    public void setLeftMinute(int leftMinute) {
        this.leftMinute = leftMinute;
    }
}
