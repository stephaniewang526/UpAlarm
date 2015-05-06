package com.stepahnieqianwang.upalarm;

/**
 * Created by stepahnieqianwang on 4/21/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DetectedActivitiesAdapter extends ArrayAdapter<DetectedActivity> {
    //Record time
    long totalDuration;

    /** Time when the gesture started. */
    private long startTime = 0;

    public DetectedActivitiesAdapter(Context context,
                                     ArrayList<DetectedActivity> detectedActivities) {
        super(context, 0, detectedActivities);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        DetectedActivity stillActivity = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.detected_activity, parent, false);
        }

        // get time
        long now = System.currentTimeMillis();

        // Find the UI widgets.
        TextView activityName = (TextView) view.findViewById(R.id.detected_activity_name);
        TextView activityPercentage = (TextView) view.findViewById(R.id.detected_activity_confidence_level);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.detected_activity_progress_bar);

        // Populate widgets with values.
        activityName.setText(Constants.getActivityString(getContext(),
                stillActivity.getType()));

        int still_confidence = stillActivity.getConfidence();
        int num_sitting = 0;
        int num_foot = 0;

        if (still_confidence > 30){

            if (startTime == 0) {
                startTime = now;
            }
            totalDuration = (now - startTime)/1000;
            num_sitting++;
        }else{
            num_foot++;
        }

        int num_sum = num_sitting + num_foot;
        int percentage_sitting = (num_sitting / num_sum) * 100;

        //how likely this activity is correctly detected
        //detectedActivity.getConfidence() + "%"
        if(totalDuration <= 60000){
            activityPercentage.setText(totalDuration + " seconds");
        }else{
            activityPercentage.setText(totalDuration/60 + " mins");
        }
        progressBar.setProgress(percentage_sitting); //detectedActivity.getConfidence()
        return view;
    }

    /**
     * Process list of recently detected activities and updates the list of {@code DetectedActivity}
     * objects backing this adapter.
     *
     * @param detectedActivities the freshly detected activities
     */
    protected void updateActivities(ArrayList<DetectedActivity> detectedActivities) {
        HashMap<Integer, Integer> detectedActivitiesMap = new HashMap<>();
        for (DetectedActivity activity : detectedActivities) {
            detectedActivitiesMap.put(activity.getType(), activity.getConfidence());
        }
        // Every time we detect new activities, we want to reset the confidence level of ALL
        // activities that we monitor. Since we cannot directly change the confidence
        // of a DetectedActivity, we use a temporary list of DetectedActivity objects. If an
        // activity was freshly detected, we use its confidence level. Otherwise, we set the
        // confidence level to zero.
        ArrayList<DetectedActivity> tempList = new ArrayList<DetectedActivity>();
        for (int i = 0; i < Constants.MONITORED_ACTIVITIES.length; i++) {
            int confidence = detectedActivitiesMap.containsKey(Constants.MONITORED_ACTIVITIES[i]) ?
                    detectedActivitiesMap.get(Constants.MONITORED_ACTIVITIES[i]) : 0;

            tempList.add(new DetectedActivity(Constants.MONITORED_ACTIVITIES[i],
                    confidence));
        }

        // Remove all items.
        this.clear();

        // Adding the new list items using {@link Adapter#addAll} notifies attached observers that
        // the underlying data has changed and views reflecting the data should refresh.
        this.addAll(tempList);
    }
}