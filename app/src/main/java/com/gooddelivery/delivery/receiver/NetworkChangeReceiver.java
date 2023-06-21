package com.gooddelivery.delivery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by santhosh@appoets.com on 07-11-2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static String NETWORK_AVAILABLE_ACTION = "com.foodie.provider";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    @Override
    public void onReceive(Context context, Intent intent) {
        NETWORK_AVAILABLE_ACTION = context.getApplicationContext().getPackageName();
        Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
        networkStateIntent.putExtra(IS_NETWORK_AVAILABLE,  isConnectedToInternet(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
    }

    public static boolean isConnectedToInternet(Context context) {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            Log.e(NetworkChangeReceiver.class.getName(), e.getMessage());
            return false;
        }
    }
}
