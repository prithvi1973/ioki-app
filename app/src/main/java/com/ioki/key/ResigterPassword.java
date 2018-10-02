package com.ioki.key;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResigterPassword extends AppCompatActivity {

    EditText mPassword;
    EditText mConfirmP;
    Button mNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_password);
        mPassword= (EditText)findViewById(R.id.password);
        mConfirmP= (EditText)findViewById(R.id.ConfirmPassword);
        mNext= (Button) findViewById(R.id.Next2);

        if(savedInstanceState!= null){

        }
    }

    boolean validate(String password) {
        int countCapital, countSmall, countdigit;
        countCapital= countdigit= countSmall= 0;

        if(password.length()>=8){
            int i=0;
            int[] hash = new int[256];

            while(i < password.length()){
                hash[password.charAt(i)]++;
                i++;
            }
            for(int j=0; j<=9; j++){
                if(hash[j]>0) countdigit++;
                if(countdigit>= 2) break;
            }
            for(int j=97; j<=122; j++){
                if(hash[j]>0) countSmall++;
                if(countSmall>= 2) break;
            }
            for(int j=65; j<=90; j++){
                if(hash[j]>0) countCapital++;
                if(countCapital>= 2) break;
            }

            if(countdigit<2 || countCapital<2 || countSmall<2){
                return false;
            }
            return true;
        }
        return false;
    }
    public void MoveToNextStepOfRegistration(View view) {
        String password = mPassword.getText().toString();
        String confirmp = mConfirmP.getText().toString();

        if(password.equals(confirmp)){

            if(validate(password)){
                User.setPassword(password);
                Intent myIntent= new Intent(this, RegisterPIN.class);
                startActivity(myIntent);
                finish();
            }else{
                Toast.makeText(this, "Password must be a combination of 2 capital letters, two small letters and 2 digits",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Password and confirm password must be same", Toast.LENGTH_SHORT ).show();
        }
    }
}
