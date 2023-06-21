package com.gooddelivery.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by santhosh@appoets.com on 14-11-2017.
 */

public class CartAddon {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_cart_id")
    @Expose
    private Integer userCartId;
    @SerializedName("addon_product_id")
    @Expose
    private Integer addonProductId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("addon_product")
    @Expose
    private AddonProduct addonProduct;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CartAddon withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserCartId() {
        return userCartId;
    }

    public void setUserCartId(Integer userCartId) {
        this.userCartId = userCartId;
    }

    public CartAddon withUserCartId(Integer userCartId) {
        this.userCartId = userCartId;
        return this;
    }

    public Integer getAddonProductId() {
        return addonProductId;
    }

    public void setAddonProductId(Integer addonProductId) {
        this.addonProductId = addonProductId;
    }

    public CartAddon withAddonProductId(Integer addonProductId) {
        this.addonProductId = addonProductId;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CartAddon withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public CartAddon withDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public AddonProduct getAddonProduct() {
        return addonProduct;
    }

    public void setAddonProduct(AddonProduct addonProduct) {
        this.addonProduct = addonProduct;
    }

    public CartAddon withAddonProduct(AddonProduct addonProduct) {
        this.addonProduct = addonProduct;
        return this;
    }
}
