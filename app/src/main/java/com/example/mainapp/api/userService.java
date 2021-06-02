package com.example.mainapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface userService {

    // the interface for the api

    @GET("/get_user/{email}")
    public Call<UserApiResponse> getUser(@Path("email") String email);

    @POST("/add_user/{}")
    public Call<UserApiResponse> addUser(@Body addUser parameters);

}
