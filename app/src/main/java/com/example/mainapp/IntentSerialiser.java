package com.example.mainapp;

import android.content.Intent;

import com.example.mainapp.api.UserApiResponse;

public class IntentSerialiser {

    public static Intent addUserToIntent(Intent intent, UserApiResponse user) {
        intent.putExtra("name", user.getName());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("hashed_password", user.getHashed_password());
        intent.putExtra("id", user.getId());
        return intent;
    }

    public static UserApiResponse getUserFromIntent(Intent intent) {
        UserApiResponse response = new UserApiResponse();
        response.setName(intent.getStringExtra("name"));
        response.setEmail(intent.getStringExtra("email"));
        response.setHashed_password(intent.getStringExtra("hashed_password"));
        response.setId(intent.getIntExtra("id", 0));
        return response;
    }
}
