package com.example.mainapp.ui.loginlogout;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mainapp.R;

import com.example.mainapp.ui.loginlogout.LoginScript;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginLogoutFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginLogoutFragment extends Fragment {

    private EditText name;
    private EditText email;
    private EditText password;
    private Button login;
    private Button register;
    private TextView messages;

    LoginScript loginScript = new LoginScript();

    public LoginLogoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_logout, container, false);

        name = v.findViewById(R.id.enterName);
        email = v.findViewById(R.id.enterEmail);
        password = v.findViewById(R.id.enterPass);
        login = v.findViewById(R.id.buttonLogin);
        register = v.findViewById(R.id.buttonRegister);
        messages = v.findViewById(R.id.messages);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loginScript.receive(email.getText().toString(), password.getText().toString(), name.getText().toString());
            }
        });

        return v;
    }

    public void postMessageToScreen(String m){

        messages.append(m + "\n");

    }

}