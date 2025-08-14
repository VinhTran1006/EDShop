package model;

import java.time.LocalDateTime;

public class Suppliers {

    private int supplierID;
    private String taxID;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String contactPerson;   
    private String description;
    private boolean isActive;

    public Suppliers() {
    }

    public Suppliers(int supplierID, String taxID, String name, String email, String phoneNumber, String address, String contactPerson, String description, boolean isActive) {
        this.supplierID = supplierID;
        this.taxID = taxID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.contactPerson = contactPerson;
        this.description = description;
        this.isActive = isActive;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

  
}
