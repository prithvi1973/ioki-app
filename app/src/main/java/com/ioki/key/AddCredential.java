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

public class AddCredential extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText link, login, password;
    Spinner spinner;
    String typeSelected = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_credential);
        link = findViewById(R.id.link);
        login = findViewById(R.id.loginAddCredential);
        password = findViewById(R.id.passwordAddCredentials);
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Username");
        categories.add("Email");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        typeSelected = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {}

    public void addANewCredential(View view) {
        if(link.getText().toString().equals("") || login.getText().toString().equals("")
                || password.getText().toString().equals("")){
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else if(typeSelected.equals("")){
            Toast.makeText(this, "Select a type", Toast.LENGTH_SHORT).show();
        }
        else{
            // TODO: Register new credential on the server
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
