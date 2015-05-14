package com.stepahnieqianwang.upalarm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
 * Created by Bo on 5/9/15.
 */
public class LastMoveAsyncTask extends AsyncTask<String, String, String> {
    protected static final String TAG = "Retrieve LastMove";

    private Context mContext;

    public LastMoveAsyncTask(Context context) {
        mContext = context;
    }

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
    protected void onPostExecute(String result) {
        // do stuff after posting data

    }
    private String retrieveData(){
        String id = Constants.androidID;

        try{
            // url where the data will be posted
            String postReceiverUrl = "http://52.11.244.12/lastMove.php";
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
                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v(TAG, "Response: " +  responseStr);
                return responseStr;
                // you can add an if statement here and do other actions based on the response
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0000-00-00 00:00:00";
    }
}


