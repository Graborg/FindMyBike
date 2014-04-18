package com.example.findmybike.app;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

    TextView actTextField;

    Float latitude;
    Float longitude;

    private ActivityRecognitionClient actClient;
    private BroadcastReceiver receiver;


    private static final String TAG = "Main";
    private static LatLng BikePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = this.getSharedPreferences("BikePosition",0);
        this.latitude = prefs.getFloat("latitude",0);
        this.longitude = prefs.getFloat("longitude",0);
        BikePosition = new LatLng(latitude,longitude);
        actTextField = (TextView)findViewById(R.id.activity_text_field);
        b1 = (Button)findViewById(R.id.b1);
        b2 = (Button)findViewById(R.id.b2);

        if(latitude ==0 && longitude==0){
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
                actTextField.setText(v);
                playSound(v);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.myactivityrecognition.ACTIVITY_RECOGNITION_DATA");
        registerReceiver(receiver, filter);


        lat = (TextView)findViewById(R.id.latitude);
        lon = (TextView)findViewById(R.id.longitude);
        lat.setText(Float.toString(latitude));
        lon.setText(Float.toString(longitude));

        Log.v(TAG, "Appen har startats");
    }

    private void playSound(String activityString) {
        MediaPlayer player = null;
        if(activityString.equals("Cykel")){
            player=MediaPlayer.create(MainActivity.this,R.raw.bikebell);
        }
        if(activityString.equals("Till fots")){
            player=MediaPlayer.create(MainActivity.this,R.raw.walking);
        }
        if(player != null) {
            player.start();
        }
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

    }



    public void openMap(View v){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("BikePosition", BikePosition);
        startActivity(intent);
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

        b2.setBackgroundResource(R.drawable.buttonleft);
        b2.setEnabled(true);
        SharedPreferences prefs = this.getSharedPreferences("BikePosition",0);
        SharedPreferences.Editor editor= prefs.edit();
        editor.putFloat("latitude",latitude);
        editor.putFloat("longitude",longitude);
        editor.commit();
    }
    //When is this used?
    protected void actResponse(String act) {
        actTextField.setText(act);
    }
}
