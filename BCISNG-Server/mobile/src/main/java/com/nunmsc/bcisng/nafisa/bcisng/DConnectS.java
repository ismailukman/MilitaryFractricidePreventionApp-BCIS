package com.nunmsc.bcisng.nafisa.bcisng;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

public class DConnectS extends AppCompatActivity {
    Button button_sent;
    EditText smessage;
    TextView chat,display_status;
    String str,msg="", Ip4;
    int serverport = 10000;
    ServerSocket serverSocket;
    Socket client;Handler handler = new Handler();
    WifiManager wmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dconnect_s);
        //wmanager = (WifiManager) getApplicationContext(WIFI_SERVICE);
        wmanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String iIPv4 =Formatter.formatIpAddress(wmanager.getConnectionInfo().getIpAddress());
        //iIPv4 = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        smessage = (EditText) findViewById(R.id.chatS);
        chat = (TextView) findViewById(R.id.historyS);
        display_status = (TextView)findViewById(R.id.ip);
        //display_status.setText("Server hosted on " + iIPv4);
        getDeviceIpAddress();
        //Thread serverThread = new Thread(new serverThread());
        //serverThread.start();

        button_sent = (Button) findViewById(R.id.sndS);

        button_sent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Thread sentThread = new Thread(new sentMessage());
                //sentThread.start();

                str = smessage.getText().toString();
                MessageH.strM = str;
                try {
                    //Create a server socket object and bind it to a port
                    ServerSocket socServer = new ServerSocket(serverport);
                    //Create server side client socket reference
                    Socket socClient = null;
                    //Infinite loop will listen for client requests to connect
                    while (true) {
                        //Accept the client connection and hand over communication to server side client socket
                        socClient = socServer.accept();

                        str = smessage.getText().toString();
                        msg = msg + "\n p0104 : " + str;
                        chat.setText(msg);
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
            }
        });

        //New thread to listen to incoming connections
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //Create a server socket object and bind it to a port
                    ServerSocket socServer = new ServerSocket(serverport);
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
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    class sentMessage implements Runnable{
        @Override
        public void run(){
            try{
                Socket client = serverSocket.accept();
                DataOutputStream os = new DataOutputStream(client.getOutputStream());
                str = smessage.getText().toString();
                msg = msg + "\n Server : " + str;

                handler.post(new Runnable(){
                    @Override
                    public void run(){
                        chat.setText(msg);}});
                os.writeBytes(str);
                os.flush();
                os.close();
                client.close();
            }catch(IOException e){

            }
        }
    }

    //DataInputStream d = new DataInputStream(in);
    //BufferedReader d  = new BufferedReader(new InputStreamReader(in));
    public class serverThread implements Runnable{
        @Override
        public void run(){
            try{
                while(true)	{

                    serverSocket = new ServerSocket(serverport);
                    Socket client = serverSocket.accept();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            display_status.setText("Connected");
                        }
                    });

                    /*******************************************
                     setup i/p streams
                     ******************************************/
                    //DataInputStream in = new DataInputStream(client.getInputStream());
                    BufferedReader d  = new BufferedReader(new InputStreamReader(client.getInputStream()) );
                    String line = d.readLine() ;
                    //Toast.makeText(getApplicationContext(), line, Toast.LENGTH_SHORT).show();
                    while(line != null){
                        msg = msg + "\n Client : " + line;
                        handler.post(new Runnable(){
                            @Override
                            public void run(){
                                chat.setText(msg);
                            }
                        });
                    }
                    d.close();
                    client.close();
                    Thread.sleep(100);
                }
            }catch (Exception e){

            }
        }
    }

    public void getDeviceIpAddress() {
        try {
            //Loop through all the network interface devices
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements();) {
                NetworkInterface networkInterface = enumeration.nextElement();
                //Loop through all the ip addresses of the network interface devices
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface.getInetAddresses(); enumerationIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumerationIpAddr.nextElement();
                    //Filter out loopback address and other irrelevant ip addresses
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
                        //Print the device ip address in to the text view

                        Ip4 = inetAddress.getHostAddress();
                        //tvServerIP.setText(inetAddress.getHostAddress());
                        display_status.setText("p0104 hosted on " + Ip4);
/*                        chat.setText("p0104: stuation report?\n" +
                                "p0202: 1 injured, taking over\n" +
                                "p0104: sending backup now\n" +
                                "p0202: all headed south of sambisa\n" +
                                "p0104: roger that\n" +
                                "p0202: where is platoon 3\n" +
                                "p0104: recently update, keeping surveillance\n" +
                                "p0202: roger that\n" +
                                "p0104: contact when u need help\n" +
                                "p0202: roger that, over");*/
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ERROR:", e.toString());
        }
    }


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
            //tvClientMsg.setText(s);
            String str = s;
            msg = msg + "\n p202 : " + str;
            chat.setText(msg);
        }
    }


}