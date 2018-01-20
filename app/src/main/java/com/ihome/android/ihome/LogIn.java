package com.ihome.android.ihome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogIn extends AppCompatActivity {
    EditText userName;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button next = (Button) findViewById(R.id.btnNext);  // When next button is pressed
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = (EditText) findViewById(R.id.NameEdit);
                pass = (EditText) findViewById(R.id.PasswordEdit);
                //TODO VALIDATE INFORMATION WITH RASPBERRY PI

                boolean isOk = true;

                if( TextUtils.isEmpty(userName.getText())){
                    userName.setError( "Username is required!");
                    isOk = false;
                }

                if( TextUtils.isEmpty(pass.getText())){
                    pass.setError( "Password is required!");
                    isOk = false;
                }

                if (isOk)       // Build the login message and sent it to server
                {
                    String message = getString(R.string.CODE_LOGIN) + getString(R.string.PARAM_DIVIDE)
                            + userName.getText().toString() + getString(R.string.PARAM_DIVIDE) + pass.getText().toString();

                    SendMessageToServer m = new SendMessageToServer();
                    m.execute(message, getString(R.string.SERVER_IP));
                }

                //TODO ONLY AFTER WE GET OK FROM SERVER CHANGE ACTIVITY, MAYBE SHOW LOADING

                if (isOk)
                {
                    Intent myIntent = new Intent(LogIn.this,
                            MainPage.class);
                    startActivity(myIntent);
                }

            }

        });

        Button register = (Button) findViewById(R.id.btnRegister);  // When register button is pressed
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LogIn.this,
                        Register.class);
                startActivity(myIntent);
            }

        });
    }
}

