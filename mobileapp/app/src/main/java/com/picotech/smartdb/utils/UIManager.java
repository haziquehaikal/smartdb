package com.picotech.smartdb.utils;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.picotech.smartdb.R;

public class UIManager {

    private LayoutInflater layoutInflater;
    private ActionBar actionBar;
    private Activity activity;

    public UIManager(LayoutInflater a, ActionBar b) {
        this.actionBar = b;
        this.layoutInflater = a;
    }


    public View setCenterTitle(String title) {
        View actionbarview = layoutInflater.inflate(R.layout.actionbar_text, null);
        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams(
                android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        );

        TextView actiontitle = actionbarview.findViewById(R.id.actionbar_textview);
        actiontitle.setText(title);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(actionbarview, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        return actionbarview;
    }


    public View setCenterFragmentTitle(String title) {
        View actionbarview = layoutInflater.inflate(R.layout.actionbar_text, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        );

        TextView actiontitle = actionbarview.findViewById(R.id.actionbar_textview);
        actiontitle.setText(title);
        actionBar.setCustomView(actionbarview, params);
        actionBar.setDisplayShowCustomEnabled(true);

        return actionbarview;
    }


}