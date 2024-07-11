package com.leegosolutions.vms_host_app.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CS_Connection {

    private Context context;

    public CS_Connection(Context context) {
        this.context = context;
    }

    public boolean getStatus() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                status = true;
            }

        } catch (Exception ignored) {}
        return status;
    }

}
