package com.ioki.key;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddCredential extends AppCompatActivity {

    EditText link, login, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_credential);
        link = findViewById(R.id.link);
        login = findViewById(R.id.loginAddCredential);
        password = findViewById(R.id.passwordAddCredentials);

    }

    public void addANewCredential(View view) {
    }
}
