package com.example.findmybike.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Mergim on 2014-05-19.
 */
public class FoundBikeActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundbike);

        Thread sc = new Thread() {

            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent("android.intent.action.MENU");
                    startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }

            }

        };
        sc.start();
    }
}