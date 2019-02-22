package com.picotech.smartdb.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.picotech.smartdb.R;
import com.picotech.smartdb.utils.SharedPref;
import com.picotech.smartdb.utils.UIManager;
import com.picotech.smartdb.utils.WebsocketPreferences;
import com.picotech.smartdb.utils.sshUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class TestActivity extends AppCompatActivity {

    public SharedPref session;
    public sshUtils haha;
    private UIManager uiManager;
    private SwitchCompat fuse1;
    public SendLah ehah;
    private WebsocketPreferences websocketPreferences;
    private WebSocket ws;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testmanage);

        uiManager = new UIManager(getLayoutInflater(), getSupportActionBar());
        uiManager.setCenterTitle("Manage Device");
        session = new SharedPref(getApplicationContext());
        haha = new sshUtils();
        fuse1 = findViewById(R.id.fuse1);
        ehah = new SendLah();

        OkHttpClient client = new OkHttpClient();
        websocketPreferences = new WebsocketPreferences(getApplicationContext());
        Request request = new Request.Builder().url("ws://192.168.43.134:8765").build();
        ws = client.newWebSocket(request, websocketPreferences);
        client.dispatcher().executorService();


        fuse1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    ws.send("1");
                } else if (!isChecked) {
                    // ehah.execute("python /home/pi/servo2.py");
                    ws.send("0");
                }
                // ehah.cancel(true);
            }
        });
    }

    private class SendLah extends AsyncTask<String, Void, String> {

        private boolean running = true;


        @Override
        protected void onCancelled() {
            running = false;

        }

        @Override
        protected String doInBackground(String... strings) {


            Log.d(">>>>>>", "param : " + strings[0]);

            String result = haha.sendCommand(strings[0], "hewehw");


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(">>>>>>", "post : " + s);
        }
    }
}
