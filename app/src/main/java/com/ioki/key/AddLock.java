package com.ioki.key;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.ioki.key.MainActivity.RESPONSE;
import static com.ioki.key.MainActivity.getPreferenceObject;

public class AddLock extends AppCompatActivity{

    EditText LockId;
    EditText LockName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lock);
        LockId = findViewById(R.id.lock_id);
        LockName = findViewById(R.id.lock_name);
    }

    public void addANewLock(View view) {
        String id = LockId.getText().toString();
        String name = LockName.getText().toString();

        if(id.equals("") || name.equals("")){
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else{
            new addLockTask(this).execute(id,name,"1");
        }
    }

    // Inner class to invoke POST request for user/credentials/add
    private static class addLockTask extends AsyncTask<String, Void, String> {

        private String response;
        private Activity act;

        public addLockTask(Activity act) {
            this.response = "";
            this.act = act;
        }

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("id", params[0]);
            dataParams.put("name",params[1]);
            dataParams.put("submit",params[2]);

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + "locks/add/";

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
