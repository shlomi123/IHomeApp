package com.ihome.android.ihome;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by User on 20-Jan-18.
 */

class SendMessageToServer extends AsyncTask<String,Void,Void> {
    @Override
    protected Void doInBackground(String... params) {   //params[0] - message, params[1] - server IP
        try{
            Socket soc;
            PrintWriter writer;
            soc = new Socket(params[1], 1618); // open socket and send message
            writer = new PrintWriter(soc.getOutputStream());
            writer.write(params[0]);
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));


            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                response.append(line);


            //ans = reader.readLine();
            if(!response.equals("200"))
            {
                            /*runOnUiThread(new Runnable() {
                                public vo id run() {

                                    Toast.makeText(<your class name>.this, "Cool Ha?", Toast.LENGTH_SHORT).show();
                                }
                            });*/

            }
            writer.close();
            //TODO RECIEVE OK FROM SERVER
            soc.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
