package com.leegosolutions.vms_host_app.utility;

import android.app.Application;
import android.content.Context;

public class CS_App extends Application {
    public static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
