package com.example.mainapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapp.api.DeviceAction;
import com.example.mainapp.api.StatusResponse;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private ArrayList<DeviceInfo> devices;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView statusText;
        public Button operate;
        private int status;

        public void setSerial_number(int serial_number) {
            this.serial_number = serial_number;
        }

        private int serial_number;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name_text);
            statusText = (TextView) v.findViewById(R.id.status_text);
            operate = (Button) v.findViewById(R.id.operate_button);

            operate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int val;
                    if (status == 100)
                        val = 0;
                    else
                        val = 100;
                    Call<StatusResponse> call = Controller.operateDevice(new DeviceAction(serial_number, val),
                            MainActivity.user.getEmail(),
                            MainActivity.user.getHashed_password());
                    call.enqueue(new Callback<StatusResponse>() {
                        @Override
                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                            if (response.code() == 200)
                                Snackbar.make(v, "Sent request to device", Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<StatusResponse> call, Throwable t) {

                        }
                    });
                }
            });
        }
        public void setStatus(int new_status) {
            this.status = new_status;
            if (status == 100) {
                statusText.setText("Open");
                operate.setText("Close");
            }else {
                statusText.setText("Closed");
                operate.setText("Open");
            }
        }
    }

    public DeviceAdapter(ArrayList<DeviceInfo> devices) {
        this.devices = devices;
    }

    @NotNull
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

    public void setDeviceStatus(int serial_number, int newState) {
        int ind = 0;
        for (DeviceInfo device : devices) {
            if (device.getSerial_number() == serial_number) {
                device.setStatus(newState);
                this.notifyItemChanged(ind);
                break;
            }
            ind++;
        }
    }

    public void changeDeviceState(int position, int newState) {
        devices.get(position).setStatus(newState);
        this.notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceInfo device = devices.get(position);

        holder.name.setText(device.getName());
        holder.setStatus(device.getStatus());
        holder.setSerial_number(device.getSerial_number());
        if (device.getStatus() == 100) {
            holder.statusText.setText("Open");
            holder.operate.setText("Close");
        } else {
            holder.statusText.setText("Closed");
            holder.operate.setText("Open");
        }
    }

    @Override
    public int getItemCount() { return devices.size(); }

}
