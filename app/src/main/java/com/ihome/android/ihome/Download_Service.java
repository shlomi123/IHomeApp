package com.ihome.android.ihome;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Download_Service extends IntentService {

    public Download_Service() {
        super("Download_Service");
    }


    @Override
    protected void onHandleIntent(final Intent intent) {

        try
        {
            // open socket and send message
            Socket soc = new Socket(getString(R.string.SERVER_IP), 1618);
            DataInputStream reader = new DataInputStream(soc.getInputStream());
            OutputStream output = null;
            String response = "";
            byte[] msg = new byte[1024];
            Boolean end = false;
            int bytesRead;
            //send message to server
            PrintWriter writer = new PrintWriter(soc.getOutputStream());
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            writer.write("105" + "@@" + pref.getString("Username", "error") + "@@" + intent.getDataString());
            writer.flush();

            // receive confirmation
            while (!end)
            {
                bytesRead = reader.read(msg);
                response += new String(msg, 0, bytesRead);
                if (response.equals("200"))
                {
                    end = true;
                }
            }

            //send confirmation
            writer.write("200");
            writer.flush();

            String filename = intent.getDataString();
            int index =  filename.lastIndexOf('.');
            String fileType = filename.substring(index);
            Log.d("file_type_bla", fileType);
            filename = filename.substring(0, index);
            Log.d("file_type_bla", filename);

            //Open file
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
            File folder = new File(root, "IHome");
            folder.mkdir();
            File file = new File(folder, filename + fileType);
            file.createNewFile();
            output = new BufferedOutputStream(new FileOutputStream(file));

            //receive file
            msg = new byte[16*1024];

            while ((bytesRead = reader.read(msg)) > 0)
            {
                output.write(msg, 0, bytesRead);
            }

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Successfully Downloaded File", Toast.LENGTH_SHORT).show();
                }
            });

            output.close();
            reader.close();
            soc.close();
        }
        catch(IOException e){
            Log.d("testing", e.toString());
            e.printStackTrace();
        }
    }
}
