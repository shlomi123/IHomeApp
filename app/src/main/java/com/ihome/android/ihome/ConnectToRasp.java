package com.ihome.android.ihome;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;

public class ConnectToRasp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_rasp);

        Button button= (Button) findViewById(R.id.Next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String wifiName = getWifiName(getApplicationContext());

                if(wifiName != null) {
                    if (wifiName.compareTo(getString(R.string.wifiSSID)) == 0) {
                        Intent myIntent = new Intent(ConnectToRasp.this,
                                wifiConfig.class);
                        startActivity(myIntent);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Not Connected to iHome", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not Connected to iHome", Toast.LENGTH_LONG);
                    toast.show();
                }*/

                //TODO JUST FOR TESTING --------- DELETE THIS
                Intent myIntent = new Intent(ConnectToRasp.this,
                        wifiConfig.class);
                startActivity(myIntent);
            }
        });
    }

    public String getWifiName(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return null;
    }
}
