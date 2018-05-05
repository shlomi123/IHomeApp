package com.ihome.android.ihome;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;

public class wifiConfig extends AppCompatActivity {
    private BluetoothSocket socket;
    private OutputStreamWriter os;
    private DataInputStream is;
    private BluetoothDevice remoteDevice;
    private String message;


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

                message = name.getText().toString() + "@@" + pass.getText().toString(); // get WIFI name and password

                test m = new test();
                m.execute();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socket != null){
            try{
                is.close();
                os.close();
                socket.close();
            }catch(Exception e){}
        }
    }

    //ansyncTask for sending message
    private class test extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {   //params[0] - message, params[1] - server IP
            try{
                byte[] msg = new byte[1024];
                String result = "";
                int bytesRead;
                boolean flag = true;

                BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
                if (blueAdapter != null)
                {
                    if (blueAdapter.isEnabled())
                    {
                        Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                        if (bondedDevices.size() > 0)
                        {
                            for (BluetoothDevice device : bondedDevices)
                            {
                                if (device.getName().equals("raspberrypi"))
                                {
                                    Log.d("testing", device.getName());
                                    ParcelUuid[] uuids = device.getUuids();
                                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                                    socket.connect();
                                    os = new OutputStreamWriter(socket.getOutputStream());
                                    is = new DataInputStream(socket.getInputStream());

                                    os.write(message);
                                    os.flush();

                                    while (flag)
                                    {
                                        bytesRead = is.read(msg);
                                        result += new String(msg, 0, bytesRead);
                                        if (result.equals("200") || result.equals("100"))
                                        {
                                            flag = false;
                                        }
                                    }
                                    Log.d("testing", result);
                                }
                                else
                                {
                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                            Toast.makeText(wifiConfig.this, "Wrong bluetooth", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }

                        Log.d("testing", "No appropriate paired devices.");
                    } else {
                        Log.d("testing", "Bluetooth is disabled.");
                    }
                }
                return result;

            }catch(IOException e){
                Log.d("testing", "Read: " + e.toString());
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
                    Toast toast = Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG);
                    toast.show();

                    Intent myIntent = new Intent(wifiConfig.this,
                            LogIn.class);
                    startActivity(myIntent);
                }
                else if (result.equals("100"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong Details", Toast.LENGTH_LONG);
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


