package com.example.mainapp.ui.devices;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mainapp.Controller;
import com.example.mainapp.DeviceAdapter;
import com.example.mainapp.DeviceInfo;
import com.example.mainapp.MainActivity;
import com.example.mainapp.R;
import com.example.mainapp.api.DeviceResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Devices#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Devices extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView layout;
    private DeviceAdapter adapter;

    MqttAndroidClient mqttAndroidClient;
    final String serverUri = "tcp://broker.hivemq.com:1883";
    final String clientId = "RA_CLIENT_" + Integer.toString(MainActivity.user.getId());

    FloatingActionButton menuActionButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Devices() {
        // Required empty public constructor
        super(R.layout.devices_fragment);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DevicesBlank.
     */
    // TODO: Rename and change types and number of parameters
    public static Devices newInstance(String param1, String param2) {
        Devices fragment = new Devices();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Call<List<DeviceResponse>> call = Controller.getDevices(MainActivity.house_id, MainActivity.user
                .getEmail(), MainActivity.user.getHashed_password());
        Log.d("Devices", "Created view");
        View view = inflater.inflate(R.layout.devices_fragment, container, false);
        layout = (RecyclerView) view.findViewById(R.id.mainDevicesLayout);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(container.getContext());
        layout.setLayoutManager(manager);

        menuActionButton = (FloatingActionButton) view.findViewById(R.id.devicesActionButton);
        menuActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        adapter = new DeviceAdapter(new ArrayList<DeviceInfo>());
        layout.setAdapter(adapter);
        ArrayList<String> topics = new ArrayList<String>();
        call.enqueue(new Callback<List<DeviceResponse>>() {
            @Override
            public void onResponse(Call<List<DeviceResponse>> call, Response<List<DeviceResponse>> response) {
                if (response.code() == 200) {
                    Log.d("DevicesBlank", String.valueOf(response.body().size()));
                    if (response.body().size() > 0) {
                        for (DeviceResponse device : response.body()) {
                            Log.d("DevicesBlank", device.getName());
                            AddDevice(new DeviceInfo(device.getName(),
                                    device.getSerial_number(),
                                    100,
                                    device.getType(),
                                    device.getId(),
                                    device.getHouse_id()));
                            topics.add(Integer.toString(device.getSerial_number()));
                        }
                    }else{
                        //there are no devices in this house; tell this to the user
                        ShowNoDevices();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DeviceResponse>> call, Throwable t) {

            }
        });

        mqttAndroidClient = new MqttAndroidClient(getContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String s) {
                if (reconnect) {
                    Log.d("Devices", "Reconnect true");
                    for (String sn : topics) {
                        subscribeToTopic("status/" + sn);
                    }
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.d("Devices", "Received " + new String(mqttMessage.getPayload()));
                String[] parts = topic.split("/");
                int serial = Integer.parseInt(parts[parts.length-1]);
                int status = Integer.parseInt(new String(mqttMessage.getPayload()));
                UpdateStatus(serial, status);
                Log.d("Status", "status of window changed: "+ status);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setAutomaticReconnect(true);
        try {
            mqttAndroidClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(true);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    for (String sn : topics) {
                        subscribeToTopic("status/" + sn);
                    }
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.d("Devices", "Failure connecting to server");
                }
            });
        }catch (MqttException ex) {
            ex.printStackTrace();
            Log.d(this.getClass().getCanonicalName(), ex.getMessage());
        }


        return view;
    }

    public void subscribeToTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("Devices", "Subscribed to " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("Devices", "Failed to subscribe to " + topic);
                }
            });
        } catch (MqttException ex) {
            System.err.println("Exception while subscribing");
            ex.printStackTrace();
        }
    }

    private void AddDevice(DeviceInfo device) {
        adapter.add(device);
    }

    private void ShowNoDevices(){
        adapter.noDevices(getView());
    }

    private void UpdateStatus(int serial, int status) {
        adapter.setDeviceStatus(serial, status);
    }
}