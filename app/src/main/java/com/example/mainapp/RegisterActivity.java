package com.example.mainapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.mainapp.api.UserApiResponse;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mainapp.databinding.ActivityRegisterBinding;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void OnRegisterClicked(View view) {
        String name = ((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString();
        String email = ((EditText) findViewById(R.id.editTextTextEmailAddress)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
        String hashed_password = PasswordHasher.hash_password(password);
        Log.d("Register", name + " " + email + " " + hashed_password);
        Call<UserApiResponse> call = Controller.AddUser(name, email, hashed_password);
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                if (response.code() == 200) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent = IntentSerialiser.addUserToIntent(intent, response.body());
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "Unable to add user", Snackbar.LENGTH_LONG).show();
                }
                Log.d("Register", "Result code: " + Integer.toString(response.code()));
            }

            @Override
            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                Snackbar.make(view, "Unable to add user", Snackbar.LENGTH_LONG).show();
            }
        });

    }
}