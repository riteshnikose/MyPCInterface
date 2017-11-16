package com.example.riteshnikose.mypcinterface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button btnCon = (Button) findViewById(R.id.btnCon);
        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText iPBox = (EditText) findViewById(R.id.IpBox);
                String  ip = iPBox.getText().toString();
                EditText portBox = (EditText) findViewById(R.id.portBox);
                String port = portBox.getText().toString();
                if(isValidIP(ip)){
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("IpAddr", ip);
                    intent.putExtra("port", port);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Start.this, "IP is not Vaild", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
    }

    public static boolean isValidIP(String ipAddr){
        // this method will not check that the IP is within range i.e is between 0 to 255.
        Pattern ptn = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher mtch = ptn.matcher(ipAddr);
        return mtch.find();
    }


}
