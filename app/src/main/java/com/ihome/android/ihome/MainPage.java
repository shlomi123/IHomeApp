package com.ihome.android.ihome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean flag = preferences.getBoolean("Flag", true);
        if(flag)
        {
            startActivity(new Intent(this, ConnectToRasp.class));
        }*/

        //startActivity(new Intent(this, ConnectToRasp.class)); //TODO testing, erase when done testing

        final ImageButton upload = (ImageButton) findViewById(R.id.btnUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getApplicationContext(), FileChooser.class);
                i2.putExtra(Constants.SELECTION_MODE,Constants.SELECTION_MODES.MULTIPLE_SELECTION.ordinal());
                startActivityForResult(i2,1);
            }
        });

        ImageButton download = (ImageButton) findViewById(R.id.btnDownload);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainPage.this,
                        Download.class);
                startActivity(myIntent);

            }
        });

        ImageButton logs = (ImageButton) findViewById(R.id.btnLogs);
        logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainPage.this,
                        Logs.class);
                startActivity(myIntent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {                 // Gets selected files for upload
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data!=null) {
            if (resultCode == RESULT_OK) {
                ArrayList<Uri> selectedFiles  = data.getParcelableArrayListExtra(Constants.SELECTED_ITEMS);
                String message = "";

                for (int i = 0; i < selectedFiles.size(); i++)
                {
                    message = message + selectedFiles.get(i).toString().substring(7) + "&&";
                }

                message = message.substring(0, message.length()-2);

                Context context = this;
                Intent upload_service= new Intent(context, Upload_Service.class);
                upload_service.setData(Uri.parse(message));
                context.startService(upload_service);
            }
        }
    }
}
