package com.ioki.key;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

public class UpdateLock extends AppCompatActivity {

    EditText name;
    private String id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_lock);
        name = findViewById(R.id.update_lock_name);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            this.id = extras.getString("LOCKID");
        }
    }

    public void updateLock(View view) {
        String newName = name.getText().toString();
        new updateLockInfoTask(this.id).execute(newName);
        finish();
    }

    // Inner class to invoke POST request for sending lock update process
    private class updateLockInfoTask extends AsyncTask<String, Void, String> {

        private String id;
        private String response;

        public updateLockInfoTask(String id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("name",params[0]);
            dataParams.put("submit","1");

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + "locks/update/" + id;

            // Fetching response using utility function
            response = NetworkUtils.performPostCall(requestURL,dataParams);
            return response;
        }

        @Override
        protected void onPostExecute(String queryResults) {
            Log.d("ioki-debug",response);
        }
    }

}
