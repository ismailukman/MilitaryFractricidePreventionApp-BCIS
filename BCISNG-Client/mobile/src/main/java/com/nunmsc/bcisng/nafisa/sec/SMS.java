package com.nunmsc.bcisng.nafisa.sec;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SMS extends AppCompatActivity {

    Button buttonSend;
    EditText textSMS;
    Spinner fon;
    String No, sm, sms, fone;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        buttonSend = (Button) findViewById(R.id.sms);
        fon = (Spinner) findViewById(R.id.platoon);
        textSMS = (EditText) findViewById(R.id.editmsg);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }*/

            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sm = textSMS.getText().toString();
                    sms = "BCISNG Message ID:p0101 "+sm;

                    fone = String.valueOf(fon.getSelectedItem());

                    if( sm.equals("") || fone.equals("Select")  ){
                        Toast.makeText(getApplicationContext(), "Please enter a message or a phone number!",
                                Toast.LENGTH_SHORT).show();
                    }else{

                        if (fone.equals("p0104") ){
                            fone = "08136605722";
                            sendSMS( fone, sms);
                        }else if (fone.equals("p0211") ) {
                            fone = "08174111113";
                            sendSMS( fone, sms);
                        }else if(fone.equals("p0211")) {
                            fone = "08065868766";
                            sendSMS( fone, sms);
                        }else if(fone.equals("p0308")) {
                            fone = "5554";
                            sendSMS( fone, sms);
                        }else if(fone.equals("p0501")) {
                            fone = "5556";
                            sendSMS( fone, sms);
                        }
                        //sendSMS(fone, sm);
                    }


/*                    //.................populate a spinner............
                    final Spinner spinner = (Spinner) findViewById(R.id.platoon);
                    //Create an ArrayAdapter by using a string array and a default spinner layout
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SMS.this, R.array.Platoons, android.R.layout.simple_spinner_item);
                    //Specify the layout to use when the list of choices appear
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Now apply the adapter to spinner
                    spinner.setAdapter(adapter);*/

                    //............spinner item selected listener........
/*                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            //We are here that means an item was selected
                            //Now we retrieve the user selection
                            //Get the selected item text
                            selectedItemText = parent.getItemAtPosition(pos).toString();
                            Toast toastSpinnerSelection = Toast.makeText(getApplicationContext(), selectedItemText, Toast.LENGTH_SHORT);
                            //display the toast notification on user interface
                            //set the toast display location
                            toastSpinnerSelection.setGravity(Gravity.LEFT | Gravity.BOTTOM, 20, 150);
                            toastSpinnerSelection.show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            //Another interface callback
                        }

                    });*/



                }
            });



        }

 /*       void  SendSMS(String pNo,String sms){
             String phoneNo = pNo ;


            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                Toast.makeText(getApplicationContext(), "SMS Sent!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }*/

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
        //PendingIntent pi = PendingIntent.getActivity(this, 0,
        // new Intent(this, Main.class), 0);
        //SmsManager sms = SmsManager.getDefault();
        // sms.sendTextMessage(phoneNo, null, message, null, null);

        //PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        //PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

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
    }


    }
