package com.example.mainapp;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.mainapp.api.UserApiResponse;
import com.example.mainapp.api.addUser;
import com.example.mainapp.api.userService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private static final userService service = retrofit.create(userService.class);

    public static Call<UserApiResponse> AddUser(String name, String email, String hashed_password) {
        return service.addUser(new addUser(name, email, hashed_password));
    }

    public static Call<UserApiResponse> getUser(String email, String username, String password) {
        return service.getUser(Credentials.basic(username, password), email);
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
