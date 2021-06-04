package com.example.mainapp.api;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DeviceService {
    @POST("/add_device/{house_id}")
    public Call<DeviceResponse> addDevice(@Header("Authorization") String creds, @Path("house_id") int house_id, @Body DeviceAdd parameters);
    @DELETE("/del_device/{device_id}")
    public Call<StatusResponse> delDevice(@Header("Authorization") String creds, @Path("device_id") int device_id);
    @POST("/operate_device/")
    public Call<StatusResponse> operateDevice(@Header("Authorization") String creds, @Body DeviceAction parameters);
    @GET("/get_devices/{house_id}")
    public Call<List<DeviceResponse>> getDevices(@Header("Authorization") String creds, @Path("house_id") int house_id);
}
