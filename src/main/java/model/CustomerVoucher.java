/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author DONG QUOC
 */
public class CustomerVoucher {
    private int customerVoucherID;
    private int voucherID;
    private int customerID;

    public CustomerVoucher() {
    }

    public CustomerVoucher(int customerVoucherID, int voucherID, int customerID) {
        this.customerVoucherID = customerVoucherID;
        this.voucherID = voucherID;
        this.customerID = customerID;
    }

    public int getCustomerVoucherID() {
        return customerVoucherID;
    }

    public void setCustomerVoucherID(int customerVoucherID) {
        this.customerVoucherID = customerVoucherID;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    
}
