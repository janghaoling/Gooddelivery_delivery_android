package com.gooddelivery.delivery.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import com.gooddelivery.delivery.helper.LocaleUtils;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gooddelivery.delivery.api.APIError;
import com.gooddelivery.delivery.api.ErrorUtils;
import com.gooddelivery.delivery.helper.CustomDialog;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.gooddelivery.delivery.model.Profile;
import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.model.ProfileError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.user_id)
    EditText userId;
    @BindView(R.id.mobile_no)
    EditText mobileNo;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.update)
    Button update;
    CustomDialog customDialog;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.upload_profile)
    ImageView uploadProfile;
    private int PICK_IMAGE_REQUEST = 1;
    File imgFile;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0;
    String device_token, device_UDID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getDeviceToken();
        title.setText(getResources().getString(R.string.profile));
        customDialog = new CustomDialog(ProfileActivity.this);

        initView();
        getProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        if (GlobalData.profile != null) {
            name.setText(GlobalData.profile.getName());
            email.setText(GlobalData.profile.getEmail());
            mobileNo.setText(GlobalData.profile.getPhone());
            userId.setText(String.valueOf(GlobalData.profile.getId()));
            Glide.with(this)
                    .load(GlobalData.profile.getAvatar())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);
            SharedHelper.putKey(this, "currency_code", GlobalData.profile.getCurrencyCode());
        }
    }

    private void getProfile() {

        HashMap<String, String> map = new HashMap<>();
        map.put("device_type", "android");
        map.put("device_id", device_UDID);
        map.put("device_token", device_token);
        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");
        System.out.println("getProfile Header " + header);
        Call<Profile> call = GlobalData.api.getProfile(header, map);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if (response.isSuccessful()) {
                    GlobalData.profile = response.body();
                    initView();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(ProfileActivity.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, "Something wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateProfile() {

        if (name.getText().toString().isEmpty()){
            Toast.makeText(this, "The name field is required.", Toast.LENGTH_SHORT).show();
            return;
        } else if(email.getText().toString().isEmpty()) {
            Toast.makeText(this, "The email field is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customDialog != null)
            customDialog.show();

        /*HashMap<String, String> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("email", email.getText().toString());*/

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("name", RequestBody.create(MediaType.parse("text/plain"), name.getText().toString()));
        map.put("email", RequestBody.create(MediaType.parse("text/plain"), email.getText().toString()));
        MultipartBody.Part filePart = null;

        if (imgFile != null)
            filePart = MultipartBody.Part.createFormData("avatar", imgFile.getName(), RequestBody.create(MediaType.parse("image/*"), imgFile));

        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");
        Call<Profile> call = GlobalData.api.updateProfileWithImage(header, map, filePart);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    GlobalData.profile = response.body();
                    initView();
                    Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Gson gson = new GsonBuilder().create();
                    try {
                        ProfileError error = gson.fromJson(response.errorBody().string(),ProfileError.class);
                        System.out.println("error "+error.toString());
                        if (error.getName() != null){
                            Toast.makeText(ProfileActivity.this, error.getName().get(0), Toast.LENGTH_SHORT).show();
                        }else if(error.getEmail() != null){
                            Toast.makeText(ProfileActivity.this, error.getEmail().get(0), Toast.LENGTH_SHORT).show();
                        }else if(error.getAvatar() != null){
                            Toast.makeText(ProfileActivity.this, error.getAvatar().get(0), Toast.LENGTH_SHORT).show();
                            initView();
                        }
                    } catch (IOException e) {
                        // handle failure to read error
                        Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(ProfileActivity.this, "Something wrong - updateProfile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();

            Glide.with(this)
                    .load(imgDecodableString)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);
            imgFile = new File(imgDecodableString);
        } else if (resultCode == Activity.RESULT_CANCELED)
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
    }

    public void goToImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }

    @OnClick({R.id.back, R.id.upload_profile, R.id.update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.upload_profile:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        goToImageIntent();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    goToImageIntent();
                }
                break;
            case R.id.update:
                updateProfile();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean permission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permission2 = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (permission1 && permission2) {
                        goToImageIntent();
                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload Profile",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                                    }
                                }).show();
                    }
                }
                break;
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
            SharedHelper.putKey(this, "device_id", "" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }
}
