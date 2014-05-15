package com.example.findmybike.app;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by micke on 5/15/14.
 */
public abstract class AbstractReceiverActivity extends ActionBarActivity {
    public abstract void savePosition(Float lat, Float lon);
}
