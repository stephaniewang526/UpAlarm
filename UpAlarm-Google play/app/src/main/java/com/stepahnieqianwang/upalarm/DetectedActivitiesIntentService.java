package com.stepahnieqianwang.upalarm;

/**
 * Created by stepahnieqianwang on 4/21/15.
 */
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.content.Context;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class DetectedActivitiesIntentService extends IntentService {
    protected static final String TAG = "activity-detection-intent-service";
    protected static ArrayList<DetectedActivity> detectedActivities = new ArrayList<DetectedActivity>();
    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public DetectedActivitiesIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * Handles incoming intents.
     * @param intent The Intent is provided (inside a PendingIntent) when requestActivityUpdates()
     *               is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        //if (result!=null) {
            Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

            // Get the list of the probable activities associated with the current state of the
            // device. Each activity is associated with a confidence level, which is an int between
            // 0 and 100.
            detectedActivities = (ArrayList) result.getProbableActivities();

            // extract aID and latlng
            Log.i(TAG, "aID is: " + intent.getStringExtra("aID"));
            Log.i(TAG, "latlng is: " + intent.getStringExtra("aID") + ", " + intent.getStringExtra("lng"));

            // Log each activity.
            Log.i(TAG, "activities detected");
            for (DetectedActivity da : detectedActivities) {
                Log.i(TAG, Constants.getActivityString(getApplicationContext(), da.getType()) + " " + da.getConfidence() + "%");
            }

            // Broadcast the list of detected activities.
            localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities);
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
            new PostDataAsyncTask().execute();
        //}
    }
}