package com.gooddelivery.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 25-10-2017.
 */

public class ProfileError {
    @SerializedName("name")
    @Expose
    private List<String> name = null;
    @SerializedName("email")
    @Expose
    private List<String> email = null;
    @SerializedName("avatar")
    @Expose
    private List<String> avatar = null;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public ProfileError withName(List<String> name) {
        this.name = name;
        return this;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public ProfileError withEmail(List<String> email) {
        this.email = email;
        return this;
    }

    public List<String> getAvatar() {
        return avatar;
    }

    public void setAvatar(List<String> avatar) {
        this.avatar = avatar;
    }

    public ProfileError withAvatar(List<String> avatar) {
        this.avatar = avatar;
        return this;
    }
}
