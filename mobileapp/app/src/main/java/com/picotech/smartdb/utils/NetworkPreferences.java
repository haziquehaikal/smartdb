package com.picotech.smartdb.utils;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkPreferences {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    private Response response;
    private String api_url, api_data, api_token;
    private String err_msg;

    public NetworkPreferences() {

        //this.api_url = url;
        // this.api_data = data;
        //this.api_token = token;
        client = new OkHttpClient();
    }


    //send data to server via POST
    public boolean postData(JSONObject data , String url) {
        try {

            RequestBody body = RequestBody.create(JSON, data.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
            return true;

        } catch (IOException e) {
            String msg = e.getMessage();
            setErrorMessage(msg);
            Log.e("ERR_NETPREF_POSTDATA", "ERR: " + msg);
            return false;
        }
    }

    //send data to server via POST
    public boolean putData(JSONObject data , String url) {
        try {
           // Log.d("DATA_SAMPAI", api_url);
            RequestBody body = RequestBody.create(JSON, data.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
            response = client.newCall(request).execute();
            return true;

        } catch (IOException e) {
            String msg = e.getMessage();
            setErrorMessage(msg);
            Log.e("ERR_NETPREF_POSTDATA", "ERR: " + msg);
            return false;
        }
    }


    //get data from user via GET
    public boolean getData(String url) {

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            return true;
        } catch (IOException e) {
            String msg = e.getMessage();
            setErrorMessage(msg);
            Log.e("ERR_NETPREF_GETDATA", "ERR: " + msg);
            return false;
        }
    }


    public String getHttpResponse() throws IOException {
        if (response.code() != 500) {
            String msgdata = response.body().string();

            if (msgdata != null) {
                return msgdata;
            } else {
                return "No response body available";
            }

        } else {
            Log.e("ERR_NETPREF_RESPONSE", "NOT SUCCESS");
            return "{\"error\":true,\"message\":\"Request Not success\"}";
        }


    }

    public int getHttpCode() {
        return response.code();
    }

    public String getErrorMessage() {
        if (err_msg != null)
            return err_msg;
        else {
            return "No Message available";
        }
    }

    private void setErrorMessage(String msg) {
        this.err_msg = msg;
    }
}
