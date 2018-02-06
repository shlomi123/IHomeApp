package com.ihome.android.ihome;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class wifiConfig extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_config);

        Button next = (Button) findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.NameEdit);

                EditText pass = (EditText) findViewById(R.id.PasswordEdit);

                String msg = name.getText().toString() + ", " + pass.getText().toString(); // get WIFI name and password

                //TODO SEND WIFI DETAILS TO SERVER

                Toast toast = Toast.makeText(getApplicationContext(), "message sent", Toast.LENGTH_LONG);
                toast.show();

                Intent myIntent = new Intent(wifiConfig.this,
                        LogIn.class);
                startActivity(myIntent);
            }
        });
    }
}
