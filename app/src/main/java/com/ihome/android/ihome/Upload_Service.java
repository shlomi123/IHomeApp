package com.ihome.android.ihome;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;


public class Upload_Service extends IntentService {

    public Upload_Service() {
        super("DisplayNotification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast toast = Toast.makeText(getApplicationContext(), intent.getDataString(), Toast.LENGTH_LONG);
        toast.show();
    }
}
