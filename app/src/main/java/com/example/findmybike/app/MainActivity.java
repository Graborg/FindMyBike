package com.example.findmybike.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        LocationHelper myLocationHelper = new LocationHelper(this);
        LocationWorker locationTask = new LocationWorker(){
            public void callback(Object object){

            }
        };
        locationTask .execute(new Boolean[] {true});

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
>>>>>>> 116d55cdc6876743e513e659c6b620b61af7764f
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

}
