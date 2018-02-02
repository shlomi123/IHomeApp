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


            //send filenames
            String[] filePaths = intent.getData().toString().split("&&");
            for (int i = 0; i < filePaths.length; i++)
            {
                parse = filePaths[i].split("/");
                fileNames = fileNames + parse[parse.length - 1] + "&&";
            }


            fileNames = fileNames.substring(0, fileNames.length()-2);
            // Send Upload code according to protocol to server
            writer.write("103" + "@@" + fileNames); // works when we send HELLO
            writer.flush();


            //read from socket
            BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);

            Log.d("testing", response.toString());


            /*File file = new File(filePaths[0]);
            byte [] byteArray  = new byte [(int)file.length()];
            fileinputstream = new FileInputStream(file);
            bufferinputstream = new BufferedInputStream(fileinputstream);
            bufferinputstream.read(byteArray,0,byteArray.length);
            outputstream = soc.getOutputStream();
            outputstream.write(byteArray,0,byteArray.length);
            outputstream.flush();

            //read from socket
            while ((line = reader.readLine()) != null)
                response.append(line);

            Log.d("testing", response.toString());*/

            //check if server approves
            /*if(!response.toString().equals("200200"))
            {
                Handler handler = new Handler(Looper.getMainLooper());//this is how to do "Toast" in service

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Upload_Service.this.getApplicationContext(),"Server Didn't Received File Names",Toast.LENGTH_SHORT).show();
                    }
                });
                writer.close();
            }
            else
            {

                for (int i = 0; i < filePaths.length; i++)
                {
                    // Send file to server
                    try {
                        // send file

                        File file = new File(filePaths[i]);
                        byte [] byteArray  = new byte [(int)file.length()];
                        fileinputstream = new FileInputStream(file);
                        bufferinputstream = new BufferedInputStream(fileinputstream);
                        bufferinputstream.read(byteArray,0,byteArray.length);
                        outputstream = soc.getOutputStream();
                        outputstream.write(byteArray,0,byteArray.length);
                        outputstream.flush();

                        Log.d("testing", "sent");
                    }
                    finally {
                        if (bufferinputstream != null) bufferinputstream.close();
                        if (outputstream != null) outputstream.close();
                        if (soc!=null) soc.close();
                    }
                }
            }*/



        }catch(IOException e){
            Log.d("testing", e.toString());
            e.printStackTrace();
        }
    }
}
/*Handler handler = new Handler(Looper.getMainLooper());

handler.poutputstreamt(new Runnable() {

@Override
public void run() {
        Toast.makeText(Upload_Service.this.getApplicationContext(), "debug",Toast.LENGTH_SHORT).show();
        }
        });*/
