package com.example.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mainapp.api.UserApiResponse;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.mainapp.databinding.ActivityLoginBinding;

import java.io.Serializable;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.Credentials;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = getSharedPreferences("RA_MAINAPP", Activity.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        if (username != "") {
            Call<UserApiResponse> call = Controller.getUser(username, username, password);
            call.enqueue(new Callback<UserApiResponse>() {
                @Override
                public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                    if (response.code() == 200) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent = IntentSerialiser.addUserToIntent(intent, response.body());
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<UserApiResponse> call, Throwable t) {
                    // TODO: Alert needed
                }
            });
        }
    }

    public void OnLoginClicked(View view) {
        EditText username = (EditText) findViewById(R.id.usernameText);
        EditText password = (EditText) findViewById(R.id.passwordText);

        Call<UserApiResponse> call = Controller.getUser(username.getText().toString(), username.getText().toString(), password.getText().toString());
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                if (response.code() == 200) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent = IntentSerialiser.addUserToIntent(intent, response.body());
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "Unable to login", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                Snackbar.make(LoginActivity.this, view, "Failed to reach server", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void OnRegisterClicked(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}