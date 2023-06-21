package com.gooddelivery.delivery.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.gooddelivery.delivery.helper.ConnectionHelper;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.gooddelivery.delivery.model.Profile;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by santhosh@appoets.com on 11-10-2017.
 */

public class GPSTracker extends Service {
    private static final String TAG = "GPSTracker";
    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 30000;
    private static final float LOCATION_DISTANCE = 10f;
    public static double distance = 0.0;
    ConnectionHelper connectionHelper;

    Context context;
    private class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener  " + provider);
            GlobalData.CURRENT_LOCATION = new Location(provider);
        }

        @Override
        public void onLocationChanged(final Location location) {
            Log.d(TAG, "onLocationChanged: " + location);
            GlobalData.CURRENT_LOCATION.set(location);
            updateLocationToServer();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider);
            //Toast.makeText(LocationTracking.this, "s_Distance : "+LocationTracking.distance, Toast.LENGTH_SHORT).show();
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        //super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        context = getBaseContext();
        initializeLocationManager();
        connectionHelper = new ConnectionHelper(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "location provider requires ACCESS_FINE_LOCATION | ACCESS_COARSE_LOCATION");
            return;
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (locationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    locationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void updateLocationToServer() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("latitude", GlobalData.CURRENT_LOCATION.getLatitude());
        map.put("longitude", GlobalData.CURRENT_LOCATION.getLongitude());
        Log.d(TAG, "updateLocationToServer "+map.toString());

        if (!connectionHelper.isConnectingToInternet())
            return;

        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");
        Call<Profile> call = GlobalData.api.updateLocation(header, map);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if (response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().toString());
                        Toast.makeText(GPSTracker.this, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(GPSTracker.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    GlobalData.profile = response.body();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Log.d("GPSTracker", "Something wrong - updateLocationToServer");
            }
        });
    }



    /*public static Double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        return SphericalUtil.computeDistanceBetween(point1, point2);
    }*/

}
