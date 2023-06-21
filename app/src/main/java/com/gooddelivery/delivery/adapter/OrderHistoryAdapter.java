package com.gooddelivery.delivery.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gooddelivery.delivery.Application;
import com.gooddelivery.delivery.activities.OrderDetail;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.model.Invoice;
import com.gooddelivery.delivery.model.Order;
import com.gooddelivery.delivery.model.Shop;
import com.gooddelivery.delivery.R;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-09-2017.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private List<Order> list;
    private Context context;
    NumberFormat numberFormat;
    public OrderHistoryAdapter(List<Order> list, Context con) {
        this.list = list;
        this.context = con;
        this.numberFormat = Application.getNumberFormat();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order obj = list.get(position);
        holder.orderId.setText("#"+obj.getId());
        holder.orderStatus.setText(obj.getStatus());
        Shop shop = obj.getShop();
        holder.shopName.setText(shop.getName());
        Glide.with(context)
                .load(shop.getAvatar())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.cutlery_64)
                        .error(R.drawable.cutlery_64))
                .into(holder.shopAvatar);
        Invoice invoice = obj.getInvoice();
        holder.orderDeliveryTime.setText(GlobalData.getTimeFromString(invoice.getCreatedAt()));
        holder.quantity.setText(String.valueOf(invoice.getQuantity())+ " items");
        holder.amount.setText(numberFormat.format(invoice.getNet()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView historyCard;
        private TextView orderStatus, shopName, orderId, orderDeliveryTime, quantity, amount;
        private ImageView shopAvatar;

        private MyViewHolder(View view) {
            super(view);
            historyCard = (CardView) view.findViewById(R.id.history_card);
            orderStatus = (TextView) view.findViewById(R.id.order_status);
            shopName = (TextView) view.findViewById(R.id.shop_name);
            orderId = (TextView) view.findViewById(R.id.order_id);
            orderDeliveryTime = (TextView) view.findViewById(R.id.order_delivery_time);
            quantity = (TextView) view.findViewById(R.id.quantity);
            amount = (TextView) view.findViewById(R.id.amount);
            shopAvatar = (ImageView) view.findViewById(R.id.shop_avatar);
            historyCard.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == historyCard.getId()) {
                GlobalData.order = list.get(position);
                context.startActivity(new Intent(context, OrderDetail.class));
                //Toast.makeText(v.getContext(), "CARD PRESSED = " + list.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}
