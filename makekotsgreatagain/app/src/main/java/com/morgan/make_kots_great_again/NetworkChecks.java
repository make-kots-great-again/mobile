package com.morgan.make_kots_great_again;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class NetworkChecks {

    protected boolean isHostUp(String host_or_ip){
        Runtime runtime = Runtime.getRuntime();
        try
        {   // (ping arguments) -c 1 => 1 echo request | -w 2 => Wait for 2 sec max
            Process  process = runtime.exec("/system/bin/ping -c 1 -w 1 " + host_or_ip);
            int status = process.waitFor();
            return status == 0;
        }
        catch (InterruptedException | IOException ignore){}
        return false;
    }

    protected boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
