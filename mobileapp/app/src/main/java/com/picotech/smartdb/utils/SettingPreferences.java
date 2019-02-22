package com.picotech.smartdb.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SettingPreferences {

    /*STATIC*/
    public final static String KEY_NTAH = "";

    /*lain*/
    private Context _context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SettingPreferences(Context context) {
        this._context = context;
        this.sharedPreferences = _context.getSharedPreferences("SettingPref", 0);
        this.editor = sharedPreferences.edit();
    }

    public void setSettingvalue() {

    }

    public HashMap<String, String> getSettingValue() {
        HashMap<String, String> settingvalue = new HashMap<>();
        return settingvalue;
    }

    public boolean resetAllSetting(){
        editor.clear();
        editor.commit();
        return true;

    }


}
