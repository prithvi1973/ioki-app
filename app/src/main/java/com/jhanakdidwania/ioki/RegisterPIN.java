package com.jhanakdidwania.ioki;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterPIN extends AppCompatActivity{

    private EditText mPin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_pin);
        mPin = findViewById(R.id.PIN);
    }

    public void moveToNextStepOfRegistration(View view) {
        if(mPin.getText().toString().length()==4){
            User.setPIN(mPin.getText().toString());
            Intent myIntent= new Intent(this, RegisterEmail.class);
            startActivity(myIntent);
            finish();
        }else{
            Toast.makeText(this, "Pin must have 4 digits", Toast.LENGTH_LONG).show();
        }
    }
}
