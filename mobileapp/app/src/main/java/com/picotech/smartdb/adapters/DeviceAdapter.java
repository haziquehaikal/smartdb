package com.picotech.smartdb.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.picotech.smartdb.R;
import com.picotech.smartdb.models.Device;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private List<Device> deviceList;
    private LayoutInflater inflater;
    private Activity activity;


    public DeviceAdapter(Activity activity, List<Device> _devicelist) {
        this.activity = activity;
        this.deviceList = _devicelist;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //get data from model

    public String getDeviceName(int i) {
        return deviceList.get(i).dName;
    }

    public String getDeviceId(int i ){
        return deviceList.get(i).dId;
    }

//    public String getDeviceIp(int i) {
//        return deviceList.get(i).dIp;
//    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_row_devicelist, null);
        }

        TextView devicename = convertView.findViewById(R.id.cartitle);
        TextView deviceip = convertView.findViewById(R.id.carip);

        devicename.setText(getDeviceName(position));
        //deviceip.setText(getDeviceIp(position));


        return convertView;
    }
}
