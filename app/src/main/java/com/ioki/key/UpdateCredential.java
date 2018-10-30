package com.ioki.key;

import android.content.Context;
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

public class UpdateCredential extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText login, password;
    Spinner spinner;
    String typeSelected = "";
    private String id = "";
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_credential);
        login = findViewById(R.id.loginUpdateCredential);
        password = findViewById(R.id.passwordUpdateCredentials);
        spinner = (Spinner) findViewById(R.id.updateSpinner);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("LOGIN");
            Log.d("ioki-debug",this.id);
        } else finish();

        this.context = this;

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
        typeSelected = typeSelected.toLowerCase();
    }

    public void onNothingSelected(AdapterView<?> arg0) {}

    public void updateCredential(View view) {
        String newLogin = login.getText().toString();
        String newPass = password.getText().toString();
        String newType = typeSelected;
        if(login.getText().toString().equals("")
                || password.getText().toString().equals("")){
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else if(typeSelected.equals("")){
            Toast.makeText(this, "Select a type", Toast.LENGTH_SHORT).show();
        }
        else{
            new updateCredentialInfoTask(this.id,context).execute(newLogin,newPass,newType);
        }
    }

    // Inner class to invoke POST request for sending credential update process
    private class updateCredentialInfoTask extends AsyncTask<String, Void, String> {

        private final Context context;
        private String id;
        private String response;

        updateCredentialInfoTask(String id, Context context) {
            this.id = id;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("login",params[0]);
            dataParams.put("password",params[1]);
            dataParams.put("type",params[2]);
            dataParams.put("submit","1");

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + "credentials/update/" + id;

            // Fetching response using utility function
            response = NetworkUtils.performPostCall(requestURL,dataParams);
            return response;
        }

        @Override
        protected void onPostExecute(String queryResults) {
            if (response != null && !response.equals("")) {
                JSONObject json;
                try {
                    json = new JSONObject(response);
                    getPreferenceObject().saveData(RESPONSE, response);
                    String type = json.getJSONArray("messages").getJSONObject(0).getString("type");
                    String message = json.getJSONArray("messages").getJSONObject(0).getString("message");
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                    if(type.equals("success")) finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
