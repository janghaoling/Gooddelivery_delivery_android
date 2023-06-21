package com.gooddelivery.delivery.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.gooddelivery.delivery.helper.LocaleUtils;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gooddelivery.delivery.Application;
import com.gooddelivery.delivery.api.APIError;
import com.gooddelivery.delivery.api.ErrorUtils;
import com.gooddelivery.delivery.helper.CustomDialog;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.gooddelivery.delivery.model.Shift;
import com.gooddelivery.delivery.model.Shiftbreaktime;
import com.gooddelivery.delivery.model.Vehicle;
import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.adapter.ShiftBreakAdapter;
import com.gooddelivery.delivery.service.GPSTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.gooddelivery.delivery.helper.GlobalData.color;

public class ShiftStatus extends AppCompatActivity {

    @BindView(R.id.shift_btn)
    Button shiftBtn;

    EditText vehicleNumber;
    Spinner vehicleNumberSpinner;
    CustomDialog customDialog;
    @BindView(R.id.message_layout)
    LinearLayout messageLayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.break_switch)
    Switch breakSwitch;
    @BindView(R.id.shift_break_rv)
    RecyclerView shiftBreakRv;

    ShiftBreakAdapter adapter;
    List<Shiftbreaktime> breaks;
    NumberFormat numberFormat;
    @BindView(R.id.owed_amount)
    TextView owedAmount;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0;
    GoogleApiClient googleApiClient;
    public static final int REQUEST_LOCATION = 1450;
    Button endShift;
    boolean isComesSplash = false;
    private Double totalPay = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_status);
        ButterKnife.bind(this);
        numberFormat = Application.getNumberFormat();
        customDialog = new CustomDialog(ShiftStatus.this);
        isComesSplash = getIntent().getBooleanExtra("is_splash", false);

        breaks = new ArrayList<>();
        adapter = new ShiftBreakAdapter(breaks, this);
        shiftBreakRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        shiftBreakRv.setItemAnimator(new DefaultItemAnimator());
        shiftBreakRv.setAdapter(adapter);

        getShift();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                startService(new Intent(this, GPSTracker.class));
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
            }
        } else {
            buildGoogleApiClient();
            startService(new Intent(this, GPSTracker.class));
        }

    }

    private void initView() {

        if (GlobalData.shift != null) {

            if (GlobalData.shift.getEndTime() != null) {
                shiftBtn.setVisibility(View.INVISIBLE);
                breakSwitch.setVisibility(View.INVISIBLE);
                popupEndShift();
            } else {
                GlobalData.isShiftOn = true;
                shiftBtn.setText(getResources().getString(R.string.end_shift));
                shiftBtn.setVisibility(View.VISIBLE);
                breakSwitch.setVisibility(View.VISIBLE);
            }
            owedAmount.setVisibility(View.VISIBLE);
            shiftBreakRv.setVisibility(View.VISIBLE);
            messageLayout.setVisibility(View.GONE);
            CharSequence amount = color(Color.BLACK, getResources().getString(R.string.you_owed), color(Color.parseColor("#ff6d00"), numberFormat.format(GlobalData.shift.getTotalAmountPay())));
            owedAmount.setText(amount);
            totalPay = GlobalData.shift.getTotalAmountPay();
            refreshBreaksRV();
            if (GlobalData.shift.getShiftbreaktimes() != null && GlobalData.shift.getShiftbreaktimes().size() > 0) {
                Shiftbreaktime lastBreakTime = GlobalData.shift.getShiftbreaktimes().get(GlobalData.shift.getShiftbreaktimes().size() - 1);
                if (lastBreakTime.getEndTime() == null) {
                    breakSwitch.setChecked(true);
                } else {
                    breakSwitch.setChecked(false);
                }
            } else if (GlobalData.shift.getShiftbreaktimes().size() == 0) {
                setSwitchClick();
            }
        }

    }

    private void initiatePopupWindow() {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShiftStatus.this);
            final FrameLayout frameView = new FrameLayout(ShiftStatus.this);
            builder.setView(frameView);

            final AlertDialog alertDialog = builder.create();
            LayoutInflater inflater = alertDialog.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.shift_popup, frameView);
            alertDialog.show();
            vehicleNumber = (EditText) dialogView.findViewById(R.id.vehicle_number);
            vehicleNumberSpinner = (Spinner) dialogView.findViewById(R.id.vehicle_number_spinner);
            getVehicleList();
            Button done = (Button) dialogView.findViewById(R.id.vehicle_done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    startShift();
                    //startActivity(new Intent(ShiftStatus.this, Home.class));
                }
            });
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getShift() {

        if (customDialog != null)
            customDialog.show();

        String header = SharedHelper.getKey(ShiftStatus.this, "token_type") + " " + SharedHelper.getKey(ShiftStatus.this, "access_token");
        Call<List<Shift>> call = GlobalData.api.getShift(header);
        call.enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(@NonNull Call<List<Shift>> call, @NonNull Response<List<Shift>> response) {
                Log.d("getShift() ", response.toString());
                customDialog.cancel();

                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        GlobalData.shift = response.body().get(0);
                        initView();
                    } else {
                        GlobalData.shift = null;
                        GlobalData.isShiftOn = false;

                        shiftBtn.setText(getResources().getString(R.string.start_shift));
                        shiftBtn.setVisibility(View.VISIBLE);
                        messageLayout.setVisibility(View.VISIBLE);
                        breakSwitch.setVisibility(View.GONE);
                        owedAmount.setVisibility(View.GONE);
                        shiftBreakRv.setVisibility(View.GONE);
                    }

                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(ShiftStatus.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Shift>> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(ShiftStatus.this, "Something wrong - getShift", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getVehicleList() {

        if (customDialog != null)
            customDialog.show();

        String header = SharedHelper.getKey(ShiftStatus.this, "token_type") + " " + SharedHelper.getKey(ShiftStatus.this, "access_token");
        Call<List<Vehicle>> call = GlobalData.api.getVehicles(header);
        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(@NonNull Call<List<Vehicle>> call, @NonNull Response<List<Vehicle>> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    List<Vehicle> vehicles = new ArrayList<Vehicle>();

                    Vehicle vehicle = new Vehicle();
                    vehicle.setId(null);
                    vehicle.setTransporterId(null);
                    vehicle.setVehicleNo(getResources().getString(R.string.select_vehicle));
                    vehicle.setDeletedAt(null);

                    vehicles.add(vehicle);
                    vehicles.addAll(response.body());

                    ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, vehicles);
                    adapter.setDropDownViewResource(R.layout.spinner_item);
                    vehicleNumberSpinner.setAdapter(adapter);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(ShiftStatus.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Vehicle>> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(ShiftStatus.this, "Something wrong - getVehicleList", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startShift() {

        String vehicle_no = "";

        if (!vehicleNumberSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.select_vehicle))) {
            vehicle_no = vehicleNumberSpinner.getSelectedItem().toString();
        }
        if (!vehicleNumber.getText().toString().isEmpty()) {
            vehicle_no = vehicleNumber.getText().toString();
        }

        if (vehicle_no.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.select_vehicle), Toast.LENGTH_LONG).show();
            return;
        }

        if (customDialog != null)
            customDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("vehicle_no", vehicle_no);

        String header = SharedHelper.getKey(ShiftStatus.this, "token_type") + " " + SharedHelper.getKey(ShiftStatus.this, "access_token");
        Call<List<Shift>> call = GlobalData.api.shiftStart(header, map);
        call.enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(@NonNull Call<List<Shift>> call, @NonNull Response<List<Shift>> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        GlobalData.shift = response.body().get(0);
                        startActivity(new Intent(ShiftStatus.this, Home.class));
                        finish();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(ShiftStatus.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Shift>> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(ShiftStatus.this, "Something wrong - startShift", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void endShift() {

        if (customDialog != null && GlobalData.shift != null)
            customDialog.show();

        String header = SharedHelper.getKey(ShiftStatus.this, "token_type") + " " + SharedHelper.getKey(ShiftStatus.this, "access_token");
        Call<List<Shift>> call = GlobalData.api.shiftEnd(header, GlobalData.shift.getId());
        call.enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(@NonNull Call<List<Shift>> call, @NonNull Response<List<Shift>> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        GlobalData.shift = response.body().get(0);
                        initView();
                    } else {
                        getShift();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(ShiftStatus.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Shift>> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(ShiftStatus.this, "Something wrong - endShift", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startBreakShift() {

        if (GlobalData.shift == null) {
            return;
        }

        customDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("vehicle_no", GlobalData.shift.getVehicle().getVehicleNo());

        String header = SharedHelper.getKey(ShiftStatus.this, "token_type") + " " + SharedHelper.getKey(ShiftStatus.this, "access_token");
        Call<List<Shift>> call = GlobalData.api.shiftBreakStart(header, map);
        call.enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(@NonNull Call<List<Shift>> call, @NonNull Response<List<Shift>> response) {
                customDialog.cancel();
                Log.i("startBreakShift", GlobalData.shift.toString());
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        GlobalData.shift = response.body().get(0);
                        refreshBreaksRV();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(ShiftStatus.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Shift>> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(ShiftStatus.this, "Something wrong - startBreakShift", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void endBreakShift() {

        if (GlobalData.shift == null || GlobalData.shift.getShiftbreaktimes().size() == 0) {
            return;
        }

        customDialog.show();

        Shiftbreaktime lastBreakTime = GlobalData.shift.getShiftbreaktimes().get(GlobalData.shift.getShiftbreaktimes().size() - 1);

        String header = SharedHelper.getKey(ShiftStatus.this, "token_type") + " " + SharedHelper.getKey(ShiftStatus.this, "access_token");
        Call<List<Shift>> call = GlobalData.api.shiftBreakEnd(header, lastBreakTime.getId());
        call.enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(@NonNull Call<List<Shift>> call, @NonNull Response<List<Shift>> response) {
                customDialog.cancel();
                Log.i("endBreakShift", GlobalData.shift.toString());
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        GlobalData.shift = response.body().get(0);
                        refreshBreaksRV();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(ShiftStatus.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Shift>> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(ShiftStatus.this, "Something wrong - endBreakShift", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void refreshBreaksRV() {
        breaks.clear();

        Shiftbreaktime shiftStart = new Shiftbreaktime();
        shiftStart.setId(null);
        shiftStart.setTransporterShiftId(GlobalData.shift.getId());
        shiftStart.setStartTime(GlobalData.shift.getStartTime());
        shiftStart.setEndTime(null);

        if (GlobalData.shift.getShiftbreaktimes().size() == 0)
            shiftStart.setOrderCount(GlobalData.shift.getTotalOrder());
        else
            shiftStart.setOrderCount(0);

        shiftStart.setDeletedAt(GlobalData.shift.getDeletedAt());
        breaks.add(shiftStart);

        breaks.addAll(GlobalData.shift.getShiftbreaktimes());

        if (GlobalData.shift.getEndTime() != null) {
            Shiftbreaktime shiftEnd = new Shiftbreaktime();
            shiftEnd.setId(null);
            shiftEnd.setTransporterShiftId(GlobalData.shift.getId());
            shiftEnd.setStartTime(null);
            shiftEnd.setEndTime(GlobalData.shift.getEndTime());

            if (GlobalData.shift.getShiftbreaktimes().size() == 0)
                shiftEnd.setOrderCount(GlobalData.shift.getTotalOrder());
            else
                shiftEnd.setOrderCount(0);

            shiftEnd.setDeletedAt(GlobalData.shift.getDeletedAt());
            breaks.add(shiftEnd);
        }

        adapter.notifyDataSetChanged();

        setSwitchClick();
    }

    private void popupEndShift() {

        if (GlobalData.shift != null) {


            System.out.println("popupEndShift");
            try {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

                final FrameLayout frameView = new FrameLayout(this);
                builder.setView(frameView);

                final android.app.AlertDialog alertDialog = builder.create();
                LayoutInflater inflater = alertDialog.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.amount_paid_popup, frameView);

                final TextView amountToBePaid = (TextView) dialogView.findViewById(R.id.amount_to_be_paid);
                if (GlobalData.shift.getTotalAmountPay() == null)
                    amountToBePaid.setText(numberFormat.format(totalPay));
                else
                    amountToBePaid.setText(numberFormat.format(GlobalData.shift.getTotalAmountPay()));
                endShift = (Button) dialogView.findViewById(R.id.end_shift);
                endShift.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        endShift();

                        alertDialog.cancel();
                    }
                });

                if (GlobalData.shift != null && GlobalData.shift.getEndTime() != null) {
                    endShift.setVisibility(View.GONE);
                } else {
                    endShift.setVisibility(View.VISIBLE);
                }

                alertDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setSwitchClick() {
        breakSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startBreakShift();
                } else {
                    endBreakShift();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.back, R.id.shift_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                startActivity(new Intent(this, Home.class));
                finish();
                break;
            case R.id.shift_btn:
                if (GlobalData.shift != null) {
                    popupEndShift();
                    //endShift();
                } else {
                    initiatePopupWindow();
                }
                break;
        }
    }

    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        googleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                    }
                }).build();
        googleApiClient.connect();

        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableLoc();
        }
    }

    private void enableLoc() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(ShiftStatus.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Request Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean FINE_LOCATIONPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean COARSE_LOCATIONPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (FINE_LOCATIONPermission && COARSE_LOCATIONPermission) {
                        startService(new Intent(this, GPSTracker.class));
                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to start service",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(ShiftStatus.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isComesSplash) {
            startActivity(new Intent(ShiftStatus.this, Home.class));
            finish();
        } else
            finish();
    }
}
