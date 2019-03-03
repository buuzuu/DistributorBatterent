package com.example.distributor_batterent.Model;

public class BatteryModel {


    public String batteryName;
    public String batteryImage;

    public BatteryModel(String batteryName, String batteryImage) {
        this.batteryName = batteryName;
        this.batteryImage = batteryImage;
    }

    public String getBatteryName() {
        return batteryName;
    }

    public void setBatteryName(String batteryName) {
        this.batteryName = batteryName;
    }

    public String getBatteryImage() {
        return batteryImage;
    }

    public void setBatteryImage(String batteryImage) {
        this.batteryImage = batteryImage;
    }
}
