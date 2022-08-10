package com.nunmsc.bcisng.nafisa.sec;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Handler;
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
import java.net.Socket;
import java.net.UnknownHostException;

public class DConnect extends AppCompatActivity {
    EditText serverIp,smessage;
    TextView chat;
    String newMesg = "First Client Hello";
    Button connectPhones,sent; String serverIpAddress = "", msg = "", str;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dconnect);
        chat = (TextView) findViewById(R.id.history);
        serverIp = (EditText) findViewById(R.id.server_ip);
        smessage = (EditText) findViewById(R.id.chat);
        sent = (Button) findViewById(R.id.csend);
        //Create an instance of AsyncTask
        ClientAsyncTask clientAST = new ClientAsyncTask();
        //Pass the server ip, port and client message to the AsyncTask
        clientAST.execute(new String[] { "192.168.43.1", "10000",  newMesg });

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Thread sentThread = new Thread(new sentMessage());
                //sentThread.start();

                str = smessage.getText().toString();
                str = str + "\n";
                msg = msg + "p0202 : " + str;
                chat.setText(msg);
                newMesg = smessage.getText().toString();
                //Create an instance of AsyncTask
                ClientAsyncTask clientAST = new ClientAsyncTask();
                //Pass the server ip, port and client message to the AsyncTask
                clientAST.execute(new String[] { "192.168.43.1", "10000", newMesg });
            }
        });

        connectPhones = (Button) findViewById(R.id.conn);
        connectPhones.setOnClickListener(
                new View.OnClickListener(){
            @Override
            public void onClick(View v){
                serverIpAddress = serverIp.getText().toString();
                if (!serverIpAddress.equals("")){
                    //Thread clientThread = new Thread(new ClientThread());
                    //clientThread.start();
                }
            }
        });

/*        chat.setText("p0104: stuation report?\n" +
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


    class sentMessage implements Runnable{
        @Override
        public void run(){
            try{
                InetAddress serverAddr =InetAddress.getByName(serverIpAddress);
                Socket socket = new Socket(serverAddr, 10000); //create client socket
                //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                str = smessage.getText().toString();
                str = str + "\n";
                msg = msg + "Client : " + str;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        chat.setText(msg);
                    }
                });
                os.writeBytes(str);
                os.flush();
                os.close();
                socket.close();
            }catch(IOException e){

            }
        }
    }
//BufferedReader d  = new BufferedReader(new InputStreamReader(client.getInputStream()));
    public class ClientThread implements Runnable{
        public void run(){
            try{
                while(true){
                    InetAddress serverAddr =InetAddress.getByName(serverIpAddress);
                    Socket socket = new Socket(serverAddr, 10000);
                    //create client socket
                    /*******************************************
                     setup i/p streams
                     ******************************************/
                    //DataInputStream in = new DataInputStream(socket.getInputStream());
                    BufferedReader d  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = d.readLine();
                    Toast.makeText(getApplicationContext(), line, Toast.LENGTH_SHORT).show();
                    while (line != null){
                        msg = msg + "Server : " + line + "\n";
                        handler.post(new Runnable(){
                            @Override
                            public void run(){
                                chat.setText(msg);
                            }
                        });
                    }

                    d.close();
                    socket.close();
                    Thread.sleep(100);
                }
            }catch (Exception e){

            }
        }
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
            msg = msg + "p0104 : " + s + "\n";
            chat.setText(msg);
            //tvServerMessage.setText(s);
            Toast.makeText(DConnect.this, "Conncected to p0202!!!" +s, Toast.LENGTH_LONG).show();
        }
    }
}