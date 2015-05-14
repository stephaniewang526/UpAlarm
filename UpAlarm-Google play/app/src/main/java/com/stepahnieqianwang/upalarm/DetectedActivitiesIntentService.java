package com.stepahnieqianwang.upalarm;

/**
 * Created by stepahnieqianwang on 4/21/15.
 */
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.content.Context;
import android.view.View;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class DetectedActivitiesIntentService extends IntentService {
    protected static final String TAG = "activity-detection-intent-service";
    protected static ArrayList<DetectedActivity> detectedActivities = new ArrayList<DetectedActivity>();
    private String androidID;
    private String lat;
    private String lng;

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
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        detectedActivities = (ArrayList) result.getProbableActivities();

        if (intent.getExtras() != null) {
            androidID = intent.getStringExtra("androidID");
            lat = intent.getStringExtra("lat");
            lng = intent.getStringExtra("lng");
        }

        // Log each activity.
        Log.i(TAG, "activities detected");
        for (DetectedActivity da: detectedActivities) {
            Log.i(TAG, Constants.getActivityString(getApplicationContext(),da.getType()) + " " + da.getConfidence() + "%");
        }

        // Broadcast the list of detected activities.
        localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        // TODO: access view somehow??
        Context mContext = getApplicationContext();
        View rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        new PostDataAsyncTask(mContext, rootView).execute();
    }
}