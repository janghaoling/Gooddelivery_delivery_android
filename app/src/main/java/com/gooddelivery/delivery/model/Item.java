package com.gooddelivery.delivery.model;

/**
 * Created by santhosh@appoets.com on 05-10-2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("promocode_id")
    @Expose
    private Integer promocodeId;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("savedforlater")
    @Expose
    private Integer savedforlater;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("cart_addons")
    @Expose
    private List<CartAddon> cartAddons = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Item withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Item withProductId(Integer productId) {
        this.productId = productId;
        return this;
    }

    public Integer getPromocodeId() {
        return promocodeId;
    }

    public void setPromocodeId(Integer promocodeId) {
        this.promocodeId = promocodeId;
    }

    public Item withPromocodeId(Integer promocodeId) {
        this.promocodeId = promocodeId;
        return this;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Item withOrderId(Integer orderId) {
        this.orderId = orderId;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Item withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Integer getSavedforlater() {
        return savedforlater;
    }

    public void setSavedforlater(Integer savedforlater) {
        this.savedforlater = savedforlater;
    }

    public Item withSavedforlater(Integer savedforlater) {
        this.savedforlater = savedforlater;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Item withProduct(Product product) {
        this.product = product;
        return this;
    }

    public List<CartAddon> getCartAddons() {
        return cartAddons;
    }

    public void setCartAddons(List<CartAddon> cartAddons) {
        this.cartAddons = cartAddons;
    }

    public Item withCartAddons(List<CartAddon> cartAddons) {
        this.cartAddons = cartAddons;
        return this;
    }
}
