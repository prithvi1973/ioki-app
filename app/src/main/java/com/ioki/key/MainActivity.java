package com.ioki.key;

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
         new IOkiLoginTask(view.getContext()).execute(name,password);
    }

    public void registerNewUser(View view) {
        Intent myIntent = new Intent(MainActivity.this, RegisterUserName.class);
        startActivity(myIntent);
    }

    // Inner class to invoke POST request for user/login
    public static class IOkiLoginTask extends AsyncTask<String, Void, String> {

        // Stores response from server
        String response = "";
        private Context context;

        public IOkiLoginTask(Context context) {
            this.context = context;
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
                    for(int i=0; i<messages.length(); i++) {
                        Toast.makeText(this.context, messages.getJSONObject(i).getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("ioki", "Response: "+response);
            }else{
                Log.d("ioki","No results fetched from user/");
            }
        }
    }
}

//cover flow for various credentials