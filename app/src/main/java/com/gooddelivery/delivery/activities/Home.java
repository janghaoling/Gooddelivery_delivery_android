package com.gooddelivery.delivery.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.adapter.TaskAdapter;
import com.gooddelivery.delivery.api.APIError;
import com.gooddelivery.delivery.api.ErrorUtils;
import com.gooddelivery.delivery.helper.ConnectionHelper;
import com.gooddelivery.delivery.helper.CustomDialog;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.LocaleUtils;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.gooddelivery.delivery.model.Message;
import com.gooddelivery.delivery.model.Order;
import com.gooddelivery.delivery.model.Profile;
import com.gooddelivery.delivery.model.Shift;
import com.gooddelivery.delivery.receiver.NetworkChangeReceiver;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.gooddelivery.delivery.receiver.NetworkChangeReceiver.IS_NETWORK_AVAILABLE;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CustomDialog customDialog;
    @BindView(R.id.new_task_rv)
    RecyclerView newTaskRv;

    TaskAdapter newTaskAdapter;
    List<Order> orders;
    TaskAdapter completedTaskAdapter;
    List<Order> completedOrders;

    Handler handler;
    Runnable runnable;
    ConnectionHelper connectionHelper;
    public static LinearLayout errorLayout;
    @BindView(R.id.error_message)
    TextView errorMessage;
    @BindView(R.id.error_img)
    ImageView errorImg;
    @BindView(R.id.new_task_label)
    TextView newTaskLabel;
    @BindView(R.id.completed_task_label)
    TextView completedTaskLabel;
    @BindView(R.id.completed_task_rv)
    RecyclerView completedTaskRv;

    CircleImageView userAvatar;
    TextView name;
    TextView userId;
    @BindView(R.id.completed_task_rootview)
    LinearLayout completedTaskRootview;
    @BindView(R.id.internet_error_layout)
    LinearLayout internetErrorLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private SkeletonScreen skeletonScreen;
    String device_token, device_UDID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        getDeviceToken();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.live_task));
        customDialog = new CustomDialog(this);
        connectionHelper = new ConnectionHelper(this);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu, Home.this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home);
        userAvatar = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        LinearLayout nav_header = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_header);
        nav_header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ProfileActivity.class));
            }
        });
        name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
        userId = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_id);
        navigationView.setNavigationItemSelectedListener(this);

        orders = new ArrayList<>();
        newTaskAdapter = new TaskAdapter(orders, this, true);
        newTaskRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        newTaskRv.setItemAnimator(new DefaultItemAnimator());
        newTaskRv.setAdapter(newTaskAdapter);

        completedOrders = new ArrayList<>();
        completedTaskAdapter = new TaskAdapter(completedOrders, this, false);
        completedTaskRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //completedTaskRv.setItemAnimator(new DefaultItemAnimator());
        completedTaskRv.setAdapter(completedTaskAdapter);

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                getProfile();
                getOrder();
            }
        };

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value1 = extras.getString("Notification");
            if (value1 != null) {
                getProfile();
                getOrder();
            }
        }*/

        IntentFilter intentFilter = new IntentFilter(getApplication().getPackageName());
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "We are back !!!" : "Could not Connect to internet";
                if (!isNetworkAvailable) {
                    internetErrorLayout.setVisibility(View.VISIBLE);
                } else {
                    internetErrorLayout.setVisibility(View.GONE);
                    onResume();
                }
            }
        }, intentFilter);

    }

    @Override
    public void onResume() {
        super.onResume();
        initProfileView();
        orders.clear();
        newTaskAdapter.notifyDataSetChanged();
        completedOrders.clear();
        completedTaskAdapter.notifyDataSetChanged();
        if (GlobalData.shift != null) {
            getOrder();
            getCompletedOrder();
        } else getShift();
        getProfile();

        errorLayout.setVisibility(View.GONE);
        newTaskRv.setVisibility(View.VISIBLE);

        getNetworkStatus();
    }

    private boolean getNetworkStatus() {
        if (NetworkChangeReceiver.isConnectedToInternet(getApplicationContext())) {
            internetErrorLayout.animate().alpha(0.0f).setDuration(1000);
            internetErrorLayout.setVisibility(View.GONE);
            return true;
        } else {
            internetErrorLayout.animate().alpha(1.0f).setDuration(1000);
            internetErrorLayout.setVisibility(View.VISIBLE);
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void getShift() {

        if (!getNetworkStatus())
            return;

        String header = SharedHelper.getKey(Home.this, "token_type") + " " + SharedHelper.getKey(Home.this, "access_token");
        System.out.println(header);
        Call<List<Shift>> call = GlobalData.api.getShift(header);
        call.enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(@NonNull Call<List<Shift>> call, @NonNull Response<List<Shift>> response) {
                if (response.isSuccessful()) {
                    List<Shift> list = response.body();
                    if (list != null && list.size() > 0) {
                        GlobalData.shift = list.get(0);
                    } else {
                        /*startActivity(new Intent(Home.this, ShiftStatus.class));
                        finish();*/
                        errorImg.setImageResource(R.drawable.purchase);
                        errorMessage.setText(getString(R.string.please_start_shift));
                        errorLayout.setVisibility(View.VISIBLE);
                        handler.removeCallbacks(runnable);
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(Home.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Shift>> call, @NonNull Throwable t) {
                Toast.makeText(Home.this, "Something wrong - getShift", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getOrder() {

        if (!getNetworkStatus())
            return;

        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");
        Call<List<Order>> call = GlobalData.api.getOrder(header);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                Log.d("getOrder", response.toString());
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        orders.clear();
                        orders.addAll(response.body());
                        newTaskAdapter.notifyDataSetChanged();
                        handler.postDelayed(runnable, 5000);
//                        handler.removeCallbacks(runnable);
                        errorLayout.setVisibility(View.GONE);
                    } else {
                        orders.clear();
                        newTaskAdapter.notifyDataSetChanged();
//                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, 5000);
                        errorLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                Toast.makeText(Home.this, "Something wrong - getOrder", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProfile() {
        if (!getNetworkStatus()) {
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("device_type", "android");
        map.put("device_id", SharedHelper.getKey(this, "device_id"));
        map.put("device_token", SharedHelper.getKey(this, "device_token"));
        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");
        System.out.println("getProfile Header " + header);
        Call<Profile> call = GlobalData.api.getProfile(header, map);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if (response.isSuccessful()) {
                    GlobalData.profile = response.body();
                    setErrorLayout();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(Home.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Toast.makeText(Home.this, "Something wrong getProfile", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCompletedOrder() {

        if (skeletonScreen == null) {
            skeletonScreen = Skeleton.bind(completedTaskRootview)
                    .load(R.layout.skeloton_list_item_new_task)
                    .color(R.color.shimmer_color)
                    .angle(0)
                    .show();
        } else {
            skeletonScreen.hide();
            skeletonScreen.show();
        }

        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this, "access_token");
        Call<List<Order>> call = GlobalData.api.getCompletedOrder(header, "today");
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                Log.d("getCompletedOrder", response.toString());
                skeletonScreen.hide();
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        completedOrders.clear();
                        completedOrders.addAll(response.body());
                        completedTaskAdapter.notifyDataSetChanged();
                        completedTaskLabel.setText(getResources().getString(R.string.completed_tasks));
                        completedTaskLabel.setVisibility(View.VISIBLE);
                    } else {
                        completedOrders.clear();
                        completedTaskAdapter.notifyDataSetChanged();
                        completedTaskLabel.setVisibility(View.GONE);
                        completedTaskLabel.setText("");
                    }

                } else {
                    skeletonScreen.hide();
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(Home.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                skeletonScreen.hide();
                Toast.makeText(Home.this, "Something wrong - getCompletedOrder", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initProfileView() {
        if (GlobalData.profile != null) {
            name.setText(GlobalData.profile.getName());
            userId.setText(String.valueOf(GlobalData.profile.getId()));
            Glide.with(this)
                    .load(GlobalData.profile.getAvatar())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);
        }
    }

    private void setErrorLayout() {
        if (GlobalData.profile != null) {
            String value = GlobalData.profile.getStatus();
            System.out.println(value);
            switch (value) {
                case "online":
                    if (orders.size() <= 0) errorLayout.setVisibility(View.VISIBLE);
                    errorImg.setImageResource(R.drawable.hour_glass);
                    errorMessage.setText(getResources().getString(R.string.waiting_for_new_task));
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    break;
                case "unsettled":
                    errorImg.setImageResource(R.drawable.coins);
                    errorMessage.setText(getResources().getString(R.string.please_settle_the_amount_to_the_respective_restaurant));
                    errorLayout.setVisibility(View.VISIBLE);
                    navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                    break;
                case "offline":
                    errorImg.setImageResource(R.drawable.purchase);
                    errorMessage.setText(getResources().getString(R.string.turn_on_start_shift));
                    errorLayout.setVisibility(View.VISIBLE);
                    handler.removeCallbacks(runnable);
                    if (!GlobalData.isShiftOn)
                        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                    else
                        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(Home.this, ProfileActivity.class));
        } else if (id == R.id.nav_shift_status) {
            startActivity(new Intent(Home.this, ShiftStatus.class));
        } else if (id == R.id.nav_notice_board) {
            startActivity(new Intent(Home.this, NoticeBoard.class));
        } else if (id == R.id.nav_order_history) {
            startActivity(new Intent(Home.this, OrderHistory.class));
        } else if (id == R.id.nav_terms_conditions) {
            startActivity(new Intent(Home.this, TermsAndConditions.class));
        } else if (id == R.id.nav_language) {
            changeLanguage();
        } else if (id == R.id.nav_logout) {
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void changeLanguage() {

        List<String> languages = Arrays.asList(getResources().getStringArray(R.array.languages));
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.language_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(getResources().getString(R.string.change_language));
        final AlertDialog alert = alertDialog.create();

        final ListView lv = (ListView) convertView.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, languages);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String item = lv.getItemAtPosition(position).toString();
                setLanguage(item, position);
                alert.dismiss();
            }
        });
        alert.show();

    }

    public void getDeviceToken() {
        String TAG = "FCM";
        try {
            if (SharedHelper.getKey(this, "device_token").equals("") && SharedHelper.getKey(this, "device_token") == null) {
                SharedHelper.putKey(this, "device_token", FirebaseInstanceId.getInstance().getToken());
                Log.d(TAG, "Failed to complete token refresh: " + FirebaseInstanceId.getInstance().getToken());
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

    private void logout() {
        if (customDialog != null)
            customDialog.show();

        String header = SharedHelper.getKey(Home.this, "token_type") + " " + SharedHelper.getKey(Home.this, "access_token");
        Call<Message> call = GlobalData.api.logout(header);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(Home.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    SharedHelper.putKey(Home.this, "logged_in", "0");
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toast.makeText(Home.this, error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(Home.this, "logout Something wrong", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void setLanguage(String value, int pos) {
        SharedHelper.putKey(Home.this, "language", value);
        switch (pos) {
            case 0:
                LocaleUtils.setLocale(this, "en");
                break;
            case 1:
                LocaleUtils.setLocale(this, "es");
                break;
            default:
                LocaleUtils.setLocale(this, "es");
                break;
        }
        startActivity(new Intent(this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("change_language",true));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }

    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @OnClick(R.id.internet_error_layout)
    public void onViewClicked() {
        getNetworkStatus();
    }
}
