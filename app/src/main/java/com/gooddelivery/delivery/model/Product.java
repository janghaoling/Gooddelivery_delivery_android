package com.gooddelivery.delivery.model;

/**
 * Created by santhosh@appoets.com on 05-10-2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("food_type")
    @Expose
    private String foodType;
    @SerializedName("avalability")
    @Expose
    private Integer avalability;
    @SerializedName("max_quantity")
    @Expose
    private Integer maxQuantity;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("prices")
    @Expose
    private Prices prices;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Product withShopId(Integer shopId) {
        this.shopId = shopId;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product withName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product withDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Product withPosition(Integer position) {
        this.position = position;
        return this;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public Product withFoodType(String foodType) {
        this.foodType = foodType;
        return this;
    }

    public Integer getAvalability() {
        return avalability;
    }

    public void setAvalability(Integer avalability) {
        this.avalability = avalability;
    }

    public Product withAvalability(Integer avalability) {
        this.avalability = avalability;
        return this;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Product withMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product withStatus(String status) {
        this.status = status;
        return this;
    }

    public Prices getPrices() {
        return prices;
    }

    public void setPrices(Prices prices) {
        this.prices = prices;
    }

    public Product withPrices(Prices prices) {
        this.prices = prices;
        return this;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Product withImages(List<Image> images) {
        this.images = images;
        return this;
    }
}
