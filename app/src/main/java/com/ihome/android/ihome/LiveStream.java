package com.ihome.android.ihome;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LiveStream extends AppCompatActivity {
    private WebView webView;
    Socket soc;
    PrintWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);

        test m = new test();
        m.execute("107", getString(R.string.SERVER_IP));
    }

    /*@Override
    protected void onDestroy()
    {
        end m = new end();
        m.execute("300", getString(R.string.SERVER_IP));
        super.onDestroy();
    }*/


    private class test extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {   //params[0] - message, params[1] - server IP
            try {
                // open socket and send message
                soc = new Socket(params[1], 1618);
                //send message to server
                writer = new PrintWriter(soc.getOutputStream());
                writer.write(params[0]);
                writer.flush();


                BufferedReader reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                //read from socket
                while ((line = reader.readLine()) != null)
                    response.append(line);

                writer.close();
                soc.close();

                return response.toString();

            }catch(IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result != null)
            {
                if (result.equals("200"))
                {
                    Log.d("qwertyuiop", "connecting to livestream....");
                    webView = (WebView) findViewById(R.id.webView1);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl("http://" + getString(R.string.SERVER_IP) + ":5000");
                    webView.setWebViewClient(new WebViewClient(){

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url){
                            view.loadUrl(url);
                            return true;
                        }
                    });
                }
                else if (result.equals("1070"))
                {
                    Toast.makeText(getApplicationContext(), "LiveStream can't be used while house is being monitered", Toast.LENGTH_LONG).show();
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




    private class end extends AsyncTask<String,String,String> {


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
            webView = (WebView) findViewById(R.id.webView1);
            webView.destroy();
        }

    }
}
