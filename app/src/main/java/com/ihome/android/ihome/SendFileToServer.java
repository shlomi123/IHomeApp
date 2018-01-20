package com.ihome.android.ihome;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by User on 20-Jan-18.
 */

public class SendFileToServer extends AsyncTask<String,Void,Void>
{
    @Override
    protected Void doInBackground(String... params) {   // Params[0] - file paths, params[1] - server IP, params[2] - Upload code
        try{
            Socket soc = new Socket(params[1], 1618); // open socket
            PrintWriter writer = new PrintWriter(soc.getOutputStream());
            String fileNames = "";
            String[] parse;
            String ans;

            //send filenames
            String[] filePaths = params[0].split("&&");
            for (int i = 0; i < filePaths.length; i++)
            {
                parse = filePaths[i].split("/");
                fileNames = fileNames + parse[parse.length - 1] + "&&";
            }

            fileNames = fileNames.substring(0, fileNames.length()-2);
            // Send Upload code according to protocol to server
            writer.write(params[2] + "@@" + fileNames); // works when we send HELLO
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            //TODO get approval

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);


            //ans = reader.readLine();
            if(!response.equals("200"))
            {
                            /*runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(<your class name>.this, "Cool Ha?", Toast.LENGTH_SHORT).show();
                                }
                            });*/
            }

            for (int i = 0; i < filePaths.length; i++)
            {
                // Send file to server
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null;

                try {
                    // send file
                    File file = new File(filePaths[i]);
                    byte [] mybytearray  = new byte [(int)file.length()];
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    os = soc.getOutputStream();
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();



                }
                finally {
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (soc!=null) soc.close();
                }

            }
            // ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
            //oos.writeObject(file.getName().getBytes());
            //oos.flush();

            //oos.writeObject(file);
            //oos.flush();
            //TODO RECIEVE OK FROM SERVER
            // soc.close();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
