package com.picotech.smartdb.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.picotech.smartdb.activities.LoginActivity;

import java.util.HashMap;

public class SessionPreferences {

    //static
    private final static String PREF_NAME = "DBBOXLogin";
    private final static String KEY_USERNAME = "username";
    private final static String KEY_NAME = "name";
    private final static String KEY_USERID = "userid";
    private final static String KEY_PASSWORD = "password";
    private final static String KEY_EMAIL = "email";
    private final static String KEY_API_TOKEN = "api_token";
    private final static String KEY_ISLOGGEDIN = "false";

    private int PRIVATE_MODE = 0;

    private Context _context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionPreferences(Context context) {
        this._context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void setSessionValue(String email, String name, String userid, String token) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_API_TOKEN, token);
        editor.commit();
    }

    public void setLoginStatus(Boolean status) {
        editor.putBoolean(KEY_ISLOGGEDIN, status);
        editor.commit();
    }

    public void setFirebaseKey(String key) {
        editor.putString("FIREKEY", key);
        editor.commit();
    }

    public String getFirebaseKey() {
        return pref.getString("FIREKEY", null);
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> userdata = new HashMap<>();
        userdata.put("name", pref.getString(KEY_NAME, "Awesome Customer"));
        userdata.put("email", pref.getString(KEY_EMAIL, "test@smartdb.com"));
        userdata.put("user_id", pref.getString(KEY_USERID, "590256125"));
        userdata.put("api_token", pref.getString(KEY_API_TOKEN, "token123"));
        return userdata;
    }

    public boolean checkLoginStatus() {
        return pref.getBoolean(KEY_ISLOGGEDIN, false);
    }


    public void logout() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);

    }

}
