package com.picotech.smartdb.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.picotech.smartdb.R;

public class UserRegisterFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Button btnsubmit, btnback;
    private EditText inputEmail, inputUsername, inputPassword, inputRePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.view_fragment_viewregister, null);
        btnsubmit = rootView.findViewById(R.id.btnSubmit);
        return rootView;
    }


    private boolean checkForm() {

        if (inputEmail.getText().toString().isEmpty()) {
            createMessage("Please enter your name").show();
            return false;

        } else if (inputUsername.getText().toString().isEmpty()) {
            createMessage("Please enter your email").show();
            return false;

        } else if (inputPassword.getText().toString().isEmpty()) {
            createMessage("Please enter your password").show();
            return false;

        } else if (inputRePassword.getText().toString().isEmpty()) {
            createMessage("Please retype your password").show();

            return false;
        }
        return true;
    }

    private Snackbar createMessage(String msg) {

        Snackbar snackbar = Snackbar.make(rootView.findViewById(android.R.id.content), msg,
                Snackbar.LENGTH_LONG);

        return snackbar;

    }

    private void doSubmit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                break;
            case R.id.btnBack:
                break;

        }
    }
}
