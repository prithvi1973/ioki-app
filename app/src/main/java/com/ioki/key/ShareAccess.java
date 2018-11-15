package com.ioki.key;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import static com.ioki.key.MainActivity.RESPONSE;
import static com.ioki.key.MainActivity.getPreferenceObject;

public class ShareAccess extends AppCompatActivity{

    EditText username;
    EditText date;
    EditText hourText;
    EditText minuteText;

    TimePicker mTimePicker;
    private OnDateSetListener mDateSetListener;
    private int month;
    private int year;
    private int day;
    private String hour;
    private String minute;
    private String type, id;

    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_access);

        username = findViewById(R.id.username);
        hourText = findViewById(R.id.hour);
        minuteText = findViewById(R.id.minute);
        date = findViewById(R.id.dateTextView);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            type = extras.getString("TYPE");
            id = extras.getString("ID");
        }
        else {
            finish();
        }

        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH)+1;
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        date.setText(""+year+"-"+month+"-"+day);

        /*date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(),
                        mDateSetListener,
                        year, month, day);
                datePickerDialog.show();
            }
        });

        mDateSetListener = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
                month = month1+1;
                day = dayOfMonth;
                year = year1;
                date.setText(day+"/"+month+"/"+year);
            }
        };*/

        this.context = this;
    }

    // TODO: Add time picker dialog, check the working of date picker

    public void shareButton(View view) {
        hour = hourText.getText().toString();
        minute = minuteText.getText().toString();
        new shareTask(id,type,context).execute(username.getText().toString(), date.getText().toString(), hour+":"+minute);
    }


    // Inner class to invoke POST request for sending credential update process
    private class shareTask extends AsyncTask<String, Void, String> {

        private String id;
        private String type;
        private String response;
        private Context context;

        shareTask(String id, String type, Context context) {
            this.id = id;
            this.type = type;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("username",params[0]);
            dataParams.put("shared_till_date",params[1]);
            dataParams.put("shared_till_time",params[2]);
            dataParams.put("submit","1");

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + type + "/share/add/" + id;

            // Fetching response using utility function
            response = NetworkUtils.performPostCall(requestURL,dataParams);
            return response;
        }

        @Override
        protected void onPostExecute(String queryResults) {
            Log.d("ioki-debug",response);
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
