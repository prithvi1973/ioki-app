package com.ioki.key;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName = findViewById(R.id.name);
        mPassword = findViewById(R.id.password);

        /*Intent afterRegistration = getIntent();

        if(afterRegistration.hasExtra("name") && afterRegistration.hasExtra("password")){
            String name= afterRegistration.getStringExtra("name");
            String password = afterRegistration.getStringExtra("password");
            mName.setText(name);
            mPassword.setText(password);
        }*/
    }

    public void LoginIntoAccount(View view) {

         String name = mName.getText().toString();
         String password = mPassword.getText().toString();
         new IOkiLoginTask(view.getContext(),this).execute(name, password);
    }

    public void registerNewUser(View view) {
        Intent myIntent = new Intent(MainActivity.this, RegisterUserName.class);
        startActivity(myIntent);
    }

    // Inner class to invoke POST request for user/login
    private static class IOkiLoginTask extends AsyncTask<String, Void, String> {

        // Stores response from server
        String response = "";
        @SuppressLint("StaticFieldLeak")
        private Context context;
        private Activity act;

        public IOkiLoginTask(Context context, Activity act) {
            this.context = context;
            this.act = act;
        }

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("username", params[0]);
            dataParams.put("password",params[1]);
            dataParams.put("submit","1");

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + "users/";

            // Fetching response using utility function
            response = NetworkUtils.performPostCall(requestURL,dataParams);
            return response;
        }

        @Override
        protected void onPostExecute(String queryResults) {
            if (response!= null && !response.equals("")) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray messages = json.getJSONArray("messages");
                    String type = messages.getJSONObject(0).getString("type");

                    if(type.equals("success")) {
                        Intent myIntent = new Intent(act, Dashboard.class);
                        act.startActivity(myIntent);
                        act.finish();
                        Toast.makeText(this.context,"Login Successful",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(this.context,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                    }

                    Log.d("ioki", "JSON Response Type: "+type);
                    Log.d("ioki", "JSON Response: "+response);
                } catch (JSONException e) {
                    Toast.makeText(this.context, "Invalid Server Response", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    Log.d("ioki", "Invalid Response: "+response);
                }
            }else{
                Toast.makeText(this.context, "Couldn't communicate with server", Toast.LENGTH_LONG).show();
                Log.d("ioki","No results fetched from user/");
            }
        }
    }
}

//cover flow for various credentials