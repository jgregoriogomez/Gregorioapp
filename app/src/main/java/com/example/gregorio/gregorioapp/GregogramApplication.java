package com.example.gregorio.gregorioapp;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by Gregorio on 03/02/2017.
 */

public class GregogramApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
