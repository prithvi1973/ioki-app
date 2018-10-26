package com.ioki.key;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class ShareAccess extends AppCompatActivity {

    EditText username;
    TextView time;
    TextView date;

    TimePicker mTimePicker;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int month, year, day, hour, minute, second;
    private String type, id;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_access);
        username = findViewById(R.id.username);
        time = findViewById(R.id.timeTextView);
        date = findViewById(R.id.dateTextView);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            type = extras.getString("TYPE");
            id = extras.getString("ID");
        }

        DateTimeFormatter dtf_date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate currentDate = LocalDate.now();
        date.setText(dtf_date.format(currentDate));

        DateTimeFormatter dtf_time = DateTimeFormatter.ofPattern("HH:MM:SS");
        LocalTime currentTime = LocalTime.now();
        time.setText(dtf_time.format(currentTime));

        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH)+1;
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(),
                        R.style.Theme_AppCompat_Dialog,
                        mDateSetListener,
                        year, month, day);
                datePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
                month = month1+1;
                day = dayOfMonth;
                year = year1;
                date.setText(dayOfMonth+"/"+month+"/"+year);
            }
        };
    }

    // TODO: Add time picker dialog, check the working of date picker

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setExpiryTime(View view) {
        hour = mTimePicker.getHour();
        minute = mTimePicker.getMinute();
    }
}
