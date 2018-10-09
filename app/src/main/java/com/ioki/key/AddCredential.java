package com.ioki.key;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ioki.key.MainActivity.RESPONSE;
import static com.ioki.key.MainActivity.getPreferenceObject;

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

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public void addANewCredential(View view) {
        if (link.getText().toString().equals("") || login.getText().toString().equals("")
                || password.getText().toString().equals("")) {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        } else if (typeSelected.equals("")) {
            Toast.makeText(this, "Select a type", Toast.LENGTH_SHORT).show();
        } else {
            new addCredentialTask(this).execute(link.getText().toString(),login.getText().toString(),password.getText().toString(),typeSelected,"1");
        }
    }

    // Inner class to invoke POST request for user/credentials/add
    private static class addCredentialTask extends AsyncTask<String, Void, String> {

        private String response;
        private Activity act;

        public addCredentialTask(Activity act) {
            this.response = "";
            this.act = act;
        }

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("link", params[0]);
            dataParams.put("login",params[1]);
            dataParams.put("password",params[2]);
            dataParams.put("type",params[3].toLowerCase());
            dataParams.put("submit",params[4]);

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + "credentials/add/";

            // Fetching response using utility function
            response = NetworkUtils.performPostCall(requestURL,dataParams);
            return response;
        }

        protected void onPostExecute(String queryResults) {
            if (response != null && !response.equals("")) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    getPreferenceObject().saveData(RESPONSE, response);

                    String status = json.getJSONArray("messages").getJSONObject(0).getString("type");
                    String message = json.getJSONArray("messages").getJSONObject(0).getString("message");
                    Toast.makeText(act.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    if(status.equals("success")) act.finish();

                } catch (JSONException e) {
                    Toast.makeText(act.getApplicationContext(),"Invalid Server Response",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}