package com.gooddelivery.delivery.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.api.APIError;
import com.gooddelivery.delivery.api.ApiClient;
import com.gooddelivery.delivery.api.ApiInterface;
import com.gooddelivery.delivery.api.ErrorUtils;
import com.gooddelivery.delivery.helper.CustomDialog;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.LocaleUtils;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.gooddelivery.delivery.model.Profile;
import com.gooddelivery.delivery.model.Token;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.philio.pinentry.PinEntryView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OTP extends AppCompatActivity {

    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.otp)
    PinEntryView otp;

    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        getDeviceToken();
        customDialog = new CustomDialog(OTP.this);

        String mobile_number = SharedHelper.getKey(OTP.this, "mobile_number");
        phone.setText(mobile_number);
//        if (GlobalData.otp != null) {
//            otp.setText(String.valueOf(GlobalData.otp.getOtp()));
//            System.out.println("OTP : " + GlobalData.otp.getOtp());
//        } else {
//            otp.setText("123456");
//        }

    }

    private void login(HashMap<String, String> map) {
        if (customDialog != null)
            customDialog.show();

        Call<Token> call = apiInterface.postLogin(map);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if (response.body().getAccessToken() != null) {
                        GlobalData.token = response.body();
                        SharedHelper.putKey(OTP.this, "token_type", GlobalData.token.getTokenType());
                        SharedHelper.putKey(OTP.this, "access_token", GlobalData.token.getAccessToken());
                        System.out.println("login " + GlobalData.token.getTokenType() + " " + GlobalData.token.getAccessToken());
                        getProfile();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(OTP.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(OTP.this, "login Something wrong", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getProfile() {
        if (customDialog != null)
            customDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("device_type", "android");
        map.put("device_id", SharedHelper.getKey(this, "device_id"));
        map.put("device_token", SharedHelper.getKey(this, "device_token"));

        String header = SharedHelper.getKey(OTP.this, "token_type") + " " + SharedHelper.getKey(OTP.this, "access_token");
        System.out.println("getProfile Header " + header);
        Call<Profile> call = apiInterface.getProfile(header, map);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    GlobalData.profile = response.body();
                    SharedHelper.putKey(OTP.this, "logged_in", "1");
                    startActivity(new Intent(OTP.this, ShiftStatus.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(OTP.this, error.getError(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(OTP.this, "Something wrong getProfile", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }

    @OnClick(R.id.continue_btn)
    public void onViewClicked() {
        String pin_value = otp.getText().toString();
        if (pin_value.length() < 6) {
            Toast.makeText(OTP.this, "Please enter the valid OTP", Toast.LENGTH_LONG).show();
        } else {
            String mobile_number = SharedHelper.getKey(OTP.this, "mobile_number");
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", mobile_number);
            map.put("otp", pin_value);
            login(map);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                System.out.println("BroadcastReceiver" + message);
                otp.setText(message);
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    public void getDeviceToken() {
        String TAG = "FCM";
        try {
            if (!SharedHelper.getKey(this, "device_token").equals("") && SharedHelper.getKey(this, "device_token") != null) {
                String device_token = SharedHelper.getKey(this, "device_token");
                Log.d(TAG, "GCM Registration Token: " + device_token);
            } else {
                String device_token = FirebaseInstanceId.getInstance().getToken();
                SharedHelper.putKey(this, "device_token", "" + FirebaseInstanceId.getInstance().getToken());
                Log.d(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            Log.d(TAG, "COULD NOT GET FCM TOKEN");
        }

        try {
            String device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            SharedHelper.putKey(this, "device_id", device_UDID);
            Log.d(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "COULD NOT GET UDID");
        }
    }


}
