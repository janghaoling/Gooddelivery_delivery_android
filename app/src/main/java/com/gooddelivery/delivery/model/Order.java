package com.gooddelivery.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 05-10-2017.
 */

public class Order {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("shift_id")
    @Expose
    private Integer shiftId;
    @SerializedName("user_address_id")
    @Expose
    private Integer userAddressId;
    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("transporter_id")
    @Expose
    private Integer transporterId;
    @SerializedName("transporter_vehicle_id")
    @Expose
    private Integer transporterVehicleId;
    @SerializedName("reason")
    @Expose
    private Object reason;
    @SerializedName("note")
    @Expose
    private Object note;
    @SerializedName("route_key")
    @Expose
    private String routeKey;
    @SerializedName("dispute")
    @Expose
    private String dispute;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("order_otp")
    @Expose
    private Integer orderOtp;
    @SerializedName("order_ready_time")
    @Expose
    private Integer orderReadyTime;
    @SerializedName("order_ready_status")
    @Expose
    private Integer orderReadyStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("schedule_status")
    @Expose
    private Integer scheduleStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("response_time")
    @Expose
    private Integer responseTime;
    @SerializedName("dispute_manager")
    @Expose
    private List<DisputeManager> disputeManager;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("transporter")
    @Expose
    private Transporter transporter;
    @SerializedName("vehicles")
    @Expose
    private Vehicles vehicles;
    @SerializedName("invoice")
    @Expose
    private Invoice invoice;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("ordertiming")
    @Expose
    private List<Ordertiming> ordertiming = null;
    @SerializedName("disputes")
    @Expose
    private List<Object> disputes = null;
    @SerializedName("reviewrating")
    @Expose
    private Object reviewrating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    public Integer getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(Integer userAddressId) {
        this.userAddressId = userAddressId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(Integer transporterId) {
        this.transporterId = transporterId;
    }

    public Integer getTransporterVehicleId() {
        return transporterVehicleId;
    }

    public void setTransporterVehicleId(Integer transporterVehicleId) {
        this.transporterVehicleId = transporterVehicleId;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public String getDispute() {
        return dispute;
    }

    public void setDispute(String dispute) {
        this.dispute = dispute;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getOrderOtp() {
        return orderOtp;
    }

    public void setOrderOtp(Integer orderOtp) {
        this.orderOtp = orderOtp;
    }

    public Integer getOrderReadyTime() {
        return orderReadyTime;
    }

    public void setOrderReadyTime(Integer orderReadyTime) {
        this.orderReadyTime = orderReadyTime;
    }

    public Integer getOrderReadyStatus() {
        return orderReadyStatus;
    }

    public void setOrderReadyStatus(Integer orderReadyStatus) {
        this.orderReadyStatus = orderReadyStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(Integer scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public List<DisputeManager> getDisputeManager() {
        return disputeManager;
    }

    public void setDisputeManager(List<DisputeManager> disputeManager) {
        this.disputeManager = disputeManager;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Transporter getTransporter() {
        return transporter;
    }

    public void setTransporter(Transporter transporter) {
        this.transporter = transporter;
    }

    public Vehicles getVehicles() {
        return vehicles;
    }

    public void setVehicles(Vehicles vehicles) {
        this.vehicles = vehicles;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Ordertiming> getOrdertiming() {
        return ordertiming;
    }

    public void setOrdertiming(List<Ordertiming> ordertiming) {
        this.ordertiming = ordertiming;
    }

    public List<Object> getDisputes() {
        return disputes;
    }

    public void setDisputes(List<Object> disputes) {
        this.disputes = disputes;
    }

    public Object getReviewrating() {
        return reviewrating;
    }

    public void setReviewrating(Object reviewrating) {
        this.reviewrating = reviewrating;
    }
}
