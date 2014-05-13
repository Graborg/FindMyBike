package com.example.findmybike.app;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by micke on 5/11/14.
 */
public class ActivityHandler {
    private static final Integer BIKE_RECOG_MIN = 3;
    public static Integer nbrOfRecognizedBikeAct;
    private MediaPlayer player = null;
    public ActivityHandler(){
        MediaPlayer player = new MediaPlayer();
        nbrOfRecognizedBikeAct = 0;
    }
    public void handle(MainActivity main, String recognizedAct) {
        switch (recognizedAct) {
            case "Cykel":
                nbrOfRecognizedBikeAct++;
                break;
            case "Till fots":
                if (nbrOfRecognizedBikeAct >= BIKE_RECOG_MIN) {
                    main.savePosition((Button)main.findViewById(R.id.b1));
                    resetBike();
                }
                break;
            default:
                break;
        }
    }


    private void resetBike(){
        nbrOfRecognizedBikeAct = 0;
    }

}