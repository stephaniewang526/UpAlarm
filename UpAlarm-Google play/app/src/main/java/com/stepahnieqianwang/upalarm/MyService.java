package com.stepahnieqianwang.upalarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {


    @Override
    public void onCreate() {
        Toast.makeText(this, "Tracking Service is Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this,"Tracking Service is started.", Toast.LENGTH_LONG).show();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        Toast.makeText(this,"Tracking Service is stopped.", Toast.LENGTH_LONG).show();

    }
}
