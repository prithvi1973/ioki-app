package com.jhanakdidwania.ioki;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

class RegisterEmail extends AppCompatActivity{

    EditText mEmail;
    Button mNext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_email);
        mEmail = (EditText)findViewById(R.id.email);
        mNext = (Button) findViewById(R.id.Next4);
    }

    public void MoveToNextStepOfRegistration(View view) {
        //check for validity of email
        //if valid, move to next step i.e. completed, move to login page again
        NetworkUtils.setEmail(mEmail.getText().toString());
        Intent intent = new Intent(this, RegisterMobile.class);
        startActivity(intent);
        finish();
    }
}
