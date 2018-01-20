package com.ihome.android.ihome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText userName;
    EditText pass;
    EditText codeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText) findViewById(R.id.NameEdit);
        pass = (EditText) findViewById(R.id.PasswordEdit);
        codeEdit = (EditText) findViewById(R.id.CodeEdit);

        Button next = (Button) findViewById(R.id.btnNext);  // When next button is pressed
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOk = true;
                if( TextUtils.isEmpty(userName.getText())){
                    userName.setError( "Username is required!");
                    isOk = false;
                }

                if( TextUtils.isEmpty(pass.getText())){
                    pass.setError( "Password is required!");
                    isOk = false;
                }

                if( TextUtils.isEmpty(codeEdit.getText())){
                    codeEdit.setError( "Code is required!");
                    isOk = false;
                }

                String code = codeEdit.getText().toString();

                if (code.compareTo(getString(R.string.code)) != 0)  // If the code doesn't match the registration code
                {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_wrongCode), Toast.LENGTH_LONG);
                    toast.show();
                    isOk = false;
                }

                //TODO  SEND INFO TO RASPBERRY PI
                if (isOk)       // Build the register message and sent it to server
                {
                    String message = getString(R.string.CODE_REGISTER) + getString(R.string.PARAM_DIVIDE)
                            + userName.getText().toString() + getString(R.string.PARAM_DIVIDE) + pass.getText().toString()
                            + getString(R.string.PARAM_DIVIDE) + codeEdit.getText().toString();

                    SendMessageToServer m = new SendMessageToServer();
                    m.execute(message, getString(R.string.SERVER_IP));
                }

                //TODO ONLY AFTER WE GET OK FROM SERVER CHANGE ACTIVITY, MAYBE SHOW LOADING

                if (isOk)
                {
                    Intent myIntent = new Intent(Register.this,
                            LogIn.class);
                    startActivity(myIntent);
                }
            }
            });
        ;
    }
}