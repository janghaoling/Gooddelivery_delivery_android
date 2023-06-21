package com.gooddelivery.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by santhosh@appoets.com on 05-10-2017.
 */

public class Vehicles {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("transporter_id")
    @Expose
    private Integer transporterId;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Vehicles withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(Integer transporterId) {
        this.transporterId = transporterId;
    }

    public Vehicles withTransporterId(Integer transporterId) {
        this.transporterId = transporterId;
        return this;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Vehicles withVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
        return this;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Vehicles withDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }
}
