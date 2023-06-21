package com.gooddelivery.delivery.model;

/**
 * Created by santhosh@appoets.com on 05-10-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoice {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("paid")
    @Expose
    private Integer paid;
    @SerializedName("gross")
    @Expose
    private Double gross;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("delivery_charge")
    @Expose
    private Double deliveryCharge;
    @SerializedName("wallet_amount")
    @Expose
    private Double walletAmount;
    @SerializedName("payable")
    @Expose
    private Double payable;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("net")
    @Expose
    private Double net;
    @SerializedName("total_pay")
    @Expose
    private Double totalPay;
    @SerializedName("tender_pay")
    @Expose
    private Double tenderPay;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Invoice withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Invoice withOrderId(Integer orderId) {
        this.orderId = orderId;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Invoice withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Invoice withPaid(Integer paid) {
        this.paid = paid;
        return this;
    }

    public Double getGross() {
        return gross;
    }

    public void setGross(Double gross) {
        this.gross = gross;
    }

    public Invoice withGross(Double gross) {
        this.gross = gross;
        return this;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Invoice withDiscount(Double discount) {
        this.discount = discount;
        return this;
    }

    public Double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(Double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Invoice withDeliveryCharge(Double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
        return this;
    }

    public Double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Double walletAmount) {
        this.walletAmount = walletAmount;
    }

    public Invoice withWalletAmount(Double walletAmount) {
        this.walletAmount = walletAmount;
        return this;
    }

    public Double getPayable() {
        return payable;
    }

    public void setPayable(Double payable) {
        this.payable = payable;
    }

    public Invoice withPayable(Double payable) {
        this.payable = payable;
        return this;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Invoice withTax(Double tax) {
        this.tax = tax;
        return this;
    }

    public Double getNet() {
        return net;
    }

    public void setNet(Double net) {
        this.net = net;
    }

    public Invoice withNet(Double net) {
        this.net = net;
        return this;
    }

    public Double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Double totalPay) {
        this.totalPay = totalPay;
    }

    public Invoice withTotalPay(Double totalPay) {
        this.totalPay = totalPay;
        return this;
    }

    public Double getTenderPay() {
        return tenderPay;
    }

    public void setTenderPay(Double tenderPay) {
        this.tenderPay = tenderPay;
    }

    public Invoice withTenderPay(Double tenderPay) {
        this.tenderPay = tenderPay;
        return this;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Invoice withPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Invoice withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Invoice withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

}
