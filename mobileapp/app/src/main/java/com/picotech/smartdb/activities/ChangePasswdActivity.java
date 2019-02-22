package com.picotech.smartdb.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.picotech.smartdb.R;
import com.picotech.smartdb.app.Endpoints;
import com.picotech.smartdb.app.Env;
import com.picotech.smartdb.utils.NetworkPreferences;
import com.picotech.smartdb.utils.SessionPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ChangePasswdActivity extends AppCompatActivity {

    public static String TAG = "FIRE";

    private NetworkPreferences networkPreferences;
    private EditText inputfullname, inputpasswd, inputrepasswd;
    private Button changePassword;
    private SessionPreferences sessionPreferences;
    private Endpoints endpoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_changepass);
        getSupportActionBar().setTitle("Change your passsword");
        sessionPreferences = new SessionPreferences(getApplicationContext());

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        sessionPreferences.setFirebaseKey(token);
                        // Log and toast
                        String msg = sessionPreferences.getFirebaseKey();
                        Log.d(TAG, msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });


        //declare input
        inputfullname = findViewById(R.id.inputFullname);
        inputpasswd = findViewById(R.id.inputPasswd);
        inputrepasswd = findViewById(R.id.inputRepasswd);

        //declare button
        changePassword = findViewById(R.id.btnUpdate);


        endpoints = new Endpoints(Env.SYSTEM_ENVIRONMENT, getApplicationContext());
        networkPreferences = new NetworkPreferences();

        //get user id
        final Bundle extra = getIntent().getExtras();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    JSONObject datastruct = new JSONObject();
                    try {
                        datastruct.put("name", inputfullname.getText());
                        datastruct.put("userid", extra.get("userid"));
                        datastruct.put("password", inputrepasswd.getText());
                        datastruct.put("mobile_token", sessionPreferences.getFirebaseKey());

                        new changePasswdThread().execute(datastruct);
                    } catch (JSONException e) {
                        String msg = e.getMessage();
                        createMessage(msg).show();
                        Log.e("ERR_JSON_CHANGEPASS", msg);
                    }
                }
            }
        });


    }

    private boolean checkForm() {

        if (inputfullname.getText().toString().isEmpty()) {
            createMessage("Please enter your name").show();
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


    public class changePasswdThread extends AsyncTask<JSONObject, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(JSONObject... jsonObjects) {
            return networkPreferences.postData(jsonObjects[0], endpoints.getTempPassApi());
        }

        @Override
        protected void onPostExecute(Boolean stat) {

            if (stat) {
                try {
                    JSONObject resdata = new JSONObject(networkPreferences.getHttpResponse());
                    if (!resdata.getBoolean("error")) {
                        createMessage(resdata.getString("message")).show();
                        Intent intent = new Intent(ChangePasswdActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        createMessage(resdata.getString("message")).show();
                    }

                } catch (IOException e) {
                    String msg = e.getMessage();
                    createMessage(msg).show();
                    Log.e("ERR_IO_CHANGEPASS", msg);
                } catch (JSONException e) {
                    String msg = e.getMessage();
                    createMessage(msg).show();
                    Log.e("ERR_JSON_CHANGEPASS", msg);
                }

            } else {
                createMessage("ASYNC ERROR").show();
            }

        }
    }


}
