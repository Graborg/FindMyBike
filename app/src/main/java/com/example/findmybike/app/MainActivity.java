package com.example.findmybike.app;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.IntentFilter;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.BroadcastReceiver;


public class MainActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    Boolean click = false;

    TextView lat;
    TextView lon;

    Button b1;
    Button b2;

    TextView activity;

    Float latitude;
    Float longitude;

    private ActivityRecognitionClient actClient;
    private BroadcastReceiver receiver;
    private GoogleMap map;

    private static final String TAG = "Main";
    private static LatLng BikePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.b1);
        b2 = (Button)findViewById(R.id.b2);

        if(!click){
            b2.setBackgroundResource(R.drawable.buttonnotclickableleft);
            b2.setEnabled(false);
        }else{
            b2.setBackgroundResource(R.drawable.buttonleft);
            b2.setEnabled(true);
        }



        int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resp == ConnectionResult.SUCCESS){
            actClient = new ActivityRecognitionClient(this, this, this);
            actClient.connect();
            Log.v(TAG,"actClient started");
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String v =  intent.getStringExtra("Activity");
                activity.setText(v);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.myactivityrecognition.ACTIVITY_RECOGNITION_DATA");
        registerReceiver(receiver, filter);




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
        actClient.requestActivityUpdates(10000, callbackIntent);
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

       b2.setBackgroundResource(R.drawable.buttonleft);
       b2.setEnabled(true);
    }



    public void openMap(View v){

        setContentView(R.layout.map_view);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        Marker bikePosition = map.addMarker(new MarkerOptions()
                .position(BikePosition)
                .title("Your bike")
                .snippet("Nice bike")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.maplogo)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(BikePosition, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

//


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
        BikePosition = new LatLng(latitude,longitude);
    }

    protected void actResponse(String act){
        activity.setText(act);
    }

}
