package com.nunmsc.bcisng.nafisa.bcisng;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nunmsc.bcisng.nafisa.bcisng.adapter.MyArrayAdapter;

public class Help extends ListActivity {

    static final String[] Modules =
            new String[] { "DConnect", "Broadcast", "GPS Location", "SMS", "Platoon", "Panic Button"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new MyArrayAdapter(this, Modules));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
        MyAlertDialog(selectedValue);
    }

    public void MyAlertDialog(String mes){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Help.this);

        // Setting Dialog Title
        alertDialog.setTitle("Help Instruction");

        // Setting Dialog Message
        //"DConnect", "Broadcast", "GPS Location", "SMS", "Platoon"
        String body;
        if(mes.equals("DConnect")){
            body="Direct Communication with other platoon members using Wi-fi Network";
        }else if(mes.equals("Broadcast")){
            body="Send a single message to all platoon members";
        }else if(mes.equals("GPS Location")){
            body="Retrieve your GPS location at a particlar time";
        }else if(mes.equals("SMS")){
            body="Secure SMS message service to other members ";
        }else if(mes.equals("Platoon")){
            body="View platoon record";
        }else {
            body="Sent a quick help message when in danger";
        }

        alertDialog.setMessage(mes+ "\n"+body);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.idea);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                // Write your code here to invoke YES event
                //Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();
            }
        });

/*		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to invoke NO event
				Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});*/

        // Showing Alert Message
        alertDialog.show();
    }

}