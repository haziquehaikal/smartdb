package com.picotech.smartdb.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.picotech.smartdb.R;
import com.picotech.smartdb.activities.ChangePasswdActivity;
import com.picotech.smartdb.activities.MainActivity;
import com.picotech.smartdb.app.Endpoints;
import com.picotech.smartdb.app.Env;
import com.picotech.smartdb.utils.NetworkPreferences;
import com.picotech.smartdb.utils.SessionPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UserLoginFragment extends Fragment implements View.OnClickListener {

    public JSONObject logindata;
    private View rootView;
    private TextView inputemail, inputpassword;
    private Button btnregister, btnlogin;
    private NetworkPreferences networkPreferences;
    private VideoView videoView;
    private ProgressDialog progressDialog;
    private SessionPreferences sessionPreferences;
    private Endpoints endpoints;

    public UserLoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.view_fragment_userlogin, null);
        btnlogin = rootView.findViewById(R.id.btnLogin);
        btnregister = rootView.findViewById(R.id.btnRegister);
        inputemail = rootView.findViewById(R.id.inputEmail);
        inputpassword = rootView.findViewById(R.id.inputPassword);
        btnlogin.setOnClickListener(this);
        btnlogin.setOnClickListener(this);

        //prog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");

        //network
        endpoints = new Endpoints(Env.SYSTEM_ENVIRONMENT, getActivity());
        networkPreferences = new NetworkPreferences();

        //session
        sessionPreferences = new SessionPreferences(getActivity());


        return rootView;
    }

    public void showDialog() {

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    public void closeDialog() {

        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }

    }

    private void doLogin() {

    }

    private Snackbar createMessage(String msg) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content)
                , msg,
                Snackbar.LENGTH_LONG);

        return snackbar;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:
                new doLoginThread().execute();
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//                doLogin();
                break;
            case R.id.btnRegister:
                break;
        }

    }

    public class doLoginThread extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            showDialog();
            logindata = new JSONObject();
            try {
                logindata.put("usermail", inputemail.getText().toString());
                logindata.put("password", inputpassword.getText().toString());
            } catch (JSONException e) {
                String msg = e.getMessage();
                createMessage(msg).show();
                Log.e("ERR_JSON_LOGIN_FIRST", "MSG: " + msg);
            }


        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            if (networkPreferences.postData(logindata, endpoints.getLoginApi())) {
                closeDialog();
                return true;
            } else {
                closeDialog();
                createMessage(networkPreferences.getErrorMessage()).show();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean stat) {

            if (stat) {
                try {
                    JSONObject resdata = new JSONObject(networkPreferences.getHttpResponse());
                    if (!resdata.getBoolean("error")) {
                        if (resdata.getInt("temp_pw") == 1) {
                            Intent intent = new Intent(getActivity(), ChangePasswdActivity.class);
                            intent.putExtra("userid", resdata.getString("user_id"));
                            startActivity(intent);
                        } else {
                            sessionPreferences.setSessionValue(
                                    resdata.getString("email"),
                                    resdata.getString("name"),
                                    resdata.getString("user_id"),
                                    resdata.getString("token_key")
                            );
                            sessionPreferences.setLoginStatus(true);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        createMessage(resdata.getString("message")).show();
                    }
                } catch (IOException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_IO_LOGIN: " + msg).show();
                    Log.e("ERR_IO_LOGIN", "MSG: " + e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    String msg = e.getMessage();
                    createMessage("ERR_JSON_LOGIN_SECOND: " + msg).show();
                    Log.e("ERR_JSON_LOGIN_SECOND", "MSG: " + e.getMessage());
                }
            }
        }

    }

}
