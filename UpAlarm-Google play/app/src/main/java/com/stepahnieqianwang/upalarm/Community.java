package com.stepahnieqianwang.upalarm;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.location.LocationServices;

import android.content.Context;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.app.Activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class Community extends ActionBarActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener{

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    protected boolean playConnected;
    protected boolean hasDrawn;
    protected Location mLastLocation;
    protected GoogleMap gMap;

    protected int RED = Color.argb(50, 255, 0, 0);
    protected int GREEN = Color.argb(50, 0, 255, 0);
    protected int YELLOW = Color.argb(50, 255, 255, 0);
    protected int BLACK = Color.argb(100, 255, 255, 255);

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        context = getApplicationContext();
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        Context context = getApplicationContext();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        //Toast.makeText(context, String.valueOf(mLastLocation.getLatitude()) + ", " +
        //        String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();

        LatLng ith = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        Log.v("ITH: ", ""+ith);
        gMap.setMyLocationEnabled(true);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ith, 17));

        if (!hasDrawn) {

            JSONObject toPlot = Constants.RETRIEVED_ALL;
            JSONArray ind = new JSONArray();
            JSONArray all = new JSONArray();

            try {
                ind = toPlot.getJSONArray("ind");
                Log.v("HEATMAP: ", ind.toString());
                int indLength = ind.length();
                JSONObject tmpJSON;
                for (int i = 0; i<indLength; i++) {
                    tmpJSON = ind.getJSONObject(i);
                    Log.v("JSONARRAY: ", tmpJSON.toString());
                    int thisColor;
                    switch (tmpJSON.getString("color")) {
                        case "YELLOW":
                            thisColor = YELLOW;
                            break;
                        case "GREEN":
                            thisColor = GREEN;
                            break;
                        default:
                        case "RED":
                            thisColor = RED;
                            break;
                    }
                    LatLng thisLoc = new LatLng(Double.parseDouble(tmpJSON.getString("lat")), Double.parseDouble(tmpJSON.getString("lng")));
                    Log.v("CIRCLES: ", ""+thisColor);
                    Log.v("CIRCLES: ", ""+thisLoc);
                    CircleOptions tmpCO = new CircleOptions()
                            .fillColor(thisColor)
                            .strokeColor(BLACK)
                            .center(thisLoc)
                            .radius(7);
                    gMap.addCircle(tmpCO);
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thisLoc, 17));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                all = toPlot.getJSONArray("all");
                Log.v("HEATMAP: ", all.toString());
                int allLength = all.length();
                JSONObject tmpJSON;
                for (int i = 0; i<allLength; i++) {
                    tmpJSON = all.getJSONObject(i);
                    Log.v("JSONARRAY: ", tmpJSON.toString());
                    int thisColor;
                    switch (tmpJSON.getString("color")) {
                        case "YELLOW":
                            thisColor = YELLOW;
                            break;
                        case "GREEN":
                            thisColor = GREEN;
                            break;
                        default:
                        case "RED":
                            thisColor = RED;
                            break;
                    }
                    LatLng thisLoc = new LatLng(Double.parseDouble(tmpJSON.getString("lat")), Double.parseDouble(tmpJSON.getString("lng")));
                    Log.v("CIRCLES: ", ""+thisColor);
                    Log.v("CIRCLES: ", ""+thisLoc);
                    CircleOptions tmpCO = new CircleOptions()
                            .fillColor(thisColor)
                            .strokeColor(thisColor)
                            .center(thisLoc)
                            .radius(7);
                    gMap.addCircle(tmpCO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*
            // roughly barton
            LatLng hub1 = new LatLng(   mLastLocation.getLatitude() + 0.0012f,
                                        mLastLocation.getLongitude() + 0.0005f);

            // roughly phillips
            LatLng hub2 = new LatLng(   mLastLocation.getLatitude() - 0.0002,
                                        mLastLocation.getLongitude() - 0.0012f);

            CircleOptions circleO2 = new CircleOptions()
                    .fillColor(RED)
                    .strokeColor(RED)
                    .center(hub1)
                    .radius(5); // In meters

            CircleOptions circleO3 = new CircleOptions()
                    .fillColor(YELLOW)
                    .strokeColor(YELLOW)
                    .center(hub2)
                    .radius(5);

            CircleOptions tmpCO = new CircleOptions().radius(5); // In meters;
            Random myRand = new Random();
            for (int i = 0; i < 20; i++) {
                int rndColor = myRand.nextInt(4);
                int tmpColor;
                switch (rndColor) {
                    case 1:
                        tmpColor = GREEN;
                        break;
                    case 2:
                        tmpColor = BLUE;
                        break;
                    case 3:
                        tmpColor = YELLOW;
                        break;
                    case 0:
                    default:
                        tmpColor = RED;
                        break;
                }

                tmpCO.fillColor(tmpColor)
                    .strokeColor(tmpColor)
                    .center(new LatLng(
                            hub1.latitude + (myRand.nextFloat()*5 -2.5f) * 0.0001f,
                            hub1.longitude + (myRand.nextFloat()*12 - 6.0f) * 0.0001f));
                gMap.addCircle(tmpCO);
                tmpCO.center(new LatLng(
                        hub2.latitude + (myRand.nextFloat()*7 -3.5f) * 0.0001f,
                        hub2.longitude + (myRand.nextFloat()*5 - 2.5f) * 0.0001f));
                gMap.addCircle(tmpCO);
                if (i%2==0) {
                    tmpCO.center(new LatLng(
                            mLastLocation.getLatitude() + (myRand.nextFloat() * 5 - 2.5f) * 0.0001f,
                            mLastLocation.getLongitude() + (myRand.nextFloat() * 7 - 1.5f) * 0.0001f));
                    gMap.addCircle(tmpCO);
                }
            }*/
            hasDrawn = true;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        Context context = getApplicationContext();
        Toast.makeText(context, "Connection Failed!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        //Log.i(TAG, "Connection suspended");
        Context context = getApplicationContext();
        Toast.makeText(context, "Connection Suspended!", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        /*
        if (playConnected) {
            Context context = getApplicationContext();
            Toast.makeText(context, String.valueOf(mLastLocation.getLatitude()) + ", " +
                    String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LatLng ith = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ith, 13));

            map.addMarker(new MarkerOptions()
                    .title("Ithaca")
                    .snippet("It's Gorges")
                    .position(ith));
        } else {
            Context context = getApplicationContext();
            Toast.makeText(context, "Play not connected!", Toast.LENGTH_SHORT).show();
        }
        */
        Toast.makeText(context, "Setting Map!", Toast.LENGTH_SHORT).show();
        gMap = map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_community, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
