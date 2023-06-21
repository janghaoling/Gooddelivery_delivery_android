package com.gooddelivery.delivery.model;

/**
 * Created by santhosh@appoets.com on 05-10-2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class User {
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
    private Object avatar;
    @SerializedName("device_token")
    @Expose
    private Object deviceToken;
    @SerializedName("device_id")
    @Expose
    private Object deviceId;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("login_by")
    @Expose
    private String loginBy;
    @SerializedName("social_unique_id")
    @Expose
    private Object socialUniqueId;
    @SerializedName("stripe_cust_id")
    @Expose
    private Object stripeCustId;
    @SerializedName("wallet_balance")
    @Expose
    private Integer walletBalance;
    @SerializedName("otp")
    @Expose
    private String otp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User withName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public User withAvatar(Object avatar) {
        this.avatar = avatar;
        return this;
    }

    public Object getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(Object deviceToken) {
        this.deviceToken = deviceToken;
    }

    public User withDeviceToken(Object deviceToken) {
        this.deviceToken = deviceToken;
        return this;
    }

    public Object getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    public User withDeviceId(Object deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public User withDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getLoginBy() {
        return loginBy;
    }

    public void setLoginBy(String loginBy) {
        this.loginBy = loginBy;
    }

    public User withLoginBy(String loginBy) {
        this.loginBy = loginBy;
        return this;
    }

    public Object getSocialUniqueId() {
        return socialUniqueId;
    }

    public void setSocialUniqueId(Object socialUniqueId) {
        this.socialUniqueId = socialUniqueId;
    }

    public User withSocialUniqueId(Object socialUniqueId) {
        this.socialUniqueId = socialUniqueId;
        return this;
    }

    public Object getStripeCustId() {
        return stripeCustId;
    }

    public void setStripeCustId(Object stripeCustId) {
        this.stripeCustId = stripeCustId;
    }

    public User withStripeCustId(Object stripeCustId) {
        this.stripeCustId = stripeCustId;
        return this;
    }

    public Integer getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Integer walletBalance) {
        this.walletBalance = walletBalance;
    }

    public User withWalletBalance(Integer walletBalance) {
        this.walletBalance = walletBalance;
        return this;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public User withOtp(String otp) {
        this.otp = otp;
        return this;
    }
}
