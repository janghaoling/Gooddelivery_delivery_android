package com.gooddelivery.delivery.model;

/**
 * Created by santhosh@appoets.com on 28-09-2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shiftbreaktime {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("transporter_shift_id")
    @Expose
    private Integer transporterShiftId;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("order_count")
    @Expose
    private Integer orderCount;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Shiftbreaktime withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getTransporterShiftId() {
        return transporterShiftId;
    }

    public void setTransporterShiftId(Integer transporterShiftId) {
        this.transporterShiftId = transporterShiftId;
    }

    public Shiftbreaktime withTransporterShiftId(Integer transporterShiftId) {
        this.transporterShiftId = transporterShiftId;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Shiftbreaktime withStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Shiftbreaktime withEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Shiftbreaktime withOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
        return this;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Shiftbreaktime withDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }
}
