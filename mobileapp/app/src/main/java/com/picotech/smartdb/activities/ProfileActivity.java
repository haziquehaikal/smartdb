package com.picotech.smartdb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.picotech.smartdb.R;
import com.picotech.smartdb.utils.NetworkPreferences;
import com.picotech.smartdb.utils.SessionPreferences;
import com.picotech.smartdb.utils.UIManager;

public class ProfileActivity extends AppCompatActivity {

    private UIManager uiManager;
    private TextView disfullname, disemail;
    private ImageView editprofile, scanqr;
    private NetworkPreferences networkPreferences;
    private SessionPreferences sessionPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_profile);
        uiManager = new UIManager(getLayoutInflater(), getSupportActionBar());
        uiManager.setCenterTitle("My Profile");
        getSupportActionBar().setElevation(0);


        disfullname = findViewById(R.id.userName);
        disemail = findViewById(R.id.userEmail);

        //onclick
        editprofile = findViewById(R.id.iconedit);
        scanqr = findViewById(R.id.iconqr);

        //session
        sessionPreferences = new SessionPreferences(getApplicationContext());

        //set value profile
        disfullname.setText(sessionPreferences.getUserDetails().get("name"));
        disemail.setText(sessionPreferences.getUserDetails().get("email"));

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                bundle.putString("name", sessionPreferences.getUserDetails().get("name"));
                bundle.putString("email", sessionPreferences.getUserDetails().get("email"));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });


    }


    public void setProfileInfo(String name, String email, String phone,
                               String deviceid) {

        disfullname.setText(email);
        disemail.setText(email);


    }


    @Override
    public void onStart() {
        super.onStart();
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


    /*too time consuming*/
//    public class getProfileData extends AsyncTask<Void, String, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            Endpoints endpoints = new Endpoints(Env.SYSTEM_ENVIRONMENT, getApplicationContext());
//            networkPreferences = new NetworkPreferences(endpoints.getProfileApi());
//
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            if (networkPreferences.getData()) {
//                return true;
//            }
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean stat) {
//            if (stat) {
//                try {
//                    JSONObject data = new JSONObject(networkPreferences.getHttpResponse());
//                    setProfileInfo(
//                            data.getString("name"),
//                            data.getString("email"),
//                            data.getString("phone_no"),
//                            data.getString("dbdevice_id")
//                    );
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


}
