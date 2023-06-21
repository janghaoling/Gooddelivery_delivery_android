package com.gooddelivery.delivery.model;

/**
 * Created by santhosh@appoets.com on 12-10-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notice {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("transporter_id")
    @Expose
    private Integer transporterId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("notice")
    @Expose
    private String notice;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Notice withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(Integer transporterId) {
        this.transporterId = transporterId;
    }

    public Notice withTransporterId(Integer transporterId) {
        this.transporterId = transporterId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Notice withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Notice withNotice(String notice) {
        this.notice = notice;
        return this;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Notice withNote(String note) {
        this.note = note;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Notice withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
