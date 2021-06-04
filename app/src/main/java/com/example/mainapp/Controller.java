package com.example.mainapp;

import android.util.Log;

import com.example.mainapp.api.DeviceAction;
import com.example.mainapp.api.DeviceAdd;
import com.example.mainapp.api.DeviceResponse;
import com.example.mainapp.api.DeviceService;
import com.example.mainapp.api.StatusResponse;
import com.example.mainapp.api.UserApiResponse;
import com.example.mainapp.api.addUser;
import com.example.mainapp.api.UserService;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.Credentials;

public class Controller {

    private static final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://xgvhln.deta.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClientBuilder.build())
            .build();
    private static final UserService userService = retrofit.create(UserService.class);
    private static final DeviceService deviceService = retrofit.create(DeviceService.class);

    public static Call<UserApiResponse> AddUser(String name, String email, String hashed_password) {
        return userService.addUser(new addUser(name, email, hashed_password));
    }

    public static Call<UserApiResponse> getUser(String email, String username, String password) {
        return userService.getUser(Credentials.basic(username, password), email);
    }

    public static Call<DeviceResponse> addDevice(int house_id, DeviceAdd device, String username, String password) {
        return deviceService.addDevice(Credentials.basic(username, password), house_id, device);
    }

    public static Call<StatusResponse> delDevice(int device_id, String username, String password) {
        return deviceService.delDevice(Credentials.basic(username, password), device_id);
    }

    public static Call<StatusResponse> operateDevice(DeviceAction action, String username, String password) {
        return deviceService.operateDevice(Credentials.basic(username, password), action);
    }

    public static Call<List<DeviceResponse>> getDevices(int house_id, String username, String password) {
        return deviceService.getDevices(Credentials.basic(username, password), house_id);
    }

    /*public static void doApi(){

        String creds = "sam@sam.com:epic";
        String basic = "Authorization: Basic" + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
        // Calling '/api/users/2'
        Call<UserApiResponse> getUserCall = service.getUser(Credentials.basic("sam@sam.com", "epic"), "sam@sam.com");
        try {

            getUserCall.enqueue(new Callback<UserApiResponse>() {
                @Override
                public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                    Print("Error code: " + Integer.toString(response.code()));
                    Print("Received user: " + response.body());
                }

                @Override
                public void onFailure(Call<UserApiResponse> call, Throwable t) {
                    Print("Failed to get data");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("SAM", "there was an error in the process lol" + ex);
        }

    }*/

    public static void Print(String data) {
        Log.d(Controller.class.getCanonicalName(), data);
    }

}
