package com.example.findmybike.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Mergim on 2014-03-31.
 */
public class SplashScreen extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        setContentView(R.layout.splash);
        Thread sc = new Thread(){

            public void run(){
                try {
                    sleep(3000);
                    Intent sintent = new Intent("android.intent.action.MENU");
                    startActivity(sintent);

                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    finish();
                }

            }

        };
        sc.start();
    }
}
