package com.morgan.make_kots_great_again;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class NetworkChecks {

    //-----------------------------------------------------------------
    // Function that checks if a host is up by sending an echo request
    //-----------------------------------------------------------------

    /* @param (String) => Takes a String reprensenting either an ip address or a host name (Eg:8.8.8.8 or google.com)
    *  @return (Boolean) => Returns true if the specified host is up, false if it's down */
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

    //------------------------------------------------------------------------
    // Function that checks if the user (smartphone) is connected to internet
    //------------------------------------------------------------------------

    /* @param (Context) => Takes a context
    *  @return (Boolean) => Returns true if the user is connected to internet, false otherwise */
    protected boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
