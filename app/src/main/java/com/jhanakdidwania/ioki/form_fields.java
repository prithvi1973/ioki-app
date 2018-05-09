package com.jhanakdidwania.ioki;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class form_fields extends AppCompatActivity implements View.OnKeyListener{

    final String FormFields[]= {"Name", "Password", "Confirm Password","Mobile_number", "Email", "PIN"};
    private ProgressBar mProgressBar;
    private EditText mEditText;
    private static int step = 1;  //total steps = 6

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mEditText = (EditText) findViewById(R.id.formFields);
        mEditText.setOnKeyListener(this);

        layout_function();
    }

    void layout_function(){
        //name
        if(step == 1){
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            mEditText.setHint("Name");
        }
        //password
        else if(step == 2 || step == 3 ){
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if(step == 2) mEditText.setHint("Password");
            else mEditText.setHint("Confirm Password");
        }
        //mobile_number
        else if(step == 4){
            mEditText.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            mEditText.setHint("Mobile number");
        }
        //email
        else if(step == 5){
            mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            mEditText.setHint("Email");
        }
        //pin
        else if(step == 6){
            mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            mEditText.setHint("6 Digit PIN");
        }
    }


    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event){
        //enter key pressed
        if(keyCode == EditorInfo.IME_NULL){
            boolean valid= true;
            //check the validity of the field input by user
            if(valid){
                String text = mEditText.getText().toString();
                step = step + 1;
                next();
            }
            else{
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    String name;
    String password;

    private void next() {
        //check the input from edit text field.
        if(step <= 6){
            mEditText.setText(FormFields[step-1]);
            layout_function();
        }
        else{
            Toast.makeText(this, "Registeration Successful!", Toast.LENGTH_SHORT).show();
            Intent mIntent = new Intent(this, MainActivity.class);
            mIntent.putExtra("name", name);
            mIntent.putExtra("password", password);
            startActivity(mIntent);
            finish();
        }
    }
}
