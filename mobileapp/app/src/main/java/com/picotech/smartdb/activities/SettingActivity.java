package com.picotech.smartdb.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.picotech.smartdb.R;
import com.picotech.smartdb.utils.UIManager;

public class SettingActivity extends AppCompatActivity {

    private UIManager uiManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_setting);
        uiManager = new UIManager(getLayoutInflater(), getSupportActionBar());
        uiManager.setCenterTitle("Setting");

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
}
