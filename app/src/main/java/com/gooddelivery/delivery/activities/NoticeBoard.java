package com.gooddelivery.delivery.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.gooddelivery.delivery.helper.LocaleUtils;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.adapter.NoticeAdapter;
import com.gooddelivery.delivery.api.APIError;
import com.gooddelivery.delivery.api.ErrorUtils;
import com.gooddelivery.delivery.helper.ConnectionHelper;
import com.gooddelivery.delivery.helper.CustomDialog;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.gooddelivery.delivery.model.Notice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NoticeBoard extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    CustomDialog customDialog;
    ConnectionHelper connectionHelper;
    @BindView(R.id.notice_rv)
    RecyclerView noticeRv;
    List<Notice> list;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.notice_board));
        customDialog = new CustomDialog(this);
        connectionHelper = new ConnectionHelper(this);

        list = new ArrayList<>();
        NoticeAdapter adapter = new NoticeAdapter(list, this);
        noticeRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        noticeRv.setItemAnimator(new DefaultItemAnimator());
        noticeRv.setAdapter(adapter);
        getNoticeBoard();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getNoticeBoard() {
        if (!connectionHelper.isConnectingToInternet()) {
            Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), getResources().getString(R.string.check_your_internet_connection), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

        customDialog.show();

        String header = SharedHelper.getKey(NoticeBoard.this, "token_type") + " " + SharedHelper.getKey(NoticeBoard.this, "access_token");
        Call<List<Notice>> call = GlobalData.api.getNoticeBoard(header);
        call.enqueue(new Callback<List<Notice>>() {
            @Override
            public void onResponse(@NonNull Call<List<Notice>> call, @NonNull Response<List<Notice>> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        list.clear();
                        list.addAll(response.body());
                        errorLayout.setVisibility(View.GONE);
                        noticeRv.getAdapter().notifyDataSetChanged();
                    } else {
                        list.clear();
                        noticeRv.getAdapter().notifyDataSetChanged();
                        errorLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(NoticeBoard.this, error.getError(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Notice>> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(NoticeBoard.this, "Something wrong getNoticeBoard", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        onBackPressed();
    }
}
