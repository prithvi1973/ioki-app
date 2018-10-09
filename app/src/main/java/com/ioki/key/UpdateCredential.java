package com.ioki.key;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UpdateCredential extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText login, password;
    Spinner spinner;
    String typeSelected = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_credential);
        login = findViewById(R.id.loginUpdateCredential);
        password = findViewById(R.id.passwordUpdateCredentials);
        spinner = (Spinner) findViewById(R.id.updateSpinner);

        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Username");
        categories.add("Email");

        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        typeSelected = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {}

    public void addANewCredential(View view) {
        if(login.getText().toString().equals("")
                || password.getText().toString().equals("")){
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else if(typeSelected.equals("")){
            Toast.makeText(this, "Select a type", Toast.LENGTH_SHORT).show();
        }
        else{
            // TODO: Update old credential on the server
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
