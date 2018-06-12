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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName = (EditText) findViewById(R.id.name);
        mPassword = (EditText) findViewById(R.id.password);

        /*Intent afterRegisteration = getIntent();

        if(afterRegisteration.hasExtra("name") && afterRegisteration.hasExtra("password")){
            String name= afterRegisteration.getStringExtra("name");
            String password = afterRegisteration.getStringExtra("password");
            mName.setText(name);
            mPassword.setText(password);
        }*/
    }

    public void LoginIntoAccount(View view) {

         String name = mName.getText().toString();
         String password = mPassword.getText().toString();

         URL LoginUrl = NetworkUtils.buildUrl(name, password);
         Log.d("ioki", LoginUrl.toString());
         new IOkiLoginTask().execute(LoginUrl);

    }

    public void registerNewUser(View view) {
        Intent myIntent = new Intent(MainActivity.this, RegisterUserName.class);
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
            }else{
                Log.d("ioki","No results fetched from login");
            }
        }
    }
}


//cover flow for various credentials