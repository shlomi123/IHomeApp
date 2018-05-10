package com.ihome.android.ihome;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Register extends AppCompatActivity {
    EditText userName;
    EditText pass;
    EditText passCheck;
    EditText codeEdit;
    EditText email;
    //TODO check register
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText) findViewById(R.id.NameEdit);
        pass = (EditText) findViewById(R.id.PasswordEdit);
        passCheck = (EditText) findViewById(R.id.PasswordCheckEdit);
        codeEdit = (EditText) findViewById(R.id.CodeEdit);
        email = (EditText) findViewById(R.id.EmailEdit);

        Button next = (Button) findViewById(R.id.btnNext);  // When next button is pressed
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOk = true;

                //check that text boxes aren't empty
                if ( TextUtils.isEmpty(userName.getText())){
                    userName.setError( "Username is required!");
                    isOk = false;
                }

                if ( TextUtils.isEmpty(pass.getText())){
                    pass.setError( "Password is required!");
                    isOk = false;
                }

                if ( TextUtils.isEmpty(passCheck.getText())){
                    passCheck.setError( "Password is required!");
                    isOk = false;
                }

                if ( TextUtils.isEmpty(codeEdit.getText())){
                    codeEdit.setError( "Code is required!");
                    isOk = false;
                }

                if (!pass.getText().toString().equals(passCheck.getText().toString()))
                {
                    isOk = false;
                }


                if (isOk)       // Build the register message and sent it to server
                {
                    String message = getString(R.string.CODE_REGISTER) + getString(R.string.PARAM_DIVIDE) + userName.getText().toString() + getString(R.string.PARAM_DIVIDE) + pass.getText().toString() + getString(R.string.PARAM_DIVIDE) + codeEdit.getText().toString() + getString(R.string.PARAM_DIVIDE) + email.getText().toString();

                    test m = new test();
                    m.execute(message, getString(R.string.SERVER_IP));
                }

            }
        });
    }


    //ansyncTask for sending message
    private class test extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {   //params[0] - message, params[1] - server IP
            try{
                Log.d("qwertyuiop", params[0]);
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
                while ((line = reader.readLine()) != null)
                    response.append(line);

                Log.d("qwertyuiop", response.toString());

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
                    Toast toast = Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_LONG);
                    toast.show();

                    finish();
                }
                else if (result.equals("1020"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong Code", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(result.equals("1021"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Username Already Exists", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(result.equals("1022"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong Email", Toast.LENGTH_LONG);
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