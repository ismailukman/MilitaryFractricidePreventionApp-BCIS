package com.nunmsc.bcisng.nafisa.bcisng;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import java.util.ArrayList;

public class Broadcast extends AppCompatActivity {

    Button buttonSend;
    EditText textSMS;
    Spinner fon;
    String fone1, sm, sms, fone, fone2, fone3;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        buttonSend = (Button) findViewById(R.id.sms);
        fon = (Spinner) findViewById(R.id.platoon);
        textSMS = (EditText) findViewById(R.id.editmsg);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm = textSMS.getText().toString();
                sms = "BCISNG Message ID:p0101 "+sm;

                //fone = String.valueOf(fon.getSelectedItem());
                fone1 = "5558";
                fone2 = "5560";
                fone3 = "5556";
                fone = "5554";

                if( sm.equals("") || fone.equals("Select")  ){
                    Toast.makeText(getApplicationContext(), "Please enter a message or a phone number!",
                            Toast.LENGTH_SHORT).show();
                }else{

/*                    if (fone.equals("p01") ){
                        fone = "5560";
                        sendSMS( fone, sms);
                    }else if (fone.equals("p02") ) {
                        fone = "5558";
                        sendSMS( fone, sms);
                    }else if(fone.equals("p03")) {
                        fone = "5554";
                        sendSMS( fone, sms);
                    }else if(fone.equals("p05")) {
                        fone = "5556";
                        sendSMS( fone, sms);
                    }*/
                    sendSMS(fone, sms);
                    sendSMS(fone1, sms);
                    sendSMS(fone2, sms);
                    sendSMS(fone3, sms);

                }
            }
        });
    }

    //---sends a SMS message to another device---
    private void sendSMS(String phoneNo, String message) {
        SmsManager sms1 = SmsManager.getDefault();
        ArrayList<String> parts= sms1.divideMessage(message);
        int numParts = parts.size();

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

        for (int i = 0; i < numParts; i++) {

            sentIntents.add(PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0));
            deliveryIntents.add(PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0));
        }

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));


        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        // sms1.sendMultiPartTextMessage(mDestAddr,null, parts, sentIntents, deliveryIntents);
        // sms1.sendTextMessage(phoneNo, null, message, sentPI, deliveredPI);
        sms1.sendMultipartTextMessage(phoneNo, null, parts, sentIntents, deliveryIntents);
        //sms1.sendMultipartTextMessage(phoneNo, null, parts, sentIntents, deliveryIntents);
    }


}
