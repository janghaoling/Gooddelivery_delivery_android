package com.gooddelivery.delivery.model;

/**
 * Created by santhosh@appoets.com on 05-10-2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Prices {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("discount_type")
    @Expose
    private String discountType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Prices withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Prices withPrice(Integer price) {
        this.price = price;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Prices withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Prices withDiscount(Integer discount) {
        this.discount = discount;
        return this;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Prices withDiscountType(String discountType) {
        this.discountType = discountType;
        return this;
    }
}
