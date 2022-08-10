package com.nunmsc.bcisng.nafisa.bcisng;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Home extends AppCompatActivity {
    private TextView clientMsg;
    Button sms, gps, connect, bcast, help, panic, platoon;
    private final int SERVER_PORT = 8080; //Define the server port
    static String str, IP4;
    double mlati, mlongi, zlati, zlongi, m=33.94,n=-118.40, x ;
    //public static final double R = 6356.752; // In kilometers
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sms = (Button) findViewById(R.id.sms);
        gps = (Button) findViewById(R.id.geo);
        connect = (Button) findViewById(R.id.connect);
        bcast = (Button) findViewById(R.id.bcast);
        help = (Button) findViewById(R.id.help);
        panic = (Button) findViewById(R.id.panic);
        platoon = (Button) findViewById(R.id.record);
        clientMsg = (TextView) findViewById(R.id.clientmsg);
        getDeviceIpAddress();
        //str = editClient.getText().toString();
        MessageH.strM = str;
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, SMS.class);
                startActivity(i);
            }
        });

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, GEO.class);
                startActivity(i);
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, DConnectS.class);
                startActivity(i);
            }
        });
        bcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Broadcast.class);
                startActivity(i);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Help.class);
                startActivity(i);
            }
        });
        platoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Platoon.class);
                startActivity(i);
            }
        });

        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //str = editClient.getText().toString();
                str = "p0202 Message";
                MessageH.strM = str;
                try {
                    //Create a server socket object and bind it to a port
                    ServerSocket socServer = new ServerSocket(SERVER_PORT);
                    //Create server side client socket reference
                    Socket socClient = null;
                    //Infinite loop will listen for client requests to connect
                    while (true) {
                        //Accept the client connection and hand over communication to server side client socket
                        socClient = socServer.accept();
                        //For each client new instance of AsyncTask will be created
                        ServerAsyncTask serverAsyncTask = new ServerAsyncTask();
                        //Start the AsyncTask execution
                        //Accepted client socket object will pass as the parameter
                        serverAsyncTask.execute(new Socket[] {socClient});
                        //updateMsg(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MyAlertDialog();

            }
        });


//New thread to listen to incoming connections
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //Create a server socket object and bind it to a port
                    ServerSocket socServer = new ServerSocket(SERVER_PORT);
                    //Create server side client socket reference
                    Socket socClient = null;
                    //Infinite loop will listen for client requests to connect
                    while (true) {
                        //Accept the client connection and hand over communication to server side client socket
                        socClient = socServer.accept();
                        //For each client new instance of AsyncTask will be created
                        ServerAsyncTask serverAsyncTask = new ServerAsyncTask();
                        //Start the AsyncTask execution
                        //Accepted client socket object will pass as the parameter
                        serverAsyncTask.execute(new Socket[]{socClient});
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


     /*  public void updateMsg(String s){
        MessageH.strM = s;
        str1 = MessageH.strM;
    }*/

    /**
     * Get ip address of the device
     */
    public void getDeviceIpAddress() {
        try {
            //Loop through all the network interface devices
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements(); ) {
                NetworkInterface networkInterface = enumeration.nextElement();
                //Loop through all the ip addresses of the network interface devices
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface.getInetAddresses(); enumerationIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumerationIpAddr.nextElement();
                    //Filter out loopback address and other irrelevant ip addresses
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
                        //Print the device ip address in to the text view
                        IP4 = inetAddress.getHostAddress();
                        //tvServerIP.setText(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ERROR:", e.toString());
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * AsyncTask which handles the commiunication with clients
     */
    class ServerAsyncTask extends AsyncTask<Socket, Void, String> {
        //Background task which serve for the client
        @Override
        protected String doInBackground(Socket... params) {
            String result = null;
            //Get the accepted socket object
            Socket mySocket = params[0];
            try {
                //Get the data input stream coming from the client
                InputStream is = mySocket.getInputStream();
                //Get the output stream to the client
                PrintWriter out = new PrintWriter(mySocket.getOutputStream(), true);
                //Write data to the data output stream
                out.println(MessageH.strM);

                //Buffer the data input stream
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                //Read the contents of the data buffer
                result = br.readLine();
                //Close the client connection
                mySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            //After finishing the execution of background task data will be write the text view
            Toast.makeText(Home.this, s, Toast.LENGTH_LONG).show();
            clientMsg.setText(s);
            String str = s;
            //sampleString.split(",");
            String[] parts = str.split(",");
            //Double d = Double.parseDouble(s);
            String s1 = parts[0];
            String s2 = parts[1];
            zlati = Double.parseDouble(s1);
            zlongi = Double.parseDouble(s2);
        }
    }


    public void MyAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        // Setting Dialog Title
        alertDialog.setTitle("Distance Between you and the p0102");
        // Setting Dialog Message
        //"DConnect", "Broadcast", "GPS Location", "SMS", "Platoon"
        //x = MyHaversineFunc(mlati, mlongi, m, n);
        x = MyHaversineFunc(mlati, mlongi, zlati, zlongi);
        alertDialog.setMessage("In Meters: "+x);
        //Double d = Double.parseDouble(s);
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.idea);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                //Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


         public double MyHaversineFunc(double lat1, double lon1, double lat2, double lon2) {
            double R = 6356.752; // In kilometers
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            lat1 = Math.toRadians(lat1);
            lat2 = Math.toRadians(lat2);

            double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) *  Math.cos(lat2);
            double c = 2 * Math.asin(Math.sqrt(a));
            return R * c;
        }
        //sample java main method to
        //MyHaversineFunc(36.12, -86.67, 33.94, -118.40);

}
