package com.ioki.key;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterEmail extends AppCompatActivity{

    EditText mEmail;
    Button mNext;

    // Used to store instance of this activity to finish to later
    @SuppressLint("StaticFieldLeak")
    public static Activity activityInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_email);
        mEmail = findViewById(R.id.email);
        mNext = findViewById(R.id.Next4);
        activityInstance = this;
    }

    // Helper Function to check email validity
    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void moveToNextStepOfRegistration(View view) {
        if(isValidEmail(mEmail.getText().toString())) {
            User.setEmail(mEmail.getText().toString());
            Intent intent = new Intent(this, RegisterMobile.class);
            startActivity(intent);
        } else Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
    }
}
