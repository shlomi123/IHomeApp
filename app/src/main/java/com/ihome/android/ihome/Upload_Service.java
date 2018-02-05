package com.ihome.android.ihome;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Upload_Service extends IntentService {

    public Upload_Service() {
        super("DisplayNotification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            Socket soc = new Socket(getString(R.string.SERVER_IP), 1618); // open socket
            PrintWriter writer = new PrintWriter(soc.getOutputStream());
            String fileNames = "";
            String[] parse;
            String ans;
            FileInputStream fileinputstream;
            BufferedInputStream bufferinputstream = null;
            OutputStream outputstream = null;
            DataInputStream reader = new DataInputStream(soc.getInputStream());
            String response = "";
            byte[] msg = new byte[1000];
            Boolean end = false;
            int bytesRead;


            //send filenames
            String[] filePaths = intent.getData().toString().split("&&");
            for (int i = 0; i < filePaths.length; i++)
            {
                filePaths[i] = filePaths[i].replaceAll("%20", " ");
                parse = filePaths[i].split("/");
                fileNames = fileNames + parse[parse.length - 1] + "&&";
                Log.d("testing", filePaths[i]);
            }


            fileNames = fileNames.substring(0, fileNames.length()-2);
            // Send Upload code according to protocol to server
            writer.write("103" + "@@" + fileNames); // works when we send HELLO
            writer.flush();


            //read from socket
            while (!end)
            {
                bytesRead = reader.read(msg);
                response += new String(msg, 0, bytesRead);
                if (response.equals("200"))
                {
                    end = true;
                }
            }

            for (int i = 0; i < filePaths.length; i++)
            {
                // Send file to server
                    // send file

                File file = new File(filePaths[i]);
                byte [] byteArray  = new byte [(int)file.length()];
                fileinputstream = new FileInputStream(file);
                bufferinputstream = new BufferedInputStream(fileinputstream);
                bufferinputstream.read(byteArray,0,byteArray.length);
                outputstream = soc.getOutputStream();
                outputstream.write(byteArray,0,byteArray.length);
                outputstream.flush();

                writer.write("200");    // Add 200 so the server will know we are going to send another file
                writer.flush();
                Log.d("testing", "sent");

                end = false;
                response = "";
                msg = new byte[1000];

                while (!end)    // TODO ADD TIMEOUT
                {
                    bytesRead = reader.read(msg);
                    response += new String(msg, 0, bytesRead);
                    if (response.equals("200"))
                    {
                        end = true;
                    }
                }

                //Log.d("testing", response);


            }

            //read from socket
            end = false;
            response = "";
            msg = new byte[1000];

            while (!end)
            {
                bytesRead = reader.read(msg);
                response += new String(msg, 0, bytesRead);
                if (response.equals("200"))
                {
                    end = true;
                }
            }

            Log.d("testing", response);

            if (bufferinputstream != null) bufferinputstream.close();
            if (outputstream != null) outputstream.close();
            if (soc!=null) soc.close();
        }
        catch(IOException e){
            Log.d("testing", e.toString());
            e.printStackTrace();
        }

    }
}
