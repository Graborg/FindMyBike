package com.example.findmybike.app;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AbstractReceiverActivity {
    private static Context context;
    Button b1;
    Button b2;

    TextView textLat;
    TextView textLon;

    private LocationHelper myLocationHelper;
    ActivityHandler activityHandler;
    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.rgb(224,52,30))); // changes the color of the actionbar
        MainActivity.context = getApplicationContext();
        TextView actTextField = (TextView)findViewById(R.id.activity_text_field);

        myLocationHelper = new LocationHelper(context, this);
        activityHandler = new ActivityHandler(myLocationHelper, actTextField, this);
        activityHandler.register(this);

        b1 = (Button)findViewById(R.id.b1);
        b2 = (Button)findViewById(R.id.b2);

        if(myLocationHelper.gotLocation()){
            b2.setBackgroundResource(R.drawable.buttonleft);
            b2.setEnabled(true);
        }else{
            b2.setBackgroundResource(R.drawable.buttonnotclickableleft);
            b2.setEnabled(false);
        }

        int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resp == ConnectionResult.SUCCESS){
            actClient = new ActivityRecognitionClient(this, this, this);
            actClient.connect();
            Log.v(TAG,"actClient started");
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String v =  intent.getStringExtra("Activity");
                actTextField.setText(v);
                activityHandler.handle(MainActivity.this, v);
                Log.i(TAG,v);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.myactivityrecognition.ACTIVITY_RECOGNITION_DATA");
        registerReceiver(receiver, filter);

        textLat = (TextView)findViewById(R.id.latitude);
        textLon = (TextView)findViewById(R.id.longitude);
        textLat.setText(Float.toString(myLocationHelper.getLong()));
        textLon.setText(Float.toString(myLocationHelper.getLat()));

        Log.v(TAG, "Appen har startats");
    }

    public void savePosition(View v){
       myLocationHelper.startListener();
    }


    public void openMap(View v){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("BikePosition", LocationHelper.BikePosition);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        actClient.disconnect();
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
    public void log(View v){
        Intent i = new Intent(this, LogActivity.class);
        //i.putExtra("activityHandler", activityHandler);
        startActivity(i);
    }
    protected void onPause() {
        super.onPause();
        activityHandler.unRegister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityHandler.register(this);
        textLat.setText(myLocationHelper.getLat().toString());
        textLon.setText(myLocationHelper.getLong().toString());
    }

    @Override
    public void savePosition(Float lat, Float lon) {
        b2.setBackgroundResource(R.drawable.buttonleft);
        b2.setEnabled(true);
        textLat.setText(lat.toString());
        textLon.setText(lon.toString());
    }
}
