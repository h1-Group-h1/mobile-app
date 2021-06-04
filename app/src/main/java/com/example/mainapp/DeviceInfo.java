package com.example.mainapp;

public class DeviceInfo {
    String name;
    int serial_number;
    int status;
    private String type;
    private int id;
    private int house_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHouse_id() {
        return house_id;
    }

    public void setHouse_id(int house_id) {
        this.house_id = house_id;
    }

    public DeviceInfo(String name, int serial_number, int status, String type, int id, int house_id) {
        this.name = name;
        this.serial_number = serial_number;
        this.status = status;
        this.type = type;
        this.id = id;
        this.house_id = house_id;
    }
}
