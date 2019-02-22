package com.picotech.smartdb.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.picotech.smartdb.R;
import com.picotech.smartdb.app.Endpoints;
import com.picotech.smartdb.app.Env;
import com.picotech.smartdb.utils.NetworkPreferences;
import com.picotech.smartdb.utils.SessionPreferences;
import com.picotech.smartdb.utils.UIManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AddNewDeviceActivity extends AppCompatActivity {

    private UIManager uiManager;
    private IntentIntegrator intentIntegrator;
    private Button submitFormBtn, scanQrcodeBtn;
    private EditText deviceIdInput;
    private NetworkPreferences networkPreferences;
    private Endpoints endpoints;
    public AlertDialog alertDialog;
    private SessionPreferences sessionPreferences;
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_adddevice);
        uiManager = new UIManager(getLayoutInflater(), getSupportActionBar());
        uiManager.setCenterTitle("Add New Device");
        deviceIdInput = findViewById(R.id.qrcodeid);

        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Scan SmartDB QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setCameraId(0);  // Use a specific camera of the device
        intentIntegrator.setBeepEnabled(true);
        scanQrcodeBtn = findViewById(R.id.scanqr);
        submitFormBtn = findViewById(R.id.submitdata);


        endpoints = new Endpoints(Env.SYSTEM_ENVIRONMENT, getApplicationContext());
        networkPreferences = new NetworkPreferences();
        sessionPreferences = new SessionPreferences(getApplicationContext());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);


        submitFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("dbdevice_id", deviceIdInput.getText().toString());
                        new checkDeviceAvailable(new CheckDeviceListener() {
                            @Override
                            public void OnSuccess(Boolean stat) {
                                if (stat) {
                                    createConfirmDialog();
                                }
                            }

                            @Override
                            public void OnError(String errmsg) {
                                createMessage(errmsg).show();
                            }
                        }).execute(jsonObject);
                    } catch (JSONException e) {
                        String msg = e.getMessage();
                        Log.e("ERR_JSON_ADDDVC", msg);
                        createMessage(msg).show();
                    }
                }
            }
        });

        scanQrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentIntegrator.initiateScan();
            }
        });
    }

    public void createConfirmDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewDeviceActivity.this);
        builder.setTitle("Device confirmation");
        builder.setMessage("Are you sure to add this device ? ");

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject userdata = new JSONObject();
                try {
                    userdata.put("user_id", sessionPreferences.getUserDetails().get("user_id"));
                    userdata.put("dbdevice_id", deviceIdInput.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new addDeviceProcess(new CheckDeviceListener() {
                    @Override
                    public void OnSuccess(Boolean stat) {
                        // createNotify();
                        Toast.makeText(getApplicationContext(),
                                "Your device has been added , you may control the device now", Toast.LENGTH_LONG)
                                .show();
                        onBackPressed();
                    }

                    @Override
                    public void OnError(String errmsg) {

                    }
                }).execute(userdata);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });


        alertDialog = builder.create();
        alertDialog.show();


    }

    public void createNotify() {
    }

    public Boolean validate() {
        if (deviceIdInput.getText().toString().isEmpty()) {
            createMessage("Please enter device id").show();
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Unable to retrieve data", Toast.LENGTH_LONG).show();
            } else {
                deviceIdInput.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    public interface CheckDeviceListener {

        void OnSuccess(Boolean stat);

        void OnError(String errmsg);

    }


    public Snackbar createMessage(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        return snackbar;
    }

    private void startDp() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void stopDp() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public class checkDeviceAvailable extends AsyncTask<JSONObject, Void, Boolean> {
        CheckDeviceListener mcallback;

        checkDeviceAvailable(CheckDeviceListener callback) {
            mcallback = callback;
        }

        @Override
        protected void onPreExecute() {

            progressDialog.setTitle("Checking device confirmation");
            startDp();

        }


        @Override
        protected Boolean doInBackground(JSONObject... jsonObjects) {
            return networkPreferences.postData(jsonObjects[0], endpoints.getCheckDeviceAvai());
        }


        @Override
        protected void onPostExecute(Boolean stat) {
            stopDp();
            if (stat) {
                try {
                    // Log.d("SININININ",networkPreferences.getHttpResponse());
                    JSONObject res = new JSONObject(networkPreferences.getHttpResponse());
                    if (!res.getBoolean("error")) {
                        mcallback.OnSuccess(true);
                    } else {
                        mcallback.OnError(res.getString("message"));
                        //createMessage(res.getString("message")).show();
                    }
                } catch (JSONException e) {
                    String msg = e.getMessage();
                    Log.d("ERR_JSON_ASYNCCHKDVC", msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    String msg = e.getMessage();
                    Log.d("ERR_IO_ASYNCCHKDVC", msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public class addDeviceProcess extends AsyncTask<JSONObject, Void, Boolean> {
        CheckDeviceListener mcallback;

        addDeviceProcess(CheckDeviceListener callback) {
            mcallback = callback;
        }

        @Override
        protected void onPreExecute() {

            progressDialog.setTitle("Registering new device");
            startDp();


        }


        @Override
        protected Boolean doInBackground(JSONObject... jsonObjects) {
            return networkPreferences.postData(jsonObjects[0], endpoints.getAddDevice());
        }


        @Override
        protected void onPostExecute(Boolean stat) {
            stopDp();
            if (stat) {
                try {
                    //   Log.d("SININININ",networkPreferences.getHttpResponse());
                    JSONObject res = new JSONObject(networkPreferences.getHttpResponse());
                    if (!res.getBoolean("error")) {
                        mcallback.OnSuccess(true);
                    } else {
                        mcallback.OnError(res.getString("message"));
                        //createMessage(res.getString("message")).show();
                    }
                } catch (JSONException e) {
                    String msg = e.getMessage();
                    Log.d("ERR_JSON_ASYNCCHKDVC", msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    String msg = e.getMessage();
                    Log.d("ERR_IO_ASYNCCHKDVC", msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            }
        }
    }


}
