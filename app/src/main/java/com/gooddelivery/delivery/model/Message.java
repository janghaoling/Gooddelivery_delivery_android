package com.gooddelivery.delivery.model;

/**
 * Created by santhosh@appoets.com on 07-10-2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Message {
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message withMessage(String message) {
        this.message = message;
        return this;
    }
}
