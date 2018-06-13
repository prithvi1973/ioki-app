package com.jhanakdidwania.ioki;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterMobile extends AppCompatActivity{

    EditText mMobile;
    Button mDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_mobile);

        mMobile = (EditText)findViewById(R.id.mobile);
        mDone = (Button)findViewById(R.id.done_button);
    }

    public void RegistrationDone(View view){
        String mobileNumber = mMobile.getText().toString();
        NetworkUtils.setMobile(mobileNumber);

        //What to do next, basically we need to send all the sign up details to the server
        //can call the login page at this time with the extra text of name and password to login
    }
}
