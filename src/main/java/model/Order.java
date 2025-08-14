/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Vinhntce181630
 */
public class Order {

    private int orderID;
    private int customerID;
    private int staffID;
    private long totalAmount;
    private String orderDate;
    private String deliveredDate;
    private int status;
    private int voucherID;
    private String addressSnapshot;
    private int addressID;
    private String updatedAt;

    public Order() {
    }

    public Order(int orderID, int customerID, int staffID, long totalAmount, String orderDate, String deliveredDate, int status, int voucherID, String addressSnapshot, int addressID, String updatedAt) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.staffID = staffID;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.deliveredDate = deliveredDate;
        this.status = status;
        this.voucherID = voucherID;
        this.addressSnapshot = addressSnapshot;
        this.addressID = addressID;
        this.updatedAt = updatedAt;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public String getAddressSnapshot() {
        return addressSnapshot;
    }

    public void setAddressSnapshot(String addressSnapshot) {
        this.addressSnapshot = addressSnapshot;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

   

}
