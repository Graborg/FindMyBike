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
    private static final Integer Walking_RECOG_MIN = 2;
    public static Integer nbrOfRecognizedBikeAct;
    public static Integer nbrOfRecognizedWalkingAct;
    private MediaPlayer player = null;
    public ActivityHandler(){
        MediaPlayer player = new MediaPlayer();
        nbrOfRecognizedBikeAct = 0;
        nbrOfRecognizedWalkingAct = 0;
    }
    public void handle(MainActivity main, String recognizedAct) {
        if (recognizedAct.equals("Cykel")) {
            nbrOfRecognizedBikeAct++;
        }
        if(recognizedAct.equals("Till fots") ){
            nbrOfRecognizedWalkingAct++;
            if( nbrOfRecognizedBikeAct >= BIKE_RECOG_MIN && nbrOfRecognizedWalkingAct >= Walking_RECOG_MIN) {
                 main.savePosition((Button) main.findViewById(R.id.b1));
                 resetBike();
             }
        }
    }


    private void resetBike(){
        nbrOfRecognizedBikeAct = 0;
        nbrOfRecognizedWalkingAct = 0;
    }

}