package com.ihome.android.ihome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

        ImageButton upload = (ImageButton) findViewById(R.id.btnUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getApplicationContext(), FileChooser.class);
                i2.putExtra(Constants.SELECTION_MODE,Constants.SELECTION_MODES.MULTIPLE_SELECTION.ordinal());
                startActivityForResult(i2,1);

                //TODO UPLOAD SELECTED FILES TO RASPBERRY
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

                SendFileToServer m = new SendFileToServer();    // Build the upload file message
                m.execute(message, getString(R.string.SERVER_IP), getString(R.string.CODE_UPLOAD));

            }
        }
    }
}
