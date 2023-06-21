package com.gooddelivery.delivery.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gooddelivery.delivery.Application;
import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.adapter.ProductAdapter1;
import com.gooddelivery.delivery.helper.GlobalData;
import com.gooddelivery.delivery.helper.LocaleUtils;
import com.gooddelivery.delivery.model.Address;
import com.gooddelivery.delivery.model.Invoice;
import com.gooddelivery.delivery.model.Item;
import com.gooddelivery.delivery.model.Shop;
import com.gooddelivery.delivery.model.User;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderDetail extends AppCompatActivity {

    @BindView(R.id.order_id)
    TextView orderId;
    @BindView(R.id.order_created_at)
    TextView orderCreatedAt;
    @BindView(R.id.product_items_rv)
    RecyclerView productItemsRv;
    @BindView(R.id.item_total)
    TextView itemTotal;
    @BindView(R.id.service_tax)
    TextView serviceTax;
    @BindView(R.id.delivery_charges)
    TextView deliveryCharges;
    @BindView(R.id.total)
    TextView total;

    ProductAdapter1 productAdapter1;
    List<Item> items;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.shop_address)
    TextView shopAddress;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_address)
    TextView userAddress;
    @BindView(R.id.order_status_icon)
    ImageView orderStatusIcon;
    @BindView(R.id.order_status)
    TextView orderStatus;
    @BindView(R.id.no_of_items)
    TextView noOfItems;
    @BindView(R.id.bill_amount)
    TextView billAmount;
    @BindView(R.id.bill_paid)
    TextView billPaid;
    @BindView(R.id.returned)
    TextView returned;
    @BindView(R.id.payment_mode)
    TextView paymentMode;
    @BindView(R.id.discount)
    TextView discount;
    @BindView(R.id.user_address_type)
    ImageView userAddressType;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.status_message)
    TextView statusMessage;

    NumberFormat numberFormat;
    @BindView(R.id.wallet_detection)
    TextView walletDetection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        numberFormat = Application.getNumberFormat();
        items = new ArrayList<>();
        productAdapter1 = new ProductAdapter1(items, this);
        productItemsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        productItemsRv.setItemAnimator(new DefaultItemAnimator());
        productItemsRv.setAdapter(productAdapter1);

        initOrder();
    }

    private void initOrder() {
        if (GlobalData.order != null) {
            Invoice invoice = GlobalData.order.getInvoice();
            orderId.setText(getResources().getString(R.string.order_message, invoice.getOrderId()));
            orderCreatedAt.setText(GlobalData.getTimeFromString2(invoice.getCreatedAt()));

            itemTotal.setText(numberFormat.format(invoice.getGross()));
            serviceTax.setText(numberFormat.format(invoice.getTax()));
            deliveryCharges.setText(numberFormat.format(invoice.getDeliveryCharge()));
            discount.setText(numberFormat.format(invoice.getDiscount()));
            walletDetection.setText(numberFormat.format(invoice.getWalletAmount()));
            total.setText(numberFormat.format(invoice.getPayable()));

            if (invoice.getPaymentMode().equalsIgnoreCase("stripe")) {
                paymentMode.setText(getString(R.string.card));
            } else if (invoice.getPaymentMode().equalsIgnoreCase("cash")){
                paymentMode.setText(getString(R.string.cash));
            } else {
                paymentMode.setText(invoice.getPaymentMode());
            }

            if (invoice.getTotalPay() != 0)
                billPaid.setText(numberFormat.format(invoice.getTotalPay()));
            if (invoice.getTenderPay() != 0)
                returned.setText(numberFormat.format(invoice.getTenderPay()));

            billAmount.setText(numberFormat.format(invoice.getNet()));
            title.setText(getResources().getString(R.string.order_message, invoice.getOrderId()));
            noOfItems.setText(String.valueOf(GlobalData.order.getItems().size()));
            statusMessage.setText(this.getResources().getString(R.string.delivered) + " | " + GlobalData.order.getItems().size() + " items" + ", " + numberFormat.format(invoice.getPayable()));
            setOrderStatus(orderStatus, orderStatusIcon, GlobalData.order.getStatus());

            Shop shop = GlobalData.order.getShop();
            shopName.setText(shop.getName());
            shopAddress.setText(shop.getAddress());

            User user = GlobalData.order.getUser();
            userName.setText(user.getName());
            Address address = GlobalData.order.getAddress();
            userAddress.setText(address.getMapAddress());
            setIcon(userAddressType, address.getType());

            items.clear();
            items.addAll(GlobalData.order.getItems());
            productAdapter1.notifyDataSetChanged();
        }
    }

    private void setOrderStatus(TextView textView, ImageView imageView, String value) {
        switch (value) {
            case "COMPLETED":
                textView.setText(this.getResources().getString(R.string.order_delivered_successfully));
                imageView.setImageResource(R.drawable.verified);
                break;
            case "CANCELLED":
                textView.setText(this.getResources().getString(R.string.order_cancelled));
                textView.setTextColor(ContextCompat.getColor(OrderDetail.this, R.color.colorRed));
                imageView.setImageResource(R.drawable.order_cancelled);
                break;
            default:
                textView.setText("");
                break;
        }
    }

    private void setIcon(ImageView imgView, String value) {
        switch (value) {
            case "home":
                imgView.setImageResource(R.drawable.ic_home);
                break;
            case "work":
                imgView.setImageResource(R.drawable.ic_work);
                break;
            default:
                imgView.setImageResource(R.drawable.ic_pin);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }

}
