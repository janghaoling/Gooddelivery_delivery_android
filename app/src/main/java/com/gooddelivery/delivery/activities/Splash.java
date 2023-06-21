package com.gooddelivery.delivery.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import com.gooddelivery.delivery.api.APIError;
import com.gooddelivery.delivery.api.ApiClient;
import com.gooddelivery.delivery.api.ApiInterface;
import com.gooddelivery.delivery.api.ErrorUtils;
import com.gooddelivery.delivery.helper.ConnectionHelper;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.LocaleUtils;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.model.Profile;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    ConnectionHelper helper;
    int retryCount = 0;
    AlertDialog alert;
    ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);
    String device_token, device_UDID;
    Boolean isNotification = false;
    private String language = "es";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        helper = new ConnectionHelper(this);
        getDeviceToken();
       /* TextView tv=null;
        tv.setText("dfadf");*/
        LocaleUtils.setLocale(getApplicationContext(), language);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value1 = extras.getString("Notification");
            if (value1 != null) {
                isNotification = true;
            }
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 3000ms
                if (SharedHelper.getKey(Splash.this, "logged_in").equalsIgnoreCase("1")) {
                    if (helper.isConnectingToInternet()) {
                        getProfile();
                    } else {
                        showDialog();
                    }
                } else {
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();
                }

            }
        }, 3000);

    }


    public void getProfile() {
        retryCount++;
        HashMap<String, String> map = new HashMap<>();
        map.put("device_type", "android");
        map.put("device_id", device_UDID);
        map.put("device_token", device_token);
        String header = SharedHelper.getKey(Splash.this, "token_type") + " " + SharedHelper.getKey(Splash.this, "access_token");
        Call<Profile> call = api.getProfile(header, map);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {

                if (response.isSuccessful()) {
                    GlobalData.profile = response.body();
                    SharedHelper.putKey(Splash.this, "logged_in", "1");
                    SharedHelper.putKey(Splash.this, "currency_code", GlobalData.profile.getCurrencyCode());
                    if (isNotification)
                        startActivity(new Intent(Splash.this, Home.class).putExtra("is_splash", true));
                    else
                        startActivity(new Intent(Splash.this, ShiftStatus.class).putExtra("is_splash", true));

                    finish();
                } else {
                    if (response.code() == 401) {
                        SharedHelper.putKey(Splash.this, "logged_in", "0");
                        startActivity(new Intent(Splash.this, Login.class));
                        finish();
                    }
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(Splash.this, error.getError(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Toast.makeText(Splash.this, "Something wrong", Toast.LENGTH_LONG).show();
                if (retryCount < 5) {
                    getProfile();
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.connect_to_network))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.connect_to_wifi), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alert.dismiss();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.quit), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alert.dismiss();
                        finish();
                    }
                });
        if (alert == null) {
            alert = builder.create();
            alert.show();
        }
    }


    public void getDeviceToken() {
        String TAG = "FCM";
        try {
            if (!SharedHelper.getKey(this, "device_token").equals("") && SharedHelper.getKey(this, "device_token") != null) {
                device_token = SharedHelper.getKey(this, "device_token");
                Log.d(TAG, "GCM Registration Token: " + device_token);
            } else {
                device_token = FirebaseInstanceId.getInstance().getToken();
                SharedHelper.putKey(this, "device_token", "" + FirebaseInstanceId.getInstance().getToken());
                Log.d(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh");
        }

        try {
            device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Device UDID:" + device_UDID);
            SharedHelper.putKey(this, "device_id", device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }
}
