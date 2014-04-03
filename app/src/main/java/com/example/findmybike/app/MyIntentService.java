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
    }

    @Override
    protected void onHandleIntent(Intent intent) {
            if (ActivityRecognitionResult.hasResult(intent)) {

                ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
                String type = getType(result);
                Intent i = new Intent("com.kpbird.myactivityrecognition.ACTIVITY_RECOGNITION_DATA");
                i.putExtra("Activity", type);
                sendBroadcast(i);


        }
    }
    private String getType(ActivityRecognitionResult result){

        DetectedActivity act = result.getMostProbableActivity();
        String ret = "";

        switch (act.getType()){
            case DetectedActivity.IN_VEHICLE: ret = "Bil";
                break;
            case DetectedActivity.ON_BICYCLE: ret = "Cykel";
                break;
            case DetectedActivity.ON_FOOT: ret = "Till fots";
                break;
            case DetectedActivity.STILL: ret = "Stilla";
                break;
            case DetectedActivity.TILTING: ret = "Lutar";
                break;
            case DetectedActivity.UNKNOWN: ret = "Okänd";
                break;
        }
        return ret;
    }
}
