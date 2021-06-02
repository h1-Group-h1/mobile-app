package com.example.mainapp.ui.loginlogout;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.UUID;

//NOTE: atm the login script is kinda actually a registering script so keep that in mind

public class LoginScript extends AppCompatActivity {

    MqttAndroidClient mqttAndroidClient;
    final String serverUri = "tcp://broker.emqx.io:1883";
    String clientId = "exampleClient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d("SAM","Connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("SAM", "Message from " + topic + ": " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("SAM", "Delivery of message complete");
            }

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.d("SAM", "Reconnected to "+ serverURI);
                    subscribeToTopic("hello/world");
                } else {
                    Log.d("SAM", "Connected to "+ serverURI);
                }
            }
        });
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setAutomaticReconnect(true);


        //mqttAndroidClient.connect(options);
        connectClient(options);
    }

    //RECIEVE: this is receives the data from the LoginLogoutFragment, it is calls the hash functions
    //and then it will send the data thought the api (still need to finish the api)

    public void receive(String email, String password, String name){ //this is actually for registering lol

        //hash the password:
        String hashedPassword = hashString(password);

        //create JSON - probably can remove this as it will be handled by the API
        //String msg = String.format("{\"email\":\"%s\", \"name\":\"%s\", \"hashed_password\":\"%s\"}", email, name, hashedPassword);

    }

    private static String hashString(String message){ //stole this from online lol https://www.codejava.net/coding/how-to-calculate-md5-and-sha-hash-values-in-java

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (Exception ex) {
            Log.e("SAM", "Could not generate hash from String "+ ex);
            return "error";
        }
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) { //stole this too xoxo
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    private void connectClient(MqttConnectOptions options) {
        try {
            mqttAndroidClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(true);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic("hello/world");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("mainView", "Failed to connect to server");
                    Log.d("SAM", "Failure connecting to server");
                    if (!mqttAndroidClient.isConnected()) {
                        connectClient(options);
                    }
                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
            Log.d(this.getLocalClassName(), ex.getMessage());

        }
    }

    public void subscribeToTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("SAM", "Subscribed to " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("SAM","Failed to subscribe to " + topic);
                }
            });
        } catch (MqttException ex) {
            System.err.println("Exception while subscribing");
            ex.printStackTrace();
        }
    }

    //PUBLISH: might need this at some point.
    //NOTE: it is copied from the demo so there are some parts that dont work atm as
    //we still need to adapt this function for this script.

    /*public void publishMessage(View view) {
        String topic = "hello/world";
        EditText payloadView = (EditText) findViewById(R.id.text2);
        String payload = payloadView.getText().toString();
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            mqttAndroidClient.publish(topic, message);
            Log.d("SAM", "Message published");
            if (!mqttAndroidClient.isConnected()) {
                Log.d("SAM", "Disconnected");
            }
        } catch (MqttException ex) {
            System.err.println("Exception while publishing");
            ex.printStackTrace();
        }
    }*/

}
