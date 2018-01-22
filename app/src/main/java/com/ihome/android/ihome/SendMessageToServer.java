package com.ihome.android.ihome;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by User on 20-Jan-18.
 */

class SendMessageToServer extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String... params) {   //params[0] - message, params[1] - server IP
        try{
            Socket soc;
            PrintWriter writer;
            // open socket and send message
            soc = new Socket(params[1], 1618);
            //send message to server
            writer = new PrintWriter(soc.getOutputStream());
            writer.write(params[0]);
            writer.flush();

            //create socket reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            //read from socket
            while ((line = reader.readLine()) != null)
                response.append(line);

            publishProgress(response.toString());

            writer.close();
            soc.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        if(result.equals("200"))
        {
        }
    }

}
