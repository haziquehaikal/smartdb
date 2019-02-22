package com.picotech.smartdb.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.picotech.smartdb.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer time = new Timer();
        time.schedule(task, 1000);


    }

}
