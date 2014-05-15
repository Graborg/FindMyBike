package com.example.findmybike.app;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

import java.io.Serializable;

/**
 * Created by micke on 5/11/14.
 */
public class ActivityHandler implements Serializable, GooglePlayServicesClient.ConnectionCallbacks , GooglePlayServicesClient.OnConnectionFailedListener {
    private static final Integer BIKE_RECOG_MIN = 3;
    private static final Integer Walking_RECOG_MIN = 2;
    public static Integer nbrOfRecognizedBikeAct;
    public static Integer nbrOfRecognizedWalkingAct;
    private static MediaPlayer player;
    private LocationHelper locationHelper;
    private static BroadcastReceiver receiver;
    private static AbstractReceiverActivity currAct;
    private ActivityRecognitionClient actClient;
    TextView actTextField;
    private static final String TAG = "ActivityHandler";
    private static IntentFilter filter;

    public ActivityHandler(LocationHelper locationHelper, final TextView actTextField, AbstractReceiverActivity activity){
        this.currAct = activity;
        this.locationHelper = locationHelper;
        this.actTextField = actTextField;
        nbrOfRecognizedBikeAct = 0;
        nbrOfRecognizedWalkingAct = 0;

        filter = new IntentFilter();
        filter.addAction("com.kpbird.myactivityrecognition.ACTIVITY_RECOGNITION_DATA");

        player = new MediaPlayer();

        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(currAct);
        if(resp == ConnectionResult.SUCCESS){
            actClient = new ActivityRecognitionClient(currAct, this, this);
            actClient.connect();
            Log.v(TAG,"actClient started");
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String v =  intent.getStringExtra("Activity");
                actTextField.setText(v);
                handleActivityChange(v);
            }
        };

    }
    public void handleActivityChange(String recognizedAct) {
        if (recognizedAct.equals("Cykel")) {
            nbrOfRecognizedBikeAct++;
            if(currAct instanceof LogActivity){
                ((LogActivity)currAct).logActivity(nbrOfRecognizedBikeAct + " sekunder har du cyklat");
            }
        }
        if(recognizedAct.equals("Till fots") ){
            nbrOfRecognizedWalkingAct++;
            if(currAct instanceof LogActivity) {
                ((LogActivity) currAct).logActivity(nbrOfRecognizedWalkingAct + " sekunder har du gått");
            }
            if( nbrOfRecognizedBikeAct >= BIKE_RECOG_MIN && nbrOfRecognizedWalkingAct >= Walking_RECOG_MIN) {
                if(currAct instanceof LogActivity) {
                    ((LogActivity) currAct).logActivity("Du lyckades parkera cykeln!");
                }
                savePosition();
                resetBike();
            }
        }
    }

    private void resetBike(){
        nbrOfRecognizedBikeAct = 0;
        nbrOfRecognizedWalkingAct = 0;
    }

    private void savePosition(){
        locationHelper.startListener();
        currAct.savePosition(locationHelper.getLat(), locationHelper.getLong());;
        Context context = currAct.getApplicationContext();
        player = MediaPlayer.create(context, R.raw.bikebell);
        if (player != null) player.start();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent intent = new Intent(currAct, MyIntentService.class);
        PendingIntent callbackIntent = PendingIntent.getService(currAct, 0, intent,
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

    public static void unRegister(AbstractReceiverActivity activity) {
        activity.unregisterReceiver(receiver);
    }

    public static void register(AbstractReceiverActivity activity) {
        currAct = activity;
        activity.registerReceiver(receiver, filter);
    }
}