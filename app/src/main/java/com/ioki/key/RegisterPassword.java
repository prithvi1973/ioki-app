package com.ioki.key;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterPassword extends AppCompatActivity {

    EditText mPassword;
    EditText mConfirmP;
    Button mNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_password);
        mPassword= findViewById(R.id.Password);
        mConfirmP= findViewById(R.id.ConfirmPassword);
        mNext= findViewById(R.id.Next2);
    }

    // Helper function to check password validity
    boolean validate(String password){
        int countCapital=0, countSmall=0, countdigit=0;
        int i=0;
        while(i<password.length()){
            if(Character.isDigit(password.charAt(i))) countdigit++;
            else if(Character.isLowerCase(password.charAt(i))) countSmall++;
            else if(Character.isUpperCase(password.charAt(i))) countCapital++;
            i++;
        }
        Log.d("test",String.valueOf(countCapital)+" "+String.valueOf(countSmall)+" "+String.valueOf(countdigit));
        return countdigit >= 2 && countCapital >= 2 && countSmall >= 2;
    }

    public void moveToNextStepOfRegistration(View view) {
        String password = mPassword.getText().toString();
        String confirmp = mConfirmP.getText().toString();

        if(password.length()>=8) {
            if (password.equals(confirmp)) {
                if (validate(password)) {
                    User.setPassword(password);
                    Intent myIntent = new Intent(this, RegisterPIN.class);
                    startActivity(myIntent);
                    finish();
                } else Toast.makeText(this, "At least 2 upper case, 2 lower case letters and 2 digits", Toast.LENGTH_LONG).show();
            } else Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Password must be 8 characters or more", Toast.LENGTH_SHORT).show();
    }
}
