package com.example.findmybike.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    Thread timer;

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        LocationHelper myLocationHelper = new LocationHelper(this, this);

        timer = new Thread(){
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally{
                    
                      Intent nn = new Intent (MainActivity.this,SplashScreen.class);
                      startActivity(nn);

                }
            }
        };
        timer.start();

        Log.v(TAG, "Appen har startats");
    }


    public void button1(View v){



        TextView lat = (TextView)findViewById(R.id.latitude);
        TextView lon = (TextView)findViewById(R.id.longitude);


        //lat.setText(Integer.toString());
        //lon.setText(Integer.toString());


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

    protected void gpsResponse(float latitude,float longitude){
        Log.v(TAG, "GPS RESPONSE");
        Log.v(TAG, Float.toString(latitude));
        Log.v(TAG, Float.toString(longitude));


    }

}
