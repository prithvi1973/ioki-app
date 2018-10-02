package com.ioki.key;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUserName extends AppCompatActivity {

    private EditText mUsername;
    private EditText mName;
    // private String USERNAME_EXTRA;
    // private String NAME_EXTRA;

    // Used to store instance of this activity to finish to later
    @SuppressLint("StaticFieldLeak")
    public static Activity activityInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_username);
        mUsername = findViewById(R.id.Username);
        mName = findViewById(R.id.Name);
        activityInstance = this;
    }

    public void MoveToNextStepOfRegistration(View view) {
        String username = getUsername();
        String name = getName();
        if (username.isEmpty() || name.isEmpty())
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        else {
            //save the name and username into the database
            User.setName(name);
            User.setUsername(username);
            Intent mIntent = new Intent(this, RegisterPassword.class);
            startActivity(mIntent);
        }
    }

    public String getUsername() {
        return mUsername.getText().toString();
    }

    public String getName() {
        return mName.getText().toString();
    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String name = mName.getText().toString();
        String username = mUsername.getText().toString();
        outState.putString(USERNAME_EXTRA, username);
        outState.putString(NAME_EXTRA, name);
    }*/
}