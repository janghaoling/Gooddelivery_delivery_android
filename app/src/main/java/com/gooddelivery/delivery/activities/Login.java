package com.gooddelivery.delivery.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gooddelivery.delivery.CountryPicker.Country;
import com.gooddelivery.delivery.CountryPicker.CountryPicker;
import com.gooddelivery.delivery.CountryPicker.CountryPickerListener;
import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.api.ApiClient;
import com.gooddelivery.delivery.api.ApiInterface;
import com.gooddelivery.delivery.helper.ConnectionHelper;
import com.gooddelivery.delivery.helper.CustomDialog;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.LocaleUtils;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.gooddelivery.delivery.model.Otp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity {

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0;
    CustomDialog customDialog;
    @BindView(R.id.country_image)
    ImageView countryImage;
    @BindView(R.id.country_number)
    TextView countryNumber;
    @BindView(R.id.mobile_no)
    EditText mobileNo;
    @BindView(R.id.submit)
    Button submit;
    String country_code;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    ConnectionHelper connectionHelper;
    private CountryPicker mCountryPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getDeviceToken();
        customDialog = new CustomDialog(Login.this);
        connectionHelper = new ConnectionHelper(this);
        if (SharedHelper.getKey(Login.this, "logged_in").equalsIgnoreCase("1")) {
            startActivity(new Intent(Login.this, ShiftStatus.class));
            finish();
        }
        mCountryPicker = CountryPicker.newInstance("Select Country");

        // You can limit the displayed countries
        List<Country> countryList = Country.getAllCountries();
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country s1, Country s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });
        mCountryPicker.setCountriesList(countryList);
        setListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        }

    }


    public void getOtpVerification(String mobile) {
        System.out.println(mobile);
        if (!connectionHelper.isConnectingToInternet()) {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        if (customDialog != null)
            customDialog.show();

        Call<Otp> call = apiInterface.getOtp(mobile);
        call.enqueue(new Callback<Otp>() {
            @Override
            public void onResponse(@NonNull Call<Otp> call, @NonNull Response<Otp> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    GlobalData.otp = response.body();
                    Toast.makeText(Login.this, "" + GlobalData.otp.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, OTP.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.has("phone"))
                            Toast.makeText(Login.this, jObjError.optString("phone"), Toast.LENGTH_LONG).show();
                        else if (jObjError.has("error")) {
                            startActivity(new Intent(Login.this, OTP.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            //Toast.makeText(Login.this, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(Login.this, "No Luck!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Otp> call, @NonNull Throwable t) {
                Toast.makeText(Login.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                customDialog.cancel();
            }
        });

    }


    private void setListener() {
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
//                mCountryNameTextView.setText(name);
//                mCountryIsoCodeTextView.setText(code);
                countryNumber.setText(dialCode);

                countryImage.setImageResource(flagDrawableResID);
                mCountryPicker.dismiss();
            }
        });
        countryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
        countryNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
        getUserCountryInfo();
    }

    private void getUserCountryInfo() {
        Locale current = getResources().getConfiguration().locale;
        Country country = Country.getCountryFromSIM(Login.this);
        if (country != null) {
            countryImage.setImageResource(country.getFlag());
            countryNumber.setText(country.getDialCode());
            country_code = country.getDialCode();
        } else {
            Country us = new Country("CL", "Chile", "+56", R.drawable.flag_cl);
            countryImage.setImageResource(us.getFlag());
            countryNumber.setText(us.getDialCode());
            country_code = us.getDialCode();
            //Toast.makeText(Login.this, "Required Sim", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }

    @OnClick({R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (!mobileNo.getText().toString().equalsIgnoreCase("")) {
                    SharedHelper.putKey(Login.this, "mobile_number", countryNumber.getText().toString() + mobileNo.getText().toString());
                    getOtpVerification(countryNumber.getText().toString() + mobileNo.getText().toString());
                } else {
                    Toast.makeText(Login.this, "Please enter your Mobile number", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                break;
        }
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
            String device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh");
        }

        try {
            String device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Device UDID:" + device_UDID);
            SharedHelper.putKey(this, "device_id", "" + device_UDID);
        } catch (Exception e) {
            String device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }
}
