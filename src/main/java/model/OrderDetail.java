/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author VinhNTCE181630
 */
public class OrderDetail {

    private int orderDetailsID;
    private int orderID;
    private int productID;
    private int quantity;
    private long price;
   

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailsID, int orderID, int productID, int quantity, long price) {
        this.orderDetailsID = orderDetailsID;
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderDetailsID() {
        return orderDetailsID;
    }

    public void setOrderDetailsID(int orderDetailsID) {
        this.orderDetailsID = orderDetailsID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

   
    
    
}
