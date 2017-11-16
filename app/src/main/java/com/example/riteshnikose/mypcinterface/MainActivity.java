package com.example.riteshnikose.mypcinterface;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintStream;
import java.net.Socket;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnClick;
    public static Socket appClientScoket;
    public static PrintStream appPS;
    public String strCommand = "";
    public static String strResponce = "";
    DataInputStream dIn;
    Thread thread;
    Handler handler;
    String ip;
    String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         ip = getIntent().getStringExtra("IpAddr");
         port = getIntent().getStringExtra("port");

        final TextView  myTextView = (TextView) findViewById(R.id.editText);
        btnClick = (Button) findViewById(R.id.button);
        btnClick.setOnClickListener(this);
        thread = new Thread(new Connection());
        thread.start();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                myTextView.setText(strResponce);
            }
        };


    }


    public void onClick(View v) {

        if (v == btnClick) {
            TextView commandTextView = (TextView) findViewById(R.id.editText2);
            CharSequence st = commandTextView.getText();
            strCommand = st.toString();
            strResponce ="";
        }
        return;
    }


    private class Connection  implements Runnable{
        @Override
        public void run() {
            socketCreation();
            while(true) {

                if (strCommand.length() != 0) {
                    Commond();
                    strCommand = "";
                }
                while (ProcessResponce()) {
                    Message mgs = Message.obtain();
                    mgs.arg1 = 1;
                    handler.sendMessage(mgs);

                }
                try {
                    sleep(500);
                }catch(Exception e){
                    System.out.print(e.getMessage());
                }
            }
        }
    }


    private  void socketCreation() {


        try{
                //appClientScoket = new Socket("10.0.2.2", 6000);
                appClientScoket = new Socket(ip, Integer.parseInt(port));
                appPS = new PrintStream(appClientScoket.getOutputStream());
                appPS.println("Establish Connection");
        } catch (IOException e){
            System.out.println(" Scoket exception " + e.getMessage());
        }

    }
    private  void Commond() {
        appPS.println(strCommand);
    }
    private boolean ProcessResponce() {
        try{

            if(dIn == null) {
                dIn = new DataInputStream(appClientScoket.getInputStream());
            }
            if(appClientScoket.getInputStream().available()>0) {
                int length = dIn.readInt();
                if (length > 0) {
                    byte[] message = new byte[length];
                    dIn.readFully(message, 0, message.length);
                    String st = new String(message);

                    strResponce = strResponce + st;
                }
            }
            else{
                return false;
            }
        }
        catch (Exception e) {
            Log.d(" error", "ProcessResponce: "+e.getMessage());
        }
        return true;
    }



}
