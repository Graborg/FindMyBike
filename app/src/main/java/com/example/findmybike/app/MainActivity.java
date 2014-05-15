package com.example.findmybike.app;


import android.app.ActionBar;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
    private static Context context;
    Boolean click = false;


    Button b1;
    Button b2;

    TextView actTextField;

    TextView textLat;
    TextView textLon;
    private ActivityRecognitionClient actClient;
    private BroadcastReceiver receiver;
    private static LocationHelper myLocationHelper;
    private MediaPlayer player;
    private static final String TAG = "Main";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        myLocationHelper = new LocationHelper(context, this);
        final ActivityHandler activityHandler = new ActivityHandler();
        player = null;
        actTextField = (TextView)findViewById(R.id.activity_text_field);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(224,52,30))); // changes the color of the actionbar


        b1 = (Button)findViewById(R.id.b1);
        b2 = (Button)findViewById(R.id.b2);
        Intent i = new Intent(this, MainActivity.class);

        if(myLocationHelper.gotLocation()){
            b2.setBackgroundResource(R.drawable.buttonleft);
            b2.setEnabled(true);
        }else{
            b2.setBackgroundResource(R.drawable.buttonnotclickableleft);
            b2.setEnabled(false);
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
                activityHandler.handle(MainActivity.this, v);
                Log.i(TAG,v);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.myactivityrecognition.ACTIVITY_RECOGNITION_DATA");
        registerReceiver(receiver, filter);

        textLat = (TextView)findViewById(R.id.latitude);
        textLon = (TextView)findViewById(R.id.longitude);
        textLat.setText(Float.toString(myLocationHelper.getLong()));
        textLon.setText(Float.toString(myLocationHelper.getLat()));

        Log.v(TAG, "Appen har startats");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent intent = new Intent(this, MyIntentService.class);
        PendingIntent callbackIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        actClient.requestActivityUpdates (1000, callbackIntent);
        Log.v(TAG, "Connected");
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void savePosition(View v){
       myLocationHelper.startListener();
    }

    public void updatePosition(){

        textLat.setText(Float.toString(myLocationHelper.getLat()));
        textLon.setText(Float.toString(myLocationHelper.getLong()));
        b2.setBackgroundResource(R.drawable.buttonleft);
        b2.setEnabled(true);

        player = MediaPlayer.create(context, R.raw.bikebell);
        if (player != null) player.start();
    }

    public void openMap(View v){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("BikePosition", LocationHelper.BikePosition);
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

    //When is this used?
    protected void actResponse(String act) {
        actTextField.setText(act);
    }
}
