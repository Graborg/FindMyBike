package com.example.findmybike.app;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    TextView lat;
    TextView lon;

    Float latitude;
    Float longitude;

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lat = (TextView)findViewById(R.id.latitude);
        lon = (TextView)findViewById(R.id.longitude);
        Log.v(TAG, "Appen har startats");
    }


    public void button1(View v){
       LocationHelper myLocationHelper = new LocationHelper(this, this);
    }

    public void button2(View v){
        LocationTracker myLocationTracker = new LocationTracker(this, this);

        //
        // Hitta positionen för cykeln, det vill säga positionen som sparas vid knapptryck
        // på ParkeMyBike!
    }


>>>>>>> 1b39b1893e2beb089b63797c96d2908066af5998

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
        this.latitude = latitude;
        this.longitude = longitude;

        Log.v(TAG, "GPS RESPONSE");
        Log.v(TAG, Float.toString(latitude));
        Log.v(TAG, Float.toString(longitude));
        lat.setText(Float.toString(latitude));
        lon.setText(Float.toString(longitude));

    }

}
