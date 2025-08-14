package model;

import java.util.Date;

public class CartItem {
    private int cartItemID;
    private int productID;
    private int quantity;
    private int customerID;
    // Default constructor
    public CartItem() {
    }

    public CartItem(int cartItemID, int productID, int quantity, int customerID) {
        this.cartItemID = cartItemID;
        this.productID = productID;
        this.quantity = quantity;
        this.customerID = customerID;
    }

    public int getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(int cartItemID) {
        this.cartItemID = cartItemID;
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

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }



  
}