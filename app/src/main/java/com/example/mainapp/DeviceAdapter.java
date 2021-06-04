package com.example.mainapp;

import android.bluetooth.BluetoothClass;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private ArrayList<DeviceInfo> devices;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView status;
        public Button operate;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name_text);
            status = (TextView) v.findViewById(R.id.status_text);
            operate = (Button) v.findViewById(R.id.operate_button);
        }
    }

    public DeviceAdapter(ArrayList<DeviceInfo> devices) {
        this.devices = devices;
    }

    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.device_row, parent, false);
        return new ViewHolder(v);
    }

    public void add(DeviceInfo device) {
        devices.add(device);
        this.notifyItemInserted(devices.size()-1);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceInfo device = devices.get(position);

        holder.name.setText(device.getName());
        if (device.getStatus() == 100) {
            holder.status.setText("Open");
            holder.operate.setText("Close");
        } else {
            holder.status.setText("Closed");
            holder.operate.setText("Open");
        }
    }

    @Override
    public int getItemCount() { return devices.size(); }

}
