package com.stepahnieqianwang.upalarm;

/**
 * Created by stepahnieqianwang on 4/21/15.
 */
import android.content.Context;
import android.content.res.Resources;
import android.location.Location;

import com.google.android.gms.location.DetectedActivity;

import org.json.JSONObject;

public final class Constants {
    private Constants() {
    }

    public static final String PACKAGE_NAME = "com.google.android.gms.location.activityrecognition";

    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";

    public static final String ACTIVITY_EXTRA = PACKAGE_NAME + ".ACTIVITY_EXTRA";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES";

    public static final String ACTIVITY_UPDATES_REQUESTED_KEY = PACKAGE_NAME +
            ".ACTIVITY_UPDATES_REQUESTED";

    public static final String DETECTED_ACTIVITIES = PACKAGE_NAME + ".DETECTED_ACTIVITIES";

    public static String androidID;

    public static Location lastLoc;

    public static JSONObject RETRIEVED_ALL;

    public static JSONObject SELF_KARMA;
    /**
     * The desired time between activity detections. Larger values result in fewer activity
     * detections while improving battery life. A value of 0 results in activity detections at the
     * fastest possible rate. Getting frequent updates negatively impact battery life and a real
     * app may prefer to request less frequent updates.
     */
    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 30000;//immediately

    /**
     * List of DetectedActivity types that we monitor.
     */
    protected static final int[] MONITORED_ACTIVITIES = {
            DetectedActivity.STILL,
    };

    /**
     * Returns a human readable String corresponding to a detected activity type.
     */
    public static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            default:
                return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
        }
    }
}
