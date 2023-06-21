package com.gooddelivery.delivery.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gooddelivery.delivery.activities.Home;
import com.gooddelivery.delivery.activities.OrderDetail;
import com.gooddelivery.delivery.activities.ServiceFlow;
import com.gooddelivery.delivery.helper.CustomDialog;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.SharedHelper;
import com.gooddelivery.delivery.model.Message;
import com.gooddelivery.delivery.model.Order;
import com.gooddelivery.delivery.model.Shop;
import com.gooddelivery.delivery.R;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Order> list;
    private Context context;
    private boolean isNewTask;
    CustomDialog customDialog;
    CountDownTimer countDownTimer;
    boolean isRunning = false;

    public TaskAdapter(List<Order> list, Context con, boolean isNewTask) {
        this.list = list;
        this.context = con;
        this.isNewTask = isNewTask;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_new_task, parent, false);

        return new MyViewHolder(itemView);
    }

    public void remove(Order order) {
        int position = list.indexOf(order);
        if (position > -1) {
            list.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
            Home.errorLayout.setVisibility(View.VISIBLE);
        }
    }

    public void update(Order order) {
        int position = list.indexOf(order);
        Order order1 = list.get(position);
        order.setStatus("PROCESSING");
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Order obj = list.get(position);
        setOrderStatus(holder.orderStatus, obj);
        Shop shop = obj.getShop();
        holder.shopName.setText(shop.getName());
        holder.shopAddress.setText(shop.getAddress());
        Glide.with(context)
                .load(shop.getAvatar())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.cutlery_64)
                        .error(R.drawable.cutlery_64))
                .into(holder.shopAvatar);
        if (isNewTask && obj.getStatus().equalsIgnoreCase("SEARCHING")) {
            Home.errorLayout.setVisibility(View.GONE);
            holder.acceptRejectLayout.setVisibility(View.VISIBLE);
            holder.timeLeft.setVisibility(View.VISIBLE);
            if (obj.getResponseTime() != null && obj.getResponseTime() > 0) {
                Home.errorLayout.setVisibility(View.GONE);
                holder.timeLeft.setText(obj.getResponseTime().toString());
                int count = obj.getResponseTime() * 1000;
                if (countDownTimer != null&&isRunning) {
                    countDownTimer.cancel();
                }
                countDownTimer = new CountDownTimer(count, 1000) {
                    public void onTick(long millisUntilFinished) {
                        isRunning = true;
                        holder.timeLeft.setText(millisUntilFinished / 1000 + " secs left");
                    }
                    public void onFinish() {
                        isRunning = false;
                        remove(obj);
                    }
                }.start();

            }
        } else {
            holder.acceptRejectLayout.setVisibility(View.GONE);
            holder.timeLeft.setVisibility(View.GONE);
        }
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = list.get(position);
                accpetRequest(order);
            }
        });
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = list.get(position);
                rejectRequest(order);
            }
        });
    }

    private void accpetRequest(final Order order) {
        customDialog = new CustomDialog(context);
        customDialog.show();
        String header = SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token");
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "PROCESSING");
        map.put("order_id", order.getId().toString());
        map.put("request_status", "ACCEPT");
        Call<Order> call = GlobalData.api.acceptRequest(header, map);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                customDialog.dismiss();
                GlobalData.order = response.body();
                countDownTimer.cancel();
                update(order);
                context.startActivity(new Intent(context, ServiceFlow.class));
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
            }
        });
    }

    private void rejectRequest(final Order order) {
        customDialog = new CustomDialog(context);
        customDialog.show();
        String header = SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token");
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "PROCESSING");
        map.put("order_id", order.getId().toString());
        map.put("request_status", "REJECT");
        Call<Message> call = GlobalData.api.rejectRequest(header, map);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                customDialog.dismiss();
                countDownTimer.cancel();
                remove(order);

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setOrderStatus(TextView view, Order order) {
        String value = order.getStatus();
        switch (value) {
            case "ASSIGNED":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                view.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                view.setText(context.getResources().getString(R.string.new_order_request));
                break;
            case "COMPLETED":
                view.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
                view.setText(context.getResources().getString(R.string.deliver) + " #" + String.valueOf(order.getId()));
                break;
            case "CANCELLED":
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));
                view.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                view.setText(context.getResources().getString(R.string.cancelled) + " #" + String.valueOf(order.getId()));
                break;
            default:
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                view.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                view.setText(context.getResources().getString(R.string.order) + " #" + String.valueOf(order.getId()));
                break;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView taskCard;
        private TextView orderStatus, shopName, shopAddress, timeLeft;
        private ImageView shopAvatar;
        LinearLayout acceptRejectLayout;
        Button acceptBtn, rejectBtn;

        private MyViewHolder(View view) {
            super(view);
            taskCard = (CardView) view.findViewById(R.id.task_card);
            orderStatus = (TextView) view.findViewById(R.id.order_status);
            timeLeft = (TextView) view.findViewById(R.id.time_left);
            shopName = (TextView) view.findViewById(R.id.shop_name);
            shopAddress = (TextView) view.findViewById(R.id.shop_address);
            shopAvatar = (ImageView) view.findViewById(R.id.shop_avatar);
            acceptRejectLayout = (LinearLayout) view.findViewById(R.id.accept_reject_layout);
            acceptBtn = view.findViewById(R.id.accept_btn);
            rejectBtn = view.findViewById(R.id.reject_btn);
            taskCard.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == taskCard.getId()) {
                GlobalData.order = list.get(position);
                if (GlobalData.order.getStatus().equals("COMPLETED") || GlobalData.order.getStatus().equals("CANCELLED"))
                    context.startActivity(new Intent(context, OrderDetail.class));
                else
                    context.startActivity(new Intent(context, ServiceFlow.class));

            }
        }

    }


}
