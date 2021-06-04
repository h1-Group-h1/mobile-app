package com.example.mainapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mainapp.Controller;
import com.example.mainapp.DeviceAdapter;
import com.example.mainapp.DeviceInfo;
import com.example.mainapp.MainActivity;
import com.example.mainapp.R;
import com.example.mainapp.api.DeviceResponse;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Devices() {
        // Required empty public constructor
        super(R.layout.fragment_devices_blank);
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
        View view = inflater.inflate(R.layout.fragment_devices_blank, container, false);
        layout = (RecyclerView) view.findViewById(R.id.mainDevicesLayout);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(container.getContext());
        layout.setLayoutManager(manager);



        adapter = new DeviceAdapter(new ArrayList<DeviceInfo>());
        layout.setAdapter(adapter);

        call.enqueue(new Callback<List<DeviceResponse>>() {
            @Override
            public void onResponse(Call<List<DeviceResponse>> call, Response<List<DeviceResponse>> response) {
                if (response.code() == 200) {
                    for (DeviceResponse device : response.body()) {
                        Log.d("DevicesBlank", device.getName());
                        AddDevice(new DeviceInfo(device.getName(),
                                                 device.getSerial_number(),
                                                 100,
                                                 device.getType(),
                                                 device.getId(),
                                                 device.getHouse_id()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DeviceResponse>> call, Throwable t) {

            }
        });
        return view;
    }

    private void AddDevice(DeviceInfo device) {
        adapter.add(device);
    }
}