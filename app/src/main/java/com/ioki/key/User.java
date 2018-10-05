package com.ioki.key;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Handles registration process
 * Contains user details filled while registration
 * uses static methods to update the details and write them to DB
 * invokes POST request to user/register
 */
public class User {

    private static String username;
    private static String name;
    private static String password;
    private static String mobile;
    private static String PIN;
    private static String email;
    public static String status;
    private static boolean registered;

    User() {
        registered = false;
    }

    public static void setUsername(String username) {User.username = username;}
    public static void setName(String name) {User.name = name;}
    public static void setPassword(String password) {User.password = password;}
    public static void setMobile(String mobile) {User.mobile = mobile;}
    public static void setPIN(String PIN) {User.PIN = PIN;}
    public static void setEmail(String email) {User.email = email;}

    public static void writeLocalDB() {
        // Logging messages temporarily
        // Code for writing to local DB will come here...
        Log.d("test",username);
        Log.d("test",name);
        Log.d("test",password);
        Log.d("test",mobile);
        Log.d("test",PIN);
        Log.d("test",email);
    }

    public static void register() {
        new IOkiRegisterTask().execute(username,name,password,mobile,PIN,email);
    }

    public static String getStatus() {return status;}

    // Inner class to invoke POST request for user/register
    public static class IOkiRegisterTask extends AsyncTask<String, Void, String> {

        // Stores response from server
        String response = "";

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("name", params[1]);
            dataParams.put("username", params[0]);
            dataParams.put("email",params[5]);
            dataParams.put("phone", params[3]);
            dataParams.put("password", params[2]);
            dataParams.put("confirmPassword", params[2]);

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + "users/register/";

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
                    User.status = messages.getJSONObject(0).getString("message");
                    if(status=="You have been successfully registered. Confirm your email, and login again") registered = true;
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