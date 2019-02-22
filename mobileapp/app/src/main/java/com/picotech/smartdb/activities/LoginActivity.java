package com.picotech.smartdb.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.picotech.smartdb.R;
import com.picotech.smartdb.fragments.UserLoginFragment;
import com.picotech.smartdb.utils.NetworkPreferences;
import com.picotech.smartdb.utils.SessionPreferences;

public class LoginActivity extends AppCompatActivity {

    private Button btnlogin, btnregister;
    private TextView email, password;
    private NetworkPreferences networkPreferences;
    private SessionPreferences sessionPreferences;
    private VideoView videoView;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_login);

        //form and button
        btnlogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);

        //Session manager
        sessionPreferences = new SessionPreferences(getApplicationContext());
        if (sessionPreferences.checkLoginStatus()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //video
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        videoView = findViewById(R.id.loginVideo);

        //video on login
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.haziq_apps);
        videoView.setDrawingCacheEnabled(true);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new UserLoginFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }

}