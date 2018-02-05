package com.ihome.android.ihome;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LogIn extends AppCompatActivity {
    EditText userName;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //if "next" button is clicked
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
                    String message = getString(R.string.CODE_LOGIN) + getString(R.string.PARAM_DIVIDE) + userName.getText().toString() + getString(R.string.PARAM_DIVIDE) + pass.getText().toString();

                    test m = new test();
                    m.execute(message, getString(R.string.SERVER_IP));
                }

                //TODO ONLY AFTER WE GET OK FROM SERVER CHANGE ACTIVITY, MAYBE SHOW LOADING

            }

        });

        //if "register" is clicked
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


    //ansyncTask for sending message
    private class test extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {   //params[0] - message, params[1] - server IP
            try{
                Socket soc;
                PrintWriter writer;
                // open socket and send message
                soc = new Socket(params[1], 1618);
                //send message to server
                writer = new PrintWriter(soc.getOutputStream());
                writer.write(params[0]);
                writer.flush();

                //create socket reader
                BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                //read from socket
                while ((line = reader.readLine()) != null)  //TODO REPLACE
                    response.append(line);

                writer.close();
                soc.close();

                return response.toString();

            }catch(IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result != null)
            {
                if(result.equals("200"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "LogIn Successful", Toast.LENGTH_LONG);
                    toast.show();

                    Intent myIntent = new Intent(LogIn.this,
                            MainPage.class);
                    startActivity(myIntent);
                }
                else if (result.equals("1010"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }
}

