package com.ihome.android.ihome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Logs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        test m = new test();
        m.execute("106", getString(R.string.SERVER_IP));

    }

    //ansyncTask for sending message
    private class test extends AsyncTask<String,String,String> {

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
            LinearLayout ll = (LinearLayout) findViewById(R.id.logsLayout);

            List<String> events = new ArrayList<String>(Arrays.asList(result.split("&&")));

            for (String i : events)
            {
                //[0] - username, [1] - date, [2] - action, [3] - content
                List<String> data = new ArrayList<String>(Arrays.asList(i.split("@@")));

                final TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundResource(R.drawable.log_text_view);

                tv.setText(data.get(2) + "\n" + "Username: " + data.get(0) + "\n" + "Date: " + data.get(1) + "\n" + "Content: " + data.get(3));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,20);
                tv.setLayoutParams(params);
                //tv.setHeight(85);
                tv.setTextSize(20);
                tv.setTextColor(Color.BLACK);
                tv.setPadding(10,0,0,0);
                /*tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int height = tv.getHeight();
                        Log.d("testing", Integer.toString(height));
                        if (height == 85)
                        {
                            Helper.expand(tv, 250, 295);
                        }
                        else
                        {
                            Helper.collapse(tv, 250, 85);
                        }
                    }
                });*/
                ll.addView(tv);
            }
        }

    }
}
