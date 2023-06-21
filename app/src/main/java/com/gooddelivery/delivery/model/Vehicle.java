package com.gooddelivery.delivery.model;

/**
 * Created by santhosh@appoets.com on 28-09-2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vehicle {
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

    public Vehicle withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(Integer transporterId) {
        this.transporterId = transporterId;
    }

    public Vehicle withTransporterId(Integer transporterId) {
        this.transporterId = transporterId;
        return this;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Vehicle withVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
        return this;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Vehicle withDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    @Override
    public String toString() {
        return vehicleNo;
    }
}
