package com.example.riteshnikose.mypcinterface;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnClick;
    public static Socket appClientScoket;
    public static PrintStream appPS;
    public String strCommand = "";
    public static InputStreamReader IR;
    public static BufferedReader BR;
    public static String strResponce = "";
    public static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Connection().execute();

        btnClick = (Button) findViewById(R.id.button);
        btnClick.setOnClickListener(this);




    }

    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            socketCreation();
            return null;
        }

    }


    public void onClick(View v) {

        if (v == btnClick) {

            TextView commandTextView = (TextView) findViewById(R.id.editText2);
            CharSequence st = commandTextView.getText();

            strCommand = st.toString();
            appPS.println(strCommand);

            TextView  myTextView = (TextView) findViewById(R.id.editText);
            myTextView.setText(strResponce);

        }
        return;
    }

    private  void socketCreation() {

        try{
                appClientScoket = new Socket("10.0.2.2", 6000);
                appPS = new PrintStream(appClientScoket.getOutputStream());
                appPS.println("Establish Connection");
                ProcessResponce();
                TextView  myTextView = (TextView) findViewById(R.id.editText);
                myTextView.setText(strResponce);

        } catch (IOException e){
            System.out.println(" Scoket exception " + e.getMessage());
        }

    }

    private String ProcessResponce() {
        String resiveMessage = "";
        try {
            if(IR == null) {
                IR = new InputStreamReader(appClientScoket.getInputStream());
                BR = new BufferedReader(IR);
                resiveMessage = BR.readLine();
                TextView  myTextView = (TextView) findViewById(R.id.editText);
                myTextView.setText(resiveMessage);

            }
            while(true) {
                    resiveMessage = BR.readLine();
                strResponce = strResponce+resiveMessage;
           }

        }
        catch (IOException e) {
            TextView  myTextView = (TextView) findViewById(R.id.editText);
            myTextView.setText("Error" + e.getMessage());
        }


        return resiveMessage;

    }



}
