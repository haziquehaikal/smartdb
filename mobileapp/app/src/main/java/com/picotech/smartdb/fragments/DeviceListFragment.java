package com.picotech.smartdb.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.picotech.smartdb.R;
import com.picotech.smartdb.activities.ManageDeviceActivity;
import com.picotech.smartdb.adapters.DeviceAdapter;
import com.picotech.smartdb.app.Endpoints;
import com.picotech.smartdb.app.Env;
import com.picotech.smartdb.models.Device;
import com.picotech.smartdb.utils.NetworkPreferences;
import com.picotech.smartdb.utils.SessionPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeviceListFragment extends Fragment {
    private View rootView;
    private DeviceAdapter deviceAdapter;
    private List<Device> deviceList;
    private ListView listView;
    private Device device;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Endpoints endpoints;
    private SessionPreferences sessionPreferences;
    private NetworkPreferences networkPreferences;

    public static DeviceListFragment newInstance() {

        DeviceListFragment fragment = new DeviceListFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.view_fragment_devicelist, container, false);
        listView = rootView.findViewById(R.id.device_list);
        endpoints = new Endpoints(Env.SYSTEM_ENVIRONMENT, getActivity());
        networkPreferences = new NetworkPreferences();

        deviceList = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(getActivity(), deviceList);
        listView.setAdapter(deviceAdapter);
        callDeviceData();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //deviceAdapter.getDeviceId(position);
                Bundle bundle = new Bundle();
                bundle.putString("device_id", deviceAdapter.getDeviceName(position));
                Intent intent = new Intent(getActivity(), ManageDeviceActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //swipe
        swipeRefreshLayout = rootView.findViewById(R.id.swipedevicelist);
        swipeRefreshLayout.setRefreshing(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                deviceList.clear();
                callDeviceData();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        return rootView;
    }

    private void callDeviceData() {

        sessionPreferences = new SessionPreferences(getActivity());
        String userid = sessionPreferences.getUserDetails().get("user_id");
        getDeviceDetails getDeviceDetails = new getDeviceDetails(userid);
//        getDeviceDetails getDeviceDetails = new getDeviceDetails("590256125");
        getDeviceDetails.execute();

    }

    public Snackbar createMessage(String msg) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        return snackbar;
    }

    public class getDeviceDetails extends AsyncTask<Void, Void, Boolean> {

        public String id;

        private getDeviceDetails(String id) {

            this.id = id;
        }

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected Boolean doInBackground(Void... voids) {
            if (networkPreferences.getData(endpoints.getDeviceList(id))) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean stat) {
            if (stat) {
                try {
//                    Toast.makeText(getActivity(),
//                            networkPreferences.getHttpResponse(),Toast.LENGTH_LONG).show();
                    String msg = networkPreferences.getHttpResponse();
                    if (msg != null) {
                        JSONObject jsonObject = new JSONObject(msg);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray dlist = jsonObject.getJSONArray("device_list");
                            for (int i = 0; i < dlist.length(); i++) {
                                //JSONObject js3 = dlist.getJSONObject(i);
//                                Toast.makeText(getActivity(),
//                                        dlist.getJSONObject(i),Toast.LENGTH_LONG).show();
                                // createMessage((String) dlist.get(i)).show();
                                device = new Device((String) dlist.get(i), "192.168.0.1");
                                deviceList.add(device);

                            }
                            deviceAdapter.notifyDataSetChanged();
                            //createMessage("Device data update").show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Value is null", Toast.LENGTH_LONG).show();
                        //createMessage("Value is null").show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                // createMessage("Error: " + networkPreferences.getErrorMessage());
                Toast.makeText(getActivity(), "Error: " + networkPreferences.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        // onRefresh();

    }
}
