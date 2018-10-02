package com.ioki.key;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterMobile extends AppCompatActivity{

    EditText mMobile;
    Button mDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_mobile);

        mMobile = findViewById(R.id.mobile);
        mDone = findViewById(R.id.done_button);
    }

    // Helper function to check mobile validity
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public void completeRegistration(View view){
        if(isValidMobile(mMobile.getText().toString())) {
            User.setMobile(mMobile.getText().toString());

            // Save user details locally and register
            User.writeLocalDB();
            User.register();
            String registered = User.getStatus();
            Toast.makeText(this, registered, Toast.LENGTH_SHORT).show();

            if(registered=="You have been successfully registered. Confirm your email, and login again") {
                Toast.makeText(this, "Login again to continue", Toast.LENGTH_SHORT).show();
                RegisterUserName.activityInstance.finish();
                RegisterEmail.activityInstance.finish();
                finish();
            }

        } else Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
    }
}
