package com.example.findmybike.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

public class CompassActivity extends Activity implements LocationListener, GpsStatus.Listener, SensorEventListener {
    static final float ALPHA = 0.15f;
    Vibrator vibrate;
    private static final String TAG = "CompassActivity";


    private TextView yourPos, bikePos, distance;
    private ImageView arrow;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private Sensor compass,accelerometer;
    private String provider;
    private Location myLocation, bikeLocation;
    private GoogleMap map;

    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;
    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;
    private boolean hasLocation;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        Intent i = getIntent();

        //yourPos = (TextView) findViewById(R.id.pos);
        //friendPos = (TextView) findViewById(R.id.friendPos);
        //distance = (TextView) findViewById(R.id.distance);

        arrow = (ImageView) findViewById(R.id.compass);

        bikeLocation = new Location("Bike location");
        myLocation = new Location("My location");

        Log.v(TAG, "Latitude for the bike: "+LocationHelper.BikePosition.latitude);
        bikeLocation.setLatitude(LocationHelper.BikePosition.latitude);
        bikeLocation.setLongitude(LocationHelper.BikePosition.longitude);  // Cykelns pos på kartan

        compass = null;
        accelerometer = null;
        sensorManager = null;
        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];
        hasLocation = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        compass = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        locationManager.addGpsStatusListener(this);

        locationManager.requestLocationUpdates(provider, 400, 1, CompassActivity.this);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }

        sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }


    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
        sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        hasLocation = false;
    }

    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(CompassActivity.this, compass);
        sensorManager.unregisterListener(CompassActivity.this, accelerometer);
        vibrate.cancel();
        hasLocation = false;

    }

    public void onDestroy(){
        super.onDestroy();
        sensorManager.unregisterListener(CompassActivity.this, compass);
        sensorManager.unregisterListener(CompassActivity.this, accelerometer);
        vibrate.cancel();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onGpsStatusChanged(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        float lat = (float) location.getLatitude();
        float lon = (float) location.getLongitude();
        float accuracy = (float) location.getAccuracy();

        myLocation = location;

        if(!hasLocation) {
            bikeLocation.setLatitude(lat + 0.0002);
            bikeLocation.setLongitude(lon + 0.0002);
            hasLocation = true;
        }

        if (location != null) {

            /**Printa positionen**/
           // yourPos.setText("Lat: " + lat + " Lon: " + lon + " Acc: " + accuracy);
            //friendPos.setText("Lat: " + bikeLocation.getLatitude() + " Lon: " + bikeLocation.getLongitude());
            //distance.setText("Distance: " + location.distanceTo(bikeLocation));


        } else {
            yourPos.setText("hhh");
        }

        if(location.distanceTo(bikeLocation) <= 2){
           System.out.println("Du har hittat din cykel!");
        }




    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onSensorChanged(SensorEvent sensorEvent) {

        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                valuesAccelerometer = lowPass(sensorEvent.values.clone(), valuesAccelerometer);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                valuesMagneticField = lowPass(sensorEvent.values.clone(), valuesMagneticField);
                break;
        }
        if((valuesAccelerometer != null) && (valuesMagneticField != null) && (sensorManager != null)) {
            boolean success = sensorManager.getRotationMatrix(matrixR, matrixI, valuesAccelerometer, valuesMagneticField);
            if (success) {
                sensorManager.getOrientation(matrixR, matrixValues);
                double azimuth = Math.toDegrees(matrixValues[0]);

                GeomagneticField geoField = new GeomagneticField(
                        Double.valueOf(myLocation.getLatitude()).floatValue(),
                        Double.valueOf(myLocation.getLongitude()).floatValue(),
                        Double.valueOf(myLocation.getAltitude()).floatValue(),
                        System.currentTimeMillis());

                azimuth -= geoField.getDeclination();

                float angle = myLocation.bearingTo(bikeLocation);
                if (angle < 0) {
                    angle = angle + 360;
                }
                float direction = angle - (float) azimuth;

                arrow.setRotation(direction);

                if (((direction>-30)&&(direction<30))||((direction>330)&&(direction<390))) {  //no chance for multiples of 360 I hope...

                    long[] pattern = {0, 50, 250, 50, 2000};

                    vibrate.vibrate(pattern, 0);
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged (Sensor sensor,int i){

    }

    protected float[] lowPass ( float[] input, float[] output){
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}