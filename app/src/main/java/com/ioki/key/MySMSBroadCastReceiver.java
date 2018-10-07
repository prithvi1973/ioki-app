package com.ioki.key;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class MySMSBroadCastReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault(); //object of smsManager

    public void onReceive(Context context, Intent intent) {

        //Get or Read the current incoming SMS data
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody().split(":")[1];

                    message = message.substring(0, message.length()-1);
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    // TODO: 10/5/2018 : Here we need to check the format of the message sent by the server and extract only the otp part from it 
                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message",message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
    }
}