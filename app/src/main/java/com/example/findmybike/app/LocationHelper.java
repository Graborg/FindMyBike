package com.example.findmybike.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

/**
 * * Location Helper Class.
 * Handles creation of the Location Manager and Location Listener.
 *
 * Created by Sven on 3/27/2014.
 */
public class LocationHelper {


    private MainActivity main;
    private SharedPreferences prefs;
    private static final String TAG = "LocationHelper";

    //variables to store lat and long
    private float latitude  = 0.0f;
    private float longitude = 0.0f;


    //my location manager and listener
    private LocationManager    locationManager;
    private MyLocationListener locationListener;
    public static LatLng BikePosition;
    /**
     * Constructor.
     *
     * @param context - The context of the calling activity.
     */
    public LocationHelper(Context context, MainActivity main){

        Log.v(TAG, "SETTING UP LISTENER");

        //setup the location manager
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        //create the location listener
        locationListener = new MyLocationListener();
        this.main = main;
        prefs = context.getSharedPreferences("BikePosition", 0);
        latitude = prefs.getFloat("latitude",0);
        longitude = prefs.getFloat("longitude",0);
        BikePosition = new LatLng(latitude, longitude);
    }

    public void startListener(){
        //setup a callback for when the GRPS/WiFi gets a lock and we receive data
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //setup a callback for when the GPS gets a lock and we receive data
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public boolean gotLocation() {
        return (latitude != 0 && longitude != 0);
    }

    /***
     * Used to receive notifications from the Location Manager when they are sent. These methods are called when
     * the Location Manager is registered with the Location Service and a state changes.
     *
     * @author Scott Helme
     */
    public class MyLocationListener implements LocationListener {

        //called when the location service reports a change in location
        public void onLocationChanged(Location location) {

            Log.v(TAG, "LOCATION CHANGE");
            Log.v(TAG, Float.toString(latitude));
            Log.v(TAG, Float.toString(longitude));

            //store lat and long
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();

            //now we have our location we can stop the service from sending updates
            //comment out this line if you want the service to continue updating the users location
            locationManager.removeUpdates(locationListener);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat("latitude", latitude).apply();
            editor.putFloat("longitude", longitude).apply();
            editor.commit();
            BikePosition = new LatLng(latitude, longitude);

        }

        //called when the provider is disabled
        public void onProviderDisabled(String provider) {}
        //called when the provider is enabled
        public void onProviderEnabled(String provider) {}
        //called when the provider changes state
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    /***
     * Stop updates from the Location Service.
     */
    public void killLocationServices(){
        locationManager.removeUpdates(locationListener);
    }

    /***
     * Get Latitude from GPS Helper.
     * Should check gotLocation() first.
     * @return - The current Latitude.
     */
    public Float getLat(){
        return latitude;
    }

    /***
     * Get Longitude from GPS Helper.
     * Should check gotLocation() first.
     * @return - The current Longitude.
     */
    public Float getLong(){
        return longitude;
    }


}
