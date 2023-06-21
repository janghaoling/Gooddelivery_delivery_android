package com.gooddelivery.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamil on 9/25/2017.
 */

public class Profile {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Profile withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Profile withName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Profile withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Profile withAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Profile withLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Profile withLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Profile withOtp(String otp) {
        this.otp = otp;
        return this;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Profile withDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Profile withDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Profile withDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Profile withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Profile withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currency = currencyCode;
    }

    public Profile withCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Profile withPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
        return this;
    }
}