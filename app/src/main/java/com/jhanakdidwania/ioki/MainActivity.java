package com.jhanakdidwania.ioki;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mPassword;
    private static boolean check_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName = (EditText) findViewById(R.id.name);
        mPassword = (EditText) findViewById(R.id.password);
    }

    boolean checker(String name, String password){
        Log.d("ioki", mName.getText().toString());
        Log.d("ioki", mPassword.getText().toString());

        if(name == "" || name.length()<5){
            Toast.makeText(this, "Invalid Username",Toast.LENGTH_SHORT).show();
            return false;
        }

        else if( password.length() < 5){
            Toast.makeText(this, "Invalid Password",Toast.LENGTH_SHORT).show();
            return false;
        }

        else{
            Toast.makeText(this, "Login Successful. Please wait.",Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    public void LoginIntoAccount(View view) {

         String name = mName.getText().toString();
         String password = mPassword.getText().toString();
         check_result = checker(name, password);

             if(check_result){

                URL LoginUrl = NetworkUtils.buildUrl(name, password);
                Log.d("ioki", LoginUrl.toString());
                new IOkiLoginTask().execute(LoginUrl);
             }

             else{
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
             }
    }

    public void registerNewUser(View view) {
        Intent myIntent = new Intent(this, form_fields.class);
        startActivity(myIntent);
        finish();
    }

    public class IOkiLoginTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String LoginResults = null;
            try {
                LoginResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return LoginResults;
        }

        @Override
        protected void onPostExecute(String queryResults) {
            if (queryResults != null && !queryResults.equals("")) {
                Log.d("ioki", queryResults);
            }
        }
    }


}
