package com.example.findmybike.app;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


/**
 * Created by svenelfgren on 02/04/14.
 */
public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService";

    public MyIntentService() {
        super("MyIntentService");
        Log.v(TAG, "Service started.");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "Intent handle");

        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            Intent i = new Intent("com.kpbird.myactivityrecognition.ACTIVITY_RECOGNITION_DATA");
            i.putExtra("Activity", getType(result) );



        }
    }
    private String getType(ActivityRecognitionResult result){
        DetectedActivity act = result.getMostProbableActivity();
        switch (act.getType()){
            case DetectedActivity.IN_VEHICLE: Log.v(TAG, "BIL");                    ;
            case DetectedActivity.ON_BICYCLE: Log.v(TAG, "CYKEL");
            case DetectedActivity.ON_FOOT: Log.v(TAG, "TILL FOTS");
            case DetectedActivity.STILL: Log.v(TAG, "STILLA");
            case DetectedActivity.TILTING: Log.v(TAG, "LUTNING");
            case DetectedActivity.UNKNOWN: Log.v(TAG, "OKÄND");
        }
        return "";
    }
}
