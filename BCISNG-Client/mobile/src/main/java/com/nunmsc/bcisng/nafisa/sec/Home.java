package com.nunmsc.bcisng.nafisa.sec;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Home extends AppCompatActivity {
    String newMesg = "First Client Hello";
     Button sms, gps, connect,panic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sms = (Button) findViewById(R.id.sms);
        gps = (Button) findViewById(R.id.geo);
        connect = (Button) findViewById(R.id.connect);
        panic = (Button) findViewById(R.id.panic);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Home.this, SMS.class);
                startActivity(i);
            }
        });

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Home.this, GEO.class);
                startActivity(i);
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Home.this, DConnect.class);
                startActivity(i);
            }
        });

        //Create an instance of AsyncTask
        ClientAsyncTask clientAST = new ClientAsyncTask();
        //Pass the server ip, port and client message to the AsyncTask
        clientAST.execute(new String[] { "192.168.43.1", "8080",  newMesg });

        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //newMesg = editServer.getText().toString();
                String newMesg = ""+GEO.zLati +", "+GEO.zLongi;
                Toast.makeText(Home.this, newMesg, Toast.LENGTH_LONG).show();

                //Create an instance of AsyncTask
                ClientAsyncTask clientAST = new ClientAsyncTask();
                //Pass the server ip, port and client message to the AsyncTask
                clientAST.execute(new String[] { "192.168.43.1", "8080", newMesg });
            }
        });

    }

    class ClientAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                //Create a client socket and define internet address and the port of the server
                Socket socket = new Socket(params[0], Integer.parseInt(params[1]));
                //Get the input stream of the client socket
                InputStream is = socket.getInputStream();
                //Get the output stream of the client socket
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                //Write data to the output stream of the client socket
                out.println(params[2]);
                //Buffer the data coming from the input stream
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                //Read data in the input buffer
                result = br.readLine();
                //Close the client socket
                socket.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            //Write server message to the text view
            //tvServerMessage.setText(s);
            //String msg = GEO.zLati +" "+GEO.zLongi;
            //Toast.makeText(Home.this, "Server is Watching!!!" +s, Toast.LENGTH_LONG).show();
        }
    }


}
