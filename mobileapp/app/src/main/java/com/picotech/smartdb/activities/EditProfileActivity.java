package com.picotech.smartdb.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.picotech.smartdb.R;
import com.picotech.smartdb.app.Endpoints;
import com.picotech.smartdb.app.Env;
import com.picotech.smartdb.utils.NetworkPreferences;
import com.picotech.smartdb.utils.SessionPreferences;
import com.picotech.smartdb.utils.UIManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private UIManager uiManager;
    private EditText inputfullname, inputemail, inputpasswd, inputrepasswd;
    private Button btnupdate;
    private NetworkPreferences networkPreferences;
    private SessionPreferences sessionPreferences;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private Endpoints endpoints;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_editprofile);

        uiManager = new UIManager(getLayoutInflater(), getSupportActionBar());
        uiManager.setCenterTitle("Edit My Profile");

        //input
        inputfullname = findViewById(R.id.inputFullname);
        inputemail = findViewById(R.id.inputEmail);
        inputpasswd = findViewById(R.id.inputPasswd);
        inputrepasswd = findViewById(R.id.inputRepasswd);
//        progressBar = findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Updating your profile");
        progressDialog.setMessage("Loading..");

        //button
        btnupdate = findViewById(R.id.btnUpdate);

        //session
        sessionPreferences = new SessionPreferences(getApplicationContext());

        //network
        endpoints = new Endpoints(Env.SYSTEM_ENVIRONMENT, getApplicationContext());
        networkPreferences = new NetworkPreferences();

        Bundle bundle = getIntent().getExtras();
        //set default
        inputfullname.setText(bundle.getString("name"));
        inputemail.setText(bundle.getString("email"));


        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    //createMessage("Done").show();
                    JSONObject datastruct = new JSONObject();
                    try {
                        datastruct.put("userid", sessionPreferences.getUserDetails().get("user_id"));
                        datastruct.put("name", inputfullname.getText());
                        datastruct.put("usermail", inputemail.getText());
                        datastruct.put("password", inputrepasswd.getText());
                        new sendDataThread().execute(datastruct);
//                        Toast.makeText(getApplicationContext(), datastruct.toString(),Toast.LENGTH_LONG).show();
//                        while (test < 100) {
//                            test += 1;
//                            progressBar.setProgress(test);
//
//                        }

                    } catch (JSONException e) {
                        String msg = e.getMessage();
                        createMessage(msg).show();
                        Log.e("ERR_JSON_EDITPROF", msg);
                    }
                }
            }
        });
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

    private boolean checkForm() {

        if (inputfullname.getText().toString().isEmpty()) {
            createMessage("Please enter your name").show();
            return false;

        } else if (inputemail.getText().toString().isEmpty()) {
            createMessage("Please enter your email").show();
            return false;

        } else if (inputpasswd.getText().toString().isEmpty()) {
            createMessage("Please enter your password").show();
            return false;

        } else if (inputrepasswd.getText().toString().isEmpty()) {
            createMessage("Please retype your password").show();
            return false;

        } else if (!inputpasswd.getText().toString().equals(inputrepasswd.getText().toString())) {
            createMessage("password not match").show();
            return false;

        }
        return true;
    }

    private Snackbar createMessage(String msg) {

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg,
                Snackbar.LENGTH_LONG);

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

    public class sendDataThread extends AsyncTask<JSONObject, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            startDp();

//            createMessage(endpoints.getProfileApi()).show();
        }

        @Override
        protected Boolean doInBackground(JSONObject... data) {
//            createMessage(data[0].toString()).show();
            ///return networkPreferences.postData(JSONObject[0]);
//            if (networkPreferences.putData(data[0])) {
//                return true;
//            } else {
//                return false;


//
//            }

            return networkPreferences.putData(data[0], endpoints.updateProfileApi());
        }

        @Override
        protected void onPostExecute(Boolean stat) {
            stopDp();
            if (stat) {
                try {
                    JSONObject resdata = new JSONObject(networkPreferences.getHttpResponse());
//                    createMessage(resdata.toString()).show();
//                    createMessage(String.valueOf(networkPreferences.getHttpCode())).show();
                    if (!resdata.getBoolean("error")) {
                        createMessage(resdata.getString("message")).show();
                    }
                } catch (IOException e) {
                    String msg = e.getMessage();
                    createMessage(msg).show();
                    Log.e("ERR_IO_EDITPROF", msg);
                } catch (JSONException e) {
                    String msg = e.getMessage();
                    createMessage(msg).show();
                    Log.e("ERR_JSON_EDITPROF_ASYNC", msg);

                }
            } else {
//               / createMessage("error").show();
                //idk

            }

        }

    }
}
