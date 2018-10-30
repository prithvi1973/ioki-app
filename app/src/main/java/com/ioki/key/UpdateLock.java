package com.ioki.key;

import android.content.Context;
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

public class UpdateLock extends AppCompatActivity {

    EditText name;
    private String id = "";
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_lock);
        name = findViewById(R.id.update_lock_name);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            this.id = extras.getString("LOCKID");
        } else finish();

        this.context = this;

    }

    public void updateLock(View view) {
        String newName = name.getText().toString();
        new updateLockInfoTask(this.id,context).execute(newName);
    }

    // Inner class to invoke POST request for sending lock update process
    private class updateLockInfoTask extends AsyncTask<String, Void, String> {

        private final Context context;
        private String id;
        private String response;

        public updateLockInfoTask(String id, Context context) {
            this.id = id;
            this.context = context;
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
