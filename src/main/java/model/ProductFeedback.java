/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.google.api.client.util.DateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * @author VinhNTCE181630
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
public class ProductFeedback {

    private int feedbackID;
    private int customerID;
    private int productID;
    private int orderID;
    private Date createdDate;
    private int star;
    private String comment;
    private boolean isActive;
    private boolean isRead;
    private String reply;
    private int staffID;
    private DateTime replyDate;
    
    public ProductFeedback() {
    }

    public ProductFeedback(int feedbackID, int customerID, int productID, int orderID, Date createdDate, int star, String comment, boolean isActive, boolean isRead, String reply, int staffID, DateTime replyDate) {
        this.feedbackID = feedbackID;
        this.customerID = customerID;
        this.productID = productID;
        this.orderID = orderID;
        this.createdDate = createdDate;
        this.star = star;
        this.comment = comment;
        this.isActive = isActive;
        this.isRead = isRead;
        this.reply = reply;
        this.staffID = staffID;
        this.replyDate = replyDate;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public DateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(DateTime replyDate) {
        this.replyDate = replyDate;
    }

    
    
   
}
