package com.stepahnieqianwang.upalarm;

import android.content.Intent;
import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.widget.Toast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.content.IntentFilter;


public class MainActivity extends Activity implements SensorEventListener{
    private Context mContext;
    SensorManager mSensorEventManager;
    Sensor mSensor;


    private LocationManager locationManager;
    private String provider;

    /** Minimum movement force to consider. */
    private static final int MIN_FORCE = 10;

    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     */
    private static final int MIN_DIRECTION_CHANGE = 3;

    /** Maximum pause between movements. */
    private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;

    long totalDuration;

    /** Time when the gesture started. */
    private long mFirstDirectionChangeTime = 0;

    /** Time when the last movement started. */
    private long mLastDirectionChangeTime;

    /** How many movements are considered so far. */
    private int mDirectionChangeCount = 0;

    /** The last x position. */
    private float lastX = 0;

    /** The last y position. */
    private float lastY = 0;

    /** The last z position. */
    private float lastZ = 0;


    // BroadcastReceiver for handling ACTION_SCREEN_OFF.
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // Unregisters the listener and registers it again.
                mSensorEventManager.unregisterListener(MainActivity.this);
                mSensorEventManager.registerListener(
                        MainActivity.this, mSensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for layout
        TextView title,tv;
        Button button_history;
        Button button_actkarma;
        Button button_community;
        Button btnStartService = (Button)findViewById(R.id.btnStartService);
        Button btnEndService = (Button)findViewById(R.id.btnEndService);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), MyService.class));
                Log.v("shake service startup","registering for shake");
                mContext = getApplicationContext();
                // Obtain a reference to system-wide sensor event manager.
                mSensorEventManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
                // Get the default sensor for accel
                mSensor = mSensorEventManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                // Register for events.
                mSensorEventManager.registerListener(MainActivity.this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

                // Register our receiver for the ACTION_SCREEN_OFF action. This will make our receiver
                // code be called whenever the phone enters standby mode.
                IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                registerReceiver(mReceiver, filter);
            }
        });

        btnEndService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MyService.class));
                // Unregister our receiver.
                unregisterReceiver(mReceiver);
                // Unregister from SensorManager.
                mSensorEventManager.unregisterListener(MainActivity.this);
            }
        });

        //go to history page
        button_history = (Button)findViewById(R.id.button_history);

        button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, History.class));
            }
        });

        //go to actkarma page
        button_actkarma = (Button)findViewById(R.id.button_actkarma);

        button_actkarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Actkarma.class));
            }
        });

        //go to community page
        button_community = (Button)findViewById(R.id.button_community);

        button_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Community.class));
            }
        });

        // gps; GPS must be enabled on phone already. Will put in code to check later
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*
        // default criteria for best provider for now
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);


        if (location != null) {
            int lat = (int) (location.getLatitude());
            int lng = (int) (location.getLongitude());
            String msg = "Latitude: " + String.valueOf(lat) + ", Longitude: " + String.valueOf(lng);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No location data", Toast.LENGTH_LONG).show();
        }
        */
    };

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do nothing
    }

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        Log.v("sensor","sensor change is verifying");

        //get accelerometer data
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // calculate movement
        float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);

        if (totalMovement > MIN_FORCE) {

            // get time
            long now = System.currentTimeMillis();

            // store first movement time
            if (mFirstDirectionChangeTime == 0) {
                mFirstDirectionChangeTime = now;
                mLastDirectionChangeTime = now;
            }

                            }
    }

    // test GPS to make sure we can extract geolocation
    public void testGPS(View view){
        // default criteria for best provider for now
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            float lat = (float) (location.getLatitude());
            float lng = (float) (location.getLongitude());
            String msg = "Latitude: " + String.valueOf(lat) + "\r\nLongitude: " + String.valueOf(lng);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No location data", Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(this, "Filler", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}