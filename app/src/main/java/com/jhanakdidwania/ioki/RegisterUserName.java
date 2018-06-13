package com.jhanakdidwania.ioki;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUserName extends AppCompatActivity {

    private EditText mUsername;
    private EditText mName;
    private Button mNext;
    private String USERNAME_EXTRA;
    private String NAME_EXTRA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_username);
        mUsername = (EditText) findViewById(R.id.Username);
        mName = (EditText) findViewById(R.id.Name);
        mNext = findViewById(R.id.Next1);
    }

    public void MoveToNextStepOfRegistration(View view) {
        String username = getUsername();
        String name = getName();
        if (username.isEmpty() || name.isEmpty())
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        else {
            //save the name and username into the database
            NetworkUtils.setName(name);
            NetworkUtils.setUsername(username);
            Intent mIntent = new Intent(this, ResigterPassword.class);
            startActivity(mIntent);
            finish();
        }
    }


    public String getUsername() {
        String username = mUsername.getText().toString();
        return username;
    }

    public String getName() {
        String name = mName.getText().toString();
        return name;
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