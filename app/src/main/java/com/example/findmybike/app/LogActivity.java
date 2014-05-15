package com.example.findmybike.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static com.example.findmybike.app.ActivityHandler.register;


public class LogActivity extends AbstractReceiverActivity {
    public String logString;
    private TextView logText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        logText = (TextView)findViewById(R.id.log_text);
        logString = "";
        logText.setText(logString);
        register(this);
    }


    public void logActivity(String update){
        logString = logString + update + " \n";
        logText.setText(logString);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log, menu);
        return true;
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

    public void reset(View v) {
        logString = "";
        logText.setText(logString);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityHandler.unRegister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        register(this);
    }

    @Override
    public void savePosition(Float lat, Float lon) {

    }
}
