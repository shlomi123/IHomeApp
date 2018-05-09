package com.ihome.android.ihome;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Motion_Detector extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private StringBuilder response = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion__detector);
        button = (Button) findViewById(R.id.btnActivate);
        button.setVisibility(View.GONE);
        textView = (TextView) findViewById(R.id.MotionDetectorStatus);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (response.toString().equals("1090"))
                {
                    task2 m = new task2();
                    m.execute("110", getString(R.string.SERVER_IP));
                }
                if (response.toString().equals("1091"))
                {
                    task2 m = new task2();
                    m.execute("111", getString(R.string.SERVER_IP));
                }
            }
        });

        task1 m = new task1();
        m.execute("109", getString(R.string.SERVER_IP));
    }

    /*@Override
    protected void onDestroy()
    {
        end m = new end();
        m.execute("300", getString(R.string.SERVER_IP));
        super.onDestroy();
    }*/

    private class task1 extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {   //params[0] - message, params[1] - server IP
            try {
                Socket soc;
                PrintWriter writer;
                BufferedReader reader;
                // open socket and send message
                soc = new Socket(params[1], 1618);
                //send message to server
                writer = new PrintWriter(soc.getOutputStream());
                writer.write(params[0]);
                writer.flush();


                reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                String line;
                //read from socket
                line = reader.readLine();
                response.append(line);


                writer.close();
                soc.close();
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
            {
                if (result.equals("1090"))
                {
                    textView.setText("Status: Not-Active");
                    button.setVisibility(View.VISIBLE);
                    button.setText("Activate");
                }
                else if (result.equals("1091"))
                {
                    textView.setText("Status: Active");
                    button.setVisibility(View.VISIBLE);
                    button.setText("Deactivate");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }



    private class task2 extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {   //params[0] - message, params[1] - server IP
            try {
                Socket soc;
                PrintWriter writer;
                BufferedReader reader;
                // open socket and send message
                soc = new Socket(params[1], 1618);
                //send message to server
                writer = new PrintWriter(soc.getOutputStream());
                writer.write(params[0]);
                writer.flush();


                reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                StringBuilder res = new StringBuilder();
                String line;
                //read from socket
                Log.d("qwertyuiop", "receiving");
                while ((line = reader.readLine()) != null)
                    res.append(line);

                Log.d("qwertyuiop", res.toString());

                writer.close();
                soc.close();

                return res.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
            {
                if (result.equals("200") && response.toString().equals("1090"))
                {
                    textView.setText("Status: Active");
                    button.setVisibility(View.VISIBLE);
                    button.setText("Deactivate");
                    response = new StringBuilder("1091");
                }
                else if (result.equals("200") && response.toString().equals("1091"))
                {
                    textView.setText("Status: Not-Active");
                    button.setVisibility(View.VISIBLE);
                    button.setText("Activate");
                    response = new StringBuilder("1090");
                }
                else if (result.equals("1100"))
                {
                    Toast.makeText(getApplicationContext(), "You cannot activate motion detection when live streaming is on", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
    }


    /*private class end extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... params) {   //params[0] - message, params[1] - server IP
            try{
                // open socket and send message
                writer.write(params[0]);
                writer.flush();

                writer.close();
                soc.close();
                return null;

            }catch(IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {

        }

    }*/
}
