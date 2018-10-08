package com.ioki.key;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddLock extends AppCompatActivity{

    EditText LockId;
    EditText LockName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lock);
        LockId = findViewById(R.id.lock_id);
        LockName = findViewById(R.id.lock_name);
    }

    public void addANewLock(View view) {

        String id = LockId.getText().toString();
        String name = LockName.getText().toString();

        // TODO: add the newly created lock to recycler view
        // TODO: register new lock on server
    }
}