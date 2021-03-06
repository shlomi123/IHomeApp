package com.ihome.android.ihome;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Download extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        asncTask m = new asncTask();
        m.execute(getString(R.string.SERVER_IP));
    }

    private class asncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try
            {
                String response = "";
                byte[] msg;
                Boolean end;
                int bytesRead;
                // open socket and send message
                Socket soc = new Socket(params[0], 1618);
                PrintWriter writer;
                //send message to server
                writer = new PrintWriter(soc.getOutputStream());
                writer.write("104");
                writer.flush();

                DataInputStream reader = new DataInputStream(soc.getInputStream());

                end = false;
                response = "";
                msg = new byte[1000];
                {
                    bytesRead = reader.read(msg);
                    response += new String(msg, 0, bytesRead);
                    if (response.substring(0, 3).equals("200"))
                    {
                        end = true;
                    }
                }
                if (!response.equals("200"))
                {
                    response = response.substring(5);
                }

                //Log.d("testing", response);

                soc.close();
                return response;
            }
            catch(IOException e){
                Log.d("testing", e.toString());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            final String[] names = result.split("&&");
            //Log.d("testing", names[0]);
            if (!names[0].equals("200")) // If no files on server
            {
                LinearLayout layout = (LinearLayout) findViewById(R.id.downloadLayout);
                for (int i = 0; i < names.length; i++) {
                    final Button myButton = new Button(Download.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0,20);
                    myButton.setLayoutParams(params);
                    myButton.setText(names[i]);
                    myButton.setId(i);
                    myButton.setBackgroundResource(R.drawable.log_text_view);
                    layout.addView(myButton);
                    final String name = myButton.getText().toString();

                    myButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Intent upload_service= new Intent(getApplicationContext(), Download_Service.class);
                            upload_service.setData(Uri.parse(name));
                            getApplicationContext().startService(upload_service);
                            finish();
                        }
                    });
                }
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "No Files To Download", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        }

    }
}
