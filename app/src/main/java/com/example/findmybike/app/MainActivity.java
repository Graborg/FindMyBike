package com.example.findmybike.app;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.content.BroadcastReceiver;


public class MainActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    TextView lat;
    TextView lon;

    TextView activity;

    Float latitude;
    Float longitude;

    private ActivityRecognitionClient actClient;
    private BroadcastReceiver receiver;


    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resp == ConnectionResult.SUCCESS){
            actClient = new ActivityRecognitionClient(this, this, this);
            actClient.connect();
            Log.v(TAG,"actClient started");
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String v =  "Activity :" + intent.getStringExtra("Activity") +"n";
            }
        };



        activity = (TextView)findViewById(R.id.activity);
        lat = (TextView)findViewById(R.id.latitude);
        lon = (TextView)findViewById(R.id.longitude);
        Log.v(TAG, "Appen har startats");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent intent = new Intent(this, MyIntentService.class);
        PendingIntent callbackIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        actClient.requestActivityUpdates(3000, callbackIntent);
        Log.v(TAG, "Connected");
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void savePosition(View v){

       LocationHelper myLocationHelper = new LocationHelper(this, this);
    }

    public void button2(View v){
        LocationTracker myLocationTracker = new LocationTracker(this, this);

        //
        // Hitta positionen för cykeln, det vill säga positionen som sparas vid knapptryck
        // på ParkeMyBike!
    }

    public void openMaps(View v){


        //Open Google maps


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void gpsResponse(float latitude,float longitude){
        this.latitude = latitude;
        this.longitude = longitude;

        Log.v(TAG, "GPS RESPONSE");
        Log.v(TAG, Float.toString(latitude));
        Log.v(TAG, Float.toString(longitude));
        lat.setText(Float.toString(latitude));
        lon.setText(Float.toString(longitude));

    }

    protected void actResponse(String act){
        activity.setText(act);
    }

}
