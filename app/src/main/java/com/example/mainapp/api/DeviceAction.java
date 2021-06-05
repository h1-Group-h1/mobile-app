package com.example.mainapp.api;

public class DeviceAction {
    public DeviceAction(int serial_number, int val) {
        this.serial_number = serial_number;
        this.value = val;
    }

    public int getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int serial_number;
    private int value;
}
