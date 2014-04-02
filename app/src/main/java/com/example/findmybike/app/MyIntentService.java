package com.example.findmybike.app;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import android.app.IntentService;
import android.content.Intent;


/**
 * Created by svenelfgren on 02/04/14.
 */
public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            // Put your application specific logic here (i.e. result.getMostProbableActivity())
            DetectedActivity act = result.getMostProbableActivity();
            switch (act.getType()){
                case DetectedActivity.IN_VEHICLE:;
                case DetectedActivity.ON_BICYCLE:;
                case DetectedActivity.ON_FOOT:;
                case DetectedActivity.STILL:;
                case DetectedActivity.TILTING:;
                case DetectedActivity.UNKNOWN:;
            }
        }
    }
}
