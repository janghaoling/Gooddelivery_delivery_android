package com.gooddelivery.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by santhosh@appoets.com on 14-11-2017.
 */

public class Addon {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Addon withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Addon withName(String name) {
        this.name = name;
        return this;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Addon withShopId(Integer shopId) {
        this.shopId = shopId;
        return this;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Addon withDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

}
