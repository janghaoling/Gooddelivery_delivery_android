package com.gooddelivery.delivery.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gooddelivery.delivery.Application;
import com.gooddelivery.delivery.model.CartAddon;
import com.gooddelivery.delivery.model.Item;
import com.gooddelivery.delivery.model.Product;
import com.gooddelivery.delivery.R;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-09-2017.
 */

public class ProductAdapter1 extends RecyclerView.Adapter<ProductAdapter1.MyViewHolder> {

    private List<Item> list;
    NumberFormat numberFormat;
    public ProductAdapter1(List<Item> list, Context con) {
        numberFormat = Application.getNumberFormat();
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_1_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = list.get(position);

        Product product = item.getProduct();
        holder.productName.setText(product.getName() + " x " + item.getQuantity());
        Integer priceAmount = item.getProduct().getPrices().getPrice()*item.getQuantity();
        if (list.get(position).getCartAddons() != null && !list.get(position).getCartAddons().isEmpty()) {
            for (int j = 0; j < list.get(position).getCartAddons().size(); j++) {
                priceAmount = priceAmount + (list.get(position).getQuantity() * (list.get(position).getCartAddons().get(j).getQuantity() *
                        list.get(position).getCartAddons().get(j).getAddonProduct().getPrice()));
            }
        }
        holder.productPrice.setText(numberFormat.format( priceAmount));

        if (item.getCartAddons() != null && !item.getCartAddons().isEmpty()) {
            List<CartAddon> cartAddonList = item.getCartAddons();
            for (int i = 0; i < cartAddonList.size(); i++) {
                if (i == 0)
                    holder.addons.setText(cartAddonList.get(i).getAddonProduct().getAddon().getName());
                else
                    holder.addons.append(", " + cartAddonList.get(i).getAddonProduct().getAddon().getName());
            }

            holder.addons.setVisibility(View.VISIBLE);
        } else {
            holder.addons.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView productName, productPrice,addons;

        private MyViewHolder(View view) {
            super(view);
            productName = (TextView) view.findViewById(R.id.product_name);
            productPrice = (TextView) view.findViewById(R.id.product_price);
            addons = (TextView) view.findViewById(R.id.addons);
            //productName.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == productName.getId()) {
                Toast.makeText(v.getContext(), "product PRESSED = " + list.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}
