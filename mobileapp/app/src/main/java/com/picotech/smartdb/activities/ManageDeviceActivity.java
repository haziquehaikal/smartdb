package com.picotech.smartdb.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.picotech.smartdb.R;
import com.picotech.smartdb.adapters.FuseAdapter;
import com.picotech.smartdb.app.Endpoints;
import com.picotech.smartdb.app.Env;
import com.picotech.smartdb.models.Fuse;
import com.picotech.smartdb.utils.NetworkPreferences;
import com.picotech.smartdb.utils.SessionPreferences;
import com.picotech.smartdb.utils.UIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageDeviceActivity extends AppCompatActivity {

    private UIManager uiManager;
    private ListView listView;

    private Button pushstate;
    private List<Fuse> motorlist;
    private FuseAdapter fuseAdapter;
    private SessionPreferences sessionPreferences;
    public NetworkPreferences networkPreferences;
    public String device_id;
    private Fuse fuse;
    private Endpoints endpoints;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_managedevice);
        uiManager = new UIManager(getLayoutInflater(), getSupportActionBar( ));
        uiManager.setCenterTitle("Manage Fuse");

        //
        listView = findViewById(R.id.fuse_list);
        pushstate = findViewById(R.id.pushstate);
        motorlist = new ArrayList<>();
        fuseAdapter = new FuseAdapter(this, motorlist);
        listView.setAdapter(fuseAdapter);

        endpoints = new Endpoints(Env.SYSTEM_ENVIRONMENT, getApplicationContext());
        networkPreferences = new NetworkPreferences();

        //bundle
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            device_id = bundle.getString("device_id");
            // createMessage(device_id).show();
            getFuseStat getFuseStat = new getFuseStat(device_id);
            getFuseStat.execute();
        } else {
            createMessage("Bundle is null").show();
        }

        pushstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new updateFuseStat().execute();
            }
        });
    }


    private Snackbar createMessage(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        return snackbar;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    public class updateFuseStat extends AsyncTask<Void, Void, Boolean> {

        private JSONObject jsonObject;
        private JSONArray statval;

        @Override
        protected void onPreExecute() {


            jsonObject = new JSONObject();
            statval = new JSONArray();
            for (int i = 0; i < fuseAdapter.getCount(); i++) {
                statval.put(fuseAdapter.getDeviceState(i));
            }
            try {
                jsonObject.put("error", false);
                jsonObject.put("device_id", device_id);
                jsonObject.put("fuse_stat", statval);
            } catch (JSONException e) {
                String m = e.getMessage();
                createMessage(m).show();
                Log.e("ERR_JSON_MGMDVC_PRE", m);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (networkPreferences.putData(jsonObject, endpoints.updateFuseState())) {
                return true;
            } else {
                createMessage(networkPreferences.getErrorMessage()).show();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean stat) {
            if (stat) {
                try {
                    String res = networkPreferences.getHttpResponse();
                    if (res != null) {
                        JSONObject jsonObject = new JSONObject(res);
                        if (!jsonObject.getBoolean("error")) {
                            createMessage(jsonObject.getString("message")).show();
                        } else {
                            createMessage(jsonObject.getString("message")).show();
                        }
                    }
                } catch (IOException e) {
                    String m = e.getMessage();
                    createMessage(m).show();
                    Log.e("ERR_IO_MGMDVC_POST", m);
                } catch (JSONException e) {
                    String m = e.getMessage();
                    createMessage(m).show();
                    Log.e("ERR_JSON_MGMDVC_POST", m);
                }

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getFuseStat extends AsyncTask<Void, Void, Boolean> {


        private String device_id;
        private JSONObject jsonObject;

        private getFuseStat(String id) {
            this.device_id = id;
        }

        @Override
        protected void onPreExecute() {

            jsonObject = new JSONObject();
            try {
                jsonObject.put("error", false);
                jsonObject.put("deviceid", device_id);
            } catch (JSONException e) {
                String m = e.getMessage();
                createMessage(m).show();
                Log.e("ERR_JSON_MGMDVC_GET", m);
            }
        }

        @Override
        protected Boolean doInBackground(Void... id) {
            if (networkPreferences.postData(jsonObject, endpoints.getDbStatus())) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean stat) {
            if (stat) {
                try {
                    String msg = networkPreferences.getHttpResponse();
                    if (msg != null) {
                        JSONObject jsonObject = new JSONObject(msg);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray fstat = jsonObject.getJSONArray("fuse_stat");
                            for (int i = 0; i < fstat.length(); i++) {
                                //String m = fstat.getString(i);
                                fuse = new Fuse("fuse " + i, fstat.getString(i));
                                motorlist.add(fuse);
                            }
                            fuseAdapter.notifyDataSetChanged();
                            createMessage("Data successfully retrieved").show();
                        } else {
                            createMessage(jsonObject.getString("message")).show();
                        }
                    }
                } catch (IOException e) {
                    String m = e.getMessage();
                    createMessage(m).show();
                    Log.e("ERR_NET_RES", m);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                createMessage(networkPreferences.getErrorMessage()).show();
            }

        }
    }
}
