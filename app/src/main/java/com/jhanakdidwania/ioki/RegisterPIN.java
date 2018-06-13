package com.jhanakdidwania.ioki;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterPIN extends AppCompatActivity{

    private EditText mPin;
    private Button mNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_pin);
        mPin = (EditText)findViewById(R.id.pin);
        mNext = (Button)findViewById(R.id.Next3);
    }

    public void MoveToNextStepOfRegistration(View view) {
        if(mPin.getText().toString().length()==4){
            NetworkUtils.setPIN(mPin.getText().toString());
            Intent myIntent= new Intent(this, RegisterEmail.class);
            startActivity(myIntent);
            finish();
        }else{
            Toast.makeText(this, "Invalid pin", Toast.LENGTH_SHORT).show();
        }
    }
}
