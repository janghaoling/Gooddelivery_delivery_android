package com.gooddelivery.delivery.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.LocaleUtils;
import com.gooddelivery.delivery.model.DisputeManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dispute extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.call_us)
    Button callUs;
    @BindView(R.id.order_id)
    TextView orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.dispute));

        if (GlobalData.order != null && GlobalData.order.getDisputeManager() != null) {
            if (GlobalData.order.getDisputeManager().size() > 0) {
                DisputeManager disputeManager = GlobalData.order.getDisputeManager().get(0);
                orderId.setText(String.format(getString(R.string.order_message), GlobalData.order.getId()));
            }
        }
    }

    private void callPhone(String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if (phone != null && !phone.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else if (phone != null && !phone.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (GlobalData.order != null && GlobalData.order.getDisputeManager() != null) {
                        DisputeManager disputeManager = GlobalData.order.getDisputeManager().get(0);
                        callPhone(disputeManager.getPhone());
                    }
                } else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.call_us)
    public void onCallClicked() {
        if (GlobalData.order != null && GlobalData.order.getDisputeManager() != null) {
            if (GlobalData.order.getDisputeManager().size() > 0) {
                DisputeManager disputeManager = GlobalData.order.getDisputeManager().get(0);
                callPhone(disputeManager.getPhone());
            } else {
                callUs.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }
}
