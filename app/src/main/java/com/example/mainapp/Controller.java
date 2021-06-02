package com.example.mainapp;

import android.util.Log;

import com.example.mainapp.api.UserApiResponse;
import com.example.mainapp.api.addUser;
import com.example.mainapp.api.userService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {

    public static void doApi(){

        Log.d("SAM", "doApi is running");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xgvhln.deta.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        userService service = retrofit.create(userService.class);

        // Calling '/api/users/2'
        Call<UserApiResponse> callSync = service.addUser(new addUser("sam", "sam@sam.com", "epic"));

        try {
            Response<UserApiResponse> response = callSync.execute();
            UserApiResponse apiResponse = response.body();
            Log.d("SAM", apiResponse.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("SAM", "there was an error in the process lol" + ex);
        }

    }

}
