package com.picotech.smartdb.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.picotech.smartdb.R;
import com.picotech.smartdb.models.Fuse;

import java.util.List;

public class FuseAdapter extends BaseAdapter {

    public LayoutInflater inflater;

    public Activity activity;
    public List<Fuse> fuse;
    private Fuse mFuse;

    public FuseAdapter(Activity _activity, List<Fuse> fuseList) {
        this.activity = _activity;
        this.fuse = fuseList;
    }

    @Override
    public int getCount() {
        return fuse.size();
    }

    @Override
    public Object getItem(int position) {
        return fuse.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getDeviceName(int position) {
        return fuse.get(position).fusename;
    }

    public String getDeviceState(int position) {
        return fuse.get(position).fusestate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_row_motor, null);
        }

        TextView dbname = convertView.findViewById(R.id.devicename);
        final SwitchCompat fusesw = convertView.findViewById(R.id.fuseswitch);

        if (getDeviceState(position).equals("1")) {
            fusesw.setChecked(true);
        } else if (getDeviceState(position).equals("0")) {
            fusesw.setChecked(false);
        }

        fusesw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mFuse = new Fuse("Fuse " + (position + 1), "1");

                } else if (!isChecked) {
                    mFuse = new Fuse("Fuse " + (position + 1), "0");
                }
                fuse.set(position, mFuse);
                notifyDataSetChanged();
              //  Toast.makeText(activity, String.valueOf(isChecked) + ", FUSE: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        dbname.setText(getDeviceName(position));

        return convertView;
    }

}
