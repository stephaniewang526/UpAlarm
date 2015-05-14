package com.stepahnieqianwang.upalarm;

import android.os.AsyncTask;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by stepahnieqianwang on 5/6/15.
 */
public class PostDataAsyncTask extends AsyncTask<String, String, String> {
    protected static final String TAG = "Post Data";
    private Context mContext;
    private View rootView;

    private TextView tv_lat;
    private TextView tv_lng;
    private TextView tv_aID;

    public PostDataAsyncTask(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;
        tv_lat = (TextView) rootView.findViewById(R.id.lat);
        tv_lng = (TextView) rootView.findViewById(R.id.lng);
        tv_aID = (TextView) rootView.findViewById(R.id.android_id);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        // do stuff before posting data
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            postText();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String lenghtOfFile) {
        // do stuff after posting data
    }
    // this will post our text data
    private void postText(){
        // get time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        String currentTime = dateFormat.format(date);

        //determine userID
        int userID = 1;

        //determine activity type
        int activity = 0;
        ArrayList<DetectedActivity> detectedActivitiesList = DetectedActivitiesIntentService.detectedActivities;
        for (DetectedActivity da: detectedActivitiesList) {
            if (da.getType() == DetectedActivity.STILL && da.getConfidence() >= 35){
                activity = 0;
            }else{
                activity = 1;
            }
        }

        //TODO: determine location color

        try{
            // url where the data will be posted
            String postReceiverUrl = "http://52.11.244.12/data.php";
            Log.v(TAG, "postURL: " + postReceiverUrl);
            Log.v(TAG, "time now is: " + currentTime);

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);


            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("userID", ""+tv_aID.getText()));
            nameValuePairs.add(new BasicNameValuePair("timestamp", currentTime));
            nameValuePairs.add(new BasicNameValuePair("lat", ""+tv_lat.getText()));
            nameValuePairs.add(new BasicNameValuePair("lng", ""+tv_lng.getText()));
            nameValuePairs.add(new BasicNameValuePair("activity", String.valueOf(activity)));
            nameValuePairs.add(new BasicNameValuePair("color", "red"));


            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v(TAG, "Response: " +  responseStr);

                // you can add an if statement here and do other actions based on the response
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


