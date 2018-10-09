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

    private Context mContext;
    private EditText mName;
    private EditText mPassword;
    private static UserDefinedSharedPreference preferenceObject;

    static final String USERNAME = "usernameKey";
    static final String PASSWORD = "passwordKey";
    static final String RESPONSE = "responseKey";
    private String defaultValue = "DEFAULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mName = findViewById(R.id.name);
        mPassword = findViewById(R.id.password);
        preferenceObject = new UserDefinedSharedPreference(mContext);

        String spUsername = preferenceObject.getPreferences(USERNAME);
        String spPassword = preferenceObject.getPreferences(PASSWORD);
        String spResponse = preferenceObject.getPreferences(USERNAME);

        Log.d("ioki-debug",spUsername+" | "+spPassword+" | "+spResponse);

        if(!spUsername.equals(defaultValue) && !spPassword.equals(defaultValue)
                && !spResponse.equals(defaultValue)){
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    public static UserDefinedSharedPreference getPreferenceObject() {
        return preferenceObject;
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
                        User.setUsername(json.getJSONObject("session").getString("user"));
                        User.setPassword(json.getJSONObject("session").getString("pass"));
                        Intent myIntent = new Intent(act, Dashboard.class);
                        act.startActivity(myIntent);
                        act.finish();
                        Toast.makeText(this.context,"Login Successful",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Toast.makeText(this.context,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                    }

                    Response resObj = new Response(json);
                    if(resObj.isValid()) {
                        preferenceObject.saveData(RESPONSE, response);
                        preferenceObject.saveData(USERNAME, User.getUsername());
                        preferenceObject.saveData(PASSWORD, User.getPassword());
                    }
                } catch (JSONException e) {
                    Toast.makeText(this.context, "Invalid Server Response", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this.context, "Couldn't communicate with server", Toast.LENGTH_LONG).show();
            }
        }
    }
}
