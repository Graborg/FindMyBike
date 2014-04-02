package com.example.findmybike.app;


import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognitionClient;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.Context;




/**
 * Created by svenelfgren on 02/04/14.
 */
public class ActivityClient implements ConnectionCallbacks, OnConnectionFailedListener {

    private ActivityRecognitionClient actClient;
    private Context context;

    public ActivityClient(Context context){
        ActivityRecognitionClient mActivityRecognitionClient =
                new ActivityRecognitionClient(context, this, this);
        mActivityRecognitionClient.connect();
        this.context = context;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent intent = new Intent(context, MyIntentService.class);
        PendingIntent callbackIntent = PendingIntent.getService(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        actClient.requestActivityUpdates(10000, callbackIntent);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
