package com.ioki.key;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class UpdateLock extends AppCompatActivity {

    EditText name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_lock);

        name = findViewById(R.id.update_lock_name);
    }

    public void updateLock(View view) {
        String newName = name.getText().toString();
        // TODO: Send this to the server

        finish();
    }
}
