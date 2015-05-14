package com.stepahnieqianwang.upalarm;

import android.os.AsyncTask;
import android.util.Log;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stepahnieqianwang on 5/6/15.
 */
public class RetrieveAllAsyncTask extends AsyncTask<String, String, String> {
    protected static final String TAG = "Retrieve Data";

    protected void onPreExecute() {
        super.onPreExecute();
        // do stuff before posting data
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            retrieveData();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String lengthOfFile) {
        // do stuff after posting data
    }
    private void retrieveData(){
        String id = Constants.androidID;

        try{
            // url where the data will be posted
            String postReceiverUrl = "http://52.11.244.12/retrieveAll.php";
            //Log.v(TAG, "postURL: " + postReceiverUrl);
            //Log.v(TAG, "ID is: " + Constants.androidID);

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("userID", Constants.androidID));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                JSONObject jsonResult = new JSONObject();
                String responseStr = EntityUtils.toString(resEntity).trim();
                try {
                    jsonResult = new JSONObject(responseStr);
                    Constants.RETRIEVED_ALL = jsonResult;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.v(TAG, "Response: " +  jsonResult.toString());

                // you can add an if statement here and do other actions based on the response
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


