package com.ihome.android.ihome;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
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
            String response = "";
            byte[] msg = new byte[1000];
            Boolean end = false;
            int bytesRead;
            //send message to server
            PrintWriter writer = new PrintWriter(soc.getOutputStream());
            writer.write("105" + "@@" + intent.getDataString());
            writer.flush();

            while (!end)
            {
                bytesRead = reader.read(msg);
                response += new String(msg, 0, bytesRead);
                if (response.equals("200"))
                {
                    end = true;
                }
            }

            writer.write("200");
            writer.flush();

            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
            File gpxfile = new File(root + File.separator + intent.getDataString());
            gpxfile.createNewFile();
            //Path path = Paths.get(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
            OutputStream output = null;
            output = new BufferedOutputStream(new FileOutputStream(gpxfile));
            //FileWriter fileWriter = new FileWriter(gpxfile);

            end = false;
            response = "";
            msg = new byte[4096];

            while (!end)
            {
                bytesRead = reader.read(msg);
                //fileWriter.write(msg.toString());
                //fileWriter.flush();
                output.write(msg);
              //  Files.write(path, aBytes); //creates, overwrites
                /*if (response.substring(response.length() - 4).equals("200"))
                {
                    end = true;
                }*/

            }

            //fileWriter.close();
            Log.d("testing", response);



            soc.close();        //TODO CLOSE SOCKETS EVERYWHERE
        }
        catch(IOException e){
            Log.d("testing", e.toString());
            e.printStackTrace();
        }
    }
}
