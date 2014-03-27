package com.example.findmybike.app;

/**
 *  * This task waits for the Location Services helper to acquire a location in a worker thread
 * so that we don't lock the UI thread whilst waiting.
 *
 * Created by Sven on 3/27/2014.
 */
public class LocationWorker {

protected void onPreExecute() {}


protected void onPostExecute(Boolean result) {
                /* Here you can call myLocationHelper.getLat() and
                myLocationHelper.getLong() to get the location data.*/
        }


protected Boolean doInBackground(Boolean... params) {

        //while the location helper has not got a lock
        //while(myLocationHelper.gotLocation() == false){
        //do nothing, just wait
       //}
        //once done return true
        return true;
        }
        }
